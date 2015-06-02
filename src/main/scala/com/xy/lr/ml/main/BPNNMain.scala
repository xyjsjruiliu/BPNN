package com.xy.lr.ml.main

import com.xy.lr.ml.bpnn.BPNeuralNetwork
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by xylr on 15-6-2.
 * BP 神经网络
 */
object BPNNMain {
  def main(args : Array[String]): Unit ={
    if(args.length != 3){
      System.err.println("Usage : $SPARK_HOME/bin/spark-submit --class com.xy.lr.ml.main.BPNNMain \n" +
        "--master spark://ip:port --executor-memory 2G " +
        "bpnn-1.0-SNAPSHOT.jar PropertiesFilePath")
      System.exit(0)
    }
    val sparkConf = new SparkConf().setAppName(args(0))
    val sparkContext = new SparkContext(sparkConf)

    val bpnn = new BPNeuralNetwork(sparkContext)
  }
}
