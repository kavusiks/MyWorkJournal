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
    errorMessage.setText("");
    Employee employee = getEmployee();
    LocalDate startDate = addMonthDatePicker.getValue(); //må throwe her
    int wage = Integer.parseInt(wageInputField.getText());// må throwe her
    WorkPeriod newPeriod = new WorkPeriod(startDate, wage);
    if (!employee.getWorkPeriods().containsKey(newPeriod.getIdentifier())) {
      employee.addWorkPeriod(newPeriod);
    }
    else {
      String error = "Work month already exists";
      errorMessage.setText(error);
      throw new IllegalArgumentException(error);
    }
    setEmployee(employee);
    sceneSwitchedUpdate();

  }

  @FXML private void goToDataInput() throws IOException {
    changeScreen("dataInput.fxml",goToDataInputBtn );
  }
}
