package tema15;

import java.io.*;
import java.net.*;

public class EjemploURL1 {

        //Cadenas de texto para crear los URLs
        String textosURL[] = {"file:///c:\\A.java",
                "http://www.terra.es:80/actualidad/nacional/portada.htm",
                "mailto:trilcejf@hotmail.com",
                "ftp://ftp.microsoft.com"};

        int numURL;

        public EjemploURL1() {
                numURL = textosURL.length;
                URL misURL[] = new URL[numURL];
                try {
                        for (int i = 0; i < numURL; i++) {
                                misURL[i] = new URL(textosURL[i]);
                        }
                } catch (MalformedURLException mue) {
                        System.out.println("Error----" + mue.toString());
                }
                obtenerPropiedades(misURL);
                obtenerIP(misURL);
        }

        private void obtenerPropiedades(URL misURL[]) {
                for (int i = 0; i < numURL; i++) {
                        System.out.print("Protocolo" + i + ": " + misURL[i].getProtocol() + "   ");
                        System.out.print("Puerto" + i + ": " + misURL[i].getPort() + "   ");
                        System.out.print("Recurso buscado" + i + ": " + misURL[i].getFile() + "   ");
                        System.out.print("Nombre de dominio del servidor" + i + ": " + misURL[i].getHost());
                        System.out.println();
                }
                System.out.println();
        }

        private void obtenerIP(URL misURL[]) {
                for (int i = 0; i < numURL; i++) {
                        if (i == 1 || i == 3) {
                                try {
                                        InetAddress ia = InetAddress.getByName(misURL[i].getHost());
                                        System.out.println("Direccion ip del servidor " + misURL[i].getHost() + ": " + ia.getHostAddress());
                                } catch (UnknownHostException uhe) {
                                        System.out.println("Host incorrecto :" + uhe.toString());
                                }
                        }
                }
        }

        public static void main(String args[]) {
                new EjemploURL1();
        }
}
