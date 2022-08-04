package com.demo.demojava.backup;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * 备份配置
 */
@Data
@Log4j2
@Component
public class BackupTaskConfig {

    @Resource
    private Environment env;

    /**
     * 是否启用备份
     */
    private static Boolean enabled = false;

    /**
     * 本地备份目录
     */
    private static String localDir;

    /**
     * 备份至FTP
     */
    private static Boolean backup2ftp = false;

    /**
     * ftp ip地址
     */
    private static String ftpHost;

    /**
     * ftp备份目录
     */
    private static String ftpDir;

    /**
     * ftp用户名
     */
    private static String ftpUsername;

    /**
     * ftp密码
     */
    private static String ftpPassword;

    /**
     * ftp端口
     */
    private static Integer ftpPort = 21;

    /**
     * 数据库备份脚本
     */
    private static String databaseScript;

    /**
     * 保留天数
     */
    private static Integer retentionPeriod = 30;

    /**
     * 定时任务规则
     */
    private static String taskCron = "5 * * * * *";

    /**
     * 读取配置
     */
    public void readProperties() {
        log.info("====开始读取备份配置文件====");
        try {
            enabled = Boolean.parseBoolean(env.getProperty("backup.enabled"));
        } catch (Exception e) {
            log.error("读取enabled出错", e);
        }

        try {
            localDir = env.getProperty("backup.localdir");
        } catch (Exception e) {
            log.error("读取localDir出错", e);
        }


        try {
            ftpHost = env.getProperty("backup.ftp.ip");
        } catch (Exception e) {
            log.error("读取ftpHost出错", e);
        }

        try {
            ftpDir = env.getProperty("backup.ftp.dir");
        } catch (Exception e) {
            log.error("读取ftpDir出错", e);
        }
        ftpDir = StrUtil.isEmpty(ftpDir) ? File.separator : ftpDir;

        try {
            ftpUsername = env.getProperty("backup.ftp.username");
        } catch (Exception e) {
            log.error("读取ftpUsername出错", e);
        }

        try {
            ftpPassword = env.getProperty("backup.ftp.password");
        } catch (Exception e) {
            log.error("读取ftpPassword出错", e);
        }

        try {
            ftpPort = Integer.parseInt(env.getProperty("backup.ftp.port"));
        } catch (Exception e) {
            log.error("读取ftpPort出错", e);
        }

        try {
            databaseScript = env.getProperty("backup.databaseScript");

        } catch (Exception e) {
            log.error("读取databaseScript出错", e);
        }

        try {
            retentionPeriod = Integer.parseInt(env.getProperty("backup.retentionPeriod"));
        } catch (Exception e) {
            log.error("读取retentionPeriod出错", e);
        }

        try {
            taskCron = env.getProperty("backup.cron");
        } catch (Exception e) {
            log.error("读取taskCron出错", e);
        }

        log.info("enabled:{}", enabled);
        log.info("localDir:{}", localDir);
        log.info("ftpHost:{}", ftpHost);
        log.info("ftpDir:{}", ftpDir);
        log.info("ftpUsername:{}", ftpUsername);
        log.info("ftpPort:{}", ftpPort);
        log.info("databaseScript:{}", databaseScript);
        log.info("retentionPeriod:{}", retentionPeriod);
        log.info("taskCron:{}", taskCron);
        log.info("====读取备份配置文件结束====");
    }

    public static boolean getEnabled() {
        return enabled;
    }

    public static String getLocalDir() {
        return localDir;
    }

    public static String getFtpHost() {
        return ftpHost;
    }

    public static String getFtpDir() {
        return ftpDir;
    }

    public static String getFtpUsername() {
        return ftpUsername;
    }

    public static String getFtpPassword() {
        return ftpPassword;
    }

    public static Integer getFtpPort() {
        return ftpPort;
    }

    public static String getDatabaseScript() {
        return databaseScript;
    }

    public static void setDatabaseScript(String databaseScript) {
        BackupTaskConfig.databaseScript = databaseScript;
    }

    public static Integer getRetentionPeriod() {
        return retentionPeriod;
    }

    public static String getTaskCron() {
        return taskCron;
    }

    public static boolean getBackup2ftp() {
        return backup2ftp;
    }

    public static void setBackup2ftp(Boolean backup2ftp) {
        BackupTaskConfig.backup2ftp = backup2ftp;
    }
}
