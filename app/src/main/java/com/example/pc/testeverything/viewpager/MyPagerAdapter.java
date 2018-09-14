package com.example.pc.testeverything.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc.testeverything.R;

import java.util.List;

/**
 * Created by PC on 2018/8/3.
 */

/**
 * viewPager的PagerAdapter适配器
 */
public class MyPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mData;

    public MyPagerAdapter(Context context, List<String> list) {
        mContext = context;
        mData = list;
    }

    /**
     * 返回viewpager要显示几页  获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
     *
     * @return
     */
    @Override
    public int getCount() {
        return mData.size();
    }

    /**
     *  当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.item_base, null);
        //在这里可以做相应的操作
        TextView tv = (TextView) view.findViewById(R.id.tv);
        //数据填充
        tv.setText(mData.get(position));
        //这一步很重要
        container.addView(view);
        return view;
    }

    /**
     * 销毁条目 PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View) object);
    }

    /**
     *  该函数用来判断instantiateItem(ViewGroup, int)
     *   来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        //几乎是固定的写法,
        return view == object;
    }


    /**
     * This method may be called by the ViewPager to obtain a title string to describe the specified page.
     * ViewPager可以调用此方法以获得描述指定页的标题字符串。
     * 目的是展示title上的文字，
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mData.get(position);
    }

    }
