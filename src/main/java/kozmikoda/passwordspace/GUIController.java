package kozmikoda.passwordspace;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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

    @FXML
    private Pane passwdForgetNamePane, passwdForgetPhonePane, passwdForgetNewPasswdPane, signUpPane;

    @FXML
    private JFXButton loginButton;

    @FXML
    private TextField passwdVerifName, signUpNameField, signUpSurnameField, signUpUsernameField, signUpPhoneField, signUpMailField, signUpPasswordField;

    @FXML
    private Hyperlink forgotPasswdLink, signUpLink;

    @FXML
    private PasswordField passwdVerifCode, passwdNewPasswd;

    @FXML
    private GridPane serviceGridPane;

    // Drag window functions
    @FXML
    void dragWindow(MouseEvent event) {
        window.setX(event.getScreenX() - offsetX);
        window.setY(event.getScreenY() - offsetY);
    }

    @FXML
    public void setWindowOffset(MouseEvent event) {
        offsetX = event.getSceneX();
        offsetY = event.getSceneY();
    }

    //Changing Scenes
    @FXML
    void changeToScene2(ActionEvent event) throws IOException{

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


    // Close button method
    @FXML
    protected void closeButton(ActionEvent event) {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    // ---------------------------------------------
    // FORGOT PASSWORD
    @FXML
    void forgetPasswdButton() {
        passwdForgetNamePane.setVisible(true);
        loginButton.setDisable(true);
        forgotPasswdLink.setDisable(true);
        signUpLink.setDisable(true);
    }

    @FXML
    void forgetPasswdBackButton() {
        passwdForgetNamePane.setVisible(false);
        loginButton.setDisable(false);
        forgotPasswdLink.setDisable(false);
        signUpLink.setDisable(false);
        passwdVerifName.clear();
    }

    @FXML
    void forgetPasswdBackButton2() {
        passwdForgetPhonePane.setVisible(false);
        passwdForgetNamePane.setVisible(true);
        passwdVerifCode.clear();
    }


    // Verification page
    @FXML
    void sendVerificationButton() {
        passwdForgetNamePane.setVisible(false);
        passwdForgetPhonePane.setVisible(true);
    }

    // Reset passwd page
    @FXML
    void verifyButton() {
        passwdForgetPhonePane.setVisible(false);
        passwdForgetNewPasswdPane.setVisible(true);
    }

    // After passwd reset
    @FXML
    void resetPasswdButton() {
        passwdForgetNewPasswdPane.setVisible(false);
        loginButton.setDisable(false);
        forgotPasswdLink.setDisable(false);
        signUpLink.setDisable(false);
        passwdNewPasswd.clear();
    }


    // ----------------------------------------------------

    // SIGN UP PART
    @FXML
    void signUpLinkAction() {
        signUpPane.setVisible(true);
        loginButton.setDisable(true);
        forgotPasswdLink.setDisable(true);
        signUpLink.setDisable(true);
    }

    @FXML
    void signUpBackButton() {
        signUpPane.setVisible(false);
        loginButton.setDisable(false);
        forgotPasswdLink.setDisable(false);
        signUpLink.setDisable(false);

        // clear the text-fields
        signUpNameField.clear();
        signUpSurnameField.clear();
        signUpUsernameField.clear();
        signUpPhoneField.clear();
        signUpMailField.clear();
        signUpPasswordField.clear();
    }

    @FXML
    void signUpButton() {
        signUpPane.setVisible(false);
        loginButton.setDisable(false);
        forgotPasswdLink.setDisable(false);
        signUpLink.setDisable(false);

        // ---------
        // GET TEXTS HERE AND SEND TO THE MAIN USER ACCOUNT
        // ---------

        signUpNameField.clear();
        signUpSurnameField.clear();
        signUpUsernameField.clear();
        signUpPhoneField.clear();
        signUpMailField.clear();
        signUpPasswordField.clear();

    }

    // ---------------------------------

    static int i = 7;
    @FXML
    void addService() {
        serviceGridPane.addRow(i, new Text(" "));
        serviceGridPane.autosize();

        i++;
        System.out.println(i);

    }

}
