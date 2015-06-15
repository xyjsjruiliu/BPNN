package com.xy.lr.ml.main

import com.jingchen.ann.DataNode
import com.jingchen.util.ConsoleHelper
import com.xy.lr.ml.bpnn.BPNeuralNetwork
import com.xy.lr.ml.util.{DataUtil, TrainDataSet}
import org.apache.spark.{SparkContext, SparkConf}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by xylr on 15-6-2.
 * BP 神经网络 主程序
 */
object BPNNMain {

  def main(args : Array[String]): Unit ={
    /*if (args.length < 8) {
      System.err.println("Usage: \n\t-train trainfile\n\t-test predictfile\n\t"
        + "-sep separator, default:','\n\t-eta eta, default:0.5\n\t" +
        "-iter iternum, default:5000\n\t" + "-learningRate learningRate\n\t"
        + "-inertiafactor inertiafactor\n\t-out outputfile")
      System.exit(0)
    }

    //命令行参数
    val helper: ConsoleHelper = new ConsoleHelper(args)
    //训练数据路径
    val trainfile: String = helper.getArg("-train", "")
    //测试数据路径
    val testfile: String = helper.getArg("-test", "")
    //文件分割符
    val separator: String = helper.getArg("-sep", ",")
    //输出文件路径
    val outputfile: String = helper.getArg("-out", "")
    //惯性因子
    val inertiafactor: Double = helper.getArg("-inertiafactor", "1.2").toDouble
    //迭代次数
    val nIter: Int = helper.getArg("-iter", 5000)*/

    //训练数据集
    val trainList: ArrayBuffer[TrainDataSet] = DataUtil.getTrainSet("data/ann/train.txt", ",")
    //测试数据集
    val testList: ArrayBuffer[TrainDataSet] = DataUtil.getTrainSet("data/ann/test.txt", ",")

    println(DataUtil.getTypesCount + "\t" + trainList(0).getAttribList.length)

    val bp = new BPNeuralNetwork(trainList(0).getAttribList.length, 2 , DataUtil.getTypesCount)
    bp.setError(0.0001)
    bp.setThreshold(0.3)
    bp.setLearningRate(0.9)
    bp.setTrainDataSet(trainList)


    bp.train(10000)

    for(i <- Range(0, testList.length)){
      val ss = testList(i)
      val gen = bp.test(ss)

      val attr = ss.getAttribList

//      println("预测类别 : " + gen + "\t\t" + attr + "\t\t" + "实际类别 : " +testList(i).getGenre)
      println(gen + " " + testList(i).getGenre)
    }

  }
  /*def main(args : Array[String]): Unit ={
    if(args.length != 3){
      System.err.println("Usage : $SPARK_HOME/bin/spark-submit --class com.xy.lr.ml.main.BPNNMain \n" +
        "--master spark://ip:port --executor-memory 2G " +
        "bpnn-1.0-SNAPSHOT.jar PropertiesFilePath")
      System.exit(0)
    }
    val sparkConf = new SparkConf().setAppName(args(0))
    val sparkContext = new SparkContext(sparkConf)

//    val bpnn = new BPNeuralNetwork(sparkContext)
  }*/
}
