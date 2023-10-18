/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shell;

/**
 *
 * @author oscar
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un comando a ejecutar en el sistema operativo.
 */
public class Command {

    private List<String> argumentos; // Argumentos del comando
    private String archivoSalida; // Archivo de salida, si se redirige
    private String salidaTexto; // Texto de salida del comando
    private int numeroSalida; // Código de salida del comando
    private boolean ejecutado; // Indica si el comando ha sido ejecutado

    /**
     * Constructor que recibe argumentos y un archivo de salida opcional.
     *
     * @param args Argumentos del comando.
     * @param archivoSalida Archivo de salida opcional (cadena vacía si no se
     * redirige).
     */
    public Command(String[] args, String archivoSalida) {
        this.argumentos = new ArrayList<>();
        for (String arg : args) {
            this.argumentos.add(arg);
        }
        this.archivoSalida = archivoSalida;
        this.ejecutado = false;
    }

    /**
     * Constructor que recibe una cadena de comando como entrada.
     *
     * @param cadenaComando Cadena de comando que puede incluir redirección de
     * salida.
     */
    public Command(String cadenaComando) {
        this.argumentos = new ArrayList<>();
        this.archivoSalida = "";
        this.ejecutado = false;

        String[] partes = cadenaComando.split("\\s+");
        boolean redirectingOutput = false;

        for (String parte : partes) {
            if (parte.equals(">")) {
                redirectingOutput = true;
            } else {
                if (redirectingOutput) {
                    this.archivoSalida = parte;
                } else {
                    this.argumentos.add(parte);
                }
            }
        }
    }

    /**
     * Obtiene la lista de argumentos del comando.
     *
     * @return Lista de argumentos.
     */
    public List<String> getArgumentos() {
        return argumentos;
    }

    /**
     * Obtiene el nombre del archivo de salida (si se redirige).
     *
     * @return Nombre del archivo de salida.
     */
    public String getArchivoSalida() {
        return archivoSalida;
    }

    /**
     * Obtiene el texto de salida del comando.
     *
     * @return Texto de salida del comando.
     */
    public String getSalidaTexto() {
        return salidaTexto;
    }

    /**
     * Obtiene el código de salida del comando.
     *
     * @return Código de salida del comando.
     */
    public int getNumeroSalida() {
        return numeroSalida;
    }

    /**
     * Comprueba si el comando ha sido ejecutado.
     *
     * @return `true` si el comando ha sido ejecutado, `false` en caso
     * contrario.
     */
    public boolean Ejecutado() {
        return ejecutado;
    }

    /**
     * Ejecuta el comando y devuelve su salida estándar como una cadena.
     *
     * @return Salida estándar del comando o cadena vacía si se redirige.
     */
    public String ejecutar() {
        if (!ejecutado) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(argumentos);
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                int exitCode = process.waitFor();
                ejecutado = true;
                numeroSalida = exitCode;

                salidaTexto = readProcessOutput(process);

                if (exitCode != 0) {
                    System.err.println("Error en la ejecución del comando.");
                }

                return salidaTexto;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        return salidaTexto != null ? salidaTexto : "";
    }

    /**
     * Lee y devuelve la salida estándar del proceso.
     *
     * @param proceso Proceso del cual leer la salida.
     * @return Salida estándar del proceso como una cadena.
     * @throws IOException Si ocurre un error al leer la salida del proceso.
     */
    private String readProcessOutput(Process proceso) throws IOException {
        StringBuilder output = new StringBuilder();
        try (InputStream is = proceso.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    /**
     * Genera una cadena con la información del comando.
     *
     * @return Cadena que incluye el comando a ejecutar, número de parámetros, PID, salida del
     * comando, código de salida y su estado de finalización.
     */
    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        info.append("Comando a ejecutar: ").append(String.join(" ", argumentos)).append("\n");
        info.append("Número de parámetros: ").append(argumentos.size()).append("\n");

        if (ejecutado) {
            info.append("PID (Identificador único): ").append(ProcessHandle.current().pid()).append("\n");
            info.append("Salida del comando: ").append(salidaTexto != null ? salidaTexto : "Redirigida al archivo").append("\n");
            info.append("Código de salida (exitValue): ").append(numeroSalida).append("\n");
            info.append("Comando finalizado: ").append(numeroSalida == 0 ? "Sí" : "No").append("\n");
        } else {
            info.append("El comando no ha sido ejecutado todavía.");
        }

        return info.toString();
    }
}
