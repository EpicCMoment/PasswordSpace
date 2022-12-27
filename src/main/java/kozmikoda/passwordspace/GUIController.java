package kozmikoda.passwordspace;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class GUIController {


    PSQLConnection db = new PSQLConnection();
    static MainUserAccount m;


    double offsetX, offsetY;

    private Stage stage;
    private Scene scene;

    @FXML
    private ImageView image;
    @FXML
    private Stage window;
    @FXML
    private Pane passwdForgetNamePane, passwdForgetPhonePane, passwdForgetNewPasswdPane, signUpPane, signupFailPane, servicesPane, addServicePane;
    @FXML
    private JFXButton loginButton;
    static JFXButton serviceButton, serviceButtons;
    @FXML
    private TextField passwdVerifName, signUpNameField, signUpSurnameField, signUpUsernameField, signUpPhoneField, signUpMailField, signUpPasswordField, addServiceNameField, addServiceUserField;
    @FXML
    private Hyperlink forgotPasswdLink, signUpLink;
    @FXML
    private PasswordField passwdVerifCode, passwdNewPasswd, addServicePasswdField;
    @FXML
    private VBox serviceVbox;
    @FXML
    private Label failLoginLabel, passwdUserNotFoundLabel, serviceInsideName, serviceInsidePassword;
    @FXML
    private TextField loginUsernameField;
    @FXML
    private PasswordField loginPasswordField;


    public GUIController() throws SQLException {}


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



    // Close button
    @FXML
    protected void closeButton(ActionEvent event) {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();

    }


    // FORGOT PASSWORD PART
    @FXML
    void forgetPasswdButton() {
        passwdForgetNamePane.setVisible(true);
        passwdUserNotFoundLabel.setVisible(false);
        loginButton.setDisable(true);
        forgotPasswdLink.setDisable(true);
        signUpLink.setDisable(true);
    }

    @FXML
    void forgetPasswdBackButton() {
        passwdForgetNamePane.setVisible(false);
        passwdUserNotFoundLabel.setVisible(false);
        loginButton.setDisable(false);
        forgotPasswdLink.setDisable(false);
        signUpLink.setDisable(false);
        passwdVerifName.clear();
    }

    @FXML
    void forgetPasswdBackButton2() {
        passwdForgetPhonePane.setVisible(false);
        passwdForgetNamePane.setVisible(true);
        passwdUserNotFoundLabel.setVisible(false);
        passwdVerifCode.clear();
    }


    // Verification page
    @FXML
    void sendVerificationButton() {
        try {
            passwdUserNotFoundLabel.setVisible(false);
            m = new MainUserAccount(db, passwdVerifName.getText());

            passwdForgetNamePane.setVisible(false);
            passwdForgetPhonePane.setVisible(true);

        } catch (Exception e) {
            passwdUserNotFoundLabel.setVisible(true);
        }

    }

    // Reset passwd page
    @FXML
    void verifyButton() {
        passwdForgetPhonePane.setVisible(false);
        passwdForgetNewPasswdPane.setVisible(true);
    }

    // After passwd reset
    @FXML
    void resetPasswdButton() throws SQLException {

        m.updatePassword(passwdNewPasswd.getText());

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
        signupFailPane.setVisible(false);
        loginButton.setDisable(true);
        forgotPasswdLink.setDisable(true);
        signUpLink.setDisable(true);
    }

    @FXML
    void signUpBackButton() {
        signUpPane.setVisible(false);
        signupFailPane.setVisible(false);
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
    void signUpButtonAction() {
        try {
            signupFailPane.setVisible(false);

            db = new PSQLConnection();

            m = new MainUserAccount(db, signUpUsernameField.getText(), signUpPasswordField.getText(),
                    signUpNameField.getText(), signUpMailField.getText(), signUpPhoneField.getText());

            // close the pane
            signUpPane.setVisible(false);
            signupFailPane.setVisible(false);
            loginButton.setDisable(false);
            forgotPasswdLink.setDisable(false);
            signUpLink.setDisable(false);

            // Clear previous values
            signUpNameField.clear();
            signUpSurnameField.clear();
            signUpUsernameField.clear();
            signUpPhoneField.clear();
            signUpMailField.clear();
            signUpPasswordField.clear();

        } catch (Exception e) {
            signupFailPane.setVisible(true);
        }
    }


    // LOGIN PART

    @FXML
    void loginButtonAction() {
        String[][] services = new String[9][3];

        try {
            failLoginLabel.setVisible(false);
            UserValidator.validateUser(db, loginUsernameField.getText(), loginPasswordField.getText());
            servicesPane.setVisible(true);

            m = new MainUserAccount(db, loginUsernameField.getText());
            final int[] loginIT = {0};
            m.getServices().getHashMap().forEach((serviceName, credentials) -> {
                serviceButton = (JFXButton) serviceVbox.getChildren().get(loginIT[0]);
                serviceButton.setText(serviceName);
                loginIT[0]++;
            });


                int[] j = {0};
                m.getServices().getHashMap().forEach((serviceName, credentials) -> {
                    services[j[0]][1] = credentials.getKey();
                    services[j[0]][2] = credentials.getValue();
                    j[0]++;
                });


                for (int k = 0; k < 9; k++) {
                    int finalK = k;
                    ((JFXButton) serviceVbox.getChildren().get(k)).setOnAction(e -> {
                        serviceInsideName.setText(services[finalK][1]);
                        serviceInsidePassword.setText(services[finalK][2]);
                    });
                }

        /*
            scene = ((Stage)FXMLLoader.load(getClass().getResource("scene2.fxml"))).getScene();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            */
        } catch (Exception e) {
            failLoginLabel.setVisible(true);
        }
    }


    @FXML
    void servicesBackButton() {
        addServicePane.setVisible(false);
        addServiceNameField.clear();
        addServicePasswdField.clear();
        addServiceUserField.clear();
    }

    @FXML
    void addServicePaneOpenerButton() {
        addServicePane.setVisible(true);
    }

    @FXML
    void addServiceButton() throws SQLException {
        m.addNewService(addServiceNameField.getText(), addServiceUserField.getText(), addServicePasswdField.getText());
        final int[] serviceIT = {0};
        m.getServices().getHashMap().forEach((serviceName, credentials) -> {
            serviceButton = (JFXButton) serviceVbox.getChildren().get(serviceIT[0]);
            serviceButton.setText(serviceName);
            serviceIT[0]++;
        });

        loginButtonAction();

        addServiceNameField.clear();
        addServicePasswdField.clear();
        addServiceUserField.clear();


    }

    @FXML
    void changeToScene1(ActionEvent event) throws IOException {
        servicesPane.setVisible(false);
        serviceInsideName.setText("");
        serviceInsidePassword.setText("");
        loginUsernameField.clear();
        loginPasswordField.clear();

        for(int i = 0; i < 9; i++) {
            serviceButton = (JFXButton) serviceVbox.getChildren().get(i);
            serviceButton.setText("");
        }




        /* scene = ((Stage)FXMLLoader.load(getClass().getResource("gui.fxml"))).getScene();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        */


    }
    // ---------------------------------
    // TEST CODES
    /*
    @FXML
    void fooAction() throws SQLException {
        final int[] i = {0};
        m.getServices().getHashMap().forEach((serviceName, credentials) -> {
            foo = (JFXButton) serviceVbox.getChildren().get(i[0]);
            foo.setText(serviceName);
            i[0]++;
        });
    }
    */
}
