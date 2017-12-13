package sqlitedatabases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sets up SQLite database with columns: screeningID (int), filmName, time (varchar()), date (varchar()) 
 * @author Ned & Sam
 *
 */
public class ScreeningsDatabase extends SQLiteDatabase {
	
	private static boolean hasData = false;
	
	public ScreeningsDatabase() {
		super("screenings");
	}

	// based on: https://www.youtube.com/watch?v=JPsWaI5Z3gs
	public void initialise() throws SQLException {
		// TODO Auto-generated method stub
		if (!hasData) {
			hasData = true;
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='"+super.tableName+"' ");
			//working out if there is a table of name screenings if there is it's the one we want to use
			if (!res.next()) {
				System.out.println("Building the screenings table with prepopulated values");
				
				//and so we start to build a table				
				Statement state2 = con.createStatement();
				state2.execute("CREATE TABLE "+tableName+"(screeningID integer," +
				"filmName varchar(60)," + "time varchar(60)," + "date varchar(60)," + 
						"primary key(screeningID));");
				
			}
		}
	}
	
	/**
	 * 
	 * Add Screening to table
	 *  
	 * @param filmName
	 * @param time
	 * @param date
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void addScreening(String filmName, String time, String date) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("INSERT INTO screenings values(?,?,?,?);");
		prep.setString(2, filmName);
		prep.setString(3, time);
		prep.setString(4, date);
		prep.execute();
		
	}
		/**
		 * Returns ResultSet of row corresponding to 'primaryKey'
		 * 
		 * @Override
		 * 
		 */
		public ResultSet displayRow(int primaryKey) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT screeningID, filmName, time, date FROM " + tableName +
					" WHERE screeningID=\"" + primaryKey + "\";");
			return res;
		}
		
		/**
		 * 
		 * Returns ResultSet of rows corresponding to 'date'
		 * 
		 * @param date
		 * @return
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public ResultSet date(String date) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT ScreeningID, filmName, time, date FROM " + tableName + " WHERE date=\"" + date + "\";");
			return res;
			
		}
		
		/**
		 * Returns ResultSet of 'ScreeningID', 'filmName' and 'time' corresponding to 'time' and 'date' arguments
		 * 
		 * @param time
		 * @param date
		 * @return
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public ResultSet dateAndTime(String time, String date) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT ScreeningID, filmName, time, date FROM screenings WHERE time=\"" + time + "\" AND date=\"" + date + "\";");
			
			return res;
		}
		
		/**
		 * Returns ResultSet of whole 'ScreeningsDatabse' table
		 * 
		 * @return
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public ResultSet getAll() throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT ScreeningID, filmName, time, date FROM screenings;");
			
			return res;
		}
		
		/**
		 * 
		 * Query for two tables where filmName on one equals film name on the other.  Any rows that satisfy this 
		 * condition are returned as the ResultSet
		 * 
		 * @param table1
		 * @param table2
		 * @param date
		 * @return
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public ResultSet getDataFromTwoTables(String table1, String table2, String date) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}

			PreparedStatement ps = con.prepareStatement("SELECT * FROM " + table1 + ", " + table2 + " WHERE "
					+ table1 + ".filmName=" + table2 + ".filmName AND " + table2 + ".date=\"" + date + "\"" + ";");

			ResultSet set1 = ps.executeQuery();
			
			return set1;
				
		}
		
		/**
		 * 
		 * Query for 'screenings' table and 'films' table where filmName on one equals film name on the other and the 
		 * date on the screenings table is the same as the date given as an argument.  
		 * 
		 * Any rows that satisfy this condition are returned as the ResultSet
		 * 
		 * @param date
		 * @return
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public ResultSet durationAndTime(String date) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			PreparedStatement ps = con.prepareStatement("SELECT * FROM screenings "
					+ "INNER JOIN films ON films.filmName=screenings.filmName "
					+ "WHERE screenings.date='" + date + "';");

			
			ResultSet res = ps.executeQuery();
			return res;
			
		}
}
