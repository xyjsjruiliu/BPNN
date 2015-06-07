package com.jingchen.ann;

import com.jingchen.util.ConsoleHelper;
import com.jingchen.util.DataUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by xylr on 15-6-5.
 * 说明：目前使用的这份测试集是从原始数据中随机抽取26个组成的
 */
public class MainClass {
    public static void main(String[] args) throws Exception {
        if (args.length < 8) {
            System.err.println("Usage: \n\t-train trainfile\n\t-test predictfile\n\t" +
                    "-sep separator, default:','\n\t-eta eta, default:0.5\n\t-iter iternum, default:5000\n\t" +
                    "-learningRate learningRate\n\t" +
                    "-inertiafactor inertiafactor\n\t-out outputfile");
            System.exit(0);
        }
        //命令行参数
        ConsoleHelper helper = new ConsoleHelper(args);
        //训练数据路径
        String trainfile = helper.getArg("-train", "");
        //测试数据路径
        String testfile = helper.getArg("-test", "");
        //文件分割符
        String separator = helper.getArg("-sep", ",");
        //输出文件路径
        String outputfile = helper.getArg("-out", "");
        //学习速率
        Double learningRate = Double.valueOf(helper.getArg("-learningRate", "0.9"));
        //惯性因子
        Double inertiafactor = Double.valueOf(helper.getArg("-inertiafactor", "1.2"));
        //误差值
        float eta = helper.getArg("-eta", 0.5f);
        //迭代次数
        int nIter = helper.getArg("-iter", 5000);

        //数据集
        DataUtil util = DataUtil.getInstance();
        //训练数据集
        List<DataNode> trainList = util.getDataList(trainfile, separator);
        //测试数据集
        List<DataNode> testList = util.getDataList(testfile, separator);

        BufferedWriter output = new BufferedWriter(new FileWriter(new File(
                outputfile)));
        //分类个数（类型个数）
        int typeCount = util.getTypeCount();
        System.out.println(typeCount);

        //神经网络
        AnnClassifier annClassifier = new AnnClassifier(trainList.get(0)
                .getAttribList().size(), 10, typeCount);
        //初始化
        annClassifier.setTrainNodes(trainList);
        //设置学习速率
        annClassifier.setLearningRate(learningRate);
        //设置惯性因子
        annClassifier.setInertiafactor(inertiafactor);
        //训练神经网络
        annClassifier.train(eta, nIter);

        for (int i = 0; i < testList.size(); i++) {
            DataNode test = testList.get(i);
            int type = annClassifier.test(test);
            List<Float> attribs = test.getAttribList();
            for (int n = 0; n < attribs.size(); n++) {
                output.write(attribs.get(n) + ",");
                output.flush();
            }
            output.write(util.getTypeName(type) + "\n");
            output.flush();
        }
        output.close();
    }
}
