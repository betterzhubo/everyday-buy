package com.seasoning.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class UpdateShippingAddressRequest {
	
	@ApiModelProperty(value = "收货信息唯一id")
    private int id;
	
	@ApiModelProperty(value = "收货地区id")
    private Integer areaId;

    @ApiModelProperty(value = "收货人地区")
    private String consigneeArea;

    @ApiModelProperty(value = "收货人名字")
    private String consigneeName;

    @ApiModelProperty(value = "收货人手机号")
    private String consigneeMobile;

    @ApiModelProperty(value = "收货地址")
    private String consigneeAddress;
}
