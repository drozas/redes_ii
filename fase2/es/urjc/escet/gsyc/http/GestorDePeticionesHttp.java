package es.urjc.escet.gsyc.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import es.urjc.escet.gsyc.aux.Md5;
import es.urjc.escet.gsyc.html.GeneradorHtml;
import es.urjc.escet.gsyc.p2p.tipos.ListaFicheros;
import es.urjc.escet.gsyc.p2p.tipos.ListaFicherosException;
import es.urjc.escet.gsyc.p2p.tipos.PeerException;
import es.urjc.escet.gsyc.p2p.tipos.Usuario;
import es.urjc.escet.gsyc.p2p.tipos.Peer;

public class GestorDePeticionesHttp extends Thread {
	private Socket socket;
	
	//drozas: ¿syncronhized?
	private ConcurrentHashMap<String, Peer> infoPeersConectados;
	
	//Añadimos una nueva tabla hash en la que almacenamos usuario y credencial.
	//Si el usuario ya estaba conectado, le daremos la misma credencial.
	private ConcurrentHashMap<String, String> infoCredenciales;
	
	/**
	 * 
	 * Constructor del gestor de peticiones
	 * 
	 * @param socket 	Socket
	 * @param infoPeersConectados	Tabla hash, con correspondencia credencial-usuario
	 * @param infoCredenciales	Tabla hash, con correspondencia usuario-credencial
	 */
	public GestorDePeticionesHttp(Socket socket, ConcurrentHashMap<String, Peer> infoPeersConectados, 
									ConcurrentHashMap<String, String> infoCredenciales){
		this.socket = socket;
		//La instancia está hecha en la clase ServidorTerminal. Aquí "le pasamos el puntero"
		this.infoPeersConectados = infoPeersConectados;
		this.infoCredenciales = infoCredenciales;
	}
	
	
	/**
	 * 
	 * Genera una página html informando de que se ha solicitado un recurso desconocido.
	 * 
	 * @return	String con la página informativa.
	 */
	private StringBuilder procesaPeticionPaginaDesconocida(){
		StringBuilder page = new StringBuilder("");
		page.append("<html><body>El recurso solicitado no se encuentra en el servidor </body></html>");
		return page;
	}
	
	
	/**
	 * 
	 * Procesa la petición de la página raíz, generando y/o checkeando la credencial si 
	 * el proceso de validación de usuario fue correcto.
	 * Si la validación no fue correcta, nos presenta la misma página del menú, pero
	 * informando del error sucedido.
	 * 
	 * 
	 * @param vars	Variables procedentes de la URI.
	 * 
	 * @return	Página HTML de respuesta.
	 */
	private StringBuilder procesaPeticionPaginaRaiz(Map<String,String> vars){
		
		//Se trata de una tabla hash, cuyas claves son los nombres de variables y valores los valores de las variables.
		//Comprobamos que haya algo en vars, y que existan clave y nick
		if (vars != null && vars.containsKey(Constantes.NICK_VAR) && vars.containsKey(Constantes.CLAVE_VAR)) 
		{
			String nick = vars.get(Constantes.NICK_VAR);
			String clave = vars.get(Constantes.CLAVE_VAR);
			
			//drozas: Modifico para comparar con clave encriptada.
			try
			{
				String clave_encriptada = Md5.Encriptar(clave);

				//System.out.println("Se intentan conectar con los datos " + nick  + " " + clave_encriptada);

				Usuario nuevoUsuario = new Usuario();
				switch (Validacion.ValidarUsuario(nick, clave_encriptada, nuevoUsuario, this.socket.getInetAddress().getHostName(), this.infoCredenciales)) {
				case 0:
					//Generamos la credencial, y guardamos en lista de usuarios a dicho usuario con su credencial
					//System.out.println("Usuario: " + infoPeer.getClave() + infoPeer.getNick() + infoPeer.getCorreoElectronico());
					String credencial = Validacion.ObtenerCredencial();
					
					//Creamos un nuevo peer, que CREARÁ UN NUEVO SERVER P2P
					Peer nuevoPeer = new Peer(nuevoUsuario);
					this.infoPeersConectados.put(credencial, nuevoPeer);
					
					//Y guardamos también referencia nick-credencial
					this.infoCredenciales.put(nick, credencial);
					
					//Generamos la página de transición, que tiene ya la variable credencial en la uri
					
					//ARRANCAMOS EL PROCESO SERVER P2P
					nuevoPeer.getServidorP2P().start();
					return GeneradorHtml.generaConfirmacionValidacion(credencial);
				case 1:
					//System.out.println("Se ha intentado conectar un usuario que ya estaba conectado");
					//Si se intentan conectar con el mismo nick, no hay que crear server.
					return GeneradorHtml.generaConfirmacionValidacion(this.infoCredenciales.get(nick));
				case -1:
					//Error en la contraseña
					return GeneradorHtml.generaRaiz(-1);
				case -2:
					//Error en el fichero de configuración del usuario
					return GeneradorHtml.generaRaiz(-2);
				default:
					return GeneradorHtml.generaRaiz(0);
				}
			
			}catch(NoSuchAlgorithmException e){
				return GeneradorHtml.generaRaiz(0);
			}
		}else{
			//Error por falta de parámetros, devolvemos a la raíz
			return GeneradorHtml.generaRaiz(0);
		}
	}
	
	/**
	 * 
	 * Procesa la petición de la página del menú.
	 * Si la credencial es correcta, mostramos el menú. En caso contrario, mostramos una 
	 * página indicando que es necesario conectarse (tener una credencial válida), para
	 * acceder a dicho recurso.
	 * 
	 * @param vars	Variables procedentes de la URI.
	 * 
	 * @return	Página HTML de respuesta.
	 * 
	 * Idea: Comprobamos la credencial del usuario. Si la credencial es correcta, en la 
	 * tabla hash de usuarios ya tendremos el objeto usuario correspondiente listo para
	 * mostrar la información. Si no, mostramos error diciendo que credencial es incorrecta.
	 * 
	 * - Para la validación de credencial. 
	 * 	Se trata de comparar el valor de la variable que nos pasan por get y buscar que existe
	 *  en la tabla hash.
	 *  
	 *  - Para generar la página. 
	 *  	- Página de recurso no disponible por falta de credencial: Un método generaNoAcceso estándar
	 *  	- Si ok, modificar función estática generaMenu. Generar desconexion (borrado de usuario de lista)
	 *  		enlace a dir_local y mostrar info Usuario
	 */
	private StringBuilder procesaPeticionPaginaMenu(Map<String,String> vars){
		
		//Lo primero es comprobar que el usuario tiene privilegios (credencial en nuestra lista)
		//Comprobamos que haya algo en vars, y que exista la variable credencial
		if (vars != null && vars.containsKey(Constantes.CREDENCIAL_VAR)) 
		{
			String credencial = vars.get(Constantes.CREDENCIAL_VAR);
			//System.out.println("Se intentan conectar al menu con credencial " + credencial);

			
			if (Validacion.ValidarCredencial(credencial, this.infoPeersConectados))
			{
				//Si la credencial es correcta, devolvemos la página del menú, en la que mostraremos info de usuario.
				return GeneradorHtml.generaMenu(infoPeersConectados.get(credencial).getUsuario(),credencial);
			}else{
				//Error por credencial inválida
				return GeneradorHtml.generaErrorCredencial();
			}

		}else{
			///Si no pasan datos nick y clave, presentamos página de error de no conexión.
			return GeneradorHtml.generaErrorParametros();
		}
		
	}
	
	/**
	 * 
	 * Procesa la petición de la página de desconexión.
	 * Si la credencial es correcta, mostramos la raíz. En caso contrario, mostramos una 
	 * página indicando que es necesario conectarse (tener una credencial válida), para
	 * acceder a dicho recurso.
	 * 
	 * @param vars	Variables procedentes de la URI.
	 * 
	 * @return	Página HTML de respuesta.
	 */
	private StringBuilder procesaPeticionPaginaDesconexion(Map<String,String> vars){
		
		//Lo primero es comprobar que el usuario tiene privilegios (credencial en nuestra lista)
		//Comprobamos que haya algo en vars, y que exista la variable credencial
		if (vars != null && vars.containsKey(Constantes.CREDENCIAL_VAR)) 
		{
			String credencial = vars.get(Constantes.CREDENCIAL_VAR);
			
			if (Validacion.ValidarCredencial(credencial, this.infoPeersConectados))
			{
				//Si la credencial existe, borramos al usuario de ambas tablas
				
				//Necesitamos recuperar el nick, para borrarlo de infoCredenciales
				Peer peerBorrado = this.infoPeersConectados.get(credencial);
				this.infoCredenciales.remove(peerBorrado.getUsuario().getNick());
				
				//Matamos el hilo del proceso servidor P2P
				peerBorrado.getServidorP2P().interrupt();
				
				//Y le borramos tambien de infoPeersConectados
				this.infoPeersConectados.remove(credencial);
				
				//Y le devolvemos a la página raíz
				return GeneradorHtml.generaRaiz(0);
			}else{
				//Error por credencial inválida
				return GeneradorHtml.generaErrorCredencial();
			}
				

		}else{
			///Si no pasan datos nick y clave, presentamos página de error de no conexión
			return GeneradorHtml.generaErrorParametros();
		}
	}
	
	/**
	 * 
	 * Procesa la petición de la página de mostrar el directorio local.
	 * Si la credencial es correcta, mostramos los ficheros del directorio del usuario.
	 * En caso contrario, mostramos una página indicando que es necesario 
	 * conectarse (tener una credencial válida), para acceder a dicho recurso.
	 * 
	 * @param vars	Variables procedentes de la URI.
	 * 
	 * @return	Página HTML de respuesta.
	 */
	private StringBuilder procesaPeticionPaginaDirLocal(Map<String,String> vars){
		
		//Lo primero es comprobar que el usuario tiene privilegios (credencial en nuestra lista)
		//Comprobamos que haya algo en vars, y que exista la variable credencial
		if (vars != null && vars.containsKey(Constantes.CREDENCIAL_VAR)) 
		{
			String credencial = vars.get(Constantes.CREDENCIAL_VAR);
			
			if (Validacion.ValidarCredencial(credencial, this.infoPeersConectados))
			{

				//Cambios, para adaptar a diseño de fase 2. Llamamos a un método del peer.
				try
				{
					//Recuperamos el peer asociado a nuestra credencial
					Peer peerAsociado = this.infoPeersConectados.get(credencial);
					ListaFicheros listaFicherosCompartidos = peerAsociado.obtenerListadoCompartido();
				
					return GeneradorHtml.generaDirCompartido(credencial, listaFicherosCompartidos);
					
				}catch (ListaFicherosException ex){
					//Si el peer nos eleva una excepción, generamos página de error
					return GeneradorHtml.generaErrorExcepcion(credencial, ex.getMensajeDeError());
				}
			}else{
				//Error por credencial inválida
				return GeneradorHtml.generaErrorCredencial();
			}

		}else{
			///Si no pasan datos nick y clave, presentamos página de error de no conexión
			return GeneradorHtml.generaErrorParametros();
		}
		
	}	

	
	/**
	 * 
	 * Procesa la petición de la página de mostrar el directorio local.
	 * Si la credencial es correcta, mostramos los ficheros del directorio del usuario.
	 * En caso contrario, mostramos una página indicando que es necesario 
	 * conectarse (tener una credencial válida), para acceder a dicho recurso.
	 * 
	 * @param vars	Variables procedentes de la URI.
	 * 
	 * @return	Página HTML de respuesta.
	 */
	private StringBuilder procesaPeticionPaginaListaTodos(Map<String,String> vars){
		
		//Lo primero es comprobar que el usuario tiene privilegios (credencial en nuestra lista)
		//Comprobamos que haya algo en vars, y que exista la variable credencial
		if (vars != null && vars.containsKey(Constantes.CREDENCIAL_VAR)) 
		{
			String credencial = vars.get(Constantes.CREDENCIAL_VAR);
			System.out.println("Se intentan conectar a lista_todos con credencial " + credencial);

			
			if (Validacion.ValidarCredencial(credencial, this.infoPeersConectados))
			{
				//Si la credencial es válida, generamos una página con la información de los 
				//peers conectados a este terminal (cambiará en fase iii)
				return GeneradorHtml.generaListaTodos(credencial, this.infoPeersConectados);
			}else{
				
				//Error por credencial inválida
				return GeneradorHtml.generaErrorCredencial();
			}				

		}else{
			///Si no pasan datos nick y clave, presentamos página de error de no conexión
			return GeneradorHtml.generaErrorParametros();
		}
		
	}		
	
	/**
	 * 
	 * Procesa la petición de la página de mostrar el directorio remoto
	 * Si la credencial es correcta:
	 * 	- Recuperamos el peer asociado a dicha credencial.
	 * 	- Comprobamos que existe un usuario en nuestra terminal que corresponde con 
	 * 		usuario remoto ¿?
	 * 	- Recuperamos los datos de conexion p2p del usuario remoto
	 * 	- Llamamos a un método de nuestro peer asociado, en el que pasándole los datos del
	 * 		usuario remoto, establecerá una conexión con el mismo.
	 * 	- Como valor de vuelta de la invocación de ese método, se ofrece la lista de ficheros
	 * 	- Con dicha lista, generamos la página html a devolver por el cliente
	 * 
	 * @param vars	Variables procedentes de la URI.
	 * 
	 * @return	Página HTML de respuesta.
	 */
	private StringBuilder procesaPeticionPaginaDirRemoto(Map<String,String> vars){
		
		//Lo primero es comprobar que el usuario tiene privilegios (credencial en nuestra lista)
		//Comprobamos que haya algo en vars, y que exista la variable credencial
		if (vars != null && vars.containsKey(Constantes.CREDENCIAL_VAR) && vars.containsKey(Constantes.USUARIO_REMOTO_VAR)) 
		{
			String credencial = vars.get(Constantes.CREDENCIAL_VAR);

			if (Validacion.ValidarCredencial(credencial, this.infoPeersConectados))
			{
				String nickRemoto = vars.get(Constantes.USUARIO_REMOTO_VAR);
				
				//Recuperamos el peer asociado a nuestra credencial
				Peer peerAsociado = this.infoPeersConectados.get(credencial);
				
				//Recuperamos la info del peer al que nos vamos a conectar (cambiará en fase 3, no?)
				if(this.infoCredenciales.containsKey(nickRemoto))
				{
					Peer peerRemoto = this.infoPeersConectados.get(this.infoCredenciales.get(nickRemoto));
					System.out.println("Se va a establecer una conexión de: " + peerAsociado.getUsuario().getNick() + " hacia " + peerRemoto.getUsuario().getNick());

					//Llamamos a un método de nuestro peerAsociado, que envia mensaje a PeerRemoto
					try
					{
						ListaFicheros listaFicherosRemoto = peerAsociado.obtenerListadoRemoto(peerRemoto.getUsuario());
				
						System.out.println("El listado de ficheros de " + peerRemoto.getUsuario().getNick() + " es:" + listaFicherosRemoto.toString());
						return GeneradorHtml.generaDirRemoto(credencial, listaFicherosRemoto, peerRemoto.getUsuario().getNick());
						
					}catch (PeerException ex){
						//Si el peer nos eleva una excepción, generamos página de error
						return GeneradorHtml.generaErrorExcepcion(credencial, ex.getMensajeDeError());
					}
				}else{
					//Nos dan todas las variables, pero el usuario remoto no existe en nuestra tabla de datos.					
					return GeneradorHtml.generaErrorUsuarioRemotoNoValido(credencial);
				}

			}else{
				//Error por credencial inválida
				return GeneradorHtml.generaErrorCredencial();
			}
		}else{
			//Error por falta de parámetros
			return GeneradorHtml.generaErrorParametros();
		}
		
	}		

	/**
	 * 
	 * Procesa la petición de la página que muestra el formulario para envio de mensajes 
	 * entre peers
	 * 
	 * @param vars	Variables procedentes de la URI.
	 * 
	 * @return	Página HTML de respuesta.
	 */
	private StringBuilder procesaPeticionPaginaEscribirMensaje(Map<String,String> vars){
		
		//Lo primero es comprobar que el usuario tiene privilegios (credencial en nuestra lista)
		//Comprobamos que haya algo en vars, y que exista la variable credencial
		if (vars != null && vars.containsKey(Constantes.CREDENCIAL_VAR) && vars.containsKey(Constantes.USUARIO_REMOTO_VAR)) 
		{
			String credencial = vars.get(Constantes.CREDENCIAL_VAR);

			if (Validacion.ValidarCredencial(credencial, this.infoPeersConectados))
			{
				String nickRemoto = vars.get(Constantes.USUARIO_REMOTO_VAR);
				
				if(this.infoCredenciales.containsKey(nickRemoto))
				{
					return GeneradorHtml.generaEscribirMensaje(credencial, nickRemoto);
					
				}else{
					//Nos dan todas las variables, pero el usuario remoto no existe en nuestra tabla de datos.					
					return GeneradorHtml.generaErrorUsuarioRemotoNoValido(credencial);
				}

			}else{
				//Error por credencial inválida
				return GeneradorHtml.generaErrorCredencial();
			}
		}else{
			//Error por falta de parámetros
			return GeneradorHtml.generaErrorParametros();
		}
		
	}	

	
	/**
	 * 
	 * Procesa la petición de envío de mensaje a un peer.
	 * Si la credencial es correcta:
	 * 	- Recuperamos el peer asociado a dicha credencial.
	 * 	- Comprobamos que existe un usuario en nuestra terminal que corresponde con 
	 * 		usuario remoto
	 * 	- Recuperamos los datos de conexion p2p del usuario remoto
	 * 	- Llamamos a un método de nuestro peer asociado, en el que pasándole los datos del
	 * 		usuario remoto, establecerá una conexión con el mismo.
	 * 	- Como valor de vuelta de la invocación de ese método, se ofrece la respuesta al envío.
	 * 	- Con dicha respuesta, generamos la página html a devolver por el terminal
	 * 
	 * @param vars	Variables procedentes de la URI.
	 * 
	 * @return	Página HTML de respuesta.
	 */
	private StringBuilder procesaPeticionPaginaEnviarMensaje(Map<String,String> vars){
		
		//Lo primero es comprobar que el usuario tiene privilegios (credencial en nuestra lista)
		//Comprobamos que haya algo en vars, y que exista la variable credencial
		if (vars != null && vars.containsKey(Constantes.CREDENCIAL_VAR) && vars.containsKey(Constantes.USUARIO_REMOTO_VAR))
				//&& vars.containsKey(Constantes.ASUNTO_VAR) && vars.containsKey(Constantes.CUERPO_VAR)) 
		{
			String credencial = vars.get(Constantes.CREDENCIAL_VAR);

			if (Validacion.ValidarCredencial(credencial, this.infoPeersConectados))
			{
				String nickRemoto = vars.get(Constantes.USUARIO_REMOTO_VAR);
				
				//Recuperamos el peer asociado a nuestra credencial
				Peer peerAsociado = this.infoPeersConectados.get(credencial);
				
				//Recuperamos la info del peer al que nos vamos a conectar (cambiará en fase 3, no?)
				if(this.infoCredenciales.containsKey(nickRemoto))
				{
					//Comprobamos que ha escrito cuerpo y asunto
					if (vars.containsKey(Constantes.ASUNTO_VAR) && vars.containsKey(Constantes.CUERPO_VAR))
					{
					
						String asunto = vars.get(Constantes.ASUNTO_VAR);
						String cuerpo = vars.get(Constantes.CUERPO_VAR);
	
						Peer peerRemoto = this.infoPeersConectados.get(this.infoCredenciales.get(nickRemoto));
						System.out.println("Se va a establecer una conexión de: " + peerAsociado.getUsuario().getNick() + " hacia " + peerRemoto.getUsuario().getNick());
						
						//Llamamos a un método de nuestro peerAsociado, que envia mensaje a PeerRemoto
						try
						{
							String resultadoEnvio = peerAsociado.entregaMensaje(peerRemoto.getUsuario(), asunto, cuerpo);
							System.out.println("Resultado de envio de mensaje " + resultadoEnvio);
							return GeneradorHtml.generaEnviarMensaje(credencial, resultadoEnvio);
						}catch(PeerException ex){
							//Si el peer nos eleva una excepción, generamos página de error
							return GeneradorHtml.generaErrorExcepcion(credencial, ex.getMensajeDeError());
						}
					}else{
						
						//Devolvemos página de error por falta de asunto o mensaje vacio
						return GeneradorHtml.generarErrorMensaje(credencial);
						
					}
					
				}else{
					//Nos dan todas las variables, pero el usuario remoto no existe en nuestra tabla de datos.					
					return GeneradorHtml.generaErrorUsuarioRemotoNoValido(credencial);
				}

			}else{
				//Error por credencial inválida
				return GeneradorHtml.generaErrorCredencial();
			}
		}else{
			
				//Error por falta de alguno de los parámetros
				return GeneradorHtml.generaErrorParametros();
		}
		
	}	
	
	
	/**
	 * 
	 * Procesa la petición de la página de mostrar la página que muestra los mensajes
	 * recibidos por este peer.
	 * 
	 * @param vars	Variables procedentes de la URI.
	 * 
	 * @return	Página HTML de respuesta.
	 */
	private StringBuilder procesaPeticionPaginaVerMensajes(Map<String,String> vars){
		
		//Lo primero es comprobar que el usuario tiene privilegios (credencial en nuestra lista)
		//Comprobamos que haya algo en vars, y que exista la variable credencial
		if (vars != null && vars.containsKey(Constantes.CREDENCIAL_VAR)) 
		{
			String credencial = vars.get(Constantes.CREDENCIAL_VAR);
			
			if (Validacion.ValidarCredencial(credencial, this.infoPeersConectados))
			{
				//Generamos página con el listado de mensajes recibidos por este peer
				return GeneradorHtml.generaVerMensajes(credencial, this.infoPeersConectados.get(credencial));
			
			}else{
				//Error por credencial inválida
				return GeneradorHtml.generaErrorCredencial();
			}
		}else{
			//Error por falta de parámetros
			return GeneradorHtml.generaErrorParametros();
		}
		
	}	
	
	/**
	 * 
	 * Agrega la cabecera de respuesta 200 (OK) a la respuesta.
	 * 
	 * @param htmlPage	Página HTML
	 * @return	Página HTML con cabecera
	 */
	private StringBuilder generaMensaje200 (StringBuilder htmlPage) {
		StringBuilder respuesta = new StringBuilder("");
		respuesta.append("HTTP/1.1 200 OK\r\n");
		respuesta.append("Connection: close\r\n");
		respuesta.append("Content-Type: text/html\r\n");
		respuesta.append("Content-Length: " + htmlPage.length() + "\r\n");
		respuesta.append("\r\n");
		respuesta.append(htmlPage);
		return respuesta;
	}

	
	/**
	 * 
	 * Agrega la cabecera de respuesta 404 (Not Found) a la respuesta.
	 * 
	 * @param htmlPage	Página HTML
	 * @return	Página HTML con cabecera
	 */	
	private StringBuilder generaMensaje404(StringBuilder htmlPage) {
		StringBuilder respuesta = new StringBuilder("");
		respuesta.append("HTTP/1.1 404 Not Found\r\n");
		respuesta.append("Connection: close\r\n");
		respuesta.append("Content-Type: text/html\r\n");
		respuesta.append("Content-Length: " + htmlPage.length() + "\r\n");
		respuesta.append("\r\n");
		respuesta.append(htmlPage);
		return respuesta;
	}
	
	/**
	 * 
	 * Genera una respuesta en función del recurso solicitado
	 * 
	 * @param path	Recurso solicitado
	 * @param vars	Variables provenientes de la URI
	 * 
	 * @return	Página HTML de respuesta
	 */
	private StringBuilder generaRespuesta(String path, Map<String, String> vars){
		
		if(path.contentEquals(Constantes.RAIZ_PATH))
			return generaMensaje200(procesaPeticionPaginaRaiz(vars));
		else if (path.contentEquals(Constantes.MENU))
			return generaMensaje200(procesaPeticionPaginaMenu(vars));
		else if (path.contentEquals(Constantes.DIR_LOCAL))
			return generaMensaje200(procesaPeticionPaginaDirLocal(vars));
		else if (path.contentEquals(Constantes.DESCONEXION))
			return generaMensaje200(procesaPeticionPaginaDesconexion(vars));
		else if (path.contentEquals(Constantes.LISTA_TODOS))
			return generaMensaje200(procesaPeticionPaginaListaTodos(vars));
		else if (path.contentEquals(Constantes.DIR_REMOTO))
			return generaMensaje200(procesaPeticionPaginaDirRemoto(vars));
		else if (path.contentEquals(Constantes.ESCRIBIR_MENSAJE))
			return generaMensaje200(procesaPeticionPaginaEscribirMensaje(vars));
		else if (path.contentEquals(Constantes.ENVIAR_MENSAJE))
			return generaMensaje200(procesaPeticionPaginaEnviarMensaje(vars));
		else if (path.contentEquals(Constantes.VER_MENSAJES))
			return generaMensaje200(procesaPeticionPaginaVerMensajes(vars));
		
		return generaMensaje404(procesaPeticionPaginaDesconocida());
	}
		
	/**
	 * 
	 * Analiza y atiende la petición HTTP del cliente.
	 * 
	 */
	public void run(){
		try{
			//Recuperamos la petición del cliente
			BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			
			//Leemos la primera línea (línea de petición HTTP) que es obligatoria
			String firstLine = in.readLine();
			System.out.println(firstLine);
						
			//Leemos resto de cabeceras, por si nos interesan. Sólo trabajamos con GET, por lo que no hay cuerpo
			String line = null;
			while( (line=in.readLine()) != null){
				//Si la línea está en blanco, entonces se han terminado las cabeceras
				if(line.contentEquals(""))
					break;
			}
				
			//Recuperamos el recuros solicitado y las variables GET presentes
			String path = AnalizadorHttpGet.getPath(firstLine);
			Map<String, String> vars = AnalizadorHttpGet.getVars(firstLine);
			
			//Procedemos a construir la respuesta
			StringBuilder respuesta = generaRespuesta(path, vars);
			
			PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			out.print(respuesta.toString());
			out.close();
			in.close();
			
		} catch (IOException e) {
			System.out.println("AVISO: ha habido un problema leyendo o escribiendo en el socket especificado");
			System.out.println("Los detalles del problema son los siguientes");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
	}

}
