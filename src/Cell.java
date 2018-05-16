
public class Cell {
	
	private int x;
	private int y;
	private int data;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int x() {
		return this.x;
	}
	
	public int y() {
		return this.y;
	}
	
	public int getData() {
		return this.data;
	}
	
	public void x(int x) {
		this.x = x;
	}
	
	public void y(int y) {
		this.y = y;
	}
	
	public void setData(int d) {
		this.data = d;
	}
	
	public boolean equals(Object c2) { //check if two cells have the same data value
		if(this.data == ((Cell) c2).getData()) {
			return true;
		}
		return false;
	}
	
}