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

/**
 * 
 * Class which stores data from the films, screenings, and reservations databases.
 *
 */
public class AddDataToTable {
	
	//Member variables
	private SimpleStringProperty filmName, filmDescription, filmTime, filmDate, seatID, endTime;
	private SimpleIntegerProperty reservationID, screeningID, duration;
	private int availableSeats;
	private ImageView filmImage;
	private Date dateObjectEnd;

	/**
	 * 
	 * AddDataToTable Object initialized with filmName, filmDescription, filmTime, screeningID
	 * 
	 * @param filmName
	 * @param filmDescription
	 * @param filmTime
	 * @param screeningID
	 */
	public AddDataToTable(String filmName, String filmDescription, String filmTime, int screeningID) {
		this.filmName = new SimpleStringProperty(filmName);
		this.filmDescription = new SimpleStringProperty(filmDescription);
		this.filmTime = new SimpleStringProperty(filmTime);	
		this.screeningID = new SimpleIntegerProperty(screeningID);
	}
	
	/**
	 *AddDataToTable Object initialized with filmName, filmTime, filmDate, seatID and reservationID
	 * 
	 * @param filmName
	 * @param filmTime
	 * @param filmDate
	 * @param seatID
	 * @param reservationID
	 */
	public AddDataToTable(String filmName, String filmTime, String filmDate, String seatID, Integer reservationID) {
		this.filmName = new SimpleStringProperty(filmName);
		this.filmTime = new SimpleStringProperty(filmTime);	
		this.filmDate = new SimpleStringProperty(filmDate);	
		this.seatID = new SimpleStringProperty(seatID);
		this.reservationID = new SimpleIntegerProperty(reservationID);
	}

	/**
	 * 
	 * AddDataToTable Object initialized with filmName, filmTime, duration, endTime and dateObjectend
	 * 
	 * @param filmName
	 * @param filmTime
	 * @param duration
	 * @param endTime
	 * @param dateObjectEnd
	 */
	public AddDataToTable(String filmName, String filmTime, int duration, String endTime, Date dateObjectEnd) {
		this.filmName = new SimpleStringProperty(filmName);
		this.filmTime = new SimpleStringProperty(filmTime);
		this.duration = new SimpleIntegerProperty(duration);
		this.endTime = new SimpleStringProperty(endTime);
		this.dateObjectEnd = dateObjectEnd;
	}

	/**
	 * FilmDescription returned as String
	 * @return
	 */
	public String getFilmDescription() {
		return filmDescription.get();
	}

	/**
	 * filmName returned as String
	 * @return
	 */
	public String getFilmName() {
		return filmName.get();
	}
	
	/**
	 * filmTime returned as String
	 * @return
	 */
	public String getFilmTime() {
		return filmTime.get();
	}

	/**
	 * screeningID returned as int
	 * @return
	 */
	public int getScreeningID() {
		return this.screeningID.get();
	}

	/**
	 * filmImage returned as ImageView
	 * @return
	 */
	public ImageView getFilmImage() {
		return filmImage;
	}

	/**
	 * Film Image setter
	 * @param filmImage
	 */
	public void setFilmImage(ImageView filmImage) {
		this.filmImage = filmImage;
	}

	/**
	 * filmDate returned as String
	 * @return
	 */
	public String getFilmDate() {
		return filmDate.get();
	}
	
	/**
	 * reservationID returned as int
	 * @return
	 */
	public int getReservationID() {
		return reservationID.get();
	}
	
	/**
	 * seatID returned as String
	 * @return
	 */
	public String getSeatID() {
		return seatID.get();
	}

	/**
	 * duration returned as int
	 * @return
	 */
	public int getDuration() {
		return duration.get();
	}
	
	/**
	 * endTime returned as String
	 * @return
	 */
	public String getEndTime() {
		return endTime.get();
	}
	
	/**
	 * dateObjectEnd returned as Date
	 * @return
	 */
	public Date getDateObjectEnd() {
		return dateObjectEnd;
	}
	
	/**
	 * availableSeats returned as int
	 * @return
	 */
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
