package com.example.bolutho.Fragments.ModelClasses.RecyclerView_Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolutho.Fragments.ModelClasses.Comment;
import com.example.bolutho.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class CommentSection extends RecyclerView.Adapter<CommentSection.CommentViewHolder> {
    List<Comment> CommentList;
    public CommentSection(List<Comment> commentList) {
        CommentList = commentList;
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_section, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            holder.profilePic.setImageBitmap(StringToBitMap(CommentList.get(position).getProfilePicture()));
            holder.userName.setText(CommentList.get(position).getUserName());
            holder.Comment.setText(CommentList.get(position).getCommentdes());
    }

    @Override
    public int getItemCount() {
        return CommentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        CircularImageView profilePic;
        TextView userName,Comment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.userPicture);
            userName=itemView.findViewById(R.id.UserName);
            Comment=itemView.findViewById(R.id.userComment);
        }
    }
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


}

