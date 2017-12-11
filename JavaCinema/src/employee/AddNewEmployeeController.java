package employee;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sqlitedatabases.UsersDatabase;

/**
 * Class defining method and components in AddNewEmployee2.fxml window. 
 * The only method is the createEmployeeAccount method which adds a
 * new employee account to the database. 
 * @author Samuel Bradshaw
 *
 */
public class AddNewEmployeeController {

	@FXML private TextField newEmployeeUsername;
	@FXML private TextField newEmployeeEmail;
	@FXML private TextField newEmployeePassword;
	@FXML private Button createEmployeeBtn;
	@FXML private Label createAccountLbl;

	/**
	 * Takes data entered in NewEmployee.fxml TextFields and adds a new employee account to the database. 
	 * This method is accessed through the NewEmployee.fxml file when the createEmployeeBtn button is pressed. 
	 * @param e
	 */
	public void createEmployeeAccount(ActionEvent e){
		String username = newEmployeeUsername.getText();
		String email = newEmployeeEmail.getText();
		String pw = newEmployeePassword.getText();

		UsersDatabase users = new UsersDatabase();
		boolean usernameTaken = false;

		//If the user name already exists, make the user enter a unique user name and prompt them
		//by changing text on label. 
		try{
			@SuppressWarnings("unused")
			ResultSet results = users.displayRow(username);
			usernameTaken = true;
			createAccountLbl.setText("User name taken.");
		} catch(ClassNotFoundException ex){//We want this exception to be thrown in order for usernameTaken to remain false.
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if(username.isEmpty()==false && email.isEmpty()==false && pw.isEmpty() == false && !usernameTaken){
			try {
				users.createUser(username, pw, email, "employee");
				newEmployeeUsername.getScene().getWindow().hide();

			} catch (ClassNotFoundException | SQLException e1) {
				System.out.println("Error adding new employee to database."
						+ "Error thrown by createEmployeeAccount() method in AdminController class");
				e1.printStackTrace();
			}
		}
	}

}
