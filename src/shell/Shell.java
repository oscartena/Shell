/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package shell;

/**
 *
 * @author oscar
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Clase que implementa un shell de línea de comandos.
 */
public class Shell {
    /**
     * Método principal que inicia el shell y permite al usuario introducir comandos.
     * @param args(no se utilizan)
     */
    public static void main(String[] args) {
        System.out.print("> ");

        Command lastCommand = null; //Variable para almacenar el último comando ejecutado

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String input = reader.readLine();

            while (input != null && !input.equals("exit")) {
                if (input.equals("last-command")) {
                    if (lastCommand != null) {
                        System.out.println("Información del último comando ejecutado:\n" + lastCommand);
                    } else {
                        System.out.println("No se ha ejecutado ningún comando todavía.");
                    }
                } else {
                    Command command = new Command(input);
                    String output = command.ejecutar();
                    lastCommand = command;
                    
                    if (output.isEmpty()) {
                        System.out.println("Salida redirigida al archivo.");
                    } else {
                        System.out.println("Resultado de la ejecución:\n" + output);
                    }
                }

                System.out.print("> ");

                input = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



