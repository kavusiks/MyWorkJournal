package myworkjournal.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import myworkjournal.core.WorkPeriod;

import java.io.IOException;
import java.text.DecimalFormat;

public class ViewStatsController extends AbstractController {

  private static final DecimalFormat df = new DecimalFormat("#.##");

  @FXML Label lonnLabel;
  @FXML Label timerLabel;
  @FXML Button goToDataInputBtn;
  @FXML Label bestWorkPeriodLabel;
  @FXML Label bestWorkPeriodSalaryLabel;
  @FXML Label avgShiftsLabel;
  @FXML Label avgWorkHoursLabel;
  @FXML Label avgSalaryLabel;
  @FXML Label avgHourlyWageLabel;





  @FXML
  private void goToDataInput() throws IOException {
    //Employee employee = new Employee(profileNameInputField.getText());
    //setLoggedInEmployee(getLoggedInEmployee());
    changeScreen("dataInput.fxml", goToDataInputBtn);
  }



  @Override void sceneSwitchedUpdate() {
    int timer = 0;
    double lonn = getLoggedInEmployee().getTotalSalary();
    for(WorkPeriod workPeriod: getLoggedInEmployee()) {
      System.out.println("WorkPeriod " + workPeriod.getIdentifier() +" totlønn: " + workPeriod.getMonthSalary() + " times "+ workPeriod.getHourlyWage() + " timer med arbeid tot: " + workPeriod.getTotalHours());
    }
    System.out.println("LØNN, " + lonn);
    WorkPeriod highestPaisWorkPeriod = getLoggedInEmployee().getBestPaidWorkPeriod();
    for (WorkPeriod workPeriod: getLoggedInEmployee().getWorkPeriods()) {
      timer+= workPeriod.getTotalHours();
    }
    bestWorkPeriodLabel.setText(highestPaisWorkPeriod.getIdentifier());
    bestWorkPeriodSalaryLabel.setText(Double.toString(highestPaisWorkPeriod.getMonthSalary()));
    lonnLabel.setText(lonn +"kr");
    timerLabel.setText((Integer.toString(timer)));
    avgShiftsLabel.setText(df.format(getLoggedInEmployee().getAverageShiftAmount()));
    avgWorkHoursLabel.setText(df.format(getLoggedInEmployee().getAverageWorkHours()));
    avgSalaryLabel.setText(df.format(getLoggedInEmployee().getAverageSalary()));
    avgHourlyWageLabel.setText(df.format(getLoggedInEmployee().getAverageHourlyWage()));

  }
}
