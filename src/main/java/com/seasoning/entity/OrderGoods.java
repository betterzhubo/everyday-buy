package com.seasoning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @since 2019-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_goods")
public class OrderGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("order_id")
    private String orderId;

    @TableField("goods_id")
    private String goodsId;

    @TableField("goods_name")
    private String goodsName;

    @TableField("goods_number")
    private Integer goodsNumber;

    @TableField("goods_price")
    private Double goodsPrice;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("create_time")
    private LocalDateTime createTime;


}
