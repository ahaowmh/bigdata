package com.atguigu.gmallpublisher.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmallpublisher.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ahao
 * @date 2022/6/23 16:51
 */
@RestController
public class Controller {

    @Autowired
    private PublisherService publisherService;

    @RequestMapping("realtime-total")
    public String getDauTotal(@RequestParam("date") String date){

        //获取日活总数据
        Integer dauTotal = publisherService.getDauTotal(date);

        //创建list集合存放结果数据
        ArrayList<Map> result = new ArrayList<>();

        //创建存放新增日活的map集合
        HashMap<String, Object> dauMap = new HashMap<>();
        dauMap.put("id","dau");
        dauMap.put("name","新增日活");
        dauMap.put("value",dauTotal);

        //创建存放新增设备的map结合
        HashMap<String, Object> devMap = new HashMap<>();
        devMap.put("id","new_mid");
        devMap.put("name","新增设备");
        devMap.put("value",233);

        //创建新增交易额的map集合
        HashMap<String, Object> gmvMap = new HashMap<>();
        gmvMap.put("id","order_amount");
        gmvMap.put("name","新增交易额");
        gmvMap.put("value",publisherService.getGmvTotal(date));

        result.add(dauMap);
        result.add(devMap);
        result.add(gmvMap);

        return JSONObject.toJSONString(result);
    }
    @RequestMapping("realtime-hours")
    public String getDauHourTotal(@RequestParam("id") String id,@RequestParam("date") String date){
        //根据传入的date获取前一天的date
        String yesterday = LocalDate.parse(date).plusDays(-1).toString();

        Map todayMap = null;
        Map yesterdayMap = null;


        //根据ID判断获取的是那个需求的分时数据
        if ("dau".equals(id)){
            //获取经过service层处理后的数据
            todayMap = publisherService.getDauHourTotal(date);

            //根据前一天的时间获取前一天的时分数据
            yesterdayMap = publisherService.getDauHourTotal(yesterday);
        }else if ("order_amount".equals(id)){
            todayMap = publisherService.getGmvHourTotal(date);

            yesterdayMap = publisherService.getGmvHourTotal(yesterday);
        }




        //最终存放数据的map
        HashMap<String, Map> result = new HashMap<>();

        result.put("today",todayMap);
        result.put("yesterday",yesterdayMap);

        return JSONObject.toJSONString(result);
    }
}
