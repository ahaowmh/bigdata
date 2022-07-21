package com.atguigu.day11.function;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

/**
 * @author ahao
 * @date 2022/7/14 19:32
 */
//ROW<word STRING, length INT>,炸裂出来两列  word是炸裂后的列名，也可以单独去做命名操作
@FunctionHint(output = @DataTypeHint("ROW<word STRING, length INT>"))
public class MyUDTF extends TableFunction<Row> {
    //eval方法每行调用一次
    public void eval(String str){
        //按照空格将要炸裂的值分割
        String[] words = str.split(" ");
        //每个要裂开的值调用一次collect
        for (String word : words) {
            collect(Row.of(word, word.length()));
        }
    }
}
