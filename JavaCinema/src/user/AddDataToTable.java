package user;


import java.time.LocalTime;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;


public class AddDataToTable {
		private SimpleStringProperty filmName, filmDescription, filmTime, screeningID;

		public AddDataToTable(String filmName, String filmDescription, String filmTime, String screeningID) {
			this.filmName = new SimpleStringProperty(filmName);
			this.filmDescription = new SimpleStringProperty(filmDescription);
			this.filmTime = new SimpleStringProperty(filmTime);	
			this.screeningID = new SimpleStringProperty(screeningID);
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
}
