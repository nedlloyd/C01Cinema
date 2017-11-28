package sqlitedatabases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sets up SQLite database with columns: screeningId, filmName, time, date 
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
	 * adds to film database 
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
	
		@Override
		ResultSet displayRow(int primaryKey) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT screeningID, filmName, time, date FROM " + tableName + " LIMIT " + 1 + " OFFSET " + (primaryKey - 1) + ";");
			return res;
		}
		
		/**
		 * Input a date and time and this outputs if there is a film on and what it's ID is
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
}
