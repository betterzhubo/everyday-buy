package com.seasoning.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Authot:better
 * @Date:2019/8/1 11:22
 */
@Data
@ApiModel
public class AddOrderRequest {

    @ApiModelProperty(value = "收货人名字")
    @NotBlank(message = "参数不能为空")
    private String consigneeName;

    @ApiModelProperty(value = "收货人手机号")
    @NotBlank(message = "参数不能为空")
    private String consigneeMobile;

    @ApiModelProperty(value = "收货地址")
    @NotBlank(message = "参数不能为空")
    private String consigneeAddress;

    @ApiModelProperty(value = "订单商品")
    @Size(min=1, message="订单商品不可以为空！")
    private List<OrderGoodsRequest> orderGoods;

    @Data
    public class OrderGoodsRequest{

        @ApiModelProperty(value = "商品id")
        @Range(min=1, max=Integer.MAX_VALUE, message = "参数错误")
        private int goodsId;

        @ApiModelProperty(value = "商品数量")
        @Range(min=1, max=Integer.MAX_VALUE, message = "参数错误")
        private int goodsCount;

    }
}
