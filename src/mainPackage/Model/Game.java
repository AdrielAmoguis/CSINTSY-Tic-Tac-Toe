package mainPackage.Model;

public class Game {
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
    public int playerTurn;
    /**
     * Represents if AI went first or second.
     */
    public int AI_Turn;
    /**
     * Represents the current status of the game. Refer to macros.
     */
    public int status;

    // MACROS:
    public static final int ROWS = 3;
    public static final int COLS = 3;

    public static final int ONGOING = 999;
    public static final int DRAW = 0;
    public static final int AI_WIN = 1;
    public static final int PLAYER_WIN = -1;


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
            updateStatus(row, col);
            nextPlayer();
        }
    }

    /**
     * Updates the current status of the game after current player made a move at (currentRow, currentCol).
     * Either ONGOING, PLAYER_WIN/AI_WIN, or DRAW.
     * @param currentRow row where currentPlayer made a move
     * @param currentCol column where currentPlayer made a move
     */
    public void updateStatus(int currentRow, int currentCol){
        if (hasWon(currentRow, currentCol)) {  // check if winning move
            // if current player is the human player, declare player win, otherwise AI wins
            status = currentPlayer == playerTurn ? PLAYER_WIN : AI_WIN;
        } else if (isDraw()) {  // check for draw
            status = DRAW;
        }
        // Otherwise, the status is still ONGOING
    }

    /**
     * Returns true if the player has won after placing at (currentRow, currentCol)
     * @param currentRow row where player placed
     * @param currentCol row where player placed
     * @return true if the player has won
     */
    public boolean hasWon(int currentRow, int currentCol) {
        return (board[currentRow][0] == currentPlayer         // 3-in-the-row
                && board[currentRow][1] == currentPlayer
                && board[currentRow][2] == currentPlayer
                || board[0][currentCol] == currentPlayer      // 3-in-the-column
                && board[1][currentCol] == currentPlayer
                && board[2][currentCol] == currentPlayer
                || currentRow == currentCol            // 3-in-the-diagonal
                && board[0][0] == currentPlayer
                && board[1][1] == currentPlayer
                && board[2][2] == currentPlayer
                || currentRow + currentCol == 2  // 3-in-the-opposite-diagonal
                && board[0][2] == currentPlayer
                && board[1][1] == currentPlayer
                && board[2][0] == currentPlayer);
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
}
