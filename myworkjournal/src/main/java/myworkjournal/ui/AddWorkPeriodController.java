package myworkjournal.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import myworkjournal.core.Employee;
import myworkjournal.core.Work;
import myworkjournal.core.WorkPeriod;

import java.io.IOException;
import java.time.LocalDate;

public class AddWorkPeriodController extends AbstractController {

  @FXML ListView<String> existingMonthListView;
  @FXML Button goToDataInputBtn;
  @FXML TextField wageInputField;
  @FXML DatePicker addMonthDatePicker;
  @FXML Label errorMessage;

  @Override void sceneSwitchedUpdate() {
    existingMonthListView.getItems().clear();
    for (String monthIdentifier: getEmployee().getWorkPeriods().keySet()){
      existingMonthListView.getItems().add(String.valueOf(monthIdentifier));
    }
  }

  @FXML private void addMonth() throws IllegalArgumentException {
    String error = "";
    errorMessage.setText(error);
    Employee employee = getEmployee();
    if (addMonthDatePicker.getValue() == null){
      error = "Måned for periode er ikke valgt";
      errorMessage.setText(error);
      throw new IllegalArgumentException(error);
    }
    LocalDate startDate = addMonthDatePicker.getValue();
    if(!wageInputField.getText().matches(("^[0-9]+$"))) {
      error = "Timeslønn må være et tall";
      errorMessage.setText(error);
      throw new IllegalArgumentException(error);
    }
    int wage = Integer.parseInt(wageInputField.getText());
    WorkPeriod newPeriod = new WorkPeriod(startDate, wage);
    if (employee.getWorkPeriods().containsKey(newPeriod.getIdentifier())) {
      error = "Work month already exists";
      errorMessage.setText(error);
      throw new IllegalArgumentException(error);
    }
    employee.addWorkPeriod(newPeriod);
    setEmployee(employee);
    sceneSwitchedUpdate();

  }

  @FXML private void goToDataInput() throws IOException {
    changeScreen("dataInput.fxml",goToDataInputBtn );
  }
}
