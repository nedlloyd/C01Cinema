package sqlitedatabases;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sets up SQLite database with columns: id, filmName, filmDescription
 * @author Ned
 *
 */
public class FilmsDatabase extends SQLiteDatabase {

	private static boolean hasData = false;

	public FilmsDatabase(){
		super("films");
	}

	// based on: https://www.youtube.com/watch?v=JPsWaI5Z3gs
	protected void initialise() throws SQLException {
		// TODO Auto-generated method stub
		if (!hasData) {
			hasData = true;

			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='"+super.tableName+"' ");
			if (!res.next()) {
				System.out.println("Building the "+tableName+" table with prepopulated values");

				//and so we start to build a table				
				Statement state2 = con.createStatement();
				state2.execute("CREATE TABLE "+tableName+"(id integer," +
						"filmName varchar(60)," + "filmDescription varchar(60)," +"primary key(id));");
			}
		}
	}

	/**
	 * adds to film to database 
	 * @param filmName
	 * @param filmDescription
	 * @param imageFilePath
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void addFilm(String filmName, String filmDescription, int filmDuration, String imageFilePath) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		PreparedStatement prep = con.prepareStatement("INSERT INTO films values(?,?,?,?,?);");
		prep.setString(2, filmName);
		prep.setString(3, filmDescription);
		prep.setBytes(4, readFile(imageFilePath));
		prep.setInt(5, filmDuration);
		prep.execute();
		// in order to add to both screenings and films database i had to not close this, i'm not sure if this has other bad effects
		//prep.close();
		//con.close();
	}

	/**
	 * adds films to database with all parameters but image
	 * @param filmName
	 * @param filmDescription
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void addFilm(String filmName, String filmDescription) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		PreparedStatement prep = con.prepareStatement("INSERT INTO films values(?,?,?,?);");
		prep.setString(2, filmName);
		prep.setString(3, filmDescription);
		prep.execute();
		prep.close();
		con.close();
	}
	
	/**
	 * 
	 * ResultSet returned of row with matching ID
	 * 
	 * @Override
	 */
	public ResultSet displayRow(int id) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT " + "id, " + "filmName, " + "filmDescription" + " FROM " + tableName + " WHERE id="+ "\"" + id + "\"" + ";");
		return res;
	}

	/**
	 * returns ResultSet of rows with name of film given as argument 
	 * @param filmName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ResultSet displayRow(String filmName) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT " + "id, " + "filmName, " + "filmDescription, " + "filmDuration" + " FROM " + tableName + " WHERE filmName="+ "\"" + filmName + "\"" + ";");
		return res;
	}
	
	/**
	 * returns ResultSet of rows with name of film given as argument 
	 *
	 *  
	 * @param filmName1
	 * @param filmName2
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 *//*
	public ResultSet displayRows(String filmName1, String filmName2) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT " + "id, " + "filmName, " + "filmDescription, " + "filmDuration" + " FROM " + tableName + 
				" WHERE (filmName=\"" + filmName1 + "\") OR (filmName=\"" + filmName2 + "\");");
		return res;
	}*/

	/**
	 * 
	 *  returns String of description for a given film 
	 *
	 * 
	 * @param filmName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String filmDescriptionString(String filmName) throws ClassNotFoundException, SQLException{
		if (con == null) {
			getConnection();
		}

		PreparedStatement ps = con.prepareStatement("SELECT * FROM " + tableName + " WHERE filmName = ?");

		//First search all persons with name "J.Baoby" and age = 5
		ps.setString(1, filmName);
		ResultSet set1 = ps.executeQuery();
		// Do something with it 
		// ....
		String description = set1.getString("filmDescription");

		return description;			
	}


	/** 
	 * 
	 * returns file form 'filePath' as a ByteArrayOutputStream that can be stored in database
	 * 
	 * @param file
	 * @return
	 * 
	 */
	//based on: http://www.sqlitetutorial.net/sqlite-java/jdbc-read-write-blob/
	private byte[] readFile(String filePath) {
		ByteArrayOutputStream bos = null;
		try {

			File f = new File(filePath);
			FileInputStream fis = new FileInputStream(f);
			byte[] buffer = new byte[1024];
			bos = new ByteArrayOutputStream();
			for (int len; (len = fis.read(buffer)) != -1;) {
				bos.write(buffer, 0, len);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e2) {
			System.err.println(e2.getMessage());
		}
		return bos != null ? bos.toByteArray() : null;
	}


	/**
	 * Picture given in filePath is converted using 'readFile' method then stored in the database as ID specified
	 *   
	 *  Image is 
	 *  
	 * @param materialId
	 * @param filename
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	// based on: https://www.youtube.com/watch?v=s80sAEbF9Fk
	/*public void addPicture(int materialId, String filePath) throws SQLException, ClassNotFoundException {
		// update sql
		int num_rows = 0;
		if (con == null) {
			getConnection();
		}

		String updateSQL = "UPDATE "+super.tableName+" "
				+ "SET image = ? "
				+ "WHERE id=?";

		PreparedStatement prep = con.prepareStatement(updateSQL);
		prep.setBytes(1, readFile(filePath));
		prep.setInt(2, materialId);

		num_rows = prep.executeUpdate();
		//asks if it has affected at least one row i.e has it added anything
		if (num_rows > 0) {
			System.out.println("Stored the file in the BLOB column.");
		}

	}*/

	/**
	 * 
	 * @param materialId
	 * @param filePath
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	// based on: http://www.sqlitetutorial.net/sqlite-java/jdbc-read-write-blob/
	/*public void readPicture(int materialId, String filePath) throws ClassNotFoundException, SQLException {
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
			File file = new File(filePath);
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
	}*/

}
