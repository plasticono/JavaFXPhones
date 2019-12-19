package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.phones.Phone;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {


    public static List<Phone> phones = new ArrayList<>();
    public static final String PHONE_OUTPUT_PATH = "/tmp/phoneData.txt";

    private Controller controller;

    boolean android;
    boolean iPhone;

    public static Scene listScene, individualScene;

    public static String CURRENT_LABEL = "Temp";
    public static Image CURRENT_IMAGE;

    public static FXMLLoader mainLoader, individualLoader;


    public static Stage PRIMARY_STAGE;

    @Override
    public void start(Stage primaryStage) throws Exception {
        int i = 0;
        mainLoader = new FXMLLoader();
        individualLoader = new FXMLLoader();
        individualLoader.setController(new IndividualController());
        Parent root = mainLoader.load(getClass().getResource("test.fxml"));
        Parent individualScene = individualLoader.load(getClass().getResource("individual.fxml"));
        primaryStage.setTitle("Phones");
        controller = new Controller();
        PRIMARY_STAGE = primaryStage;
        listScene = new Scene(root, 600, 600);
        this.individualScene = new Scene(individualScene, 600, 600);

        primaryStage.setScene(listScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        //Phone.loadPhones();
        launch(args);
    }

    @Override
    public void stop() {
        Phone.save(PHONE_OUTPUT_PATH);
    }

    public static void print(final String msg) {
        System.out.println(msg);
    }

    public static String loadFile(final String path) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line + "\n");

            reader.close();
        } catch (IOException e) {
            System.out.println("File not found!");
        }
        return builder.toString();
    }
}
