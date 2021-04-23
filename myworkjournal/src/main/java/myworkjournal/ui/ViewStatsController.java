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
    changeScreen("dataInput.fxml", goToDataInputBtn);
  }



  @Override void sceneSwitchedUpdate() {
    WorkPeriod highestPaisWorkPeriod = getLoggedInEmployee().getBestPaidWorkPeriod();
    bestWorkPeriodLabel.setText(highestPaisWorkPeriod.getIdentifier());
    bestWorkPeriodSalaryLabel.setText(Double.toString(highestPaisWorkPeriod.getMonthSalary()));
    lonnLabel.setText(df.format(getLoggedInEmployee().getTotalSalary()) +"kr");
    timerLabel.setText(df.format(getLoggedInEmployee().getTotalWorkHours()));
    avgShiftsLabel.setText(df.format(getLoggedInEmployee().getAverageShiftAmount()));
    avgWorkHoursLabel.setText(df.format(getLoggedInEmployee().getAverageWorkHours()));
    avgSalaryLabel.setText(df.format(getLoggedInEmployee().getAverageSalary()));
    avgHourlyWageLabel.setText(df.format(getLoggedInEmployee().getAverageHourlyWage()));

  }
}
