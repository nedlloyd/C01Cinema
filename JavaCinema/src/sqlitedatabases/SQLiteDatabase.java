package sqlitedatabases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SQLiteDatabase {
	
	protected static Connection con;
	protected boolean hasData = false;
	protected String tableName;
	
	SQLiteDatabase(String tableName){
		this.tableName = tableName;
	}
	
	abstract void initialise() throws SQLException;
	abstract ResultSet displayRow(String primaryKey) throws ClassNotFoundException, SQLException;
	
	// gets connection to database
	protected void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:SQLiteTest1.db");
		initialise();
	}


		
	//deletes from database 
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
	
	
	//adds new column to database 
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
	
	// methods to return column values 
		public ResultSet displayColumns(String column) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT " + column + " FROM " + tableName);
			return res;
		}
		// overloaded displayColumns method
		public ResultSet displayColumns(String column1, String column2) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT " + column1 + ", " + column2 + " FROM " + tableName);
			return res;
		}
		

	
	
	
	

}
