/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.krintos.krintos.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Stack;

import static android.R.attr.id;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "android_api";

	// Login table name
	private static final String TABLE_USER = "user";
	// Photo table user
	private static final String TABLE_PHOTO = "user_photo";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_PHOTO = "image";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PHONE = "phone";
	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";


	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
				+ KEY_PHONE + " TEXT UNIQUE," + KEY_UID + " TEXT,"
				+ KEY_CREATED_AT + " TEXT"  +")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
		String CREATE_PHOTO_TABLE =  "CREATE TABLE " + TABLE_PHOTO + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_PHONE + " TEXT UNIQUE,"
				+ KEY_PHOTO + " BLOB"  + ")";
        db.execSQL(CREATE_PHOTO_TABLE);
        Log.d(TAG,"Database photo table created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String username,String phone,String uid, String created_at) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, username); // UserName
		values.put(KEY_PHONE, phone); // phone
		values.put(KEY_UID, uid); // Email
		values.put(KEY_CREATED_AT, created_at); // Created At


		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}
	/**
	 *  Storing user photo
	 */
	public void addUserPhoto(String phone, byte[] image) {
		SQLiteDatabase dtbs = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PHONE, phone); // phone
		values.put(KEY_PHOTO, image); // Email

		// Inserting Row
		long id = dtbs.insert(TABLE_PHOTO, null, values);
		dtbs.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}
	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("username", cursor.getString(1));
			user.put("phone", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("created_at", cursor.getString(4));

		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}
	/**
	 * Getting user data from database
	 * */
	public HashMap<String, byte[]> getUserPhoto() {
		HashMap<String, byte[]> userphoto = new HashMap<String, byte[]>();
		String selectQuery = "SELECT  * FROM " + TABLE_PHOTO;

		SQLiteDatabase dtbs = this.getReadableDatabase();
		Cursor cursor = dtbs.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			/*userphoto.put("phone", cursor.getString(1));*/

			userphoto.put("image", cursor.getBlob(2));

		}
		cursor.close();
		dtbs.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + userphoto.toString());

		return userphoto;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
        db.delete(TABLE_PHOTO, null, null);
        db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}
	public  void updateuserphoto(){
		SQLiteDatabase dtbs = this.getWritableDatabase();
		dtbs.delete(TABLE_PHOTO,null,null);
		dtbs.close();
		Log.d(TAG, "Deleted userphoto from sqlite");
	}

}
