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
	private SimpleStringProperty filmName, filmDescription, filmTime, screeningID;
	private int availableSeats;
	private ImageView filmImage;


	public AddDataToTable(String filmName, String filmDescription, String filmTime, String screeningID) {
		this.filmName = new SimpleStringProperty(filmName);
		this.filmDescription = new SimpleStringProperty(filmDescription);
		this.filmTime = new SimpleStringProperty(filmTime);	
		this.screeningID = new SimpleStringProperty(screeningID);
	}

	public AddDataToTable(String filmName, String filmDescription, String filmTime, String screeningID, ImageView filmImage) {
		this.filmName = new SimpleStringProperty(filmName);
		this.filmDescription = new SimpleStringProperty(filmDescription);
		this.filmTime = new SimpleStringProperty(filmTime);	
		this.screeningID = new SimpleStringProperty(screeningID);
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

	public String getScreeningID() {
		return screeningID.get();
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
