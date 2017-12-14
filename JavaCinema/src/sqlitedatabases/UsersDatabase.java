package sqlitedatabases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sets up SQLite database 'users' with columns: userID, userName, password, role.
 * Deals with all communications to this database.
 * @author Sam
 *
 */
public class UsersDatabase extends SQLiteDatabase {

	static boolean hasData = false;

	public UsersDatabase(){
		super("users");
	}

	
	/**
	 * Checks to see if there already exists a database table for the name we require.  If there is this ons is used 
	 * if not one is created
	 * 
	 * based on: https://www.youtube.com/watch?v=JPsWaI5Z3gs
	 * 
	 */
	public void initialise() throws SQLException {
		// TODO Auto-generated method stub
		if (!hasData) {
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

	/**
	 * Adds new user to database 
	 * @param userName
	 * @param password
	 * @param role
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void createUser(String userName, String password, String email, String role) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		PreparedStatement prep = con.prepareStatement("INSERT INTO "+super.tableName+" values(?,?,?,?,?);");
		prep.setString(2, userName);
		prep.setString(3, password);
		prep.setString(4, role);
		prep.setString(5, email);
		prep.execute();
		//prep.close();
		//con.close();
	}


	/**
	 * return row based on userName
	 */
	public ResultSet displayRow(String userName) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT " +
				"userID, "+ "userName, " + "password, "+ "role, " + "email" +
				" FROM " + super.tableName + " WHERE userName="+ "\"" + userName + "\"" + ";");
		return res;
	}

	/**
	 * @Override
	 * 
	 * Resturns as ResultSet rows corresponding to 'primaryKey' given as argument
	 * 
	 */
	public ResultSet displayRow(int primaryKey) throws ClassNotFoundException, SQLException {
		if (con == null) {
			getConnection();
		}

		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT " 
				+ "userID, "+ "userName, " + "password, " + "role "+
				" FROM " + super.tableName + " WHERE userID="+ "\"" + primaryKey + "\"" + ";");
		return res;
	}
	
	/**
	 * 
	 * 'users' table updated with 'username', 'email', 'password', 'userID'
	 * 
	 * @param username
	 * @param email
	 * @param password
	 * @param userID
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void updateUserInfo(String username, String email, String password, int userID) throws SQLException, ClassNotFoundException {
		if (con == null) {
			getConnection();
		}
				
		PreparedStatement prep = con.prepareStatement("UPDATE users SET userName = ? , "
                + "password = ?, email = ? "
                + "WHERE userID = "+userID+";");
		prep.setString(1, username);
		prep.setString(2, password);
		prep.setString(3, email);
		prep.execute();
	
		

	}
	


}
