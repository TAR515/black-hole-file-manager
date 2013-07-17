package ro.edu.ubb.blackhole.dal.localdatabase;

import java.util.ArrayList;
import java.util.List;

import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.datastructures.Server;
import ro.edu.ubb.blackhole.datastructures.guirequest.GuiRequest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This object provides the basic operations for creating and accessing the SQLite database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class SQLiteDatabaseProvider extends SQLiteOpenHelper implements AndroidDatabaseService {

	/**
	 * Version of the SQLite database.
	 */
	private static int DATABASE_VERSION = 2;

	/**
	 * Name of the first column from the data table.
	 */
	private static final String KEY_SERVERNAME = "servername";

	/**
	 * Name of the second column from the data table.
	 */
	private static final String KEY_PASSWORD = "password";

	/**
	 * Name of the third column from the data table.
	 */
	private static final String KEY_IP = "ip";

	/**
	 * Name of the fourth column from the data table.
	 */
	private static final String KEY_PORT = "port";

	/**
	 * Name of the database.
	 */
	private static final String DATABASE_NAME = "BHDatabase";

	/**
	 * Name of the data table.
	 */
	private static final String TABLE_SERVERS = "servers";

	/**
	 * Create database SQL command.
	 */
	private static final String DATABASE_CREATE = "create table if not exists " + TABLE_SERVERS + "("
			+ KEY_SERVERNAME + " VARCHAR primary key, " + KEY_PASSWORD + " VARCHAR, " + KEY_IP + " VARCHAR, "
			+ KEY_PORT + " VARCHAR " + ");";

	/**
	 * Context of the application.
	 */
	private Context applicationContext = null;

	/**
	 * SQLite Database.
	 */
	private SQLiteDatabase sqLiteDatabase = null;

	public SQLiteDatabaseProvider(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		this.applicationContext = ctx;
	}

	/**
	 * @see #onCreate(SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) throws SQLException {
		Log.i("onCreate()");
		try {
			// Creating the database if it not exists yet.
			db.execSQL(DATABASE_CREATE);
		} catch (SQLException e) {
			Log.e("onCreate() - " + e.getMessage());
			throw new SQLException("onCreate() - " + e.getMessage());
		}
	}

	/**
	 * @see #onUpgrade(SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("onUpgrade()");

		// Deleting the old database and creating a new one.
		this.applicationContext.deleteDatabase(DATABASE_NAME);
		onCreate(db);

		// Setting up the database version to the new one.
		SQLiteDatabaseProvider.DATABASE_VERSION = newVersion;
	}

	/**
	 * Opens the database for writing.
	 * 
	 * @return a database provider which is suitable to do database operations.
	 * @throws SQLException
	 *             If the database cannot open for write.
	 */
	public SQLiteDatabaseProvider open() throws SQLException {
		try {
			// Getting the database to writable.
			this.sqLiteDatabase = this.getWritableDatabase();
		} catch (Exception e) {
			Log.e("open() - " + e.getMessage());
			throw new SQLException("open() - " + e.getMessage());
		}

		return this;
	}

	/**
	 * Close any opened databases.
	 */
	public void close() {
		this.sqLiteDatabase.close();
	}

	/**
	 * @see #insertNewServer(GuiRequest)
	 */
	public synchronized long insertNewServer(Server server) throws SQLException {
		long returnValue = -1;

		// Opening the database.
		open();

		// Creating the content values for the insert.
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SERVERNAME, server.getServerName());

		if (server.getPassword() == null) {
			initialValues.putNull(KEY_PASSWORD);
		} else {
			initialValues.put(KEY_PASSWORD, server.getPassword());
		}

		if (server.getIp() == null) {
			initialValues.putNull(KEY_IP);
		} else {
			initialValues.put(KEY_IP, server.getIp());
		}

		if (server.getPort() == -1) {
			initialValues.put(KEY_PORT, -1);
		} else {
			initialValues.put(KEY_PORT, server.getPort());
		}

		// Inserting the new server into the database.
		returnValue = sqLiteDatabase.insert(TABLE_SERVERS, null, initialValues);
		if (returnValue == -1) { // An error occurred with the insert.
			Log.e("insertNewServer() - Insert error");
			throw new SQLException("insertNewServer() - Insert error");
		}

		Log.i("insertNewServer() - New server were inserted: " + server.getServerName());

		// Closing the database.
		close();

		return returnValue;
	}

	/**
	 * @see #getAllServerNames(GuiRequest)
	 */
	public synchronized List<String> getAllServerNames() {
		List<String> allServerNames = new ArrayList<String>();

		open();

		// Getting date from database to cursor.
		Cursor cursor = sqLiteDatabase.query(TABLE_SERVERS, new String[] { KEY_SERVERNAME }, null, null,
				null, null, null);

		// Getting data from cursor and fill it up to response.
		if (cursor.getCount() != 0) { // If there are servers in the database.
			cursor.moveToFirst();
			do {
				allServerNames.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}

		close();
		return allServerNames;
	}

	/**
	 * @see #deleteServer(String)
	 */
	@Override
	public synchronized boolean deleteServer(String serverName) {
		open();

		// Replace the special characters.
		serverName = serverName.replaceAll("'", "''");

		// Deleting the given server from the database.
		int retValue = this.sqLiteDatabase.delete(TABLE_SERVERS, KEY_SERVERNAME + " = '" + serverName + "'",
				null);

		if (retValue <= 0) { // Warning message if the given server were not deleted.
			Log.w("deleteServer() - Server named: " + serverName
					+ " were not deleted or does't exists in the database.");
		} else {
			Log.i("deleteServer() - Server named: " + serverName + "were deleted.");
		}

		close();
		return retValue > 0;
	}

	/**
	 * @see #getServerInformations(GuiRequest)
	 */
	@Override
	public synchronized Server getServerInformations(String serverName) {
		open();

		// Replace the special characters.
		serverName = serverName.replaceAll("'", "''");

		// Getting the data from the database.
		Cursor cursor = this.sqLiteDatabase.query(TABLE_SERVERS, new String[] { KEY_SERVERNAME, KEY_PASSWORD,
				KEY_IP, KEY_PORT }, KEY_SERVERNAME + " = '" + serverName + "'", null, null, null, null);

		if (cursor != null && cursor.getCount() != 0) { // If there are result.
			Server currentServer = null;
			cursor.moveToFirst();

			do { // Getting all the informations about the given server.
				currentServer = new Server();

				currentServer.setServerName(cursor.getString(0));
				currentServer.setPassword(cursor.getString(1));
				currentServer.setIp(cursor.getString(2));
				currentServer.setPort(cursor.getInt(3));
			} while (cursor.moveToNext());

			close();

			return currentServer;
		}

		close();
		// If there are no result, we have to return null.
		return null;
	}

	// BUGG Mielott a serverName-et updateoljuk meg kell nezni, hogy az uj server name-nem-e letezik mar kulonben runtime errort
	// kapunk!!!
	public synchronized boolean updateServer(String oldServerName, Server newServer) {
		open();

		// Setting the content values with the new server informations.
		ContentValues arguments = new ContentValues();
		arguments.put(KEY_SERVERNAME, newServer.getServerName());
		arguments.put(KEY_PASSWORD, newServer.getPassword());
		arguments.put(KEY_IP, newServer.getIp());
		arguments.put(KEY_PORT, newServer.getPort());

		// Replace the special characters.
		oldServerName = oldServerName.replaceAll("'", "''");

		// Update the database.
		int retValue = this.sqLiteDatabase.update(TABLE_SERVERS, arguments, KEY_SERVERNAME + "= '"
				+ oldServerName + "'", null);

		Log.i("updateServer() - Number of rows updated: " + retValue);

		close();
		// Return true if there were updated items or false if not.
		return retValue > 0;
	}
}
