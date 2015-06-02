package com.xy.lr.ml.bpnn

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by xylr on 15-6-1.
 * BP 神经网络
 */
class BPNeuralNetwork {
  private var sparkContext : SparkContext = _
  private var number : Int = _

  def this(sc : SparkContext){
    this()
    this.sparkContext = sc
  }
}