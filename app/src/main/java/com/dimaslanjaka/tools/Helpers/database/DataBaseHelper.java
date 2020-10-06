package com.dimaslanjaka.tools.Helpers.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.*;

public class DataBaseHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static String DATABASE_PATH;
	private static String DATABASE_NAME = "fb.db";
	private final Context myContext;
	private SQLiteDatabase myDataBase;

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
		DATABASE_PATH = new File(context.getFilesDir().getPath(), "databases").toString();
	}

	public DataBaseHelper(Context context, String dbname) {
		super(context, dbname, null, DATABASE_VERSION);
		DATABASE_NAME = dbname;
		this.myContext = context;
		DATABASE_PATH = new File(context.getFilesDir().getPath(), "databases").toString();
	}

	// This method will check if column exists in your table
	public boolean isFieldExist(String tableName, String fieldName) {
		boolean isExist = false;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
		res.moveToFirst();
		do {
			String currentColumn = res.getString(1);
			if (currentColumn.equals(fieldName)) {
				isExist = true;
			}
		} while (res.moveToNext());
		///Log.e("column " + fieldName + " in " + tableName, "is exist > " + isExist);
		res.close();
		return isExist;
	}

	public boolean isTableExists(SQLiteDatabase mDatabase, String tableName, boolean openDb) {
		if (openDb) {
			if (mDatabase == null || !mDatabase.isOpen()) {
				mDatabase = getReadableDatabase();
			}

			if (!mDatabase.isReadOnly()) {
				mDatabase.close();
				mDatabase = getReadableDatabase();
			}
		}

		String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'";
		try (Cursor cursor = mDatabase.rawQuery(query, null)) {
			if (cursor != null) {
				if (cursor.getCount() > 0) {
					return true;
				}
			}
			return false;
		}
	}

	//Create a empty database on the system
	public void createDatabase() {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			//Log.v("DB Exists", "db exists");
			// By calling this method here onUpgrade will be called on a
			// writeable database, but only if the version number has been
			// bumped
			//onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
		}

		boolean dbExist1 = checkDataBase();
		if (!dbExist1) {
			this.getReadableDatabase();
			try {
				this.close();
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}

	}

	//Check database already exist or not
	private boolean checkDataBase() {
		boolean checkDB = false;
		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			File dbfile = new File(myPath);
			checkDB = dbfile.exists();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return checkDB;
	}

	//Copies your database from your local assets-folder to the just created empty database in the system folder
	private void copyDataBase() throws IOException {
		InputStream mInput = myContext.getAssets().open(DATABASE_NAME);
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[2024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	//delete database
	public void db_delete() {
		File file = new File(DATABASE_PATH + DATABASE_NAME);
		if (file.exists()) {
			if (file.delete())
				System.out.println("delete database file.");
		}
	}

	//Open database
	public void openDatabase() throws SQLException {
		String myPath = DATABASE_PATH + DATABASE_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}

	public synchronized void closeDataBase() throws SQLException {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			Log.v("Database Upgrade", "Database version higher than old.");
			db_delete();
		}

	}

}