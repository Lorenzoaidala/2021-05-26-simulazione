/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="btnCreaGrafo"
	private Button btnCreaGrafo; // Value injected by FXMLLoader

	@FXML // fx:id="btnLocaleMigliore"
	private Button btnLocaleMigliore; // Value injected by FXMLLoader

	@FXML // fx:id="btnPercorso"
	private Button btnPercorso; // Value injected by FXMLLoader

	@FXML // fx:id="cmbCitta"
	private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

	@FXML // fx:id="txtX"
	private TextField txtX; // Value injected by FXMLLoader

	@FXML // fx:id="cmbAnno"
	private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

	@FXML // fx:id="cmbLocale"
	private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doCalcolaPercorso(ActionEvent event) {
		
		
		Business partenza = cmbLocale.getValue();
		Double soglia = -1.0;
		try {
			soglia = Double.parseDouble(txtX.getText());
		}catch(NumberFormatException e) {
			txtResult.setText("ERRORE - Sono ammessi solo valori numerici nel campo 'soglia'.");
			throw new RuntimeException(e);

		}
		if(partenza==null)
			txtResult.setText("Scegli un locale da valutare.");
		if(soglia<0 || soglia>1) {
			txtResult.appendText("Il valore di soglia deve essere compreso fra 0 e 1.");
		}
		Business arrivo = model.getLocaleMigliore();
		List<Business> best = model.trovaPercorso(partenza, arrivo, soglia);
		if(best.size()==0) {
			txtResult.setText("Non esiste un percorso.");
		return;
		}
		else {
			txtResult.appendText(best.toString());
			txtResult.appendText("Passi effettuati: "+best.size()+".\n");
			return;
		}
	
	

}

@FXML
void doCreaGrafo(ActionEvent event) {
	txtResult.clear();
	String città = cmbCitta.getValue();
	int anno = cmbAnno.getValue();
	if(cmbCitta.getValue()==null || cmbAnno.getValue()==null) {
		txtResult.setText("ERRORE - Selezionare una anno ed una città.");
		return;
	}
	model.creaGrafo(anno, città);
	txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi.\n", model.getNVertici(), model.getNArchi()));
	cmbLocale.getItems().addAll(model.getLocali());
}

@FXML
void doLocaleMigliore(ActionEvent event) {
	if(cmbCitta.getValue()==null || cmbAnno.getValue()==null) {
		txtResult.setText("ERRORE - Selezionare una anno ed una città.");
		return;
	}
	Business migliore = model.getLocaleMigliore();
	txtResult.appendText("Il locale migliore per i parametri selezionati è "+migliore.getBusinessName()+"\n");

}

@FXML // This method is called by the FXMLLoader when initialization is complete
void initialize() {
	assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
	assert btnLocaleMigliore != null : "fx:id=\"btnLocaleMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
	assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
	assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
	assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
	assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
	assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
	assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
}

public void setModel(Model model) {
	this.model = model;
	cmbAnno.getItems().addAll(model.getAnni());
	cmbCitta.getItems().addAll(model.getCittà());
}
}
