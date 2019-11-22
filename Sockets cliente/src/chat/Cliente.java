package chat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Cliente extends javax.swing.JFrame {

    //Declaramos los atributos de la clase de manera privada
    JFileChooser dlg;
    private Socket cliente;
    private final int PUERTOC = 5050;//Puerto por el cual se conecta y salen los mensajes
    public int proceso;
    public int contador;
    //private String host = "192.168.157.1";

    private String host = "127.0.0.1";
    private DataOutputStream salida;
    public String nombre;
    public String paquete;

    public Cliente() {
        initComponents();
        proceso = 1;
        contador = 0;

        try {
            nombre = JOptionPane.showInputDialog("Su Nombre");
            super.setTitle(super.getTitle() + nombre);
            super.setVisible(true);
            cliente = new Socket(host, PUERTOC);//Permite enviar la conexion al servidor
            cliente.setSoTimeout(2000);
            cliente.setKeepAlive(true);
            DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
            salida.writeUTF(nombre);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.toString());

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cliente en Linea ");
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(null);

        jButton2.setText("SELECCIONAR ARCHIVO");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(350, 50, 160, 23);

        jButton3.setText("ENVIAR ARCHIVO");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(370, 130, 130, 23);

        jLabel1.setText("ARCHIVO A SELECCIONAR");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(360, 20, 140, 20);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(340, 90, 200, 20);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NUMERO", "NOMBRE", "TAMAÑO", "ESTADO"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(10, 10, 330, 290);

        setSize(new java.awt.Dimension(562, 365));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dlg = new JFileChooser();
        int opcion = dlg.showOpenDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            String file = dlg.getSelectedFile().getPath();
            jLabel2.setText(file);
        }
        if (opcion == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(this, "Accion cancelada por el usuario");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed


    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
        
        String retorno;
        Scanner reader = new Scanner(System.in);
        int numero = 0;

        CorregirRuta corregir = new CorregirRuta(jLabel2.getText(), "\\", "\\\\");
        String ruta = corregir.obtenerRutaCorregidaWindows();
        File archivo = new File(ruta);
        proceso = 1;

        int tamañoArchivo = (int) archivo.length();
        EnviarArchivo ea;
        paquete= JOptionPane.showInputDialog("ingrese el numero de paquetes a enviar");
        numero=Integer.parseInt(paquete);
        numero = tamañoArchivo / numero;
        numero = numero + 1000;

        int sizeOfFiles = numero;//1MB
        byte[] buffer = new byte[sizeOfFiles];

        String fileName = archivo.getName();

        //try-with-resources to ensure closing stream
        try (FileInputStream fis = new FileInputStream(archivo);
                BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount = 0;

            while ((bytesAmount = bis.read(buffer)) > 0) {
///////////////////////////////////////////////
                File newFile = new File("\\Users\\OSCAR\\Desktop\\buffer\\", fileName);
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                    out.close();
                    do {
                        ea = new EnviarArchivo("C:\\Users\\OSCAR\\Desktop\\buffer\\" + fileName);

                        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                        Object[] objTabla = new Object[4];
                        objTabla[0] = proceso;
                        objTabla[1] = fileName;
                        File file = new File("\\Users\\OSCAR\\Desktop\\buffer\\", fileName);
                        int tamaño = (int) file.length();
                        objTabla[2] = tamaño / 1024 + " KB";
                        objTabla[3] = "esperando";
                        jTable1.setVisible(false);
                        modelo.addRow(objTabla);
                        jTable1.setModel(modelo);
                        modelo.fireTableDataChanged();
                        jTable1.setVisible(true);
                        retorno = ea.enviarArchivo();
                        if (retorno == "1") {
                            jTable1.setValueAt("erroneo", contador, 3);
                            contador++;
                        }
                    } while (retorno == "1");
                    JOptionPane.showMessageDialog(null, "Archivo Enviado: " + archivo.getName());
                    jTable1.setValueAt("finalizado", contador, 3);
                    proceso++;
                    contador++;
                }
                //////////////////////////////////////////                  

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new Cliente();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
