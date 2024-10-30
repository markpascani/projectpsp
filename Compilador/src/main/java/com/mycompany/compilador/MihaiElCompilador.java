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
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Mihai
 */
public class MihaiElCompilador {

    public static void main(String[] args) {
        if (!validarArgumentos(args)) {
            return;
        }

        String rutaRelativaDelArchivito = args[0];
        File archivito = new File(rutaRelativaDelArchivito);

        if (archivito.exists()) {
            try {
                compilarArchivo(rutaRelativaDelArchivito);
            } catch (IOException | InterruptedException ex) {
                System.out.println("Error durante el proceso: " + ex.getMessage());
            }
        } else {
            System.out.println("El archivo no existe.");
        }
    }


    //----------------------------------------------
    //Comprobar si el argumento recibido es correcto
    //----------------------------------------------
    private static boolean validarArgumentos(String[] args) {
        if (args.length == 0) {
            System.out.println("No has escrito nada, date una vuelta.");
            return false;
        } else if (!args[0].endsWith(".java")) {
            System.out.println("hey man! Necesito un poco de .java!!!");
            return false;
        }
        return true;
    }

    //------------------------------------------   
    //Inicia el proceso para compilar el archivo
    //------------------------------------------
    private static void compilarArchivo(String rutaRelativaDelArchivito) throws IOException, InterruptedException {
        long inicio = System.currentTimeMillis();
        ProcessBuilder pb = new ProcessBuilder("javac", rutaRelativaDelArchivito);
        Process proceso = pb.start();

        iniciarCancelacion(proceso);

        capturarSalidaYError(proceso);

        int resultado = proceso.waitFor();
        long duracion = System.currentTimeMillis() - inicio;
        System.out.println("Tiempo de ejecución: " + duracion + " ms");

        if (resultado == 0) {
            System.out.println("Compilación exitosa.");
            copiarArchivoCompilado(rutaRelativaDelArchivito);
        } else {
            System.out.println("Error en la compilación.");
        }
    }

//-------------------------------------------
// Una especie de observer para la cancelación
//-------------------------------------------
    private static void iniciarCancelacion(Process proceso) {
        File archivoCancelacion = new File("cancel_signal.txt"); // Archivo de señalización para cancelar

        // Monitorear el archivo de cancelación mientras se espera que termine el proceso de compilación
        try {
            while (proceso.isAlive()) {
                // Espera de 500 ms en cada ciclo
                if (proceso.waitFor(500, TimeUnit.MILLISECONDS)) {
                    // Salir del bucle si el proceso ha terminado naturalmente
                    break;
                }

                // Verificar si el archivo de cancelación existe
                if (archivoCancelacion.exists()) {
                    proceso.destroy(); // Terminar el proceso de compilación
                    System.out.println("Proceso de compilación cancelado por el usuario.");
                    archivoCancelacion.delete(); // Eliminar el archivo de señalización
                    return;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("La espera del proceso fue interrumpida: " + e.getMessage());
        }
    }

    //--------------------------------
    //Metodo para la salida y el error
    //--------------------------------
    private static void capturarSalidaYError(Process proceso) {
        EarlElPoliglota esclavo = new EarlElPoliglota(proceso.getInputStream(), "SALIDA");
        EarlElPoliglota esclavo2 = new EarlElPoliglota(proceso.getErrorStream(), "ERROR");

        esclavo.capturarSalida();
        esclavo2.capturarSalida();
    }

    //--------------------------------------
    //Metodo para copiar el archivoCompilado
    //--------------------------------------
    private static void copiarArchivoCompilado(String rutaRelativaDelArchivito) {
        String nombreClase = rutaRelativaDelArchivito.replace(".java", ".class");
        System.out.print("En que ruta te coloco el archivo .class: ");
        Scanner scanner = new Scanner(System.in);
        String rutaDestino = scanner.nextLine();

        try {
            Path destino = Paths.get(rutaDestino, Paths.get(nombreClase).getFileName().toString());
            Files.copy(Paths.get(nombreClase), destino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo copiado a: " + destino.toString());
        } catch (IOException ex) {
            System.out.println("Error al copiar el archivo: " + ex.getMessage());
        }
    }
}
