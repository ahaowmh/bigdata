package com.atguigu;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class Myinterceptot implements Interceptor {
    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        //获取输入信息
        String date = new String(event.getBody(), StandardCharsets.UTF_8);
        //获取头部信息
        Map<String, String> headers = event.getHeaders();

        //判断第一个字符是数字还是字母
        char c = date.charAt(0);
        if (c >= '0' && c <= '9') {
            //数字
            headers.put("type","number");
        }else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            //字母
            headers.put("type","letter");
            //引用类型，不需要再将header放回event
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        for (Event event : events) {
            intercept(event);
        }
        return events;
    }

    @Override
    public void close() {

    }
    public static class Builder implements Interceptor.Builder{

        @Override
        public Interceptor build() {
            return new Myinterceptot();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
