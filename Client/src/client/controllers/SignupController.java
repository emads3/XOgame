/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;

import client.ClientApp;
import client.network.Session;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class SignupController implements Initializable {
    
    @FXML private Label label=new Label();
    @FXML private Text massage = new Text();
    @FXML private TextField userName = new TextField();
    @FXML private TextField userPassword = new TextField();
    @FXML private TextField confirmPassword = new TextField();
    @FXML private TextField firstName = new TextField();
    @FXML private TextField lastName = new TextField();
    @FXML private ImageView userPic = new ImageView();
    @FXML ListView<ImageView> imglist = new ListView<>();
    @FXML private ImageView v1 = new ImageView();
    private ObservableList list = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    @FXML private void handleButtonAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Registration error!");
        if( userName.getText().equals("") || 
            userPassword.getText().equals("") ||
            confirmPassword.getText().equals("") || 
            firstName.getText().equals("") ||
            lastName.getText().equals("") ){
            alert.setContentText("Please complete all your information!");
            alert.showAndWait();
        }else if(!userPassword.getText().equals(confirmPassword.getText())){
            alert.setContentText("Password doesn't match the confirmation!");
            alert.showAndWait();
        }else if(imglist.getSelectionModel().getSelectedIndex()<0){
            alert.setContentText("Please select profile picture!");
            alert.showAndWait();
        }else{
            if(ClientApp.session == null){
                ClientApp.session = new Session("127.0.0.1", 5555);
            }
            ClientApp.session.openConnection();
            if(ClientApp.session.connected){
                String avatar = "person"+imglist.getSelectionModel().getSelectedIndex()+".png";
                boolean regResult = ClientApp.session.playerSignup(firstName.getText(), lastName.getText(), userName.getText(), userPassword.getText(), avatar);
                if(regResult){
                    Alert success = new Alert(AlertType.INFORMATION);
                    success.setTitle("Registration succeded!");
                    success.setContentText("Congratulations! you've registered successfully!\nYou will be redirected to login page");
                    success.showAndWait();
                    ClientApp.primaryStage.hide();
                    ClientApp.primaryStage.setScene(client.ClientApp.signIn);
                    ClientApp.primaryStage.show();
                }else{
                    alert.setContentText("Registration failed! username already existed!");
                    alert.showAndWait();
                }
            }else{
                alert.setContentText("Cannot establish connection with server");
                alert.showAndWait();
            }
            ClientApp.session.closeConnection();
        }
    }
    @FXML private void handleButton_back_Action(ActionEvent event) {
        ClientApp.primaryStage.hide();
        ClientApp.primaryStage.setScene(ClientApp.signIn);
        ClientApp.primaryStage.show();
    }
    
}
