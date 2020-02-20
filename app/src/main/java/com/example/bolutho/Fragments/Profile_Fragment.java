package com.example.bolutho.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolutho.Fragments.ModelClasses.User_Profile;
import com.example.bolutho.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;


public class Profile_Fragment extends Fragment {
    private CircularImageView profile;
    private EditText user_Name, user_Email,user_Phone;
    private FirebaseAuth auth;
    private  String temp;
    private DatabaseReference databaseReference;
//    User_Profile user_profile = new User_Profile();
    private TextView heading;
    private static int SELECT_FILE = 1;
    private static int REQUEST_CAMERA = 2;
    private Button update;
    private List<User_Profile> list = new ArrayList<>();
    private boolean dataLoaded = false;
    private static final String TAG = "Updated";
    private Bitmap bitmap;
    Realm realm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);
        profile = view.findViewById(R.id.profile_picture);
        user_Name = view.findViewById(R.id.user_Name);
        user_Email = view.findViewById(R.id.user_Email);
        user_Phone=view.findViewById(R.id.user_Phone);
        heading=getActivity().findViewById(R.id.title);
        heading.setText("PROFILE");
        Realm.init(getActivity());
        User_Profile user_profile = User_Profile.getInstance();
        profile.setImageBitmap(StringToBitMap(user_profile.getProfile_Picture()));
        user_Name.setText(user_profile.getName());
        user_Email.setText(user_profile.getEmail());
        user_Phone.setText(user_profile.getPhone());
        realm = Realm.getDefaultInstance();
        update = view.findViewById(R.id.btUpdate);
        realm = Realm.getDefaultInstance();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
                Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_LONG).show();

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        return view;
    }

    private void updateUser() {
        dataLoaded = false;
        User_Profile userInformation = new User_Profile();

        final String Name = user_Name.getText().toString().trim();
        userInformation.setName(Name);
        if (TextUtils.isEmpty(Name)) {
            user_Name.setError("Please Enter Your Name");
            user_Name.requestFocus();

            return;
        }
        final String Email = user_Email.getText().toString().trim();
        if (TextUtils.isEmpty(Email)) {
            user_Email.setError("Please Enter Your Email");
            user_Email.requestFocus();

            return;
        } final String Phone=user_Phone.getText().toString().trim();
        if (TextUtils.isEmpty(Phone)){
            user_Phone.setError("Please Enter Your Emergency Number");
            user_Phone.requestFocus();
        }
        else if (TextUtils.isEmpty(Name) && TextUtils.isEmpty(Email) &&TextUtils.isEmpty(Phone) ) {
            Toast.makeText(getActivity(), "Empty Fields Not Allow Please Fill this Fields", Toast.LENGTH_LONG).show();
        } else if (!(TextUtils.isEmpty(Name) && TextUtils.isEmpty(Email))) {
            list.add(userInformation);
            User_Profile user_profile = User_Profile.getInstance();
            String id = user_profile.getUserID() == null ? databaseReference.push().getKey() : user_profile.getUserID();
            user_profile.setUserID(id);
            user_profile.setName(Name);
            user_profile.setPhone(Phone);
            user_profile.setEmail(Email);
        //    user_profile.setProfile_Picture(BitMapToString(bitmap));
            user_profile.save();
            databaseReference.child("Profile").child(id).setValue(user_profile);
        }
    }

    public void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select Picture from gallery",
                "Capture Picture from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePictureFromGallery();
                                break;
                            case 1:
                                takePictureFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void choosePictureFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void takePictureFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profile.setImageBitmap(bitmap);

        String img = BitMapToString(bitmap);

        Log.v(TAG, "base64" + img);//perfectly string base64 to image


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {


        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        profile.setImageBitmap(bitmap);
        String img = BitMapToString(bitmap);

        Log.v(TAG, "base64" + img);

    }
    public String BitMapToString(Bitmap bitmap) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;

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
