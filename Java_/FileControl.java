/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Java_;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author javie
 */
public class FileControl {

    public static final String CONF = "Access.txt";//ruta para la configuracion de accesos
    public static final String PREFICONS = ".\\PrefAppIcons";//ruta para los iconos predefinidos
    public static final String PLUS_ICON = ".\\Icons\\plus.png";//ruta de los iconos de aplicacion del sistema
    public static final String IconList = "icons.txt";//ruta del archivo con las rutas de todos los iconos tanto por defecto como nuevos añadidos por el usuario
    public static final String LOG = "log.txt";//ruta del archivo de logs
    public static final String SYSTEM = "system.txt";//archivo con informaicon del sistema
    

    /**
     * lee un archivo y devuelve el contenido del docuemnto
     *
     * @param ruta ruta donde se encuentra el archivo
     * @return String
     */
    public static String leerarchivo(String ruta) {
        String info = "";
        File archivo = new File(ruta);
        if(archivo.exists()){
            FileReader flujo = null;
            BufferedReader lectura = null;

            try {
                flujo = new FileReader(archivo);
                lectura = new BufferedReader(flujo);
                String line = "";
                while ((line = lectura.readLine()) != null) {
                    info += line;
                }

            } catch (IOException ex) {
                FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 4\n"));
            } finally {
                try {
                    lectura.close();
                    flujo.close();
                } catch (IOException ex) {
                    FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 5\n"));
                }
            }
        }
        
        return info;
    }

    /**
     * guarda contenido en un archivo
     *
     * @param ruta del archivo
     * @param contenido a guardar en el archivo
     */
    public static void escribirArchivo(String ruta, String contenido) {
        File archivo = new File(ruta);
        FileWriter flujo = null;
        BufferedWriter escribir = null;

        try {
            flujo = new FileWriter(archivo);
            escribir = new BufferedWriter(flujo);

            escribir.write(contenido);

        } catch (IOException ex) {
            FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 6\n"));
        }finally{
            try {
                escribir.close();
                flujo.close();
            } catch (IOException ex) {
                FileControl.escribirArchivo(FileControl.LOG, (ex.getCause().toString() + " error code: 7\n"));
            }
        }
    }

    /**
     * crea un directorio y/o archivo en caso de que no exista
     *
     * @param path
     */
    public static void crearArchivo(String path) {
        File archivo = new File(path);
        if (!archivo.exists()) {
            archivo.mkdirs();
        }
    }

    /**
     * extrae del archivo de configuracion la lista de accesos
     *
     * @param xml con la informacion de los accesos
     * @return
     */
    public static ArrayList<Acceso> getAccesos(String xml) {
        ArrayList<Acceso> accesos = new ArrayList();

        String[] xmlDiv = xml.split("<acceso>|</acceso>");

        Acceso acc = null;
        for (int i = 0; i < xmlDiv.length; i++) {
            if (!xmlDiv[i].equals("") && !xmlDiv[i].equals("\n")) {
                acc = new Acceso();
                acc.xmlTojava(xmlDiv[i]);//obtiene la informacion del xml y lo almacena en la clase Acceso
                accesos.add(acc);
            }
        }

        return accesos;
    }

}
