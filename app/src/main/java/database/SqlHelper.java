package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class SqlHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PollutionDB";

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create Pollution table
        String CREATE_POLLUTION_TABLE = "CREATE table pollutions ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "latitude decimal, " +
                "longitude decimal)";

        // create Pollutions table
        db.execSQL(CREATE_POLLUTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older Pollutions table if existed
        db.execSQL("DROP TABLE IF EXISTS pollutions");

        // create fresh Pollutions table
        this.onCreate(db);
    }
    // Pollutions table name
    private static final String TABLE_POLLUTIONS = "pollutions";

    // pollutions Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LONG = "longitude";
    private static final String KEY_LAT= "latitude";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_LAT,KEY_LONG};

    public void addPollutionSource(PollutionDbItem item){
        Log.d("addPollutionSource", item.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, item.getTitle()); // get title
        values.put(KEY_LONG, item.getLongitude()); // get author
        values.put(KEY_LAT, item.getLatitude()); // get author
        // 3. insert
        db.insert(TABLE_POLLUTIONS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public PollutionDbItem getPollutionSource(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_POLLUTIONS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build dbItem object
        PollutionDbItem dbItem = new PollutionDbItem();
        dbItem.setId(Integer.parseInt(cursor.getString(0)));
        dbItem.setTitle(cursor.getString(1));
        dbItem.setLatitude(cursor.getDouble(2));
        dbItem.setLongitude(cursor.getDouble(3));

        Log.d("getPollution"+id+")", dbItem.toString());

        // 5. return dbItem
        return dbItem;
    }

    // Get All PollutionDbItem
    public List<PollutionDbItem> getAllPollutionSources() {
        List<PollutionDbItem> dbItems = new LinkedList<PollutionDbItem>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_POLLUTIONS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build dbItem and add it to list
        PollutionDbItem dbItem = null;
        if (cursor.moveToFirst()) {
            do {
                dbItem = new PollutionDbItem();
                dbItem.setId(Integer.parseInt(cursor.getString(0)));
                dbItem.setTitle(cursor.getString(1));
                dbItem.setLatitude(cursor.getDouble(2));
                dbItem.setLongitude(cursor.getDouble(3));

                // Add dbItem to dbItems
                dbItems.add(dbItem);
            } while (cursor.moveToNext());
        }

        Log.d("getAllPollutionSource()", dbItems.toString());

        // return dbItems
        return dbItems;
    }

    // Updating single dbItem
    public int updatePollutionSource(PollutionDbItem dbItem) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", dbItem.getTitle()); // get title
        values.put(KEY_LONG, dbItem.getLongitude()); // get author
        values.put(KEY_LAT, dbItem.getLatitude()); // get author
        // 3. updating row
        int i = db.update(TABLE_POLLUTIONS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(dbItem.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single dbItem
    public void deletePollutionSource(PollutionDbItem dbItem) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. delete
        db.delete(TABLE_POLLUTIONS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(dbItem.getId()) });
        // 3. close
        db.close();
        Log.d("deletePollution", dbItem.toString());
    }
    // Deleting single dbItem
    public void deleteAllPollutionSource() {

        SQLiteDatabase db = this.getWritableDatabase();
        // 2. delete
        db.execSQL("delete from "+ TABLE_POLLUTIONS);
    }
}