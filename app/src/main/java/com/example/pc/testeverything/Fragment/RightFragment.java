package com.example.pc.testeverything.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.testeverything.R;

/**
 * 1. 运行状态
 当一个碎片是可见的，并且它所关联的活动正处于运行状态时，该碎片也处于运行状态。

 2. 暂停状态
 当一个活动进入暂停状态时（由于另一个未占满屏幕的活动被添加到了栈顶），与它相关联的可见碎片就会进入到暂停状态。

 3. 停止状态
 当一个活动进入停止状态时，与它相关联的碎片就会进入到停止状态。或者通过调用FragmentTransaction 的remove()、replace()方法将碎片从活动中移除，
 但有在事务提交之前调用addToBackStack()方法，这时的碎片也会进入到停止状态。总的来说，进入停止状态的碎片对用户来说是完全不可见的，有可能会被系统回收。

 4. 销毁状态
 碎片总是依附于活动而存在的，因此当活动被销毁时，与它相关联的碎片就会进入到销毁状态。或者通过调用FragmentTransaction 的remove()、replace()方
 法将碎片从活动中移除，但在事务提交之前并没有调用addToBackStack()方法，这时的碎片也会进入到销毁状态。
 */

public class RightFragment extends Fragment {

    /**
     * 当碎片和活动建立关联的时候调用。
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 为碎片创建视图（加载布局）时调用。
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.right_fragment, container, false);
        return view;
    }

    /**
     * 确保与碎片相关联的活动一定已经创建完毕的时候调用。
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    /**
     * 当与碎片关联的视图被移除的时候调用。
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 当碎片和活动解除关联的时候调用。
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }
}