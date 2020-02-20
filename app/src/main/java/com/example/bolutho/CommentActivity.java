package com.example.bolutho;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolutho.Fragments.ModelClasses.RecyclerView_Adapter.CommentSection;
import com.example.bolutho.Fragments.ModelClasses.User_Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import com.example.bolutho.Fragments.ModelClasses.Comment;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class CommentActivity extends AppCompatActivity {
    private CircularImageView Profile;
    private ImageView sender, back;
    private EditText comment;
    private RecyclerView recyclerView;
    public FirebaseAuth auth;
    private User_Profile user_profile;
    List<Comment> comments = new ArrayList<>();
    List<Comment> commentList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private String postId;
    Toolbar toolbar;
    private ProgressDialog progressDialog;
    private CommentSection commentSection;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        Realm.init(this);
        user_profile = User_Profile.getInstance();
        recyclerView = findViewById(R.id.comment_List);
        Profile = findViewById(R.id.userProfile);
        sender = findViewById(R.id.sender);
        recyclerView = findViewById(R.id.comment_List);
        comment = findViewById(R.id.commentET);
        toolbar = findViewById(R.id.toolBar);
        back = findViewById(R.id.backPress);
        setSupportActionBar(toolbar);
        Profile.setImageBitmap(StringToBitMap(user_profile.getProfile_Picture()));
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComments();
                Toast.makeText(CommentActivity.this, "posted", Toast.LENGTH_LONG).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getAllPostComments();
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
    private void postComments() {
        Comment postComment = new Comment();
        String commentDetail = comment.getText().toString();
        postComment.setCommentdes(commentDetail);
        if (TextUtils.isEmpty(commentDetail)) {
            comment.setError("Please Enter Your Comment");
            comment.requestFocus();
            return;
        } else if (!(TextUtils.isEmpty(commentDetail))) {
            comments.add(postComment);
            id = databaseReference.push().getKey();
            Comment comment = new Comment();
            comment.setCommentdes(commentDetail);
            comment.setProfilePicture(user_profile.getProfile_Picture());
            comment.setUserId(user_profile.getUserID());
            comment.setPostId(postId);
            comment.setUserName(user_profile.getName());
            databaseReference.child("CommentSection").child(id).setValue(comment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    private void getAllPostComments() {
        progressDialog.show();
        databaseReference.child("CommentSection").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Comment commentDetails = userSnapshot.getValue(Comment.class);
                    if (commentDetails.getPostId().equalsIgnoreCase(postId)) {
                        commentList.add(commentDetails);
                    }
                }
                        progressDialog.dismiss();
                setAdapter(commentList);
                commentSection.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ERROR", " Error" + databaseError);
                progressDialog.dismiss();
            }
        });
    }

    private void setAdapter(List<Comment> postComment) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentSection = new CommentSection(postComment);
        recyclerView.setAdapter(commentSection);
    }



}
