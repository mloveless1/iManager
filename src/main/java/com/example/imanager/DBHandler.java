package com.example.imanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHandler extends SQLiteOpenHelper {
    Context context;
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "inventorydb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE1_NAME = "items";
    private static final String TABLE2_NAME = "users";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our item name column
    private static final String NAME_COL = "name";
    private static final String USERNAME_COL = "username";
    private static final String PASSWORD_COL = "password";

    // below variable id for our quantity column.
    private static final String QUANTITY_COL = "quantity";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE1_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + QUANTITY_COL + " INTEGER)";

        String query2 = "CREATE TABLE " + TABLE2_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USERNAME_COL + " TEXT,"
                + PASSWORD_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
        db.execSQL(query2);
    }
    /*****  CRUD FOR Items *****/
    // this method is use to add new course to our sqlite database.
    void addItem(String itemName, int quantity) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, itemName);
        values.put(QUANTITY_COL, quantity);

        // after adding all values we are passing
        // content values to our table.
       long result = db.insert(TABLE1_NAME, null, values);

       if (result == -1){
           Toast.makeText(context, "Failed to add item", Toast.LENGTH_SHORT).show();
       } else{
           Toast.makeText(context, "Added item successfully", Toast.LENGTH_SHORT).show();
       }

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    void addUser(String username, int password) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, password);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE2_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE1_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if( db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    void updateData(String row_id, String itemName, int itemQuantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(NAME_COL, itemName);
        content.put(QUANTITY_COL, itemQuantity);

        long result = db.update(TABLE1_NAME, content, "id=?", new String[]{row_id});

        if (result == 0){
            Toast.makeText(context, "No rows were updated. Check if item with ID: " + row_id + " exists.", Toast.LENGTH_SHORT).show();
        } else if (result == -1){
            Toast.makeText(context, "Failed to update item", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated item successfully.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public void deleteItem(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE1_NAME, "id=?", new String[]{row_id});

        if (result == 0){
            Toast.makeText(context, "No rows were deleted. Check if item with ID: " + row_id + " exists.", Toast.LENGTH_SHORT).show();
        } else if (result == -1){
            Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted item successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /***** Create and Read FOR Users *****/
    void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // Open the database for writing
        ContentValues values = new ContentValues();     // Create a new set of values

        // Put the values into the ContentValues
        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, password);

        // Insert the values into the table
        long result = db.insert(TABLE2_NAME, null, values);

        if (result == -1) {
            // If there's an error inserting the values, show a Toast message
            Toast.makeText(context, "Failed to add user", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, show a success message
            Toast.makeText(context, "Added user successfully", Toast.LENGTH_SHORT).show();
        }

        db.close(); // Close the database
    }
    public boolean userCheck(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for a row with the given username and password
        Cursor cursor = db.query(TABLE2_NAME,
                null,
                USERNAME_COL + " = ? AND " + PASSWORD_COL + " = ?",
                new String[]{username, password},
                null,
                null,
                null);

        int cursorCount = cursor.getCount();
        cursor.close();

        // If count is more than 0, it means such a combination exists
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);

        onCreate(db);
    }
}
