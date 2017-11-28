package sqlitedatabases;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeatPlanDatabase extends SQLiteDatabase {

	SeatPlanDatabase(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}

	@Override
	void initialise() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	ResultSet displayRow(String primaryKey) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
