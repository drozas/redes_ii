package es.urjc.escet.gsyc.p2p;

import java.io.IOException;
import java.net.ServerSocket; 
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.urjc.escet.gsyc.p2p.tipos.*;

/**
 * 
 * Clase que implementa el servidor p2p.
 * 
 * drozas: Modifico a public
 * 
 * @author drozas
 *
 */
public class ServidorP2P extends Thread{
	
	private static final int NUM_THREADS_EN_POOL= 15;
	
	private Peer peer;
	
	//ThreadPool
	private ExecutorService threadPool;

	
	public ServidorP2P(Peer peer)
	{
		this.peer = peer; 
	}
	
	public void run(){

		//Declaramos server antes del bloque try{}catch{} para poder utilizarlo fuera del mismo 		
		ServerSocket server = null;
		//Instanciamos el threadPool
		threadPool = Executors.newFixedThreadPool(NUM_THREADS_EN_POOL);
		
		try{
			//Nos atamos en el puerto que nos indica nuestra clase usuario
			server = new ServerSocket(peer.getUsuario().getPuertoP2P());
			server.setSoTimeout(500);
			System.out.println("El servidor P2P del peer " + peer.getUsuario().getNick() + 
								" se ha atado al puerto " + peer.getUsuario().getPuertoP2P());
		} catch(IOException e)
		
		{
			//Esta excepción indica que ha habido algún problema al atarse al puerto especificado
			//Consideramos que esta excepción es grave, por lo que el programa no puede continuar.
			
			//TODO: Agregar puerto
			System.out.println("ERROR: No se puede crear un ServerSocket en el puerto " + peer.getUsuario().getPuertoP2P());
			System.out.println("Los detalles del error son los siguientes:");
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
		//Aceptamos conexiones y las servimos, cada una en su Thread
		while(!this.isInterrupted()){
			try{
				Socket conn = server.accept();
				GestorDePeticionesP2P gestor = new GestorDePeticionesP2P(conn, peer);
				
				//drozas: en lugar de crear el thread, se lo asignamos a uno de los threads creados en el pool
				// gestor.start();
				threadPool.execute(gestor);
				
			} catch (SocketTimeoutException e){
			} catch (IOException e){
				
				threadPool.shutdown();
				
				
				//	Esta excepción indica que algo ha ido mal en el establecimiento de la conexión
				//	Quizás otras conexiones puedan funcionar, por lo que dejamos que la aplicación continúe
				//	De todos modos, informamos de que algo no ha ido bien al usuario.
				System.out.println("AVISO: Se ha producido un error estableciendo una conexión");
				System.out.println("Los detalles del problema son los siguientes:");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		try{
			server.close();
		} catch (IOException e){
			System.out.println("AVISO: Se ha producido un error cerrando la conexión");
			System.out.println("Los detalles del problema son los siguientes:");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		//TODO: Agregar puerto y nick
		System.out.println("El servidor P2P del usuario " + peer.getUsuario().getNick() + 
				" que usa el puerto " + peer.getUsuario().getNick() + " se ha cerrado correctamente");
	}
}