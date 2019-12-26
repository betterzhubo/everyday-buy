package com.seasoning.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Authot:better
 * @Date:2019/8/1 11:22
 */
@Data
@ApiModel
public class OrderSearchRequest {

    @ApiModelProperty(value = "订单唯一id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "订单状态 0,已删除;1,待支付;2,待配送;3,配送中;4,已完成;5,已取消")
    private Integer orderStatus;

    @ApiModelProperty(value = "当前页")
    private Integer current;

    @ApiModelProperty(value = "每页显示条数，默认 10")
    private Integer pageSize;

}
