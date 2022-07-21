package com.atguigu.gmallpublisher.service.impl;

import com.atguigu.gmallpublisher.mapper.DauMapper;
import com.atguigu.gmallpublisher.mapper.OrderMapper;
import com.atguigu.gmallpublisher.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ahao
 * @date 2022/6/23 16:48
 */
@Service
public class PublisherServiceImpl implements PublisherService {

    @Autowired
    private DauMapper dauMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Integer getDauTotal(String date) {
        return dauMapper.selectDauTotal(date);
    }

    @Override
    public Map getDauHourTotal(String date) {
        //获取DAO（mapper)层的数据
        List<Map> list = dauMapper.selectDauTotalHourMap(date);

        //创建map集合存放返回的数据
        HashMap<String, Long> result = new HashMap<>();

        //遍历list集合，获取老map，封装新map
        for (Map map : list) {
            result.put((String) map.get("LH"),(Long) map.get("CT"));
        }
        return result;
    }

    @Override
    public Double getGmvTotal(String date) {
        return orderMapper.selectOrderAmountTotal(date);
    }

    @Override
    public Map getGmvHourTotal(String date) {
        //获取DAO（mapper)层的数据
        //List<Map> list = dauMapper.selectDauTotalHourMap(date);
        List<Map> list = orderMapper.selectOrderAmountHourMap(date);

        //创建map集合存放返回的数据
        HashMap<String, Double> result = new HashMap<>();

        //遍历list集合，获取老map，封装新map
        for (Map map : list) {
            result.put((String) map.get("CREATE_HOUR"),(Double) map.get("SUM_AMOUNT"));
        }
        return result;
    }

}
