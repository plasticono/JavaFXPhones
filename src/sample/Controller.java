package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.phones.Phone;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    String currentItemSelected;

    @FXML
    ContextMenu contextMenu;

    @FXML
    MenuItem editItem;

    @FXML
    ListView listView;

    MenuBar menuBar;

    @FXML
    ProgressBar progressBar;

    @FXML
    Label percentageBar;

    @FXML
    ComboBox<Integer> minYearCombo;

    @FXML
    ComboBox<Integer> maxYearCombo;

    @FXML
    BorderPane borderPane;

    private void filter() {
        int minYear = minYearCombo.getValue();
        int maxYear = maxYearCombo.getValue();
        if ((Integer) maxYear == null) {
            maxYear = 3000;
        }
        if ((Integer) minYear == null) {
            minYear = 0;
        }
        ObservableList filteredList = FXCollections.observableArrayList();
        for (Phone phone : Main.phones) {
            if (phone.getYearFromDate() >= minYear && phone.getYearFromDate() <= maxYear) {
                filteredList.add(phone.getName() + " - " + phone.getYearFromDate());
            }
        }
        if (listView != null && listView.getItems() != null) {
            listView.getItems().clear();
        }
        progressBar.setProgress(((double) filteredList.size()) / ((double) Main.phones.size()));
        DecimalFormat df = new DecimalFormat("#.##");
        percentageBar.setText(df.format(progressBar.getProgress() * 100) + "%");
        filteredList.forEach(phone -> listView.getItems().add(phone));

    }

    @FXML
    public void buttonClicked(Event e) {
        filter();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File savedDataFile = new File(Main.PHONE_OUTPUT_PATH);
        if (savedDataFile.exists()) {
            try {
                FileInputStream file = new FileInputStream(Main.PHONE_OUTPUT_PATH);
                Phone.restoreData(file);
                Main.print("Data restored");
                file.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        DecimalFormat df = new DecimalFormat("#.##");

        menuBar = new MenuBar();
        menuBar.setUseSystemMenuBar(true);
        Menu file = new Menu("File");
        MenuItem importItem = new MenuItem("Import");
        importItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(".txt", "Text Files"));
                File selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                    try {
                        if (selectedFile.getName().contains("android")) {
                            Phone.loadAndroid(selectedFile);
                        } else {
                            Phone.loadiPhones(selectedFile);
                        }
                        filter();
                    } catch (Exception e) {
                        Main.print("File wasn't read correctly.");
                    }
                }
            }
        });
        MenuItem addItem = new MenuItem();
        file.getItems().add(addItem);
        file.getItems().add(importItem);
        menuBar.getMenus().add(file);

        borderPane.getChildren().add(menuBar);

        progressBar.setProgress(1D);
        percentageBar.setText(df.format(progressBar.getProgress() * 100) + "%");

        maxYearCombo.getItems().addAll(
                2000, 2001, 2002,
                2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013,
                2014, 2015, 2016, 2017, 2018, 2019, 2020);
        minYearCombo.getItems().addAll(
                2000, 2001, 2002,
                2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013,
                2014, 2015, 2016, 2017, 2018, 2019, 2020);

        minYearCombo.setValue(2000);
        maxYearCombo.setValue(2020);

        minYearCombo.setOnAction(event -> filter());
        maxYearCombo.setOnAction(event -> filter());

        List<String> phoneListString = new ArrayList<>();
        Main.phones.forEach(phone -> phoneListString.add(phone.getName() + " - " + phone.getYearFromDate()));
        FXCollections.observableArrayList(phoneListString).forEach(s -> listView.getItems().add(s));

        contextMenu = new ContextMenu();
        listView.setContextMenu(contextMenu);
        MenuItem editItem = new MenuItem("Edit");
        MenuItem viewItem = new MenuItem("View");
        contextMenu.getItems().addAll(viewItem, editItem);
        editItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createEditWindow();
            }
        });
        viewItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    changeSceneWithData((String) listView.getSelectionModel().getSelectedItem());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        listView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            public void handle(ContextMenuEvent event) {
                contextMenu.show(listView, event.getScreenX(), event.getScreenY());
            }
        });

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {
                if (click.getButton() == MouseButton.PRIMARY && click.getClickCount() == 2) {
                    //Use ListView's getSelected Item
                    currentItemSelected = (String) listView.getSelectionModel().getSelectedItem();
                    try {
                        changeSceneWithData(currentItemSelected);
                        listView.getSelectionModel().select(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void closeWindow(Stage stage, String text) {
        stage.close();
        Phone phone = Phone.getPhoneByDisplayedName((String) listView.getSelectionModel().getSelectedItem());
        if (phone != null) {
            phone.setName(text);
            filter();
        }
    }

    private void createEditWindow() {
        Stage editWindow = new Stage();
        editWindow.initModality(Modality.APPLICATION_MODAL);
        editWindow.setAlwaysOnTop(true);
        editWindow.setResizable(false);
        editWindow.setTitle("Edit");
        TextField field = new TextField(((String) listView.getSelectionModel().getSelectedItem()).substring(0, listView.getSelectionModel().getSelectedItem().toString().length() - 7));
        VBox layout = new VBox(10);
        layout.getChildren().add(field);

        Button okButton = new Button("Done");
        okButton.setOnAction(e -> closeWindow(editWindow, field.getText()));
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> editWindow.close());

        layout.getChildren().addAll(okButton, cancelButton);

        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 250);
        editWindow.setScene(scene);
        editWindow.showAndWait();
    }

    private static void changeSceneWithData(String newValue) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Controller.class.getResource("individual.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent, 600, 600);
        IndividualController controller = loader.getController();
        controller.initData(newValue);


        Main.PRIMARY_STAGE.setUserData(newValue);
        Main.PRIMARY_STAGE.setScene(scene);

    }

}
