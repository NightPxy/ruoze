package night.homework.common.io.text

import java.io.FileWriter

import night.homework.common.io.{TraversableReadAble, TraversableReadWriter, TraversableWriteAble}
import night.homework.common.utils.patterns.UsingPattern

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.reflect.ClassTag

/**
  * Created by Night on 2018/7/22.
  */
class TextTraversableReadWriter extends TraversableReadWriter with UsingPattern {
  override def write[T <: TraversableWriteAble : ClassTag](traversableData: Traversable[T], path: String,
                                                           lineSeq: String = "\n", fieldSeq: String = "\t"): Unit = {
    using(new FileWriter(path, true)) {
      writer => {
        traversableData.map {
          ele => {
            this.runtimeClassFields.map(field => writer.write(s"${field.get(ele).toString}${fieldSeq}"))
            writer.write(s"${lineSeq}")
          }
        }
      }
    }
  }

  override def read[T <: TraversableReadAble : ClassTag](traversableData: Traversable[T], path: String,
                                                              lineSeq: String = "\n", fieldSeq: String="\t"): Unit = {
    using(Source.fromFile(path)) {
      reader => {
        val listBuffer = ListBuffer[T]()
        while(true){
          for(line <- reader.getLines){
            if(line == null || line.length <= 0)
            {
               return;
            }
            val element = this.runtimeClass.newInstance().asInstanceOf[T]
            val rows = line.split(fieldSeq)
            for(i <- 0 until this.runtimeClassFields.length){
              val field = this.runtimeClassFields(i);
              val value = rows(i);
              field.set(value,value)
            }
            listBuffer += element
            traversableData= traversableData.++(element)
          }
          traversableData = listBuffer
          val line = reader.getLines()
        }
        traversableData.map {
          ele => {
            this.runtimeClassFields.map(field => writer.write(s"${field.get(ele).toString}${fieldSeq}"))
            writer.write(s"${lineSeq}")
          }
        }
      }
    }
  }
}

object TextTraversableReadWriter {
  def apply(): TextTraversableReadWriter = new TextTraversableReadWriter()
}
