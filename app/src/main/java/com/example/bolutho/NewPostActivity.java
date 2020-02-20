package com.example.bolutho;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.bolutho.Fragments.Home_Fragment;
import com.example.bolutho.Fragments.ModelClasses.User_Profile;
import com.example.bolutho.Fragments.ModelClasses.UserStories;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class NewPostActivity extends AppCompatActivity {
    private CircularImageView profile;
    private TextView userName, location, gallerySelection, addressPicker, post;
    private ImageView selectedFile, back;
    private EditText description;
    private Bitmap bitmap;
    private String address;
    private User_Profile user_profile;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private String videoPath;
    FirebaseStorage storage;
    private String imageUrl;
    ProgressDialog progressDialog;
    private String fileExt;
    private static int VIDEO = 2;
    private Uri filePath;
    List<UserStories> userStories = new ArrayList<>();
    private static int LOCATION = 1;
    private Context context;
    private String upload;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("UrlFolder");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final Intent intent = getIntent();
        Realm.init(this);
        user_profile = User_Profile.getInstance();
        back = findViewById(R.id.back);
        post = findViewById(R.id.post);
        profile = findViewById(R.id.profile);
        profile.setImageBitmap(StringToBitMap(user_profile.getProfile_Picture()));
        userName = findViewById(R.id.tvuserName);
        userName.setText(user_profile.getName());
        location = findViewById(R.id.tvLocation);
        description = findViewById(R.id.description);
        selectedFile = findViewById(R.id.thumbnail);
        gallerySelection = findViewById(R.id.filePicker);
        addressPicker = findViewById(R.id.map);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        gallerySelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* requestForStoragePermission();*/
                showPictureDialog();
            }

        });
        addressPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent location = new Intent(NewPostActivity.this, MapsActivity.class);
                startActivityForResult(location, LOCATION);
            }
        });
    }

    public void ChooseVideo() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");
        startActivityForResult(pickIntent, VIDEO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LOCATION) {
            address = data.getStringExtra("address");
            location.setText("- At" + address);
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                filePath = result.getUri();
                fileExt = MimeTypeMap.getFileExtensionFromUrl(filePath.toString());
                selectedFile.setImageURI(filePath);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == VIDEO && resultCode == RESULT_OK) {
            onSelectVideoFromGallery(data);
        }

    }


    public void onSelectVideoFromGallery(Intent data) {
        if (data != null) {
            Uri contentURI = data.getData();
            context = this;


            String selectedVideoPath = getPathFromURI(contentURI);
            filePath = Uri.fromFile(new File(selectedVideoPath));
            fileExt = MimeTypeMap.getFileExtensionFromUrl(filePath.toString());
            videoPath = filePath.toString();
            Glide.with(context)
                    .load(filePath)
                    .apply(
                            new RequestOptions()
                                    .error(R.drawable.play)
                                    .centerCrop()
                    )
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.d("TAG","Error" +e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.d("TAG","Success");
                            return false;
                        }
                    })
                    .transition(withCrossFade())
                    .into(selectedFile);
            Log.d("path", selectedVideoPath);
        }
    }

    public String getPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);


        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            if (idx == -1) {
                return uri.getPath();
            }

            String path = cursor.getString(idx);
            cursor.close();
            return path;
        }
    }

    private void onSelectPictureFromGallery(Intent data) {


        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        filePath = Uri.fromFile(new File("filePath"));

        selectedFile.setImageBitmap(bitmap);
        String img = BitMapToString(bitmap);


        Log.v("tag", "base64" + img);

    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
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

    public void newPost(String fileUrl) {
        UserStories user_stories = new UserStories();
        String des = description.getText().toString().trim();
        user_stories.setDescription(des);
        if (TextUtils.isEmpty(des)) {
            description.setError("Please Enter About This Post");
            description.requestFocus();
            return;
        } else if (!(TextUtils.isEmpty(des))) {
            userStories.add(user_stories);
            String id = databaseReference.push().getKey();
            UserStories stories = new UserStories();
            stories.setName(user_profile.getName());
            stories.setProfilePicture(user_profile.getProfile_Picture());
            stories.setDescription(des);
            stories.setLocation(address);
            stories.setFileType(fileExt);
            stories.setUserId(stories.getUserIdUUID());
            stories.setFileUrl(fileUrl);
            stories.setId(id);
            databaseReference.child("Stories").child(id).setValue(stories);

        }
    }
    private void showPictureDialog() {
        android.app.AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog=new AlertDialog.Builder(this,R.style.MyDialogTheme);
        pictureDialog.setTitle(getString(R.string.Action));



        String[] pictureDialogItems = {
                getString(R.string.Picture)
                ,
                getString(R.string.Video)};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                CroppingPicture();
                                break;
                            case 1:
                                ChooseVideo();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    private void CroppingPicture () {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512, 512)
                .setAspectRatio(1, 1)
                .start(NewPostActivity.this);
    }
    private void uploadFile() {
        if (filePath != null) {
          final ProgressDialog  progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            final StorageReference ref  = storageReference.child("Files" + System.currentTimeMillis() + "." + UUID.randomUUID().toString());
            ref.putFile(filePath)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newPost(uri.toString());

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
    }
}
