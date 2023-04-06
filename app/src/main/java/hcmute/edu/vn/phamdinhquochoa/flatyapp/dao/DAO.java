package hcmute.edu.vn.phamdinhquochoa.flatyapp.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Calendar;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.*;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dbcontext.DatabaseHandler;

public class DAO {

    DatabaseHandler dbHelper;
    SQLiteDatabase db ;

    public DAO(Context context){
        dbHelper = new DatabaseHandler(context);
        db = dbHelper.getReadableDatabase();
    }

    // region Region
    public Region getRegionInformation(Integer RegionId){
        String query = "SELECT * FROM tblRegion WHERE id=" + RegionId;
        Cursor cursor = dbHelper.getDataRow(query);
        return new Region(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getBlob(4));
    }

    public Region getRegionByName(String RegionName){
        String query = "SELECT * FROM tblRegion WHERE name='" + RegionName + "'";
        Cursor cursor = dbHelper.getDataRow(query);
        return new Region(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getBlob(4));
    }

    public ArrayList<Region> getRegionList(){
        ArrayList<Region> RegionArrayList = new ArrayList<>();
        String query = "SELECT * FROM tblRegion";
        Cursor cursor = dbHelper.getData(query);
        while (cursor.moveToNext()){
            RegionArrayList.add(new Region(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getBlob(4)));
        }
        return RegionArrayList;
    }
    // endregion

    // region RegionSaved
    public boolean addRegionSaved(RegionSaved RegionSaved){
        String query = "INSERT INTO tblRegionSaved VALUES(" + RegionSaved.getRegionId() +
                ", " + RegionSaved.getUserId() + ")";
        try{
            dbHelper.queryData(query);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    public boolean deleteRegionSaved(RegionSaved RegionSaved){
        String query = "DELETE FROM tblRegionSaved WHERE Region_id=" + RegionSaved.getRegionId() +
                " AND user_id=" + RegionSaved.getUserId();
        try{
            dbHelper.queryData(query);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    public ArrayList<RegionSaved> getRegionSavedList(Integer userId){
        ArrayList<RegionSaved> RegionSavedArrayList = new ArrayList<>();
        String query = "SELECT * FROM tblRegionSaved WHERE user_id=" + userId;
        Cursor cursor = dbHelper.getData(query);
        while (cursor.moveToNext()){
            RegionSavedArrayList.add(new RegionSaved(cursor.getInt(0), cursor.getInt(1)));
        }
        return RegionSavedArrayList;
    }
    // endregion

    // region Notify
    public void addNotify(Notify n) {
        String query = "INSERT INTO tblNotify VALUES(null,'" +
                n.getTitle() + "', '" +
                n.getContent() + "', '" +
                n.getDateMake() + "')";
        dbHelper.queryData(query);
    }

    public void addNotifyToUser(NotifyToUser notifyToUser) {
        String query = "INSERT INTO tblNotifyToUser VALUES(" +
                notifyToUser.getNotifyId() + "," +
                notifyToUser.getUserId() + ")";
        dbHelper.queryData(query);
    }

    public Integer getNewestNotifyId(){
        String query = "SELECT * FROM tblNotify";
        Cursor cursor = dbHelper.getData(query);
        cursor.moveToLast();
        return cursor.getInt(0);
    }

    public ArrayList<Notify> getSystemNotify(){
        ArrayList<Notify> notifyArrayList = new ArrayList<>();
        String query = "SELECT * FROM tblNotify WHERE id NOT IN (SELECT notify_id FROM tblNotifyToUser)";
        Cursor cursor = dbHelper.getData(query);
        while(cursor.moveToNext()){
            notifyArrayList.add(new Notify(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }
        return notifyArrayList;
    }

    public ArrayList<Notify> getUserNotify(Integer userId){
        ArrayList<Notify> notifyArrayList = new ArrayList<>();
        String query = "SELECT tblNotify.* FROM tblNotify, tblNotifyToUser " +
                "WHERE tblNotify.id = tblNotifyToUser.notify_id AND tblNotifyToUser.user_id=" + userId;
        Cursor cursor = dbHelper.getData(query);
        while(cursor.moveToNext()){
            notifyArrayList.add(new Notify(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }
        return notifyArrayList;
    }
    // endregion

    // region User
    public void addUser(User user) {
        String query = "INSERT INTO tblUser VALUES(null,'" +
                user.getName() + "', '" +
                user.getGender() + "', '" +
                user.getDateOfBirth() + "', '" +
                user.getPhone() + "', '" +
                user.getUsername() + "', '" +
                user.getPassword() + "', '" +
                user.getRole().name() + "')";
        dbHelper.queryData(query);
    }

    public void updateUser(User user){
        String query = "UPDATE tblUser SET " +
                "name='" + user.getName() + "'," +
                "gender='" + user.getGender() + "'," +
                "date_of_birth='" + user.getDateOfBirth() + "'," +
                "phone='" + user.getPhone() + "'," +
                "password='" + user.getPassword() + "'," +
                "role='" + user.getRole().name() + "' " +
                "WHERE id=" + user.getId();
        dbHelper.queryData(query);
    }

    public Integer getNewestUserId(){
        String query = "SELECT * FROM tblUser";
        Cursor cursor = dbHelper.getData(query);
        cursor.moveToLast();
        return cursor.getInt(0);
    }

    public boolean UserExited(String username) {
        String query = "SELECT * FROM tblUser WHERE username='" + username + "'";
        Cursor cursor = dbHelper.getData(query);
        return cursor.moveToNext();
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        String query = "SELECT * FROM tblUser WHERE username='" + username + "' and password='" + password + "'";
        Cursor cursor = dbHelper.getDataRow(query);

        if (cursor.getCount() > 0) {
            return new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7));
        }
        return null;
    }

    public boolean signIn(User user){
        User existedUser = getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        return existedUser != null;
    }
    // endregion

    // region Flat
    public FlatSize getFlatDefaultSize(Integer FlatId){
        String sql = "SELECT * FROM tblFlatSize WHERE Flat_id=" + FlatId;
        Cursor cursor = dbHelper.getDataRow(sql);
        if (cursor == null)
            return null;
        return new FlatSize(cursor.getInt(0), cursor.getInt(1), cursor.getDouble(2));
    }

    public FlatSize getFlatSize(Integer FlatId, Integer size){
        String sql = "SELECT * FROM tblFlatSize WHERE Flat_id=" + FlatId + " AND size=" + size;
        Cursor cursor = dbHelper.getDataRow(sql);
        if (cursor == null)
            return null;
        return new FlatSize(cursor.getInt(0), cursor.getInt(1), cursor.getDouble(2));
    }

    public ArrayList<FlatSize> getAllFlatSize(Integer FlatId){
        ArrayList<FlatSize> FlatSizeList = new ArrayList<>();
        String sql = "SELECT * FROM tblFlatSize WHERE Flat_id=" + FlatId;
        Cursor cursor = dbHelper.getData(sql);
        while (cursor.moveToNext()){
            FlatSizeList.add(new FlatSize(cursor.getInt(0), cursor.getInt(1), cursor.getDouble(2)));
        }
        return FlatSizeList;
    }

    public void deleteFlatById(Integer id) {
        String query = "DELETE FROM tblFlat WHERE id=" + id;
        dbHelper.queryData(query);
    }

    public void addFlat(Flat flat) {
        String query = "INSERT INTO tblFlat VALUES(null, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(query);
        statement.clearBindings();
        statement.bindString(1, flat.getName());
        statement.bindString(2, flat.getType());
        statement.bindBlob(3, flat.getImage());
        statement.bindString(4, flat.getDescription());
        statement.bindLong(5, flat.getRegionId());
        statement.executeInsert();
    }

    public void updateFlat(Flat flat) {
        String query = "UPDATE tblFlat SET name=?, type=?, image=?, description=?, Region_id=? WHERE id=?";
        SQLiteStatement statement = db.compileStatement(query);
        statement.clearBindings();
        statement.bindString(1, flat.getName());
        statement.bindString(2, flat.getType());
        statement.bindBlob(3, flat.getImage());
        statement.bindString(4, flat.getDescription());
        statement.bindLong(5, flat.getRegionId());
        statement.bindLong(6, flat.getId());
        statement.executeUpdateDelete();
    }

    public Flat getFlatById(Integer id){
        String query = "SELECT * FROM tblFlat WHERE id=" + id;
        Cursor cursor = dbHelper.getDataRow(query);
        return new Flat(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getString(4), cursor.getInt(5));
    }

    public ArrayList<Flat> getFlatByKeyWord(String keyword, Integer RegionId){
        ArrayList<Flat> listFlat = new ArrayList<>();
        String query = "SELECT * FROM tblFlat WHERE name LIKE '%" + keyword + "%'";
        if(RegionId != null){
            query += " AND Region_id=" + RegionId;
        }

        Cursor cursor = dbHelper.getData(query);
        while(cursor.moveToNext()){
            listFlat.add(new Flat(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3),
                    cursor.getString(4),
                    cursor.getInt(5))
            );
        }
        return listFlat;
    }

    public ArrayList<Flat> getFlatByType(String type){
        ArrayList<Flat> listFlat = new ArrayList<>();
        String query = "SELECT * FROM tblFlat WHERE type='" + type + "'";
        Cursor cursor = dbHelper.getData(query);
        while(cursor.moveToNext()){
            listFlat.add(new Flat(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3),
                    cursor.getString(4),
                    cursor.getInt(5))
            );
        }
        return listFlat;
    }

    public ArrayList<Flat> getFlatByRegion(Integer RegionId){
        ArrayList<Flat> listFlat = new ArrayList<>();
        String query = "SELECT * FROM tblFlat WHERE Region_id=" + RegionId;
        Cursor cursor = dbHelper.getData(query);
        while(cursor.moveToNext()){
            listFlat.add(new Flat(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3),
                    cursor.getString(4),
                    cursor.getInt(5))
            );
        }
        return listFlat;
    }
    // endregion

    // region Flat Saved
    public ArrayList<FlatSaved> getFlatSaveList(Integer userId){
        ArrayList<FlatSaved> FlatSavedArrayList = new ArrayList<>();
        String query = "SELECT * FROM tblFlatSaved WHERE user_id=" + userId;
        Cursor cursor = dbHelper.getData(query);
        while (cursor.moveToNext()){
            FlatSavedArrayList.add(new FlatSaved(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)));
        }
        return FlatSavedArrayList;
    }

    public boolean addFlatSaved(FlatSaved FlatSaved){
        String query = "INSERT INTO tblFlatSaved VALUES(" + FlatSaved.getFlatId() + ", "
                + FlatSaved.getSize() + ", "
                + FlatSaved.getUserId() + ")";
        try {
            dbHelper.queryData(query);
            return true;
        } catch (Exception err){
            return false;
        }
    }

    public void deleteFlatSavedByFlatIdAndSize(Integer FlatId, Integer size) {
        String query = "DELETE FROM tblFlatSaved WHERE Flat_id=" +
                FlatId + " and size=" + size;
        dbHelper.queryData(query);
    }
    // endregion

    public String getDate(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return day + "/" + month + "/" + year;
    }
}
