package com.atguigu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ahao
 * @date 2022/7/4 21:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBehavior {
    private Long userId;
    private Long itemId;
    private Integer categoryId;
    private String behavior;
    private Long timestamp;
}
