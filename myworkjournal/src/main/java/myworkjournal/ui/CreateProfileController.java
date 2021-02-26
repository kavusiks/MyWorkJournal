package myworkjournal.ui;

import java.io.IOException;

import myworkjournal.core.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class CreateProfileController extends AbstractController{
    @FXML
    TextField profileNameInputField;
    
    @FXML
    Button createProfileBtn;

    /*protected CreateProfileController(Employee employee) {
        super(employee);
    }*/


    @FXML
    private void initialize(){
    }
    
    @FXML
    private void createProfile() throws IOException {
        Employee employee = new Employee(profileNameInputField.getText());
        setEmployee(employee);
        //setEmployee(employee);
        changeScreen("addWorkPeriod.fxml", createProfileBtn);
    }

    @Override void sceneSwitchedUpdate() {

    }
}
