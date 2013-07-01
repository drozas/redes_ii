package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import msg.Peticion;
import msg.Respuesta;

public class ClienteSencillo {
    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Debe especificar el host y el puerto del servidor");
            System.exit(-1);
        }
        String hostServidor = args[0];
        int puertoServidor = Integer.parseInt(args[1]);

        try{
        	//Creamos un socket de datos, y nos conectamos con el servidor
            Socket socket = new Socket(hostServidor, puertoServidor);
            System.out.println("El cliente se ha conectado con éxito al servidor " + hostServidor + ":" + puertoServidor);
            
            //Creamos un mensaje de petición, que vamos a enviar al server como objeto serializado
            Peticion peticion_a_enviar = new Peticion("david.rozas@gmail.com", "Mi primer mensaje serializado", "Pues eso, a ver si llega. lalalalalalalallaa");
            
            //Creamos un ObjectOutputStream, al que vamos a enchufar el OutputStream del socket de datos
            ObjectOutputStream ous = new ObjectOutputStream(socket.getOutputStream());
            
            //Escribimos el objeto a enviar
            ous.writeObject(peticion_a_enviar);
            
            //Ahora vamos a esperar la respuesta del servidor. La llamada a read es bloqueante
            
            //Creamos un ObjectInputStream, al que enchufaremos el InputStream del socket de datos
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            
            //Creamos una instancia de la clase de mensaje de respuesta, donde haremos la llamada
            //bloqueante a readObject y guardaremos y posteriormente imprimiremos la respuesta
            Respuesta respuesta_recibida = (Respuesta)ois.readObject();
            
            System.out.println("Recibida respuesta del cliente: ");
            System.out.println("Código: " + respuesta_recibida.getCodigo());
            System.out.println("Descripción: " + respuesta_recibida.getDescripcion());
            
            
            //Cerramos...ver orden
            ous.close();
            ois.close();
            


        }catch(IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
        	e.printStackTrace();
        }
    }
}
