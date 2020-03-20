package com.seasoning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author better
 * @since 2020-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("region")
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "areaid", type = IdType.AUTO)
    private Integer areaid;

    /**
     * 父id
     */
    @TableField("parentid")
    private Integer parentid;

    /**
     * 简称
     */
    @TableField("shortname")
    private String shortname;

    /**
     * 名称
     */
    @TableField("areaname")
    private String areaname;

    /**
     * 全称
     */
    @TableField("merger_name")
    private String mergerName;

    /**
     * 层级 123 省市区县
     */
    @TableField("level")
    private Integer level;

    /**
     * 拼音
     */
    @TableField("pinyin")
    private String pinyin;

    /**
     * 长途区号
     */
    @TableField("code")
    private String code;

    /**
     * 邮编
     */
    @TableField("zip_code")
    private String zipCode;

    /**
     * 首字母
     */
    @TableField("first")
    private String first;

    /**
     * 经度
     */
    @TableField("lng")
    private String lng;

    /**
     * 纬度
     */
    @TableField("lat")
    private String lat;

    /**
     * 1：没删除，0：删除
     */
    @TableField("isdisplay")
    private Boolean isdisplay;

    /**
     * 推荐医院id
     */
    @TableField("hospitalid")
    private Integer hospitalid;

    /**
     * 0：非热门  1：热门
     */
    @TableField("ishot")
    private Integer ishot;

    /**
     * 原来表的id null代表没有
     */
    @TableField("oldid")
    private Integer oldid;


}
