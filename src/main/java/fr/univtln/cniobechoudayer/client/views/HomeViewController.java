package fr.univtln.cniobechoudayer.client.views;

import com.jfoenix.controls.*;
import fr.univtln.cniobechoudayer.model.entities.Choice;
import fr.univtln.cniobechoudayer.model.entities.Poll;
import fr.univtln.cniobechoudayer.server.controllers.PollController;
import fr.univtln.cniobechoudayer.server.exceptions.PersistanceException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller of the HomeView
 * Created by Corentin on 21/10/2017.
 */
public class HomeViewController implements Initializable{

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField codeTextField;
    @FXML
    private JFXTextField titlePollTextField;
    @FXML
    private JFXTextField nameCreatorTextField;
    @FXML
    private JFXTextField mailCreatorTextField;
    @FXML
    private JFXButton pollCreateButton;
    @FXML
    private JFXButton searchCodeButton;

    private JFXSnackbar errorPollNotFound;


    public HomeViewController(){

    }

     /**
     * Method to display a poll using a code
     */
    @FXML
    public void displayPollView() throws IOException{
        Poll pollToDisplay = null;
        if(codeTextField.getText().length() != 0 || codeTextField.getText() != null){
            try {
                pollToDisplay = PollController.searchPollByCode(Integer.parseInt(codeTextField.getText()));
                System.out.println(pollToDisplay);
            } catch (PersistanceException e) {
                e.printStackTrace();
            }

        }else{
            System.out.println("POLL NOT FOUND");
        }
        if(pollToDisplay != null){
            try {
                loadScreen("PollView", pollToDisplay);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            errorPollNotFound.show("Poll not found, try again.", 3500);
        }
    }


    /**
     * Method to display poll creation view
     */
    @FXML
    private void displayPollCreationView() throws IOException {
        loadScreen("PollCreationStep1View");
    }


    /**
    Method to check if access code text field is empty
    Enable - Disable access button
     */
    @FXML
    private void isAccessCodeTextFieldEmpty(){
        if(codeTextField.getText().length() > 0){
            try{
                Integer.parseInt(codeTextField.getText());
                searchCodeButton.setDisable(false);
            }catch(Exception e){
            }

        }else{
            searchCodeButton.setDisable(true);
        }
    }

    /**
     * Method to load a new view
     * @param resource
     * @throws IOException
     */
    @FXML
    public void loadScreen(String resource) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + resource + ".fxml"));
        System.out.println("Loading : /fxml/" + resource + ".fxml");
        loader.setController(new Step1ViewController());
        AnchorPane ap = loader.load();
        rootPane.getChildren().setAll(ap);
    }

    /**
     * Method to display a searched poll
     */
    @FXML
    public void loadScreen(String resource, Poll searchedPoll) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + resource + ".fxml"));
        System.out.println("Loading : /fxml/" + resource + ".fxml");
        loader.setController(new PollViewController(searchedPoll));
        AnchorPane ap = loader.load();
        rootPane.getChildren().setAll(ap);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorPollNotFound = new JFXSnackbar(rootPane);
    }
}
