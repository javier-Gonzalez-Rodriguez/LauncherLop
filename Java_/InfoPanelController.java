/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Java_;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author javie
 */
public class InfoPanelController implements Initializable {
    /**
     * valores finales, estos valores dejan de ser null cuando se pulsa sobre
     * aceptar
     */
    public boolean tipoF;
    public String nombreF;
    public String imgPath;
    private boolean acept;

    @FXML
    private ComboBox<HBox> icoApp;
    @FXML
    private TextField nombre, ruta;
    @FXML
    private RadioButton tipo1, tipo2;
    @FXML
    private Button aceptar, cancelar;
    @FXML
    private ToggleGroup tipo;
    @FXML
    private BorderPane ventana;
    @FXML
    private ListView<String> listaRutas;
    @FXML
    private Button btEliminar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            this.tipo1.setToggleGroup(tipo);
            this.tipo2.setToggleGroup(tipo);
            eventoCambioComboBox();

            this.activarActComboBox();
            cargarIconos();
        } catch (FileNotFoundException ex) {
            FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 8\n"));
        }

    }
    
    /**
     * devuelve la lista de rutas de ejecutables
     * @return 
     */
    public ArrayList<String> getPaths(){
        ObservableList lista = this.listaRutas.getItems();
        ArrayList<String> resultado = new ArrayList();
        
        resultado.addAll(lista);
        return resultado;
    }

    /**
     * activa el evento del comboBox para cuando el valor seleccionado cambia
     */
    private void eventoCambioComboBox() {
        // Configurar el manejador de eventos para detectar cambios en el ComboBox
        this.icoApp.valueProperty().addListener(new ChangeListener<HBox>() {
            @Override
            public void changed(ObservableValue<? extends HBox> observable, HBox oldValue, HBox newValue) {
                if (newValue != null) {
                    if (((Label) newValue.getChildren().get(1)).getText().equals("Nuevo Icono")) {
                        try {
                            //se selecicona la imagen para el nuevo icono
                            FileChooser fchooser = new FileChooser();

                            // Configurar el filtro para mostrar solo archivos .png
                            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos PNG (*.png)", "*.png");
                            fchooser.getExtensionFilters().add(extFilter);

                            File acceso = fchooser.showOpenDialog(null);
                            if (acceso != null) {
                                //se crea el nuevo elemento de la lista para los iconos
                                HBox hbox = generateHbox(acceso);
                                HBox fin = icoApp.getItems().get(icoApp.getItems().size() - 1);
                                //se elimina el hbox de nuevo icono para añadirlo al final de la lista
                                icoApp.getItems().remove(fin);
                                icoApp.getItems().add(hbox);
                                icoApp.getItems().add(fin);
                                //se selecciona el nuevo icono de forma automatica
                                icoApp.getSelectionModel().select(hbox);

                                imgPath = acceso.getPath();

                                String nuevoIco = FileControl.leerarchivo(FileControl.IconList);

                                nuevoIco += "<Ico>" + imgPath + "</Ico>";

                                FileControl.escribirArchivo(FileControl.IconList, nuevoIco);
                            }

                        } catch (FileNotFoundException ex) {
                            FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 9\n"));
                        }

                    } else {
                        imgPath = FileControl.PREFICONS + "\\" + ((Label) newValue.getChildren().get(1)).getText() + ".png";
                    }
                }
            }
        });
    }

    /**
     * carga los iconos predefinidos de aplicaciones o si se han cargado nuevos
     * iconos estos tambien se cargan
     *
     * @throws FileNotFoundException
     */
    private void cargarIconos() throws FileNotFoundException {
        File[] archivos = null;
        File archivo = new File(FileControl.PREFICONS);

        //si el archivo con las rutas de los iconos no existe se crea y almacenan las rutas de los iconos
        if (!(new File(FileControl.IconList).exists())) {
            FileControl.escribirArchivo(FileControl.IconList, "");
            archivos = archivo.listFiles();

            String xmlIco = "";
            for (int i = 0; i < archivos.length; i++) {
                xmlIco += "<Ico>" + archivos[i].getPath() + "</Ico>";
            }

            FileControl.escribirArchivo(FileControl.IconList, xmlIco);
        } else {
            String xml = FileControl.leerarchivo(FileControl.IconList);

            String[] xmlDiv = xml.split("<Ico>|</Ico>");
            archivos = new File[(xmlDiv.length / 2)];
            int pos = 0;
            for (int i = 0; i < xmlDiv.length; i++) {
                if (!xmlDiv[i].equals("")) {
                    archivos[pos] = new File(xmlDiv[i]);
                    pos++;
                }
            }
        }

        List<HBox> icons = new ArrayList();

        for (int i = 0; i < archivos.length; i++) {
            icons.add(generateHbox(archivos[i]));
        }

        archivo = new File(FileControl.PLUS_ICON);

        HBox hbFinal = generateHbox(archivo);
        ObservableList<Node> list = hbFinal.getChildren();

        ((Label) list.get(1)).setText("Nuevo Icono");

        icons.add(hbFinal);

        this.icoApp.getItems().addAll(icons);

    }

    /**
     * genera un Hbox para el comboBox
     *
     * @param archivo Objeto tipo File con la ruta del icono
     * @return
     */
    private HBox generateHbox(File archivo) throws FileNotFoundException {
        // Cargar la imagen
        FileInputStream inputStream = new FileInputStream(archivo.getPath());
        Image imagen = new Image(inputStream);
        ImageView imageView = new ImageView(imagen);

        imageView.setFitWidth(40);
        imageView.setFitHeight(40);

        // Crear el Label y establece el nombre del icono
        String nombre = archivo.getName();
        nombre = nombre.split("\\.")[0];
        Label label = new Label(nombre);
        label.setPrefWidth(60);
        label.setPrefHeight(40);

        // Crear el HBox y agregar la imagen y el label
        HBox hbox = new HBox(10); // Espaciado de 10 unidades entre los nodos
        hbox.setAlignment(Pos.CENTER); // Centrar los nodos horizontalmente
        hbox.getChildren().addAll(imageView, label);

        return hbox;
    }

    /**
     * funcion necesaria para que no se eliminen del comboBox los elementos al
     * seleccionarlos
     */
    public void activarActComboBox() {
        // Configurar la fábrica de celdas para personalizar la visualización de los elementos
        this.icoApp.setCellFactory(param -> new ListCell<HBox>() {
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        // Cargar la imagen
                        Image imagen = ((ImageView) item.getChildren().get(0)).getImage();
                        ImageView imageView = new ImageView(imagen);

                        imageView.setFitWidth(40);
                        imageView.setFitHeight(40);

                        // Crear el Label y establece el nombre del icono
                        String nombre = ((Label) item.getChildren().get(1)).getText();
                        Label label = new Label(nombre);
                        label.setPrefWidth(60);
                        label.setPrefHeight(40);

                        // Crear el HBox y agregar la imagen y el label
                        HBox hbox = new HBox(10); // Espaciado de 10 unidades entre los nodos
                        hbox.setAlignment(Pos.CENTER); // Centrar los nodos horizontalmente
                        hbox.getChildren().addAll(imageView, label);

                        setGraphic(hbox);
                    } catch (Exception ex) {
                        FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 10\n"));
                    }

                }
            }
        });

    }

    @FXML
    private void aceptar(MouseEvent event) {
        this.acept = true;
        this.tipoF = !this.tipo1.isSelected();
        this.nombreF = this.nombre.getText();
        cerrar();
    }

    @FXML
    private void cancelar(MouseEvent event) {
        this.acept = false;
        cerrar();
    }

    /**
     * cierra la ventana
     */
    private void cerrar() {
        Scene sc = this.ventana.getScene();
        Stage stg = (Stage) sc.getWindow();
        stg.close();
    }

    public boolean continuar() {
        return this.acept;
    }

    @FXML
    private void addrutas(MouseEvent event) {
        FileChooser fchooser = new FileChooser();

        // Configurar el filtro para mostrar solo archivos .exe
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos exe (*.exe)", "*.exe");
        fchooser.getExtensionFilters().add(extFilter);
        
        File acceso = fchooser.showOpenDialog(null);
        if(acceso != null){
            if (this.tipo1.isSelected()) {
                this.ruta.setText(acceso.getAbsolutePath());
                
                if(!this.listaRutas.getItems().isEmpty()){
                    this.listaRutas.getItems().remove(0);
                }
                
                this.listaRutas.getItems().add(acceso.getAbsolutePath());
            } else {
                this.listaRutas.getItems().add(acceso.getAbsolutePath());
            }
        }
    }

    @FXML
    private void eliminarElemento(MouseEvent event) {
        this.listaRutas.getItems().remove(this.listaRutas.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void cambiarTipo(MouseEvent event) {
        if(this.tipo1.isSelected()){
            this.listaRutas.setVisible(false);
            this.btEliminar.setVisible(false);
            this.ruta.setVisible(true);
        }else{
            this.listaRutas.setVisible(true);
            this.btEliminar.setVisible(true);
            this.ruta.setVisible(false);
        }
    }
}
