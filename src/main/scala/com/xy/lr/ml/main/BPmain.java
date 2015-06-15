package com.xy.lr.ml.main;

import com.xy.lr.ml.util.imageProcess;

/**
 * Created by xylr on 15-6-8.
 */
public class BPmain {
    public static void main(String[] args) {
        // TODO Auto-generated method stub



    }

    public double[] get(String filename){
        double[] feature = new double[30];
        feature = new imageProcess().getFeature(filename);

        for(int i = 0 ; i<feature.length;i++){
            System.out.println(feature[i]);
        }
        return feature;
    }
}
