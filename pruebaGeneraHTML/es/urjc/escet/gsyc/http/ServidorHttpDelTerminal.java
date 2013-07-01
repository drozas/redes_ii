package es.urjc.escet.gsyc.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//import es.urjc.escet.gsyc.terminal.GestorPeticionesHTTP;

public class ServidorHttpDelTerminal extends Thread {

	private int portNumber;

	public ServidorHttpDelTerminal(int portNumber){
		this.portNumber = portNumber;
	}
	
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
				GestorDePeticionesHttp gestor = new GestorDePeticionesHttp(conn);
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
