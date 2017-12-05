package user;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import sqlitedatabases.ReservationsDatabase;
import sqlitedatabases.ScreeningsDatabase;
import sqlitedatabases.UsersDatabase;

public class MakeReservationController {

	public int screeningID;
	private int userID;

	@FXML private Label filmLabel; @FXML private Label timeLabel;
	@FXML private Button makeReservation;
	@FXML private Circle key1; @FXML private Label keyLbl1;
	@FXML private Circle key2; @FXML private Label keyLbl2;
	@FXML private Circle key3; @FXML private Label keyLbl3;
	//Seats:
	@FXML private Button seat_a1;	@FXML private Button seat_a2; 	@FXML private Button seat_a3;
	@FXML private Button seat_a4;	@FXML private Button seat_a5;	@FXML private Button seat_a6;
	@FXML private Button seat_a7; 	@FXML private Button seat_a8; 	@FXML private Button seat_a9;
	@FXML private Button seat_a10; 	@FXML private Button seat_b1; 	@FXML private Button seat_b2;
	@FXML private Button seat_b3; 	@FXML private Button seat_b4; 	@FXML private Button seat_b5;
	@FXML private Button seat_b6;	@FXML private Button seat_b7;	@FXML private Button seat_b8;
	@FXML private Button seat_b9; 	@FXML private Button seat_b10; 	@FXML private Button seat_c1;
	@FXML private Button seat_c2; 	@FXML private Button seat_c3;  	@FXML private Button seat_c4;
	@FXML private Button seat_c5;  	@FXML private Button seat_c6; 	@FXML private Button seat_c7;
	@FXML private Button seat_c8; 	@FXML private Button seat_c9; 	@FXML private Button seat_c10;
	@FXML private Button seat_d1; 	@FXML private Button seat_d2; 	@FXML private Button seat_d3;
	@FXML private Button seat_d4;	@FXML private Button seat_d5; 	@FXML private Button seat_d6;
	@FXML private Button seat_d7; 	@FXML private Button seat_d8; 	@FXML private Button seat_d9;
	@FXML private Button seat_d10;	@FXML private Button seat_e1;	@FXML private Button seat_e2;
	@FXML private Button seat_e3;	@FXML private Button seat_e4;	@FXML private Button seat_e5;
	@FXML private Button seat_e6;	@FXML private Button seat_e7;	@FXML private Button seat_e8;
	@FXML private Button seat_e9;	@FXML private Button seat_e10;

	// arraylist of class chairs
	private ArrayList<Chairs> seatList = new ArrayList<Chairs>(50);

	@FXML
	public void initialize() {

		// populates seatList with the buttons whether they are already booked and what image they are showing set to false	
		seatList.add(0, new Chairs(seat_a1, "a1")); seatList.add(1, new Chairs(seat_a2, "a2"));
		seatList.add(2, new Chairs(seat_a3, "a3")); seatList.add(3, new Chairs(seat_a4, "a4"));
		seatList.add(4, new Chairs(seat_a5, "a5")); seatList.add(5,  new Chairs(seat_a6, "a6"));
		seatList.add(6, new Chairs(seat_a7, "a7")); seatList.add(7, new Chairs(seat_a8, "a8"));
		seatList.add(8, new Chairs(seat_a9, "a9")); seatList.add(9, new Chairs(seat_a10, "a10"));

		seatList.add(10, new Chairs(seat_b1, "b1")); seatList.add(11, new Chairs(seat_b2, "b2"));
		seatList.add(12, new Chairs(seat_b3, "b3")); seatList.add(13, new Chairs(seat_b4, "b4"));
		seatList.add(14, new Chairs(seat_b5, "b5")); seatList.add(15, new Chairs(seat_b6, "b6"));
		seatList.add(16, new Chairs(seat_b7, "b7")); seatList.add(17, new Chairs(seat_b8, "b8"));
		seatList.add(18, new Chairs(seat_b9, "b9")); seatList.add(19, new Chairs(seat_b10, "b10"));

		seatList.add(20, new Chairs(seat_c1, "c1")); seatList.add(21, new Chairs(seat_c2, "c2"));
		seatList.add(22, new Chairs(seat_c3, "c3")); seatList.add(23, new Chairs(seat_c4, "c4"));
		seatList.add(24, new Chairs(seat_c5, "c5")); seatList.add(25, new Chairs(seat_c6, "c6"));
		seatList.add(26, new Chairs(seat_c7, "c7")); seatList.add(27, new Chairs(seat_c8, "c8"));
		seatList.add(28, new Chairs(seat_c9, "c9")); seatList.add(29, new Chairs(seat_c10, "c10"));

		seatList.add(30, new Chairs(seat_d1, "d1")); seatList.add(31, new Chairs(seat_d2, "d2"));
		seatList.add(32, new Chairs(seat_d3, "d3")); seatList.add(33, new Chairs(seat_d4, "d4"));
		seatList.add(34, new Chairs(seat_d5, "d5")); seatList.add(35, new Chairs(seat_d6, "d6"));
		seatList.add(36, new Chairs(seat_d7, "d7")); seatList.add(37, new Chairs(seat_d8, "d8"));
		seatList.add(38, new Chairs(seat_d9, "d9")); seatList.add(39, new Chairs(seat_d10, "d10"));

		seatList.add(40, new Chairs(seat_e1, "e1")); seatList.add(41, new Chairs(seat_e2, "e2"));
		seatList.add(42, new Chairs(seat_e3, "e3")); seatList.add(43, new Chairs(seat_e4, "e4"));
		seatList.add(44, new Chairs(seat_e5, "e5")); seatList.add(45, new Chairs(seat_e6, "e6"));
		seatList.add(46, new Chairs(seat_e7, "e7")); seatList.add(47, new Chairs(seat_e8, "e8"));
		seatList.add(48, new Chairs(seat_e9, "e9")); seatList.add(49, new Chairs(seat_e10, "e10"));

		//Update label with film title and viewing time/date
		try {
			ScreeningsDatabase sdb = new ScreeningsDatabase();
			ResultSet screeningResult = sdb.displayRow(screeningID);
			String filmName = screeningResult.getString("filmName");
			String time = screeningResult.getString("time");
			String date = screeningResult.getString("date");
			
			filmLabel.setText(filmName);
			timeLabel.setText(date+" "+time);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the seats in the auditorium based on whether they have been reserved.
	 * This method is called in the UserMainController class.
	 * @param screeningID
	 */
	public void setSeats(int screeningID) {

		//variable this screening set to screeningID
		this.screeningID = screeningID;

		// sets each seat in cinema to unoccupied and sets the image as an unoccupied chair
		for (int i = 0; i < seatList.size(); i++) {
			
			seatList.get(i).getButton().setStyle("-fx-background-color: #2dd321;");
		}

		// arraylist (seats) created and populated by seatIDs
		ArrayList<String> reservedSeats = new ArrayList<String>();
		try {
			reservedSeats = getReservedSeatIDs();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// seats iterated over in order to change any seats that have been reserved to occupied 
		for (String reservedSeat : reservedSeats) {		
			for (int i = 0; i < seatList.size(); i++) {

				// if the reserved SeatID matches a seat number it is changed to the occupied picture
				if (reservedSeat.equals(seatList.get(i).getId())) {
					//	seatList.get(i).getButton().setGraphic(occupied);
					seatList.get(i).getButton().setStyle("-fx-background-color: #d31818;");
					seatList.get(i).setOccupied(true);
					break;
				}
			}
		}	
	}

	/**
	 * ArrayList created populated by seatIDs for reserved seats
	 * @return 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ArrayList<String> getReservedSeatIDs() throws SQLException, ClassNotFoundException {
		//reservations database initialised 
		ReservationsDatabase rd = new ReservationsDatabase();

		// resultSet created by inputing screening id chosen by user and viewing reservations that for this screening ID
		ResultSet res = rd.displayRows(screeningID);

		ArrayList<String> reservedSeats = new ArrayList<String>();

		//reserved seatIDs are passed into an ArrayList 
		while (res.next()) {
			String seatID = res.getString("seatID");
			reservedSeats.add(seatID);
		}
		return reservedSeats;
	}

	/**
	 * Method to change graphic when seat is clicked on by user 
	 * @param chair
	 */
	public void changeGraphic(Chairs chair) {

		// The button colour can only be changed if it has not already been booked 
		// if the chair is unoccupied then set to occupied
		if (chair.isSelectedForBooking()==false  && chair.isOccupied()==false) {  			
			//			chair.getButton().setGraphic(occupied);
			chair.getButton().setStyle("-fx-background-color:  #ffdc00;");// #ffdc00 is yellow for personal booking
			chair.setBooked(true);
			// if the chair has been set to occupied and it has not been booked by someone else i.e. as long as it had been clicked to occupied by the current user
		} else if (chair.isOccupied()==false){
			//			chair.getButton().setGraphic(unoccupied);
			chair.getButton().setStyle("-fx-background-color: #2dd321;");
			chair.setBooked(false);
		}
	}

	/**
	 * When a chair is clicked the image is changed based on the change graphic method 
	 * @param event
	 */
	public void clickToBook(ActionEvent event) {
		// gets the source of the ActionEvent so it know what seat to change
		Button butt = (Button) event.getSource();

		// for each chair if it's the button pressed then the image is changes based upon the change graphic method 
		for (Chairs seat : seatList) {		
			if (seat.getButton() == butt) {
				changeGraphic(seat);
			} 
		}
	}	

	/**
	 * When the make reservation button is pressed a new reservation is created including setting userID screeningID and seatID
	 * @param e
	 */
	public void reserve(ActionEvent e) {

		ReservationsDatabase rd = new ReservationsDatabase();
		String seatId;
		// for each seat which has been newly set to occupied a new reservation has been created 
		for (Chairs seat : seatList) {
			// if seat is occupied now and it was not like that to begin with then a reservation is created
			if (seat.isSelectedForBooking() && seat.isOccupied()==false){
				seatId = seat.getId();
				try {
					rd.createReservation(userID, screeningID, seatId);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * userID is set from the UserMainController, it takes the username and queries the database to find the userID 
	 * @param user
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void setUser(String user) throws ClassNotFoundException, SQLException {
		UsersDatabase ud = new UsersDatabase();
		//finds the userID based on the user name
		ResultSet res = ud.displayRow(user);
		int id = 0;
		while (res.next()) {
			id = res.getInt("userID");
		}
		this.userID = id;

	}

}
