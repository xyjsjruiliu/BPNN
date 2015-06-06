package com.xy.lr.ml.bpnn

import com.xy.lr.ml.properties.BPNNProperty
import org.apache.spark.SparkContext

/**
 * Created by xylr on 15-6-1.
 * BP 神经网络
 */
class BPNeuralNetwork {
  //sparkContext
  private var sparkContext : SparkContext = _
  private var number : Int = _
  //配置文件
  private var bpnnProperty : BPNNProperty = _

  def this(sc : SparkContext, bPNNPropertyPath: String){
    this()
    this.sparkContext = sc
    bpnnProperty = new BPNNProperty(bPNNPropertyPath)
  }
}