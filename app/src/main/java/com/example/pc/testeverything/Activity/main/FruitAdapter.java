package com.example.pc.testeverything.Activity.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.testeverything.Activity.BraodCastActivity;
import com.example.pc.testeverything.Activity.MainFragmentActivity;
import com.example.pc.testeverything.Activity.OkHttpActivity;
import com.example.pc.testeverything.Activity.PopWindowActivity;
import com.example.pc.testeverything.Activity.PropertyAnimationActivity;
import com.example.pc.testeverything.Activity.QRcodeActivity;
import com.example.pc.testeverything.Activity.QuanJuDialogActivity;
import com.example.pc.testeverything.Activity.SettingsActivity;
import com.example.pc.testeverything.Activity.TestLayoutActivity;
import com.example.pc.testeverything.viewpager.ViewpagerActivity;
import com.example.pc.testeverything.Activity.culActivity;
import com.example.pc.testeverything.R;
import com.example.pc.testeverything.Recyclerview.RecycleViewTwoActivity;
import com.example.pc.testeverything.gridview.GridViewActivity;

import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder>{

    private static final String TAG = "FruitAdapter";

    private Context mContext;

    private List<Fruit> mFruitList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView fruitImage;
        TextView fruitName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            fruitName = (TextView) view.findViewById(R.id.fruit_name);
        }
    }

    public FruitAdapter(List<Fruit> fruitList) {
        mFruitList = fruitList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fruit_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                switch (position){
                    case 0:
                        //二维码
                        Intent intent = new Intent(mContext,  QRcodeActivity.class);
                        mContext.startActivity(intent);

                        break;
                    case 1:
                        //约束布局
                        Intent intent1 = new Intent(mContext,  TestLayoutActivity.class);
                        mContext.startActivity(intent1);
                        break;
                    case 2:
                        //下单
                        Intent intent2 = new Intent(mContext,  OkHttpActivity.class);
                        mContext.startActivity(intent2);
                        break;
                    case 3:
                        //碎片
                        Intent intent3 = new Intent(mContext,  MainFragmentActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    case 4:
                        //计算器
                        Intent intent4 = new Intent(mContext,  culActivity.class);
                        mContext.startActivity(intent4);
                        break;
                    case 5:
                        //添加声音
                        Intent intent5 = new Intent(mContext,  SoundPoolActivity.class);
                        mContext.startActivity(intent5);
                        break;
                    case 6:
                        //设置
                        Intent intent6 = new Intent(mContext, SettingsActivity.class);
                        mContext.startActivity(intent6);
                        break;
                    case 7:
                        //动态广播
                        Intent intent7 = new Intent(mContext, BraodCastActivity.class);
                        mContext.startActivity(intent7);
                        break;
                    case 8:
                        //属性动画研究
                        Intent intent8 = new Intent(mContext, PropertyAnimationActivity.class);
                        mContext.startActivity(intent8);
                        break;
                    case 9:
                        //ViewPager
                        Intent intent9 = new Intent(mContext, ViewpagerActivity.class);
                        mContext.startActivity(intent9);
                        break;
                    case 10:
                        //RecyclerView嵌套RecylerView
                        Intent intent10 = new Intent(mContext, RecycleViewTwoActivity.class);
                        mContext.startActivity(intent10);
                        break;
                    case 11:
                        //网格
                        Intent intent11 = new Intent(mContext, GridViewActivity.class);
                        mContext.startActivity(intent11);
                        break;
                    case 12:
                        //13popwindow
                        Intent intent12 = new Intent(mContext, PopWindowActivity.class);
                        mContext.startActivity(intent12);
                        break;
                    case 13:
                        //13popwindow
                        Intent intent13 = new Intent(mContext, QuanJuDialogActivity.class);
                        mContext.startActivity(intent13);
                        break;
                }
               /* Intent intent = new Intent(mContext, FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME, fruit.getName());
                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID, fruit.getImageId());
                mContext.startActivity(intent);*/
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        holder.fruitName.setText(fruit.getName());
        Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImage);
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

}
