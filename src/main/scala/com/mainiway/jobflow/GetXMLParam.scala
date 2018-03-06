package com.mainiway.jobflow

import scala.xml._

import org.apache.spark.SparkFiles
import ToMySQL.spark

object GetXMLParam{
    
    //读取内部XML配置文件
    //val is = Thread.currentThread().getContextClassLoader().getResourceAsStream("hive_mysql_conf.xml")
    //val root: scala.xml.Elem = XML.load(xml_file)
       
    //读取外部XML配置文件
    spark.sparkContext.addFile("hive_mysql_conf.xml")
    val xml_file = SparkFiles.get("hive_mysql_conf.xml")
    val root: scala.xml.Elem = XML.load(xml_file)
    
    //hive数据库参数
    val hive_db = (root\"From"\"hive"\"database").text
    
    //写入分析指标结果的mysql配置参数
    val address_result = (root\"To"\"mysql_result"\"address").text
    val port_result = (root\"To"\"mysql_result"\"port").text
    val database_result = (root\"To"\"mysql_result"\"database").text
    val user_result = (root\"To"\"mysql_result"\"user").text
    val password_result = (root\"To"\"mysql_result"\"password").text
    
    //分析指标sql语句存储表所在的mysql配置参数
    val address_sql = (root\"To"\"mysql_service_sql"\"address").text
    val port_sql = (root\"To"\"mysql_service_sql"\"port").text
    val database_sql = (root\"To"\"mysql_service_sql"\"database").text
    val table_sql = (root\"To"\"mysql_service_sql"\"table").text
    val user_sql = (root\"To"\"mysql_service_sql"\"user").text
    val password_sql = (root\"To"\"mysql_service_sql"\"password").text
    
    //写入分析指标sql运行message的mysql配置参数
    val address_message = (root\"To"\"mysql_sql_run_message"\"address").text
    val port_message = (root\"To"\"mysql_sql_run_message"\"port").text
    val database_message = (root\"To"\"mysql_sql_run_message"\"database").text
    val table_message = (root\"To"\"mysql_sql_run_message"\"table").text
    val user_message = (root\"To"\"mysql_sql_run_message"\"user").text
    val password_message = (root\"To"\"mysql_sql_run_message"\"password").text                 
}



