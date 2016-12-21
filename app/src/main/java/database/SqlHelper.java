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
                "longitude decimal, "+
                "type TEXT) ";

        // create Pollutions table
        db.execSQL(CREATE_POLLUTION_TABLE);
        addElementsToDatabase(db);
    }

    private void addElementsToDatabase(SQLiteDatabase db) {
//        addAirplaneData(db);
//        addPlants(db);
    }

    private void addAirplaneData(SQLiteDatabase db) {
//         addPollutionSource(db,new PollutionDbItem(46.17655,21.262022,"Arad","airplane"));
//         addPollutionSource(db,new PollutionDbItem(46.521946,26.910278,"Bacau","airplane"));
//         addPollutionSource(db,new PollutionDbItem(47.658389,23.470022,"Tautii Magheraus","airplane"));
//         addPollutionSource(db,new PollutionDbItem(44.503194,26.102111,"Aurel Vlaicu","airplane"));
//         addPollutionSource(db,new PollutionDbItem(44.362222,28.488333,"Mihail Kogalniceanu","airplane"));
//         addPollutionSource(db,new PollutionDbItem(46.785167,23.686167,"Cluj Napoca","airplane"));
//         addPollutionSource(db,new PollutionDbItem(45.42,22.253333,"Caransebes","airplane"));
//         addPollutionSource(db,new PollutionDbItem(44.318139,23.888611,"Craiova","airplane"));
//         addPollutionSource(db,new PollutionDbItem(47.178492,27.620631,"Iasi","airplane"));
//         addPollutionSource(db,new PollutionDbItem(47.025278,21.9025,"Oradea","airplane"));
//         addPollutionSource(db,new PollutionDbItem(44.572161,26.102178,"Henri Coanda","airplane"));
//         addPollutionSource(db,new PollutionDbItem(45.785597,24.091342,"Sibiu","airplane"));
//         addPollutionSource(db,new PollutionDbItem(47.703275,22.8857,"Satu Mare","airplane"));
//         addPollutionSource(db,new PollutionDbItem(47.6875,26.354056,"Stefan Cel Mare","airplane"));
//         addPollutionSource(db,new PollutionDbItem(45.062486,28.714311,"Cataloi","airplane"));
//         addPollutionSource(db,new PollutionDbItem(46.467714,24.412525,"Transilvania Targu Mures","airplane"));
//         addPollutionSource(db,new PollutionDbItem(45.809861,21.337861,"Traian Vuia","airplane"));
//         addPollutionSource(db,new PollutionDbItem(46.3201,24.3157,"Aeroclub Mures","airplane"));
//         addPollutionSource(db,new PollutionDbItem(45.4649,24.053,"Aeroclub Sibiu","airplane"));
//         addPollutionSource(db,new PollutionDbItem(45.4649,24.053,"Aeroclub Sibiu","airplane"));
//         addPollutionSource(db,new PollutionDbItem(46.436,24.4445,"Aerodrom Cioca","airplane"));
//         addPollutionSource(db,new PollutionDbItem(45.471009,21.111967,"Aeroclub Cioca","airplane"));
//         addPollutionSource(db,new PollutionDbItem(45.4153,25.3137,"Aeroclub Ghimbav","airplane"));
//         addPollutionSource(db,new PollutionDbItem(45.5153,22.5813,"Aeroclub Deva","airplane"));
//         addPollutionSource(db,new PollutionDbItem(46.4643,23.4258,"Aeroclub Cluj","airplane"));
//         addPollutionSource(db,new PollutionDbItem(43.9841995,28.6096992,"Tuzla","airplane"));
    }

    public void addPlants(SQLiteDatabase db){
//        addPollutionSource(db,new PollutionDbItem(44.668622, 23.405771,"Turceni ","plant"));
//        addPollutionSource(db,new PollutionDbItem(44.906803, 23.138344,"Rovinari ","plant"));
//        addPollutionSource(db,new PollutionDbItem(45.914004, 22.825234,"Mintia ","plant"));
//        addPollutionSource(db,new PollutionDbItem(44.388562, 23.718002,"Işalniţa ","plant"));
//        addPollutionSource(db,new PollutionDbItem(45.166214, 27.922847,"Brăila ","plant"));
//        addPollutionSource(db,new PollutionDbItem(44.8829, 26.008265,"Brazi ","plant"));
//        addPollutionSource(db,new PollutionDbItem(46.467749, 24.18329,"Ludus-Iernut ","plant"));
//        addPollutionSource(db,new PollutionDbItem(46.250669, 26.833098,"Borzeşti ","plant"));
//        addPollutionSource(db,new PollutionDbItem(44.404645, 26.152332,"Bucharest South ","plant"));
//        addPollutionSource(db,new PollutionDbItem(45.43603, 27.981362,"Galaţi ","plant"));
//        addPollutionSource(db,new PollutionDbItem(44.345251, 23.814647,"Craiova II ","plant"));
//        addPollutionSource(db,new PollutionDbItem(45.364750, 23.260975,"Paroşeni ","plant"));

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
    private static final String KEY_TYPE= "type";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_LAT,KEY_LONG,KEY_TYPE};

    public void addPollutionSource(SQLiteDatabase db, PollutionDbItem item){
        Log.d("addPollutionSource", item.toString());
        // 1. get reference to writable DB

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, item.getTitle()); // get title
        values.put(KEY_LONG, item.getLongitude()); // get author
        values.put(KEY_LAT, item.getLatitude()); // get author
        values.put(KEY_TYPE, item.getType()); // get author

        // 3. insert
        db.insert(TABLE_POLLUTIONS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
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
        dbItem.setType(cursor.getString(4));

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
        SQLiteDatabase db = this.getReadableDatabase();
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
                dbItem.setType(cursor.getString(4));

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
        values.put(KEY_TITLE, dbItem.getTitle()); // get title
        values.put(KEY_LONG, dbItem.getLongitude()); // get author
        values.put(KEY_LAT, dbItem.getLatitude()); // get author
        values.put(KEY_TYPE, dbItem.getType()); // get author

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