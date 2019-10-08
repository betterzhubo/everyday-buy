package com.seasoning.request;

import lombok.Data;

/**
 * @Authot:better
 * @Date:2019/8/1 11:22
 */
@Data
public class OrderSearchRequest {

    private Long id;

    private String userName;

    private Integer orderStatus;

    private Integer current;

    private Integer pageSize;

}
