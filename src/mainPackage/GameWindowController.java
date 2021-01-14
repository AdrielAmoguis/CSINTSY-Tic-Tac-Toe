package mainPackage;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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

    @FXML
    Button backButton;

    @FXML
    Button newRoundButton;

    // Controller Constructor
    public GameWindowController(int rationality, boolean playAsX)
    {
        this.rationality = rationality;
        this.gameInstance = new Game();
        this.gameInstance.newGame(playAsX ? 1 : 2);
        this.grid = new ImageView[3][3];
    }

    // JavaFX Initialization
    @FXML
    void initialize()
    {
        // For each iteration, add a Pane with an ImageView into the GridPane
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
                        case 1: // Level 1
                            this.gameInstance.AI_genericMove();
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
                    case 10: // AI Wins
                        displayPrompt("Computer Wins!");
                        break;
                    case -10: // Player Wins
                        displayPrompt("Human Wins!");
                        break;
                    default:
                        displayPrompt("If you are seeing this, this is a bug.");
                }
                this.timeline.stop();
                this.backButton.setDisable(false);
                this.newRoundButton.setDisable(false);
            }

        }));
            this.timeline.setCycleCount(Animation.INDEFINITE);
            this.timeline.play();

        // Disable the back button
        this.backButton.setDisable(true);

        // Disable the nextRound button
        this.newRoundButton.setDisable(true);

        // Display Start Message
        String playerStart = new String(this.gameInstance.getCurrentPlayer() == this.gameInstance.getPlayerTurn() ? "Human" : "Computer");
        String otherPlayer = new String(this.gameInstance.getCurrentPlayer() == this.gameInstance.getPlayerTurn() ? "Computer" : "Human");
        this.displayPrompt(String.format("%s is X\n%s is O\n%s Start", playerStart, otherPlayer, playerStart));
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
        // Check for button
        if(ev.getSource() instanceof Button)
        {
            Button button = (Button) ev.getSource();

            // Check Button ID
            if(button.getId().equals(this.backButton.getId()))
            {
                // Load main primaryStage
                Stage primaryStage = (Stage) ((Node) ev.getSource()).getScene().getWindow();

                // Load the FXML File
                Parent root = null;
                try
                {
                    root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }

                // Window Options
                primaryStage.setTitle("Tic-Tac-Toe | AMOGUIS & SUN");
                primaryStage.setResizable(false);

                // Launch Window
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            }
            else if(button.getId().equals(this.newRoundButton.getId()))
            {
                // Start a new round (replace current window with another game state
                // Load main primaryStage
                Stage primaryStage = (Stage) ((Node) ev.getSource()).getScene().getWindow();

                // Set custom constructor
                GameWindowController controller = new GameWindowController(this.rationality, this.gameInstance.getPlayerTurn() != 1);

                // Load the FXML File
                Parent root = null;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GameWindow.fxml"));
                loader.setController(controller);
                try
                {
                    root = loader.load();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }

                // Window Options
                primaryStage.setTitle("Tic-Tac-Toe Game Window | AMOGUIS & SUN");
                primaryStage.setResizable(false);

                // Launch Window
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            }
        }
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