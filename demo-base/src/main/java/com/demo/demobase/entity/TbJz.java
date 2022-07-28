//package com.demo.demobase.entity;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
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
//    @TableId(value = "id", type = IdType.AUTO)
//    private Integer id;
//
//    /**
//     * 名称
//     */
//    @TableField(value = "name")
//    private String name;
//
//    /**
//     * 坐标
//     */
//    @TableField(value = "shape")
//    private Integer shape;
//
//    @TableField(exist = false)
//    private static final long serialVersionUID = 1L;
//}