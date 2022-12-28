package kozmikoda.passwordspace;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
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
    private Pane passwdForgetNamePane, passwdForgetPhonePane, passwdForgetNewPasswdPane, signUpPane, signupFailPane, servicesPane, addServicePane, deleteServicePane, openinInfoPane, serviceInsidePane;
    @FXML
    private JFXButton loginButton, deleteServiceButton;
    static JFXButton serviceButton, serviceButtons;
    @FXML
    private JFXCheckBox signinShowPassword;
    @FXML
    private TextField passwdVerifName, signUpNameField, signUpSurnameField, signUpUsernameField, signUpPhoneField, signUpMailField, signUpPasswordField, addServiceNameField, addServiceUserField, passwdShowText;
    @FXML
    private Hyperlink forgotPasswdLink, signUpLink;
    @FXML
    private PasswordField passwdVerifCode, passwdNewPasswd, addServicePasswdField;
    @FXML
    private VBox serviceVbox;
    @FXML
    private Label failLoginLabel, passwdUserNotFoundLabel, deleteServiceName, serviceNameShower, welcomeLabel;
    @FXML
    private TextField loginUsernameField, serviceInsideName;
    @FXML
    private PasswordField loginPasswordField, serviceInsidePassword;




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

    // Minimize button
    @FXML
    void minimizeButton() {
        window.setIconified(true);
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
    JFXButton lastClickedService;
    static boolean lastClicked = false;
    @FXML
    void loginButtonAction() {
        String[][] services = new String[serviceVbox.getChildren().size()][3];

        if (showPasswdFlag) {
            loginPasswordField.setText(passwdShowText.getText());
        }

        try {
            failLoginLabel.setVisible(false);
            UserValidator.validateUser(db, loginUsernameField.getText(), loginPasswordField.getText());
            servicesPane.setVisible(true);

            m = new MainUserAccount(db, loginUsernameField.getText());
            final int[] loginIT = {0};
            m.getServices().getHashMap().forEach((serviceName, credentials) -> {
                serviceButton = (JFXButton) serviceVbox.getChildren().get(loginIT[0]);
                serviceButton.setText(serviceName);
                serviceButton.setVisible(true);
                services[loginIT[0]][1] = credentials.getKey();
                services[loginIT[0]][2] = credentials.getValue();
                loginIT[0]++;
            });


                for (int k = 0; k < serviceVbox.getChildren().size(); k++) {
                    int finalK = k;
                    ((JFXButton) serviceVbox.getChildren().get(k)).setOnAction(e -> {
                        lastClicked = true;
                        serviceInsideName.setText(services[finalK][1]);
                        serviceInsidePassword.setText(services[finalK][2]);
                        lastClickedService =  ((JFXButton) serviceVbox.getChildren().get(finalK));
                        if(lastClickedService.getText().equals("")) {
                            serviceInsidePane.setVisible(false);
                            deleteServiceButton.setVisible(false);
                            serviceNameShower.setVisible(false);
                        }
                        else {
                            serviceNameShower.setVisible(true);
                            serviceNameShower.setText(lastClickedService.getText());
                            deleteServiceButton.setVisible(true);
                            serviceInsidePane.setVisible(true);
                            openinInfoPane.setVisible(false);
                            welcomeLabel.setVisible(false);
                        }

                    });
                }

            openinInfoPane.setVisible(true);
            welcomeLabel.setVisible(true);
            deleteServiceButton.setVisible(false);

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
        servicesPane.setDisable(false);

    }

    @FXML
    void addServicePaneOpenerButton() {
        addServicePane.setVisible(true);
        servicesPane.setDisable(true);
    }

    @FXML
    void addServiceButton() throws SQLException {
        m.addNewService(addServiceNameField.getText(), addServiceUserField.getText(), addServicePasswdField.getText());
        final int[] serviceIT = {0};
        m.getServices().getHashMap().forEach((serviceName, credentials) -> {
            serviceButton = (JFXButton) serviceVbox.getChildren().get(serviceIT[0]);
            serviceButton.setText(serviceName);
            serviceButton.setVisible(true);
            serviceIT[0]++;
        });

        loginButtonAction();

        if (lastClicked) {
            welcomeLabel.setVisible(false);
            openinInfoPane.setVisible(false);
            deleteServiceButton.setVisible(true);

        }
        else {
            welcomeLabel.setVisible(true);
            openinInfoPane.setVisible(true);
            deleteServiceButton.setVisible(false);
        }


        addServiceNameField.clear();
        addServicePasswdField.clear();
        addServiceUserField.clear();
    }



    @FXML
    void deleteServicePaneOpenerButton() {
        deleteServicePane.setVisible(true);
        servicesPane.setDisable(true);
        deleteServiceName.setText("\"" + lastClickedService.getText() + "\"");
    }

    @FXML
    void deleteServiceNoButton() {
        deleteServicePane.setVisible(false);
        servicesPane.setDisable(false);
        deleteServiceName.setText("");
    }

    @FXML
    void deleteServiceYesButton() throws SQLException {
        m.removeService(lastClickedService.getText());
        lastClickedService.setText(" ");
        deleteServiceButton.setVisible(false);
        servicesPane.setDisable(false);
        serviceInsidePane.setVisible(false);
        deleteServicePane.setVisible(false);
        serviceNameShower.setVisible(false);
        openinInfoPane.setVisible(true);
        for(int i = 0; i < serviceVbox.getChildren().size(); i++) {
            serviceButton = (JFXButton) serviceVbox.getChildren().get(i);
            serviceButton.setText("");
        }
        loginButtonAction();
        welcomeLabel.setVisible(false);
    }


    @FXML
    void signOutButton(ActionEvent event) throws IOException {
        servicesPane.setVisible(false);
        lastClicked = false;
        serviceInsideName.setText("");
        serviceInsidePassword.setText("");
        loginUsernameField.clear();
        loginPasswordField.clear();
        passwdShowText.clear();
        serviceNameShower.setVisible(false);
        serviceInsidePane.setVisible(false);

        for(int i = 0; i < serviceVbox.getChildren().size(); i++) {
            serviceButton = (JFXButton) serviceVbox.getChildren().get(i);
            serviceButton.setText("");
        }
    }

    static boolean showPasswdFlag = false;
    @FXML
    void signinShowPasswordButton() {

        showPasswdFlag = !showPasswdFlag;
        if(showPasswdFlag) {
            loginPasswordField.setVisible(false);
            passwdShowText.setText(loginPasswordField.getText());
            passwdShowText.setVisible(true);
        }
        else {
            loginPasswordField.setVisible(true);
            passwdShowText.setVisible(false);
            loginPasswordField.setText(passwdShowText.getText());
        }
    }

    @FXML
    void servicesBigButton() {
        deleteServiceButton.setVisible(false);
        serviceInsidePane.setVisible(false);
        openinInfoPane.setVisible(true);
        serviceNameShower.setVisible(false);

    }
}
