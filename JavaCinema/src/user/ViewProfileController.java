package user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sqlitedatabases.ReservationsDatabase;
import sqlitedatabases.ScreeningsDatabase;
import sqlitedatabases.UsersDatabase;

/**
 * Controller class for the View Profile window. Contains methods for receiving data 
 * from the database and populating a table with the user's reservations.
 */
public class ViewProfileController {

	@FXML private TableView<AddDataToTable> tableView;
	@FXML private TableColumn<AddDataToTable, String> filmNameColumn;
	@FXML private TableColumn<AddDataToTable, Integer> reservationIDColumn;
	@FXML private TableColumn<AddDataToTable, String> filmTimeColumn;
	@FXML private TableColumn<AddDataToTable, String> filmDateColumn;
	@FXML private TableColumn<AddDataToTable, Integer> seatIDColumn;
	@FXML private ImageView filmImage;
	@FXML private Button deleteReservation;
	@FXML private Button editLogin;
	@FXML private Label name;
	@FXML private Label email;
	@FXML private Label filmDuration;
	@FXML private Label bookingRemovedLbl;

	private ObservableList<AddDataToTable> reservationsData = FXCollections.observableArrayList();
	private ObservableList<AddImageToTable> someImages = FXCollections.observableArrayList();
	private String currentFilm;
	private String currentUser;
	private String currentEmail;
	private int currentID;
	private int userID;
	private LocalDate today;
	
	UserMainController userMain;

	@FXML
	public void initialize() {	
		
		//set up columns in the table
		reservationIDColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, Integer>("reservationID"));
		filmNameColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmName"));
		filmDateColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmDate"));
		filmTimeColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmTime"));
		seatIDColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, Integer>("seatID"));

		//Populating the class with data from the database:
		try {
			getReservationsData();
			tableView.setItems(reservationsData);
		} catch (ClassNotFoundException | SQLException | IOException e1) {
			e1.printStackTrace();
			System.out.println("Error populating table with reservation data. Error with getFilms() method");
		}

		//event listener setting 'currentFilm' to film selected, then using this to set the correct poster and description 
		tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			//sets image which is returned from the getImageFromTableMethod
			int rowIndex = tableView.getSelectionModel().getSelectedIndex();
			// sets variable current film to the film selected 
			if(rowIndex >=0 && rowIndex < tableView.getItems().size()){
				currentFilm = tableView.getItems().get(rowIndex).getFilmName();
				currentID = tableView.getItems().get(rowIndex).getReservationID();
				try {
					setImageAndDescription();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});  
		
		tableView.getStylesheets().add(getClass().getResource("tableview.css").toExternalForm());
		tableView.getSortOrder().add(filmDateColumn);
	
	}//End of initialize() method

	/**
	 * Queries 'ScreeningsDatabase' for all screenings with userID selected. 
	 * 
	 * 'addImageToList' method is then called and set to 'film name' 'film description' and, if it exists, 'binaryStream'.
	 * An AddDataToTable object is created taking 'filmName', 'screeningTime', 'screeningDate', 'seatID' and 
	 * 'filmReservationID'. For each row in the Result Set this object is added to the ObservableList 'reservationsData'.
	 * @param date
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	private void  getReservationsData() throws ClassNotFoundException, SQLException, IOException {	

		reservationsData.clear();

		ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();

		// creates a result set by calling  the detDataFromTwoTables method present in ScreeningDatabase
		ResultSet res = screeningDatabase.queryForProfile(userID);

		// populates two observableLists on with film data the other with film names and posters 		
		while (res.next()) {

			//We only want to display future reservations. 
			//If the reservation is from a screening which has passed, do not display to user.
			String screeningDate = res.getString("date");

			String filmName = res.getString("filmName");
			String screeningTime = res.getString("time");
			String filmDescription = res.getString("filmDescription");
			String seatID = res.getString("seatID");
			int filmReservationID = res.getInt("reservationID");
			InputStream binaryStream = res.getBinaryStream("image");

			if (binaryStream != null) {
				addImageToList(binaryStream, filmName, filmDescription);
			} else {
				addImageToList(filmName, filmDescription);
			}

			//initialises AddDataToTable object with constructor that takes the variables name, time and description and id
			AddDataToTable nextObject = new AddDataToTable(filmName, screeningTime, screeningDate, seatID, filmReservationID);		
			//adds AddDataToTable objects to observable list 
			reservationsData.add(nextObject);
		}
		res.close();	
	}
	

	/**
	 * Sets 'tableView' with all reservations in ObservableList 'reservationsData'
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void viewAllReservations() throws ClassNotFoundException, SQLException, IOException {
		getReservationsData();
		tableView.setItems(reservationsData);
	}



	/**
	 * Converts binary stream to image, creates an addImageToTable object and adds object to observableList
	 * @param binaryStream
	 * @param filmName
	 * @param description
	 * @throws IOException
	 */
	public void addImageToList(InputStream binaryStream, String filmName, String description) throws IOException {
		//ensures that there is a photo for a certain film 
		if (binaryStream != null) {
			//creates file 
			OutputStream os = new FileOutputStream (new File("photo.jpg"));
			byte[] content = new byte[1024];
			int size = 0;
			//while binary Stream holds more than -1
			while ((size = binaryStream.read(content)) != -1) {
				//os becomes an image
				os.write(content, 0, size);
			}
			os.close();
			binaryStream.close();

			//image variable becomes the file we just wrote
			Image image = new Image("file:photo.jpg", 100, 150, true, true);

			//image becomes an AddImageToTable
			AddImageToTable nextImage = new AddImageToTable(image, filmName, description);
			//it is then added to an observableList 
			someImages.add(nextImage);
		}				
	}

	/**
	 * Creates an addImageToTable object and adds object to ObservableList 'reservationsData'
	 * @param filmName
	 * @param description
	 * @throws IOException
	 */
	public void addImageToList(String filmName, String description) throws IOException {

		//image becomes an AddImageToTable
		AddImageToTable nextImage = new AddImageToTable(filmName, description);
		//it is then added to an observableList 
		someImages.add(nextImage);
	}	

	/**
	 * Sets image and description based upon current film selected
	 * @throws IOException
	 */
	public void setImageAndDescription() throws IOException {

		//clears the previous film and description
		filmImage.setImage(null);

		// image is initialised to curtains in case film does not contain a photo
		Image theImage = new Image("images/cinema_curtains.png", 100, 150, true, true);

		//returns the poster that matches the film name 
		for (AddImageToTable item : someImages) {
			if (item.getFilmName() == currentFilm) {
				if (item.getFilmImage() != null) {
					theImage = item.getFilmImage();
				}
			} 

			filmImage.setImage(theImage);
		}
	}


	/**
	 * Method called in UserMainController to pass variable 'username' to this controller. This variable is used 
	 * to obtain userID and email both of which are set as variables for this class
	 * @param user
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void setUserEmailName(String user) throws ClassNotFoundException, SQLException {
		UsersDatabase ud = new UsersDatabase();
		//finds the userID based on the user name
		ResultSet res = ud.displayRow(user);


		int id = 0;
		while (res.next()) {
			id = res.getInt("userID");
			currentEmail = res.getString("email");			
		}

		this.currentUser = user;
		this.userID = id;
		email.setText("Email: " + currentEmail);
		this.name.setText("Name: " + user);			
	}

	/**
	 * Deletes reservation selected on table (by removing row in 'ReservationsDatabase') and updates the tableView
	 * @param e
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void deleteReservation(ActionEvent e) throws ClassNotFoundException, SQLException, IOException {
		ReservationsDatabase rd = new ReservationsDatabase();
		
		String filmName = tableView.getSelectionModel().getSelectedItem().getFilmName();
		String date = tableView.getSelectionModel().getSelectedItem().getFilmDate();
		String time = tableView.getSelectionModel().getSelectedItem().getFilmTime();
		String seat = tableView.getSelectionModel().getSelectedItem().getSeatID();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		LocalDate dateObject = LocalDate.parse(date, formatter);
		
		today = LocalDate.now();
		
		if(dateObject.isAfter(today)){
			rd.delete(currentID);
			//Display a label showing the user that the booking has been successfully deleted:
			bookingRemovedLbl.setText("Booking deleted: "+filmName+" "+time+" "+date+" seat "+seat);		
		} else {
			bookingRemovedLbl.setText("Sorry, it is only possible to delete future reservations");
		}

		//Refresh the items in the table; 
		viewAllReservations();

	}

	/**
	 * Opens editUser Window.
	 * @param e
	 */
	public void editUserInfo(ActionEvent e) {

		try {
			// variable screeningID set to currently selected films screening id
			Stage newEditProfileController = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/user/EditUserInfo.fxml").openStream());

			//calls up reservation controller allowing variables to be set from current controller

			EditUserProfileController eupc  = (EditUserProfileController)loader.getController();
			// uses the setScreening method from reservationController in order to pass the variable screeningID and set the seats bases on whether they are reserved

			eupc.setUsername(currentUser);
			eupc.setEmail(currentEmail);
			eupc.setPassword(currentUser);
			eupc.setUserMainController(userMain);
			
			Scene scene = new Scene(root,500,320);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			newEditProfileController.setScene(scene);
			newEditProfileController.setTitle("User Info");
			newEditProfileController.show(); 	
			tableView.getScene().getWindow().hide();

		} catch(Exception exc) {
			exc.printStackTrace();
		}

	}
	
	/**
	 * Links the ViewProfileController to the UserMainController object which birthed it.
	 * @param userMain
	 */
	public void setUserMainController(UserMainController userMain){
		this.userMain = userMain;
	}

}
