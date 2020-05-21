package com.seasoning.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel
public class OrderListResponse {

    @ApiModelProperty(value = "订单号")
    private Integer orderId;
    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;
    @ApiModelProperty(value = "商品总数")
    private Integer totalCount = 0;
    @ApiModelProperty(value = "收货地址")
    private String consigneeAddress;
    @ApiModelProperty(value = "商品总价")
    private BigDecimal totalPrice = new BigDecimal(0);
    @ApiModelProperty(value = "下单时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "订单商品")
    private List<OrderGoodsResponse> orderGoodsList;

    @Data
    public class OrderGoodsResponse{

        @ApiModelProperty(value = "商品id")
        private int goodsId;

        @ApiModelProperty(value = "商品数量")
        private int goodsCount;

        @ApiModelProperty(value = "商品名")
        private String goodsName;

        @ApiModelProperty(value = "商品价格")
        private BigDecimal goodsPrice;

        @ApiModelProperty(value = "展示图片")
        private String showImage;

    }
}
