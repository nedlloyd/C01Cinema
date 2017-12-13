package user;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sqlitedatabases.UsersDatabase;

public class EditUserProfileController {
	@FXML private Button edit1;
	@FXML private Button edit2;
	@FXML private Button edit3;
	@FXML private Button confirm;
	@FXML private Button backToProfile;

	@FXML private TextField username; 
	@FXML private TextField email; 
	@FXML private TextField password; 
	
	@FXML private Label confirmMessage;
	
	private int currentUserID;
	private String currentUsername;
	private String currentEmail;
	private String currentPassword;

	
	@FXML
	public void initialize() {
		confirm.setVisible(false);
	}
	
	/**
	 * method called in 'ViewUserProfileController' in order to set variable 'username' in this class
	 * @param username
	 */
	public void setUsername(String username) {
		this.username.setText(username);
	}

	/**
	 * method called in 'ViewUserProfileController' in order to set variable 'email' in this class
	 * @param username
	 */
	public void setEmail(String email) {
		this.email.setText(email);
	}

	/**
	 * Queries the 'UserDatabase' to find the password associated with username given.  The TextField 'password'
	 * is then set to display this password.  
	 * 
	 * @param username
	 */
	public void setPassword(String username) {
		
		UsersDatabase ud = new UsersDatabase();
		try {	

			ResultSet res = ud.displayRow(username);
			if(res != null) {
				currentPassword = res.getString("password");
				currentUserID = res.getInt("userID");
			}
			res.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.password.setText(currentPassword);
	}
	
	
	/**
	 * When Button is pressed the 'confirm' button is set to visible and 'confirmMessage' Label is set to a String
	 * @param e
	 */
	public void update(ActionEvent e) {
		confirm.setVisible(true);
		confirmMessage.setText("Are you sure you want to confirm these updates?");
	}
	
	/**
	 * When Button is pressed variables 'currentUsername', 'currentEmail' and 'currentPassword' are all set to the 
	 * text contained in the TextFields.  The 'UserDatabase' is then updated to reflect any changes that 
	 * have been made to the original text set.  
	 * @param e
	 */
	public void confirm(ActionEvent e) {
		UsersDatabase ud = new UsersDatabase();
		this.currentUsername = username.getText();
		this.currentEmail = email.getText();
		this.currentPassword = password.getText();
		try {
			ud.updateUserInfo(currentUsername, currentEmail, currentPassword, currentUserID);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		confirmMessage.setText("Changes saved");
		confirm.setVisible(false);
		
	}
	
	

}
