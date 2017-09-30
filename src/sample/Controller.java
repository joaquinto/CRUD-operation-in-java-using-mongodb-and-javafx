package sample;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    private final static String HOST = "localhost";
    private final static int PORT = 27017;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private Label status;
    @FXML
    private TextField email;
    @FXML
    public ComboBox<String> gender;
    @FXML
    private TextField pnumber;
    @FXML
    private Button att;

//  create an observable list to hold the content of the combobox
    ObservableList<String> list  = FXCollections.observableArrayList("Male", "Female");

//  create a primary stage object
    Stage primaryStage = new Stage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//      set the items of the combobox
        gender.setItems(list);
    }

    public void getFieldValues(ActionEvent event){
        try{
//          create a connection to mongodb server
            MongoClient mongoClient = new MongoClient(HOST, PORT);

//          create a database name
            MongoDatabase mongoDatabase = mongoClient.getDatabase("Confab");

//          create a collection
            MongoCollection coll = mongoDatabase.getCollection("Attendance");

//          get the values of the fields
            Document doc = new Document("firstname", firstname.getText())
                    .append("lastname", lastname.getText())
                    .append("email", email.getText())
                    .append("gender", gender.getValue())
                    .append("phone_number", pnumber.getText());

//          save the document
            coll.insertOne(doc);

//          display a success message
            status.setText("Saved Successfully!!!");

//          set the fields to null or empty
            firstname.setText("");
            lastname.setText("");
            email.setText("");
            gender.setValue(null);
            pnumber.setText("");
        }
        catch (Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
//          display the error message
            status.setText("Failed to save");
        }
    }

    public void goToAttendanceList() throws Exception{
//      get the current window
        Stage stage = (Stage)att.getScene().getWindow();

//      close the current window
        stage.close();

//      load the attendance list window
        Parent root = FXMLLoader.load(getClass().getResource("AttendanceList.fxml"));
        primaryStage.setTitle("Attendance List");
        primaryStage.setScene(new Scene(root, 747, 400));
        primaryStage.show();

    }


}
