package org.contextlogger.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "CL_database.db";
	private static final int DATABASE_VERSION = 4;
	
	public DatabaseHelper(Context context, CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override 
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SIGNAL_STRENGTH);
		db.execSQL(CREATE_TABLE_CALL_STATE);
		db.execSQL(CREATE_TABLE_CELL_LOCATION);
		db.execSQL(CREATE_TABLE_CALL_FORWARDING);
		db.execSQL(CREATE_TABLE_DATA_CONNECTION_STATE);
		db.execSQL(CREATE_TABLE_SERVICE_STATE);
		db.execSQL(CREATE_TABLE_WIFI_ONOFF);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_CALL_STATE);
		db.execSQL(DROP_TABLE_SIGNAL_STRENGTH);
//		db.execSQL(DROP_TABLE_CELL_LOCATION);
//		db.execSQL(DROP_TABLE_CALL_FORWARDING);
//		db.execSQL(DROP_TABLE_DATA_CONNECTION_STATE);
//		db.execSQL(DROP_TABLE_SERVICE_STATE);
//		db.execSQL(DROP_TABLE_WIFI_ONOFF);
		onCreate(db);
	}
	
//	SQL for creating tables
	private final String CREATE_TABLE_SIGNAL_STRENGTH = "CREATE TABLE signal_strength (time NUMERIC, gsmSignal NUMERIC, gsmBitErrorRate NUMERIC)";
	private final String CREATE_TABLE_CALL_STATE = "CREATE TABLE call_state (time NUMERIC, state NUMERIC)";
	private final String CREATE_TABLE_CELL_LOCATION = "CREATE TABLE cell_location (time NUMERIC, cellLocation TEXT)";
	private final String CREATE_TABLE_CALL_FORWARDING = "CREATE TABLE call_forwarding (time NUMERIC, forwarding_enabled NUMERIC)";
	private final String CREATE_TABLE_DATA_CONNECTION_STATE = "CREATE TABLE data_connection_state (type NUMERIC, time NUMERIC, state NUMERIC)";
	private final String CREATE_TABLE_SERVICE_STATE = "CREATE TABLE service_state (time NUMERIC, value NUMERIC)";
	private final String CREATE_TABLE_WIFI_ONOFF = "CREATE TABLE wifi_onoff (time NUMERIC, value NUMERIC)";
	
//	SQL for deleting tables
	private final String DROP_TABLE_SIGNAL_STRENGTH = "DROP TABLE signal_strength";
	private final String DROP_TABLE_CALL_STATE = "DROP TABLE call_state";
	private final String DROP_TABLE_CELL_LOCATION = "DROP TABLE cell_location";
	private final String DROP_TABLE_CALL_FORWARDING = "DROP TABLE call_forwarding";
	private final String DROP_TABLE_DATA_CONNECTION_STATE = "DROP TABLE data_connection_state";
	private final String DROP_TABLE_SERVICE_STATE = "DROP TABLE service_state";
	private final String DROP_TABLE_WIFI_ONOFF = "DROP TABLE wifi_onoff";
	
//	table specific data
	public static final String TABLE_NAME_SIGNAL_STRENGTH = "signal_strength";
	public static final String TABLE_NAME_CALL_STATE = "call_state";
	public static final String TABLE_NAME_CELL_LOCATION = "cell_location";
	public static final String TABLE_NAME_CALL_FORWARDING = "call_forwarding";
	public static final String TABLE_NAME_DATA_CONNECTION_STATE = "data_connection_state";
	public static final String TABLE_NAME_SERVICE_STATE = "service_state";
	public static final String TABLE_NAME_WIFI_ONOFF = "wifi_onoff";
	
	public static final String TABLE_SIGNAL_STRENGTH_TIME = "time";
	public static final String TABLE_SIGNAL_STRENGTH_GSMSIGNAL = "gsmSignal";
	public static final String TABLE_SIGNAL_STRENGTH_GSMBITERRORRATE = "gsmBitErrorRate";
	
	public static final String TABLE_CALL_STATE_TIME = "time";
	public static final String TABLE_CALL_STATE_STATE = "state";
	
	public static final String TABLE_CELL_LOCATION_TIME = "time";
	public static final String TABLE_CELL_LOCATION_CELLLOCATION = "cellLocation";
	
	public static final String TABLE_CALL_FORWARDING_TIME = "time";
	public static final String TABLE_CALL_FORWARDING_VALUE = "forwarding_enabled";
	
	public static final String TABLE_DATA_CONNECTION_STATE_TIME = "time";
	public static final String TABLE_DATA_CONNECTION_STATE_STATE = "state";
	public static final String TABLE_DATA_CONNECTION_STATE_NETWORKTYPE = "type";
	
	public static final String TABLE_SERVICE_STATE_TIME = "time";
	public static final String TABLE_SERVICE_STATE_VALUE = "value";
	
	public static final String TABLE_WIFI_ONOFF_TIME = "time";
	public static final String TABLE_WIFI_ONOFF_VALUE = "value";
}
