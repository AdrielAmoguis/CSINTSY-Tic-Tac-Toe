package mainPackage;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import mainPackage.Model.*;
import javafx.event.Event;
import javafx.event.EventHandler;

public class GameWindowController implements EventHandler<Event>
{
    // Controller Attributes
    private final int rationality;
    private final Game gameInstance;
    private ImageView[][] grid;
    Timeline timeline;

    // JavaFX Components
    @FXML
    TextArea promptConsole;

    @FXML
    GridPane mainGrid;

    // Controller Constructor
    public GameWindowController(int rationality)
    {
        this.rationality = rationality;
        this.gameInstance = new Game();
        this.gameInstance.newGame(1);
        this.grid = new ImageView[3][3];
    }

    // JavaFX Initialization
    @FXML
    void initialize()
    {
        // For each iteration, add an AnchorPane with an ImageView into the GridPane
        for(int i = 0; i < 3; i++)
            for(int j = 0 ; j < 3; j++)
            {
                Pane pane = new Pane();

                // Set Pane Click Handler
                pane.setOnMouseReleased(this::handle);

                // Set ImageView properties
                ImageView img = new ImageView();
                img.fitHeightProperty().bind(pane.heightProperty());
                img.fitWidthProperty().bind(pane.widthProperty());

                this.grid[i][j] = img;
                pane.getChildren().add(img);
                mainGrid.add(pane, j, i);
            }

        // Initialize the Board Updater (10 FPS)
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), e ->
        {
            // Check if Game Over
            if(this.gameInstance.getStatus() == 999)
            {
                // Check if AI Move
                if(this.gameInstance.getAI_Turn() == this.gameInstance.getCurrentPlayer())
                {
                    displayPrompt("AI Move");

                    // Move AI
                    switch(this.rationality)
                    {
                        case 0: // Random
                            this.gameInstance.AI_randomMove();
                            break;
                        case 1: // Pre-defined Table
                            break;
                        case 2: // Best Move
                            this.gameInstance.AI_bestMove();
                            break;
                        case 3: // Mashin Lerning
                            break;
                    }
                    updateBoard();
                }
            }
            else
            {
                // Display Game Over
                switch(this.gameInstance.getStatus())
                {
                    case 0: // Tie
                        displayPrompt("It's a tie!");
                        break;
                    case 1: // AI Wins
                        displayPrompt("Computer Wins!");
                        break;
                    case -1: // Player Wins
                        displayPrompt("Human Wins!");
                        break;
                    default:
                        displayPrompt("If you are seeing this, this is a bug.");
                }
                this.timeline.stop();
            }

        }));
            this.timeline.setCycleCount(Animation.INDEFINITE);
            this.timeline.play();
    }

    // Main Handler
    @Override
    public void handle(Event ev)
    {
        // Check for ActionEvent
        if(ev instanceof ActionEvent)
            handle((ActionEvent) ev);

        // Pane Handler
        if(ev.getSource() instanceof Pane)
        {
            // Return if game no longer ongoing
            if(this.gameInstance.getStatus() != 999)
                return;

            // Get Pane Coordinates
            int row = GridPane.getRowIndex((Node) ev.getSource());
            int col = GridPane.getColumnIndex((Node) ev.getSource());

            // Check if Player's Turn
            int playerLegend = this.gameInstance.getPlayerTurn();
            if(playerLegend == this.gameInstance.getCurrentPlayer())
            {
                displayPrompt("Human's Move.");

                // Perform the move
                this.gameInstance.cPlayerMove(row, col);

                // Update Board
                updateBoard();
            }
        }
    }

    // ActionEvent Handler
    private void handle(ActionEvent ev)
    {
    }

    // Prompt
    private void displayPrompt(String prompt)
    {
        String text = this.promptConsole.getText();
        text += "\n";
        String newText = text += prompt;
        this.promptConsole.setText(newText);
        this.promptConsole.setScrollTop(Double.MAX_VALUE);
    }

    // Update Board
    public void updateBoard()
    {
        // Get the active board
        int[][] board = this.gameInstance.getBoard();

        // Scan the board
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                switch(board[i][j])
                {
                    case 1:
                        // Fill with X
                        this.grid[i][j].setImage(new Image("/mainPackage/assets/X.png"));
                        break;
                    case 2:
                        // Fill with O
                        this.grid[i][j].setImage(new Image("/mainPackage/assets/O.png"));
                        break;
                }
            }
        }
    }
}
