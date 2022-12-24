package kozmikoda.passwordspace;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIController {


    double offsetX, offsetY;

    private Stage stage;
    private Scene scene;

    @FXML
    private ImageView image;

    @FXML
    private Stage window;



    // Drag window functions

    @FXML
    void dragWindow(MouseEvent event) {
        window.setX(event.getScreenX() - offsetX);
        window.setY(event.getScreenY() - offsetY);
    }


    public void setWindowOffset(MouseEvent event) {
        offsetX = event.getSceneX();
        offsetY = event.getSceneY();

    }



    @FXML
    private void changeToScene2(ActionEvent event) throws IOException{
        scene = ((Stage)FXMLLoader.load(getClass().getResource("scene2.fxml"))).getScene();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void changeToScene1(ActionEvent event) throws IOException {
        scene = ((Stage)FXMLLoader.load(getClass().getResource("gui.fxml"))).getScene();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void closeButton(ActionEvent event) {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

}
