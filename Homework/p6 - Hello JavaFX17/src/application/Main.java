package application;

import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    // store any command-line arguments that were entered.
    // NOTE: this.getParameters().getRaw() will get these also
    private List<String> args;

    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 200;
    private static final String APP_TITLE = "Hello JavaFX17 World!";
	
    @Override
    public void start(Stage primaryStage) throws Exception {
        // save args example
        args = this.getParameters().getRaw();
			
        // Create a vertical box with Hello labels for each args
        VBox vbox = new VBox();
            for (String arg : args) {
                vbox.getChildren().add(new Label("hello "+arg));
            }

        // Main layout is Border Pane example (top,left,center,right,bottom)
        BorderPane root = new BorderPane();

        // Add the vertical box to the center of the root pane
        root.setTop(new Label(APP_TITLE));
        root.setTop(new Label("CS400 MyFirstJavaFX"));
        root.setCenter(vbox);
        
        // Create a ComboBox in left panel which generates a list 
        String options[] = {"Option 1", "Option 2"};
        root.setLeft(new ComboBox(FXCollections.observableArrayList(options)));
        Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Create an ImageView in the center
        String filePath = "D://Documents/CS400/Homework/p6 - Hello JavaFX17/face.png";
        Image image = new Image(filePath);
        ImageView imageView = new ImageView(image);
        root.setCenter(imageView);
        
        // Create a button at the bottom panel
        root.setBottom(new Button("Done"));
        
        // Create a text field at the right panel
        root.setRight(new TextField("This is me. Who are you?"));
        
        // Add the stuff and set the primary stage
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}