package com.seasoning.request;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * @Authot:better
 * @Date:2019/8/1 11:22
 */
@Data
public class GoodsSearchRequest {

    private String goodsName;

    private Integer purpose;

    @DecimalMin(value = "0")
    private BigDecimal minPrice;
    @DecimalMin(value = "0")
    private BigDecimal maxPrice;

    private Integer goodsTypeId;

    private Boolean selling;
}
