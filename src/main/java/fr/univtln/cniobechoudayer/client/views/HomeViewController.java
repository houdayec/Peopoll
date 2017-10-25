package fr.univtln.cniobechoudayer.client.views;

import com.jfoenix.controls.*;
import fr.univtln.cniobechoudayer.model.entities.Choice;
import fr.univtln.cniobechoudayer.model.entities.Poll;
import fr.univtln.cniobechoudayer.server.controllers.PollController;
import fr.univtln.cniobechoudayer.server.exceptions.PersistanceException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Controller of the HomeView
 * Created by Corentin on 21/10/2017.
 */
public class HomeViewController extends VBox {

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

    private PollController pollController = new PollController();

    public HomeViewController(){

    }

    /**
     * Method to search a poll using a code when a user click
     */
    @FXML
    private void searchPoll() throws IOException {
        List<Choice> listChoice = new ArrayList<>();
        listChoice.add(new Choice(new Date(Calendar.getInstance().getWeekYear())));
        listChoice.add(new Choice(new Date(Calendar.getInstance().getWeekYear()), 1502, 1403));
        listChoice.add(new Choice(new Date(Calendar.getInstance().getWeekYear()), 1604, 5811));

        loadScreen("PollView", new Poll.PollBuilder("test","corentin", "mail").setManagerCode("4444").setChoicesList(listChoice).build());
        /*if(codeTextField.getText().length() != 0 || codeTextField.getText() != null){
            Poll searchedPoll = null;
            try {
                searchedPoll = pollController.searchPollByCode(Integer.parseInt(codeTextField.getText()));
            } catch (PersistanceException e) {
                e.printStackTrace();
            }
            if(searchedPoll != null){
                loadScreen("PollView", searchedPoll);
            }
        }else{
            //error msg
        }*/
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




}
