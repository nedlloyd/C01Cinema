package application;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabase {
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
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='films' ");
			//working out if there is a table of name films if there is it's the one we want to use
			if (!res.next()) {
				System.out.println("Building the films table with prepopulated values");
				
				//and so we start to build a table				
				Statement state2 = con.createStatement();
				state2.execute("CREATE TABLE films(id integer," +
				"filmName varchar(60)," + "filmDescription varchar(60)," +
						"primary key(id));");
				
				//and then start to insert data
				PreparedStatement prep = con.prepareStatement("INSERT INTO films values(?,?,?);");
				prep.setString(2, "Vertigo");
				prep.setString(3, "best");
				prep.execute();
				
				PreparedStatement prep2 = con.prepareStatement("INSERT INTO films values(?,?,?);");
				prep2.setString(2, "Breathless");
				prep2.setString(3, "french");
				prep2.execute();
				
			}
		}
	}
	
	//adds to film database 
	public void addFilm(String filmName, String filmDescription, String imageFilePath) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("INSERT INTO films values(?,?,?,?);");
		prep.setString(2, filmName);
		prep.setString(3, filmDescription);
		prep.setBytes(4, readFile(imageFilePath));
		prep.execute();
		prep.close();
        con.close();
	}
	
	//deletes from database 
	public void delete(int id) throws SQLException, ClassNotFoundException {
		if (con == null) {
			getConnection();
		}
		
		String sql = "DELETE FROM films WHERE id = ?";
		PreparedStatement prep = con.prepareStatement(sql);
		
		// set the corresponding param
        prep.setInt(1, id);
        // execute the delete statement
        prep.executeUpdate();
				
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
	
	// methods to return column values 
		public ResultSet displayColumns(String table, String column) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT " + column + " FROM " + table);
			return res;
		}
		// overloaded column value method
		public ResultSet displayColumns(String table, String column1, String column2) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT " + column1 + ", " + column2 + " FROM " + table);
			return res;
		}
		
		
		//displays single row identified by id
		public ResultSet displayRow(String table, int id) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT " + "id, " + "filmName, " + "filmDescription" + " FROM " + table + " LIMIT " + 1 + " OFFSET " + (id - 1) + ";");
			return res;
		}
		
		//return row based on filmName
		public ResultSet displayRow(String table, String filmName) throws ClassNotFoundException, SQLException {
			if (con == null) {
				getConnection();
			}
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT " + "id, " + "filmName, " + "filmDescription" + " FROM " + table + " WHERE filmName="+ "\"" + filmName + "\"" + ";");
			return res;
		}
		
	
	
	// return file in output that can be stored in database
	// copied from: http://www.sqlitetutorial.net/sqlite-java/jdbc-read-write-blob/
	private byte[] readFile(String file) {
        ByteArrayOutputStream bos = null;
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }
	
	
	// adds picture to the id sepcified 
	// based on: https://www.youtube.com/watch?v=s80sAEbF9Fk
	public void addPicture(int materialId, String filename) throws SQLException, ClassNotFoundException {
        // update sql
		int num_rows = 0;
		if (con == null) {
			getConnection();
		}
		
		
		
        String updateSQL = "UPDATE films "
                + "SET image = ? "
                + "WHERE id=?";
        
        PreparedStatement prep = con.prepareStatement(updateSQL);
        prep.setBytes(1, readFile(filename));
        prep.setInt(2, materialId);

        num_rows = prep.executeUpdate();
        //asks if it has affected at least one row i.e has it added anything
        if (num_rows > 0) {
        System.out.println("Stored the file in the BLOB column.");
        }
       
	}
	
	
	// based on: http://www.sqlitetutorial.net/sqlite-java/jdbc-read-write-blob/
	public void readPicture(int materialId, String filename) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}
		
		// update sql
        String selectSQL = "SELECT image FROM films WHERE id=?";
        ResultSet rs = null;
        FileOutputStream fos = null;
       // Connection con = null;
        PreparedStatement prep = null;
 
        try {
            
            prep = con.prepareStatement(selectSQL);
            prep.setInt(1, materialId);
            rs = prep.executeQuery();
            
            
 
            // write binary stream into file
            File file = new File(filename);
            fos = new FileOutputStream(file);
 
            System.out.println("Writing BLOB to file " + file.getAbsolutePath());
            while (rs.next()) {
                InputStream input = rs.getBinaryStream("image");
                byte[] buffer = new byte[1024];
                while (input.read(buffer) > 0) {
                    fos.write(buffer);
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (prep != null) {
                    prep.close();
                }
 
                if (con != null) {
                    con.close();
                }
                if (fos != null) {
                    fos.close();
                }
 
            } catch (SQLException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
        
 
       
	
}
