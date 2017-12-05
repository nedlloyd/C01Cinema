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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sqlitedatabases.ScreeningsDatabase;

public class UserMainController {
	
	@FXML
	private Label helloMessage;
	@FXML
	private Button seeScreenings;
	@FXML
	private Label viewingsUser;
	@FXML
	private DatePicker datePickerUser; 
	@FXML 
	private TableView<AddDataToTable> tableView;
	@FXML 
	private TableColumn<AddDataToTable, String> filmNameColumn;
	@FXML 
	private TableColumn<AddDataToTable, String> filmDescriptionColumn;
	@FXML 
	private TableColumn<AddDataToTable, String> filmTimeColumn;
	@FXML
	private ImageView filmImage;
	@FXML 
	private Button makeReservation;
	@FXML
	private Label selectFilm;

	private ObservableList<AddImageToTable> someImages = FXCollections.observableArrayList();
	private ObservableList<AddDataToTable> films = FXCollections.observableArrayList();
	private String currentFilm;
	private int screeningID;
	private String user;

	@FXML
	public void initialize() throws ClassNotFoundException, SQLException{	
		
		//Display current time
		datePickerUser.setValue(LocalDate.now());
		
		//set up columns in the table
		filmNameColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmName"));
        filmDescriptionColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmDescription"));
        filmTimeColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmTime"));
        
        // event listener for datePicker when date is changes films outputted are changed 
        datePickerUser.valueProperty().addListener((ov, oldValue, newValue) -> {
        	try {
        		String theDate = datePickerUser.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        		tableView.setItems(getFilms(theDate));
			} catch (IOException | ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        });
        
        //create even listener on table so by selecting row you can view the film poster
        // it also sets the variable filmName to the film selected
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {      	
			try {
				//sets image which is returned from the getImageFromTableMethod
				TablePosition pos = tableView.getSelectionModel().getSelectedCells().get(0);
				int row = pos.getRow();
				// sets variable current film to the film selected 
				currentFilm = tableView.getItems().get(row).getFilmName();
				//sets image based on film selected
				filmImage.setImage(getImageFromTable()); 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        
	}
	
	
	// creates and returns an observable of the films on a certain date this can then be added to the table
	// also gets the film poster and places it in another observableList
	// both in the same method as both based on the same querry
	public ObservableList<AddDataToTable>  getFilms(String date) throws ClassNotFoundException, SQLException, IOException
	{	
		ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();
		
		// creates a result set by calling  the detDataFromTwoTables method present in ScreeningDatabase
		ResultSet res = screeningDatabase.getDataFromTwoTables("films", "screenings", date);
		films.clear();
		// populates two observableLists on with film data the other with film names and posters 
		try {
			int i = 1;
			while (res.next()) {

				//creates variables for each field that we need for the Observable list and then the table
				String name = res.getString("filmName");
				String description = res.getString("filmDescription");
				String time = res.getString("time");
				InputStream binaryStream = res.getBinaryStream("image");
				String id = res.getString("screeningID");

				//initialises AddDataToTable object with constructor that takes the variables name, time and description and id
				AddDataToTable nextObject = new AddDataToTable(name, description, time, id);

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
					AddImageToTable nextImage = new AddImageToTable(image, name);
					//it is then added to an observableList 
					someImages.add(nextImage);
				}				
				//adds AddDataToTable objects to observable list 
				films.add(nextObject);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error with populating table form database");
		}
		//returns the observable list 
		return films;
	}

	
	// finds out what row of table is selected, gets the name of the film and returns the correct poster
	public Image getImageFromTable() throws IOException {
		
		// image is initialised to curtains in case film does not contain a photo
		Image theImage = new Image("images/cinemaCurtains.png");
		
		//returns the poster that matches the film name 
		for (AddImageToTable item : someImages) {
			if (item.getFilmName() == currentFilm) {
				theImage = item.getFilmImage();
			} 
		}
		
		
		return theImage;
	}
	
	// when the make a reservation button is pressed the make a reservation window is opened
	// variables of filmName, screeningid and user are also passed onto the next controller
	public void makeReservation(ActionEvent e) {
		String theDate = datePickerUser.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
		try {
			// variable screeningID set to currently sleected films screening id
			screeningID();
			if (screeningID != 0) {
				Stage newReservationStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Parent root = loader.load(getClass().getResource("/user/MakeReservation.fxml").openStream());
				//calls up reservation controller allowing vraibles to be set from current controller
				MakeReservationController reservationController = (MakeReservationController)loader.getController();
				// uses the setScreening method from reservationController in order to pass the variable screeningID and set the seats bases on whether they are reserved
				reservationController.setSeats(screeningID);
				//does the same but with user
				reservationController.setUser(user);
				Scene scene = new Scene(root,500,500);
				//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				newReservationStage.setScene(scene);
				newReservationStage.setTitle("Select a seat");
				newReservationStage.show(); 	
			} else {
				selectFilm.setText("please select a screening");
			}
			
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	
	// returns the Screening ID for the film selected
	public void screeningID() {
		
		for (AddDataToTable item : films) {
			if (item.getFilmName() == currentFilm) {
				Integer si = Integer.parseInt(item.getScreeningID());
				screeningID = si;
			} 
		}
	}
	//method in order to recieve variable username from login controller and set it as a variable 
	public void setuserID(String user) {
		this.user = user;
	}
	
	
}
