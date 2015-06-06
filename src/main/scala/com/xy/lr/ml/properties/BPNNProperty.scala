package com.xy.lr.ml.properties

import java.io.{FileNotFoundException, FileInputStream, BufferedInputStream, File}
import java.util.Properties

/**
 * Created by xylr on 15-6-2.
 * 配置文件
 */
class BPNNProperty {
  private var p : Properties = _

  //输入层神经元个数：特征向量的维数
  private var InputLayerNeureNumber : Int = _
  //隐含层神经元个数
  private var HiddenLayerNeureNumber : Int = _
  //输出层神经元个数
  private var OutputLayerNeureNumber : Int = _
  //学习率
  private var LearningRate : Double = _

  def this(path : String){
    this()
    //导入配置文件
    loadPropertiesFile(path)

    this.InputLayerNeureNumber = getProperties(p, "InputLayerNeureNumber").toInt
    this.HiddenLayerNeureNumber = getProperties(p, "HiddenLayerNeureNumber").toInt
    this.OutputLayerNeureNumber = getProperties(p, "OutputLayerNeureNumber").toInt
    this.LearningRate = getProperties(p, "LearningRate").toDouble
  }

  private def loadPropertiesFile(path : String): Unit ={
    val f: File = new File(path)
    p = new Properties()
    var in : BufferedInputStream = null
    try {
      val fis: FileInputStream = new FileInputStream(path)
      in = new BufferedInputStream(fis)
      p.load(in)
    }
    catch {
      case e: FileNotFoundException =>
        System.err.println("BPNN.properties file not found!")
      case _ =>
        System.err.println("load BPNN.properties error")
    }
  }

  private def getProperties(p: Properties, pro: String): String = {
    p.getProperty(pro)
  }

  def getInputLayerNeureNumber : Int = {
    InputLayerNeureNumber
  }

  def getHiddenLayerNeureNumber : Int = {
    HiddenLayerNeureNumber
  }

  def getOutputLayerNeureNumber : Int = {
    OutputLayerNeureNumber
  }

  def getLearningRate : Double = {
    LearningRate
  }

}
