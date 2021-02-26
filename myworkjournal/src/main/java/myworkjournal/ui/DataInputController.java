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
  DatePicker workDatePicker;

  @FXML
  TextField workHoursInputField;

  @FXML
  TextField shiftStartInput;
  @FXML
  Button addDataBtn;

  @FXML
  Button viewStatsBtn;

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
    System.out.println("Den clearer");
    myDataListView.getItems().clear();
    int startHour = Integer.parseInt(shiftStartInput.getText(0, 2));
    int startMinute = Integer.parseInt(shiftStartInput.getText(3, 5));
    System.out.println("starthour:"+startHour+"startminute"+startMinute);

    Work newData=new Work(workDatePicker.getValue().atTime(startHour, startMinute),Integer.parseInt(workHoursInputField.getText()));
    workPeriod.addWork(newData);
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

    shiftStartInput.setText(getEmployee().getName());
  }
}
