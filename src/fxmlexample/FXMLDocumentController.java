/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxmlexample;

import java.awt.TextArea;
import java.awt.TextField;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;



/**
 *
 * @author ak
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Text filename;
    private TextField txt_btchsize;
    private TextArea appout;
    private TextField txt_recCount;
    
    private String tracknos;
    
    @FXML
    private void filePickerClicked(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter( "*.xls","xls");
    fileChooser.getExtensionFilters().add(extFilter);
    File file = fileChooser.showOpenDialog(null);
    filename.setText(file.getAbsolutePath());
    tracknos = file.getAbsolutePath();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    @FXML
    private void processClicked(ActionEvent event)
    {
    
    }
    
}
