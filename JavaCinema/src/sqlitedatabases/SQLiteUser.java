package sqlitedatabases;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteUser extends SQLiteDatabase {

	SQLiteUser(){
		super("users");
	}
	
	// based on: https://www.youtube.com/watch?v=JPsWaI5Z3gs
	public void initialise() throws SQLException {
		// TODO Auto-generated method stub
		if (!super.hasData) {
			hasData = true;
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='"+super.tableName +"' "); 
			//working out if there is a table of name users if there is it's the one we want to use
			if (!res.next()) {
				System.out.println("Building the "+super.tableName+" table with prepopulated values");
				
				//and so we start to build a table				
				Statement state2 = con.createStatement();
				state2.execute("CREATE TABLE "+super.tableName+"(userID integer," +
				"userName varchar(20),"+"password varchar(20),"+"role varchar(10),"+
						"primary key(userID));");
				
				//and then start to insert data
				PreparedStatement prep = con.prepareStatement("INSERT INTO "+super.tableName+" values(?,?,?,?);");
				prep.setString(2, "Jim");
				prep.setString(3, "qwerty");
				prep.setString(4, "employee");
				prep.execute();
				
			}
		}
	}
	
	//adds to user database 
	public void createUser(String userName, String password, String role) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("INSERT INTO "+super.tableName+" values(?,?,?,?);");
		prep.setString(2, userName);
		prep.setString(3, password);
		prep.setString(4, role);
		prep.execute();
		//prep.close();
        //con.close();
	}

	
	
	//adds new column to database 
	public void addColumn(String tableName, String columnName, String type) throws SQLException, ClassNotFoundException {
		if (con == null) {
			getConnection();
		}
		
		String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + type + ";";
		PreparedStatement prep = con.prepareStatement(sql);
		
        prep.executeUpdate();
        prep.close();
        con.close();
				
    }
		
		//return row based on userName
		@Override
		public ResultSet displayRow(String userName) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT " + "userName, " + "password" + " FROM " + super.tableName + " WHERE userName="+ "\"" + userName + "\"" + ";");
			return res;
		}
		
	

	



	


}
