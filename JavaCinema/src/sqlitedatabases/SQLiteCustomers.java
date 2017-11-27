package sqlitedatabases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteCustomers {
	private static Connection con;
	private static boolean hasData = false;
	
	
	// gets connection to database
	private void getConnection() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:SQLiteTest1.db");
		initialise();
	}

	// based on: https://www.youtube.com/watch?v=JPsWaI5Z3gs
	private void initialise() throws SQLException {
		// TODO Auto-generated method stub
		if (!hasData) {
			hasData = true;
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='seatPlan' ");
			//working out if there is a table of name seatPlan if there is it's the one we want to use
			if (!res.next()) {
				System.out.println("Building the seatPlan table with prepopulated values");
				
				//and so we start to build a table				
				Statement state2 = con.createStatement();
				state2.execute("CREATE TABLE seatPlan(seatID String, + "
						+ "re, primary key(seatID));");
				
			}
		}
	}
	
	//adds to film database 
	public void addScreening(String filmName, String time, String date) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("INSERT INTO seatPlan values(?,?,?,?);");
		prep.setString(2, filmName);
		prep.setString(3, time);
		prep.setString(4, date);
		prep.execute();
		
	}
	
	//deletes from database 
	public void delete(int id) throws SQLException, ClassNotFoundException {
		if (con == null) {
			getConnection();
		}
		
		String sql = "DELETE FROM seatPlan WHERE id = ?";
		PreparedStatement prep = con.prepareStatement(sql);
		
		// set the corresponding param
        prep.setInt(1, id);
        // execute the delete statement
        prep.executeUpdate();
				
    }
	
	
	// methods to return column values 
		public ResultSet displayColumns(String table, String column) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT " + column + " FROM " + table);
			return res;
		}
		
		// overloaded column value method 2 columns
		public ResultSet displayColumns(String table, String column1, String column2) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT " + column1 + ", " + column2 + " FROM " + table);
			return res;
		}

		// overloaded column value method 3 columns
		public ResultSet displayColumns(String table, String column1, String column2, String column3) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}

			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT " + column1 + ", " + column2 + ", " + column3 + " FROM " + table);
			return res;
		}
		
		//displays single row identified by id
		public ResultSet displayRow(String table, int id) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT screeningID, filmName, time, date FROM " + table + " LIMIT " + 1 + " OFFSET " + (id - 1) + ";");
			return res;
		}
		
		// input a date and time and this outputs if there is a film on and what it's ID is
		public ResultSet dateAndTime(String time, String date) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT ScreeningID, filmName, time, date FROM seatPlan WHERE time=\"" + time + "\" AND date=\"" + date + "\";");
			
			return res;
		}
}
