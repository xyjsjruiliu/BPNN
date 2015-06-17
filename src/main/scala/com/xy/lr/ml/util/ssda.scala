package com.xy.lr.ml.util

import java.io.{File, FileWriter, BufferedWriter}

import com.xy.lr.scala.io.FileList

/**
 * Created by xylr on 15-6-8.
 * ssda
 */
object ssda{
  def main(args : Array[String]): Unit = {
    val file = new FileList("data/ImgDB/")
    val fileList = file.getList
    var num = 0

    val output = new BufferedWriter(new FileWriter(new File("type")))

    for(i <- fileList){
      var tmp : Array[Double] = null
      val outputType = i.substring(i.lastIndexOf("/") + 1, i.indexOf("_"))
//      println(outputType)
      try{

        tmp = new imageProcess().getFeature(i)

        for(j <- tmp) {
          output.write(tmp(j.toInt).toString + ",")
          output.flush()
        }
        output.write(outputType + "\n")
        output.flush()
//        println(tmp)
      }catch {
        case e : ArrayIndexOutOfBoundsException =>
//          e.printStackTrace()
        num += 1
      }
    }

    output.close()
    println(num)
  }
}