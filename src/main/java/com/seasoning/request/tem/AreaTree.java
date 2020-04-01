package com.seasoning.request.tem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AreaTree {
    @ApiModelProperty(value = "区id")
    private Integer areaid;
    @ApiModelProperty(value = "区名称")
    private String areaname;
    @ApiModelProperty(value = "等级")
    private Integer level;
}
