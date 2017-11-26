package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController {

	
	@FXML
	private Label lblstatus;

	@FXML
	private TextField txtUserName;

	@FXML
	private TextField txtPassword;
	

	/**
	 * Returns a BufferedReader of a file.
	 * @param filePath A String signifying the location of the file.
	 * @return a BufferedReader of the file at location specified by filePath.
	 * @throws FileNotFoundException
	 */
	private BufferedReader getBufferedReaderFromFile(String filePath) throws FileNotFoundException{
		FileReader file = new FileReader (filePath);
		BufferedReader br = new BufferedReader(file);
		return br;
	}

	/**
	 * Method triggered when the user presses the log in Button on the Login window. 
	 * Tries to find a match for the user name and password entered by the user in a file 
	 * of all the possible user name and password pairs. If a match is found, a new window is launched.  
	 * @param event
	 * @throws Exception
	 */
	public void login(ActionEvent event) throws Exception {

		//Storing the user name and password that the user enters into the login TextFields. 
		String usernameAttempt = txtUserName.getText();
		String passwordAttempt = txtPassword.getText();

		//Storing all of the valid user names and passwords in a BufferedReader object.  
		BufferedReader reader = getBufferedReaderFromFile("src/loginDetails/login_details.txt");

		Scanner scanner = new Scanner(reader);
		boolean userMatched = false;

		//Scan through all of the user names and passwords until a match is found
		while (scanner.hasNextLine() && userMatched == false) {
			
			//Create a new Scanner object for each line where the data of each user is stored. 
			String line = scanner.nextLine();
			Scanner lineScanner = new Scanner(line);

			while (lineScanner.hasNext()) {
				
				String userName = lineScanner.next();
				String password = lineScanner.next();

				if (usernameAttempt.equals(userName)) {			// If the user name and
					if (passwordAttempt.equals(password)) {		// password entered is a match...  
						
						//Check to see if user is an administrator:
						if (lineScanner.hasNext() && lineScanner.next().equals("admin")) { 

							//lblstatus.setText("Login as admin is a success!!!");
							lblstatus.getScene().getWindow().hide();
							userMatched = true;
							lineScanner.close();

							// Launch the new admin window:
							try{
								Stage adminStage = new Stage();
								Parent root = FXMLLoader.load(getClass().getResource("/application/AdminMain.fxml"));
								Scene scene = new Scene(root,400,400);
								//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
								adminStage.setScene(scene);
								adminStage.show();
							}
							catch(Exception e){
								System.out.println("Launching Admin Scene failed: "+e);
							}

							break;

						} else /*If the user is NOT an admin (is a regular user)*/ { 
							
							//lblstatus.setText("Login as user is a success!!!");
							lblstatus.getScene().getWindow().hide();
							lineScanner.close();
							userMatched = true;

							// Launch the new user window: 
							try{
								Stage userStage = new Stage();
								Parent root = FXMLLoader.load(getClass().getResource("/user/UserMain.fxml"));
								Scene scene = new Scene(root,400,400);
								scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
								userStage.setScene(scene);
								userStage.show();
							}
							catch(Exception e){
								System.out.println("Launching Admin Scene failed: "+e);
							}

							break;
						}
					} else /*If user name entered is valid but password is wrong*/{
						lblstatus.setText("login failed, incorrect password");
						lineScanner.close();
						break;
					} 
				} else /*If user name or password entered does not match the current item in the lineScanner*/{
					lineScanner.close();
					break;
				}
			}
			if (scanner.hasNextLine() == false && userMatched == false) {	// If we have scanned the whole 
				lblstatus.setText("User name not recognised");		// document of possible users
				lineScanner.close();										// and a match has not been found	
			}
		}

		scanner.close();

	}
	
	


}
