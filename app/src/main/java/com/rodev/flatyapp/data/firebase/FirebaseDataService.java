package com.rodev.flatyapp.data.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import com.rodev.flatyapp.data.AuthData;
import com.rodev.flatyapp.data.DataService;
import com.rodev.flatyapp.data.FavoriteFlatData;
import com.rodev.flatyapp.data.FavoriteRegionData;
import com.rodev.flatyapp.data.FeedbackRequestData;
import com.rodev.flatyapp.data.FlatData;
import com.rodev.flatyapp.data.ImageStorage;
import com.rodev.flatyapp.data.NotificationData;
import com.rodev.flatyapp.data.RegionData;
import com.rodev.flatyapp.data.UserData;

public class FirebaseDataService implements DataService {

    private final FirebaseFirestore database;
    private final FavoriteRegionData favoriteRegionData;
    private final FavoriteFlatData favoriteFlatData;
    private final FlatData flatData;
    private final NotificationData notificationData;
    private final RegionData regionData;
    private final AuthData authData;
    private final UserData userData;
    private final ImageStorage imageStorage;
    private final FeedbackRequestData feedbackRequestData;

    public FirebaseDataService() {
        database = FirebaseFirestore.getInstance();
        favoriteFlatData = new FavoriteFlatDataImpl(this::getDatabase);
        flatData = new FlatDataImpl(this::getDatabase);
        notificationData = new NotificationDataImpl(this::getDatabase);
        regionData = new RegionDataImpl(this::getDatabase);
        favoriteRegionData = new FavoriteRegionDataImpl(this::getDatabase);
        authData = new AuthDataImpl(this::getDatabase);
        userData = new UserDataImpl(this::getDatabase);
        imageStorage = new ImageStorageImpl();
        feedbackRequestData = new FeedbackRequestDataImpl(this::getDatabase);
    }

    public FirebaseFirestore getDatabase() {
        return database;
    }

    @Override
    public FavoriteRegionData getFavoriteRegionData() {
        return favoriteRegionData;
    }

    @Override
    public FavoriteFlatData getFavoriteFlatData() {
        return favoriteFlatData;
    }

    @Override
    public FlatData getFlatData() {
        return flatData;
    }

    @Override
    public NotificationData getNotificationData() {
        return notificationData;
    }

    @Override
    public RegionData getRegionData() {
        return regionData;
    }

    @Override
    public AuthData getAuthData() {
        return authData;
    }

    @Override
    public UserData getUserData() {
        return userData;
    }

    @Override
    public ImageStorage getImageStorage() {
        return imageStorage;
    }

    @Override
    public FeedbackRequestData getFeedbackRequestData() {
        return feedbackRequestData;
    }
}
