package sqlitedatabases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import user.AddDataToTable;

public class DatabaseDriver {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		// TODO Auto-generated method stub
		
		ReservationsDatabase rd = new ReservationsDatabase();
		rd.createReservation(1, 1, 1);
		
		
		
		/*FilmsDatabase test = new FilmsDatabase();
		ObservableList<AddDataToTable> films = FXCollections.observableArrayList();
		
		ScreeningsDatabase filmViewing = new ScreeningsDatabase();
		ResultSet res = test.getDataFromTwoTables("films", "screenings", "24/12/17");
		
		
		try {
			while (res.next()) {
				String des = res.getString("filmDescription");
				String time = res.getString("time");
				String name = res.getString("filmName");
				String id = res.getString("screeningID");
				String id1 = res.getString("id");
				
				
				//System.out.println(id1);
				//System.out.println(name);
				//System.out.println(id);
	


				
				films.add(new AddDataToTable(name, des, time));
				
				for (AddDataToTable item : films) {
					System.out.println(item.getFilmDescription());
				}
				
			*/

				/*String name = " ";
				String time = " ";

				if (res.next()) {
					name = res.getString("filmName");
					time = res.getString("time");
				}

				System.out.println(name);
				System.out.println(time);

				
				System.out.println("how");
				
				String description = test.filmDescriptionString(name);
				System.out.print(description);

				//System.out.print(description + " " + time + " " + name);

				//films.add(new AddDataToTable(name, description, time));
*/			}

		/*} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error with populating table form database");
		}
*/

		//			//rs = screenings.dateAndTime("12:00", "31/11/17");
		//			rs = test.displayColumns("filmName");
		//			
		//			while (rs.next()) {
		//				System.out.println(rs.getString("filmName"));
		//			}

		//			while (rs2.next()) {
		//				System.out.println(rs2.getString("userName") + ", " +  rs2.getString("password") /*+ ", " + rs2.getString("role") */);
		//			}
		//			



		

	
	
	

}


