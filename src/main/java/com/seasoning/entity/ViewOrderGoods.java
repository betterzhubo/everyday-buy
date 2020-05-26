package com.seasoning.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author better
 * @since 2020-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("view_order_goods")
public class ViewOrderGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("consignee_name")
    private String consigneeName;

    @TableField("consignee_mobile")
    private String consigneeMobile;

    @TableField("order_status")
    private Integer orderStatus;

    @TableField("consignee_address")
    private String consigneeAddress;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("goods_id")
    private Integer goodsId;

    @TableField("goods_name")
    private String goodsName;

    @TableField("goods_price")
    private BigDecimal goodsPrice;

    @TableField("goods_count")
    private Integer goodsCount;

    /**
     * 展示图片
     */
    @TableField("show_image")
    private String showImage;


}
