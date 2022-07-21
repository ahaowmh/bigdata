package com.atguigu.day11.function;

import org.apache.flink.table.functions.ScalarFunction;

/**
 * @author ahao
 * @date 2022/7/14 19:12
 */

/**
 * UDF，一进一出，将单词转化为大写
 */
public class MyUDF extends ScalarFunction {
    //eval()名称固定，是通过反射调用的
    public String eval(String value) {
        return value.toUpperCase();
    }
}
