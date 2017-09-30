package sample;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.bson.Document;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by joaquinto on 9/28/17.
 */
public class AttendanceList implements Initializable{
    private final static String HOST = "localhost";
    private final static int PORT = 27017;
    private String firstna;
    private String lastna;
    private String genda;
    private String email_;
    private String fone_no;
    private String fname;
    private String lname;
    private String _email;
    private String _gender;
    private String pnumber;
    private int pos;

    @FXML
    private Label status;
    @FXML
    private TableView<Attendees> table;
    @FXML
    private TableColumn<Attendees, Integer> id;
    @FXML
    private TableColumn<Attendees, String> firstname;
    @FXML
    private TableColumn<Attendees, String> lastname;
    @FXML
    private TableColumn<Attendees, String> email;
    @FXML
    private TableColumn<Attendees, String> gender;
    @FXML
    private TableColumn<Attendees, String> phone_number;
    @FXML
    private Button addAttend;

//  create a primary stage object
    Stage primaryStage = new Stage();

//  create an observable list to hold the Attendees object in the Attendees class
    public ObservableList<Attendees> list;

    public List attend = new ArrayList();


//  create a mongodb connection
    MongoClient mongoClient = new MongoClient(HOST, PORT);

//  create a database name
    MongoDatabase mongoDatabase = mongoClient.getDatabase("Confab");

//  create a collection
    MongoCollection coll = mongoDatabase.getCollection("Attendance");
//  call the find all method
    MongoCursor<Document> cursor = coll.find().iterator();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.setEditable(true);

        try{
            for(int i = 0; i < coll.count(); i++){
                pos = i +1;

                Document doc = cursor.next();
                fname = doc.getString("firstname");
                lname = doc.getString("lastname");
                _email = doc.getString("email");
                _gender = doc.getString("gender");
                pnumber = doc.getString("phone_number");

                attend.add(new Attendees(pos, fname, lname, _email, _gender, pnumber ));
            }
            list = FXCollections.observableArrayList(attend);


        }
        finally {
//          close the connection
            cursor.close();
        }

//      call the setTable method
        setTable();
    }



    public void addAttendance() throws Exception{
//      get the current window
        Stage stage = (Stage)addAttend.getScene().getWindow();

//      close the current window
        stage.close();

//      load the main class window
        Main mainClass = new Main();

        mainClass.start(primaryStage);
    }

    public void editAttendanceList() {
        Attendees selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null){
//          display an error message if no row was selected
            status.setText("Please select a row and perform this action again");
        }
        else{
            firstna = selectedItem.getFirstname();
            lastna = selectedItem.getLastname();
            genda = selectedItem.getGender();
            email_ = selectedItem.getEmail();
            fone_no = selectedItem.getPhone_number();

//          here i am using the email as my primary key to find each document to update it in the database
            coll.updateOne(eq("email", email_), new Document("$set",
                    new Document("firstname",firstna )
                            .append("lastname", lastna)
                            .append("gender", genda)
                            .append("email", email_)
                            .append("phone_number", fone_no)));

//          call the rePopulateTable method
            rePopulateTable();

//          call the setTable method
            setTable();

//          hide the error message
            status.setText("");
        }

    }


    public void deleteAttendance(){
//      get the selected row
        Attendees selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null){
//          display an error message
            status.setText("Please select a row and perform this action again");
        }
        else{
//          get the value of the selected email column
            String email_ = selectedItem.getEmail();

//          here i am using the email as my primary key to find each document to delete from the database
            coll.deleteOne(eq("email", email_));

//          call the rePopulateTable method
            rePopulateTable();

//          call the setTable method
            setTable();

//          hide the error message
            status.setText("");
        }
    }

    public void setTable(){
//      this makes the table editable
        table.setEditable(true);

//      make firstname column editable with a textfield
        firstname.setCellFactory(TextFieldTableCell.forTableColumn());

//      gets the new value and calls the setFirstname method
        firstname.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Attendees, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Attendees, String> event) {

                ((Attendees)event.getTableView().getItems().get(event.getTablePosition().getRow()))
                        .setFirstname(event.getNewValue());

            }
        });

//      make lastname column editable with a textfield
        lastname.setCellFactory(TextFieldTableCell.forTableColumn());

//      gets the new value and calls the setFirstname method
        lastname.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Attendees, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Attendees, String> event) {
                ((Attendees)event.getTableView().getItems().get(event.getTablePosition().getRow()))
                        .setLastname(event.getNewValue());
            }
        });

//      make phone number column editable with a textfield
        phone_number.setCellFactory(TextFieldTableCell.forTableColumn());

//      gets the new value and calls the setFirstname method
        phone_number.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Attendees, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Attendees, String> event) {
                ((Attendees)event.getTableView().getItems().get(event.getTablePosition().getRow()))
                        .setPhoneNumber(event.getNewValue());
            }
        });

//      set the values of each columns to display on the table
        id.setCellValueFactory(new PropertyValueFactory<Attendees, Integer>("id"));
        firstname.setCellValueFactory( new PropertyValueFactory<Attendees, String>("firstname"));
        lastname.setCellValueFactory( new PropertyValueFactory<Attendees, String>("lastname"));
        email.setCellValueFactory( new PropertyValueFactory<Attendees, String>("email"));
        gender.setCellValueFactory( new PropertyValueFactory<Attendees, String>("gender"));
        phone_number.setCellValueFactory( new PropertyValueFactory<Attendees, String>("phone_number"));
        table.setItems(list);
    }

    private void rePopulateTable(){
//      calls the find all methods from the mongodb database
        MongoCursor<Document> cursor = coll.find().iterator();

//      clears the attend list so that the previous data won't be displayed together with this new ones on the table
        attend.clear();
        try{
//          loop through the database and then populate the list
            for(int i = 0; i < coll.count(); i++){
                pos = i +1;

                Document doc = cursor.next();
                fname = doc.getString("firstname");
                lname = doc.getString("lastname");
                _email = doc.getString("email");
                _gender = doc.getString("gender");
                pnumber = doc.getString("phone_number");

                attend.add(new Attendees(pos, fname, lname, _email, _gender, pnumber ));
            }
            list = FXCollections.observableArrayList(attend);


        }
        finally {
//          close the connection
            cursor.close();
        }
    }
}
