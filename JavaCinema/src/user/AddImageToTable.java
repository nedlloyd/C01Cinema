package user;

import java.sql.Blob;

import javafx.scene.image.Image;

public class AddImageToTable {
	private Image filmImage;
	private String filmName;
	 
	public AddImageToTable(Image filmImage, String filmName) {
		this.filmImage = filmImage;
		this.filmName = filmName;
	}
	
	public Image getFilmImage() {
		return filmImage;
	}
	
	
	public void setFilmImage(Image filmImage) {
		this.filmImage = filmImage;
	}
	
	public String getFilmName() {
		return filmName;
	}
}
