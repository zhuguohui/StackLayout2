package com.zhuguohui.myapplication.stack;

import android.view.View;
import android.view.ViewGroup;

import com.zhuguohui.myapplication.TRSAction;
import com.zhuguohui.myapplication.TRSFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * Created by zhuguohui
 * Date: 2023/5/19
 * Time: 15:00
 * Desc:
 * </pre>
 */
public abstract class StackAdapter<T> {
    List<T> data=new ArrayList<>();
    List<TRSAction> dataChangeListeners=new ArrayList<>();
    public  int getCount(){
        return data.size();
    }

    public  int getVisibleCount(){
        return 3;
    }

    public void addData(List<T> list){
        data.clear();
        data.addAll(list);
        notifyDataSetChange();
    }

    public abstract View createView(ViewGroup parent);

    public final void bindDataToView(View view,int position){
        T t = data.get(position);
        onBindDataToView(view,t,position);
    }

    protected abstract void onBindDataToView(View view,T t,int position);



    public  void addDataChangeListener(TRSAction callBack){
        dataChangeListeners.add(callBack);
    }

    public  void removeDataChangeListener(TRSAction callBack){
        dataChangeListeners.add(callBack);
    }

    public  void notifyDataSetChange(){
        for(TRSAction action:dataChangeListeners){
            action.call();
        }
    }
}
