package com.example.bolutho.Fragments.ModelClasses;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserStories {
    public static final int PICTURE_TYPE = 0;
    public static final int VIDEO_TYPE = 1;
    private int viewType;
    private String id;
    private String profilePicture;
    private String name;
    private String location;
    private String fileType;
    private String date;
    private String userId;
    private String fileUrl;
    private String description;
    private Integer like;
    List<Comment> comments = new ArrayList<>();



    public UserStories(String fileUrl, String userId) {
        this.fileUrl = fileUrl;
        this.userId=userId;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public UserStories(int viewType) {
        this.viewType = viewType;
    }

    public UserStories(String profilePicture, String name, String location, String date
            , Integer like, List<Comment> comments) {
        this.profilePicture = profilePicture;
        this.name = name;
        this.location = location;
        this.date = date;
        this.like = like;
        this.comments = comments;
    }

    public UserStories() {
    }

    public String getUserIdUUID() {
        userId=getID();
        return userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    //User Id
    private String getID() {
        long hi = UUID.randomUUID().getMostSignificantBits();
        long lo = UUID.randomUUID().getLeastSignificantBits();
        byte[] bytes = ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();
        BigInteger big = new BigInteger(bytes);
        String numericUuid = big.toString().replace('-', '1'); // just in case
        return numericUuid;
    }

}



