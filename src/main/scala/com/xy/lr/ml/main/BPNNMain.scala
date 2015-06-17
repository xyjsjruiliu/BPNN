package com.xy.lr.ml.main

import com.xy.lr.ann.DataNode
import com.xy.lr.ml.bpnn.BPNeuralNetwork
import com.xy.lr.ml.util.{DataUtil, TrainDataSet}
import com.xy.lr.util.ConsoleHelper
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
    val trainList: ArrayBuffer[TrainDataSet] = DataUtil.getTrainSet("data/train.txt", ",")
    //测试数据集
    val testList: ArrayBuffer[TrainDataSet] = DataUtil.getTrainSet("data/type", ",")

    println("类别个数 : " + DataUtil.getTypesCount + "\t向量的唯数 : " + trainList(0).getAttribList.length)

    val bp = new BPNeuralNetwork(trainList(0).getAttribList.length, 2 , DataUtil.getTypesCount)
    // wu cha
    bp.setError(0.000001)
    // yu zhi
    bp.setThreshold(0.3)
    // bu chang
    bp.setStep(0.3)

    // train data
    bp.setTrainDataSet(trainList)


    bp.train(100000)

    for(i <- Range(0, testList.length)){
      val ss = testList(i)
      val gen = bp.test(ss)

      val attr = ss.getAttribList

//      println("预测类别 : " + gen + "\t\t" + attr + "\t\t" + "实际类别 : " +testList(i).getGenre)
      println("预测类别 : " + DataUtil.getTypeName(gen) + "\t实际类别 : " + DataUtil.getTypeName(testList(i).getGenre))
    }

  }
}