package com.seasoning.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PayOrderRequest {

    @ApiModelProperty(value = "订单唯一id")
    private Integer orderId;
}
