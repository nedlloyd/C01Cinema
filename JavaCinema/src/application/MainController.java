package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainController {

	@FXML
	private Label lblstatus;

	@FXML
	private TextField txtUserName;

	@FXML
	private TextField txtPassword;

	/*public static void main(String[] args) throws FileNotFoundException {

	} */

	public void Login(ActionEvent event) throws FileNotFoundException {

		String usernameAttempt = txtUserName.getText();
		String passwordAttempt = txtPassword.getText();

		FileReader file = new FileReader ("src/loginDetails/login_details.txt");
		BufferedReader reader = new BufferedReader(file);
		Scanner scanner = new Scanner(reader);
		boolean userMatched = false;
		while (scanner.hasNextLine() && userMatched == false) {

			String line = scanner.nextLine();
			Scanner lineScanner = new Scanner(line);
			System.out.println(line);
			while (lineScanner.hasNext()) {
				String user = lineScanner.next();
				String password = lineScanner.next();
				boolean isAdmin = false;
				if (usernameAttempt.equals(user)) {
					if (passwordAttempt.equals(password)) {
						if (lineScanner.hasNext()) {
							isAdmin = true;
							lblstatus.setText("Login as admin is a success!!!");
							lineScanner.close();
							userMatched = true;
							break;
						} else {
							lblstatus.setText("Login as user is a success!!!");
							lineScanner.close();
							userMatched = true;
							break;
						}
					} else {
						lblstatus.setText("login failed, incorrect password");
						lineScanner.close();
						break;
					} 
				} else {
					break;
				}
			}
			if (scanner.hasNextLine() == false && userMatched ==false) {
				lblstatus.setText("login failed, incorrect username");
				break;	
			}
		}



	}


}
