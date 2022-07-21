package com.atguigu.gmallpublisher.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author ahao
 * @date 2022/6/26 18:18
 */
public interface OrderMapper {
    //获取当天总数Gmv
    public Double selectOrderAmountTotal(String date);

    //获取当天分时Gmv
    public List<Map> selectOrderAmountHourMap(String date);
}
