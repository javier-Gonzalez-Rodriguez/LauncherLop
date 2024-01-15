/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package Java_;

import javafx.application.Application;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

/**
 *
 * @author javie
 */
public class LauncherLop extends Application {
    private static final String APP = "LauncherLop";
    private static final String RUTA_INICIO_AUTOMATICO = "C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\StartUp\\LauncherLoop.lnk";
    private Stage stg;
    
    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED); // Oculta la decoración de la ventana
        Parent root = null;
        try{
            root = FXMLLoader.load(getClass().getResource("/FXML_/Principal.fxml"));
        }catch(Exception ex){
            FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 11\n"));
            System.exit(-1);
        }
        
        
        Scene sc = new Scene(root);
        sc.setFill(Color.TRANSPARENT);
        
        stage.setScene(sc);
        stage.initStyle(StageStyle.TRANSPARENT);
        
        stg = stage;
               
        stg.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    

    /**
     * ejecuta un comando en la consola de comandos de windows
     * @param comando a ejecutar
     */
    public static void ejecutarComando(String comando){
        try{
            // Crear un objeto ProcessBuilder con el comando
            ProcessBuilder processBuilder = new ProcessBuilder(comando.split(" "));

            // Redirigir la salida y el error estándar a la consola
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

            // Iniciar el proceso
            Process process = processBuilder.start();

            // Esperar a que el proceso termine
            int exitCode = process.waitFor();

        }catch(IOException | InterruptedException ex){
            FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 12\n"));
        }
        
    }
}