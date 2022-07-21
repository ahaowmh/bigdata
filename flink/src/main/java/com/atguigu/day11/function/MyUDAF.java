package com.atguigu.day11.function;

import com.atguigu.bean.Acc;
import org.apache.flink.table.functions.AggregateFunction;

/**
 * @author ahao
 * @date 2022/7/14 20:11
 */

//第一个泛型是输出，第二个是中间值
public class MyUDAF extends AggregateFunction<Integer, Acc> {

    //初始化
    @Override
    public Acc createAccumulator() {
        Acc acc = new Acc();
        acc.setAcc(Integer.MIN_VALUE);
        return acc;
    }

    //返回结果
    @Override
    public Integer getValue(Acc accumulator) {
        return accumulator.getAcc();
    }

    //计算逻辑
    public void accumulate(Acc acc, Integer iValue) {
        acc.setAcc(Math.max(acc.getAcc(),iValue));
    }
}
