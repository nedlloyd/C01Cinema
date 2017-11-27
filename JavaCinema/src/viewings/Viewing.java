package viewings;

import java.time.LocalDate;

public class Viewing {
	
	int startTimeHour;
	int startTimeMin;
	LocalDate startDate; 
	Film film;
	boolean[][] bookedSeats = new boolean[10][10];
	
	public Viewing(Film film, int startTimeHour, int startTimeMin, LocalDate date){
		this.film = film;
		this.startTimeHour = startTimeHour;
		this.startTimeMin = startTimeMin;
		this.startDate = date;
			
	}
	
	public void bookSeat(int row, int col){
		bookedSeats[row][col] = true;
	}
	
	

}
