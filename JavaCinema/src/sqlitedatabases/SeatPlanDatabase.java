package sqlitedatabases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sets up SQLite database with columns: seatID, row varchar(1), column integer
 *
 */
public class SeatPlanDatabase extends SQLiteDatabase {

	private static boolean hasData = false;

	SeatPlanDatabase() {
		super("seatPlan");
		// TODO Auto-generated constructor stub
	}

	@Override
	void initialise() throws SQLException {
		// TODO Auto-generated method stub
		if (!hasData) {
			hasData = true;

			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='"+super.tableName+"' ");
			//working out if there is a table of name seatPlan if there is it's the one we want to use
			if (!res.next()) {
				System.out.println("Building the "+super.tableName+" table with prepopulated values");

				//and so we start to build a table				
				Statement state2 = con.createStatement();
				state2.execute("CREATE TABLE "+super.tableName+"(seatID String, "+
						"row varchar(1),"+ "column integer,"+
						"primary key(seatID));");

				//and then start to insert seats into data
				for(char row = 'a'; row <= 'h'; row++){
					for(int column = 1; column < 11; column++){
						PreparedStatement prep = con.prepareStatement("INSERT INTO "+super.tableName+" values(?,?,?);");
						String rowString = Character.toString(row);
						prep.setString(2, rowString);
						prep.setInt(3, column);
						prep.execute();
					}

				}

			}
		}

	}

	@Override
	ResultSet displayRow(int seatID) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT seatID, row, column FROM " + super.tableName + " LIMIT " + 1 + " OFFSET " + (seatID - 1) + ";");
		return res;
	}

}
