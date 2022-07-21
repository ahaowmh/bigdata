package chapter04

object WordCount {
  def main(args: Array[String]): Unit = {
    val list = List("Hello Scala Hbase Kafka", "Scala Hello Hbase", "Hbase Hello", "Hello Scala")

    //拆散长字符串，扁平化操作
    val wordList = list.flatMap(_.split(" "))
    println(wordList)

    //分组聚合
    val wordGroupMap = wordList.groupBy(w => w)
    println(wordGroupMap)

    //转化结构
    //val wordCountMap = wordGroupMap.map(tuple => (tuple._1, tuple._2.length))
    //如果元素是二元组，map的映射的时候，key(String)不变，只有value(list)变化  ---》使用mapValues
    val wordCountMap = wordGroupMap.mapValues(_.length)
    println(wordCountMap)//Map(Kafka -> 1, Hello -> 4, Hbase -> 3, Scala -> 3)

    //排序。map不支持排序，需要先转化结构为list,再调用排序方法
    //sortBy
    //val sortWordCount = wordCountMap.toList.sortBy(tuple => tuple._2)(Ordering[Int].reverse)
    //sortWith
    val sortWordCount = wordCountMap.toList.sortWith((left, right) => left._2 > right._2)
    println(sortWordCount)//List((Hello,4), (Hbase,3), (Scala,3), (Kafka,1))
    //取前三
    val result = sortWordCount.take(3)
    println(result)//List((Hello,4), (Hbase,3), (Scala,3))

    //熟练简写
    val list1 = list.flatMap(_.split(" "))
      .groupBy(a=>a)//分组
      .mapValues(_.length)//转化结构
      .toList//转化list（map不能排序）
      .sortWith(_._2>_._2)//排序
      .take(3)//取前三
    println(list1)

  }
}
