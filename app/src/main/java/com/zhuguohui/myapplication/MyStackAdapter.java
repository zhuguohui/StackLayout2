package com.zhuguohui.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuguohui.myapplication.stack.StackAdapter;

/**
 * <pre>
 * Created by zhuguohui
 * Date: 2023/5/19
 * Time: 15:54
 * Desc:
 * </pre>
 */
public class MyStackAdapter extends StackAdapter<String> {
    int[] colors=new int[]{Color.RED,Color.GREEN,Color.BLUE};

    @Override
    public View createView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.stack_adapter_item, parent, false);
        return view;
    }


    @Override
    public int getVisibleCount() {
        return 6;
    }

    @Override
    protected void onBindDataToView(View view, String s, int position) {
        view.setBackgroundColor(colors[position%colors.length]);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(s);
    }
}
