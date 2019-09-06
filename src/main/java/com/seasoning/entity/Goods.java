package com.seasoning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author better
 * @since 2019-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("goods_name")
    private String goodsName;

    /**
     * 0:餐饮,1:自用
     */
    @TableField("purpose")
    private Integer purpose;

    @TableField("price")
    private BigDecimal price;

    @TableField("goods_type_id")
    private Integer goodsTypeId;

    /**
     * 展示图片
     */
    @TableField("show_image")
    private String showImage;

    /**
     * 详情内图片
     */
    @TableField("detail_image")
    private String detailImage;

    /**
     * 是否销售中
     */
    @TableField("selling")
    private Boolean selling;


}
