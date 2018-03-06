package com.mainiway.jobflow
import scala.xml.XML
import org.apache.spark.sql.SparkSession
import scala.util.Properties
import java.util.Properties
import scala.io.Source
import java.io.FileInputStream
import scala.beans.BeanProperty
import java.io.IOException
import java.sql.SQLException
import org.apache.spark.sql.AnalysisException

case class sqlMessage(
      job_id:Int,
      message:String, 
      app_id:String  
      )

class ToMySQL {

  //取出分析指标sql语句
  def getTableSqlList():List[(String,String,Int)] = {   
     val param_sql = UtilAnalysis.getMySQLParam_Sql
 
     import ToMySQL._
     val df = spark.read.format("jdbc")
     .option("url", param_sql._1)
     .option("dbtable",param_sql._2)
     .load()   
     val table_sql_list = df.rdd.map(r => (r.getString(1),r.getString(2),r.getInt(0))).collect().toList
     table_sql_list
  }

  //将分析指标sql运行结果和运行message写入mysql
  def write2Mysql(hive2Myql: Seq[(String, String, Int)]): Unit = {
    import ToMySQL._
    import spark.implicits._;
    
    //hive连接参数
    val hive_param = UtilAnalysis.getHiveParam
    spark.sql("use " + hive_param);
    //写入分析指标结果的mysql配置参数
    val param_result = UtilAnalysis.getMySQLParam_Result
    //分析指标sql运行message的mysql配置参数
    val param_message = UtilAnalysis.getMySQLParam_Message
    
    var sqlmessagelist = List[sqlMessage]()
    //写入MySQL数据库中
    for (h2m <- hive2Myql) {
      println("MySQL：" + h2m._1 + "，分析HQL：" + h2m._2)
      //分析数据
      try {
        val df = spark.sql(h2m._2).na.fill(0);
        df.write.mode("overwrite").jdbc(param_result._1, h2m._1, param_result._2);
        sqlmessagelist = sqlmessagelist :+ sqlMessage(h2m._3,"success",app_id)
      } catch {
        case sqlerror: AnalysisException =>{
        sqlmessagelist = sqlmessagelist :+ sqlMessage(h2m._3,sqlerror.getMessage,app_id);
        }
        case sqlerror: SQLException =>{
        sqlmessagelist = sqlmessagelist :+ sqlMessage(h2m._3,sqlerror.getMessage,app_id);
        }
        case ex: Exception => {
        sqlmessagelist = sqlmessagelist :+ sqlMessage(h2m._3,ex.getMessage,app_id);  
        }        
      }       
    }
    sqlmessagelist.toDF().write.mode("append").jdbc(param_message._1, param_message._3, param_message._2);
    //结束分析结果
    spark.stop();
  }
}

object ToMySQL {
  /**
   * @desc创建spark启动
   */
  val spark = SparkSession.builder().appName("统计指标分析")
  .enableHiveSupport().getOrCreate();
  val app_id = spark.sparkContext.applicationId 
}