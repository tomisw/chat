package tema15;
import java.io.*;
import java.net.*;
import java.util.*;
public class MiServidor {
    ServerSocket servidor;
    Socket socket;
    int numCliente;
    ArrayList clientes;
    Buffer b;
    public static void main(String args[]){
        new MiServidor();
    }
    MiServidor(){
        try{
            servidor=new ServerSocket(2525);
            System.out.println("Esperando Clientes");
            clientes=new ArrayList();
            b = new Buffer();
            new Enviador(b,this);
            while(true){
                socket=servidor.accept();
                numCliente++;
                System.out.println("Cliente conectado");
                Herramientas h=new Herramientas(socket);
                clientes.add(new Conexiones(h,socket,this,b));
                
            }
        }catch(IOException e){}
    }
    public void cerrarConexión(Conexiones c){
        clientes.remove(c);
    }
    
}
class Enviador extends Thread{
    Buffer b;
    MiServidor m;
    Enviador(Buffer b,MiServidor m){
        this.b=b;
        this.m=m;
        start();
    }
    public void run(){
        try{
            while(true){
                b.espera=1;
                for(int i=0;i<m.clientes.size();i++){
                        Conexiones c=(Conexiones)m.clientes.get(i);
                        c.h.dos.writeUTF(b.devolverString());
                }
                b.limpiarBuffer();
            }
        }catch(Exception e){}
    }
}

class Buffer{
    String s;
    MiServidor m;
    int espera;
    Buffer(){
        s="";
        espera=0;
    }
    public synchronized void meterBuffer(String x){
        notifyAll();
        if(espera==1)
            try{
                 this.wait();
            }catch(InterruptedException e){}
        s+="\n"+x;
        
    }
    public synchronized String devolverString(){
        notifyAll();
        return s;
    }
    public synchronized void limpiarBuffer(){
        s="";
        espera=0;
        notifyAll();
        try{wait();}catch(InterruptedException ie){}
    }
}

class Conexiones extends Thread{
    Herramientas h;
    Socket socket;
    MiServidor m;
    boolean bucle;
    Buffer b;
    Conexiones(Herramientas h, Socket s, MiServidor m, Buffer b){
        this.h=h;
        this.socket=s;
        this.m=m;
        this.b=b;
        bucle=true;
        start();
    }
    public void run(){
        while(bucle){
            try{
                String texto=h.dis.readUTF();
                b.meterBuffer(texto);
            }catch(IOException e){
                cerrarRecursos();
            }
        }
    }
    public void cerrarRecursos(){
        m.cerrarConexión(this);
        bucle=false;
        try{
            h.dis.close();
            h.dos.close();
            socket.close();
        }catch(IOException e){}
    }
}

class Herramientas{
    DataInputStream dis;
    DataOutputStream dos;
    Herramientas(Socket socket){
        try{
            dis=new DataInputStream(socket.getInputStream());
            dos=new DataOutputStream(socket.getOutputStream());
        }catch(IOException e){};
    }
}