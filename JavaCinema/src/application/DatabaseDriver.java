package application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseDriver {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SQLiteDatabase test = new SQLiteDatabase();
		SQLiteScreenings screenings = new SQLiteScreenings();
		ResultSet rs;
		
		
		
		try {
			
			//screenings.addScreening("Apocalyspe Now", "12:00", "31/11/17");

			
			rs = screenings.dateAndTime("12:00", "31/11/17");
			while (rs.next()) {
				System.out.println(rs.getString("filmName") + ", " +  rs.getString("time") + ", " + rs.getString("date") 
				+ ", " + rs.getString("screeningID"));
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


