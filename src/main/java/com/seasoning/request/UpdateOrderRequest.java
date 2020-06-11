package com.seasoning.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
@Data
@ApiModel
public class UpdateOrderRequest {

    @ApiModelProperty(value = "订单唯一id")
    private String id;

    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;

    @ApiModelProperty(value = "收货人电话")
    private String consigneeMobile;

    @ApiModelProperty(value = "收货人地址")
    private String consigneeAddress;

    @ApiModelProperty(value = "订单状态 0,已删除;1,待支付;2,待配送;3,配送中;4,已完成;5,已取消")
    private Integer orderStatus;


}
