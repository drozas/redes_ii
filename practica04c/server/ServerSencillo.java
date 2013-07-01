package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import msg.Peticion;
import msg.Respuesta;

public class ServerSencillo {

    public static void main(String[] args) {
        try{
            ServerSocket server = new ServerSocket(6666);
            System.out.println("El ServerSocket se ha atado con éxito al puerto " + server.getLocalPort());

            while(true){
            	//El socket se bloquea a la espera de peticiones
                Socket conn = server.accept();
             
                //Enchufamos el InputStream a un ObjectInputStream
                ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
                
                //Creamos una instancia para guardar el mensaje recibido, con los datos del ois
                Peticion peticion_recibida = (Peticion)ois.readObject();
                
                //Mostramos su contenido por pantalla
                System.out.println("Emisor: " + peticion_recibida.getEmisor());
                System.out.println("Asunto: " + peticion_recibida.getAsunto());
                System.out.println("Cuerpo: " + peticion_recibida.getCuerpo());
                
                //Ahora vamos a preparar una respuesta
                Respuesta respuesta = new Respuesta(0, "Todo ok, " + peticion_recibida.getEmisor() + " agur!");
                
                //Creamos un ObjectOutputStream, y le enchufamos el OutputStream de nuestro socket de datos
                ObjectOutputStream ous = new ObjectOutputStream(conn.getOutputStream());
                ous.writeObject(respuesta);
                
                //Cerramos la entrada y la salida: ver orden!
                ois.close();
                ous.close();

            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e)
        {
        	e.printStackTrace();
        }
    }
}
