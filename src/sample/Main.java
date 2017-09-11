package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        primaryStage.setTitle("File Type Getter");
        primaryStage.setScene(new Scene(root, 600, 350));
	    primaryStage.setResizable(false);   //禁止用户更改窗口大小
	    primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
