/**
 * Sample Skeleton for 'NewUfoSightings.fxml' Controller Class
 */

package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewUfoSightingsController {

	private Model model;
	private int anno;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="txtxG"
	private TextField txtxG; // Value injected by FXMLLoader

	@FXML // fx:id="btnCreaGrafo"
	private Button btnCreaGrafo; // Value injected by FXMLLoader

	@FXML // fx:id="txtT1"
	private TextField txtT1; // Value injected by FXMLLoader

	@FXML // fx:id="txtT2"
	private TextField txtT2; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	/*
	 * Permettere all’utente di inserire nelle apposite caselle di testo un numero
	 * di giorni xG (con valori tra il 1 e 180, estremi inclusi) e l’anno da
	 * considerare (con valori tra il 1906 ed il 2014, estremi inclusi).
	 */
	@FXML
	void doCreaGrafo(ActionEvent event) {
		int anno, xG = 0;
		try {
			anno = Integer.parseInt(txtAnno.getText());
			xG = Integer.parseInt(txtxG.getText());
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Errore");
			alert.setHeaderText("Formato dati non valido");
			alert.showAndWait();
			return;
		}
		if (xG < 1 || xG > 180) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Errore");
			alert.setHeaderText("La var xG deve essere tra 1 e 180, estremi inclusi");
			alert.showAndWait();
			return;
		}
		if (anno < 1906 || anno > 2014) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Errore");
			alert.setHeaderText("La var T1 deve essere tra 1 e 180, estremi inclusi");
			alert.showAndWait();
			return;
		}
		this.anno = anno;
		model.creaGrafo(anno, xG);
		Map<State, Double> res = model.getListaPesiPerStato();
		for (Entry<State, Double> entry : res.entrySet())
			txtResult.appendText(String.format("%s - %.0f\n", entry.getKey().getId(), entry.getValue()));
	}

	@FXML
	void doSimula(ActionEvent event) {
		int T1 = 0, T2 = 0;
		try {
			T1 = Integer.parseInt(txtT1.getText());
			T2 = Integer.parseInt(txtT2.getText());
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Errore");
			alert.setHeaderText("Formato dati non valido");
			alert.showAndWait();
			return;
		}
		if (T1 > 364 || T2 > 364 || T1 < 1 || T2 < 1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Errore");
			alert.setHeaderText("La var T1 e T2 deve essere tra 1 e 364, estremi inclusi");
			alert.showAndWait();
			return;
		}
		model.simula(T1, T2, this.anno);
		Map<State, Integer> resSim = model.getRisultatiSimulazione();
		resSim.entrySet().forEach(a->txtResult.appendText(a.getKey() + " - " + a.getValue()));

	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtxG != null : "fx:id=\"txtxG\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtT2 != null : "fx:id=\"txtT2\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;

	}
}
