//package com.demo.demobase.;
//
//import com.baomidou.mybatisplus.annotations.TableField;
//import com.baomidou.mybatisplus.annotations.TableId;
//import com.baomidou.mybatisplus.annotations.TableName;
//import java.io.Serializable;
//import lombok.Data;
//
///**
// * 界桩
// * @TableName tb_jz
// */
//@TableName(value ="tb_jz")
//@Data
//public class TbJz implements Serializable {
//    /**
//     *
//     */
//    @TableId
//    private Integer id;
//
//    /**
//     * 名称
//     */
//    private String name;
//
//    /**
//     * 坐标
//     */
//    private Integer shape;
//
//    @TableField(exist = false)
//    private static final long serialVersionUID = 1L;
//}