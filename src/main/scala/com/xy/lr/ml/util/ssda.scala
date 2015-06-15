package com.xy.lr.ml.util

import java.io.File

import com.xy.lr.ml.main.BPmain

/**
 * Created by xylr on 15-6-8.
 */
object ssda{
  def subdirs(dir: File): Iterator[File] = {
    val children = dir.listFiles.filter(_.isDirectory)
    children.toIterator ++ children.toIterator.flatMap(subdirs _)
  }
  def main(args : Array[String]): Unit ={
    val file = new File("opencv/")

    val filelist = subdirs(file)
    val ss = new BPmain()
    val fea = ss.get("opencv/Indoor_001.jpg")
    println(filelist)
  }
}