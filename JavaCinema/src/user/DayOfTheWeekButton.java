package user;

import java.time.LocalDate;

import javafx.scene.control.Button;

public class DayOfTheWeekButton {
	
	Button button;
	LocalDate date;
	
	public DayOfTheWeekButton(Button b, LocalDate d){
		this.button = b;
		this.date = d;
	}
	
	public Button getButton(){
		return this.button;
	}
	
	public LocalDate getDate(){
		return this.date;
	}
	
	
	
	
	

}
