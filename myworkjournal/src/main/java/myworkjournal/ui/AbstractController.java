package myworkjournal.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import myworkjournal.core.Employee;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import myworkjournal.persistence.EmployeePersistence;


/**
 * Abstract controller class used by all controllers. This class contain common methods used by the controllers.
 */
public abstract class AbstractController {
	@FXML AnchorPane popupPane;
	@FXML AnchorPane deletePopupPane;

	//loggedInEmployee can only be sat from createProfileController
	protected Employee loggedInEmployee;

	/**
	 * Used to set the employee. Method is used when the employee logs in and everytime the scene switches..
	 * @param loggedInEmployee the employee who is sat as logged in
	 */
	protected void setLoggedInEmployee(Employee loggedInEmployee) {
		this.loggedInEmployee = loggedInEmployee;
	}



	/**
	 * Used to get the currently logged in employee
	 * @return the employee who is logged in
	 */
	protected Employee getLoggedInEmployee() {
		return this.loggedInEmployee;
	}

	/**
	 * Used to update elements according to the scene with data from the logged in employee.
	 */
	abstract void sceneSwitchedUpdate();


	/**
	 * Used to switch scene
	 * @param URLName name of the fxml file that we want to load in the next scene.
	 * @param button the button that causes the scene switch. Needed to get the current stage.
	 * @throws IOException if error occurs while loading the fxml-file with the FXML-loader.
	 */
	protected void changeScreen(String URLName, Node button) throws IOException {
		Stage stage = (Stage) button.getScene().getWindow();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(URLName));
		Parent root=fxmlLoader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		AbstractController controller = fxmlLoader.getController();
		controller.setLoggedInEmployee(getLoggedInEmployee());
		stage.show();
		controller.sceneSwitchedUpdate();
	}

	@FXML
	protected void saveDataAndClose() throws FileNotFoundException  {
		if (loggedInEmployee == null) {
			throw new IllegalArgumentException("Employee to save is not defined");
		}
		EmployeePersistence employeeSaver = new EmployeePersistence("src/main/resources/myworkjournal/persistence/employee.txt");
		employeeSaver.writeFile(loggedInEmployee);
		System.exit(0);
	}

	@FXML
	protected void closeApp() {
		System.exit(0);
	}

	@FXML
	protected void showPopup() {
		popupPane.setVisible(true);
	}

	@FXML
	protected void closePopup() {
		popupPane.setVisible(false);
	}

	@FXML
	private void closeDeletePopup() {
		deletePopupPane.setVisible(false);
	}

	@FXML
	private void showDeletePopup() {
		deletePopupPane.setVisible(true);
	}


}
