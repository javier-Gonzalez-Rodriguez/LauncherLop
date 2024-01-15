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
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author javie
 */
public class AccesoEditController implements Initializable {

    private String imgPath;
    private AjustesController padre;

    @FXML
    private ImageView img, b_borrarAcceso;
    @FXML
    private TextField tfNombre, tfRuta;
    @FXML
    private Button buscarRuta;
    @FXML
    private RadioButton rdunico, rbmultiple;
    @FXML
    private ToggleGroup tipo;
    @FXML
    private VBox ventana;
    @FXML
    private BorderPane multipleVentana;
    @FXML
    private Label lRuta;
    @FXML
    private ListView<String> listaEjecutables;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void cambiarImg(MouseEvent event) {
        //se selecicona la imagen para el nuevo icono
        FileChooser fchooser = new FileChooser();

        // Configurar el filtro para mostrar solo archivos .png
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos PNG (*.png)", "*.png");
        fchooser.getExtensionFilters().add(extFilter);

        File acceso = fchooser.showOpenDialog(null);
        
        if(acceso != null){
            FileInputStream inputStream = null;
            try {
                this.imgPath = acceso.getPath();
                inputStream = new FileInputStream(this.imgPath);
                Image imagen = new Image(inputStream);
                this.img.setImage(imagen);
            } catch (FileNotFoundException ex) {
                FileControl.escribirArchivo(FileControl.LOG, ex.getCause().toString() + "error code: 3\n");
            } finally {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 2\n"));
                }
            }
        }
    }

    @FXML
    private void nuevaRuta(MouseEvent event) {
        FileChooser d = new FileChooser();

        //File acceso = d.showOpenDialog(null);
        File ruta = d.showOpenDialog(null);

        if(ruta != null){
            this.tfRuta.setText(ruta.getPath());
        
            ArrayList<String> rut = new ArrayList();

            rut.add(ruta.getPath());
        }
        
    }

    /**
     * establece los datos del acceso en la interfaz
     *
     * @param acc clase Acceso con la informacion necesaria para la interfaz
     */
    public void setAcceso(Acceso acc) throws FileNotFoundException {
        this.rbmultiple.setSelected(acc.isTipo());
        this.rdunico.setSelected(!acc.isTipo());
        if(acc.isTipo()){//tipo multiple
            this.tfRuta.setText(acc.getExePath_());
            this.listaEjecutables.getItems().addAll(acc.getExePath());
        }else{//tipo unico
            this.tfRuta.setText(acc.getExePath_());
            this.listaEjecutables.getItems().add(acc.getExePath_());//inserta la ruta unica para en caso de cambiar a multiple que se pueda ver la ruta principal
        }
        
        this.tfNombre.setText(acc.getNombre());

        this.imgPath = acc.getImgPath();
        FileInputStream inputStream = new FileInputStream(this.imgPath);
        Image imgage = new Image(inputStream);
        this.img.setImage(imgage);
        
        cambioTipo(null);
    }

    public ImageView getImg() {
        return img;
    }

    public TextField getTfNombre() {
        return tfNombre;
    }

    public TextField getTfRuta() {
        return tfRuta;
    }

    public Button getBuscarRuta() {
        return buscarRuta;
    }

    /**
     * obtiene el XML del acceso
     *
     * @return
     */
    public String getInfo() {
        Acceso acc = new Acceso();
        acc.setImgPath(this.imgPath);
        acc.setTipo(!this.rdunico.isSelected());
        acc.setNombre(this.tfNombre.getText());
        
        ArrayList rutasN = new ArrayList();
        if(this.rdunico.isSelected()){
            ArrayList<String> unico = new ArrayList();
            unico.add(this.tfRuta.getText());
            rutasN = unico;
        }else{
            rutasN.addAll(this.listaEjecutables.getItems());
        }
        
        acc.setExePath(rutasN);
        
        return acc.toXML();
    }
    
    public String getPath(){
        return "<Ico>" + this.imgPath + "</Ico>";
    }

    public void setPadre(AjustesController padre) {
        this.padre = padre;
    }

    @FXML
    private void icoExit(MouseEvent event) {
        this.img.setStyle("");
    }

    @FXML
    private void icoEnter(MouseEvent event) {
        this.img.setStyle("-fx-effect: dropshadow(three-pass-box, #00ff00, 5, 0, 0, 0);");
    }

    @FXML
    private void borrarExit(MouseEvent event) {
        this.b_borrarAcceso.setStyle("");
    }

    @FXML
    private void borrarEnter(MouseEvent event) {
        this.b_borrarAcceso.setStyle("-fx-effect: dropshadow(three-pass-box, #00ff00, 5, 0, 0, 0);");
    }

    @FXML
    private void borrarAcceso(MouseEvent event) {
        this.padre.borrarAcceso(this.ventana, this);
    }

    @FXML
    private void cambioTipo(MouseEvent event) {
        if(!this.rdunico.isSelected()){
            this.multipleVentana.setVisible(true);
            this.lRuta.setVisible(false);
            this.tfRuta.setVisible(false);
            this.buscarRuta.setVisible(false);
        }else{
            this.multipleVentana.setVisible(false);
            this.lRuta.setVisible(true);
            this.tfRuta.setVisible(true);
            this.buscarRuta.setVisible(true);
        }
    }

    @FXML
    private void addMultiRuta(MouseEvent event) {
        //se selecicona una nueva ruta para el ejecutable
        FileChooser fchooser = new FileChooser();

        // Configurar el filtro para mostrar solo archivos .exe
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos exe (*.exe)", "*.exe");
        fchooser.getExtensionFilters().add(extFilter);

        File acceso = fchooser.showOpenDialog(null);
        
        if(acceso != null){
            this.listaEjecutables.getItems().add(acceso.getPath());
        }
    }

    @FXML
    private void delMultiRuta(MouseEvent event) {
        this.listaEjecutables.getItems().remove(this.listaEjecutables.getSelectionModel().getSelectedIndex());
    }

}
