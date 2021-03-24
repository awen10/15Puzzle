import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Heisenberg
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        int T=10, t = 0;

        while (t < T) {
            Scanner in = new Scanner(new File("file"+t+".txt"));
            System.out.println("Case No: " + t + "\n");

            int N;
            N = in.nextInt();
            Constants.N = N; // numbers in the puzzle
            N++;
            N = (int) Math.sqrt(N);
            Constants.rowNum = Constants.colmNumber = N;

            int[][] initialBoard = new int[N][N];

            for (int i = 0; i < Constants.rowNum; i++) {
                for (int j = 0; j < Constants.colmNumber; j++) {
                    initialBoard[i][j] = in.nextInt();
                }
            }

            Node initialNode = new Node(initialBoard, 0, null);

            System.out.println("The given board is: ");
            initialNode.printBoard();

            if (initialNode.isSolvable()) {
                System.out.println("This board is SOLVABLE. ");
                System.out.println("");

                Node tem;
                aStarSearch search;

                Constants.HeuristicType = heuristicName.Conf;

                tem = initialNode;
                search = new aStarSearch(tem);
                search.printAllMoves();
                t++;

            } else {
                System.out.println("This board is NOT Solvable! Try again. ");
                t++;
            }

        }
    }
}