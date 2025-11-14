package com.eventbooking.eventbookingapp.controller;

import com.eventbooking.eventbookingapp.model.Event;
import com.eventbooking.eventbookingapp.model.EventManager;
import com.eventbooking.eventbookingapp.util.ViewSwitcher;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AttendeeController {

    @FXML
    private FlowPane eventFlowPane;

    public void initialize() {
        loadEventCards();
    }

    private void loadEventCards() {
        eventFlowPane.getChildren().clear();
        ObservableList<Event> events = EventManager.getEvents();

        for (Event event : events) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/eventbooking/eventbookingapp/event-card.fxml"));
                VBox eventCard = loader.load();

                EventCardController controller = loader.getController();
                controller.setData(event);

                eventCard.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getClickCount() == 2) {
                        showEventDetailsWindow(event);
                    }
                });

                eventFlowPane.getChildren().add(eventCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showEventDetailsWindow(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/eventbooking/eventbookingapp/event-details-view.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Event Details: " + event.getName());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(eventFlowPane.getScene().getWindow());

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/com/eventbooking/eventbookingapp/styles.css").toExternalForm());
            dialogStage.setScene(scene);

            EventDetailsController controller = loader.getController();
            controller.loadEventData(event);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not load the event details view.");
            alert.showAndWait();
        }
    }

    public void switchToOrganizer(ActionEvent event) throws IOException {
        ViewSwitcher.switchScene(event, "/com/eventbooking/eventbookingapp/organizer-view.fxml");
    }
}