package com.seasoning.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class HotPageRequest {

    @NotNull(message = "参数不能为空")
    private Integer current;

    @NotNull(message = "参数不能为空")
    private Integer pageSize;

    private String goodsName;
}
