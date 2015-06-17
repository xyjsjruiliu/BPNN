package com.xy.lr.ml.util

import scala.collection.mutable.ArrayBuffer

/**
 * Created by xylr on 15-6-7.
 * 训练数据集
 */
class TrainDataSet extends java.io.Serializable{
  /**
   * 属性
   * */
  private var mAttribList: ArrayBuffer[Double] = _
  /**
   * 类型
   * */
  private var genre : Int = 0

  /***/
  mAttribList = new ArrayBuffer[Double]()


  /**
   * 设置类型
   * */
  def setGenre(genre : Int): Unit = this.genre = genre

  /**
   * 获取类型
   * */
  def getGenre : Int = this.genre

  /**
   * 添加属性
   * */
  def addAttr(attr : Double) = mAttribList += attr

  /**
   * 获取属性列表
   * */
  def getAttribList : ArrayBuffer[Double] = mAttribList
}
