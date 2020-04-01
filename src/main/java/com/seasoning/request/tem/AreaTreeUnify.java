package com.seasoning.request.tem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AreaTreeUnify {
    @ApiModelProperty(value = "区id")
    private Integer id;
    @ApiModelProperty(value = "区名称")
    private String lable;
    @ApiModelProperty(value = "等级")
    private Integer level;
}
