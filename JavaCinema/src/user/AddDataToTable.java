package user;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sqlitedatabases.ReservationsDatabase;
import sqlitedatabases.SQLiteDatabase;
import sqlitedatabases.ScreeningsDatabase;


public class AddDataToTable {
	private SimpleStringProperty filmName, filmDescription, filmTime, filmDate, seatID;
	private SimpleIntegerProperty reservationID, screeningID;
	private int availableSeats;
	private ImageView filmImage;


	public AddDataToTable(String filmName, String filmDescription, String filmTime, int screeningID) {
		this.filmName = new SimpleStringProperty(filmName);
		this.filmDescription = new SimpleStringProperty(filmDescription);
		this.filmTime = new SimpleStringProperty(filmTime);	
		this.screeningID = new SimpleIntegerProperty(screeningID);
	}
	
	public AddDataToTable(String filmName, String filmTime, String filmDate, String seatID, Integer reservationID) {
		this.filmName = new SimpleStringProperty(filmName);
		this.filmTime = new SimpleStringProperty(filmTime);	
		this.filmDate = new SimpleStringProperty(filmDate);	
		this.seatID = new SimpleStringProperty(seatID);
		this.reservationID = new SimpleIntegerProperty(reservationID);
	}

	public AddDataToTable(String filmName, String filmDescription, String filmTime, int screeningID, ImageView filmImage) {
		this.filmName = new SimpleStringProperty(filmName);
		this.filmDescription = new SimpleStringProperty(filmDescription);
		this.filmTime = new SimpleStringProperty(filmTime);	
		this.screeningID = new SimpleIntegerProperty(screeningID);
		this.filmImage = filmImage;
	}

	public AddDataToTable(String filmName, String filmDescription, String filmTime) {
		this.filmName = new SimpleStringProperty(filmName);
		this.filmDescription = new SimpleStringProperty(filmDescription);
		this.filmTime = new SimpleStringProperty(filmTime);	
	}

	public String getFilmDescription() {
		return filmDescription.get();
	}

	public void setFilmDescription(SimpleStringProperty filmDescription) {
		this.filmDescription = filmDescription;
	}

	public String getFilmName() {
		return filmName.get();
	}

	public void setFilmName(SimpleStringProperty filmName) {
		this.filmName = filmName;
	}

	public String getFilmTime() {
		return filmTime.get();
	}

	public void setFilmTime(SimpleStringProperty filmTime) {
		this.filmTime = filmTime;
	}
	

	public int getScreeningID() {
		return this.screeningID.get();
	}

	public ImageView getFilmImage() {
		return filmImage;
	}


	public void setFilmImage(ImageView filmImage) {
		this.filmImage = filmImage;
	}


	public int getAvailableSeats(){
		return availableSeats;
	}
	
	public String getFilmDate() {
		return filmDate.get();
	}
	
	public void setFilmDate(SimpleStringProperty filmDate) {
		this.filmDate = filmDate;
	}
	
	public int getReservationID() {
		return reservationID.get();
	}
	
	public void setReservationID(SimpleIntegerProperty reservationID) {
		this.reservationID = reservationID;
	}
	
	public String getSeatID() {
		return seatID.get();
	}
	
	public void setSeatID(SimpleStringProperty seatID) {
		this.seatID = seatID;
	}
	
	

	/**
	 * Calculates the number of available seats in a screening and sets the 
	 * corresponding member variable of AddDataToTable Object, availableSeats, to this value.
	 * @param reservationsDatabase
	 * @param screeningId 
	 * @param cinemaCapacity the number of seats in the cinema
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void calculateAndSetAvailableSeatsCount(ReservationsDatabase reservationsDatabase, String screeningId, int cinemaCapacity) throws ClassNotFoundException, SQLException{
		
		ResultSet reservationsResultSet = reservationsDatabase.displayRows(Integer.parseInt(screeningId));
		int reservationCount = reservationsDatabase.countRowsInResultSet(reservationsResultSet);
		this.availableSeats = cinemaCapacity - reservationCount;
	}

}
