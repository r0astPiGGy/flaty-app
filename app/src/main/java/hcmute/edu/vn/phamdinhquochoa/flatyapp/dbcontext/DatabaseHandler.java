package hcmute.edu.vn.phamdinhquochoa.flatyapp.dbcontext;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.*;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "home.sqlite11";
    private static final Integer DATABASE_VERSION = 1;
    private static final SQLiteDatabase.CursorFactory DATABASE_FACTORY = null;
    private final Context context;

    // region List Sample Data
    private List<User> userList;
    private List<Region> RegionList;
    private List<RegionSaved> RegionSavedList;
    private List<Flat> FlatList;
    private List<FlatSize> FlatSizeList;
    private List<Notify> notifyList;
    private List<NotifyToUser> notifyToUsers;
//    private List<Order> orderList;
//    private List<OrderDetail> orderDetailList;
    private List<FlatSaved> FlatSavedList;
    // endregion

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, DATABASE_FACTORY, DATABASE_VERSION);
        this.context = context;
    }

    public void queryData(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public Cursor getData(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public Cursor getDataRow(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c;
    }

    @Deprecated
    public static byte[] convertDrawableToByteArray(Drawable drawable){
        return ImageUtils.convertDrawableToByteArray(drawable);
    }

    @Deprecated
    public static Bitmap convertByteArrayToBitmap(byte[] image){
        return ImageUtils.convertByteArrayToBitmap(image);
    }
    // endregion

    private void SampleData(){
        userList = new ArrayList<>();

        // region Region
        RegionList = new ArrayList<>();
        RegionList.add(new Region(1, "Район 1", "г. Минск",
                "96346", convertDrawableToByteArray(ResourcesCompat.getDrawable(context.getResources(), R.drawable.region1, null))));
        RegionList.add(new Region(2, "Район 2", "г. Гродно",
                "78946", convertDrawableToByteArray(ResourcesCompat.getDrawable(context.getResources(), R.drawable.region2, null))));
        RegionList.add(new Region(3, "Район 3", "г. Брест",
                "75412", convertDrawableToByteArray(ResourcesCompat.getDrawable(context.getResources(), R.drawable.region5, null))));
        // endregion

        // region Region saved
        RegionSavedList = new ArrayList<>();
        // endregion

        // region Flat
        FlatList = new ArrayList<>();
        FlatList.add(new Flat(1, "Квартира 1", "Район 1",
                convertDrawableToByteArray(ResourcesCompat.getDrawable(context.getResources(), R.drawable.flat, null)),
                "Описание!", 1));
        FlatList.add(new Flat(2, "Квартира 2", "Район 1",
                convertDrawableToByteArray(ResourcesCompat.getDrawable(context.getResources(), R.drawable.flat1, null)),
                "Описание!", 1));
        FlatList.add(new Flat(3, "Квартира 3", "Район 1",
                convertDrawableToByteArray(ResourcesCompat.getDrawable(context.getResources(), R.drawable.flat2, null)),
                "Описание!", 1));
        FlatList.add(new Flat(4, "Квартира 4", "Район 2",
                convertDrawableToByteArray(ResourcesCompat.getDrawable(context.getResources(), R.drawable.flat3, null)),
                "Описание!", 2));
        FlatList.add(new Flat(5, "Квартира 5", "Район 2",
                convertDrawableToByteArray(ResourcesCompat.getDrawable(context.getResources(), R.drawable.flat4, null)),
                "Описание!", 2));
        FlatList.add(new Flat(6, "Квартира 6", "Район 2",
                convertDrawableToByteArray(ResourcesCompat.getDrawable(context.getResources(), R.drawable.flat5, null)),
                "Описание!", 2));
        FlatList.add(new Flat(7, "Квартира 7", "Район 3",
                convertDrawableToByteArray(ResourcesCompat.getDrawable(context.getResources(), R.drawable.flat7, null)),
                "Описание!", 3));
        FlatList.add(new Flat(8, "Квартира 8", "Район 3",
                convertDrawableToByteArray(ResourcesCompat.getDrawable(context.getResources(), R.drawable.flat8, null)),
                "Описание!", 3));


        // region FlatSize
        FlatSizeList = new ArrayList<>();
        Random random = new Random();
        for(int i = 1; i<= 55; i++){
            FlatSizeList.add(new FlatSize(i, 1, (random.nextInt(20) + 41) * 1000d));
            FlatSizeList.add(new FlatSize(i, 2, (random.nextInt(20) + 21) * 1000d));
            FlatSizeList.add(new FlatSize(i, 3, (random.nextInt(20) + 41) * 1000d));
        }
        // endregion

        // region FlatSaved
        FlatSavedList = new ArrayList<>();


        // region notify
        notifyList = new ArrayList<>();
        notifyList.add(new Notify(1, "Уведомление1!",
                "В 1 районе появились новые квартиры!", "3/12/2022"));
        notifyList.add(new Notify(2, "Уведомление2!",
                "Квартира 5 подешевела!", "4/12/2022"));
        notifyList.add(new Notify(3, "Уведомление3!",
                "Не пропустите обновление перечня квартир!", "12/12/2022"));
        // endregion

        // region notify to user
        notifyToUsers = new ArrayList<>();
        notifyToUsers.add(new NotifyToUser(3, 1));
        notifyToUsers.add(new NotifyToUser(3, 2));
        notifyToUsers.add(new NotifyToUser(3, 3));
        notifyToUsers.add(new NotifyToUser(3, 4));

    }

    private void addSampleData(SQLiteDatabase db) {
        SampleData();

        // Add user
        for (User user : userList) {
            db.execSQL(format("INSERT INTO tblUser VALUES(null, '%s','%s', '%s', '%s', '%s', '%s', '%s')",
                    user.getName(), user.getGender(), user.getDateOfBirth(), user.getPhone(), user.getUsername(), user.getPassword(), user.getRole().name()));
        }

        // Add Region
        for (Region Region : RegionList) {
            String sql = "INSERT INTO tblRegion VALUES(null, ?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.clearBindings();
            statement.bindString(1, Region.getName());
            statement.bindString(2, Region.getAddress());
            statement.bindString(3, Region.getPhone());
            statement.bindBlob(4, Region.getImage());
            statement.executeInsert();
        }

        // Add Region saved
        for(RegionSaved RegionSaved: RegionSavedList){
            db.execSQL("INSERT INTO tblRegionSaved VALUES(" + RegionSaved.getRegionId() + ", " + RegionSaved.getUserId() + ")");
        }

        // Add Flat
        for (Flat Flat : FlatList) {
            String sql = "INSERT INTO tblFlat VALUES (null, ?, ?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.clearBindings();
            statement.bindString(1, Flat.getName());
            statement.bindString(2, Flat.getType());
            statement.bindBlob(3, Flat.getImage());
            statement.bindString(4, Flat.getDescription());
            statement.bindLong(5, Flat.getRegionId());
            statement.executeInsert();
        }

        // Add Flat size
        for (FlatSize FlatSize : FlatSizeList) {
            String sql = "INSERT INTO tblFlatSize VALUES(?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.clearBindings();
            statement.bindLong(1, FlatSize.getFlatId());
            statement.bindLong(2, FlatSize.getSize());
            statement.bindDouble(3, FlatSize.getPrice());
            statement.executeInsert();
        }

        // Add Flat saved
        for (FlatSaved FlatSaved : FlatSavedList) {
            String sql = "INSERT INTO tblFlatSaved VALUES(?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.clearBindings();
            statement.bindLong(1, FlatSaved.getFlatId());
            statement.bindLong(2, FlatSaved.getSize());
            statement.bindLong(3, FlatSaved.getUserId());
            statement.executeInsert();
        }

        // Add notify
        for (Notify notify : notifyList) {
            db.execSQL(format("INSERT INTO tblNotify VALUES(null, '%s', '%s', '%s')",
                    notify.getTitle(), notify.getContent(), notify.getDateMake()));
        }

        // Add notify to user
        for (NotifyToUser notifyToUser : notifyToUsers) {
            db.execSQL("INSERT INTO tblNotifyToUser VALUES('"
                    + notifyToUser.getNotifyId() + "', '"
                    + notifyToUser.getUserId() + "')");
        }


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create table "User"
        String queryCreateUser = "CREATE TABLE IF NOT EXISTS tblUser(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name NVARCHAR(200)," +
                "gender VARCHAR(10)," +
                "date_of_birth VARCHAR(20)," +
                "phone VARCHAR(15)," +
                "username VARCHAR(30)," +
                "password VARCHAR(100)," +
                "role VARCHAR(15))";
        sqLiteDatabase.execSQL(queryCreateUser);

        //Create table "Region"
        String queryCreateRegion = "CREATE TABLE IF NOT EXISTS tblRegion(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name NVARCHAR(200), " +
                "address NVARCHAR(200)," +
                "phone CHAR(10)," +
                "image BLOB)";
        sqLiteDatabase.execSQL(queryCreateRegion);

        //Create table "RegionSaved"
        String queryCreateRegionSaved = "CREATE TABLE IF NOT EXISTS tblRegionSaved(" +
                "Region_id INTEGER, user_id INTEGER," +
                "PRIMARY KEY(Region_id, user_id))";
        sqLiteDatabase.execSQL(queryCreateRegionSaved);

        //Create table "Flat"
        String queryCreateFlat = "CREATE TABLE IF NOT EXISTS tblFlat(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name NVARCHAR(200)," +
                "type NVARCHAR(200)," +
                "image BLOB," +
                "description NVARCHAR(200)," +
                "Region_id INTEGER)";
        sqLiteDatabase.execSQL(queryCreateFlat);

        //Create table "FlatSize"
        String queryCreateFlatSize = "CREATE TABLE IF NOT EXISTS tblFlatSize(" +
                "Flat_id INTEGER," +
                "size INTEGER ," +
                "price DOUBLE," +
                "PRIMARY KEY (Flat_id, size))";
        sqLiteDatabase.execSQL(queryCreateFlatSize);

        //Create table "FlatSaved"
        String queryCreateFlatSaved = "CREATE TABLE IF NOT EXISTS tblFlatSaved(" +
                "Flat_id INTEGER," +
                "size INTEGER ," +
                "user_id INTEGER," +
                "PRIMARY KEY (Flat_id, size, user_id))";
        sqLiteDatabase.execSQL(queryCreateFlatSaved);

        //Create table "Notify"
        String queryCreateNotify = "CREATE TABLE IF NOT EXISTS tblNotify(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title NVARCHAR(200)," +
                "content NVARCHAR(200)," +
                "date_make VARCHAR(20))";
        sqLiteDatabase.execSQL(queryCreateNotify);

        //Create table "NotifyToUser"
        String queryCreateNotifyToUser = "CREATE TABLE IF NOT EXISTS tblNotifyToUser(" +
                "notify_id INTEGER," +
                "user_id INTEGER," +
                "PRIMARY KEY (notify_id, user_id))";
        sqLiteDatabase.execSQL(queryCreateNotifyToUser);

        Log.i("SQLite", "DATABASE CREATED");
        addSampleData(sqLiteDatabase);
        Log.i("SQLite", "ADDED DATA");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("SQLite","Upgrade SQLite");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblNotifyToUser");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblNotify");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblFlatSaved");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblFlatSize");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblFlat");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblRegionSaved");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblRegion");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblUser");

        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}
