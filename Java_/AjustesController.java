/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Java_;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author javie
 */
public class AjustesController implements Initializable {

    @FXML
    private BorderPane ventana;
    @FXML
    private VBox vBox;
    @FXML
    private ImageView cerrar;
    @FXML
    private BorderPane toolbar_ajustes;
    
    
    
    private ArrayList<AccesoEditController> dataAccesos;
    private double xOffset = 0;//establece la posicion x para el desplazamiento de la ventana
    private double yOffset = 0;//establece la posicion y para el desplazamiento de la ventana
    private PrincipalController padre;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.dataAccesos = new ArrayList();
        activarMovimientoManual();
        
        
    }

    @FXML
    private void cerrar(MouseEvent event) {
        String xml = "";
        for (int i = 0; i < this.dataAccesos.size(); i++) {
            xml += this.dataAccesos.get(i).getInfo() + "\n";
        }
        
        //-->
        FileControl.escribirArchivo(FileControl.CONF, xml);
        Scene sc = this.ventana.getScene();
        Stage stg = (Stage) sc.getWindow();
        stg.close();
    }

    /**
     * inserta los elementos graficos que representan cada elemento de acceso
     * @param accesos Lista de accesos a insertar en elemento grafico
     */
    public void insertarAccesos(ArrayList<Acceso> accesos) {
        
        for (int i = 0; i < accesos.size(); i++) {
            Acceso acc = accesos.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_/AccesoEdit.fxml"));
                VBox accesoEdit = loader.load();

                AccesoEditController aec = (AccesoEditController) loader.getController();
                aec.setPadre(this);
                
                aec.setAcceso(acc);
                
                this.dataAccesos.add(aec);
                
                this.vBox.getChildren().add(accesoEdit);
            } catch (IOException ex) {
                FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 3\n"));
            }
        }
        
    }

    @FXML
    private void cerrarNotHover(MouseEvent event) {
        Image newImage = new Image(getClass().getResourceAsStream("/Icons/cerrar-icon.png"));
        this.cerrar.setImage(newImage);
    }

    @FXML
    private void cerrarHover(MouseEvent event) {
        Image newImage = new Image(getClass().getResourceAsStream("/Icons/cerrar-icon_hover.png"));
        this.cerrar.setImage(newImage);
    }
    
    /**
     * activa el movimiento del toolbar
     */
    private void activarMovimientoManual() {
        // Evento para manejar el inicio del arrastre
        this.toolbar_ajustes.setOnMousePressed((MouseEvent event) -> {
            Stage stage = (Stage) toolbar_ajustes.getScene().getWindow();
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Evento para manejar el arrastre
        this.toolbar_ajustes.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) toolbar_ajustes.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    @FXML
    private void guardarCambios(MouseEvent event) {
        String xml = "";
        
        for (int i = 0; i < this.dataAccesos.size(); i++) {
            xml += this.dataAccesos.get(i).getInfo();
        }
        
        FileControl.escribirArchivo(FileControl.CONF, xml);
        this.padre.refrescarInterfaz();
    }
    
    public void setPadre(PrincipalController padre){
        this.padre = padre;
    }
    
    /**
     * limpia la interfaz y elimina de la memoria el acceso para guardar posteriormente la eliminacion del acceso
     * @param acc VBox que contiene el acceso en la interfaz
     * @param controller controllador del objeto que contiene el acceso
     */
    public void borrarAcceso(VBox acc, AccesoEditController controller){
        this.vBox.getChildren().remove(acc);
        this.dataAccesos.remove(controller);
    }
}
