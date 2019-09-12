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
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_goods")
public class OrderGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableId("order_id")
    private String orderId;

    @TableField("goods_id")
    private String goodsId;

    @TableField("goods_name")
    private String goodsName;

    @TableField("goods_count")
    private Integer goodsCount;

    @TableField("goods_price")
    private BigDecimal goodsPrice;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("create_time")
    private LocalDateTime createTime;


}
