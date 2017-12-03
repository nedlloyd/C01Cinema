package user;

import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sqlitedatabases.ReservationsDatabase;
import sqlitedatabases.UsersDatabase;

public class MakeReservationController {

	@FXML
	private Label littleLabel;
	

	public int screeningID;
	private int userID;

	
	@FXML private Button btn1;
	@FXML private Button btn2;
	@FXML private Button btn3;
	@FXML private Button btn4;
	@FXML private Button btn5;
	@FXML private Button btn6;
	
	@FXML private Button makeReservation;
	
	// arraylist of class chairs
	private ArrayList<Chairs> seatList = new ArrayList<Chairs>();
	
	@FXML
	public void initialize() {

		// populates seatlist with the buttons whether they are already booked and what image they are showing set to false	
		seatList.add(0, new Chairs(btn1, 1, false, false));
		seatList.add(1, new Chairs(btn2, 2, false, false));
		seatList.add(2, new Chairs(btn3, 3, false, false));
		seatList.add(3, new Chairs(btn4, 4, false, false));
		seatList.add(4, new Chairs(btn5, 5, false, false));
		seatList.add(5, new Chairs(btn6, 6, false, false));
		
	}

	// sets the seats in the auditorium based on whether they have been reserved
	//should porbably be split into a few methods
	public void setSeats(int screeningID) {
		
		//variable this screening set to screeningID
		this.screeningID = screeningID;
		
		// sets each seat in cinema to unoccupied and sets the image as an unoccupied chair
		for (int i = 0; i < seatList.size(); i++) {
			// new image created from empty chair
			Image seatUnoccupied = new Image("/images/empty_chair.png", 40, 40, false, false);
			// image view created from seatUnoccupied
			ImageView unoccupied = new ImageView(seatUnoccupied);
			
			// sets seats images for seats to unocupied 
			seatList.get(i).getButton().setGraphic(unoccupied);
		}
	
		// arraylist (seats) created and populated by seatIDs
		ArrayList<Integer> seats = new ArrayList<Integer>();
		try {
			seats = getSeatID();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// seats iterated over in order to change any seats that have been reserved to occupied 
		for (int item : seats) {		
			for (int i = 0; i < seatList.size(); i++) {
				Image seatOccupied = new Image("/images/occupied_chair.png", 40, 40, false, false);
				ImageView occupied = new ImageView(seatOccupied);
				
				// if the reserved SeatID matches a seat number it is changed to the occupied picture
				if (item == i+1) {
					seatList.get(i).getButton().setGraphic(occupied);
					seatList.get(i).setFixed(true);
				}
			}
		}	
	}
	
	// arraylist created populated by seatIDs for reserved seats
	public ArrayList getSeatID() throws SQLException, ClassNotFoundException {
		//reservations database initialised 
		ReservationsDatabase rd = new ReservationsDatabase();
		
		// resultSet created by inputing screening id chosen by user and viewing reservations that for this screening ID
		ResultSet res = rd.displayRows(screeningID);
		ArrayList<Integer> seats = new ArrayList<Integer>();
		
		//reserved seatIDs are passed into an arraylist 
		while (res.next()) {
			int seatID = res.getInt("seatID");
			seats.add(seatID);
		}
		
		return seats;
	}
	
	// method to change graphic when seat is clicked on by user 
	public void changeGraphic(Chairs chair) {
		//images created with which to add to reserved seats 
		Image seatUnoccupied = new Image("/images/empty_chair.png", 40, 40, false, false);
		ImageView unoccupied = new ImageView(seatUnoccupied);
		Image seatOccupied = new Image("/images/occupied_chair.png", 40, 40, false, false);
		ImageView occupied = new ImageView(seatOccupied);
		
		
		// the image can only be changed if it has not already been booked 
		// if the chair is unoccupied then set to occupied
		if (!chair.isOccupied()  && !chair.isFixed()) {  			
			chair.getButton().setGraphic(occupied);
			chair.setOccupied(true);
			// if the chair has been set to occupied and it has not been booked by someone else i.e. as long as it had been clicked to occupied by the current user
		} else if (!chair.isFixed()){
			chair.getButton().setGraphic(unoccupied);
			chair.setOccupied(false);
		}
	}
	
	// when a chair is clicked the image is changed based on the change graphic method 
	public void clickToBook(ActionEvent event) {
		// gets the source of the ActionEvent so it know what seat to change
		Button o = (Button)event.getSource();
		
		// for each chair if it's the button pressed then the image is changes based upon the change graphic method 
		for (Chairs item : seatList) {		
			if (item.getButton() == o) {
				changeGraphic(item);
				} 
			}
		}	
	
	// when the make reservation button is pressed a new reservation is created including setting userID screeningID and seatID
	public void reserve(ActionEvent e) {
		
		ReservationsDatabase rd = new ReservationsDatabase();
		int i;
		// for each seat which has been newly set to occupied a new reservation has been created 
		for (Chairs item : seatList) {
			// if seat is occupied now and it was not like that to begin with then a reservation is created
			if (item.isOccupied() && !item.isFixed()) {
				i = item.getId();
				try {
					rd.createReservation(userID, screeningID, i);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		
	}
	
	// userID is set from the UserMainController, it takes the username and querries the databse to find the userID 
	public void setUser(String user) throws ClassNotFoundException, SQLException {
		UsersDatabase ud = new UsersDatabase();
		//finds the userID based on the username
		ResultSet res = ud.displayRow(user);
		
		int id = 0;
		while (res.next()) {
			id = res.getInt("userID");
		}
		
		this.userID = id;
		
	}
	
}
