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
 * Sets up SQLite database 'films' with columns: id, filmName, filmDescription.
 * @author Ned
 *
 */
public class FilmsDatabase extends SQLiteDatabase {

	private static boolean hasData = false;

	public FilmsDatabase(){
		super("films");
	}

	/**
	 * 
	 * Checks to see if there already exists a database table for the name we require.  If there is this ons is used 
	 * if not one is created
	 * 
	 * based on: https://www.youtube.com/watch?v=JPsWaI5Z3gs
	 */
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
	 * based on: http://www.sqlitetutorial.net/sqlite-java/jdbc-read-write-blob/
	 * 
	 */
	private static byte[] readFile(String filePath) {
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

}
