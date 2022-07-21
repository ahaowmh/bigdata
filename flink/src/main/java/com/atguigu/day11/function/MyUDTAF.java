package com.atguigu.day11.function;

import com.atguigu.bean.TOP2;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.table.functions.TableAggregateFunction;
import org.apache.flink.util.Collector;

/**
 * @author ahao
 * @date 2022/7/14 20:56
 */
public class MyUDTAF extends TableAggregateFunction<Tuple2<Double,Integer>, TOP2> {
    //初始化
    @Override
    public TOP2 createAccumulator() {
        TOP2 top2 = new TOP2();
        top2.setFirst(Double.MIN_VALUE);
        top2.setSecond(Double.MIN_VALUE);
        return top2;
    }
    //计算逻辑
    public void accumulate(TOP2 acc, Double value) {
        if (value > acc.getFirst()) {
            acc.setSecond(acc.getFirst());
            acc.setFirst(value);
        } else if (value > acc.getSecond()) {
            acc.setSecond(value);
        }
    }

    //返回结果
    public void emitValue(TOP2 acc, Collector<Tuple2<Double, Integer>> out) {
        Double first = acc.getFirst();
        Double second = acc.getSecond();
        if (first > Double.MIN_VALUE) {
            out.collect(Tuple2.of(first, 1));
        }
        if (second > Double.MIN_VALUE) {
            out.collect(Tuple2.of(second, 2));
        }
    }
}
