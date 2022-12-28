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


    // Default constructor with empty body
    public GUIController() throws SQLException {}

    // Drag window setter
    @FXML
    void dragWindow(MouseEvent event) {
        window.setX(event.getScreenX() - offsetX);
        window.setY(event.getScreenY() - offsetY);
    }

    // Set window according to the drag operation
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
        // GUI stuff below
        passwdForgetNamePane.setVisible(true);
        passwdUserNotFoundLabel.setVisible(false);
        loginButton.setDisable(true);
        forgotPasswdLink.setDisable(true);
        signUpLink.setDisable(true);
    }

    // Back button1
    @FXML
    void forgetPasswdBackButton() {
        // GUI stuff below
        passwdForgetNamePane.setVisible(false);
        passwdUserNotFoundLabel.setVisible(false);
        loginButton.setDisable(false);
        forgotPasswdLink.setDisable(false);
        signUpLink.setDisable(false);
        passwdVerifName.clear();
    }

    // Back button2
    @FXML
    void forgetPasswdBackButton2() {
        // GUI stuff below
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

    // Actual reset password page
    @FXML
    void verifyButton() {
        passwdForgetPhonePane.setVisible(false);
        passwdForgetNewPasswdPane.setVisible(true);
    }

    // After password reset button clicked
    @FXML
    void resetPasswdButton() throws SQLException {
        // Updating SQL password
        m.updatePassword(passwdNewPasswd.getText());

        // GUI stuff below
        passwdForgetNewPasswdPane.setVisible(false);
        loginButton.setDisable(false);
        forgotPasswdLink.setDisable(false);
        signUpLink.setDisable(false);
        passwdNewPasswd.clear();
    }



    // SIGN UP PART
    @FXML
    void signUpLinkAction() {
        // Open the panes
        signUpPane.setVisible(true);
        signupFailPane.setVisible(false);
        loginButton.setDisable(true);
        forgotPasswdLink.setDisable(true);
        signUpLink.setDisable(true);
    }

    @FXML
    void signUpBackButton() {
        // Close the panes
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
            // New SQL connection
            db = new PSQLConnection();

            // Registering to the database according to signup info
            m = new MainUserAccount(db, signUpUsernameField.getText(), signUpPasswordField.getText(),
                    signUpNameField.getText(), signUpMailField.getText(), signUpPhoneField.getText());

            // Close the pane. And other GUI stuff below
            signupFailPane.setVisible(false);
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


    // LOG IN (SIGN IN) PART
    JFXButton lastClickedService;
    static boolean lastClicked = false;

    @FXML
    void loginButtonAction() {
        String[][] services = new String[serviceVbox.getChildren().size()][3];

        // Check the show password flag
        if (showPasswdFlag) {
            loginPasswordField.setText(passwdShowText.getText());
        }

        try {
            // Check the username and password
            UserValidator.validateUser(db, loginUsernameField.getText(), loginPasswordField.getText());

            // GUI stuff below
            servicesPane.setVisible(true);
            failLoginLabel.setVisible(false);

            // Connect the user's database
            m = new MainUserAccount(db, loginUsernameField.getText());

            // Get service info from the database
            final int[] loginIT = {0};
            m.getServices().getHashMap().forEach((serviceName, credentials) -> {
                serviceButton = (JFXButton) serviceVbox.getChildren().get(loginIT[0]);
                serviceButton.setText(serviceName);
                serviceButton.setVisible(true);
                services[loginIT[0]][1] = credentials.getKey();
                services[loginIT[0]][2] = credentials.getValue();
                loginIT[0]++;
            });

            // Set service info to text-fields and labels
            // TL;DR: Lots of GUI stuff below
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

            // Another GUI stuff below
            openinInfoPane.setVisible(true);
            welcomeLabel.setVisible(true);
            deleteServiceButton.setVisible(false);

        } catch (Exception e) {
            failLoginLabel.setVisible(true);
        }
    }

    // Show password button in the log in (sign in) part
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

    // Inside services back button
    @FXML
    void servicesBackButton() {
        // GUI stuff below
        addServicePane.setVisible(false);
        addServiceNameField.clear();
        addServicePasswdField.clear();
        addServiceUserField.clear();
        servicesPane.setDisable(false);
    }

    // Open add service pane
    @FXML
    void addServicePaneOpenerButton() {
        // GUI stuff below
        addServicePane.setVisible(true);
        servicesPane.setDisable(true);
    }


    // Adding services to the database
    @FXML
    void addServiceButton() throws SQLException {
        // Add service to the database
        m.addNewService(addServiceNameField.getText(), addServiceUserField.getText(), addServicePasswdField.getText());

        // Update the GUI for service names
        final int[] serviceIT = {0};
        m.getServices().getHashMap().forEach((serviceName, credentials) -> {
            serviceButton = (JFXButton) serviceVbox.getChildren().get(serviceIT[0]);
            serviceButton.setText(serviceName);
            serviceButton.setVisible(true);
            serviceIT[0]++;
        });

        loginButtonAction();

        // GUI stuff below
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


    // Delete services part

    // Delete service yes no pane opener
    @FXML
    void deleteServicePaneOpenerButton() {
        deleteServicePane.setVisible(true);
        servicesPane.setDisable(true);
        deleteServiceName.setText("\"" + lastClickedService.getText() + "\"");
    }

    // Delete service no button
    // TL;DR: GUI stuff below
    @FXML
    void deleteServiceNoButton() {
        deleteServicePane.setVisible(false);
        servicesPane.setDisable(false);
        deleteServiceName.setText("");
    }

    // Delete service yes button
    @FXML
    void deleteServiceYesButton() throws SQLException {
        // Remove service from database according to the last clicked service
        m.removeService(lastClickedService.getText());

        // GUI stuff below
        lastClickedService.setText(" ");
        deleteServiceButton.setVisible(false);
        servicesPane.setDisable(false);
        serviceInsidePane.setVisible(false);
        deleteServicePane.setVisible(false);
        serviceNameShower.setVisible(false);
        openinInfoPane.setVisible(true);

        // Clear the buttons
        for(int i = 0; i < serviceVbox.getChildren().size(); i++) {
            serviceButton = (JFXButton) serviceVbox.getChildren().get(i);
            serviceButton.setText("");
        }

        // Set back to login position
        loginButtonAction();
        welcomeLabel.setVisible(false);
    }

    // Sign out part
    @FXML
    void signOutButton(ActionEvent event) throws IOException {
        // GUI stuff below
        servicesPane.setVisible(false);
        lastClicked = false;
        serviceInsideName.setText("");
        serviceInsidePassword.setText("");
        loginUsernameField.clear();
        loginPasswordField.clear();
        passwdShowText.clear();
        serviceNameShower.setVisible(false);
        serviceInsidePane.setVisible(false);

        // Clear the buttons
        for(int i = 0; i < serviceVbox.getChildren().size(); i++) {
            serviceButton = (JFXButton) serviceVbox.getChildren().get(i);
            serviceButton.setText("");
        }
    }

    // GUI stuff for cool things (not cool btw)
    @FXML
    void servicesBigButton() {
        deleteServiceButton.setVisible(false);
        serviceInsidePane.setVisible(false);
        openinInfoPane.setVisible(true);
        serviceNameShower.setVisible(false);

    }
}
