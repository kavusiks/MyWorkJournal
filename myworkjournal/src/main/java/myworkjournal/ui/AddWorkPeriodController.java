package myworkjournal.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import myworkjournal.core.Employee;
import myworkjournal.core.Work;
import myworkjournal.core.WorkPeriod;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class AddWorkPeriodController extends AbstractController {

  @FXML ListView<String> existingMonthListView;
  @FXML Button goToDataInputBtn;
  @FXML Button addMonthBtn;
  @FXML TextField wageInputField;
  @FXML ChoiceBox<String> monthChoiceBox;
  @FXML ChoiceBox<Integer> yearChoiceBox;
  @FXML Label errorMessage;

  @FXML
  private void initialize() {
    monthChoiceBox.setValue("Velg måned");
    yearChoiceBox.setValue(2021);


    monthChoiceBox.setOnAction((event) -> {
      yearChoiceBox.setDisable(false);
      wageInputField.setDisable(false);
      addMonthBtn.setDisable(false);
    });

  }



    @Override void sceneSwitchedUpdate() {
    existingMonthListView.getItems().clear();
    for (String monthIdentifier: getEmployee().getWorkPeriods().keySet()){
      existingMonthListView.getItems().add(String.valueOf(monthIdentifier));
    }
    monthChoiceBox.getItems().setAll(WorkPeriod.months);
    Collection<Integer> yearsToAdd = Arrays.asList((LocalDate.now().getYear() - 1), (LocalDate.now().getYear()), (LocalDate.now()
        .getYear() + 1));
    yearChoiceBox.getItems().setAll(yearsToAdd);
  }

  @FXML private void addMonth() throws IllegalArgumentException {
    String error = "";
    errorMessage.setText(error);
    Employee employee = getEmployee();
    if (monthChoiceBox.getValue() == null || yearChoiceBox.getValue() ==null){
      error = "Måned og år for periode må være valgt";
      errorMessage.setText(error);
      throw new IllegalArgumentException(error);
    }
    String startMonth = monthChoiceBox.getValue();
    System.out.println(startMonth);
    int startYear = yearChoiceBox.getValue();

    if(!wageInputField.getText().matches(("^[0-9]+$"))) {
      error = "Timeslønn må være et tall";
      errorMessage.setText(error);
      throw new IllegalArgumentException(error);
    }
    int wage = Integer.parseInt(wageInputField.getText());
    WorkPeriod newPeriod = new WorkPeriod(startMonth, startYear, wage);
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
