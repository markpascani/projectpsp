/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.compilador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**Esta clase tiene la habilidad tan buscada de entender 
 * lenguajes y traducirlos para que los mortales
 * lo entiendan
 *
 * @author Mihai
 */
public class EarlElPoliglota {
    InputStream is; //Aquí está la esencia de la vida (bueno, solo es una tubería de bytes);
    String tipoSalida;
    
    //-----------------------
    //Un búlgaro(constructor)
    //-----------------------
    public EarlElPoliglota(InputStream is, String tipoSalida) {
        this.is = is;
        this.tipoSalida = tipoSalida;
    }
    
    //----------------------------------------------------------------------------------------------
    //Vamos a hacer un poco de magia en Java para capturar la salida estándar o de error del proceso
    //----------------------------------------------------------------------------------------------
    public void capturarSalida(){
        //Lo intento jefe
        try(BufferedReader elMaquina = new BufferedReader(new InputStreamReader(is))){
            String linea;
            //Mostramos cada línea de salida o error mientra el proceso hace su trabajo
            while((linea= elMaquina.readLine())!=null) System.out.println(tipoSalida + "->"+ linea);
            
        }catch(IOException ex){
            System.out.println("Fallos y más fallos: "+ tipoSalida +"->"+ex.getMessage());
        }
    }
    
}
