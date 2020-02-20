package com.example.bolutho.Fragments.ModelClasses.RecyclerView_Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolutho.CommentActivity;
import com.example.bolutho.Fragments.ModelClasses.UserStories;
import com.example.bolutho.R;
import com.example.bolutho.callbacks.SharePostCallback;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bolutho.Fragments.ModelClasses.UserStories.PICTURE_TYPE;
import static com.example.bolutho.Fragments.ModelClasses.UserStories.VIDEO_TYPE;

public class UserPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<UserStories> userDetailsPost;
//    private Context context;
    private Activity activity;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    Uri bmpUri;
    SharePostCallback callback;





    public UserPostAdapter(FragmentActivity activity, List<UserStories> userDetailsPost, SharePostCallback callback) {
        this.activity=activity;
        this.userDetailsPost=userDetailsPost;
        this.callback = callback;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == PICTURE_TYPE) {
            View view = LayoutInflater.from(activity).inflate(R.layout.picture_layout, parent, false);
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            return new ImageViewHolder(view);

        } else {
            View view = LayoutInflater.from(activity).inflate(R.layout.video_layout, parent, false);
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            return new VideoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == PICTURE_TYPE) {
            ((ImageViewHolder) holder).setPictureDetails(userDetailsPost.get(position));
        } else {
            ((VideoViewHolder) holder).setVideoDetail(userDetailsPost.get(position));
        }

    }
    @Override
    public int getItemViewType(int position) {
        if (userDetailsPost.get(position).getFileType().equals("jpg")) {
            return PICTURE_TYPE;
        } else {
            return VIDEO_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return userDetailsPost.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        CircularImageView profilePicture;
        TextView userName,userLocation,postDescription,likeCounter,comment;
        ImageView userPost;
        ImageView likeButton,commentIV,share;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture=itemView.findViewById(R.id.userProfile);
            userName=itemView.findViewById(R.id.userName);
            userLocation=itemView.findViewById(R.id.curLocation);
            postDescription=itemView.findViewById(R.id.postDetail);
            userPost=itemView.findViewById(R.id.postPicture);
            likeButton=itemView.findViewById(R.id.like);
            likeCounter=itemView.findViewById(R.id.likeCounter);
            comment=itemView.findViewById(R.id.tvComment);
            commentIV=itemView.findViewById(R.id.comment);
            share=itemView.findViewById(R.id.share);
        }
        public void updateLikesCount(int count) {
            likeCounter=itemView.findViewById(R.id.likeCounter);
            likeCounter.setText( " Likes  " +count  );
        }
        void setPictureDetails(final UserStories story) {
            final String userId=story.getUserId();
            final String postId=story.getId();
            userName.setText(story.getName());
            userLocation.setText("is at- " +story.getLocation());
            postDescription.setText(story.getDescription());
            profilePicture.setImageBitmap(StringToBitMap(story.getProfilePicture()));
              Picasso.get()
                    .load(story.getFileUrl())
                    .placeholder(R.drawable.profile)
                    .into(userPost, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Success","Picture Set");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Success","Picture" + e);
                        }
                    });
            db.collection("Posts/" + postId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshots != null) {
                        if (!documentSnapshots.isEmpty()) {
                            int count = documentSnapshots.size();
                                    updateLikesCount(count);
                        } else {
                            updateLikesCount(0);
                        }
                    }
                }
            });
            db.collection("Posts/" + postId + "/Likes").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        if (documentSnapshot.exists()) {
                            likeButton.setImageDrawable(activity.getDrawable(R.drawable.action_like_accent));
                        } else {
                            likeButton.setImageDrawable(activity.getDrawable(R.drawable.like));
                        }


                    }
                }
            });
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.collection("Posts/" + postId + "/Likes").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.getResult().exists()) {
                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());

                                db.collection("Posts/" + postId + "/Likes").document(userId).set(likesMap);
                            } else {
                                db.collection("Posts/" + postId + "/Likes").document(userId).delete();
                            }
                        }
                    });
                }
            });
            likeCounter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("Posts/" + postId + "/Likes").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.getResult().exists()) {
                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());

                                db.collection("Posts/" + postId + "/Likes").document(userId).set(likesMap);
                            } else {
                                db.collection("Posts/" + postId + "/Likes").document(userId).delete();
                            }
                        }
                    });
                }
            });
            db.collection("Posts/" + postId + "/Likes").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        if (documentSnapshot.exists()) {
                            likeButton.setImageDrawable(activity.getDrawable(R.drawable.action_like_accent));
                        } else {
                            likeButton.setImageDrawable(activity.getDrawable(R.drawable.like));
                        }


                    }
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent commentIntent=new Intent(activity, CommentActivity.class);
                    commentIntent.putExtra("postId",postId);
                    activity.startActivity(commentIntent);
                }
            });
            commentIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent commentIntent=new Intent(activity, CommentActivity.class);
                    commentIntent.putExtra("postId",postId);
                    activity.startActivity(commentIntent);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        int permissionCheck = ContextCompat.checkSelfPermission(context,
//                                Manifest.permission.READ_EXTERNAL_STORAGE);
//
//                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                            Log.e("MainActivity ", "P granted");
//
//                            bmpUri = getLocalBitmapUri(userPost);
//
//                        } else {
//                            ActivityCompat.requestPermissions(context,
//                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                        }
//                    } else {
//                        Log.e("MainActivity", "Lower Than MarshMallow");
//                        bmpUri = getLocalBitmapUri(userPost);
//                    }
                    bmpUri = getLocalBitmapUri(userPost);

                    if (bmpUri != null) {
                        // Construct a ShareIntent with link to image
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        shareIntent.setType("image/*");
                      activity.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    } else {
                        Toast.makeText(activity, "Sharing Failed !!", Toast.LENGTH_SHORT).show();
                    }


                }
            });


        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        CircularImageView profilePicture;
        TextView userName, userLocation, postDescription, likeCounter,comment;
        SimpleExoPlayerView exoPlayerView;
        SimpleExoPlayer exoPlayer;
        ImageView likeButton,commentIV,share;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.userProfile);
            userName = itemView.findViewById(R.id.userName);
            userLocation = itemView.findViewById(R.id.curLocation);
            postDescription = itemView.findViewById(R.id.postDetail);
            exoPlayerView = itemView.findViewById(R.id.postVideo);
            likeCounter = itemView.findViewById(R.id.likeCounter);
            likeButton=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.tvComment);
            commentIV=itemView.findViewById(R.id.comment);
            share=itemView.findViewById(R.id.share);

        }

        public void updateLikesCount(int count) {
            likeCounter = itemView.findViewById(R.id.likeCounter);
            likeCounter.setText( " Likes  " +count);
        }

        void setVideoDetail(final UserStories story) {
            final String userId = story.getUserId();
            final String postId = story.getId();
            userName.setText(story.getName());
            userLocation.setText(story.getLocation());
            postDescription.setText(story.getDescription());
            profilePicture.setImageBitmap(StringToBitMap(story.getProfilePicture()));
            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = ExoPlayerFactory.newSimpleInstance(activity, trackSelector);
                Uri videoURI = Uri.parse(story.getFileUrl());
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
                exoPlayerView.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);
            } catch (
                    Exception e) {
                Log.e("MainAcvtivity", " exoplayer error " + e.toString());
            }
            db.collection("Posts/" + postId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshots != null) {
                        if (!documentSnapshots.isEmpty()) {
                            int count = documentSnapshots.size();
                            updateLikesCount(count);
                        } else {
                            updateLikesCount(0);
                        }
                    }
                }
            });
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.collection("Posts/" + postId + "/Likes").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.getResult().exists()) {
                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());

                                db.collection("Posts/" + postId + "/Likes").document(userId).set(likesMap);
                            } else {
                                db.collection("Posts/" + postId + "/Likes").document(userId).delete();
                            }
                        }
                    });
                }
            });
            likeCounter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("Posts/" + postId + "/Likes").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.getResult().exists()) {
                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());

                                db.collection("Posts/" + postId + "/Likes").document(userId).set(likesMap);
                            } else {
                                db.collection("Posts/" + postId + "/Likes").document(userId).delete();
                            }
                        }
                    });
                }
            });
            db.collection("Posts/" + postId + "/Likes").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        if (documentSnapshot.exists()) {
                            likeButton.setImageDrawable(activity.getDrawable(R.drawable.action_like_accent));
                        } else {
                            likeButton.setImageDrawable(activity.getDrawable(R.drawable.like));
                        }


                    }
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent commentIntent=new Intent(activity, CommentActivity.class);
                    commentIntent.putExtra("postId",postId);
                    activity.startActivity(commentIntent);
                }
            });
            commentIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent commentIntent=new Intent(activity, CommentActivity.class);
                    commentIntent.putExtra("postId",postId);
                    activity.startActivity(commentIntent);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onShareClick(story);
                }
            });
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
    public Uri getLocalBitmapUri(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        Uri bmpUri = null;
        try {
            File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            bmpUri = Uri.fromFile(file);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }



}