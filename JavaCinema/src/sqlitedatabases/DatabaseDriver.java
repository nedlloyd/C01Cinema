package sqlitedatabases;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseDriver {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FilmsDatabase test = new FilmsDatabase();
		ScreeningsDatabase screenings = new ScreeningsDatabase();
		ResultSet rs;
		
		UsersDatabase user = new UsersDatabase();
		ResultSet rs2;
		
		try {
			
			//screenings.addScreening("Apocalyspe Now", "12:00", "31/11/17");
			user.createUser("bob", "abc123", "employee");

			
			//rs = screenings.dateAndTime("12:00", "31/11/17");
			rs2 = user.displayRow("bob");
			
//			while (rs.next()) {
//				System.out.println(rs.getString("filmName") + ", " +  rs.getString("time") + ", " + rs.getString("date") 
//				+ ", " + rs.getString("screeningID"));
//			}
//			
			while (rs2.next()) {
				System.out.println(rs2.getString("userName") + ", " +  rs2.getString("password") /*+ ", " + rs2.getString("role") */);
			}
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			// i think we need to write code here
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// i think we need to write code here
			e.printStackTrace();
		}
		
		

	}

}


