package com.seasoning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
@TableName("`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.INPUT)
    private String id;

    @TableField("user_id")
    private Integer userId;

    @TableField("prepay_id")
    private String prepayId;

    @TableField("consignee_name")
    private String consigneeName;

    @TableField("consignee_mobile")
    private String consigneeMobile;

    /**
     * 0,已删除;1,待支付;2,待配送;3,配送中;4,已完成;5,已取消
     */
    @TableField("order_status")
    private Integer orderStatus;

    /**
     * SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（刷卡支付）
     * USERPAYING--用户支付中
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    @TableField("trade_state")
    private String tradeState;

    @TableField("consignee_address")
    private String consigneeAddress;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("create_time")
    private LocalDateTime createTime;


}
