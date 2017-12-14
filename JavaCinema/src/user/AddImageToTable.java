package user;


import javafx.scene.image.Image;

/**
 * Class used for storing the images, names and descriptions of films from the
 * films database.
 *
 */
public class AddImageToTable {
	private Image filmImage;
	private String filmName, filmDescription;
	
	/**
	 * AddImageToTable Object initialized with filmImage and filmName
	 * @param filmImage
	 * @param filmName
	 */
	public AddImageToTable(Image filmImage, String filmName) {
		this.filmImage = filmImage;
		this.filmName = filmName;
	}
	
	/**
	 * AddImageToTable Object initialized with filmImage and description
	 * @param filmName
	 * @param description
	 */
	public AddImageToTable(String filmName, String description) {
		this.filmName = filmName;
		this.filmDescription = description;
	}
	
	/**
	 * AddImageToTable Object initialized with filmImage and filmName and filmDescription
	 * 
	 * @param filmImage
	 * @param filmName
	 * @param filmDescription
	 */
	public AddImageToTable(Image filmImage, String filmName, String filmDescription) {
		this.filmImage = filmImage;
		this.filmName = filmName;
		this.filmDescription = filmDescription;
	}
	
	/**
	 * Image returned as Image
	 * @return
	 */
	public Image getFilmImage() {
		return filmImage;
	}
	
	/**
	 * film Image set
	 * @param filmImage
	 */
	public void setFilmImage(Image filmImage) {
		this.filmImage = filmImage;
	}
	
	/**
	 * filmName returned as String
	 * @return
	 */
	public String getFilmName() {
		return filmName;
	}
	
	/**
	 * film description returned as String
	 * @return
	 */
	public String getFilmDescription() {
		return filmDescription;
	}
}
