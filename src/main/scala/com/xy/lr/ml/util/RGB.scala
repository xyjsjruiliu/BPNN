package com.xy.lr.ml.util

import java.util.regex.PatternSyntaxException

import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Display

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * Created by xylr on 15-6-3.
 */
class RGB {
  //图片分辨率（高度）
  private var height : Int = _
  //图片分辨率（宽度）
  private var width : Int = _
  //r
  private var imageR : ArrayBuffer[Int] = new ArrayBuffer[Int]()
  //g
  private var imageG : ArrayBuffer[Int] = new ArrayBuffer[Int]()
  //b
  private var imageB : ArrayBuffer[Int] = new ArrayBuffer[Int]()

  //构造函数
  def this(rgbFile : String){
    this()
    //获取图片的分辨率和RGB
    getImageResolutionAndRGB(rgbFile)
  }

  //输出函数
  def myPrint(): Unit ={
    println(height + "\t" + width)
    println(imageR.length)
  }

  //rgb to hsv
  def rgbToHSV(): Unit ={

  }

  //获取图片的分辨率和RGB
  private def getImageResolutionAndRGB(rgbFile : String): Boolean ={
    val file = Source.fromFile(rgbFile)
    val fileLine = file.getLines()

    for(eachline <- fileLine){
      var tmp : Array[String] = null
      try{
        tmp = eachline.split(",")
      }catch {
        case e : PatternSyntaxException => {
          println("PatternSyntaxException In RGB this()")
          e.printStackTrace()
        }
      }
      //文件第一行，表示图片的分辨率
      if(tmp.length.equals(2)){
        var height, width = 0
        try{
          width = tmp(1).toInt
          height = tmp(0).toInt
        }catch {
          case e : NumberFormatException =>{
            println("NumberFormatException")
            e.printStackTrace()
          }
        }
        this.height = height
        this.width = width
      }
      //rgb
      else if(tmp.length.equals(3)){
        var r,g,b = 0
        try{
          r = tmp(0).toDouble.toInt
          g = tmp(1).toDouble.toInt
          b = tmp(2).toDouble.toInt
        }catch {
          case e : NumberFormatException =>{
            println("NumberFormatException")
            e.printStackTrace()
          }
        }
        imageR += r
        imageG += g
        imageB += b
      }
//      println(eachline)
    }

    true
  }
}
object RGB{
  def main(args : Array[String]): Unit ={
    val rgb = new RGB("opencv/nohup.out")
    rgb.myPrint()

//    val image = new Image()
//    val dis = new Display()
  }
}
