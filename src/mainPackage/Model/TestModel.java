package mainPackage.Model;

import java.util.Scanner;

public class TestModel {
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        Game game = new Game();
        // Human starts first
        game.newGame(2);
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

        // AI Test
        while(game.getStatus() == 999){
            game.displayBoard();
            // if human turn
            if (game.getCurrentPlayer() == game.getPlayerTurn()){
                // ask for input
                System.out.print("Enter [row, col]: ");
                int row = kb.nextInt();
                int col = kb.nextInt();
                game.cPlayerMove(row, col);
            }
            else{
                //game.AI_randomMove();
                game.AI_bestMove();
            }
        }
        // display end result
        game.displayBoard();
        System.out.println("End game status: " + game.getStatus());

    }
}
