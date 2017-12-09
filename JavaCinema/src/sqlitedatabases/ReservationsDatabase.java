package sqlitedatabases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sets up SQLite database with columns: reservationID, userID, screeningID, seatID (varchar(3))
 * @author Samuel Bradshaw
 *
 */
public class ReservationsDatabase extends SQLiteDatabase  {
	
	static boolean hasData = false;

	public ReservationsDatabase() {
		super("reservations");
	}

	@Override
	void initialise() throws SQLException {
		// TODO Auto-generated method stub
				if (!hasData) {
					hasData = true;

					Statement state = con.createStatement();
					ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='"+super.tableName +"' ");

					//working out if there is a table of name reservations if there is it's the one we want to use
					if (!res.next()) {
						System.out.println("Building the "+super.tableName+" table with prepopulated values");
						
						//and so we start to build a table				
						Statement state2 = con.createStatement();
						state2.execute("CREATE TABLE "+super.tableName+"(reservationID integer," +
						"userID integer,"+"screeningID integer,"+"seatID varchar(3),"+
								"primary key(reservationID));");
						
					}
				}
		
	}
	
	/**
	 * adds to reservation database 
	 * @param userId
	 * @param screeningId
	 * @param seatId
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void createReservation(int userId, int screeningId, String seatId) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("INSERT INTO "+super.tableName+" values(?,?,?,?);");
		prep.setInt(2, userId);
		prep.setInt(3, screeningId);
		prep.setString(4, seatId);
		prep.execute();
		/*prep.close();
        con.close();*/
	}


	@Override
	public ResultSet displayRow(int reservationID) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}
		
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT " + "reservationID, " + "userID, " + "screeningID,"+
		" seatID," + " FROM " + super.tableName + " LIMIT " + 1 + " OFFSET " + (reservationID - 1) + ";");
		return res;
	}
	
	/**
	 * Returns the rows in the reservations database table for a given screeningID  
	 * @param screeningID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ResultSet displayRows(int screeningID) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}
		
		String sql = "SELECT userID, screeningID, seatID"
				+ " FROM " + super.tableName 
                + " WHERE screeningID = " + screeningID;
		
		
		Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery(sql);
		
		return res;
	}
	
	
	//http://www.sqlitetutorial.net/sqlite-java/delete/
	public void delete(int id) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		String sql = "DELETE FROM reservations WHERE reservationID = ?";

		PreparedStatement pstmt = con.prepareStatement(sql); {

			pstmt.setInt(1, id);
			// execute the delete statement
			pstmt.executeUpdate();

		}
	}


}
