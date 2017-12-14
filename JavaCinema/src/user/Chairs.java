package user;

import javafx.scene.control.Button;

/**
 * Holds data such as the seat Id, whether the seat is available for booking, whether the seat is 
 * selected for booking by the user, and the corresponding button in the makeReservation window.
 */
public class Chairs {
	
	private Button button;
	private String id;
	private boolean bookSeat;
	private boolean occupied;
	
	/**
	 * Chairs object initialized with btn, id, bookSeat, occupied
	 * @param btn
	 * @param id
	 * @param bookSeat
	 * @param occupied
	 */
	public Chairs(Button btn, String id, boolean bookSeat, boolean occupied) {
		this.button = btn;
		this.id = id;
		this.bookSeat = bookSeat; //user has just clicked it
		this.occupied = occupied; //Already occupied by other user 
	}
	
	/**
	 * Chairs object initialized with btn and id
	 * @param btn
	 * @param id
	 */
	public Chairs(Button btn, String id){
		this.button = btn;
		this.id = id;
		this.bookSeat = false;
		this.occupied = false;
	}
	
	/**
	 * 
	 * returns button as Button
	 * 
	 * @return button
	 */
	public Button getButton() {
		return button;
	}
	
	/**
	 * sets Button object
	 * 
	 * @param button
	 */
	public void setButton(Button button) {
		this.button = button;
	}
	
	/**
	 * returns boolean bookseat
	 * 
	 * @return bookSeat
	 */
	public boolean isSelectedForBooking(){
		return bookSeat;
	}
	
	/**
	 * sets bookSeat
	 * 
	 * @param bookSeat
	 */
	public void setBooked(boolean bookSeat) {
		this.bookSeat = bookSeat;
	}
	
	/**
	 * returns boolean occupied
	 * 
	 * @return
	 */
	public boolean isOccupied() {
		return occupied;
	}
	
	/**
	 * sets fixed boolean
	 * 
	 * @param fixed
	 */
	public void setOccupied(boolean fixed) {
		this.occupied = fixed;
	}
	
	/**
	 * returns id as String
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}
	
}

