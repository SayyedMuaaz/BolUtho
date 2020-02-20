
package com.example.bolutho.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bolutho.Article_DetailsActivity;
import com.example.bolutho.Fragments.ModelClasses.User_Profile;
import com.example.bolutho.LoginActivity;
import com.example.bolutho.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class Details_Fragment extends Fragment {
    public CircularImageView profile;
    private boolean dataLoaded = false;
    private TextView userName, viewPicture, complaint, logout, article,heading;
    private FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_, container, false);
        Realm.init(getActivity());
        profile = view.findViewById(R.id.profile_picture);
        userName = view.findViewById(R.id.user_Name);
        viewPicture = view.findViewById(R.id.view_Profile);
        complaint = view.findViewById(R.id.complaint);
        logout = view.findViewById(R.id.logout);
        heading=getActivity().findViewById(R.id.title);
        heading.setText("DETAILS");
        article = view.findViewById(R.id.article);
        User_Profile user_profile = User_Profile.getInstance();
        profile.setImageBitmap(StringToBitMap(user_profile.getProfile_Picture()));
        userName.setText(user_profile.getName());
        auth = FirebaseAuth.getInstance();
        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.fospah.gov.pk/cmis.php"));
                startActivity(intent);
            }
        });
        article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Article_DetailsActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.putExtra("finish", true);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); ;
                startActivity(i);
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                getActivity().finish();
            }
        });
        return view;
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




