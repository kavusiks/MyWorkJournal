package myworkjournal.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import myworkjournal.core.Employee;
import myworkjournal.core.WorkPeriod;

import java.io.IOException;

public class ViewStatsController extends AbstractController {

  @FXML Label lonnLabel;
  @FXML Label timerLabel;
  @FXML Button goToDataInputBtn;




  @FXML
  private void goToDataInput() throws IOException {
    //Employee employee = new Employee(profileNameInputField.getText());
    setEmployee(getEmployee());
    changeScreen("dataInput.fxml", goToDataInputBtn);
  }



  @Override void sceneSwitchedUpdate() {
    int timer = 0;
    int lonn = 0;
    for (WorkPeriod workPeriod: getEmployee().getWorkPeriods().values()) {
      timer+= workPeriod.getTotalHours();
      lonn += workPeriod.getMonthSalary();
    }
    lonnLabel.setText(Integer.toString(lonn)+"kr");
    timerLabel.setText((Integer.toString(timer)));
  }
}
