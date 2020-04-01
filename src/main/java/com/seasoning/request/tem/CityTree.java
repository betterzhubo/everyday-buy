package com.seasoning.request.tem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class CityTree {
    @ApiModelProperty(value = "市id")
    private Integer cityid;
    @ApiModelProperty(value = "市名称")
    private String cityname;
    @ApiModelProperty(value = "等级")
    private Integer level;
    @ApiModelProperty(value = "区级列表")
    private List<AreaTree> areaTreeList;
}
