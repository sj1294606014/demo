package com.demo.demojava.backup;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自动备份任务
 */
@Log4j2
@Configuration
@EnableScheduling
public class BackupTask {

    @Resource
    private BackupTaskConfig backupTaskConfig;

    /**
     * 工程启动执行一次, 判断是否需要创建备份的定时任务
     */
    @PostConstruct
    private void generateBackupTask() {
        // 检查备份配置
        checkConfig();
        if (!BackupTaskConfig.getEnabled()) {
            return;
        }
        log.info("====创建备份定时任务====");
        // 创建备份定时任务
        CronUtil.schedule(backupTaskConfig.getTaskCron(), new Task() {
            @Override
            public void execute() {
                backup();
            }
        });

        CronUtil.setMatchSecond(true);
        CronUtil.start();
        log.info("====备份定时任务启动成功====");
    }

    /**
     * 检查备份配置
     */
    private void checkConfig() {
        log.info("=======================================检查备份配置开始=======================================");
        backupTaskConfig.readProperties();

        if (!BackupTaskConfig.getEnabled()) {
            log.info("未开启备份");
            return;
        }

        String dir = BackupTaskConfig.getLocalDir();
        File localRoot = new File(dir);
        if (!localRoot.isDirectory()) {
            log.info("本地备份目录不是有效目录");
        }
        if (!localRoot.exists()) {
            localRoot.mkdirs();
        }

        if (StrUtil.isEmpty(BackupTaskConfig.getDatabaseScript())) {
            BackupTaskConfig.setDatabaseScript(generateMysqlBackupScript());
        }

        String ftpIp = BackupTaskConfig.getFtpHost();
        if (StrUtil.isEmpty(ftpIp)) {
            log.info("ftp host配置缺失,不备份到ftp");
            BackupTaskConfig.setBackup2ftp(false);
        }

        Integer ftpPort = BackupTaskConfig.getFtpPort();
        if (ftpPort == null) {
            log.info("ftp port配置缺失,不备份到ftp");
            BackupTaskConfig.setBackup2ftp(false);
        }

        try {
            Ftp ftp = new Ftp(ftpIp, ftpPort, BackupTaskConfig.getFtpUsername(), BackupTaskConfig.getFtpPassword());
            boolean ftpDirExist = ftp.exist(BackupTaskConfig.getFtpDir());
            if (!ftpDirExist) {
                ftp.mkDirs(BackupTaskConfig.getFtpDir());
            }
            ftp.close();
            BackupTaskConfig.setBackup2ftp(true);
        } catch (Exception e) {
            log.info("连接到FTP失败,不备份到ftp", e);
        }

        log.info("工程名称:{}", getProjectName());

        log.info("=======================================检查备份配置结束=======================================");
    }

    /**
     * 备份
     */
    private void backup() {
        log.info("=======================================备份开始=======================================");
        long start = System.currentTimeMillis();

        String database = backupDatabase();
        if (database == null) {
            return;
        }
        String deployPackage = backupDeployPackage();
        String zip = compressBackupFiles(database, deployPackage);
        upload2ftp(zip);
        clearHistory();
        long end = System.currentTimeMillis();
        log.info("备份耗时:{}秒", (end - start) / 1000);
        log.info("=======================================备份结束=======================================");
    }

    /**
     * 备份数据库
     */
    private String backupDatabase() {
        String cmd = backupTaskConfig.getDatabaseScript();
        if (StrUtil.isEmpty(cmd)) {
            return null;
        }
        String tmpdir = System.getProperty("java.io.tmpdir") + getProjectName() + "_" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + ".sql";
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        try {
            Process exec = Runtime.getRuntime().exec(cmd);
            inputStreamReader = new InputStreamReader(exec.getInputStream(), "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tmpdir), "utf8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                printWriter.println(line);
            }
            printWriter.flush();
            if (exec.waitFor() == 0) {
                return tmpdir;
            }
            log.info("备份数据库成功{}", tmpdir);
            return tmpdir;
        } catch (IOException e) {
            log.error("备份数据库出错", e);
            return null;
        } catch (InterruptedException e) {
            log.error("备份数据库出错", e);
            return null;
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("【数据库备份】流关闭失败！");
            }


        }
    }

    /**
     * 备份部署包
     */
    private String backupDeployPackage() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if (!path.contains("jar")) {
            return null;
        }
        path = path.substring(0, path.indexOf("jar") + 3);
        String tmpdir = System.getProperty("java.io.tmpdir") + getProjectName() + "_" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + ".jar";
        FileUtil.copy(path, tmpdir, true);
        log.info("备份部署包成功{}", tmpdir);
        return tmpdir;
    }

    /**
     * 压缩备份文件
     *
     * @return
     */
    private String compressBackupFiles(String databaseFilePath, String packageFilePath) {
        String dir = BackupTaskConfig.getLocalDir();
        File database = new File(StrUtil.emptyToDefault(databaseFilePath, ""));
        File packageFile = new File(StrUtil.emptyToDefault(packageFilePath, ""));

        String zipName = dir + File.separator + getProjectName() + "_" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + ".zip";

        List<File> files = new LinkedList<>();
        if (database.exists()) {
            files.add(database);
        }
        if (packageFile.exists()) {
            files.add(packageFile);
        }
        if (files.isEmpty()) {
            return null;
        }
        File[] fileArr = new File[files.size()];
        for (int i = 0; i < files.size(); i++) {
            fileArr[i] = files.get(i);
        }
        ZipUtil.zip(FileUtil.file(zipName), false, fileArr);

        if (database.exists()) {
            database.delete();
        }
        if (packageFile.exists()) {
            packageFile.delete();
        }
        log.info("备份文件压缩成功{}", zipName);
        return zipName;
    }

    private String getProjectName() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if (!path.contains("jar")) {
            return null;
        }
        path = path.substring(0, path.indexOf(".jar") + 4);
        File file = FileUtil.file(path);
        String projectName = file.getName();
        projectName = projectName.replace(".jar", "");
        return projectName;
    }

    /**
     * 备份文件上传至FTP
     */
    private boolean upload2ftp(String backupFile) {
        if (!BackupTaskConfig.getBackup2ftp()) {
            return true;
        }
        if (backupFile == null) {
            return false;
        }
        try {
            Ftp ftp = new Ftp(BackupTaskConfig.getFtpHost(), BackupTaskConfig.getFtpPort(), BackupTaskConfig.getFtpUsername(), BackupTaskConfig.getFtpPassword());
            ftp.setMode(FtpMode.Passive);
            ftp.cd(BackupTaskConfig.getFtpDir());
            boolean uploaded = ftp.upload(BackupTaskConfig.getFtpDir(), FileUtil.file(backupFile));
            if (!uploaded) {
                ftp.setMode(FtpMode.Active);
                uploaded = ftp.upload(BackupTaskConfig.getFtpDir(), FileUtil.file(backupFile));
            }
            ftp.close();
            log.info("备份文件上传到FTP成功");
            if (!uploaded) {
                log.error("备份文件上传到FTP失败");
            }
        } catch (Exception e) {
            log.info("连接到FTP失败,不备份到ftp", e);
        }
        return true;
    }

    /**
     * 生成mysql备份脚本
     */
    private String generateMysqlBackupScript() {
        String driver = backupTaskConfig.getEnv().getProperty("spring.datasource.driver-class-name");
        // 暂只考虑mysql
        if (!StrUtil.contains(driver, "com.mysql")) {
            return null;
        }

        String url = backupTaskConfig.getEnv().getProperty("spring.datasource.url");
        url = url == null ? "" : url;
        url = url.replaceAll("/", ":");
        url = url.replaceAll("\\?", ":");
        Pattern pattern = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}){1}(:\\d+:){0,1}((.+:)|(.+))");
        Matcher m = pattern.matcher(url);
        if (m.find()) {
            url = m.group();
        } else {
            return null;
        }
        String[] array = url.split(":");
        if (array.length < 2) {
            log.info("生成mysql备份脚本失败");
            return null;
        }
        String host = array[0];
        String port = array.length == 2 ? "80" : array[1];
        String database = array.length == 3 ? array[2] : array[1];
        String username = backupTaskConfig.getEnv().getProperty("spring.datasource.username");
        String password = backupTaskConfig.getEnv().getProperty("spring.datasource.password");

        String cmd = "mysqldump -h" + host + " -P" + port + " -u" + username + " -p" + password + " " + database;
        return cmd;
    }

    /**
     * 清理超期备份文件
     */

    private void clearHistory() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ");
        Integer day = BackupTaskConfig.getRetentionPeriod();
        DateTime time = DateUtil.offset(DateUtil.date(), DateField.DAY_OF_MONTH, -day);
        File root = FileUtil.file(BackupTaskConfig.getLocalDir());
        File[] files = root.listFiles();
        for (int i = files.length - 1; i >= 0; i--) {
            try {
                FileTime fileTime = Files.readAttributes(Paths.get(files[i].getPath()), BasicFileAttributes.class).creationTime();
                if (time.getTime() > fileTime.toMillis()) {
                    log.info("清理过期备份文件{}", files[i].getAbsolutePath());
                    files[i].delete();
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

}
