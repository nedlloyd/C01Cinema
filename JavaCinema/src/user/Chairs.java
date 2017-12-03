package user;

import javafx.scene.control.Button;

// class in order to hold buttons for booking seats 
public class Chairs {
	
	private Button button;
	private boolean occupied;
	private boolean fixed;
	private int id;
	
	public Chairs(Button btn, int id, boolean occupied, boolean fixed) {
		this.button = btn;
		this.id = id;
		this.occupied = occupied;
		this.fixed = fixed;
		
	}
	
	public Button getButton() {
		return button;
	}
	
	public void setButton(Button button) {
		this.button = button;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	public boolean isFixed() {
		return fixed;
	}
	
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	
	public int getId() {
		return id;
	}
	
}

