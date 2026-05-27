package aeroviajes.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidad de bajo nivel para el manejo de archivos. Encapsula la
 * serializacion / deserializacion de objetos (archivos .dat) y la escritura
 * de texto plano (archivos .txt, por ejemplo los tickets).
 *
 * Es una clase de utilidad: no se instancia (constructor privado) y todos
 * sus metodos son estaticos.
 *
 * Demuestra: manejo de archivos (File I/O), genericos y excepciones.
 *
 * @author Equipo Aeroviajes
 */
public final class GestorArchivos {

    private GestorArchivos() {
        // Clase de utilidad: no se permite instanciarla.
    }

    /**
     * Serializa una lista de objetos a un archivo .dat.
     * Crea el directorio padre si no existe.
     *
     * @param lista la coleccion a guardar
     * @param ruta  la ruta del archivo destino
     * @throws IOException si ocurre un error de escritura
     */
    public static <T> void guardarLista(List<T> lista, String ruta) throws IOException {
        File archivo = new File(ruta);
        File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(lista);
        }
    }

    /**
     * Deserializa una lista de objetos desde un archivo .dat.
     * Si el archivo no existe (primera ejecucion), devuelve una lista vacia.
     *
     * @param ruta la ruta del archivo de origen
     * @return la lista leida, o una lista vacia si el archivo no existe
     * @throws IOException            si ocurre un error de lectura
     * @throws ClassNotFoundException si la clase serializada no se encuentra
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> cargarLista(String ruta)
            throws IOException, ClassNotFoundException {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<T>) ois.readObject();
        }
    }

    /**
     * Escribe contenido de texto plano a un archivo (por ejemplo, un ticket).
     * Crea el directorio padre si no existe.
     *
     * @param contenido el texto a escribir
     * @param ruta      la ruta del archivo destino
     * @throws IOException si ocurre un error de escritura
     */
    public static void guardarTexto(String contenido, String ruta) throws IOException {
        File archivo = new File(ruta);
        File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(archivo, StandardCharsets.UTF_8)) {
            writer.print(contenido);
        }
    }

    /**
     * Indica si un archivo existe en la ruta dada.
     */
    public static boolean existe(String ruta) {
        return new File(ruta).exists();
    }
}
