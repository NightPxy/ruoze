package night.homework.common.io.jdbc

import java.io.FileWriter
import java.util.Date

import night.homework.common.io.TraversableReadWriter
import night.homework.common.scalikejdbc.{ScalikejdbcConf, ScalikejdbcContext}
import night.homework.common.utils.patterns.UsingPattern

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.reflect.ClassTag

/**
  * Created by Administrator on 2018/7/23.
  */
//class JdbcTraversableReadWriter[T](traversableData: Traversable[T], conf: mutable.HashMap[String, String])
//  extends TraversableReadWriter[T](traversableData,conf) with UsingPattern {
//
//  val jdbcConf = ScalikejdbcConf(conf);
//
//  override def write(traversableData: Traversable[T]): Unit = {
//
//    using(new FileWriter(path, true)) {
//      writer => {
//        traversableData.map {
//          ele => {
//            this.runtimeClassFields.map(field => {
//              field.setAccessible(true);
//              if (field.getGenericType == classOf[Date]) {
//                writer.write(s"${simpleDateFormat.format(field.get(ele).asInstanceOf[Date])}${fieldSeq}")
//              }
//              else {
//                writer.write(s"${field.get(ele).toString}${fieldSeq}")
//              }
//            })
//            writer.write(s"${lineSeq}")
//          }
//        }
//      }
//    }
//  }
//
//  override def read(path: String, lineSeq: String = "\n", fieldSeq: String = "\t"): ListBuffer[T] = {
//    using(Source.fromFile(path)) {
//      reader => {
//        val listBuffer = ListBuffer[T]()
//        for (line <- reader.getLines) {
//          val element = this.runtimeClass.newInstance().asInstanceOf[T]
//          val rows = line.split(fieldSeq)
//          if(rows.length == this.runtimeClassFields.length) {
//            for (i <- 0 until this.runtimeClassFields.length) {
//              val field = this.runtimeClassFields(i);
//              val value = rows(i);
//              field.setAccessible(true);
//
//              if (field.getGenericType == classOf[Date]) {
//                field.set(element, simpleDateFormat.parse(value))
//              }
//              else {
//                field.set(element, value)
//              }
//            }
//            listBuffer += element
//          }
//        }
//        return listBuffer
//      }
//    }
//  }
//}
//
//object JdbcTraversableReadWriter {
//  def apply[T: ClassTag](): JdbcTraversableReadWriter[T] = new JdbcTraversableReadWriter[T]()
//}
