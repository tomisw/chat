package tema15;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class MiCliente {
    Socket socket;
    Herramientas h;
    VentanaNombreUsuario j;
    VentanaCliente v;
    Conversacion c;
    boolean bucle;
    MiCliente(){
        try{
            socket= new Socket(InetAddress.getLocalHost(),2525);
            j = new VentanaNombreUsuario();
            j.b.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    crearVentanaChat();
                }
            });
            h=new Herramientas(socket);
        }catch(IOException e){}
    }
    public void crearVentanaChat(){
        v = new VentanaCliente(j.tf.getText(),h);
        v.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                liberarCliente();
            }
        });
        c = new Conversacion(h,socket,v);
        j.dispose();
        j=null;
    }
    public void liberarCliente(){
        v.dispose();
        c.terminarConversacion();
        try{
            socket.close();
            h.dis.close();
            h.dos.close();
        }catch(Exception e){}
    }
    public static void main(String args[]){      
        new MiCliente();
    }
}

class VentanaNombreUsuario extends JFrame{
        JTextField tf;
        JButton b; 
        String s;
        VentanaNombreUsuario(){
            super("Selecciona tu nombre de usuario");
            setLocation(300, 300);
            setLayout(new BorderLayout());
            tf =new JTextField();
            tf.setToolTipText("Escribe tu nombre de usuario");
            b= new JButton("Aceptar");
            getRootPane().setDefaultButton(b);
            add(tf,BorderLayout.NORTH);
            add(b,BorderLayout.SOUTH);
            pack();
            setVisible(true);
            addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                dispose();
            }
        });
        }
}

class VentanaCliente extends JFrame{
    JTextArea ta1;
    JPanel p2;
    JButton b;
    JTextField ta2;
    Herramientas h;
    String s;
    JScrollPane p1;
    String conversacion;
    VentanaCliente(String s,Herramientas h){
        super(s);
        this.s=s;
        this.h=h;
        setBounds(300,300,300,300);
        ta1= new JTextArea();
        ta1.setEditable(false);
        setLayout(new BorderLayout());
        p1=new JScrollPane(ta1);
        p2=new JPanel(new FlowLayout());
        add(p1,BorderLayout.CENTER);
        add(p2,BorderLayout.SOUTH);
        ta2 = new JTextField("Escribe tu mensaje");
        b= new JButton("Enviar");
        getRootPane().setDefaultButton(b);
        p2.add(b);
        p2.add(ta2);
        ta2.setSize(10,50);
        ta2.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                ta2.setText("");
                ta2.removeMouseListener(this);
            }
        });
        setVisible(true);
        conversacion="";
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mandarChat();
                ta2.setText("");
            }
        });
        
    }
    
    public void mandarChat(){
        try{
            h.dos.writeUTF(s+": "+ta2.getText());
        }catch(IOException ioe){}
    }
    
    public void leerChat(String mensajesNuevos){
        ta1.setText(conversacion+=mensajesNuevos+"\n");
    }
}
class Conversacion extends Thread{
    Socket socket;
    Herramientas h;
    VentanaCliente v;
    boolean activo;
    Conversacion(Herramientas h, Socket s, VentanaCliente v){
        this.h=h;
        this.v=v;
        this.socket=s;
        activo=true;
        start();
    }
    public void run(){
        while(activo){
            try{
                v.leerChat(h.dis.readUTF());
            }catch(IOException e){
                
            }
        }
    }
    public void terminarConversacion(){
        activo=false;
    }
}
