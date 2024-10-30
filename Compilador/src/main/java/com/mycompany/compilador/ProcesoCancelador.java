/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.compilador;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Mihai
 */
public class ProcesoCancelador {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escribe 'exit' en cualquier momento para cancelar el proceso...");
        String input = scanner.nextLine();
        if ("exit".equalsIgnoreCase(input)) {
            try {
                File archivoCancelacion = new File("cancel_signal.txt");
                if (archivoCancelacion.createNewFile()) {
                    System.out.println("Señal de cancelación enviada.");
                }
            } catch (IOException e) {
                System.out.println("Error al crear archivo de cancelación: " + e.getMessage());
            }
        }
    }
}
