package com.xy.lr.ml.bpnn

import java.lang.Math._

import com.xy.lr.ml.util.NeureType

/**
 * Created by xylr on 15-6-3.
 * 神经元
 */
class Neuron {
  /**
   * 神经元类型（0表示输入层，1表示隐含层，2表示输出层）
   * */
  private var genre : Int = _

  /**
   * Threshold Yuzhi
   * */
  private var threshold : Double = _

  /**
   * 设置神经元类型
   * */
  def setGenre(genre : Int): Unit ={
    this.genre = genre
  }

  def getGenre : Int = {
    genre
  }

  /**
   * 神经元 前向 输入输出值
   * */
  private var neuronForwardInputValue : Double = _
  private var neuronForwardOutputValue : Double = _

  /**
   * 神经元 反向 输入输出值
   * */
  private var neuronBackwardInputValue : Double = _
  private var neuronBackwardOutputValue : Double = _

  /**
   * 构造函数（设置神经元类型,yuzhi）
   * */
  def this(genre : Int){
    this()
    this.genre = genre
//    this.threshold = threshold
  }

  /**
  * log-sigmoid函数
  */
  def logS(x : Double) : Double = 1 / (1 + exp(-x))

  /**
  * log-sigmoid函数的导数
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
      case NeureType.TYPE_INPUT => in
      case NeureType.TYPE_HIDDEN => logS(in)
      case NeureType.TYPE_OUTPUT => logS(in)
    }
  }

  /**
   * 设置 前向 输入数据
   * */
  def setForwardInputValue(in : Double): Unit ={
    this.neuronForwardInputValue = in
    setForwardOutputValue(in)
  }

  /**
   * 设置 前向 输出数据
   * */
  def setForwardOutputValue(in : Double): Unit = this.neuronForwardOutputValue = forwardSigmoid(in)

  /**
   * 获取 前向 输出数据
   * */
  def getForwardOutputValue : Double = this.neuronForwardOutputValue

  /**
   * 获取 前向 输入数据
   * */
  def getForwardInputValue : Double = this.neuronForwardInputValue

  /**
   * 误差反向传播时，激活函数的导数
   * */
  def backwardPropagate(in : Double) : Double = {
    genre match {
      case NeureType.TYPE_INPUT => in
      case NeureType.TYPE_HIDDEN => in * logSDerivative(this.getForwardInputValue)
      case NeureType.TYPE_OUTPUT => (in - this.getForwardOutputValue) * logSDerivative(this.getForwardInputValue)
    }
  }

  /**
   * 设置 反向 输入数据
   * */
  def setBackwardInputValue(in : Double): Unit ={
    this.neuronBackwardInputValue = in
//    setBackwardOutputValue(in)
  }

  /**
   * 设置 反向 输出数据
   * */
  def setBackwardOutputValue(in : Double): Unit ={
    this.neuronBackwardOutputValue = backwardPropagate(in)
  }

  /**
   * 获取 反向 输入数据
   * */
  def getBackwardInputValue : Double = this.neuronBackwardInputValue

  /**
   * 获取 反向 输出数据
   * */
  def getBackwardOutputValue : Double = this.neuronBackwardOutputValue
}