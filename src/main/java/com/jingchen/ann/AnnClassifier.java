package com.jingchen.ann;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xylr on 15-6-5.
 * 人工神经网络
 */
public class AnnClassifier {
    //输入层节点数量
    private int mInputCount;
    //隐含层节点数量
    private int mHiddenCount;
    //输出层节点数量
    private int mOutputCount;

    //每层节点
    private List<NetworkNode> mInputNodes;
    private List<NetworkNode> mHiddenNodes;
    private List<NetworkNode> mOutputNodes;

    //连接权值
    private float[][] mInputHiddenWeight;
    private float[][] mHiddenOutputWeight;

    //训练数据集
    private List<DataNode> trainNodes;

    //学习速率
    private Double learningRate;

    //惯性因子
    private Double inertiafactor;

    //设置训练数据集
    public void setTrainNodes(List<DataNode> trainNodes) {
        this.trainNodes = trainNodes;
    }

    //设置学习速率
    public void setLearningRate(Double lR){
        this.learningRate = lR;
    }

    //设置惯性因子
    public void setInertiafactor(Double iF){
        this.inertiafactor = iF;
    }

    /**
     * 构造函数
     * inputCount   输入层节点数量
     * hiddenCount  隐藏层节点数量
     * outputCount  输出层节点数量
     * */
    public AnnClassifier(int inputCount, int hiddenCount, int outputCount) {
        //训练数据集
        trainNodes = new ArrayList<DataNode>();

        //每层节点数量
        mInputCount = inputCount;
        mHiddenCount = hiddenCount;
        mOutputCount = outputCount;

        //每层节点
        mInputNodes = new ArrayList<NetworkNode>();
        mHiddenNodes = new ArrayList<NetworkNode>();
        mOutputNodes = new ArrayList<NetworkNode>();

        //连接权值
        mInputHiddenWeight = new float[inputCount][hiddenCount];
        mHiddenOutputWeight = new float[mHiddenCount][mOutputCount];
    }

    /**
     * 更新权重，每个权重的梯度都等于与其相连的前一层节点的输出乘以与其相连的后一层的反向传播的输出
     */
    private void updateWeights(float eta) {
        // 更新输入层到隐层的权重矩阵
        for (int i = 0; i < mInputCount; i++)
            for (int j = 0; j < mHiddenCount; j++)
                mInputHiddenWeight[i][j] -= eta
                        * mInputNodes.get(i).getForwardOutputValue()
                        * mHiddenNodes.get(j).getBackwardOutputValue();
        // 更新隐层到输出层的权重矩阵
        for (int i = 0; i < mHiddenCount; i++)
            for (int j = 0; j < mOutputCount; j++)
                mHiddenOutputWeight[i][j] -= eta
                        * mHiddenNodes.get(i).getForwardOutputValue()
                        * mOutputNodes.get(j).getBackwardOutputValue();
    }

    /**
     * 前向传播
     */
    private void forward(List<Float> list) {
        // 输入层
        for (int k = 0; k < list.size(); k++)
            mInputNodes.get(k).setForwardInputValue(list.get(k));
        // 隐层
        for (int j = 0; j < mHiddenCount; j++) {
            float temp = 0;
            for (int k = 0; k < mInputCount; k++)
                temp += mInputHiddenWeight[k][j]
                        * mInputNodes.get(k).getForwardOutputValue();
            mHiddenNodes.get(j).setForwardInputValue(temp);
        }
        // 输出层
        for (int j = 0; j < mOutputCount; j++) {
            float temp = 0;
            for (int k = 0; k < mHiddenCount; k++)
                temp += mHiddenOutputWeight[k][j]
                        * mHiddenNodes.get(k).getForwardOutputValue();
            mOutputNodes.get(j).setForwardInputValue(temp);
        }
    }

    /**
     * 反向传播
     */
    private void backward(int type) {
        // 输出层
        for (int j = 0; j < mOutputCount; j++) {
            // 输出层计算误差把误差反向传播，这里-1代表不属于，1代表属于
            float result = -1;
            if (j == type)
                result = 1;
            mOutputNodes.get(j).setBackwardInputValue(
                    mOutputNodes.get(j).getForwardOutputValue() - result);
        }
        // 隐层
        for (int j = 0; j < mHiddenCount; j++) {
            float temp = 0;
            for (int k = 0; k < mOutputCount; k++)
                temp += mHiddenOutputWeight[j][k]
                        * mOutputNodes.get(k).getBackwardOutputValue();
        }
    }

    /**
     * 训练
     * eta 表示误差
     * n 表示迭代的次数
     */
    public void train(float eta, int n) {
        reset();
        //
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < trainNodes.size(); j++) {
                //向前传播
                forward(trainNodes.get(j).getAttribList());
                //反向传播
                backward(trainNodes.get(j).getType());
                //更新权值
                updateWeights(eta);
            }
        }
    }

    /**
     * 初始化
     */
    private void reset() {
        //Removes all of the elements from this list
        mInputNodes.clear();
        mHiddenNodes.clear();
        mOutputNodes.clear();

        //生成节点
        for (int i = 0; i < mInputCount; i++)
            mInputNodes.add(new NetworkNode(NetworkNode.TYPE_INPUT));
        for (int i = 0; i < mHiddenCount; i++)
            mHiddenNodes.add(new NetworkNode(NetworkNode.TYPE_HIDDEN));
        for (int i = 0; i < mOutputCount; i++)
            mOutputNodes.add(new NetworkNode(NetworkNode.TYPE_OUTPUT));

        //初始化权值
        for (int i = 0; i < mInputCount; i++)
            for (int j = 0; j < mHiddenCount; j++)
                mInputHiddenWeight[i][j] = (float) (Math.random() * 0.1);
        for (int i = 0; i < mHiddenCount; i++)
            for (int j = 0; j < mOutputCount; j++)
                mHiddenOutputWeight[i][j] = (float) (Math.random() * 0.1);
    }

    public int test(DataNode dn) {
        forward(dn.getAttribList());
        float result = 2;
        int type = 0;
        // 取最接近1的
        for (int i = 0; i < mOutputCount; i++)
            if ((1 - mOutputNodes.get(i).getForwardOutputValue()) < result) {
                result = 1 - mOutputNodes.get(i).getForwardOutputValue();
                type = i;
            }
        return type;
    }
}
