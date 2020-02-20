package com.example.bolutho.Fragments.ModelClasses;

public class Comment {
    private String profilePicture,userName,commentDes,postId,userId;

    public Comment(String profilePicture, String userName, String commentDes, String postId,String userId) {
        this.profilePicture = profilePicture;
        this.userName = userName;
        this.commentDes = commentDes;
        this.postId = postId;
        this.userId=userId;
    }

    public Comment() {
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentdes() {
        return commentDes;
    }

    public void setCommentdes(String commentdes) {
        this.commentDes = commentdes;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
