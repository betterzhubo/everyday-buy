package com.seasoning.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class GoodsPageRequest {

    @NotNull(message = "参数不能为空")
    private Integer current;

    @NotNull(message = "参数不能为空")
    private Integer pageSize;

    private String goodsName;

    @Min(value = 1, message = "商品类型错误")
    private Integer goodsTypeId;

    @Range(min = 0, max = 1, message = "商品用途错误")
    private Integer purpose;
}
