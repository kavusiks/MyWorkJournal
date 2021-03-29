package myworkjournal.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import myworkjournal.core.Employee;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import myworkjournal.persistence.EmployeePersistence;


public abstract class AbstractController {
	@FXML Button saveBtn;

	private Employee employee;

	protected void setEmployee(Employee employee) {
		this.employee = employee;
	}

	protected Employee getEmployee() {
		return this.employee;
	}

	abstract void sceneSwitchedUpdate();

	protected void changeScreen(String URLName, Node button) throws IOException {
		Stage stage = (Stage) button.getScene().getWindow();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(URLName));
		Parent root=fxmlLoader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		AbstractController controller = fxmlLoader.getController();
		controller.setEmployee(getEmployee());
		stage.show();
		controller.sceneSwitchedUpdate();
		System.out.println(getEmployee().toString());
	}

	@FXML
	protected void saveData() throws FileNotFoundException  {
		if (employee== null) {
			throw new IllegalArgumentException("Employee to save is not defined");
		}
		EmployeePersistence employeeSaver = new EmployeePersistence("src/main/resources/myworkjournal/persistence/employee.txt", employee);
		employeeSaver.writeFile();
		System.exit(0);
	}
	
	
}
