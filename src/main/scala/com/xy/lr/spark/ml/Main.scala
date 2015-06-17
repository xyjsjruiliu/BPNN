package com.xy.lr.spark.ml

import com.xy.lr.ml.util.{DataUtil, TrainDataSet}
import com.xy.lr.spark.ml.bpnn.BPNeuralNetwork
import org.apache.spark.{SparkContext, SparkConf}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by xylr on 15-6-16.
 */
object Main {
  def main(args : Array[String]): Unit ={

    val sparkConf = new SparkConf().setAppName("BPNeuralNetwork").setMaster("local[2]")
    val sparkContext = new SparkContext(sparkConf)

    val trainList: ArrayBuffer[TrainDataSet] = DataUtil.getTrainSet("data/ann/train.txt", ",")

    val bpnn = new BPNeuralNetwork()
    bpnn.setSparkContext(sparkContext)
    bpnn.setTrainDataSet(trainList)
    bpnn.train()
  }
}
