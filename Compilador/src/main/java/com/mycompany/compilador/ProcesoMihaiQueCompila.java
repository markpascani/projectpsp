/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.compilador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/**
 *
 * @author Mihai
 */
public class ProcesoMihaiQueCompila {

    public static void main(String[] args) {
        //----------------------------------------------
        //Comprobar si el argumento recibido es correcto
        //----------------------------------------------
        if (args.length == 0) {
            System.out.println("Borracho? No has escrito nada en argumentos, échate una siesta anda.");
            return;
        } else if (!args[0].endsWith(".java")) {
            System.out.println("Hey man!! Necesito un poquito de .java!!");
            return;
        }

        // --------------------------------------------------------------------------
        // Creo una variable para almacenar el archivo (ruta relativa) y creo un File
        // --------------------------------------------------------------------------
        String rutaRelativaDelArchivito = args[0];
        File archivito = new File(rutaRelativaDelArchivito);

        // -------------------------------------------------
        // Usamos un ProcessBuilder para compilar el archivo
        // -------------------------------------------------
        if (archivito.exists()) {

            try {
                // Paso 3: Registro del tiempo de inicio para calcular duración total después
                long inicio = System.currentTimeMillis();

                //Paso 1 (ejecución correcta del proceso
                ProcessBuilder pb = new ProcessBuilder("javac", rutaRelativaDelArchivito);
                Process proceso = pb.start(); //Aquí comienza el proceso de compilación del archivo

                //Llamamos a Earl para que nos ayude a capturar de manera sincrónica la salida estandar y de error del proceso
                EarlElPoliglota esclavo = new EarlElPoliglota(proceso.getInputStream(), "SALIDA");
                EarlElPoliglota esclavo2 = new EarlElPoliglota(proceso.getErrorStream(), "ERROR");

                esclavo.capturarSalida();
                esclavo2.capturarSalida();

                //Paso 3: esperamos que temrine y calculamos tiempo de proceso
                int resultado = proceso.waitFor();
                long duracion = System.currentTimeMillis() - inicio;

                System.out.println("Tiempo de ejecución: " + duracion + " ms");

                // Paso 4 y 6: Verificar si la compilación fue exitosa y proceder con la copia del archivo
                if (resultado == 0) {
                    System.out.println("Compilación exitosa.");

                    // Paso 6: Solicitar la ubicación para copiar el archivo .class generado
                    String nombreClase = rutaRelativaDelArchivito.replace(".java", ".class");
                    System.out.print("Ingrese la ubicación de destino para el archivo .class: ");
                    Scanner scanner = new Scanner(System.in);
                    String rutaDestino = scanner.nextLine();

                    // Copiar el archivo .class al destino especificado por el usuario
                    Path destino = Paths.get(rutaDestino, nombreClase);
                    Files.copy(Paths.get(nombreClase), destino, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Archivo copiado a: " + destino.toString());
                }else{
                    System.out.println("Jefe, fallo la compilación, no sé que ha pasado.");
                }
                    //Este catch salta si el proceso no se ha iniciado correctamente  
                }catch (IOException ex) {
                System.out.println("No hay manera de que java haga esto de buenas, tendrá que ser por las malas."+ex.getMessage());
            }catch (InterruptedException ex){
                System.out.println("No me dejaste termin...");
            }

            }else{
            System.out.println("Yo confiaba en tí... no existe el archivo.");
        }

        }
    }
