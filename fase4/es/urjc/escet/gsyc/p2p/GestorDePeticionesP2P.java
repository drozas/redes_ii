package es.urjc.escet.gsyc.p2p;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import es.urjc.escet.gsyc.config.ConfigException;
import es.urjc.escet.gsyc.html.GeneradorHtml;
import es.urjc.escet.gsyc.p2p.mensajes.*;

import es.urjc.escet.gsyc.p2p.tipos.*;
import es.urjc.escet.gsyc.rmi.Registrador;
import es.urjc.escet.gsyc.rmi.ServidorCentral;

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
	
	
	private Registrador servidorCentral;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param socket
	 * @param peer
	 */
	public GestorDePeticionesP2P(Socket socket, Peer peer, Registrador servidorCentral){
		this.socket = socket;
		this.peer = peer;
		this.servidorCentral = servidorCentral;
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

		String rutaFichero = this.peer.getUsuario().getDirectorioExportado() + peticion.getNombreFichero();
		try
		{
			//Recuperamos el fichero solicitado
			FileInputStream fis = new FileInputStream(rutaFichero);
			
			//Recuperamos la información sobre el peer local que espera los datos
			if (servidorCentral.existeNick(peticion.getEmisor()))
			{
				Usuario usuario_espera_fichero= servidorCentral.getUsuario(peticion.getEmisor());
				String host_usuario_espera = usuario_espera_fichero.getHost();
				
				//Si todo fue bien, respondemos al peer por el canal de control
				RespuestaFichero respuesta = new RespuestaFichero(RespuestaP2P.OK, "OK. Va a comenzar la transmisión del fichero");
				enviaRespuesta(respuesta);
				System.out.println("Enviada respuesta de envio de fichero");
				
				//Y realizamos la transferencia por el canal de datos, de kb en kb
				try{ 
					System.out.println("Comenzamos transferencia de envio de fichero a " + peticion.getEmisor() 
							+ " en " + this.socket.getInetAddress() + ":" + peticion.getPuertoReceptor());
					//Creamos un socket hacia el usuario remoto en el puerto aleatorio
					//en el que está a la escucha ServidorDeFicherosDelPeer
					//COGEMOS EL HOST DEL LOCAL DE NUESTRO SOCKET!
					Socket socket = new Socket(this.socket.getInetAddress(), peticion.getPuertoReceptor()); 
					DataOutputStream os = new DataOutputStream(socket.getOutputStream());
					
					byte[] buffer = new byte[1048];
					int num;
					while((num = fis.read(buffer))>0)
					{
						os.write(buffer, 0, num);
					}
					
					os.close();
					fis.close();
					
				}catch(IOException e){
					System.out.println("Se ha producido un error de entrada/salida en la transferencia del fichero");
					e.printStackTrace();
					return;
				}
				
				
			}else{
				//No se pudo localizar al peer local en el server central
				RespuestaFichero respuesta = new RespuestaFichero(RespuestaP2P.ERROR_DE_COMUNICACION, 
						"El peer remoto no pudo localizarte en el servidor central.");
				enviaRespuesta(respuesta);
			}
		
		}catch(RemoteException e){
			//Excepción con el objeto remoto del servidor central
			System.out.println("Ocurrió una excepcion con el objeto remoto al ir a por info del peer que espera el fichero");
			e.printStackTrace();
			RespuestaFichero respuesta = new RespuestaFichero(RespuestaP2P.ERROR_DE_COMUNICACION, 
					"Ocurrió una excepcion con el objeto remoto del servidor central en el peer remoto.\n"
					+"Descripción:" + e.getMessage());
			enviaRespuesta(respuesta);
		}catch(IOException e){
			//Si no se pudo recuperar el fichero, devolvemos un mensaje informando del error
			RespuestaFichero respuesta = new RespuestaFichero(RespuestaP2P.ERROR_GESTION_FICHEROS, 
					"No fue posible acceder al fichero  " + peticion.getNombreFichero() + " en el peer remoto.\n" +
					"Quizá lo haya borrado o haya modificado su nombre, refresca su listado de ficheros");
			enviaRespuesta(respuesta);
			
		}
		
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
					System.out.println("Se ha recibido una solicitud de fichero " + this.peer.getUsuario().getNick());					
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