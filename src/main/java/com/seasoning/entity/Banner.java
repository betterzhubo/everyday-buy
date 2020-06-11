package com.seasoning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author better
 * @since 2020-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("banner")
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 轮播图名称
     */
    @TableField("banner_name")
    private String bannerName;

    @TableField("sort")
    private Integer sort;

    /**
     * 0隐藏，1显示
     */
    @TableField("is_show")
    private Integer isShow;

    /**
     * 轮播图片
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 点击跳转地址
     */
    @TableField("page_url")
    private String pageUrl;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("create_time")
    private LocalDateTime createTime;


}
