package com.seasoning.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Authot:better
 * @Date:2019/8/1 11:22
 */
@Data
@ApiModel
public class AddOrderByShoppingCartRequest {

    @ApiModelProperty(value = "收货人名字")
    @NotBlank(message = "参数不能为空")
    private String consigneeName;

    @ApiModelProperty(value = "收货人手机号")
    @NotBlank(message = "参数不能为空")
    private String consigneeMobile;

    @ApiModelProperty(value = "收货地址")
    @NotBlank(message = "参数不能为空")
    private String consigneeAddress;

    @ApiModelProperty(value = "订单商品(购物车内唯一id)")
    @Size(min=1, message="订单商品不可以为空！")
    private List<Integer> shoppingCartIds;

}
