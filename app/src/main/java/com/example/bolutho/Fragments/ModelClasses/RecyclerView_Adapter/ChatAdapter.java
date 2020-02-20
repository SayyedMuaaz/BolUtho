package com.example.bolutho.Fragments.ModelClasses.RecyclerView_Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolutho.Fragments.ModelClasses.Chat;
import com.example.bolutho.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    Context mContext;

    public ChatAdapter(Context mContext, List<Chat> mChatList) {
        this.mContext = mContext;
        this.mChatList = mChatList;
    }

    List<Chat> mChatList;
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatadapter,parent,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat message = mChatList .get(position);

        holder.mBubbleTextView.setText(message.getMsg());

        if(message.isMine()){
            holder.mBubbleParentLinearLayout.setGravity(Gravity.RIGHT);
            holder.mBubbleLinearLayout.setBackgroundResource(R.drawable.chat_ui);
        }else{
            holder.mBubbleLinearLayout.setBackgroundResource(R.drawable.chat_ui2);
            holder.mBubbleParentLinearLayout.setGravity(Gravity.LEFT);
        }


    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }
    public void add(Chat chatMessage) {
        mChatList.add(chatMessage);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.bubble_layout_parent)
            LinearLayout mBubbleParentLinearLayout;
            @BindView(R.id.bubble_layout) LinearLayout mBubbleLinearLayout;
            @BindView(R.id.message_body)
            TextView mBubbleTextView;
        public ChatViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);

            }
    }
}
