public class Board { //send to ted :D
	
	private int dimension = 10;
	private int[][] board = new int[dimension][dimension]; //water is 0, missed is -1, hit is 1

	public Board() {}//your target board
	
	public Board(int[][] carrier, int[][] battleship, int[][] destroyer, int[][] submarine, int[][] cruiser) {
		for(int i = 0; i < 5; i++) {
			this.board[carrier[i][0]][carrier[i][1]] = 2;
		}
		for(int i = 0; i < 4; i++) {
			this.board[battleship[i][0]][battleship[i][1]] = 2;
		}
		for(int i = 0; i < 3; i++) {
			this.board[destroyer[i][0]][destroyer[i][1]] = 2;
			this.board[submarine[i][0]][submarine[i][1]] = 2;
		}
		for(int i = 0; i < 2; i++) {
			this.board[cruiser[i][0]][cruiser[i][1]] = 2;
		}
	}
	
	public int getDimension() {
		return this.dimension;
	}
	
	public void addCoordinates(int[][] coordinates) {
		for(int i = 0; i < coordinates.length; i++) {
			this.board[coordinates[i][0]][coordinates[i][1]] = 2;
		}
	}
	
	public boolean gameOver() {
		boolean status = true;
		for(int i = 0; i < this.board.length; i++) {
			for(int j = 0; j < this.board[i].length; j++){		
				if(this.board[i][j] == 2) {
					status = false;
				}
			}
		}	
		return status;
	}
	
	public void changePoint(int[] coordinate, int value) {
		this.board[coordinate[0]][coordinate[1]] = value;
	}
	
	public boolean checkPoint(int[] point) {
		if(board[point[0]][point[1]] == 2) {
			return true;
		}
		return false;
	}
	
	public boolean checkPoint(int[] point, int checkNum) {
		if(board[point[0]][point[1]] == checkNum) {
			return true;
		}
		return false;
	}
	
	public int[][] getBoard(){
		return this.board;
	}
	
	public String printBoard() { //utility
		String print = "";
		for(int i = 0; i < this.board.length; i++) {
			for(int j = 0; j < this.board[i].length; j++){		
				print += board[i][j];
				print += " ";
			}
			print += "\n";
		}
		return print;
	}
	
}
