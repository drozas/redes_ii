package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorMultiThread {

    private final static int puerto = 6666;
    private static int numPeticiones = 0;

    //Estos métodos están marcados como syncronized para evitar problemas de concurrencia.
    //De esta manera, accedemos de forma atómica.
    //El sistema que utiliza es el de un cerrojo. Al marcar el bloque de código como syncronized, 
    //si alguien hace uso tomará el cerrojo, y no lo soltará hasta que termine la ejecución de
    //dicho bloque. El resto esperarán, y cuando el otro lo suelte, la máquina virtual se lo 
    //asignará a uno de ellos.
    public static synchronized int getNumPeticiones(){
        return numPeticiones;
    }
    public static synchronized void incrNumPeticiones(){
        numPeticiones++;
    }

    public  static void main(String[] args){
        try{
        	//Creamos una instancia de server socket
            ServerSocket serverSocket = new ServerSocket(puerto);
            System.out.println("El ServerSocket se ha atado con éxito al puerto " + puerto);
            while(true){
            	//Nos bloqueamos hasta recibir una nueva conexión. En ese momento creamos un nuevo
            	//socket de datos que le pasamos a nuestro procesador de peticiones.
                Socket conn = serverSocket.accept();
                //Primero crearemos una instancia de dicho procesador, y después con start la 
                //procesaremos en un nuevo hilo
                ProcesadorHttp p = new ProcesadorHttp(conn);
                p.start();
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }
}