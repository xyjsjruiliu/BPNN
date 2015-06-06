package com.xy.lr.ml.bpnn

/**
 * Created by xylr on 15-6-3.
 * 神经元
 */
class Neure {

  def logS(in : Double) : Double = {
    1 / (1 + Math.exp(-in))
  }

  def tanhS(in : Double) : Double = {
    (Math.exp(in) - Math.exp(-in))/(Math.exp(in) + Math.exp(-in))
  }
}
