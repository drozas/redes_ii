package es.urjc.escet.gsyc.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import es.urjc.escet.gsyc.peer.Peer;


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
	
	//drozas: Agregamos el atributo lista de usuarios ¿synchronized?
	private Map<String, Peer> infoPeersConectados;
	//Añadimos una nueva tabla hash en la que almacenamos usuario y credencial.
	//Si el usuario ya estaba conectado, le daremos la misma credencial.
	private Map<String, String> infoCredenciales;

	/**
	 * 
	 * Constructor
	 * 
	 * @param portNumber Puerto en el que se atará el servidor.
	 */
	public ServidorHttpDelTerminal(int portNumber){
		this.portNumber = portNumber;
		//drozas: Instanciamos la clase ¡como HashMap!
		infoPeersConectados = new HashMap<String, Peer>();
		infoCredenciales = new HashMap<String,String>();
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
			System.out
					.println("ERROR: No se puede crear un ServerSocket en el puerto "
							+ this.portNumber);
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
				GestorDePeticionesHttp gestor = new GestorDePeticionesHttp(conn, this.infoPeersConectados, this.infoCredenciales);
				 gestor.start();
			} catch (IOException e) {
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
