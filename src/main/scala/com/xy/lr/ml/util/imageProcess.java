package com.xy.lr.ml.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xylr on 15-6-8.
 */
public class imageProcess {

    int height,width;//图像长宽
    int[][] image_quan = new int[384][256];//图像HSV颜色空间值矩阵
    int[][] image_gray = new int[384][256];//图像灰度值矩阵
    public class Block{//图像矩阵分块
        int start_x;
        int start_y;
        int end_x;
        int end_y;
    }

    public double[] getFeature(String imagename){//获取图像特征
        getRGB(imagename);//获取图像RGB函数
        double[] feature = new double[30];
        double[] quan = getThreeMoment(image_quan);//颜色特征向量
        double[] gray = getThreeMoment(image_gray);//纹理特征向量

        if(quan.length == gray.length)
            for (int i = 0 ;i < quan.length ; i++){//特征向量二合一
                feature[i] = quan[i];
                feature[quan.length+i] = gray[i];
            }
        else System.out.println("error!");

        double mean = getArrayMean(feature);//得到融合后特征向量均值
        double vari = getArrayVariance(feature, mean);//得到融合后特征向量标准差

        if (vari == 0) {
            for (int i = 0; i < feature.length; i++) { //值在[-1，1]
                feature[i] = feature[i] - mean;
                if (feature[i] > 1) {
                    feature[i] = 1;
                }
                if (feature[i] < -1) {
                    feature[i] = -1;
                }
            }
        } else {
            for (int i = 0; i < feature.length; i++) {
                feature[i] = (feature[i] - mean) / (3.0 * vari);
                if (feature[i] > 1) {
                    feature[i] = 1;
                }
                if (feature[i] < -1) {
                    feature[i] = -1;
                }
            }
        }

        for (int i = 0; i < feature.length; i++) {  //值在[0,1]
            feature[i] = (feature[i] + 1) / 2.0;
        }

        return feature;
    }



    public double[] getThreeMoment (int[][] data){//获取矩阵块的前三阶矩

        /**
         * 一阶矩指的是随机变量的平均值,即期望值
         * 二阶矩指的是随机变量的方差
         * 三阶矩指的是随机变量的偏度
         * 四阶矩指的是随机变量的峰度
         **/

        double[] Feature = new double[15];
        int y = height / 4;
        int x = width / 4;
        Block block = new Block();
        ArrayList<Block> Blo = new ArrayList<Block>();

        block.start_x = 0;
        block.start_y = 0;
        block.end_x = 2*x;
        block.end_y = 2*y;
        Blo.add(block);//块A


        block = new Block();
        block.start_x = 2*x;
        block.start_y = 0;
        block.end_x = 4*x;
        block.end_y = 2*y;
        Blo.add(block);//块B

        block = new Block();
        block.start_x = 0;
        block.start_y = 2*y;
        block.end_x = 2*x;
        block.end_y = 4*y;
        Blo.add(block);//块C

        block = new Block();
        block.start_x = 2*x;
        block.start_y = 2*y;
        block.end_x = 4*x;
        block.end_y = 4*y;
        Blo.add(block);//块D

        block = new Block();
        block.start_x = x;
        block.start_y = y;
        block.end_x = 3*x;
        block.end_y = 3*y;
        Blo.add(block);//块E

        for(int i = 0 ;i < 5; i++){
            double mean = getMean(data, Blo.get(i));//一阶矩
            Feature[i*3 + 0] = mean;
            Feature[i*3 + 1] = getVariance(data, mean, Blo.get(i));//二阶矩
            Feature[i*3 + 2] = getSkewness(data, mean, Blo.get(i));//三阶矩
        }

        return Feature;

    }




    private int[] RGB_TO_HSV(int[] rgb) {//图像由RGB转向HSV

        double h, s, v;
        int H;
        int S;
        int V;
        int r = rgb[0];
        int g = rgb[1];
        int b = rgb[2];

        int[] maxmin = getMaxMin(rgb);
        int max = maxmin[0];//最大值
        int min = maxmin[1];//最小值
        int imax = maxmin[2];//最大值下标
        int imin = maxmin[3];//最小值下标
        if (max == min) {//最大最小值相等
            h = s = 0;
            v = r / 255.0;
        } else {//最大最小值不等
            int _v = max;

            double _r, _g, _b, _h = 0;
            _r = (_v - r) * 1.0 / (_v - min);
            _g = (_v - g) * 1.0 / (_v - min);
            _b = (_v - b) * 1.0 / (_v - min);

            v = max / 255.0;
            s = (_v - min) * 1.0 / _v;

            if (imax == 0) {
                if (imin == 1) {
                    _h = 5 + _b;
                } else {
                    _h = 1 - _g;
                }
            } else if (imax == 1) {
                if (imin == 2) {
                    _h = 1 + _r;
                } else {
                    _h = 3 - _b;
                }
            } else if (imax == 2) {
                if (imin == 0) {
                    _h = 3 + _g;
                } else {
                    _h = 5 - _r;
                }
            }
            h = _h * 60;
        }

        /**将色调H空间分成8份，饱和度S和亮度V空间分别分成3份，根据色彩的不同范围进行量化**/
        if (0 <= v && v < 0.2) {
            V = 0;
        } else if (0.2 <= v && v < 0.7) {
            V = 1;
        } else {
            V = 2;
        }

        if (0 <= s && s < 0.2) {
            S = 0;
        } else if (0.2 <= s && s < 0.7) {
            S = 1;
        } else {
            S = 2;
        }
        if (21 <= h && h <= 40) {
            H = 1;
        } else if (41 <= h && h <= 75) {
            H = 2;
        } else if (76 <= h && h <= 155) {
            H = 3;
        } else if (156 <= h && h <= 190) {
            H = 4;
        } else if (191 <= h && h <= 270) {
            H = 5;
        } else if (271 <= h && h <= 295) {
            H = 6;
        } else if (296 <= h && h <= 315) {
            H = 7;
        } else {
            H = 0;
        }
        return new int[] { H, S, V };
    }

    public void getRGB(String name) {//获取图像RGB值函数体
        int[] rgb = new int[3];//像素点
        File file = new File(name);

        BufferedImage ima = null;

        try {
            ima = ImageIO.read(file);//读取图片
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.width = ima.getWidth();
        this.height = ima.getHeight();


        /**(0xABCDEF &0xFF0000 )>>16
         * (0xABCDEF &0xFF00)>>8
         * 0xABCDEF &0xFF
         * 分别获得原数据的AB、CD、EF不同位置的数据
         * 分别对应RGB三色中的红色R为AB，绿色G为CD，蓝色B为EF**/
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = ima.getRGB(i, j);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);

                int[] HSV = new int[3];
                HSV = RGB_TO_HSV(rgb);
                int l = 9 * HSV[0] + 3 * HSV[1] + HSV[2];//根据量化级数确定系数
                image_quan[i][j] = l;

                int gray  = (rgb[0]*299 + rgb[1]*587 + rgb[2]*114 + 500) / 1000;//灰度的整数运算。加的500是为了实现四舍五入
                image_gray[i][j] = gray;
            }
        }
    }
    /******************************************功能性函数****************************************/

    public int[] getMaxMin(int[] x) {//获取x[ ]最大最小值函数

        int max = x[0];
        int min = x[0];
        int imax = 0;
        int imin = 0;

        for (int i = 1; i < x.length; i++) {

            if (x[i] > max) {
                max = x[i];
                imax = i;
            }
            if (x[i] < min) {
                min = x[i];
                imin = i;
            }
        }
        return new int[] { max, min, imax, imin };
    }

    public double getArrayMean(double[] array) {//获取数组均值

        double mean = 0;
        for (double d : array) {
            mean += d;
        }
        return mean * 1.0 / (array.length);
    }

    public double getArrayVariance(double[] array,double mean) {//获取数组标准差
        double variance = 0;
        for (double d : array) {
            variance += (mean - d) * (mean - d);
        }
        return Math.sqrt(variance * 1.0 / array.length);
    }

    private double getMean(int[][] Data, Block block) {//获取矩阵均值
        double mean = 0;
        for (int i = block.start_x; i < block.end_x; i++) {
            for (int j = block.start_y; j < block.end_y; j++) {
                mean += Data[i][j];
            }
        }
        return mean / ((block.end_y - block.start_y) * (block.end_x - block.start_x) * 1.0);
    }

    private double getVariance(int[][] Data, double mean ,Block block) {//获取矩阵方差

        double variance = 0;
        for (int i = block.start_x; i < block.end_x; i++) {
            for (int j = block.start_y; j < block.end_y; j++)  {
                variance += Math.pow((mean - (double) Data[i][j]),2);
            }
        }
        return Math.sqrt(variance / ((block.end_y - block.start_y) * (block.end_x - block.start_x) * 1.0));
    }

    private double getSkewness(int[][] Data, double mean, Block block) {//获取矩阵偏度

        double variance = 0;
        for (int i = block.start_x; i < block.end_x; i++) {
            for (int j = block.start_y; j < block.end_y; j++) {
                variance += Math.pow((mean - (double) Data[i][j]),3);//3次方
            }
        }
        return Math.cbrt(variance / ((block.end_y - block.start_y) * (block.end_x - block.start_x) * 1.0));//开立方
    }



}