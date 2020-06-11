package com.seasoning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ShopingCartGoods implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("goods_id")
    private String goodsId;

    @TableField("goods_count")
    private Integer goodsCount;

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
     * 是否销售中
     */
    @TableField("selling")
    private Boolean selling;

    /**
     * 是否销售中
     */
    @TableField("is_hot")
    private Boolean isHot;
}
