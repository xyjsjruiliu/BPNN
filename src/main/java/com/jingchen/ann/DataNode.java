package com.jingchen.ann;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xylr on 15-6-5.
 * 每一个数据实例
 */
public class DataNode {
    //属性
    private List<Float> mAttribList;
    //类型
    private int type;

    //获取类型
    public int getType() {
        return type;
    }

    //设置类型
    public void setType(int type) {
        this.type = type;
    }

    //获取属性
    public List<Float> getAttribList() {
        return mAttribList;
    }

    //添加属性
    public void addAttrib(Float value) {
        mAttribList.add(value);
    }

    //构造函数
    public DataNode() {
        mAttribList = new ArrayList<Float>();
    }
}
