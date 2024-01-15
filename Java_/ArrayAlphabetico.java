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
public class ArrayAlphabetico {
    private ArrayAlphabetico next;
    private ArrayList<Acceso> accesos;
    private char id_char;
    
    public ArrayAlphabetico(char id_char){
        this.id_char = id_char;
        this.accesos = new ArrayList();
    }

    public ArrayList<Acceso> getAccesos() {
        return accesos;
    }

    public char getId_char() {
        return id_char;
    }

    public ArrayAlphabetico getNext() {
        return next;
    }

    public void setNext(ArrayAlphabetico next) {
        this.next = next;
    }

    public void setAccesos(ArrayList<Acceso> accesos) {
        this.accesos = accesos;
    }
    
    
    /**
     * inserta un nuevo acceso en la posicion del arrayAplhabetico correspondiente
     * @param acc Objeto Accion a insertar
     */
    public void insertarAcceso(Acceso acc){
        String nombre = acc.getNombre().toLowerCase();
        char ch = nombre.charAt(0);
        
        if(this.id_char == ch){//si el primer caracter es igual al id_char de se inserta aqui el acceso
            this.accesos.add(acc);
        }else if(this.id_char > ch){//si el ch es menor al id_char los datos actuales se pasan al siguiente bloque (next) e insertan los datos nuevos en el acutal
            ArrayAlphabetico prev = new ArrayAlphabetico(this.id_char);
            prev.setNext(this.next);
            prev.setAccesos(this.accesos);
            
            this.accesos = new ArrayList();
            this.accesos.add(acc);
            this.next = prev;
            this.id_char = ch;
        }else{
            if(this.next != null){
                if(this.next.getId_char() > ch){ //el acceso ha de insertarse entre dos ArrayAplhabetico
                    ArrayAlphabetico prev = this.next; //antiguo proximo valor
                    this.next = new ArrayAlphabetico(ch); //nuevo proximo valor
                    this.next.setNext(prev); // se pone a la cola del nuevo proximo valor el antiguo proximo valor
                }
                this.next.insertarAcceso(acc);
            }else{
                this.next = new ArrayAlphabetico(ch);
                this.next.insertarAcceso(acc);
            }
        }
    }
    
    
    /**
     * se filtran los accesos por nombre 
     * @param filtro expresion regular usada para el filtrado de accesos
     * @return 
     */
    public ArrayList<Acceso> getAccesoFiltro(String filtro){
        ArrayList<Acceso> filtrado = new ArrayList();
        
        for (int i = 0; i < this.accesos.size(); i++) {
            if(this.accesos.get(i).getNombre().toLowerCase().matches(filtro)){
                filtrado.add(this.accesos.get(i));
            }
        }
        
        return filtrado;
    }
    
    /**
     * obtiene todos los accesos almacenados de todo el array alphabetico
     * @return 
     */
    public ArrayList<Acceso> getAll(){
        ArrayList<Acceso> filtro = new ArrayList();
        
        filtro.addAll(this.accesos);
        
        if(this.next != null){
            filtro.addAll(this.next.getAll());
        }
        
        return filtro;
    }

    @Override
    public String toString() {
        String xml = "";
        
        for (int i = 0; i < this.accesos.size(); i++) {
            xml += this.accesos.get(i).toXML() + "\n";
        }
        
        if(this.next != null){
            xml += this.next.toString();
        }
        
        return xml;
    }
    
    
}
