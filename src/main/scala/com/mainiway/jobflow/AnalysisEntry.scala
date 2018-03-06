package com.mainiway.jobflow

import ToMySQL.spark
object AnalysisEntry {
  /**
   * @desc 启动分析
   */
  def main(args: Array[String]) {
    val hive2MySQL = new ToMySQL
//    val rap = hive2MySQL.readAnalysisParam();
    val rap = hive2MySQL.getTableSqlList;
    rap.foreach(s => println(s._1 + "," + s._2));    
    println("总共分析指标个数：" + rap.size)
    //执行分析指标，将结果存储到MySQL数据库中
    hive2MySQL.write2Mysql(rap) 
    println("统计指标分析完毕!");  
  }
  
}