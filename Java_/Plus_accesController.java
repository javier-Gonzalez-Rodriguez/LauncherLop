/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Java_;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author javie
 */
public class Plus_accesController implements Initializable {

    private ArrayList<String> exePath;
    private boolean isAgregation;
    private double FitHeight = 24.0, FitWidth = 30.0; //tamaños predefinidos del icono del botón
    private int amplificacion = 5; //ampliacion de tamaño del icono del botón
    private PrincipalController padre;

    @FXML
    private ImageView add;
    @FXML
    private BorderPane fondo;
    @FXML
    private Label Lnombre;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.isAgregation = true;
    }

    /**
     * cambia el valor del boton de acceso
     */
    public void changeAgregation() {
        this.isAgregation = !this.isAgregation;
    }

    /**
     * modifica el icono de ajustes cuando el cursor se se quita de encima de el
     * elemento
     *
     * @param event
     */
    @FXML
    private void mouseExit(MouseEvent event) {
        if (this.isAgregation) {
            Image newImage = new Image(getClass().getResourceAsStream("/Icons/plus.png"));
            this.add.setImage(newImage);
        }

        this.fondo.getStyleClass().remove("fondo_icono_gaming_hover");
        this.fondo.getStyleClass().add("fondo_icono_gaming");

        this.add.setFitHeight(this.FitHeight); //reestablece el tamaño del icono

    }

    /**
     * modifica el icono de ajustes cuando el cursor se encuentra encima de el
     * elemento
     *
     * @param event
     */
    @FXML
    private void mouseEnter(MouseEvent event) {
        if (this.isAgregation) {
            Image newImage = new Image(getClass().getResourceAsStream("/Icons/plus_hover.png"));
            this.add.setImage(newImage);
        }

        this.fondo.getStyleClass().remove("fondo_icono_gaming");
        this.fondo.getStyleClass().add("fondo_icono_gaming_hover");

        this.add.setFitHeight(this.FitHeight + this.amplificacion); //amplia el tamaño del icono
    }

    /**
     * Se ejecuta al pulsar en el boton y en caso de que sea de tipo agregacion
     * se añade un nuevo acceso a la interfaz principal
     *
     * @param event
     */
    @FXML
    private void addIcon(MouseEvent event) {
        if (this.isAgregation) {

            Acceso acc = new Acceso();

            try {
                InfoPanelController inf = generarInfoPane();

                if (inf.continuar()) {
                    acc.setNombre(inf.nombreF);
                    acc.setTipo(inf.tipoF);
                    acc.setImgPath(inf.imgPath);
                    acc.setExePath(inf.getPaths());

                    this.padre.insertarAddAccesos();

                    changeAgregation();

                    this.exePath = acc.getExePath();
                    this.Lnombre.setText(acc.getNombre());

                    FileInputStream inputStream = new FileInputStream(acc.getImgPath());
                    Image imagen = new Image(inputStream);
                    this.add.setImage(imagen);

                    this.padre.setNuevoAcceso(acc);
                    this.padre.filtrado("", true);
                }

            } catch (IOException ex) {
                FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 13\n"));
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("Ha ocurrido un probelma al crear el acceso");
                alerta.setContentText(ex.getCause().toString());

                alerta.showAndWait();
            }

        } else {
            executePath();
        }

    }

    /**
     * genera la interfaz de recogida de datos para el nuevo acceso
     *
     * @return
     * @throws IOException
     */
    private InfoPanelController generarInfoPane() throws IOException {
        Stage stg = new Stage();
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.initStyle(StageStyle.UNDECORATED);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_/InfoPanel.fxml"));
        BorderPane recogidaDatos = loader.load();

        InfoPanelController inf = (InfoPanelController) loader.getController();

        Scene sc = new Scene(recogidaDatos);

        sc.setFill(Color.TRANSPARENT); //elimina el fondo de la escena para redondear con css las esquinas
        stg.initStyle(StageStyle.TRANSPARENT); //elimina el fondo de la escena para redondear con css las esquinas

        stg.setScene(sc);
        stg.showAndWait();

        return inf;
    }

    /**
     * configura el acceso dado un objeto de este tipo
     *
     * @param acc Objeto de tipo Acceso con la informacion del acceso
     */
    public void accesoPers(Acceso acc) throws FileNotFoundException {
        this.changeAgregation();

        FileInputStream inputStream = new FileInputStream(acc.getImgPath());
        Image img = new Image(inputStream);

        this.add.setImage(img);

        this.Lnombre.setText(acc.getNombre());

        this.exePath = acc.getExePath();
    }

    private void executePath() {

        for (int i = 0; i < this.exePath.size(); i++) {
            Hilo hilo = new Hilo();
            hilo.exe = this.exePath.get(i);
            
            Thread th = new Thread(hilo);
            th.start();
        }

    }

    public void setPadre(PrincipalController padre) {
        this.padre = padre;
    }

    //hilo encargado de ejecutar los programas del acceso
    public class Hilo implements Runnable {

        String exe;

        @Override
        public void run() {
            try {
                ProcessBuilder prc = new ProcessBuilder(exe);

                Process process = prc.start();
            } catch (IOException ex) {
                FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 14\n"));
            }
        }

    }
}
