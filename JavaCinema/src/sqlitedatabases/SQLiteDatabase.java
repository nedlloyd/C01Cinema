package sqlitedatabases;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Abstract class and parent class of all SQLite database classes. Contains methods for connecting with the database, and querying the database.
 * Also contains abstract methods which must be implemented by all of its sub-class databases. 
 */
public abstract class SQLiteDatabase {

	protected static Connection con;
	protected String tableName;

	SQLiteDatabase(String tableName){
		this.tableName = tableName;
	}

	/**
	 * Checks to see if database already exists. If it does not exist, this method will create a new 
	 * database. 
	 * @throws SQLException
	 */
	abstract void initialise() throws SQLException;

	/**
	 * returns a ResultSet of a given row 
	 * @param primaryKey
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	abstract ResultSet displayRow(int primaryKey) throws ClassNotFoundException, SQLException;

	/**
	 * Creates a connection with the sqlite database
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		String os = System.getProperty("os.name");
		con = DriverManager.getConnection("jdbc:sqlite:C:SQLiteTest1.db");
		initialise();
	}


	/**
	 * Deletes row corresponding to 'primaryKey' from database 
	 *  
	 * @param primarykey - the row we want to delete
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void delete(int primarykey) throws SQLException, ClassNotFoundException {
		if (con == null) {
			getConnection();
		}

		String sql = "DELETE FROM "+tableName+" WHERE id = ?";
		PreparedStatement prep = con.prepareStatement(sql);

		// set the corresponding param
		prep.setInt(1, primarykey);
		// execute the delete statement
		prep.executeUpdate();

	}

	/**
	 * Adds new column to database 
	 * @param columnName
	 * @param type
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void addColumn(String columnName, String type) throws SQLException, ClassNotFoundException {
		if (con == null) {
			getConnection();
		}

		String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + type + ";";
		PreparedStatement prep = con.prepareStatement(sql);

		prep.executeUpdate();
		prep.close();
		con.close();

	}

	/**
	 *  methods to return all column values based on column name given as String 
	 */
	public ResultSet displayColumns(String column) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT " + column + " FROM " + tableName);
		return res;
	}

	/**
	 * overloaded displayColumns method
	 * 
	 * ResultSet returned as all rows corresponding to column names given  
	 * 
	 * @param column1
	 * @param column2
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ResultSet displayColumns(String column1, String column2) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT " + column1 + ", " + column2 + " FROM " + tableName);
		return res;
	}

	/**
	 * 
	 * overloaded displayColumns method
	 * 
	 * ResultSet returned as all rows corresponding to column names given  
	 * 
	 * @param column1
	 * @param column2
	 * @param column3
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ResultSet displayColumns(String column1, String column2, String column3) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT " + column1 + ", " + column2 + ", " + column3 + " FROM " + tableName);
		return res;
	}

	/**
	 * Returns number of rows in a ResultSet
	 * @param res
	 * @return
	 * @throws SQLException
	 */
	public int countRowsInResultSet(ResultSet res) throws SQLException{
		int rowcount = 0;
		while(res.next()){
			rowcount = res.getRow();
		}
		return rowcount;

	}

	/**
	 * 
	 * Query for films, screenings and reservations tables where 'userID' and 'date' are the same as the userID 
	 * and date given as arguments.
	 * 
	 * All rows meeting the conditions Retruned as ResultSet
	 * 
	 * @param username
	 * @param date
	 * @param userID
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ResultSet queryForProfile(String username, String date, int userID) throws SQLException, ClassNotFoundException {
		if (con == null) {
			getConnection();
		}

		PreparedStatement ps = con.prepareStatement("SELECT * FROM films "
				+ "INNER JOIN screenings ON films.filmName=screenings.filmName "
				+ "INNER JOIN reservations ON reservations.screeningID=screenings.screeningID "
				+ "WHERE reservations.userID=" + userID + " AND screenings.date='" + date + "';");

		ResultSet set1 = ps.executeQuery();

		return set1;
	}

	/**
	 * 
	 * Query for films, screenings and reservations tables where 'userID' is the same as the userID 
	 * and given as an argument.
	 * 
	 * All rows meeting the conditions Retruned as ResultSet
	 * 
	 * @param userID
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ResultSet queryForProfile(int userID) throws SQLException, ClassNotFoundException {
		if (con == null) {
			getConnection();
		}

		PreparedStatement ps = con.prepareStatement("SELECT * FROM films "
				+ "INNER JOIN screenings ON films.filmName=screenings.filmName "
				+ "INNER JOIN reservations ON reservations.screeningID=screenings.screeningID "
				+ "WHERE reservations.userID=" + userID + ";");

		ResultSet set1 = ps.executeQuery();

		return set1;
	}



}
