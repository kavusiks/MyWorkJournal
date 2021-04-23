package myworkjournal.ui;

import java.io.IOException;
import java.time.LocalDate;

import javafx.scene.control.*;
import myworkjournal.core.Work;
import myworkjournal.core.WorkPeriod;
import myworkjournal.core.Employee;
import javafx.fxml.FXML;


public class DataInputController extends AbstractController {
  @FXML
  DatePicker workStartDatePicker;

  @FXML
  DatePicker workEndDatePicker;


  @FXML
  TextField shiftStartTimeInput;

  @FXML
  TextField shiftEndTimeInput;

  @FXML
  Button addDataBtn;


  @FXML
  Button removeDataBtn;

  @FXML
  Button viewStatsBtn;
  @FXML Label errorMessage;

  @FXML Button goToAddMonthBtn;

  @FXML
  ListView<Work> myDataListView;

  @FXML ChoiceBox<WorkPeriod> monthChoiceBox;

  private WorkPeriod workPeriod;




  @FXML
  private void initialize(){
  workStartDatePicker.setOnAction((event) -> {
    LocalDate selectedDate = workStartDatePicker.getValue();
    workEndDatePicker.setValue(selectedDate);

  });

    monthChoiceBox.setOnAction((event) -> {
      workPeriod = monthChoiceBox.getSelectionModel().getSelectedItem();
      workStartDatePicker.setDisable(false);
      workEndDatePicker.setDisable(false);
      shiftStartTimeInput.setDisable(false);
      shiftEndTimeInput.setDisable(false);
      addDataBtn.setDisable(false);
      removeDataBtn.setDisable(true);
      updateListView();
    });

    myDataListView.setOnMouseClicked((event) -> {
      if(myDataListView.getSelectionModel().getSelectedItem() != null)
      removeDataBtn.setDisable(false);
    });

  }

  /**
   * Used to update elements according to the scene with data from the logged in employee.
   */
  @Override void sceneSwitchedUpdate() {
    monthChoiceBox.getItems().clear();
    for (WorkPeriod workPeriod : getLoggedInEmployee().getWorkPeriods()) {
      monthChoiceBox.getItems().add(workPeriod);
    }
    updateChoiceBox();

  }

  @FXML
  private void updateChoiceBox() {
    monthChoiceBox.getItems().clear();
    for (WorkPeriod workPeriod : getLoggedInEmployee().getWorkPeriods()){
      monthChoiceBox.getItems().add(workPeriod);
    }
  }

  @FXML
  private void addToWorkPeriod(){
    String error = "";
    int startHour;
    int startMinute;
    int endHour;
    int endMinute;
    errorMessage.setText(error);
    if (monthChoiceBox.getValue()==null){
      error = "Arbeidsmåned er ikke valgt";
      errorMessage.setText(error);
      throw new IllegalArgumentException((error));
    }

    if ((workStartDatePicker.getValue() == null) || (workEndDatePicker.getValue() == null)) {
      error = "Både start- og sluttdato må være valgt.";
      errorMessage.setText(error);
      throw new IllegalArgumentException(error);
    }

    if ((!shiftStartTimeInput.getText().matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) || (!shiftEndTimeInput.getText().matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"))) {
      error = "Både start- og sluttidspunkt må være i riktig format: (15:00)";
      errorMessage.setText((error));
      throw new IllegalArgumentException(error);
    }
    else {
    startHour = Integer.parseInt(shiftStartTimeInput.getText(0, 2));
    startMinute = Integer.parseInt(shiftStartTimeInput.getText(3, 5));
    endHour = Integer.parseInt(shiftEndTimeInput.getText(0, 2));
    endMinute = Integer.parseInt(shiftEndTimeInput.getText(3, 5));
    }

    try{
    Work newData=new Work(workStartDatePicker.getValue().atTime(startHour, startMinute),workEndDatePicker.getValue().atTime(endHour, endMinute));
    workPeriod.addWork(newData);
    } catch (IllegalArgumentException e){
      errorMessage.setText(e.getMessage());
    }
    updateListView();

  }

  private void updateListView() {
    myDataListView.getItems().clear();
    for (Work work:workPeriod.getPeriodWorkHistory()) {
      myDataListView.getItems().add(work);
    }
  }


  @FXML
  private void removeFromWorkPeriod() {
    try {
      this.workPeriod.removeWork(myDataListView.getSelectionModel().getSelectedItem());
    } catch (IllegalArgumentException e) {
    errorMessage.setText(e.getMessage());
    }
    updateListView();
    deletePopupPane.setVisible(false);
    removeDataBtn.setDisable(true);

  }




  @FXML
  private void goToStats() throws IOException {
    changeScreen("myStats.fxml", viewStatsBtn);
  }

  @FXML
  private void goToAddMonth() throws IOException {
    changeScreen("addWorkPeriod.fxml", goToAddMonthBtn);
  }


}
