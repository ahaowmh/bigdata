package com.atguigu.gmallpublisher.service;

import java.util.Map;

/**
 * @author ahao
 * @date 2022/6/23 16:41
 */
public interface PublisherService {
    //日活总数数据接口
    public Integer getDauTotal(String date);

    //日活分时数据接口
    public Map getDauHourTotal(String date);

    //交易额总数数据接口
    public Double getGmvTotal(String date);

    //交易额分时数据接口
    public Map getGmvHourTotal(String date);
}
