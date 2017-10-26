package fr.univtln.cniobechoudayer.client.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTimePicker;
import fr.univtln.cniobechoudayer.client.net.SendEmail;
import fr.univtln.cniobechoudayer.model.entities.Choice;
import fr.univtln.cniobechoudayer.server.controllers.ChoiceController;
import fr.univtln.cniobechoudayer.server.controllers.PollController;
import fr.univtln.cniobechoudayer.server.exceptions.PersistanceException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Step3ViewController implements Initializable{

    @FXML
    private AnchorPane rootView;

    @FXML
    private JFXDatePicker dayPollPicker;

    @FXML
    private JFXTimePicker startPollPicker;

    @FXML
    private JFXTimePicker endPollPicker;

    @FXML
    private JFXListView timeSlotsListView;

    @FXML
    private ImageView previousStepArrow;

    @FXML
    private Pane validationCreationArrow;

    @FXML
    private ImageView homeButton;

    @FXML
    private Text titlePollText;

    @FXML
    private JFXButton addTimeSlotButton;


    private int idPollCreated;
    private int currentStartingTime; //get it from timePicker
    private int currentEndingTime; //get it from timePicker
    private java.sql.Date choiceSqlDate;

    public static List<Choice> staticListCreatedChoices = new ArrayList<>();
    private HashMap<String, String> mapArgsNewPoll;


    /**
     * constructor retrieving previous data
     * @param passedArgsPoll is the current poll in creation
     */
    public Step3ViewController(HashMap passedArgsPoll){
        this.mapArgsNewPoll = passedArgsPoll;
    }

    @FXML
    private void goBackHome() throws IOException {
        loadScreen("HomeView");
    }

    @FXML
    public int validatePollCreation() throws IOException, PersistanceException {
        int nbMax;
        try {
            nbMax = Integer.parseInt(mapArgsNewPoll.get("NBMAX"));
        }catch(Exception e){
            nbMax = 0;
        }
        idPollCreated = PollController.createPoll(mapArgsNewPoll.get("Title"), mapArgsNewPoll.get("Location"), mapArgsNewPoll.get("Info"), mapArgsNewPoll.get("Creator"), mapArgsNewPoll.get("Mail"), nbMax,false);
        try {
            for (Choice choice: staticListCreatedChoices
                 ) {
                choice.setIdPoll(idPollCreated);
                ChoiceController.createChoiceInDB(choice.getDateChoice(), choice.getStartingTime(), choice.getEndingTime(),choice.getIdPoll());
            }
        } catch (PersistanceException e) {
            e.printStackTrace();
        }


        loadScreen("PollShareCodesView", idPollCreated);
        return 1;
    }

    @FXML
    private void goToPreviousStep() throws IOException {
        loadScreen("PollCreationStep2View", mapArgsNewPoll);
    }

    @FXML
    private void addTimeSlotToPoll(){
        currentStartingTime = getDateFromRawValue(startPollPicker);
        currentEndingTime = getDateFromRawValue(endPollPicker);
        LocalDate localDate = dayPollPicker.getValue();
        Instant ins = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date dayDate = Date.from(ins);
        choiceSqlDate = convertUtilDateToSqlDate(dayDate);

        Choice addedChoice = new Choice(choiceSqlDate,currentStartingTime,currentEndingTime);
        System.out.println("addedchoice: " + addedChoice);
        staticListCreatedChoices.add(addedChoice);
        bindListViewChoices(addedChoice);
        clearView();

        //TODO erase line below further
        validationCreationArrow.setVisible(true);
    }

    public void clearView(){
        dayPollPicker.getEditor().clear();
        startPollPicker.getEditor().clear();
        endPollPicker.getEditor().clear();
    }

    public void setView(){
        Callback<ListView<Choice>, ListCell<Choice>> factory = lv -> new ListCell<Choice>() {

            @Override
            protected void updateItem(Choice item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : String.valueOf(item.getDateChoice() + " " + Choice.getFormattedDate(item.getStartingTime(), true) + " " + Choice.getFormattedDate(item.getEndingTime(), false)));
            }

        };

        timeSlotsListView.setCellFactory(factory);
    }

    public int getDateFromRawValue(JFXTimePicker picker){
        int finalValue = 0;
        if(picker.getValue() != null) {
            String date = picker.getValue().toString();
            String dateArray[] = date.split(":");
            String stickedDates = dateArray[0] + dateArray[1];
            try {
                finalValue = Integer.parseInt(stickedDates);
            } catch (Exception e) {
                //TODO
            }
        }
        return finalValue;
    }

    public void bindListViewChoices(Choice choice){
        timeSlotsListView.getItems().add(choice);
    }

    @FXML
    public void loadScreen(String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + resource + ".fxml"));
        AnchorPane ap = loader.load();
        rootView.getChildren().setAll(ap);
    }

    /**
     Method to loadScreen with parameters
     @param resource is the view
     @param currentPoll in creation to sharfinalDatee its data through the steps
     */
    @FXML
    public void loadScreen(String resource, HashMap currentPoll) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + resource + ".fxml"));
        if(resource == "PollCreationStep2View")
            loader.setController(new Step2ViewController(currentPoll));
        AnchorPane ap = loader.load();
        rootView.getChildren().setAll(ap);
    }

    /**
     * load final share screen
     * @param resource name of view
     * @param idPoll id of created poll in order to display final screen
     * @throws IOException
     */
    @FXML
    public void loadScreen(String resource, int idPoll) throws IOException, PersistanceException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + resource + ".fxml"));
        loader.setController(new PollShareViewController(idPoll));
        AnchorPane ap = loader.load();
        rootView.getChildren().setAll(ap);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titlePollText.setText(mapArgsNewPoll.get("Title"));
        timeSlotsListView.getItems().removeAll();
        addTimeSlotButton.setDisable(true);
        for (Choice item:
             staticListCreatedChoices) {
            bindListViewChoices(item);
        }
        setView();
    }

    public void checkFields(){
        if(dayPollPicker.getValue() != null){
            addTimeSlotButton.setDisable(false);
        }else{
            addTimeSlotButton.setDisable(true);
        }
    }

    private static java.sql.Date convertUtilDateToSqlDate(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

}
