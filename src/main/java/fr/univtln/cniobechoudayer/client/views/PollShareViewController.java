package fr.univtln.cniobechoudayer.client.views;

import fr.univtln.cniobechoudayer.client.net.SendEmail;
import fr.univtln.cniobechoudayer.model.entities.Poll;
import fr.univtln.cniobechoudayer.server.controllers.PollController;
import fr.univtln.cniobechoudayer.server.exceptions.PersistanceException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PollShareViewController implements Initializable{

    @FXML
    private AnchorPane rootView;

    @FXML
    private Text accessCodeText;

    @FXML
    private Text managementCodeText;

    @FXML
    private ImageView sendCodeMailButton;

    @FXML
    private ImageView viewPollButton;

    private Poll currentPoll;

    public PollShareViewController(int codePoll) throws PersistanceException {
        this.currentPoll = PollController.searchPollByCode(codePoll);
    }

    /*
    Method to display HomeView
     */
    @FXML
    private void goBackHome() throws IOException {
        loadScreen("HomeView");
    }

    /**
     * Method to display pollview
     */
    public void displayPoll() throws IOException {
        loadScreen("PollView", currentPoll);
    }

    /**
    Method to load a view
     */
    @FXML
    public void loadScreen(String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + resource + ".fxml"));
        System.out.println("Loading : /fxml/" + resource + ".fxml");
        AnchorPane ap = loader.load();
        rootView.getChildren().setAll(ap);
    }

    /**
     * Method to load pollview
     * @param poll
     */
    @FXML
    public void loadScreen(String resource, Poll poll) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + resource + ".fxml"));
        loader.setController(new PollViewController(poll));
        AnchorPane ap = loader.load();
        rootView.getChildren().setAll(ap);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(currentPoll != null){
            accessCodeText.setText(String.valueOf(currentPoll.getIdPoll()));
            managementCodeText.setText(String.valueOf(currentPoll.getManagerCode()));
        }
    }

    @FXML
    public void sendEmailToUser(){
        SendEmail.sendEmail(currentPoll);
    }
}
