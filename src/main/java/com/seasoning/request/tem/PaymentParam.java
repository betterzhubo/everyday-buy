package com.seasoning.request.tem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PaymentParam {
    @ApiModelProperty(value = "时间戳 秒")
    private String timeStamp;
    @ApiModelProperty(value = "随机字符串")
    private String nonceStr;
    @ApiModelProperty(value = "统一下单接口返回的 prepay_id 参数值")
    private String packageParam;
    @ApiModelProperty(value = "签名")
    private String paySign;
}
