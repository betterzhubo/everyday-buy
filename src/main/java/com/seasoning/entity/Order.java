package com.seasoning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("order_id")
    private String orderId;

    @TableField("user_id")
    private Integer userId;

    @TableField("take_delivery_name")
    private String takeDeliveryName;

    @TableField("take_delivery_mobiles")
    private String takeDeliveryMobiles;

    /**
     * 1,待支付;2,待配送;3,配送中;4,已完成;5,已取消
     */
    @TableField("order_type")
    private Integer orderType;

    @TableField("delivery_address")
    private String deliveryAddress;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("create_time")
    private LocalDateTime createTime;


}
