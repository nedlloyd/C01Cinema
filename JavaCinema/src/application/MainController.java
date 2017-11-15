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
	
	public static void main(String[] args) throws FileNotFoundException {
		FileReader file = new FileReader ("src/loginDetails/login_details.txt");
		BufferedReader reader = new BufferedReader(file);
		Scanner scanner = new Scanner(reader);
		while (scanner.hasNextLine()) {
			String user = scanner.next();
			String password = scanner.next();
			System.out.println(user + " user");
			System.out.println(password + " pass");
		}

	}
	
	public void Login(ActionEvent event) throws FileNotFoundException {
		
		FileReader file = new FileReader ("/loginDetails/login_details.txt");
		BufferedReader reader = new BufferedReader(file);
		Scanner scanner = new Scanner(reader);
		while (scanner.hasNextLine()) {
			String user = scanner.next();
			String password = scanner.next();
			System.out.println(user);
			System.out.println(password);

		}

		
		if (txtUserName.getText().equals("user") && txtPassword.getText().equals("password")) { 
			lblstatus.setText("Login is a success!!!");
		} else {
			lblstatus.setText("login failed");
		}
	}
	

}
