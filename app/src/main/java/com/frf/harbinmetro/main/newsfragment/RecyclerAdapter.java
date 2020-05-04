package com.frf.harbinmetro.main.newsfragment;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.frf.harbinmetro.R;
import com.frf.harbinmetro.main.model.NewsInfo;
import com.frf.harbinmetro.util.Utils;
import com.frf.harbinmetro.web.WebActivity;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AuthorViewHolder> {
    /**
     * 传入的解析后的新闻数据集合
     */
    private ArrayList<NewsInfo> objects;
    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public RecyclerAdapter(ArrayList<NewsInfo> objects) {
        this.objects = objects;
    }

    @Override
    public AuthorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View childView = inflater.inflate(R.layout.fragment_news_tablayout_item, parent, false);
        AuthorViewHolder viewHolder = new AuthorViewHolder(childView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AuthorViewHolder holder, final int position) {
        Glide.with(Utils.getContext()).load(objects.get(position).getPic()).into(holder.mNewsImageView);
        holder.mNickNameView.setText(objects.get(position).getTitle());
        holder.mMottoView.setText(objects.get(position).getTime());
        //方法一：在adapter中设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //可以选择直接在本位置直接写业务处理
//                Toast.makeText(v.getContext(),"点击了新闻",Toast.LENGTH_SHORT).show();
//                //此处回传点击监听事件
//                if(onItemClickListener!=null){
//                    onItemClickListener.OnItemClick(v);
//                }

                Intent intent = new Intent(Utils.getContext(), WebActivity.class);
                //因为用了context调用，必须写上这句；如果是activity调用则不用写
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", objects.get(position).getUrl());
                Utils.getContext().startActivity(intent);
            }
        });
    }

    /**
     * 新闻的数量
     *
     * @return objects
     */
    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置item的监听事件的接口
     */
    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图
         */
        public void OnItemClick(View view);
    }

    static class AuthorViewHolder extends RecyclerView.ViewHolder {

        TextView mNickNameView;
        TextView mMottoView;
        ImageView mNewsImageView;

        public AuthorViewHolder(final View itemView) {
            super(itemView);

            mNickNameView = itemView.findViewById(R.id.tv_nickname);
            mMottoView = itemView.findViewById(R.id.tv_motto);
            mNewsImageView = itemView.findViewById(R.id.iv_portrait);

        }
    }
}
