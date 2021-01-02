package mainPackage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class MainWindowController implements EventHandler<Event>
{
    // Controller Attributes
    private int rationality;

    // JavaFX Components
    @FXML
    ComboBox rationalityDropdown;

    @FXML
    Button startGame;

    @FXML
    CheckBox checkPlayAsX;

    // Controller Constructor
    public MainWindowController()
    {
        this.rationality = -1;
    }

    // JavaFX Initializer
    @FXML
    void initialize()
    {
        // Add options to the combobox
        this.rationalityDropdown.getItems().add(new String("Level 0: Random"));
        this.rationalityDropdown.getItems().add(new String("Level 1: Hard-Coded Rules"));
        this.rationalityDropdown.getItems().add(new String("Level 2: Heuristic Search Strategies"));
        this.rationalityDropdown.getItems().add(new String("Level 3: Artificial Intelligence"));

        // Disable Start Game Button
        this.startGame.setDisable(true);
    }

    // Main Event Handler
    public void handle(Event ev)
    {
        if(ev instanceof ActionEvent)
            handle((ActionEvent) ev);
    }

    // ActionEvent Handler
    private void handle(ActionEvent ev)
    {
        // Check Source Instance
        if(ev.getSource() instanceof ComboBox)
        {
            // ComboBox
            ComboBox box = (ComboBox) ev.getSource();

            // Check combobox identity
            if(box.getId().equals(this.rationalityDropdown.getId()))
            {
                // Main ComboBox
                this.rationality = box.getItems().indexOf(box.getValue());

                // Update button
                if(this.rationality >= 0)
                    this.startGame.setDisable(false);
                else
                    this.startGame.setDisable(true);
            }
        }
        else if(ev.getSource() instanceof Button)
        {
            // Button
            Button button = (Button) ev.getSource();

            // Start Game Button
            if(button.getId().equals(this.startGame.getId()))
            {
                // Set the primaryStage
                Stage primaryStage = (Stage) ((Button) ev.getSource()).getScene().getWindow();

                // Set custom constructor
                GameWindowController controller = new GameWindowController(this.rationality, this.checkPlayAsX.isSelected());

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
}
