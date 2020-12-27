package mainPackage.Model;

public class TestModel {
    public static void main(String[] args){
        Game game = new Game();
        // Human starts first
        game.newGame(1);
        /* Human Wins
        System.out.println("Game status: " + game.getStatus());
        game.cPlayerMove(0,0);
        game.cPlayerMove(0,0); // Test invalid move
        game.cPlayerMove(0,1);
        game.cPlayerMove(1,1);
        game.cPlayerMove(0,2);
        game.cPlayerMove(2,2);
        // Expected output: X|0|0
        //                  _|X|_
        //                  _|_|X
        // Game status: -1
        game.displayBoard();
        System.out.println("Game status: " + game.getStatus());
        */

        /* AI Wins
        game.cPlayerMove(0,0);
        game.cPlayerMove(0,1);
        game.cPlayerMove(0,2);
        game.cPlayerMove(1,1);
        game.cPlayerMove(1,0);
        game.cPlayerMove(2,1);
        game.displayBoard();
        System.out.println("Game status: " + game.getStatus());
        */

        /* Tie
        game.cPlayerMove(0,0); // X
        game.cPlayerMove(0,1);
        game.cPlayerMove(1,1); // X
        game.cPlayerMove(0,2);
        game.cPlayerMove(1,2); // X
        game.cPlayerMove(1,0);
        game.cPlayerMove(2,0); // X
        game.cPlayerMove(2,2);
        game.cPlayerMove(2,1); // X
        game.displayBoard();
        System.out.println("Game status: " + game.getStatus());
        */
    }
}
