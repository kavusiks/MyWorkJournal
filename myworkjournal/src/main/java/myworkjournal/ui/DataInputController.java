package myworkjournal.ui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
  Button viewStatsBtn;
  @FXML Label errorMessage;

  @FXML Button goToAddMonthBtn;

  @FXML
  ListView<String> myDataListView;

  @FXML ChoiceBox<String> monthChoiceBox;

  //Fjerne dette og lag observable choicebox som må vøre valgt før man kan enable de andre.
  LocalDate today = LocalDate.now();
  WorkPeriod workPeriod = new WorkPeriod(today, 150);

  /*
  protected DataInputController(Employee employee) {
    //super(employee);
  }*/


  @FXML
  private void initialize(){
    //AllPlans allPlans=new AllPlans();
    myDataListView.getItems().clear();
    for (Work work: workPeriod.getWorkHistory()){
      myDataListView.getItems().add(String.valueOf(work));
    }
  }
  
  private void updateChoiceBox() {
    monthChoiceBox.getItems().clear();
    for (WorkPeriod workPeriod : getEmployee().getWorkPeriods().values()){
      monthChoiceBox.getItems().add(workPeriod.getIdentifier());
    }
  }

  @FXML
  private void addToWorkPeriod(){
    //an=new Day(datoFrist.getValue());
    //this.initialize();
    String error = "";
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

    myDataListView.getItems().clear();
    int startHour = Integer.parseInt(shiftStartTimeInput.getText(0, 2));
    int startMinute = Integer.parseInt(shiftStartTimeInput.getText(3, 5));
    int endHour = Integer.parseInt(shiftEndTimeInput.getText(0, 2));
    int endMinute = Integer.parseInt(shiftEndTimeInput.getText(3, 5));
    System.out.println("starthour:"+startHour+"startminute"+startMinute);

    try{
    Work newData=new Work(workStartDatePicker.getValue().atTime(startHour, startMinute),workEndDatePicker.getValue().atTime(endHour, endMinute));
    System.out.println(newData.getHours());
    workPeriod.addWork(newData);
    } catch (IllegalArgumentException e){
      errorMessage.setText(e.getMessage());
    }
    for (Work work:workPeriod.getWorkHistory()){
      myDataListView.getItems().add(String.valueOf(work));
      System.out.println(work.toString());
    }

  }


  @FXML
  private void goToStats() throws IOException {
    Employee employee = getEmployee();
    employee.addWorkPeriod(workPeriod);
    setEmployee(employee);

    changeScreen("myStats.fxml", viewStatsBtn);
  }

  @FXML
  private void goToAddMonth() throws IOException {
    changeScreen("addWorkPeriod.fxml", goToAddMonthBtn);
  }



  @Override void sceneSwitchedUpdate() {
    monthChoiceBox.getItems().clear();
    for (WorkPeriod workPeriod : getEmployee().getWorkPeriods().values()) {
      monthChoiceBox.getItems().add(workPeriod.getIdentifier());
    }
    updateChoiceBox();

    //.setText(getEmployee().getName());
  }
}
