package myworkjournal.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import myworkjournal.core.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import myworkjournal.persistence.EmployeePersistence;


public class CreateProfileController extends AbstractController{
    @FXML
    TextField profileNameInputField;
    
    @FXML
    Button createProfileBtn;

    @FXML
    Button getProfileBtn;
    
    @FXML
    TextField savedUserDisplayField;
    
    @FXML
    Button savedUserLoginBtn;
    
    @FXML
    Label errorMessage1;


    @FXML
    private void initialize(){
      sceneSwitchedUpdate();
    }
    
    @FXML
    private void createProfile() throws IOException {
    	try {
    		Employee employee = new Employee(profileNameInputField.getText());
    		setEmployee(employee);
    		goToAddWorkPeriod();
    	} catch (IllegalArgumentException e) {
    		errorMessage1.setText(e.getMessage());   		
    	}
       
    }

    @FXML
    private void goToAddWorkPeriod() throws IOException {
      changeScreen("addWorkPeriod.fxml", createProfileBtn);
    }

/*
    @FXML
    private void getProfile() {
    	// Hent profil fra
      System.out.println("Kjører sceneswirched");
      try {
        EmployeePersistence employeePersistence = new EmployeePersistence("src/main/resources/myworkjournal/persistence/employee.txt");
        employeePersistence.readFile();
        Employee employee = employeePersistence.getEmployee();
        setEmployee(employee);
        //System.out.println(employee.getName());
        savedUserDisplayField.setText(employee.getName());
      } catch (FileNotFoundException e) {
        savedUserDisplayField.setText("No saved user");
      }
    }


    @FXML
    private void userLogin() {
    	// Logg inn til saved profile
    }
    */

    @Override void sceneSwitchedUpdate() {
      try {
        EmployeePersistence employeePersistence = new EmployeePersistence("src/main/resources/myworkjournal/persistence/employee.txt");
        //employeePersistence.readFile();
        //Employee employee = employeePersistence.getEmployee();
        Employee employee = employeePersistence.readFile();
        setEmployee(employee);
        savedUserDisplayField.setText(employee.getName());
      } catch (FileNotFoundException e) {
        savedUserDisplayField.setText("No saved user");
      }
    }
}
