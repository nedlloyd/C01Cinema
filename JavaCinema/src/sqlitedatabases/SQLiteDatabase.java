package sqlitedatabases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.image.Image;

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
	
	// gets connection to database
	protected void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:SQLiteTest1.db");
		initialise();
	}
	
		
	/**
	 * Deletes row with from database 
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
	 *  methods to return column values 
	 */
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
		
		// overloaded displayColumns method 3 columns
		public ResultSet displayColumns(String column1, String column2, String column3) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}

			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT " + column1 + ", " + column2 + ", " + column3 + " FROM " + tableName);
			return res;
		}

}
