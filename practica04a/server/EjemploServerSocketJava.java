package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class EjemploServerSocketJava {

	//Ponemos puerto a 0, se conectará a uno de forma aleatoria
    //private static final int puerto = 0;
    private static int numPeticiones = 0;

    public static void main(String[] args) {
        try{
            ServerSocket server = new ServerSocket(6666);
            System.out.println("El ServerSocket se ha atado con éxito al puerto " + server.getLocalPort());

            while(true){
            	//El socket se bloquea a la espera de peticiones
                Socket conn = server.accept();
                //Enchufamos el InputStreamReader del socket a un InputStreamReader, y este a un BufferedStreamReader
                BufferedReader entrada = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                while( (line = entrada.readLine()) != null){
                    System.out.println(line);
                    if(line.equals(""))
                        break;
                }

                StringBuilder page = new StringBuilder("");
                page.append("<html>");
                page.append("<head>");
                page.append("<title>PAGINA DE PRUEBA</title>");
                page.append("</head>");
                page.append("<body>");
                numPeticiones++;
                page.append("<hr><p align=\"center\"> Hola, has solicitado la pagina " + numPeticiones + " veces</p><hr>");
                page.append("<body>");
                page.append("</html>");

                //Enchufamos el outputStream a un printwriter para devolver la respuesta
                PrintWriter salida = new PrintWriter(conn.getOutputStream());
                salida.print("HTTP/1.0 200 OK\r\n");
                salida.print("Content-type: text/html\r\n");
                salida.print("Content-length: " + page.length() + "\r\n");
                salida.print("\r\n");
                salida.print(page);

                salida.close();
                entrada.close();

            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}