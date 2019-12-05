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
 * @since 2019-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("shipping_address")
public class ShippingAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    /**
     * 收货人地区
     */
    @TableField("consignee_area")
    private String consigneeArea;

    /**
     * 收货人地址
     */
    @TableField("consignee_address")
    private String consigneeAddress;

    /**
     * 收货人
     */
    @TableField("consignee_name")
    private String consigneeName;

    /**
     * 收货人电话
     */
    @TableField("consignee_mobile")
    private String consigneeMobile;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("create_time")
    private LocalDateTime createTime;


}
