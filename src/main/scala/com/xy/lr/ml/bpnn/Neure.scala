package com.xy.lr.ml.bpnn

import java.lang.Math._

/**
 * Created by xylr on 15-6-3.
 * 神经元
 */
class Neure {
  /**
   * 神经元类型（0表示输入层，1表示隐含层，2表示输出层）
   * */
  private var genre : Int = _

  /**
   * 设置神经元类型
   * */
  def setGenre(genre : Int): Unit ={
    this.genre = genre
  }

  /**
   * 神经元 前向 输入输出值
   * */
  private var neureForwardInputValue : Double = _
  private var neureForwardOutputValue : Double = _

  /**
   * 神经元 反向 输入输出值
   * */
  private var neureBackwardInputValue : Double = _
  private var neureBackwardOutputValue : Double = _

  /**
   * 构造函数（设置神经元类型）
   * */
  def this(genre : Int){
    this()
    this.genre = genre
  }

  /**
  * log-sigmoid函数
  */
  def logS(x : Double) : Double = 1 / (1 + exp(-x))

  /**
  * log -sigmoid函数的导数
  */
  def logSDerivative(x : Double) : Double = logS(x) * abs(1 - logS(x))

  /**
   * tan-sigmoid函数
   */
  def tanhS(in : Double) : Double = (exp(in) - exp(-in))/(exp(in) + exp(-in))

  /**
   * tan -sigmoid函数的导数
   */
  def tanhSDerivative(x : Double) : Double = 1 - pow(tanhS(x), 2)

  /**
   * sigmoid函数，这里用tan-sigmoid，经测试其效果比log-sigmoid好！
   * */
  def forwardSigmoid(in : Double) : Double = {
    genre match {
      case Neure.TYPE_INPUT => in
      case Neure.TYPE_HIDDEN => tanhS(in)
      case Neure.TYPE_OUTPUT => tanhS(in)
    }
  }

  /**
   * 设置 前向 输入数据
   * */
  def setForwardInputValue(in : Double): Unit ={
    this.neureForwardInputValue = in
    setForwardOutputValue(in)
  }

  /**
   * 设置 前向 输出数据
   * */
  def setForwardOutputValue(in : Double): Unit = this.neureForwardOutputValue = forwardSigmoid(in)

  /**
   * 获取 前向 输出数据
   * */
  def getForwardOutputValue : Double = this.neureForwardOutputValue

  /**
   * 获取 前向 输入数据
   * */
  def getForwardInputValue : Double = this.neureForwardInputValue

  /**
   * 误差反向传播时，激活函数的导数
   * */
  def backwardPropagate(in : Double) : Double = {
    genre match {
      case Neure.TYPE_INPUT => in
      case Neure.TYPE_HIDDEN => tanhSDerivative(in)
      case Neure.TYPE_OUTPUT => tanhSDerivative(in)
    }
  }

  /**
   * 设置 反向 输入数据
   * */
  def setBackwardInputValue(in : Double): Unit ={
    this.neureBackwardInputValue = in
    setBackwardOutputValue(in)
  }

  /**
   * 设置 反向 输出数据
   * */
  def setBackwardOutputValue(in : Double): Unit ={
    this.neureBackwardOutputValue = backwardPropagate(in)
  }

  /**
   * 获取 反向 输入数据
   * */
  def getBackwardInputValue : Double = this.neureForwardInputValue

  /**
   * 获取 反向 输出数据
   * */
  def getBackwardOutputValue : Double = this.neureBackwardOutputValue
}
object Neure{
  //输入层节点标志
  def TYPE_INPUT : Int = 0
  //隐含层节点标志
  def TYPE_HIDDEN : Int = 1
  //输出层节点标志
  def TYPE_OUTPUT : Int = 2
}
