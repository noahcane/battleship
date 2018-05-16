public class Board { //send to ted :D
	
	private int dimension = 10;
	private Cell[] board = new Cell[dimension * dimension]; //water is 0, missed is -1, hit is 1

	{
		for(int i = 1; i <= dimension; i++) {
			for(int j = 1; j <= dimension; j++) {
				board[i].x(i);
				board[i].y(j);
				board[i].setData(0);
			}
		}
	}
	
	public Board() {}//your target board
	
	public Board(Cell[] ships) { //always will be 17 cells
		for(int i = 0; i < ships.length; i++) {
			board[cellFinder(ships[i])].setData(2);
		}
	}
	
	/*
	public void addCoordinates(Cell[] coordinates) { //changing the values at certain coordinates
		for(int i = 0; i < coordinates.length; i++) {
			board[coordinates[i].x() + (coordinates[i].y() * 10)].setData(2);

		}
	}
	*/
	
	public int getDimension() {
		return this.dimension;
	}
	
	public boolean gameOver() {
		boolean status = true;
		for(int i = 0; i < dimension * dimension; i++) {
			if(board[i].getData() == 2) {
				status = false;
			}
		}	
		return status;
	}
	
	public void changePoint(Cell coordinate, int value) {
		board[cellFinder(coordinate)].setData(value);
	}

	/*
	public boolean checkPoint(int[] point, int checkNum) {
		if(board[point[0]][point[1]] == checkNum) {
			return true;
		}
		return false;
	}
	*/
	
	public void printBoard() { //utility
		for(int i = 0; i < dimension; i++) {
			if(i % 10 == 0) {
				System.out.println(board[i].getData());
			}else {
				System.out.print(board[i].getData() + " ");
			}
		}
	}
	
	public int cellFinder(Cell c) { //finds a cell depending on coordinates
		return (c.y() * 10) + c.x();
	}
	
}
