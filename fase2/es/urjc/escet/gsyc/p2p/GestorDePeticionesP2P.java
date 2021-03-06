package es.urjc.escet.gsyc.p2p;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import es.urjc.escet.gsyc.p2p.mensajes.*;

import es.urjc.escet.gsyc.p2p.tipos.*;

/**
 * 
 * Clase encargada de la generación de respuestas a operaciones remotas, a través de
 * mecanismos de serialización de java.
 * 
 * @author drozas
 *
 */
public class GestorDePeticionesP2P extends Thread {
	
	private Socket socket;
	private Peer peer;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param socket
	 * @param peer
	 */
	public GestorDePeticionesP2P(Socket socket, Peer peer){
		this.socket = socket;
		this.peer = peer;
	}
	
	/**
	 * 
	 * Envía la respuesta por el socket. El tipo de respuesta es de la clase padre,
	 * de esta forma este método nos vale para enviar cualquier tipo de respuesta.
	 * 
	 * @param respuesta
	 * @throws IOException
	 */
	private void enviaRespuesta(RespuestaP2P respuesta) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		out.writeObject(respuesta);
		out.flush();
		out.close();
	}
	
	/**
	 * 
	 * Procesa la petición de recuperación de un fichero
	 * 
	 * @param peticion
	 * @throws IOException
	 */
	private void procesaPeticionFichero(PeticionFichero peticion) throws IOException {
//		1- Recupera el fichero solicitado
//		2- Chequea que el fichero existe
//		3- Si el fichero no existe, manda un mensaje de respuesta
//		con error
//		4- Si el fichero existe,
//		1- Envía un mensaje de respuesta sin error
//		2- abre el socket al puerto especificado y lo envía
//		ESTE MÉTODO SE ANALIZARÁ EN LA FASE IV,
//		POR AHORA LO DEJAREMOS VACÍO
	}
	
	/**
	 * 
	 * Procesa la petición de listar el directorio de ficheros compartidos
	 * 
	 * @param peticion
	 * @throws IOException
	 */
	private void procesaPeticionListaFicheros (PeticionListaFicheros peticion) throws IOException {

		
		//Recuperamos nuestra lista de ficheros
		ListaFicheros listaFicheros = new ListaFicheros();
		try
		{
			listaFicheros.CargarListaFicherosDirectorio(this.peer.getUsuario().getDirectorioExportado());
		
			//Construimos el mensaje de respuesta
			RespuestaListaFicheros respuesta =	new RespuestaListaFicheros(RespuestaP2P.OK, "OK", listaFicheros);
		
			//Lo enviamos con un método que permite enviar cualquier tipo de respuesta que herede de RespuestaP2P
			enviaRespuesta(respuesta);
		}catch(ListaFicherosException e){
			
			//Si hubo una excepción con la lista de ficheros, creamos un mensaje de error
			RespuestaListaFicheros respuesta =	new RespuestaListaFicheros(RespuestaP2P.ERROR_GESTION_FICHEROS, "No se pueden listar los ficheros. " +
					"El usuario remoto no comparte un directorio, o no tiene permisos", listaFicheros);
			enviaRespuesta(respuesta);
		}
	}
	
	/**
	 * 
	 * Procesa la petición de una entrega de mensaje.
	 * 
	 * @param peticion
	 * @throws IOException
	 */
	private void procesaPeticionEntregaMensaje(PeticionEntregaMensaje peticion) throws IOException {
		
		//Almacenamos el mensaje recibido
		this.peer.guardarMensaje(peticion.getMensaje());
		System.out.println("Se ha recibido el siguiente mensaje en " + this.peer.getUsuario().getNick());
		System.out.println(peticion.getMensaje().toString());
		
		
		RespuestaEntregaMensaje respuesta = new	RespuestaEntregaMensaje(RespuestaP2P.OK, "OK");
		enviaRespuesta(respuesta);
	}
	
	/**
	 * 
	 * Ejecución del thread
	 * 
	 */
	public void run(){
		try{
			ObjectInputStream in = new ObjectInputStream(new
					BufferedInputStream(this.socket.getInputStream()));
			Object msg = null;
			try{
				msg = in.readObject();
				if(msg.getClass() == PeticionFichero.class)
				{
					procesaPeticionFichero((PeticionFichero)msg);
				}else if (msg.getClass()== PeticionListaFicheros.class){
					System.out.println("Se ha recibido una solicitud de listado de ficheros en " + this.peer.getUsuario().getNick());
					procesaPeticionListaFicheros((PeticionListaFicheros)msg);
				}else if (msg.getClass()== PeticionEntregaMensaje.class){
					System.out.println("Se ha recibido una solicitud de entrega de mensaje en " + this.peer.getUsuario().getNick());					
					procesaPeticionEntregaMensaje((PeticionEntregaMensaje)msg);
				} else {
					//Error de protocolo
					RespuestaP2P response = new	RespuestaP2P(-1,"Mensaje de tipo desconocido");
					ObjectOutputStream out = new ObjectOutputStream(new	BufferedOutputStream(
									this.socket.getOutputStream()));
					out.writeObject(response);
					out.flush();
					out.close();
				}
			} catch (ClassNotFoundException e) {
				//Se ha producido un error recibiendo el mensaje
				System.out.print("Error recibiendo un mensaje");
				System.out.println("Detalles del problema:");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			in.close();
		} catch (IOException e) {
			System.out.println("AVISO: Error de I/O");
			System.out.println("Detalles del problema:");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
	}
}