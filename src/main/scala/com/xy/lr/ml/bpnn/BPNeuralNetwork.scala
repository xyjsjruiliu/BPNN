package com.xy.lr.ml.bpnn

import com.xy.lr.ml.properties.BPNNProperty
import com.xy.lr.ml.util.{NeuronType, TrainDataSet}

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
  private var inputLayerNeuronArray : ArrayBuffer[Neuron] = _
  private var hiddenLayerNeuronArray : ArrayBuffer[Neuron] = _
  private var outputLayerNeuronArray : ArrayBuffer[Neuron] = _

  //连接权重
  private var InputHiddenWeight : Array[Array[Double]] = _
  private var HiddenOutputWeight : Array[Array[Double]] = _

  // 步长
  private var step: Double = _
  // 阈值
  private var threshold: Double = _
  // 误差
  private var Error : Double = _

//  private var lastError : Double = 0.0

  //训练数据集
  private var trainSet : ArrayBuffer[TrainDataSet] = _

  //配置文件
  private var bpnnProperty : BPNNProperty = _

  /**
   * construct fuction
   * */
  def this(inputCount : Int, hiddenCount : Int, outputCount : Int){
    this()

    //创建训练数据集对象
    trainSet = new ArrayBuffer[TrainDataSet]()

    //设置每层神经元个数
    setNeureCount(inputCount, hiddenCount, outputCount)

    //初始化每层神经元和连接权重
    initNeuronAndConnectionWeights()
  }

  //设置 步长
  def setStep(lR: Double) = this.step = lR

  //设置 阈值
  def setThreshold (iF: Double) = this.threshold = iF

  //设置 误差
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
  private def initNeuronAndConnectionWeights(): Unit ={
    init()

    //生成节点,设置阈值
    for(i <- Range(0, inputLayerCount)){
      val temp = new Neuron(NeuronType.TYPE_INPUT)
      //设置阈值
      temp.setThreshold(threshold)
      inputLayerNeuronArray += temp
    }
    for(i <- Range(0, hiddenLayerCount)){
      val temp = new Neuron(NeuronType.TYPE_HIDDEN)
      temp.setThreshold(threshold)
      hiddenLayerNeuronArray += temp
    }
    for(i <- Range(0, outputLayerCount)){
      val temp = new Neuron(NeuronType.TYPE_OUTPUT)
      temp.setThreshold(threshold)
      outputLayerNeuronArray += temp
    }

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
    inputLayerNeuronArray = new ArrayBuffer[Neuron]()
    hiddenLayerNeuronArray = new ArrayBuffer[Neuron]()
    outputLayerNeuronArray = new ArrayBuffer[Neuron]()

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
      var errorAnd : Double = 0.0

      for(j <- Range(0, trainSet.length)){
        // 前向传播
        forward(trainSet(j).getAttribList)
        // 反向传播
        backward(trainSet(j).getGenre)
        // 更新权值
        updateWeights()
        // 更新阈值
        updateThreshold()

        errorAnd = calError(errorAnd, trainSet(j).getGenre)
      }

//      val a = Math.abs(errorAnd - lastError)

//      println(errorAnd)
//      lastError = errorAnd

      if(errorAnd < Error)
        return
    }
  }

  // print the BPNN status
  def printlnBPNN(): Unit ={
    for(i <- Range(0, inputLayerCount)){
      print(inputLayerNeuronArray(i).getForwardOutputValue + " ")
    }
    println()

    for(i <- Range(0, hiddenLayerCount)){
      print(hiddenLayerNeuronArray(i).getForwardInputValue + " ")
    }
    println()

    for(i <- Range(0, outputLayerCount)){
      print(outputLayerNeuronArray(i).getBackwardOutputValue + " ")
    }
    println()
  }

  /**
   * 前向传播
   * */
  def forward(list : ArrayBuffer[Double]): Unit ={
    //输入层
    for(i <- Range(0, list.length))
      inputLayerNeuronArray(i).setForwardInputValue(list(i))

    //隐含层
    for(j <- Range(0, hiddenLayerCount)){
      var temp : Double = 0.0
      for(i <- Range(0, inputLayerCount))
        temp += InputHiddenWeight(i)(j) * inputLayerNeuronArray(i).getForwardOutputValue
      hiddenLayerNeuronArray(j).setForwardInputValue(temp - hiddenLayerNeuronArray(j).getThreshold)
    }

    //输出层
    for(k <- Range(0, outputLayerCount)){
      var temp : Double = 0.0
      for(j <- Range(0, hiddenLayerCount))
        temp += HiddenOutputWeight(j)(k) * hiddenLayerNeuronArray(j).getForwardOutputValue
      outputLayerNeuronArray(k).setForwardInputValue(temp - outputLayerNeuronArray(k).getThreshold)
    }
  }

  def calError(errAnd : Double, genre : Int) : Double = {
    var eA = errAnd
    for(i <- Range(0, outputLayerCount)){
      if(i == genre){
        val tmp = outputLayerNeuronArray(i).getForwardOutputValue - 1.0
        eA += Math.pow(tmp, 2)
      }
    }
    eA
  }

  /**
   * 反向传播
   * */
  def backward(genre : Int): Unit ={
    // 输出层
    for(i <- Range(0, outputLayerCount)){
      var result = 0
      if(i == genre)
        result = 1
      outputLayerNeuronArray(i).setBackwardInputValue(result)
      outputLayerNeuronArray(i).setBackwardOutputValue(result)
    }
    //隐含层
    for(i <- Range(0, hiddenLayerCount)){
      var temp : Double = 0.0
      for(j <- Range(0, outputLayerCount)){
        temp += HiddenOutputWeight(i)(j) * outputLayerNeuronArray(j).getBackwardOutputValue
      }
      hiddenLayerNeuronArray(i).setBackwardInputValue(temp)
      hiddenLayerNeuronArray(i).setBackwardOutputValue(temp)
    }
  }

  /**
   * 更新权重，每个权重的梯度都等于与其相连的前一层节点的输出乘以与其相连的后一层的反向传播的输出
   * */
  def updateWeights() : Unit ={
    //输入-隐含
    for(i <- Range(0, inputLayerCount)){
      for(j <- Range(0, hiddenLayerCount)){
        InputHiddenWeight(i)(j) += hiddenLayerNeuronArray(j).getBackwardOutputValue *
          inputLayerNeuronArray(i).getForwardOutputValue * step
      }
    }

    //隐含-输出
    for(j <- Range(0, hiddenLayerCount)){
      for(k <- Range(0, outputLayerCount)){
        HiddenOutputWeight(j)(k) += outputLayerNeuronArray(k).getBackwardOutputValue *
          hiddenLayerNeuronArray(j).getForwardOutputValue * step
      }
    }
  }

  def updateThreshold() : Unit ={
    //输出
    for(i <- Range(0, outputLayerCount)){
      outputLayerNeuronArray(i).setThreshold(outputLayerNeuronArray(i).getThreshold -
        outputLayerNeuronArray(i).getBackwardOutputValue * step)
    }
    for(i <- Range(0, hiddenLayerCount)){
      hiddenLayerNeuronArray(i).setThreshold(hiddenLayerNeuronArray(i).getThreshold -
        hiddenLayerNeuronArray(i).getBackwardOutputValue * step)
    }
  }



  /**
   * test
   * */
  def test(trset : TrainDataSet) : Int = {
    forward(trset.getAttribList)

    var testType : Int = 0
    var maxOutputValue : Double = 0.0

    for(i <- Range(0, outputLayerCount)){
      val eachOutputValue = outputLayerNeuronArray(i).getForwardOutputValue

      //get the max neuron output
      if(eachOutputValue > maxOutputValue){
        maxOutputValue = eachOutputValue
        testType = i
      }
    }
    testType
  }
}