package com.example.bolutho.Fragments.ModelClasses;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User_Profile extends RealmObject {
    @PrimaryKey
    String userID;
    String name;
    String email;
    String phone;
    String profile_Picture;
    public User_Profile(String userID) {

        this.userID = userID;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_Picture() {
        return profile_Picture;
    }

    public void setProfile_Picture(String profile_Picture) {
        this.profile_Picture = profile_Picture;
    }
    public User_Profile() {
        this.name = name = "";
        this.email = email = "";
        this.profile_Picture = "";
        this.phone = phone = "";
    }
    public void save() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.insert(this);
        realm.commitTransaction();
    }
    public static User_Profile getInstance() {
        final long DB_VERSION = 1;
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("BolUtho.realm")
                .schemaVersion(DB_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        User_Profile user_profile = null;
        if (realm.where(User_Profile.class).count() > 0) {
            user_profile = realm.copyFromRealm(realm.where(User_Profile.class).findFirst());
        }
        return user_profile != null ? user_profile : new User_Profile();
    }
    public static User_Profile getProfileByID(String userID) {
        Realm realm = Realm.getDefaultInstance();
        User_Profile user_profile = null;
        if (realm.where(User_Profile.class).count() > 0) {
            user_profile = realm.copyFromRealm(realm.where(User_Profile.class).equalTo("userID", userID).findFirst());
        }
        return user_profile;
    }

}
