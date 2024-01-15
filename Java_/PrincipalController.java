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
import javafx.application.Platform;//no eliminar o peta la aplicacion
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author javie
 */
public class PrincipalController implements Initializable {

    @FXML
    private BorderPane toolbar;
    @FXML
    private ImageView ajustesButt;
    @FXML
    private ImageView minimizar;
    @FXML
    private ImageView cerrar;
    @FXML
    private GridPane fondo;
    /**
     * Initializes the controller class.
     */

    private double xOffset = 0;//establece la posicion x para el desplazamiento de la ventana
    private double yOffset = 0;//establece la posicion y para el desplazamiento de la ventana
    private int col = 0, row = 0, maxCol, maxRow; //valores usados para saber donde colocar el siguiente icono
    private boolean maxIcons = false; //establece si se pueden seguir insertando iconos
    private ArrayAlphabetico accesos; //contiene la ruta a las aplicaciones
    private ArrayList<Acceso> filtroAccesos; //contiene los accesos filtrados o todos en su defecto
    @FXML
    private TextField TFfiltrado;
    @FXML
    private Label eliminarFiltro;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.filtroAccesos = new ArrayList();

            this.maxCol = this.fondo.getColumnCount() - 1;
            this.maxRow = this.fondo.getRowCount() - 1;
            activarMovimientoManual();
            establecerFondo("/Icons/fondoDefault.png");
            insertarAccesos();
            insertarAddAccesos();
        } catch (IOException ex) {
            FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 15\n"));
        }

    }

    /**
     * activa el movimiento del toolbar
     */
    private void activarMovimientoManual() {
        // Evento para manejar el inicio del arrastre
        toolbar.setOnMousePressed((MouseEvent event) -> {
            Stage stage = (Stage) toolbar.getScene().getWindow();
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Evento para manejar el arrastre
        toolbar.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) toolbar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * dada la ruta de una imagen esta se carga como fondo de pantalla
     *
     * @param path ruta de la imagen .png
     */
    private void establecerFondo(String path) {
        Image img = new Image(path);
        BackgroundImage myBI = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(200, 200, true, true, true, true));

        Background bg = new Background(myBI);

        fondo.setBackground(bg);
    }

    /**
     * obtiene los accesos directos del archivo de configuracion
     */
    private void insertarAccesos() throws IOException {
        String xml = FileControl.leerarchivo(FileControl.CONF);

        ArrayList<Acceso> acc = FileControl.getAccesos(xml);
        
        if(acc.size()>0){
            String lowerNombre = acc.get(0).getNombre().toLowerCase();
            char ch = lowerNombre.charAt(0);

            this.accesos = new ArrayAlphabetico(ch);

            for (int i = 0; i < acc.size(); i++) {
                insertarAcceso(acc.get(i));

                this.accesos.insertarAcceso(acc.get(i));
            }

            this.filtroAccesos.addAll(this.accesos.getAll());
        }
    }
    
    /**
     * inserta un nuevo Objeto Acceso al arrayAlphabetico de accesos
     * @param acc Objeto de tipo Acceso a insertar
     */
    public void addNuevoAcceso(Acceso acc){
        this.accesos.insertarAcceso(acc);
    }

    /**
     * inserta un acceso personalizado en la interfaz
     *
     * @param acc Objeto de tipo Acceso con la informacion
     * @throws IOException
     */
    public void insertarAcceso(Acceso acc) throws IOException {
        if (this.row <= this.maxRow) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_/Plus_acces.fxml"));
            BorderPane butt = loader.load();

            ((Plus_accesController) loader.getController()).accesoPers(acc);

            this.fondo.add(butt, col, row);

            aumentarPosicionesGridLayout();
        }
    }

    /**
     * inserta el boton para agregar nuevos accesos accesos
     */
    public void insertarAddAccesos() throws IOException {

        if (this.row <= this.maxRow) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_/Plus_acces.fxml"));
            BorderPane butt = loader.load();
            
            ((Plus_accesController) loader.getController()).setPadre(this);
            
            this.fondo.add(butt, col, row);
            aumentarPosicionesGridLayout();
        }

    }

    /**
     * Establece la posicion del siguiente acceso directo
     */
    private void aumentarPosicionesGridLayout() {
        this.col++;
        //si el numero de columnas ha alcanzado el maximo se pasa a la 
        //siguiente fila y se reinician las columnas
        if (this.col > this.maxCol) {
            this.col = 0;
            this.row++;
        }

        if (this.row > this.maxRow) {
            this.maxIcons = true;
        }
    }

    @FXML
    private void minimizar(MouseEvent event) {
        // Obtén el objeto Stage asociado a la ventana actual
        Stage stage = (Stage) this.toolbar.getScene().getWindow();

        // Minimiza la ventana
        stage.setIconified(true);
    }

    @FXML
    private void cerrar(MouseEvent event) {
        Scene sc = this.fondo.getScene();
        Stage stg = (Stage) sc.getWindow();
        stg.close();
    }

    /**
     * modifica el icono de ajustes cuando el cursor se sale de encima de el
     * elemento
     *
     * @param event
     */
    @FXML
    private void ajustesNotHover(MouseEvent event) {
        Image newImage = new Image(getClass().getResourceAsStream("/Icons/ajustes_icon.png"));
        this.ajustesButt.setImage(newImage);
    }

    /**
     * modifica el icono de ajustes cuando el cursor se encuentra encima de el
     * elemento
     *
     * @param event
     */
    @FXML
    private void ajusterHover(MouseEvent event) {
        Image newImage = new Image(getClass().getResourceAsStream("/Icons/ajustes_icon_hover.png"));
        this.ajustesButt.setImage(newImage);
    }

    /**
     * modifica el icono de ajustes cuando el cursor se sale de encima de el
     * elemento
     *
     * @param event
     */
    @FXML
    private void minExit(MouseEvent event) {
        Image newImage = new Image(getClass().getResourceAsStream("/Icons/minimizar_icon.png"));
        this.minimizar.setImage(newImage);
    }

    /**
     * modifica el icono de ajustes cuando el cursor se encuentra encima de el
     * elemento
     *
     * @param event
     */
    @FXML
    private void minEnter(MouseEvent event) {
        Image newImage = new Image(getClass().getResourceAsStream("/Icons/minimizar_icon_hover.png"));
        this.minimizar.setImage(newImage);
    }

    /**
     * modifica el icono de ajustes cuando el cursor se sale de encima de el
     * elemento
     *
     * @param event
     */
    @FXML
    private void closeExit(MouseEvent event) {
        Image newImage = new Image(getClass().getResourceAsStream("/Icons/cerrar-icon.png"));
        this.cerrar.setImage(newImage);
    }

    /**
     * modifica el icono de ajustes cuando el cursor se encuentra encima de el
     * elemento
     *
     * @param event
     */
    @FXML
    private void closeEnter(MouseEvent event) {
        Image newImage = new Image(getClass().getResourceAsStream("/Icons/cerrar-icon_hover.png"));
        this.cerrar.setImage(newImage);
    }

    @FXML
    private void filtrar(KeyEvent event) {
        filtrado(event.getCode().toString(), false);
    }
    /**
     * aplica un nuevo filtro
     * @param nuevoCh nuevo valor para el filtro
     * @param forzar fuerza un filtrado sin tener en cuenta el valor "nuevoCh"
     */
    public void filtrado(String nuevoCh, boolean forzar){
        String regex = this.TFfiltrado.getText();
        if (nuevoCh.length() == 1 || forzar) {
            regex += nuevoCh + ".*";
            regex = regex.toLowerCase();

            AplicarFiltro(regex);
        } else {
            if (nuevoCh.equals("BACK_SPACE")) {
                regex = this.TFfiltrado.getText().toLowerCase() + ".*";

                AplicarFiltro(regex);
            }
        }
    }

    /**
     * aplica el filtro eliminando todos los elementos de pantalla y añadiendo
     * solo los del filtro
     *
     * @param regex patron del filtro, para mostrar todos los elementos regex =
     * ".*"
     */
    private void AplicarFiltro(String regex) {
        if(this.accesos != null){
            if (!regex.equals(".*")) {
                this.filtroAccesos = this.accesos.getAccesoFiltro(regex);
            } else {
                this.filtroAccesos = this.accesos.getAll();
            }

            this.fondo.getChildren().clear();

            this.col = 0;
            this.row = 0;

            for (int i = 0; i < this.filtroAccesos.size(); i++) {
                try {
                    this.insertarAcceso(this.filtroAccesos.get(i));
                } catch (IOException ex) {
                    FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 16\n"));
                }
            }

            try {
                insertarAddAccesos();
            } catch (IOException ex) {
                FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 17\n"));
            }
        }
    }

    @FXML
    private void abrirAjustes(MouseEvent event) {
        try {
            Stage stg = new Stage();
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.initStyle(StageStyle.UNDECORATED);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_/Ajustes.fxml"));
            Parent root = loader.load();
            
            if(this.accesos != null){
                ((AjustesController) loader.getController()).insertarAccesos(this.accesos.getAll());
            }
            
            ((AjustesController) loader.getController()).setPadre(this);
            
            Scene sc = new Scene(root);
            
            sc.setFill(Color.TRANSPARENT); //elimina el fondo de la escena para redondear con css las esquinas
            stg.initStyle(StageStyle.TRANSPARENT); //elimina el fondo de la escena para redondear con css las esquinas
            
            stg.setScene(sc);
            stg.show();
        } catch (IOException ex) {
            FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 18\n"));
        }
    }
    
    /**
     * controla el arrayAplhabetico para insertar nuevos accesos y guarda los cambios en el archivo de configuracion
     * @param acc nuevo acceso a insertar
     */
    public void setNuevoAcceso(Acceso acc){
        if(this.accesos == null){
            String lowerNombre = acc.getNombre().toLowerCase();
            char ch = lowerNombre.charAt(0);

            this.accesos = new ArrayAlphabetico(ch);
        }
        
        this.accesos.insertarAcceso(acc);
        
        String xml = this.accesos.toString();
        
        FileControl.escribirArchivo(FileControl.CONF, xml);
    }

    @FXML
    private void rmFiltroExit(MouseEvent event) {
        this.eliminarFiltro.setStyle("");
    }

    @FXML
    private void rmfiltroEnter(MouseEvent event) {
        this.eliminarFiltro.setStyle("-fx-effect: dropshadow(three-pass-box, #00ff00, 5, 0, 0, 0);");
    }
    
    public void refrescarInterfaz(){
        
        try {
            this.fondo.getChildren().clear();
            this.row = 0;
            this.col = 0;
            this.accesos = null;
            this.filtroAccesos = new ArrayList();
            insertarAccesos();
            insertarAddAccesos();
        } catch (IOException ex) {
            FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 19\n"));
        }
    }

    @FXML
    private void borrarFiltro(MouseEvent event) {
        this.TFfiltrado.setText("");
        filtrado("", true);
    }
}
