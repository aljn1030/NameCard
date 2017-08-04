package sg.edu.rp.namecard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VER = 1;

    private static final String DATABASE_NAME = "contacts.db";
    private static final String TABLE_CONTACT = "contact";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_MOBILE = "mobile";
    private static final String COLUMN_COMPANY = "company";
    private static final String COLUMN_EMAIL = "email";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACT +  "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_MOBILE + " TEXT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_COMPANY + " TEXT )";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        Log.i("info" ,"created tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(db);
    }

    public void insertContact(Contact contact){
        if(contact == null){
            return;
        }

        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.name);
        values.put(COLUMN_MOBILE, contact.mobile);
        values.put(COLUMN_EMAIL, contact.email);
        values.put(COLUMN_COMPANY, contact.company);

        db.insert(TABLE_CONTACT, null, values);
        db.close();
    }

    public ArrayList<Contact> getAllContacts() {
        final ArrayList<Contact> tasks = new ArrayList<Contact>();
        final String selectQuery = "SELECT * FROM " + TABLE_CONTACT;

        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                final int id = cursor.getInt(0);
                final String name = cursor.getString(1);
                final String mobile = cursor.getString(2);
                final String email = cursor.getString(3);
                final String company = cursor.getString(4);
                tasks.add(new Contact(id, name, mobile, email, company));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tasks;
    }


    public void clearContacts(){
        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();
            db.delete(TABLE_CONTACT, null, null);
        } catch (final SQLiteException e) {
            e.printStackTrace();
        } finally {
            if(db != null){
                db.close();
            }
        }
    }

    public int deleteById(int id){
        final SQLiteDatabase db = this.getReadableDatabase();

        try{
            return db.delete(TABLE_CONTACT, COLUMN_ID + " = " + id, null);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db != null){
                db.close();
            }
        }

        return -1;
    }

    public void recreateTable(){
        final SQLiteDatabase db = getWritableDatabase();
        try{
            //delete table
            db.execSQL("DROP TABLE " + TABLE_CONTACT + ";");
            //recreate table
            db.execSQL(SQL_CREATE_TABLE);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(db != null){
                db.close();
            }
        }
    }

    public String getContactByName(String name){
        String selectQuery = "SELECT * FROM " + TABLE_CONTACT + " WHERE name = " + name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }

        cursor.close();
        db.close();

        return null;
    }

}
