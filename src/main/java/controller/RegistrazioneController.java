// src/main/java/controller/RegistrazioneController.java
package controller;

import dao.ClienteDAO;
import dao.PersonaleDAO;
import domain.Cliente;
import domain.Personale;
import domain.Personale.TipoPersonale;
import exception.DAOException;

import java.util.Scanner;

public final class RegistrazioneController {

    private RegistrazioneController() {}

    public static void registraCliente(Scanner in) {
        System.out.println("\n--- Registrazione cliente ---");
        System.out.print("Nome: ");      String nome = in.nextLine().trim();
        System.out.print("Cognome: ");   String cogn = in.nextLine().trim();
        System.out.print("Username: ");  String user = in.nextLine().trim();
        System.out.print("Password: ");  String pass = in.nextLine().trim();

        try {
            Cliente nuovo = ClienteDAO.registraCliente(nome, cogn, user, pass);
            System.out.println("✅  Cliente registrato: " + nuovo + "\n");
        } catch (DAOException e) {
            System.err.println("❌ Errore registrazione cliente: " + e.getMessage() + "\n");
        }
    }

    public static void registraPersonale(Scanner in) {
        System.out.println("\n--- Registrazione personale viaggiante ---");
        System.out.print("Nome: ");      String nome = in.nextLine().trim();
        System.out.print("Cognome: ");   String cogn = in.nextLine().trim();
        System.out.print("Tipo (MACCHINISTA/CAPOTRENO): ");
        TipoPersonale tipo = TipoPersonale.valueOf(in.nextLine().trim().toUpperCase());
        System.out.print("Username: ");  String user = in.nextLine().trim();
        System.out.print("Password: ");  String pass = in.nextLine().trim();

        try {
            Personale p = PersonaleDAO.registraPersonale(nome, cogn, tipo, user, pass);
            System.out.println("✅  Personale registrato: " + p + "\n");
        } catch (DAOException e) {
            System.err.println("❌ Errore registrazione personale: " + e.getMessage() + "\n");
        }
    }
}