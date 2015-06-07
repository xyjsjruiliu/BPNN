package com.xy.lr.ml.bpnn

import com.xy.lr.ml.properties.BPNNProperty
import com.xy.lr.ml.util.TrainDataSet

//import org.apache.spark.SparkContext

import scala.collection.mutable.ArrayBuffer

/**
 * Created by xylr on 15-6-1.
 * BP 神经网络
 */
class BPNeuralNetwork {
  //输入层神经元个数
  private var inputLayerCount : Int = _
  //隐含层神经元个数
  private var hiddenLayerCount : Int = _
  //输出层神经元个数
  private var outputLayerCount : Int = _

  //每层神经元
  private var inputLayerNeureArray : ArrayBuffer[Neure] = _
  private var hiddenLayerNeureArray : ArrayBuffer[Neure] = _
  private var outputLayerNeureArray : ArrayBuffer[Neure] = _

  //连接权重
  private var InputHiddenWeight : ArrayBuffer[ArrayBuffer[Double]] = _
  private var HiddenOutputWeight : ArrayBuffer[ArrayBuffer[Double]] = _

  //学习速率
  private var learningRate: Double = _
  //惯性因子
  private var inertiafactor: Double = _

  //训练数据集
  private var trainSet : ArrayBuffer[TrainDataSet] = _

  //sparkContext
  //private var sparkContext : SparkContext = _
  //private var number : Int = _

  //配置文件
  private var bpnnProperty : BPNNProperty = _

  def this(bPNNPropertyPath: String, inputCount : Int, hiddenCount : Int, outputCount : Int){
    this()
    //this.sparkContext = sc
    //配置文件
    bpnnProperty = new BPNNProperty(bPNNPropertyPath)

    //创建训练数据集对象
    trainSet = new ArrayBuffer[TrainDataSet]()

    //设置每层神经元个数
    setNeureCount(inputCount, hiddenCount, outputCount)

    //初始化每层神经元和连接权重
    initNeureAndConnectionWeights()
  }

  //设置学习速率
  def setLearningRate(lR: Double) = this.learningRate = lR

  //设置惯性因子
  def setInertiafactor (iF: Double) = this.inertiafactor = iF

  //设置训练数据集
  def setTrainDataSet(dataSet : ArrayBuffer[TrainDataSet]) = this.trainSet = dataSet

  //设置每层神经元个数
  private def setNeureCount(inputCount : Int, hiddenCount : Int, outputCount : Int): Unit ={
    this.inputLayerCount = inputCount
    this.hiddenLayerCount = hiddenCount
    this.outputLayerCount = outputCount
  }

  /**
   * 初始化每层神经元和连接权重
   * */
  private def initNeureAndConnectionWeights(): Unit ={
  }

  /**
   * 训练BP神经网络
   * */
  def train()

}