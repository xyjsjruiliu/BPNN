package com.xy.lr.scala.io

import java.io.File

import scala.collection.mutable.ArrayBuffer

/**
 * Created by xylr on 15-6-17.
 * 获取一个目录下的所有文件
 */
class FileList extends java.io.Serializable{
  //目录下的所有文件
  private val ListOfFile : ArrayBuffer[String] = new ArrayBuffer[String]()

  def this(path : String){
    this()
    fileList(path)
  }

  private def fileList(filePath: String) {
    val file: File = new File(filePath)
    val files: Array[File] = file.listFiles
    if (files != null) {
      for (f <- files) {
        if (f.isDirectory) {
          fileList(f.getAbsolutePath)
        }
        else {
          ListOfFile += f.getPath
//          System.out.println(f.getAbsolutePath)
        }
      }
    }
  }

  def getList : ArrayBuffer[String] = {
    this.ListOfFile
  }

}
/*object FileList{
  def main(args : Array[String]): Unit ={
    new FileList().fileList("data")
  }
}*/