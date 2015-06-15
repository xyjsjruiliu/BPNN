package com.xy.lr.ml.bpnn

import com.xy.lr.ml.properties.BPNNProperty
import com.xy.lr.ml.util.{NeureType, TrainDataSet}

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
  private var inputLayerNeureArray : ArrayBuffer[Neuron] = _
  private var hiddenLayerNeureArray : ArrayBuffer[Neuron] = _
  private var outputLayerNeureArray : ArrayBuffer[Neuron] = _

  //连接权重
  private var InputHiddenWeight : Array[Array[Double]] = _
  private var HiddenOutputWeight : Array[Array[Double]] = _

  //学习速率
  private var learningRate: Double = _
  //阈值
  private var threshold: Double = _
  //误差
  private var Error : Double = _

  //训练数据集
  private var trainSet : ArrayBuffer[TrainDataSet] = _

  //sparkContext
  //private var sparkContext : SparkContext = _
  //private var number : Int = _

  //配置文件
  private var bpnnProperty : BPNNProperty = _

  /**
   * construct fuction
   * */
  def this(inputCount : Int, hiddenCount : Int, outputCount : Int){
    this()
    //this.sparkContext = sc
    //配置文件
//    bpnnProperty = new BPNNProperty(bPNNPropertyPath)

    //创建训练数据集对象
    trainSet = new ArrayBuffer[TrainDataSet]()

    //设置每层神经元个数
    setNeureCount(inputCount, hiddenCount, outputCount)

//    println(inputLayerCount + "," + hiddenLayerCount + "," + outputLayerCount)

    //初始化每层神经元和连接权重
    initNeureAndConnectionWeights()
  }

  //设置学习速率
  def setLearningRate(lR: Double) = this.learningRate = lR

  //设置惯性因子
  def setThreshold (iF: Double) = this.threshold = iF

  //设置误差
  def setError(Error : Double) = this.Error = Error

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
    init()

    /**
     * 生成节点
     * */
    for(i <- Range(0, inputLayerCount))
      inputLayerNeureArray += new Neuron(NeureType.TYPE_INPUT)
    for(i <- Range(0, hiddenLayerCount))
      hiddenLayerNeureArray += new Neuron(NeureType.TYPE_HIDDEN)
    for(i <- Range(0, outputLayerCount))
      outputLayerNeureArray += new Neuron(NeureType.TYPE_OUTPUT)

    /**
     * 初始化权值
     * */
    for(i <- Range(0, inputLayerCount))
      for(j <- Range(0, hiddenLayerCount)){
        InputHiddenWeight(i)(j) = Math.random * 0.1
      }
    for(i <- Range(0, hiddenLayerCount))
      for(j <- Range(0, outputLayerCount)){
        HiddenOutputWeight(i)(j) = Math.random * 0.1
      }

  }

  /**
   * 初始化对象
   * */
  private def init(): Unit ={
    inputLayerNeureArray = new ArrayBuffer[Neuron]()
    hiddenLayerNeureArray = new ArrayBuffer[Neuron]()
    outputLayerNeureArray = new ArrayBuffer[Neuron]()

    InputHiddenWeight = Array.ofDim[Double](inputLayerCount, hiddenLayerCount)
    HiddenOutputWeight = Array.ofDim[Double](hiddenLayerCount, outputLayerCount)
  }

  /**
   * 训练BP神经网络
   * n 表示迭代次数
   * */
  def train(n : Int): Unit ={
//    initNeureAndConnectionWeights()


    for(i <- Range(0, n)){

//      println("input hidden weight!")
//      for(i <- Range(0, inputLayerCount)){
//        for(j <- Range(0, hiddenLayerCount))
//          print(InputHiddenWeight(i)(j) + " ")
//        println()
//      }
//      println("hidden output weigth!")
//
//      for(i <- Range(0, hiddenLayerCount)){
//        for(j <- Range(0, outputLayerCount))
//          print(HiddenOutputWeight(i)(j) + " ")
//        println()
//      }
//      println()
      for(j <- Range(0, trainSet.length)){

//        println(trainSet(j).getAttribList + "\t" + trainSet(j).getGenre)
        //
        forward(trainSet(j).getAttribList)

        //
        backward(trainSet(j).getGenre)

        //
        updateWeights()
//        printlnBPNN()
//        println()

      }



    }
  }

  //
  def printlnBPNN(): Unit ={
    for(i <- Range(0, inputLayerCount)){
      print(inputLayerNeureArray(i).getForwardOutputValue + " ")
    }
    println()

    for(i <- Range(0, hiddenLayerCount)){
      print(hiddenLayerNeureArray(i).getForwardInputValue + " ")
    }
    println()

    for(i <- Range(0, outputLayerCount)){
      print(outputLayerNeureArray(i).getBackwardOutputValue + " ")
    }
    println()
  }

  /**
   * 前向传播
   * */
  def forward(list : ArrayBuffer[Double]): Unit ={
    //输入层
    for(i <- Range(0, list.length))
      inputLayerNeureArray(i).setForwardInputValue(list(i))

    //隐含层
    for(j <- Range(0, hiddenLayerCount)){
      var temp : Double = 0.0
      for(i <- Range(0, inputLayerCount))
        temp += InputHiddenWeight(i)(j) * inputLayerNeureArray(i).getForwardOutputValue
      hiddenLayerNeureArray(j).setForwardInputValue(temp)
    }

    //输出层
    for(k <- Range(0, outputLayerCount)){
      var temp : Double = 0.0
      for(j <- Range(0, hiddenLayerCount))
        temp += HiddenOutputWeight(j)(k) * hiddenLayerNeureArray(j).getForwardOutputValue
      outputLayerNeureArray(k).setForwardInputValue(temp)
    }
  }

  /**
   * 反向传播
   * */
  def backward(genre : Int): Unit ={
//    println("反向传播 ： " + genre)
    // 输出层
    for(i <- Range(0, outputLayerCount)){
      var result = 0
      if(i == genre)
        result = 1
      outputLayerNeureArray(i).setBackwardInputValue(result)
      outputLayerNeureArray(i).setBackwardOutputValue(result)
    }
    //隐含层
    for(i <- Range(0, hiddenLayerCount)){
      var temp : Double = 0.0
      for(j <- Range(0, outputLayerCount)){
        temp += HiddenOutputWeight(i)(j) * outputLayerNeureArray(j).getBackwardOutputValue
      }
      hiddenLayerNeureArray(i).setBackwardInputValue(temp)
      hiddenLayerNeureArray(i).setBackwardOutputValue(temp)
    }
  }

  /**
   * 更新权重，每个权重的梯度都等于与其相连的前一层节点的输出乘以与其相连的后一层的反向传播的输出
   * */
  def updateWeights() : Unit ={
    //输入-隐含
    for(i <- Range(0, inputLayerCount)){
      for(j <- Range(0, hiddenLayerCount)){
        InputHiddenWeight(i)(j) += hiddenLayerNeureArray(j).getBackwardOutputValue *
          inputLayerNeureArray(i).getForwardOutputValue * 0.3
      }
    }

    //隐含-输出
    for(j <- Range(0, hiddenLayerCount)){
      for(k <- Range(0, outputLayerCount)){
        HiddenOutputWeight(j)(k) += outputLayerNeureArray(k).getBackwardOutputValue *
          hiddenLayerNeureArray(j).getForwardOutputValue * 0.3
      }
    }
  }

  def test(trset : TrainDataSet) : Int = {
    forward(trset.getAttribList)

    var result : Int = 0
    var n = 0.0

    for(i <- Range(0, outputLayerCount)){
      val ss = outputLayerNeureArray(i).getForwardOutputValue
      println(ss)
      if(ss > n){
        n = ss
        result = i
      }
    }
    result
  }

}