package user;

import javafx.scene.control.Button;

// class in order to hold buttons for booking seats 
public class Chairs {
	
	private Button button;
	private String id;
	private boolean bookSeat;
	private boolean occupied;
	
	
	public Chairs(Button btn, String id, boolean bookSeat, boolean occupied) {
		this.button = btn;
		this.id = id;
		this.bookSeat = bookSeat; //user has just clicked it
		this.occupied = occupied; //Already occupied by other user 
	}
	
	public Chairs(Button btn, String id){
		this.button = btn;
		this.id = id;
		this.bookSeat = false;
		this.occupied = false;
	}
	
	public Button getButton() {
		return button;
	}
	
	public void setButton(Button button) {
		this.button = button;
	}
	
	public boolean isSelectedForBooking(){
		return bookSeat;
	}
	
	public void setBooked(boolean bookSeat) {
		this.bookSeat = bookSeat;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	public void setOccupied(boolean fixed) {
		this.occupied = fixed;
	}
	
	public String getId() {
		return id;
	}
	
}

