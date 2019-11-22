package chat;

import static com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close;
import java.awt.HeadlessException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

public class EnviarArchivo {

    private String nombreArchivo = "";
    private String estado = "";

    public EnviarArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String enviarArchivo() {

        try {

            // Creamos la direccion IP de la maquina que recibira el archivo
            // InetAddress direccion = InetAddress.getByName( "192.168.0.4" );
            InetAddress direccion = InetAddress.getByName("127.0.0.1");

            // Creamos el Socket con la direccion y elpuerto de comunicacion
            Socket socket = new Socket(direccion, 5050);
            socket.setSoTimeout(2000);
            socket.setKeepAlive(true);
            // Creamos el archivo que vamos a enviar
            File archivo = new File(nombreArchivo);

            // Obtenemos el tamaño del archivo
            int tamañoArchivo = (int) archivo.length();

            // Creamos el flujo de salida, este tipo de flujo nos permite 
            // hacer la escritura de diferentes tipos de datos tales como
            // Strings, boolean, caracteres y la familia de enteros, etc.
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            System.out.println("Enviando Archivo: " + archivo.getName());

            // Enviamos el nombre del archivo 
            dos.writeUTF(archivo.getName());

            // Enviamos el tamaño del archivo
            dos.writeInt(tamañoArchivo);

            // Creamos flujo de entrada para realizar la lectura del archivo en bytes
            FileInputStream fis = new FileInputStream(nombreArchivo);
            BufferedInputStream bis = new BufferedInputStream(fis);

            // Creamos el flujo de salida para enviar los datos del archivo en bytes
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

            // Creamos un array de tipo byte con el tamaño del archivo 
            byte[] buffer = new byte[tamañoArchivo];

            // Leemos el archivo y lo introducimos en el array de bytes 
            bis.read(buffer);

            // Realizamos el envio de los bytes que conforman el archivo
            for (int i = 0; i < buffer.length; i++) {
                bos.write(buffer[i]);
            }

            // Cerramos socket y flujos
            bis.close();
            bos.close();
            socket.close();
            return ("0");
        } catch (HeadlessException | IOException e) {
          JOptionPane.showMessageDialog(null, "Esperando Conexion...");
            System.out.println(e.toString());
            System.out.println("Esperando Conexion...");
            return ("1");
        }
    }
}
