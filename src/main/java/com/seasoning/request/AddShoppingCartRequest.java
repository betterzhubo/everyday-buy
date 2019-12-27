package com.seasoning.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@ApiModel
public class AddShoppingCartRequest {

    @ApiModelProperty(value = "商品唯一id")
    @Range(min=1, max=Integer.MAX_VALUE, message = "商品id错误")
    private int goodsId;

    @ApiModelProperty(value = "商品数量")
    @Range(min=1, max=Integer.MAX_VALUE, message = "商品数量错误")
    private int goodsCount;
}
