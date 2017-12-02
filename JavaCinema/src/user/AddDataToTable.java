package user;


import java.time.LocalTime;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

public class AddDataToTable {
		private SimpleStringProperty filmName, filmDescription, filmTime;

		
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
}
