package com.seasoning.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class AddShippingAddressRequest {
	
	@ApiModelProperty(value = "收货地区id")
    @NotNull(message = "参数不能为空")
    private Integer areaId;

    @ApiModelProperty(value = "是否为默认地址")
    private Boolean isDefault;

    @ApiModelProperty(value = "收货人地区")
    @NotBlank(message = "参数不能为空")
    private String consigneeArea;

    @ApiModelProperty(value = "收货人名字")
    @NotBlank(message = "参数不能为空")
    private String consigneeName;

    @ApiModelProperty(value = "收货人手机号")
    @NotBlank(message = "参数不能为空")
    private String consigneeMobile;

    @ApiModelProperty(value = "收货地址")
    @NotBlank(message = "参数不能为空")
    private String consigneeAddress;
}
