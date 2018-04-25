import java.util.*;

public class aiBoard{
	
	private int dimension = 10;
	private bNode[][] gameBoard = new bNode[dimension][dimension];
	private int[][] ships = new int[17][2];
	
	private int mode; //0 is neutral, 1 is attack
	
	private boolean secondHit; //determine notation
	private int misses; //2 misses = finally, we are done here
	private int[] initialHit; //first hit spot
	private int[] lastHit;
	private int direction; //0 is right, 1 is left; 2 is up, 3 is down
	
	public aiBoard(int[][][] s) {
		for(int i = 0; i < dimension; i++) {
			for(int j = 0; j < dimension; j++) {
				gameBoard[i][j] = new bNode();
			}
		}
		int counter = 0;
		for(int i = 0; i < s.length; i++) {
			for(int j = 0; j < s[i].length; j++) {
				this.ships[counter] = s[i][j];
				counter++;
			}
		}
		this.mode = 0;
		this.direction = 0;
		this.misses = 0;
		this.initialHit = new int[2];
		this.lastHit = new int[2];
		this.secondHit = false;
	}
	
	public void initializeBoard() { //setting all nodes to each other
		gameBoard[0][0].setE(gameBoard[0][1]);
		gameBoard[0][0].setS(gameBoard[1][0]);
		gameBoard[0][9].setS(gameBoard[1][9]);
		gameBoard[0][9].setW(gameBoard[0][8]);
		gameBoard[9][0].setE(gameBoard[9][1]);
		gameBoard[9][0].setN(gameBoard[8][0]);
		gameBoard[9][9].setW(gameBoard[9][8]);
		gameBoard[9][9].setN(gameBoard[8][9]);
		for(int i = 0; i < dimension; i++) {
			for(int j = 0; j < dimension; j++) {
				if(i > 0 && i < 9 && j > 0 && j < 9) { //all nodes with 8 open spaces around it
					gameBoard[i][j].setN(gameBoard[i - 1][j]);
					gameBoard[i][j].setE(gameBoard[i][j + 1]);
					gameBoard[i][j].setS(gameBoard[i + 1][j]);
					gameBoard[i][j].setW(gameBoard[i][j - 1]);
				}else {
					if(i == 0 && j > 0 && j < 9) { //all nodes with 5 open spaces around it
						gameBoard[i][j].setS(gameBoard[i + 1][j]);
						gameBoard[i][j].setE(gameBoard[i][j + 1]);
						gameBoard[i][j].setW(gameBoard[i][j - 1]);
					}else if(i == 9 && j > 0 && j < 9) {
						gameBoard[i][j].setN(gameBoard[i - 1][j]);
						gameBoard[i][j].setE(gameBoard[i][j + 1]);
						gameBoard[i][j].setW(gameBoard[i][j - 1]);
					}else if(j == 0 && i > 0 && i < 9) {
						gameBoard[i][j].setN(gameBoard[i - 1][j]);
						gameBoard[i][j].setE(gameBoard[i][j + 1]);
						gameBoard[i][j].setS(gameBoard[i + 1][j]);
					}else if(j == 9 && i > 0 && i < 9) {
						gameBoard[i][j].setN(gameBoard[i - 1][j]);
						gameBoard[i][j].setS(gameBoard[i + 1][j]);
						gameBoard[i][j].setW(gameBoard[i][j - 1]);
					}
				}
			}
		}
		for(int i = 0; i < dimension; i++) { //initialize board with priorities
			for(int j = 0; j < dimension; j++) {
				int priority = 0;
				if(gameBoard[i][j].getN() != null) {
					priority++;
				}
				if(gameBoard[i][j].getE() != null) {
					priority++;
				}
				if(gameBoard[i][j].getS() != null) {
					priority++;
				}
				if(gameBoard[i][j].getW() != null) {
					priority++;
				}
				gameBoard[i][j].setPriority(priority);
			}
		}
	}
	
	/*
	public String shipStatus() {
		if(gameBoard[ships[0][0][0]][ships[0][0][1]] == null && gameBoard[ships[0][1][0]][ships[0][1][1]] == null && gameBoard[ships[0][2][0]][ships[0][2][1]] == null && gameBoard[ships[0][3][0]][ships[0][3][1]] == null && gameBoard[ships[0][4][0]][ships[0][4][1]] == null) {
			return "Your carrier has been SUNK";
		}else if(gameBoard[ships[1][0][0]][ships[1][0][1]] == null && gameBoard[ships[1][1][0]][ships[1][1][1]] == null && gameBoard[ships[1][2][0]][ships[1][2][1]] == null && gameBoard[ships[1][3][0]][ships[1][3][1]] == null) {
			return "Your battleship has been SUNK";
		}else if(gameBoard[ships[2][0][0]][ships[2][0][1]] == null && gameBoard[ships[2][1][0]][ships[2][1][1]] == null && gameBoard[ships[2][2][0]][ships[2][2][1]] == null) {
			return"Your submarine has been SUNK";
		}else if(gameBoard[ships[3][0][0]][ships[3][0][1]] == null && gameBoard[ships[3][1][0]][ships[3][1][1]] == null && gameBoard[ships[3][2][0]][ships[3][2][1]] == null) {
			return "Your destroyer has been SUNK";
		}else if(gameBoard[ships[4][0][0]][ships[4][0][1]] == null && gameBoard[ships[4][1][0]][ships[4][1][1]] == null) {
			return "Yo'er cruiser has been SUNK";
		}
		return "";
	}
	*/
	
	public int[] getCoordinate() { //neutral mode
		Random rand = new Random();
		int num = 0;
		ArrayList<int[]> possibleCoordinates = new ArrayList<int[]>();
		double[] numbers = new double[4];
		for(int i = 0; i < gameBoard.length; i++) {
			for(int j = 0; j < gameBoard[i].length; j++) {
				if(gameBoard[i][j].getPriority() == 4) {
					numbers[0] += 1;
				}else if(gameBoard[i][j].getPriority() == 3) {
					numbers[1] += 1;
				}else if(gameBoard[i][j].getPriority() == 2) {
					numbers[2] += 1;
				}else if(gameBoard[i][j].getPriority() == 1) {
					numbers[3] += 1;
				}
			}
		}
		numbers[0] *= 1.3;
		numbers[1] *= 1.1;
		numbers[2] *= 0.9;
		numbers[3] *= 0.7; //inflate or deflate higher and lower probabilities
		double total = numbers[0] + numbers[1] + numbers[2] + numbers[3];
		numbers[1] += numbers[0];
		numbers[2] += numbers[1];
		numbers[3] += numbers[2];
		double rdm = rand.nextDouble() * total + 1;
		if(rdm > 0 && rdm < numbers[0]) {
			num = 4;
		}else if(rdm > numbers[0] && rdm < numbers[1]) {
			num = 3;
		}else if(rdm > numbers[1] && rdm < numbers[2]) {
			num = 2;
		}else if(rdm > numbers[2] && rdm < numbers[3]) {
			num = 1;
		}
		for(int i = 0; i < gameBoard.length; i++) {
			for(int j = 0; j < gameBoard[i].length; j++) {
				if(gameBoard[i][j].getPriority() == num) {
					int[] addr = {i, j};
					possibleCoordinates.add(addr);
				}
			}
		}
		int coo = rand.nextInt(possibleCoordinates.size());
		return possibleCoordinates.get(coo);
	}
	
	public int[] getSecondHit() {
		int[] spot = new int[2];
		if(gameBoard[initialHit[0]][initialHit[1]].getN() != null && gameBoard[initialHit[0]][initialHit[1]].getN().getPriority() > 0) {
			spot[0] = initialHit[0] - 1;
			spot[1] = initialHit[1];
		}else if(gameBoard[initialHit[0]][initialHit[1]].getE() != null && gameBoard[initialHit[0]][initialHit[1]].getE().getPriority() > 0) {
			spot[0] = initialHit[0];
			spot[1] = initialHit[1] + 1;
		}else if(gameBoard[initialHit[0]][initialHit[1]].getS() != null && gameBoard[initialHit[0]][initialHit[1]].getS().getPriority() > 0) {
			spot[0] = initialHit[0] + 1;
			spot[1] = initialHit[1];
			misses = 1;
		}else { 
			spot[0] = initialHit[0];
			spot[1] = initialHit[1] - 1;
			misses = 1;
		}
		return spot;
	}
	
	public void checkHit(int[] coordinate) {
		for(int i = 0; i < ships.length; i++) {
			if(coordinate[0] == ships[i][0] && coordinate[1] == ships[i][1]) {//indicates hit again
				secondHit = true;
				lastHit = coordinate; 
				if(coordinate[0] == initialHit[0]) {//y coords match means same vert column
					if(initialHit[1] == coordinate[1] + 1) {//what direction are we tracking 
						direction = 0;// track east
					}else {
						direction = 1;// track west
					}
				}else {//n x coords match
					if(initialHit[0] == coordinate[0] - 1) { // north or south?
						direction = 2;//track North
					}else {
						direction = 3;//south
					}
				}
			}
		}
	}
	
	public int[] getHitCoordinate() {
		if(misses < 2) {
			int[] newCoord = new int[2];
			if(direction == 0) {
				if(gameBoard[lastHit[0]][lastHit[1]].getE() != null && gameBoard[lastHit[0]][lastHit[1]].getE().getPriority() != 0) {
					newCoord[0] = lastHit[0]; //check if the hit is a miss, e.t.c. (just because the slot is open doesn't mean it's a hit *TODO
					newCoord[1] = lastHit[1];
					newCoord[1]++;
					return newCoord; //new coordinate pair instead of lastHit
				}else {
					direction = 1; //don't assume that the opposite point is valid. *TODO
					misses++;
					newCoord[0] = initialHit[0];
					newCoord[1] = initialHit[1];
					newCoord[1]--;
					return newCoord;
				}
			}else if(direction == 1) {
				if(gameBoard[lastHit[0]][lastHit[1]].getW() != null && gameBoard[lastHit[0]][lastHit[1]].getW().getPriority() > 0) {
					newCoord[0] = lastHit[0];
					newCoord[1] = lastHit[1];
					newCoord[1]--;
					return newCoord;
				}else {
					direction = 0;
					misses++;
					newCoord[0] = initialHit[0];
					newCoord[1] = initialHit[1];
					newCoord[1]++;
					return newCoord;
				}
			}else if(direction == 2) {
				if(gameBoard[lastHit[0]][lastHit[1]].getN() != null && gameBoard[lastHit[0]][lastHit[1]].getN().getPriority() > 0) {
					newCoord[0] = lastHit[0];
					newCoord[1] = lastHit[1];
					newCoord[0]--;
					return newCoord;
				}else {
					direction = 3;
					misses++;
					newCoord[0] = initialHit[0];
					newCoord[1] = initialHit[1];
					newCoord[0]++;
					return newCoord;
				}
			}else if(direction == 3) {
				if(gameBoard[lastHit[0]][lastHit[1]].getS() != null && gameBoard[lastHit[0]][lastHit[1]].getS().getPriority() > 0) {
					newCoord[0] = lastHit[0];
					newCoord[1] = lastHit[1];
					newCoord[0]++;
					return newCoord;
				}else {
					direction = 2;
					misses++;
					newCoord[0] = initialHit[0];
					newCoord[1] = initialHit[1];
					newCoord[0]--;
					return newCoord;
				}
			}
		}
		this.mode = 0;
		this.direction = 0;
		this.misses = 0;
		this.secondHit = false;
		return this.getCoordinate();
	}
	
	public boolean checkCoordinate(int[] coordinate) {
		for(int i = 0; i < ships.length; i++) {
			if(coordinate[0] == ships[i][0] && coordinate[1] == ships[i][1]) {
				return true;
			}
		}
		return false;
	}
	
	public int[] updateBoard() { //update board with new priorities and determines hit and misses
		int[] coordinate = new int[2];
		if(mode == 1) {
			if(secondHit == false) { //THIS ALSO WORKS
				coordinate = this.getSecondHit();
				this.checkHit(coordinate);
			}else {// already traking a particular direction
				coordinate = this.getHitCoordinate(); // change lastHit after confirming the hit
			}
			if(this.checkCoordinate(coordinate) == true) {
				lastHit[0] = coordinate[0];
				lastHit[1] = coordinate[1];
			}
			gameBoard[coordinate[0]][coordinate[1]].setPriority(0);
		}else {// mode == 0 (normal mode)
			coordinate = this.getCoordinate(); //THIS WORKS
			gameBoard[coordinate[0]][coordinate[1]].setPriority(0); 
			for(int i = 0; i < dimension; i++) {
				for(int j = 0; j < dimension; j++) {
					if(gameBoard[i][j].getPriority() != 0) {
						int priority = 0;
						if(gameBoard[i][j].getN() != null) {
							if(gameBoard[i][j].getN().getPriority() > 0) { //CHECK IF NULL BEFORE ACCESSING VALUE
								priority++;
							}
						}
						if(gameBoard[i][j].getE() != null) {
							if(gameBoard[i][j].getE().getPriority() > 0) {
								priority++;
							}
						}
						if(gameBoard[i][j].getS() != null) {
							if(gameBoard[i][j].getS().getPriority() > 0) {
								priority++;
							}
						}
						if(gameBoard[i][j].getW() != null) {
							if(gameBoard[i][j].getW().getPriority() > 0) {
								priority++;
							}
						}
						gameBoard[i][j].setPriority(priority);
					}
				}
			}
			for(int i = 0; i < ships.length; i++) {
				if(coordinate[0] == ships[i][0] && coordinate[1] == ships[i][1]) {
					this.mode = 1;
					initialHit = coordinate;
					lastHit = coordinate;
				}
			}
		}
		System.out.println(coordinate[0] + ", " + coordinate[1]);
		return coordinate;
	}
	
}
