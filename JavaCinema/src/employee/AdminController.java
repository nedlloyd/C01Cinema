package employee;

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
import user.AddDataToTable;
import user.AddImageToTable;

public class AdminController {
	
	@FXML 
	private Button addScreeningButton;
	
	@FXML
	private Label viewingsLbl;
	
	@FXML
	private DatePicker datePicker;
	
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
	private Button logOutButton;
	
	private ObservableList<AddImageToTable> someImages = FXCollections.observableArrayList();
	
	@FXML 
	void initialize(){
		datePicker.setValue(LocalDate.now());	
		//viewingsLbl.setText("Viewings on "+datePicker.getValue().toString());
		//Display viewings on that date
		
		filmNameColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmName"));
        filmDescriptionColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmDescription"));
        filmTimeColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmTime"));
        
        // event listener for datePicker when date is changes films outputted are changed 
        datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
        	try {
        		String theDate = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        		tableView.setItems(getFilms(theDate));
			} catch (IOException | ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        });
        
        filmImage.setImage(new Image("images/cinemaCurtains.png"));
        
        //create even listener on table so by selecting row you can view the film poster
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {      	
			try {
				//sets image which is returned from the getImageFromTableMethod
				filmImage.setImage(getImageFromTable()); 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        
	}
	
	public void changeViewingDayListings(ActionEvent e){
		viewingsLbl.setText("Viewings on "+datePicker.getValue().toString());
		//Change to viewings on the given day
	}

	public void openAddNewScreeningWindow(ActionEvent e){
	
		try {
			Stage newScreeningStage = new Stage();
		    Parent root = FXMLLoader.load(getClass().getResource("/employee/AddScreeningForm.fxml"));
			Scene scene = new Scene(root,500,500);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			newScreeningStage.setScene(scene);
			newScreeningStage.setTitle("New Screening");
			newScreeningStage.show(); 	
			
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		
		
	}
	
	public ObservableList<AddDataToTable>  getFilms() throws ClassNotFoundException, SQLException
	{	
		ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();

		//initialises observable list 
		ObservableList<AddDataToTable> films = FXCollections.observableArrayList();

		//gets the date from the date picker in the correct format
		String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
		System.out.print(date);

		// creates a result set by calling  the detDataFromTwoTables method present in ScreeningDatabase
		ResultSet res = screeningDatabase.getDataFromTwoTables("films", "screenings", date);

		try {
			while (res.next()) {

				//creates variables for each field that we need for the Observable list and then the table
				String name = res.getString("filmName");
				String description = res.getString("filmDescription");
				String time = res.getString("time");

				//initialises AddDataToTable object with constructor that takes the variables we just intialised
				AddDataToTable i = new AddDataToTable(name, description, time);

				//adds AddDataToTable objects to observable list 
				films.add(i);


			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error with populating table form database");
		}

		//returns the observable list 
		return films;
	}
	
	public void getScreenings(ActionEvent e) throws ClassNotFoundException, SQLException {
		tableView.setItems(getFilms());
	}
	
	// creates and returns an observable of the films on a certain date this can then be added to the table
		public ObservableList<AddDataToTable>  getFilms(String date) throws ClassNotFoundException, SQLException, IOException
		{	
			ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();


			//initialises observable list 
			ObservableList<AddDataToTable> films = FXCollections.observableArrayList();


			//gets the date from the date picker in the correct format
			//String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));


			// creates a result set by calling  the detDataFromTwoTables method present in ScreeningDatabase
			ResultSet res = screeningDatabase.getDataFromTwoTables("films", "screenings", date);

			// populates two observableLists on with film data the other with film names and posters 
			try {
				int i = 1;
				while (res.next()) {

					//creates variables for each field that we need for the Observable list and then the table
					String name = res.getString("filmName");
					String description = res.getString("filmDescription");
					String time = res.getString("time");
					InputStream binaryStream = res.getBinaryStream("image");

					//initialises AddDataToTable object with constructor that takes the variables name, time and description
					AddDataToTable nextObject = new AddDataToTable(name, description, time);

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
			
			// gets index of row selected
			TablePosition pos = tableView.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();
			
			// gets film name of object on that row 
			String fn = tableView.getItems().get(row).getFilmName();
			
			//returns the poster that matches the film name 
			for (AddImageToTable item : someImages) {
				if (item.getFilmName() == fn) {
					theImage = item.getFilmImage();
				} 
			}
			return theImage;
		}
		
		/**
		 * Logs out of employee portal (i.e. closes employee portal window)
		 *  and re-opens login window. 
		 *  Triggered when Log out button is pressed.
		 */
		public void logOut(ActionEvent e){
			try {
				Stage primaryStage = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
				Scene scene = new Scene(root,800,500);
				scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.show();
				logOutButton.getScene().getWindow().hide();
				
			} catch(Exception exception) {
				exception.printStackTrace();
			}
			
			
		}

	
	

}
