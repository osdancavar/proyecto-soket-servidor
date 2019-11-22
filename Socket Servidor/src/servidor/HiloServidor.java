package servidor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

class HiloServidor extends Thread {

    private DataInputStream entrada;
    private Servidor server;
    private Socket Cliente;
    public static Vector<HiloServidor> usuarioActivo = new Vector();//Atributo que almacenara objetos de tipo hilo en el servidor (usuarios activos)


    public HiloServidor(Socket socketcliente, Servidor serv) throws Exception {
        this.Cliente = socketcliente;
        this.server = serv;
        usuarioActivo.add(this);
    }

    public void run() {
        double tamaño;
       List Lista = new ArrayList();//inicia la lista 
        //abren archivo 
        File archivo = null;
        File fichero1=null;
        FileReader fr = null;
        BufferedReader br = null;
        //crean archivo 
         FileWriter fichero = null;
        PrintWriter pw = null;
       File directorio=null;
         

         
        try {
            while (true) {
                try {
                    entrada = new DataInputStream(Cliente.getInputStream());
                    //     mensaje = entrada.readUTF();
                   String nombreArchivo = entrada.readUTF().toString();
                    // Obtenemos el tamaño del archivo
                    int tam = entrada.readInt();
                    System.out.println("Recibiendo paquete " + nombreArchivo);
                     System.out.println("tamaño del paquete: " + tam);
                    // Creamos flujo de salida, este flujo nos sirve para
                    // indicar donde guardaremos el archivo
                    directorio = new File("C:/Users/OSCAR/Desktop/recepcion/" + Cliente.getInetAddress().getHostAddress()); 
                   directorio.mkdir();
                    FileOutputStream fos = new FileOutputStream("C:\\Users\\OSCAR\\Desktop\\recepcion\\"+ Cliente.getInetAddress().getHostAddress()+"\\" + nombreArchivo,true);
                    BufferedOutputStream out = new BufferedOutputStream(fos);
                    BufferedInputStream in = new BufferedInputStream(Cliente.getInputStream());
                    // Creamos el array de bytes para leer los datos del archivo
                    byte[] buffer = new byte[tam];
                    // Obtenemos el archivo mediante la lectura de bytes enviados
                    for (int i = 0; i < buffer.length; i++) {
                        buffer[i] = (byte) in.read();
                    }
                    //Escribimos el archivo
                    out.write(buffer);
                    out.flush();
                    in.close();
                    out.close();
                    Cliente.close();
                    //Cerramos los flujos
                    tamaño=tam;
                    server.mensajeria("paquete "+nombreArchivo+" recibido");
                    server.mensajeria("tamaño del paquete: "+tamaño/1024+" KB");                    
                    
                } catch (Exception e) {
                    break;
                }
            }
            
            usuarioActivo.removeElement(this);
            server.mensajeria("esperando mas archivos...");
            Cliente.close();
            
            
            
            
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

 

}
