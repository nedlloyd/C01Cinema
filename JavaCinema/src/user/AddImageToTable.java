package user;


import javafx.scene.image.Image;

public class AddImageToTable {
	private Image filmImage;
	private String filmName, filmDescription;
	 
	public AddImageToTable(Image filmImage, String filmName) {
		this.filmImage = filmImage;
		this.filmName = filmName;
	}
	
	public AddImageToTable(String filmName, String description) {
		this.filmName = filmName;
		this.filmDescription = description;
	}
	
	public AddImageToTable(Image filmImage, String filmName, String filmDescription) {
		this.filmImage = filmImage;
		this.filmName = filmName;
		this.filmDescription = filmDescription;
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
	
	public void setFilmDescription(String filmDescription) {
		this.filmDescription = filmDescription;
	}
	
	public String getFilmDescription() {
		return filmDescription;
	}
}
