package myworkjournal.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  public static void main(final String[] args) {
    launch(args);
  }

  @Override public void start(final Stage primaryStage) throws Exception {
    final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("createProfile.fxml"));
    final Parent parent = fxmlLoader.load();
    primaryStage.setScene(new Scene(parent));
    primaryStage.show();
  }
}
