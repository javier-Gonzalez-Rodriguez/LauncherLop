/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Java_;

import java.util.ArrayList;

/**
 *
 * @author javie
 */
public class Acceso {
    private ArrayList<String> exePath;
    private String imgPath;
    private String nombre;
    private boolean tipo;//0 == unico, 1 == multiple

    public Acceso() {
    }

    public void setExePath(ArrayList<String> exePath) {
        this.exePath = exePath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }
        
    public ArrayList<String> getExePath() {
        return exePath;
    }
    
    public String getExePath_(){
        return exePath.get(0);
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isTipo() {
        return tipo;
    }
    
    
    /**
     * pasa los datos a formato XML
     * @return 
     */
    public String toXML(){
        String xml = "<acceso>\n<paths>";
        
        for (int i = 0; i < this.exePath.size(); i++) {
            xml += "<path>" + this.exePath.get(i) + "</path>\n";
        }
        
        xml += "</paths>";
        
        xml += "<img>" + this.imgPath +"</img>\n";
        
        xml += "<nombre>" + this.nombre + "</nombre>";
        
        xml += "<tipo>" + this.tipo + "</tipo>";
        
        xml += "\n</acceso>";
        
        return xml;
    }
    
    /**
     * extrae la informacion del xml y lo almacena
     * @param xml 
     */
    public void xmlTojava(String xml){
        
        String[] xmlDiv = xml.split("<paths>|</paths>");
        
        xmlDiv = xmlDiv[1].split("<path>|</path>");
        this.exePath = new ArrayList();
        //recoge todas las rutas de ejecutables y los almacena
        for (int i = 0; i < xmlDiv.length; i++) {
            if(!xmlDiv[i].equals("") && !xmlDiv[i].equals("\n")){
                this.exePath.add(xmlDiv[i]);
            }
        }
                
        xmlDiv = xml.split("<img>|</img>");
        
        this.imgPath = xmlDiv[1];
        
        xmlDiv = xml.split("<nombre>|</nombre>");
        
        this.nombre = xmlDiv[1];        
        
        xmlDiv = xml.split("<tipo>|</tipo>");
        
        this.tipo = Boolean.valueOf(xmlDiv[1]);

    }
    
}
