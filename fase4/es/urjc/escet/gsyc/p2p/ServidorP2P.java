package es.urjc.escet.gsyc.p2p;

import java.io.IOException;
import java.net.ServerSocket; 
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.rmi.Naming;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.urjc.escet.gsyc.config.ConfiguracionGeneral;
import es.urjc.escet.gsyc.p2p.tipos.*;
import es.urjc.escet.gsyc.rmi.Registrador;

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
	
	//Necesitamos accecer al servidor central para saber el host destino de las peticiones de descarga de ficheros
	private Registrador servidorCentral;

	
	public ServidorP2P(Peer peer)
	{
		this.peer = peer;
		
		
		//Modificaciones fase 4
		//Recuperamos el objeto remoto al arranque del servidor p2p
		String localizacion = "//" + ConfiguracionGeneral.getHostRmiRegistry() + ":" +
							ConfiguracionGeneral.getPuertoRmiRegistry() + "/" +
							ConfiguracionGeneral.getPathRmiDelRegistrador();
		//Y una instancia nula del objeto remoto
		this.servidorCentral = null;
		try{
			//Recuperamos el objeto remoto 
			this.servidorCentral = (Registrador)Naming.lookup(localizacion);
		}catch(Exception e)
		{
			//Esta excepción indica que ha sido imposible recuperar el objeto remoto.
			//Es lo suficientemente grave como para abortar la ejecución
			System.out.println("Ha sido imposible conectarse al servidor central");
			System.out.println("Los detalles del error son los siguientes: ");
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);

		}
	}
	
	public void run() {

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
				GestorDePeticionesP2P gestor = new GestorDePeticionesP2P(conn, peer, this.servidorCentral);
				
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
		
		System.out.println("El servidor P2P del usuario " + peer.getUsuario().getNick() + 
				" que usa el puerto " + peer.getUsuario().getNick() + " se ha cerrado correctamente");
	}
}