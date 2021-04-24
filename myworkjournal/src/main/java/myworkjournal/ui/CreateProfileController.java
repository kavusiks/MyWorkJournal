package myworkjournal.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import myworkjournal.core.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import myworkjournal.persistence.DataSaverInterface;
import myworkjournal.persistence.EmployeePersistence;


public class CreateProfileController extends AbstractController{
    @FXML
    TextField profileNameInputField;
    
    @FXML
    Button createProfileBtn;

    @FXML
    TextField savedUserDisplayField;

    @FXML
    Button savedUserLoginBtn;

    @FXML
    Label errorMessage1;


    @FXML
    private void initialize(){
      //loading any saved user
      sceneSwitchedUpdate();
    }



    @FXML
    private void createProfile() throws IOException {
      Employee employee = null;
      try {
    	  employee = new Employee(profileNameInputField.getText());
    	} catch (IllegalArgumentException e) {
    		errorMessage1.setText(e.getMessage());
    	}
    		setLoggedInEmployee(employee);
    		goToAddWorkPeriod();

    }

    @FXML
    private void goToAddWorkPeriod() throws IOException {
      changeScreen("addWorkPeriod.fxml", createProfileBtn);
    }


    @Override void sceneSwitchedUpdate() {
      try {
        DataSaverInterface<Employee> employeePersistence = new EmployeePersistence("src/main/resources/myworkjournal/persistence/employee.txt");
        Employee employee = employeePersistence.readFile();
        setLoggedInEmployee(employee);
        savedUserDisplayField.setText(employee.getName());
        savedUserLoginBtn.setDisable(false);
      } catch (FileNotFoundException | IllegalStateException e) {
        errorMessage1.setText("An error occured while trying to load user: " +e.getMessage());
        savedUserDisplayField.setText("No saved user");
      }
    }
}
