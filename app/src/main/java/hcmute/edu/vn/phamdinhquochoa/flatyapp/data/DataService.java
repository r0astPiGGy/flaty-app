package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

public interface DataService {

    // TODO: add cache

    FavoriteRegionData getFavoriteRegionData();

    FavoriteFlatData getFavoriteFlatData();

    FlatData getFlatData();

    NotificationData getNotificationData();

    RegionData getRegionData();

    AuthData getAuthData();

    UserData getUserData();

    ImageStorage getImageStorage();

    FeedbackRequestData getFeedbackRequestData();

}
