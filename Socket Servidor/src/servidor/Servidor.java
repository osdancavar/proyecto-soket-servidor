package servidor;

import java.net.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import java.io.IOException;

public class Servidor extends javax.swing.JFrame {

    public Servidor() throws IOException {
        initComponents();
        System.out.println("Esperando recepcion de archivos...");
    }
    Socket cliente;

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = null;
        JOptionPane.showMessageDialog(null, "Servicio Iniciado", "Mensaje", JOptionPane.INFORMATION_MESSAGE);

        Servidor p = new Servidor();
        servidor = new ServerSocket(5050);
        p.iniciarServidor(servidor);
        //new Servidor();

    }

    public void iniciarServidor(ServerSocket servidor) {

        try {
            mensajeria("\n*.:SERVIDOR ENCENDIDO:.* \n");
            super.setVisible(true);//Nos permite ver al servidor y lo pone a correr

            while (true) {

                cliente = servidor.accept();
                mensajeria("CLIENTE CONECTADO DESDE LA DIRECCION: " + cliente.getInetAddress().getHostAddress());//Se captura la direccion ip del cliente que se conecta
                HiloServidor hilo = new HiloServidor(cliente, this);
                hilo.start();

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.toString());
            System.out.println("Recibir " + e.toString());
        }

    }

    public void mensajeria(String msg) {
        this.jTextArea1.append(" " + msg + "\n");//El .append es el que

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Servidor Chat");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setEnabled(false);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Baskerville Old Face", 0, 13)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        setBounds(0, 0, 400, 500);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}
