package com.seasoning.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Authot:better
 * @Date:2019/8/1 11:22
 */
@Data
public class AddOrderRequest {

    @Range(min=1, max=Integer.MAX_VALUE, message = "参数错误")
    private int userId;
    @NotBlank(message = "参数不能为空")
    private String consigneeName;
    @NotBlank(message = "参数不能为空")
    private String consigneeMobile;
    @NotBlank(message = "参数不能为空")
    private String consigneeAddress;
    @Size(min=1, message="订单商品不可以为空！")
    private List<OrderGoodsRequest> orderGoods;

    @Data
    public class OrderGoodsRequest{

        @Range(min=1, max=Integer.MAX_VALUE, message = "参数错误")
        private int goodsId;
        @NotBlank(message = "参数不能为空")
        private String goodsName;
        @Range(min=1, max=Integer.MAX_VALUE, message = "参数错误")
        private int goodsCount;
        @DecimalMin(value = "0")
        private BigDecimal goodsPrice;

    }
}
