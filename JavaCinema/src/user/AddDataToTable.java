package user;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Date;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sqlitedatabases.ReservationsDatabase;
import sqlitedatabases.SQLiteDatabase;
import sqlitedatabases.ScreeningsDatabase;


public class AddDataToTable {
	private SimpleStringProperty filmName, filmDescription, filmTime, filmDate, seatID, endTime;
	private SimpleIntegerProperty reservationID, screeningID, duration;
	private int availableSeats;
	private ImageView filmImage;
	private Date dateObjectEnd;


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

	
	public AddDataToTable(String filmName, String filmTime, int duration, String endTime, Date dateObjectEnd) {
		this.filmName = new SimpleStringProperty(filmName);
		this.filmTime = new SimpleStringProperty(filmTime);
		this.duration = new SimpleIntegerProperty(duration);
		this.endTime = new SimpleStringProperty(endTime);
		this.dateObjectEnd = dateObjectEnd;
	}

	
	public String getFilmDescription() {
		return filmDescription.get();
	}

	public String getFilmName() {
		return filmName.get();
	}

	public String getFilmTime() {
		return filmTime.get();
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

	public String getFilmDate() {
		return filmDate.get();
	}
	
	public int getReservationID() {
		return reservationID.get();
	}
	
	public String getSeatID() {
		return seatID.get();
	}

	public int getDuration() {
		return duration.get();
	}
	
	public String getEndTime() {
		return endTime.get();
	}
	
	public Date getDateObjectEnd() {
		return dateObjectEnd;
	}
	public int getAvailableSeats(){
		return this.availableSeats;
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
	public void calculateAndSetAvailableSeatsCount(ReservationsDatabase reservationsDatabase, int screeningId, int cinemaCapacity) throws ClassNotFoundException, SQLException{
		
		ResultSet reservationsResultSet = reservationsDatabase.displayRows(screeningId);
		int reservationCount = reservationsDatabase.countRowsInResultSet(reservationsResultSet);
		this.availableSeats = cinemaCapacity - reservationCount;
	}

}
