package com.xy.lr.ml.util

import scala.collection.immutable.HashMap
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * Created by xylr on 15-6-7.
 * 获取训练和测试数据集
 */
object DataUtil {

  /**
   * 类别和编号
   * */
  private var mTypes : HashMap[String, Int] = new HashMap[String, Int]()
  /**
   * 类别数
   * */
  private var mTypesCount : Int = 0

  /**
   * 获取类别和编号
   * */
  def getTypes : HashMap[String, Int] = mTypes

  /**
   * 获取类别数
   * */
  def getTypesCount : Int = mTypesCount

  /**
   * 通过编号获取类别
   * */
  def getTypeName(ty : Int) : String = {
    if (ty == -1) "null"
    else{
      val sp = mTypes.iterator
      for (i <- sp){
        if(i._2 == ty)
          return i._1
      }
      "null"
    }
  }

  /**
   * 获取训练和测试数据集
   * */
  def getTrainSet(filename : String, sep : String) : ArrayBuffer[TrainDataSet] = {
    val set = new ArrayBuffer[TrainDataSet]()
    val file = Source.fromFile(filename)
    val line = file.getLines()

    for(i <- line){
      val splits = i.split(sep)
      val trainSet = new TrainDataSet()
      for(j <- Range(0, splits.length)){
        try{
          trainSet.addAttr(splits(j).toDouble)
        }catch {
          case e : NumberFormatException => {
            // 非数字，则为类别名称，将类别映射为数字
            if(mTypes.getOrElse(splits(j), "null").equals("null")){
              mTypes += ((splits(j), mTypesCount))
              mTypesCount += 1
            }
            trainSet.setGenre(mTypes.get(splits(j)).get)
            set += trainSet
          }
        }
      }
    }
    set
  }

  def main(args : Array[String]): Unit ={
    val s = getTrainSet("data/ann/train.txt",",")
    println(getTypes)
    println(getTypeName(1))
    println(getTypesCount)

    for(i <- s){
      for(j <- i.getAttribList)
        print(j + "  ")
      println(":::" + i.getGenre)
    }
  }
}
