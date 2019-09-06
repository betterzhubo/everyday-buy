package com.seasoning.request;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * @Authot:better
 * @Date:2019/8/1 11:22
 */
@Data
public class OrderSearchRequest {

    private String orderId;

    private String userName;

    private Integer orderStatus;

    private Integer current;

    private Integer pageSize;

}
