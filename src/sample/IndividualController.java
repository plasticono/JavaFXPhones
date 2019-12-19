package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.phones.Phone;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IndividualController implements Initializable {

    @FXML
    Label phoneLabel;

    @FXML
    Circle phoneImageCircle;

    @FXML
    Button backButton;

    @FXML
    public void backClicked(Event e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(Main.listScene);
        stage.show();
    }

    @FXML
    public void avatarClicked(Event e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        fileChooser.setInitialDirectory(new File("data"));
        File selectedFile = fileChooser.showOpenDialog(((Node) e.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            try {
                Image image = new Image(new FileInputStream(selectedFile));
                Phone phone = Phone.getPhoneByDisplayedName(phoneLabel.getText());
                Phone.getPhoneByDisplayedName(phoneLabel.getText()).setImage(image);
                phoneImageCircle.setFill(new ImagePattern(phone.getImage()));
                Main.print(Phone.getPhoneByDisplayedName(phoneLabel.getText()).getImage() + "");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData(Main.CURRENT_LABEL);
    }

    public void initData(String labelName) {
        if (labelName != null && (labelName.equalsIgnoreCase("Temp") || labelName.equalsIgnoreCase("test"))) {
            return;
        }
        phoneLabel.setText(labelName);
        Phone phone = Phone.getPhoneByDisplayedName(labelName);
        Image image = phone.getImage();
        if (image != null) {
            phoneImageCircle.setFill(new ImagePattern(image));
        } else {
            Main.print("IMAGE IS NULL");
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream("src/uploadAvatar.png");
                image = new Image(fileInputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            phoneImageCircle.setFill(new ImagePattern(image));
        }
    }
}
