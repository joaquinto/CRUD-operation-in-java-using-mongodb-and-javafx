package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by joaquinto on 9/28/17.
 */

public class Attendees {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty firstname;
    private final SimpleStringProperty lastname;
    private final SimpleStringProperty email;
    private final SimpleStringProperty gender;
    private final SimpleStringProperty phone_number;

    public Attendees(Integer id, String firstname, String lastname, String email, String gender, String phone_number) {
        this.id = new SimpleIntegerProperty(id);
        this.firstname = new SimpleStringProperty(firstname);
        this.lastname = new SimpleStringProperty(lastname);
        this.email = new SimpleStringProperty(email);
        this.gender = new SimpleStringProperty(gender);
        this.phone_number = new SimpleStringProperty(phone_number);
    }
    
    public Integer getId() {
        return id.get();
    }

    public String getFirstname() {
        return firstname.get();
    }

    public String getLastname() {
        return lastname.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getGender() {
        return gender.get();
    }

    public String getPhone_number() {
        return phone_number.get();
    }

    public void setFirstname(String fname) {
        firstname.set(fname);
    }

    public void setLastname(String lname) {
        lastname.set(lname);
    }

    public void setPhoneNumber(String phoneNumber) {
        phone_number.set(phoneNumber);
    }
}
