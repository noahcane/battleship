import java.util.*;

public class Game { //main class
	
	private static Board p1board = new Board(); //different board types
	private static Board aiboard = new Board(); 
	private static Board p1attack = new Board();
	private static aiBoard aiattack; 
	
	public String printTwoBoards(Board b1, Board b2) { //utility
		String print = "";
		for(int i = 0; i < p1board.getBoard().length; i++) {
			for(int j = 0; j < p1board.getBoard()[i].length; j++){		
				print += b1.getBoard()[i][j];
				print += " ";
			}
			print += "\t";
			for(int j = 0; j < p1board.getBoard()[i].length; j++){		
				print += b2.getBoard()[i][j];
				print += " ";
			}
			print += "\n";
		}	
		return print;
	}
	
	public boolean check(String s) { //checks if your input wants the ship to rotate
		if(s.equals("R") || s.equals("ROTATE")) {
			return true;
		}
		return false;
	}
	
	public int[][] aiplacement(int type, Board b){
		Random rand = new Random();
		int[][] ret = new int[type][2];
		boolean valid = false;
		int notation = rand.nextInt(2);
		while(valid == false) {
			int[] coordinate = {rand.nextInt(10), rand.nextInt(10)};
			if(notation == 0 && coordinate[0] <= aiboard.getDimension() - type) {
				valid = true;
				for(int i = 0; i < type; i++) {
					ret[i][0] = coordinate[0] + i;
					ret[i][1] = coordinate[1];
					if(b.checkPoint(ret[i])) {
						valid = false;
						break;
					}
				}
			}else if(notation == 1 && coordinate[1] <= aiboard.getDimension() - type){
				valid = true;
				for(int i = 0; i < type; i++) {
					ret[i][0] = coordinate[0];
					ret[i][1] = coordinate[1] + i;
					if(b.checkPoint(ret[i])) {
						valid = false;
						break;
					}
				}
			}
		}
		return ret;
	}
	
	public int[][] placement(int type, Board b){
		Scanner input = new Scanner(System.in);
		int[][] ret = new int[type][2];
		boolean valid = false;
		int rotated = 0;
		while(valid == false) {	
			String user = input.nextLine();
			if(user.equals("R")) {
				if(rotated == 0) {
					rotated = 1;
				}else {
					rotated = 0;
				}
			}else {
				int[] coordinate = {(Integer.parseInt(user.substring(user.indexOf(' ') + 1, user.length()))) - 1, (Integer.parseInt(user.substring(0, user.indexOf(',')))) - 1};
				if(rotated == 0 && coordinate[0] <= p1board.getDimension() - type) {
					valid = true;
					for(int i = 0; i < type; i++) {
						ret[i][0] = coordinate[0] + i;
						ret[i][1] = coordinate[1];
						if(b.checkPoint(ret[i])) {
							valid = false;
							break;
						}
					}
				}else if(rotated == 1 && coordinate[1] <= p1board.getDimension() - type){
					valid = true;
					for(int i = 0; i < type; i++) {
						ret[i][0] = coordinate[0];
						ret[i][1] = coordinate[1] + i;
						if(b.checkPoint(ret[i])) {
							valid = false;
							break;
						}
					}
				}
			}
		}
		System.out.println("Placed!");	
		return ret;
	}
	
	public static void main(String[] args) {
			
		Random rand = new Random();
		Scanner input = new Scanner(System.in);
		Game g = new Game();
		boolean gameRunning = true;
		String[] shipNames = {"Carrier", "Battleship", "Submarine", "Destroyer", "Cruiser"};
		int[] ship = {5, 4, 3, 3, 2};
		int[][][] ships = {{{}, {}, {}, {}, {}}, {{}, {}, {}, {}}, {{}, {}, {}}, {{}, {}, {}}, {{}, {}}}; //to help me visualize

		for(int i = 0; i < ship.length; i++) {
			System.out.println("Let's place your " + shipNames[i] + "!");
			ships[i] = g.placement(ship[i], p1board);
			g.p1board.addCoordinates(ships[i]);
			g.aiboard.addCoordinates(g.aiplacement(ship[i], aiboard));
			System.out.println(g.p1board.printBoard());
		}
		
		for(int i = 0; i < ships.length; i++) {
			for(int j = 0; j < ships[i].length; j++) {
				for(int k = 0; k < 2; k++) {
					System.out.print(ships[i][j][k] + " ");
				}
				System.out.println();
			}
			System.out.println();
		}
		
		aiattack = new aiBoard(ships);
		aiattack.initializeBoard();
		System.out.println("You have finished placing your board! Let's start the game!\n");
		
		while(gameRunning == true) {
			System.out.print(g.printTwoBoards(p1board, p1attack) + "\n\n");
			if(g.p1board.gameOver() || g.aiboard.gameOver()){
				gameRunning = false;
			}else {
				System.out.print("Coordinate: ");
				String user = input.nextLine();
				int[] coordinate = {Integer.parseInt(user.substring(0, user.indexOf(','))), Integer.parseInt(user.substring(user.indexOf(' ') + 1, user.length()))};
				if(p1attack.checkPoint(coordinate, 0)) {
					if(aiboard.checkPoint(coordinate)) {
						System.out.println("HIT!");
						aiboard.changePoint(coordinate, 8);
						p1attack.changePoint(coordinate, 8);
						int[] cd = aiattack.updateBoard();
						p1board.changePoint(cd, 1);
					}else {
						System.out.println("MISS!");
						aiboard.changePoint(coordinate, 9);
						p1attack.changePoint(coordinate, 9);
						int[] cd = aiattack.updateBoard();
						p1board.changePoint(cd, 1);
					}
				}else {
					System.out.println("Invalid coordinate. Try again.");
				}
				
			}
		}
		System.out.print("Game over! ");
		if(g.p1board.gameOver() == true) {
			System.out.println("Player 2 wins!");
		}else {
			System.out.println("Player 1 wins!");
		}
	}
	
}
