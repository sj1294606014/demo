package com.demo.demoweb;

import ch.qos.logback.core.util.FileUtil;
import com.demo.demoweb.controller.RoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoWebApplicationTests {

    @Test
    void contextLoads() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> m1 = new HashMap<>();
//        Polygon polygon = new Polygon();
//        int npoints = polygon.npoints;
//
//        polygon.contains()
        System.out.println(RoleEnum.valueOf("ROLE_ROOT_ADMIN").op());
    }


    public static void main(String[] args) throws Exception {
//        readShape();
//		readHeliuExcel();
//		readShuikuExcel();
    }


//    public static void readShape() throws IOException {
//        String shp = "E:\\文件\\河湖划界GCS2000标准结果\\冻江数据库(7)(5)\\数据库\\范围管理线.shp";
//        List<Map<String, Object>> items = ShapeTools.readToArray(new File(shp));
//        StringBuffer buffer = new StringBuffer("");
//        int total = 0;
//        String name ="冻江";
//        Map<String, Integer> nameMap = new HashMap<>();
//        for (Map<String, Object> item : items) {
//            CommonUtil.keyToLowerCase(item);
//            String oname = (String) item.get("o_name");
//            String ocomment = (String) item.get("o_comment");
//            String shape = (String) item.get("shape");
//            if (shape == null) {
//                continue;
//            }
//            oname = oname == null ? "''" : "'" + oname + "'";
//            ocomment = ocomment == null ? "''" : "'" + ocomment + "'";
//            StringBuffer sql = new StringBuffer("set @oname = '" + shape + "';\r\n insert into sl_gis_skxxb(name,shape) values(");
//            sql.append(name);
//            sql.append(",ST_GeomFromText(");
//            sql.append("@oname");
//            sql.append(")");
//            buffer.append(sql).append(");\r\n");
//
//            if (nameMap.containsKey(oname)) {
//                System.out.println(oname);
//            }
//            nameMap.put(oname, 1);
//        }
////        FileUtil.writeBytes(buffer.toString().getBytes(StandardCharsets.UTF_8), new File(new File(shp).getAbsolutePath().replace(".shp", ".sql")));
//    }
}
