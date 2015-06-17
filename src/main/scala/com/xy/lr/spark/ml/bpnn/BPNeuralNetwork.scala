package com.xy.lr.spark.ml.bpnn

import com.xy.lr.ml.util.{DataUtil, TrainDataSet}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ArrayBuffer

/**
 * Created by xy lr on 15-6-16.
 * BP 神经网络 for Spark
 */
class BPNeuralNetwork {
  //sparkContext
  private var sparkContext : SparkContext = _

  //输入层神经元个数
  private var inputLayerCount : Int = _
  //隐含层神经元个数
  private var hiddenLayerCount : Int = _
  //输出层神经元个数
  private var outputLayerCount : Int = _

  // 步长
  private var step: Double = _
  // 阈值
  private var threshold: Double = _
  // 误差
  private var error : Double = _

  //训练数据集
  private var trainSet : ArrayBuffer[TrainDataSet] = _

  def this(inputCount : Int, hiddenCount : Int, outputCount : Int){
    this()

    //设置每层神经元个数
    setNeuralCount(inputCount, hiddenCount, outputCount)
  }

  //设置 步长
  def setStep(step: Double) = this.step = step

  //设置 阈值
  def setThreshold (threshold: Double) = this.threshold = threshold

  //设置 误差
  def setError(error : Double) = this.error = error

  //设置每层神经元个数
  private def setNeuralCount(inputCount : Int, hiddenCount : Int, outputCount : Int): Unit ={
    this.inputLayerCount = inputCount
    this.hiddenLayerCount = hiddenCount
    this.outputLayerCount = outputCount
  }

  def setSparkContext(sparkContext : SparkContext): Unit ={
    this.sparkContext = sparkContext
  }

  //设置训练数据集
  def setTrainDataSet(trainSet : ArrayBuffer[TrainDataSet]): Unit ={
    this.trainSet = trainSet
  }

  //train
  def train(): Unit ={
    val train = sparkContext.parallelize(trainSet)
    println(train.count())
  }
}