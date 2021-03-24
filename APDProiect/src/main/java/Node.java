import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Heisenberg
 */
public class Node {

    private int[][] Board;
    private int movesTillNow, N, rowNum, colmNum;
    private Node previousNode;

    private int[][] goalBoard;
    private positionPair[] goalPositionOfEachNumber, currentPositionOfEachNumber;

    public Node(int[][] board, int movesTillNow, Node previousNode) {
        this.N = Constants.N;
        this.rowNum = Constants.rowNum;
        this.colmNum = Constants.colmNumber;

        this.Board = board;
        this.movesTillNow = movesTillNow;
        this.previousNode = previousNode;

        int t = 1;
        goalBoard = new int[rowNum][colmNum];
        goalPositionOfEachNumber = new positionPair[N + 10];
        currentPositionOfEachNumber = new positionPair[N + 10];

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {
                goalBoard[i][j] = t % (N + 1);
                goalPositionOfEachNumber[t % (N + 1)] = new positionPair(i, j);
                t++;
            }
        }

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {
                currentPositionOfEachNumber[Board[i][j]] = new positionPair(i, j);
            }
        }


    }

    public int getInversionNo() {

        int inversionNo = 0;

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {

                for (int k = j + 1; k < colmNum; k++) {
                    if (Board[i][k] != 0 && Board[i][j] != 0 && Board[i][k] < Board[i][j]) {

                        inversionNo++;
                    }
                }

                for (int k = i + 1; k < rowNum; k++) {
                    for (int l = 0; l < colmNum; l++) {
                        if (Board[k][l] != 0 && Board[i][j] != 0 && Board[k][l] < Board[i][j]) {
                             inversionNo++;
                        }
                    }
                }

            }
        }
        return inversionNo;
    }

    public boolean isSolvable() {

        if (rowNum % 2 == 0) // even row
        {
            int tem;
            tem = getInversionNo() + getblankPosition().row;

            if (tem % 2 != 0) // even board has odd getInversionNo() + getblankPosition().row = true
            {
                return true;
            } else {
                return false;
            }
        } else // odd row
        {
            if (getInversionNo() % 2 != 0) // odd board has odd no of inversion = false 
            {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean isThisTheGoalBoard() {
        int t = 1;

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {
                if (Board[i][j] != goalBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public positionPair getblankPosition() {
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {
                if (Board[i][j] == 0) {
                    return new positionPair(i, j);
                }
            }
        }

        return null;
    }

    public int getPriorityFunction() {
        if (Constants.HeuristicType == heuristicName.Ham) {
            return movesTillNow + HammingDistance();
        } else if (Constants.HeuristicType == heuristicName.Man) {
            return movesTillNow + ManhattanDistance();
        } else if (Constants.HeuristicType == heuristicName.Conf) {
            return movesTillNow + linearConflict();
        }
        return movesTillNow;
    }

    public int HammingDistance() {
        int wrongPlace = 0;
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {
                if (Board[i][j] != 0 && Board[i][j] != goalBoard[i][j]) {
                    wrongPlace++;
                }
            }
        }
        return wrongPlace;
    }

    public int ManhattanDistance() {
        int distance = 0;

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {
                if (Board[i][j] != 0) {
                    distance += (Math.abs(i - goalPositionOfEachNumber[Board[i][j]].row)
                            + Math.abs(j - goalPositionOfEachNumber[Board[i][j]].colm));
                }
            }
        }

        return distance;
    }

    public int linearConflict() {

        int conflictNo = 0;

        // compare all pairs of numbers to find conflict
        for (int i = 1; i <= N; i++) {

            // in the inner loop, j = i + 1 - because, we don't need same 
            //numbers to compare and (+1) because otherwise 
            //same pair will be compared twice like (1,5) and (5,1) 
            for (int j = i + 1; j <= N; j++) {

                // in this if , we are checking  - 
                // 1. if i and j are on the same line 
                // 2. if i and j's goals are on the same line 
                // 3. and if both these lines are the same line. 
                // if one of them is false, there will be no conflict for this pair. 
                if (currentPositionOfEachNumber[i].row == currentPositionOfEachNumber[j].row
                        && goalPositionOfEachNumber[i].row == goalPositionOfEachNumber[j].row
                        && goalPositionOfEachNumber[i].row == currentPositionOfEachNumber[i].row) {

                    // now we have to check which one is on the left. 
                    // j starts from i+1, so always, j > i. 
                    // As they are on the same line and their goal is also on that line
                    // so, bigger one (j)'s goal will be on the right for sure. 
                    //if tj is to the right of tk, and 
                    //goal position of tj is to the left of the goal position of tk, 
                    // then there is a conflict. 
                    // ^The true meaning of this line is - "choto shongkha daan paash e ache ki na sheta
                    // check koro. thakle eta ekta conflict. 
                    // as j > i , so, let's check 
                    if (currentPositionOfEachNumber[i].colm > currentPositionOfEachNumber[j].colm) {
                        conflictNo++;
                    }

                } else if (currentPositionOfEachNumber[i].colm == currentPositionOfEachNumber[j].colm
                        && goalPositionOfEachNumber[i].colm == goalPositionOfEachNumber[j].colm
                        && goalPositionOfEachNumber[i].colm == currentPositionOfEachNumber[i].colm) {

                    if (currentPositionOfEachNumber[i].row > currentPositionOfEachNumber[j].row) {
                        conflictNo++;
                    }
                }

            }
        }

        return (ManhattanDistance() + 2 * conflictNo);

    }

    public int[][] copy2DArray(int tem[][]) {
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {
                tem[i][j] = Board[i][j];
            }
        }
        return tem;
    }

    public List<Node> getSuccessors() {
        List<Node> successors = new ArrayList<>();

        positionPair blankPosition = getblankPosition();
        int[][] temBoard = new int[rowNum][colmNum];
        if (blankPosition != null) {
            int blankRow, blankColm, tem;
            blankRow = blankPosition.row;
            blankColm = blankPosition.colm;

            if (blankRow - 1 >= 0) {

                temBoard = new int[rowNum][colmNum];
                temBoard = copy2DArray(temBoard);

                tem = temBoard[blankRow - 1][blankColm];
                temBoard[blankRow - 1][blankColm] = temBoard[blankRow][blankColm];
                temBoard[blankRow][blankColm] = tem;

                successors.add(new Node(temBoard, movesTillNow + 1, this));
            }
            if (blankRow + 1 < rowNum) {

                temBoard = new int[rowNum][colmNum];
                temBoard = copy2DArray(temBoard);

                tem = temBoard[blankRow + 1][blankColm];
                temBoard[blankRow + 1][blankColm] = temBoard[blankRow][blankColm];
                temBoard[blankRow][blankColm] = tem;

                successors.add(new Node(temBoard, movesTillNow + 1, this));
            }
            if (blankColm - 1 >= 0) {

                temBoard = new int[rowNum][colmNum];
                temBoard = copy2DArray(temBoard);

                tem = temBoard[blankRow][blankColm - 1];
                temBoard[blankRow][blankColm - 1] = temBoard[blankRow][blankColm];
                temBoard[blankRow][blankColm] = tem;

                successors.add(new Node(temBoard, movesTillNow + 1, this));
            }
            if (blankColm + 1 < colmNum) {

                temBoard = new int[rowNum][colmNum];
                temBoard = copy2DArray(temBoard);

                tem = temBoard[blankRow][blankColm + 1];
                temBoard[blankRow][blankColm + 1] = temBoard[blankRow][blankColm];
                temBoard[blankRow][blankColm] = tem;

                successors.add(new Node(temBoard, movesTillNow + 1, this));
            }
        }
        return successors;
    }

    public void printBoard() {
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {
                System.out.print(Board[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("-------------------");
    }


    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.Board);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) {
            return false;
        }

        Node node = (Node) obj;


        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {
                if (node.Board[i][j] != Board[i][j]) {
                    return false;  
                }
            }
        }
        
        return true;
    }

    public void printArr(int[][] arr) {
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println("");
        }
    }


    @Override
    public String toString() {
        String s = "";

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colmNum; j++) {
                s += "" + Board[i][j] + " ";
            }
            s += "\n";
        }
        s += "--------------";

        return s;
    }

    public int[][] getBoard() {
        return Board;
    }

    public void setBoard(int[][] Board) {
        this.Board = Board;
    }

    public int getMovesTillNow() {
        return movesTillNow;
    }

    public void setMovesTillNow(int movesTillNow) {
        this.movesTillNow = movesTillNow;
    }

    public int getN() {
        return N;
    }

    public void setN(int N) {
        this.N = N;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getColmNum() {
        return colmNum;
    }

    public void setColmNum(int colmNum) {
        this.colmNum = colmNum;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

}

class positionPair {

    int row, colm;

    public positionPair(int row, int colm) {
        this.row = row;
        this.colm = colm;
    }
}