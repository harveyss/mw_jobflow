package com.mainiway.jobflow
import java.util.Properties
import GetXMLParam._

/**
 * @desc 获取HIVE,MySQL等连接参数
 */
private[jobflow] object UtilAnalysis {
  //获取HIVE参数
  def getHiveParam:String = {
    val hive_database = hive_db
    return hive_database
  }
  
  //获取写入分析指标结果的mysql配置参数
  def getMySQLParam_Result: (String, Properties) = {
    //写入统计指标的数据库
    val url_result = "jdbc:mysql://"+address_result+":"+port_result+"/"+database_result+"?useUnicode=true&characterEncoding=utf8";
    val prop_result = new Properties();
    prop_result.setProperty("driver", "com.mysql.jdbc.Driver");
    prop_result.setProperty("user", user_result);
    prop_result.setProperty("password", password_result);
    return (url_result, prop_result)
  }
  
  //分析指标sql语句存储表所在的mysql配置参数
  def getMySQLParam_Sql: (String,String) = {
    //写入统计指标的数据库
    val url_sql = "jdbc:mysql://"+address_sql+":"+port_sql+"/"+database_sql+
                  "?user="+user_sql+"&password="+password_sql+"&useUnicode=true&characterEncoding=utf8";
    return (url_sql,table_sql)
  }
  
  //写入分析指标sql运行message的mysql配置参数
  def getMySQLParam_Message: (String, Properties,String) = {
    val url_message = "jdbc:mysql://"+address_message+":"+port_message+"/"+database_message+"?useUnicode=true&characterEncoding=utf8";
    val prop_message = new Properties();
    prop_message.setProperty("driver", "com.mysql.jdbc.Driver");
    prop_message.setProperty("user", user_message);
    prop_message.setProperty("password", password_message);
    return (url_message, prop_message,table_message)
  }
  
}