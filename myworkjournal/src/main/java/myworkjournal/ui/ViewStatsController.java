package myworkjournal.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import myworkjournal.core.Employee;
import myworkjournal.core.Work;
import myworkjournal.core.WorkPeriod;
import myworkjournal.core.MyStats;

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
    setEmployee(getEmployee());
    changeScreen("dataInput.fxml", goToDataInputBtn);
  }



  @Override void sceneSwitchedUpdate() {
    MyStats stats = new MyStats(getEmployee());
    int timer = 0;
    int lonn = stats.getTotalSalary();
    for(WorkPeriod workPeriod: getEmployee().getWorkPeriods().values()) {
      System.out.println("WorkPeriod " + workPeriod.getIdentifier() +" totlønn: " + workPeriod.getMonthSalary() + " times "+ workPeriod.getHourlyWage() + " timer med arbeid tot: " + workPeriod.getTotalHours());
    }
    System.out.println("LØNN, " + lonn);
    WorkPeriod highestPaisWorkPeriod = stats.getBestPaidWorkPeriod();
    for (WorkPeriod workPeriod: getEmployee().getWorkPeriods().values()) {
      timer+= workPeriod.getTotalHours();
    }
    bestWorkPeriodLabel.setText(highestPaisWorkPeriod.getIdentifier());
    bestWorkPeriodSalaryLabel.setText(Integer.toString(highestPaisWorkPeriod.getMonthSalary()));
    lonnLabel.setText(lonn +"kr");
    timerLabel.setText((Integer.toString(timer)));
    avgShiftsLabel.setText(df.format(stats.getAverageShiftAmount()));
    avgWorkHoursLabel.setText(df.format(stats.getAverageWorkHours()));
    avgSalaryLabel.setText(df.format(stats.getAverageSalary()));
    avgHourlyWageLabel.setText(df.format(stats.getAverageHourlyWage()));
  }
}
