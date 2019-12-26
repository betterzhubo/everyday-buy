package com.seasoning.entity;

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

    @TableId(value = "id")
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("consignee_name")
    private String consigneeName;

    @TableField("consignee_mobile")
    private String consigneeMobile;

    /**
     * 0,已删除;1,待支付;2,待配送;3,配送中;4,已完成;5,已取消
     */
    @TableField("order_status")
    private Integer orderStatus;

    @TableField("consignee_address")
    private String consigneeAddress;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("create_time")
    private LocalDateTime createTime;


}
