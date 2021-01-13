package mainPackage.Model;
import java.time.LocalDateTime;
import java.util.*;

// Reference: https://www3.ntu.edu.sg/home/ehchua/programming/java/JavaGame_TicTacToe.html
public class Game implements Minimax{
    /**
     * Represents the board.
     * The board is filled with values of either 0, 1, or 2.
     * 0 is an empty cell.
     * 1 is assigned to first player - X Symbol.
     * 2 is assigned to second player - O Symbol.
     */
    private int[][] board;
    /**
     * The turn of the current player, either 1 or 2 but is always initialized as 1.
     */
    private int currentPlayer;
    /**
     * Represents if Player went first or second. Determines the symbol to be displayed X or O
     */
    private int playerTurn;
    /**
     * Represents if AI went first or second.
     */
    private int AI_Turn;
    /**
     * Represents the current status of the game. Refer to macros.
     */
    private int status;

    // MACROS:
    public static final int ROWS = 3;
    public static final int COLS = 3;

    public static final int ONGOING = 999;
    public static final int DRAW = 0;
    public static final int AI_WIN = 10;
    public static final int PLAYER_WIN = -10;


    public Game(){
        board = new int[ROWS][COLS];
        currentPlayer = 1;
        status = ONGOING;
    }

    /**
     * Creates a new game and sets the human player's turn (first/second) based on input.
     * @param playerTurn human player's turn (first/second)
     */
    public void newGame(int playerTurn){
        if (playerTurn == 1 || playerTurn == 2){
            currentPlayer = 1;
            this.playerTurn = playerTurn;
            // If player is going first, then AI is going second. Otherwise AI goes first
            AI_Turn = playerTurn == 1 ? 2 : 1;
            status = ONGOING;
        }
        else
            System.out.println("[Game]: Error in newGame -> playerTurn = " + playerTurn);
    }

    /**
     * Current player places a token on the board IF it is a valid move.
     * @param row target row
     * @param col target column
     */
    public void cPlayerMove(int row, int col){
        if (validMove(row, col)){
            board[row][col] = currentPlayer;
            updateStatus();
            nextPlayer();
        }
    }

    /**
     * Level 0 AI Rational Behavior.
     * The agent makes random (but valid) moves, regardless of the past moves. Note at even the lowest level,
     * there is a record of past moves and current configuration of the board to be able to make valid moves.
     * Also, there is already a DETECTION OF ANY WINNING MOVE.
     */
    public void AI_randomMove(){
        List<int[]> validMoves = new ArrayList<>();
        boolean hasWon = false;
        for (int row = 0; row < ROWS && !hasWon; row++){
            for (int col = 0; col < COLS && !hasWon; col++){
                // if possible move
                if (board[row][col] == 0){
                    board[row][col] = AI_Turn;
                    // get the score after attempting this move
                    int score = checkStatus(board, 0);
                    // revert back to original board state
                    board[row][col] = 0;
                    // if move is a winning move for AI
                    if (score == 1){
                        // AI immediately chooses the move
                        cPlayerMove(row, col);
                        hasWon = true;
                        break;
                    }
                    // else append to list of possible moves
                    int[] validMove = {row, col};
                    validMoves.add(validMove);
                }
            }
        }
        // If AI has not found a winning move
        if (!hasWon){
            Random randomizer = new Random();
            randomizer.setSeed(LocalDateTime.now().getNano());
            // Randomly select from the list of possible moves
            int[] chosenMove = validMoves.get(randomizer.nextInt(validMoves.size()));
            // AI performs the randomly chosen move
            cPlayerMove(chosenMove[0], chosenMove[1]);
        }
    }

    /**
     * Level 1 AI Rational Behavior.
     * the agent uses a hard-coded table that generates a move for every possible state/configuration;
     * note that since there is a very large number (9!) of possible configurations, the “hard-coded table of moves”
     * can make generalizations, take advantage of symmetries, perform some kind of clustering of configurations, and the like.
     *
     * 
     *
     */
    public void AI_tableMove()
    {

    }

    /**
     * This is a helper method for AI_tableMove().
     * This method checks if two boards are identical when taking symmetry into account.
     * @return boolean - true if the two boards are symmetric. False otherwise.
     */
    private boolean isSymmetric(int[][] board_1, int[][] board_2)
    {
        // Create a copy of array 2
        int[][] compareBoard = new int[3][3];

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                compareBoard[i][j] = board_2[i][j];

        // Rotate 3 times
        for(int i = 0; i < 3; i++)
        {
            if(Arrays.equals(board_1, compareBoard))
            {
                // isSymmetric
                return true;
            }

            // Perform rotation
            int[][] rotated = new int[3][3];

            // Rotate Centerpieces
            rotated[1][2] = compareBoard[0][1];
            rotated[2][1] = compareBoard[1][2];
            rotated[1][0] = compareBoard[2][1];
            rotated[0][1] = compareBoard[1][0];

            // Rotate Edges
            rotated[0][2] = compareBoard[0][0];
            rotated[2][2] = compareBoard[0][2];
            rotated[2][0] = compareBoard[2][2];
            rotated[0][0] = compareBoard[2][0];

            // Copy Center
            rotated[1][1] = compareBoard[1][1];

            compareBoard = rotated;
        }

        return false;
    }

    /**
     * Updates the current status of the game based on baord state.
     * Either ONGOING, PLAYER_WIN/AI_WIN, or DRAW.
     */
    public void updateStatus(){
        status = checkStatus(board, 0);
    }

    /**
     * Can be optimized further.
     * Returns the current status of the game.
     * Returns one of the following: -1 : AI_WIN | 1 : PLAYER_WIN | 0 : TIED | 999 : ONGOING
     * @return returns the current status of the game
     */
    public int checkStatus(int[][] board, int depth) {
        // check if there is already a winner
        // check rows
        for (int row = 0; row < ROWS; row++){
            if (board[row][0] == board[row][1] && board[row][0] == board[row][2] && board[row][0] != 0){
                int winner = board[row][0];
                // if the winner is the AI, return macro for AI
                if (winner == AI_Turn)
                    return AI_WIN - depth; // the higher the depth, the score worsens
                else
                    return PLAYER_WIN + depth; // the lower the depth, the score improves
            }
        }
        // check columns
        for (int col = 0; col < COLS; col++){
            if (board[0][col] == board[1][col] && board[0][col] == board[2][col] && board[0][col] != 0){
                int winner = board[0][col];
                if (winner == AI_Turn)
                    return AI_WIN - depth; // the higher the depth, the score worsens
                else
                    return PLAYER_WIN + depth; // the lower the depth, the score improves
            }
        }
        // check right diagonal
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != 0){
            int winner = board[0][0];
            if (winner == AI_Turn)
                return AI_WIN - depth; // the higher the depth, the score worsens
            else
                return PLAYER_WIN + depth; // the lower the depth, the score improves
        }

        // check left diagonal
        if (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != 0){
            int winner = board[0][2];
            if (winner == AI_Turn)
                return AI_WIN - depth; // the higher the depth, the score worsens
            else
                return PLAYER_WIN + depth; // the lower the depth, the score improves
        }

        // check if draw
        if (isDraw())
            return DRAW;

        // else it's still ongoing
        return ONGOING;
    }

    /**
     * Return true if it is a draw (no more 0's)
     * @return true if it is a draw
     */
    public boolean isDraw() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == 0) {
                    return false;  // an empty cell found, not draw, exit
                }
            }
        }
        return true;  // no empty cell, it's a draw
    }

    public void AI_bestMove(){
        int bestScore = Integer.MIN_VALUE;
        int targetRow = 999, targetCol= 999;
        int depth = 0;
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS; col++){
                if(board[row][col] == 0){
                    board[row][col] = AI_Turn;
                    int score = minimax(board, false, depth);
                    if (score > bestScore){
                        bestScore = score;
                        targetRow = row;
                        targetCol = col;
                    }
                    board[row][col] = 0; // reset
                }
            }
        }
        cPlayerMove(targetRow, targetCol);
    }

    /**
     * Returns the current board state of the game.
     * @return the current board state of the game.
     */
    public int[][] getBoard(){
        return board;
    }

    /**
     * Returns the current player / whose turn it currently is
     * @return the current player
     */
    public int getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Determines if human player went first or second (1 or 2).
     * @return returns 1 if human went first, returns 2 if otherwise
     */
    public int getPlayerTurn(){
        return playerTurn;
    }

    /**
     * Determines if AI went first or second (1 or 2)
     * @return returns 1 if AI went first, returns 2 if otherwise
     */
    public int getAI_Turn(){
        return AI_Turn;
    }


    /**
     * Returns the current status of the game.
     * Status: 0: Tie | 1: AI wins | -1: Player wins | 999: Ongoing
     * @return the current status of the game.
     */
    public int getStatus(){
        return status;
    }

    /**
     * Checks if the given position is valid.
     * A position is valid if the position is empty and is within range
     * @param row row to be checked (index notation)
     * @param col column to be checked (index notation)
     * @return true if the position is valid, otherwise returns false
     */
    private boolean validMove(int row, int col){
        if (row >= 0 && row < ROWS && col >= 0 && col <= COLS){
            if (board[row][col] == 0)
                return true;
            else{
                //System.out.println("[Game]: Invalid move");
                return false;
            }
        }
        //System.out.println("[Game]: Invalid move");
        return false;
    }

    /**
     * Changes the current player after a player has made a valid move.
     */
    private void nextPlayer() {
        // If currentPlayer is 1, change to 2. Else if currentPlayer is 2, change to 1
        currentPlayer = currentPlayer == 1 ? 2 : 1;
    }

    public void displayBoard(){
        for (int row = 0; row < ROWS; row++){
            System.out.print("|");
            for (int col = 0; col < COLS; col++){
                int token = board[row][col];
                char ch = '_';
                if (token == 1)
                    ch = 'X';
                else if (token == 2)
                    ch = 'O';
                System.out.print(ch + "|");
            }
            System.out.println();
        }
    }


    @Override
    public int minimax(int[][] board, boolean isMax, int depth) {
        int status = checkStatus(board, depth);
        int bestScore;
        // Have reached a leaf node (base case)
        if (status != 999)
            return status;
        // AI is maximizing its best score
        if (isMax){
            bestScore = Integer.MIN_VALUE;
            for (int row = 0; row < ROWS; row++){
                for (int col = 0; col < COLS; col++){
                    // available move
                    if (board[row][col] == 0){
                        board[row][col] = AI_Turn;
                        // compute score
                        int score = minimax(board, false, depth + 1);
                        // reset board state
                        board[row][col] = 0;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
        }
        // Player is minimizing the AI's best score
        else{
            bestScore = Integer.MAX_VALUE;
            for (int row = 0; row < ROWS; row++){
                for (int col = 0; col < COLS; col++){
                    // available move
                    if (board[row][col] == 0){
                        board[row][col] = playerTurn;
                        // compute score
                        int score = minimax(board, true, depth + 1);
                        // reset board state
                        board[row][col] = 0;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
        }
        return bestScore;
    }
}


