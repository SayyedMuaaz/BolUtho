package com.example.bolutho.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolutho.CommentActivity;
import com.example.bolutho.Fragments.ModelClasses.Comment;
import com.example.bolutho.Fragments.ModelClasses.RecyclerView_Adapter.UserPostAdapter;
import com.example.bolutho.Fragments.ModelClasses.UserStories;
import com.example.bolutho.LoginActivity;
import com.example.bolutho.NewPostActivity;
import com.example.bolutho.R;
import com.example.bolutho.callbacks.ShareDataCallBack;
import com.example.bolutho.callbacks.SharePostCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class Home_Fragment extends Fragment implements SharePostCallback {
    private FloatingActionButton fab;
    private RecyclerView postDetailRV;
    private TextView heading;
    public FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private List<UserStories> userPost = new ArrayList<>();
    private ProgressDialog progressDialog;
    UserPostAdapter userPostAdapter;
    static boolean calledAlready = false;
    List<String> unsafeLocation = new ArrayList<>();
    ShareDataCallBack shareDataCallBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_, container, false);
        heading=getActivity().findViewById(R.id.title);
        heading.setText("HOME");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..!");
        progressDialog.setCancelable(false);
        try {
            if (!calledAlready)
            {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                calledAlready = true;
            }

        }catch (Exception e){
            Log.d("LOG","" + e);
        }

        getAllPostDetail();
        firebaseAuth = FirebaseAuth.getInstance();
        printHashKey(getContext());
        fab = view.findViewById(R.id.fab);
        postDetailRV = view.findViewById(R.id.postDetailRV);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        shareDataCallBack = (ShareDataCallBack) getActivity();
    }
    private void getAllPostDetail() {
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Stories");
        mDatabase.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> addressList = new ArrayList<>();
                        userPost.clear();
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            UserStories userDetail = userSnapshot.getValue(UserStories.class);
                            userPost.add( userDetail);
                            addressList.add(userDetail.getLocation());

                        }
                        int count = 0;
                        int max = count;
                        String addressss = "";
                        Set<String> address = new HashSet<>(addressList);
                        for (String key : address) {
                            count = Collections.frequency(addressList, key);
                            if (count >= 1) {
                                unsafeLocation.add(key);
                                shareDataCallBack.ShareData(unsafeLocation);
                            }
                        }
                        progressDialog.dismiss();
                        setAdapter(userPost);
                        userPostAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressDialog.dismiss();
                    }
                });
    }
    private void getUnsafePlaces(String key) {
        for (UserStories item : userPost) {
            if (item.getLocation() == key) {
            }
        }
    }
    private void setAdapter(List<UserStories> userPost) {
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getContext());
        postDetailRV.setLayoutManager(linearLayoutManager);
        if (getActivity() != null) {
            userPostAdapter = new UserPostAdapter(getActivity(), userPost, this);
            postDetailRV.setAdapter(userPostAdapter);
        }
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
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onShareClick(UserStories user_stories) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, user_stories.getFileUrl());
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }
    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("Tag", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("Tag", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("Tag", "printHashKey()", e);
        }

    }



}
