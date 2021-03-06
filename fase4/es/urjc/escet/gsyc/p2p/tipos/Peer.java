package es.urjc.escet.gsyc.p2p.tipos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import es.urjc.escet.gsyc.html.internal.Html;
import es.urjc.escet.gsyc.html.internal.HtmlP;
import es.urjc.escet.gsyc.html.internal.HtmlTable;
import es.urjc.escet.gsyc.html.internal.HtmlTableTd;
import es.urjc.escet.gsyc.html.internal.HtmlTableTr;
import es.urjc.escet.gsyc.p2p.ServidorDeFicherosDelPeer;
import es.urjc.escet.gsyc.p2p.ServidorP2P;
import es.urjc.escet.gsyc.p2p.mensajes.*;


/**
 * 
 * Clase Peer. 
 * 
 * Idea: Clase con una clase Usuario y una ServidorP2P.
 * 
 * Cuando un usuario se loguea correctamente:
 * 	- Se crea un Peer
 * 		- En dicho Peer se almacena info de usuario
 * 		- En dicho Peer se crea un serverP2P
 *  - En el server del terminal, se guarda la info de Peers conectados actualmente
 *  
 *  - El usuario accede a través del proceso servidor del terminal:
 *  	- Operaciones locales, que el propio proceso resuelve.
 *  	- Operaciones remotas, que el terminal solicita a los procesos servidores de otros peers.
 *  
 * 
 */
public class Peer {
	
	private Usuario usuario;
	private ServidorP2P servidor;
	
	//En esta tabla hash guardaremos los mensajes recibidos por orden de recepción
	private ConcurrentHashMap<Long, Mensaje> mensajesRecibidos;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param usuario
	 * @param servidor
	 */
	public Peer (Usuario usuario, ServidorP2P servidor)
	{
		this.usuario = usuario;
		this.servidor = servidor;
		this.mensajesRecibidos = new ConcurrentHashMap<Long, Mensaje>();
	}
	
	/**
	 * 
	 * Constructor en el que creamos el server pasándole el peer
	 * 
	 * @param usuario
	 */
	public Peer (Usuario usuario)
	{
		this.usuario = usuario;
		this.servidor = new ServidorP2P(this);
		this.mensajesRecibidos = new ConcurrentHashMap<Long, Mensaje>();
		
	}
	
	/**
	 * 
	 * Devuelve el usuario
	 * 
	 * @return	usuario
	 */
	public Usuario getUsuario()
	{
		return this.usuario;
	}
	
	/**
	 * 
	 * Devuelve el servidor p2p
	 * 
	 * @return	servidor p2p
	 */
	public ServidorP2P getServidorP2P()
	{
		return this.servidor;
	}
	
	/**
	 * 
	 * Devuelve los mensajes recibidos por este peer
	 * 
	 * @return	Tabla hash con mensajes recibidos, cuya key es el momento de recepción.
	 */
	public ConcurrentHashMap<Long, Mensaje> getMensajesRecibidos()
	{
		return this.mensajesRecibidos;
	}
	
	
	/**
	 * 
	 * Método sobrecargado, que realiza la conexión con el peer remoto para el envío de 
	 * una solicitud de listado de directorio remoto.
	 * 
	 * 	- Crea un socket con el peer remoto
	 * 	- Devuelve la respuesta de dicho peer, o una respuesta de error si se produjo 
	 * 
	 * @param usuarioRemoto
	 * @param peticion
	 * 
	 * @return
	 * 
	 * @throws PeerException	La eleva si alguno de los parámetros es nulo, o hubo un error
	 * 							creando la conexión
	 */
	public RespuestaListaFicheros p2pEnviaMensaje(Usuario usuarioRemoto, PeticionListaFicheros peticion) throws PeerException 
	{
		if(usuarioRemoto == null || peticion == null)
		{
			PeerException pe = new PeerException();
			pe.setMensajeDeError("Se ha especificado un usuario o un mensaje nulos");
			
			throw pe;

		}else{
	
			//Lanzamos la peticion
			ObjectOutputStream out = null;
			ObjectInputStream in = null;
	
			try{
				//Creamos un socket hacia el usuario remoto
				Socket socket = new Socket(usuarioRemoto.getHost(), usuarioRemoto.getPuertoP2P()); 
				out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				
				//Enviamos la petición por el socket 
				out.writeObject(peticion);
				out.flush();
				
				//Esperamos la respuesta
				in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				RespuestaListaFicheros respuesta = (RespuestaListaFicheros) in.readObject();
				
				//Cerramos socket
				out.close();
				in.close();
				
				return respuesta;
				
			} catch (Exception e){
				PeerException pe = new PeerException();
				pe.setMensajeDeError(e.getMessage());
				pe.initCause(e);
				throw pe;

			}
		}		

	}
	
	
	/**
	 * 
	 * Método sobrecargado, que realiza la conexión con el peer remoto para el envío de una entrega de mensaje
	 * 	- Crea un socket con el peer remoto
	 * 	- Devuelve la respuesta de dicho peer, o una respuesta de error si se produjo 
	 * 
	 * @param usuarioRemoto
	 * @param peticion
	 * 
	 * @return
	 * 
	 * @throws PeerException	La eleva si alguno de los parámetros es nulo, o hubo un error
	 * 							creando la conexión
	 */
	public RespuestaEntregaMensaje p2pEnviaMensaje(Usuario usuarioRemoto, PeticionEntregaMensaje peticion) throws PeerException 
	{
		if(usuarioRemoto == null || peticion == null)
		{
			PeerException pe = new PeerException();
			pe.setMensajeDeError("Se ha especificado un usuario o un mensaje nulos");
			
			throw pe;
	
		}else{
	
			//Lanzamos la peticion
			ObjectOutputStream out = null;
			ObjectInputStream in = null;
	
			try{
				//Creamos un socket hacia el usuario remoto
				Socket socket = new Socket(usuarioRemoto.getHost(), usuarioRemoto.getPuertoP2P()); 
				out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				
				//Enviamos la petición por el socket 
				out.writeObject(peticion);
				out.flush();
				
				//Esperamos la respuesta
				in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				RespuestaEntregaMensaje respuesta = (RespuestaEntregaMensaje) in.readObject();
				
				//Cerramos socket
				out.close();
				in.close();
				
				return respuesta;
				
			} catch (Exception e){
				PeerException pe = new PeerException();
				pe.setMensajeDeError(e.getMessage());
				pe.initCause(e);
				throw pe;

			}
		}		

	}
	
	
	
	/**
	 * 
	 * Método sobrecargado, que realiza la conexión con el peer remoto para el envío de una solicitud de fichero
	 * 	- Crea un socket con el peer remoto
	 * 	- Devuelve la respuesta de dicho peer, o una respuesta de error si se produjo 
	 * 
	 * @param usuarioRemoto
	 * @param peticion
	 * 
	 * @return
	 * 
	 * @throws PeerException	La eleva si alguno de los parámetros es nulo, o hubo un error
	 * 							creando la conexión
	 */
	public RespuestaFichero p2pEnviaMensaje(Usuario usuarioRemoto, PeticionFichero peticion) throws PeerException 
	{
		if(usuarioRemoto == null || peticion == null)
		{
			PeerException pe = new PeerException();
			pe.setMensajeDeError("Se ha especificado un usuario o un mensaje nulos");
			
			throw pe;
	
		}else{
	
			//Lanzamos la peticion
			ObjectOutputStream out = null;
			ObjectInputStream in = null;
	
			try{
				//Creamos un socket hacia el usuario remoto
				Socket socket = new Socket(usuarioRemoto.getHost(), usuarioRemoto.getPuertoP2P()); 
				out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				
				//Enviamos la petición por el socket 
				out.writeObject(peticion);
				out.flush();
				
				//Esperamos la respuesta
				in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				RespuestaFichero respuesta = (RespuestaFichero) in.readObject();
				
				//Cerramos socket
				out.close();
				in.close();
				
				return respuesta;
				
			} catch (Exception e){
				PeerException pe = new PeerException();
				pe.setMensajeDeError(e.getMessage());
				pe.initCause(e);
				throw pe;

			}
		}		

	}

	/**
	 * 
	 * Devuelve el directorio compartido localmente
	 * 
	 * @return
	 */
	public ListaFicheros obtenerListadoCompartido() throws ListaFicherosException
	{
		ListaFicheros listaFicheros = new ListaFicheros();
		listaFicheros.CargarListaFicherosDirectorio(this.getUsuario().getDirectorioExportado());
		return listaFicheros;
	}	
	

	/**
	 * 
	 * @param usuarioRemoto
	 * @return
	 */
	public ListaFicheros obtenerListadoRemoto(Usuario usuarioRemoto) throws PeerException
	{
		//Construimos la petición
		PeticionListaFicheros peticion = new PeticionListaFicheros(this.getUsuario().getNick());
		
		//Enviamos el mensaje
		try
		{
			RespuestaListaFicheros respuesta = p2pEnviaMensaje(usuarioRemoto, peticion);
		
			if (respuesta.getCodigoError()<0)
			{	
				//Si el mensaje es de error, elevamos una excepción con la descripción
				PeerException pe = new PeerException();
				pe.setMensajeDeError(respuesta.getDescription());
				
				throw pe;
				
			}else{
				//Si todo fue correcto, devolvemos la lista de ficheros
				return respuesta.getListaFicheros();
			}
		}catch (PeerException ex)
		{
			//Si se produjo una excepción al enviar el mesnaje, la elevamos
			throw ex;
		}
	}
	
	/**
	 * 
	 * Método que permite el envío de mensajes a otros peers
	 * 
	 * @param usuarioRemoto	Usuario al que vamos a enviar el mensaje
	 * @param asunto	Asunto del mensaje
	 * @param contenido	Contenido del mensaje
	 * 
	 * @return	Cadena con el resultado del envío
	 */
	public String entregaMensaje(Usuario usuarioRemoto, String asunto, String contenido) throws PeerException
	{
		//Construimos el mensaje a enviar
		Mensaje mensaje = new Mensaje(this.getUsuario().getNick(), usuarioRemoto.getNick(),
										asunto, contenido);
		
		//Construimos la petición de entrega de mensaje.
		PeticionEntregaMensaje peticion = new PeticionEntregaMensaje(this.getUsuario().getNick(), mensaje);
		
		//Enviamos el mensaje
		try
		{
			RespuestaEntregaMensaje respuesta = p2pEnviaMensaje(usuarioRemoto, peticion);
		

			if (respuesta.getCodigoError()<0)
			{	
				//Si el mensaje es de error, elevamos una excepción con la descripción
				PeerException pe = new PeerException();
				pe.setMensajeDeError(respuesta.getDescription());
				
				throw pe;
			}else{
				//Si todo fue correcto, devolvemos la descripción de la respuesta
				return respuesta.getDescription();
			}
		}catch (PeerException ex)
		{
			//Si se produjo una excepción al enviar el mesnaje, la elevamos
			throw ex;
		}
	}
	
	
	/**
	 * 
	 * @param usuarioRemoto
	 * @return
	 */
	public String obtenerFicheroRemoto(Usuario usuarioRemoto, String ficheroRemoto) throws PeerException
	{
		
		File fichero = new File(this.getUsuario().getDirectorioExportado() + ficheroRemoto);
		
		if (!fichero.exists())
		{	
			//Creamos un ServerSocket en el que vamos a recibir el fichero en un puerto aleatorio
			try
			{
				ServerSocket serverSocketRecepcionFichero = new ServerSocket(0);
				System.out.println("El usuario " + this.getUsuario().getNick() + " ha abierto un SeverSocket en puerto " + serverSocketRecepcionFichero.getLocalPort() + " para descargar el fichero " + ficheroRemoto);
			
				//Creamos una instancia de la clase servidor de ficheros del peer, y arrancamos su hilo de ejecución.
				ServidorDeFicherosDelPeer servidorFicherosPeer = new ServidorDeFicherosDelPeer(this,serverSocketRecepcionFichero,ficheroRemoto);
				servidorFicherosPeer.start();
				
				//Construimos la petición
				PeticionFichero peticion = new PeticionFichero(this.getUsuario().getNick(), ficheroRemoto, serverSocketRecepcionFichero.getLocalPort());
				
				//Enviamos la petición, y nos bloqueamos esperando respuesta
				RespuestaFichero respuesta = p2pEnviaMensaje(usuarioRemoto, peticion);
			
				if (respuesta.getCodigoError()<0)
				{	
					//Si el mensaje es de error, elevamos una excepción con la descripción
					PeerException pe = new PeerException();
					pe.setMensajeDeError(respuesta.getDescription());
					
					throw pe;
					
				}else{
					//Si todo fue correcto, devolvemos la descripción de la respuesta
					return respuesta.getDescription();
				}
				
			}catch (PeerException ex){
				//Si se produjo una excepción al enviar el mesnaje, la elevamos
				throw ex;
			}catch (IOException ex){
				//Si se produce una excepcion con el server socket, la detallamos y la elevamos
				//como una PeerExcepcion que será recogida en el gestor de peticiones.
				PeerException pe = new PeerException();
				pe.setMensajeDeError("Error al crear el Server Socket en el que esperabamos recibir el fichero " 
						+ "\n"+ "Descripción: " + ex.getMessage());
				pe.initCause(ex);
				throw pe;
	
			
			}
		}else{
			//Si se el fichero ya existía, elevamos una peer exception informando del error
			PeerException pe = new PeerException();
			pe.setMensajeDeError("El fichero que quieres descargar ya existe en tu carpeta compartida (" 
					+ this.getUsuario().getDirectorioExportado() + ")\n" +
					"Si quieres descargarlo, tendrás que borrarlo previamente");
			throw pe;
		}
	}
	
	
	/**
	 * 
	 * Guarda un mensaje en la lista de mensajes recibidos.
	 * 
	 * @param mensaje
	 */
	public void guardarMensaje(Mensaje mensaje)
	{
		this.mensajesRecibidos.put(System.currentTimeMillis(), mensaje);
	}
	
	
	/**
	 * 
	 * Devuelve la lista de mensajes recibidos en un String
	 * 
	 * @return	String con lista de mensajes recibidos
	 */
	public String listaMensajesToString()
	{
		if (this.mensajesRecibidos.size()>0)
		{
			Enumeration<Mensaje> iterador = this.mensajesRecibidos.elements();
			String listaMensajesString = new String();
		
			while (iterador.hasMoreElements())		
				listaMensajesString += iterador.nextElement().toString();
			
			return listaMensajesString;
		}else{
			return "No hay mensajes";
		}
	}

	/**
	 * 
	 * Escribe en la página html proporcionada el listado de mensajes recibidos por 
	 * ese peer utilizando clases html.
	 * 
	 * @param page	Página en la que queremos añadir la lista
	 */
	public void addListaMensajes(Html page)
	{
		
		if (this.mensajesRecibidos.size()>0)
		{
			//Crearemos una tabla con tantas filas como mensajes, más una de títulos
			//Creamos una tabla de 3x3
			HtmlTable table = page.addTable();

			
			HtmlTableTr[] tr = new HtmlTableTr[this.mensajesRecibidos.size() + 1]; 
			HtmlTableTd[][] td = new HtmlTableTd[this.mensajesRecibidos.size() + 1][4];
			
			//Creamos los trs
			for(int i = 0; i < this.mensajesRecibidos.size() + 1; i++)
				tr[i] = table.addTr();
			
			//Creamos los tds con colores
			for(int i = 0; i < this.mensajesRecibidos.size() + 1; i++)
				for(int j = 0; j < 4; j++)
				{
					td[i][j] = tr[i].addTd();
					
					if (i==0)
						td[i][j].setBgColor("4682B4");
					else
						td[i][j].setBgColor("ADD8E6");
				}
			
			//Formato de la tabla
			table.setBorder(1);
			table.setBorderColor("00008B");

			//Cabecera de títulos
			td[0][0].addText("Emisor");
			td[0][0].setWidth(10);
			
			td[0][1].addText("Timestamp");
			td[0][1].setWidth(10);
			
			td[0][2].addText("Asunto");
			td[0][2].setWidth(20);
			
			td[0][3].addText("Mensaje");
			td[0][3].setWidth(60);
			
			//Escribimos tabla de mensajes
			Enumeration<Mensaje> iterador = this.mensajesRecibidos.elements();
			int i = 1;
			while (iterador.hasMoreElements())	
			{
				Mensaje mensaje = iterador.nextElement();
				td[i][0].addText(mensaje.getEmisor());
				td[i][1].addText(mensaje.getInstanteEnvio() + " ");
				td[i][2].addText(mensaje.getAsunto());
				td[i][3].addText(mensaje.getMensaje());
				i++;
			}
				

		}else{
			HtmlP p = page.addP();
			p.addText("No se han recibido mensajes");
			p.setAlign(HtmlP.AlignType.CENTER);
		}
	}

}
