package es.urjc.escet.gsyc.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.util.concurrent.ConcurrentHashMap;

import es.urjc.escet.gsyc.config.ConfiguracionGeneral;
import es.urjc.escet.gsyc.p2p.tipos.Peer;
import es.urjc.escet.gsyc.rmi.Registrador;

//Importaciones para threadPool
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 
 * Clase servidor del terminal. Es la clase que prepara el socket y crea un thread
 * para cada petición.
 * 
 * @author drozas
 *
 */
public class ServidorHttpDelTerminal extends Thread {

	private int portNumber;
	private static final int NUM_THREADS_EN_POOL= 15;
	
	//TODO: syncronized y cambio a HashTable
	//drozas: Agregamos el atributo lista de usuarios ¿synchronized?
	//private synchronized Map<String, Peer> infoPeersConectados;
	private ConcurrentHashMap<String, Peer> infoPeersConectados;
	//Añadimos una nueva tabla hash en la que almacenamos usuario y credencial.
	//Si el usuario ya estaba conectado, le daremos la misma credencial.
	private ConcurrentHashMap<String, String> infoCredenciales;
	
	private Registrador servidorCentral;
	
	//ThreadPool
	private ExecutorService threadPool;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param portNumber Puerto en el que se atará el servidor.
	 */
	public ServidorHttpDelTerminal(int portNumber){
		this.portNumber = portNumber;
		//drozas: Instanciamos la clase ¡como HashMap!
		infoPeersConectados = new ConcurrentHashMap<String, Peer>();
		infoCredenciales = new ConcurrentHashMap<String,String>();
		
		//Modificaciones fase 3
		//Recuperamos el objeto remoto al arranque dels servidor, y compartimos
		//su uso por todos los usuarios de ese terminal		//Preparamos el localizador
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
	
	/**
	 * 
	 * Método en el que se atienden las peticiones, cada una en su thread
	 * 
	 */
	public void run() {

		// Declaramos server sobre del bloque try{}catch{} para poder utilizarlo
		// fuera del mismo
		ServerSocket server = null;
		
		//Instanciamos el threadPool
		threadPool = Executors.newFixedThreadPool(NUM_THREADS_EN_POOL);
		
		try {
			server = new ServerSocket(this.portNumber);
			System.out
					.println("El servidor HTTP del Terminal se ha atado al puerto "
							+ this.portNumber);
		} catch (IOException e) {
			// Esta excepción indica que, seguramente, ha habido algún problema
			// al atarse al puerto especificado
			// Consideramos que esta excepción es grave, por lo que el programa
			// no puede continuar.
			System.out.println("ERROR: No se puede crear un ServerSocket en el puerto "	+ this.portNumber);
			System.out.println("Los detalles del error son los siguientes:");
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}

		// Aceptamos conexiones y las servimos, cada una en su Thread
		while (true) {
			try {
				Socket conn = server.accept();
				//drozas: Le pasamos también la tabla de usuarios
				GestorDePeticionesHttp gestor = new GestorDePeticionesHttp(conn, this.infoPeersConectados, this.infoCredenciales,
																			this.servidorCentral);

				threadPool.execute(gestor);
			} catch (IOException e) {
				
				threadPool.shutdown();
				
				// Esta excepción indica que algo ha ido mal en el
				// establecimiento de la conexión
				// Quizás otras conexiones puedan funcionar, por lo que dejamos
				// que la aplicación continúe
				// De todos modos, informamos de que algo no ha ido bien al
				// usuario.
				System.out.println("AVISO: Se ha producido un error estableciendo una conexión");
				System.out.println("Los detalles del problema son los siguientes:");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}// try
		}// while
	}// run
}// class
