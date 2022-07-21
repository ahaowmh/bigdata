package com.atguigu.gmallpublisher.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author ahao
 * @date 2022/6/23 16:36
 */
public interface DauMapper {
    //获取日活总数据
    public Integer selectDauTotal(String date);

    //获取日活分时数据
    public List<Map> selectDauTotalHourMap(String date);
}
