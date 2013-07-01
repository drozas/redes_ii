package es.urjc.escet.gsyc.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import es.urjc.escet.gsyc.aux.Md5;
import es.urjc.escet.gsyc.html.GeneradorHtml;
import es.urjc.escet.gsyc.peer.Peer;

public class GestorDePeticionesHttp extends Thread {
	private Socket socket;
	
	//drozas: ¿syncronhized?
	private Map<String, Peer> infoPeersConectados;
	
	//Añadimos una nueva tabla hash en la que almacenamos usuario y credencial.
	//Si el usuario ya estaba conectado, le daremos la misma credencial.
	private Map<String, String> infoCredenciales;
	
	/**
	 * 
	 * Constructor del gestor de peticiones
	 * 
	 * @param socket 	Socket
	 * @param infoPeersConectados	Tabla hash, con correspondencia credencial-usuario
	 * @param infoCredenciales	Tabla hash, con correspondencia usuario-credencial
	 */
	public GestorDePeticionesHttp(Socket socket, Map<String, Peer> infoPeersConectados, 
									Map<String, String> infoCredenciales){
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

				System.out.println("Se intentan conectar con los datos " + nick  + " " + clave_encriptada);

				Peer infoPeer = new Peer();
				switch (Validacion.ValidarUsuario(nick, clave_encriptada, infoPeer, this.infoCredenciales)) {
				case 0:
					//Generamos la credencial, y guardamos en lista de usuarios a dicho usuario con su credencial
					System.out.println("Usuario: " + infoPeer.getClave() + infoPeer.getNick() + infoPeer.getCorreoElectronico());
					String credencial = Validacion.ObtenerCredencial();
					this.infoPeersConectados.put(credencial, infoPeer);
					//Y lo guardamos también en la tabla que gestiona nick-credencial.
					this.infoCredenciales.put(nick, credencial);
					
					//Generamos la página de transición, que tiene ya la variable credencial en la uri
					return GeneradorHtml.generaConfirmacionValidacion(credencial);
				case 1:
					System.out.println("Se ha intentado conectar un usuario que ya estaba conectado");
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
			///Si no pasan datos nick y clave, presentamos raíz estándar
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
			System.out.println("Se intentan conectar al menu con credencial " + credencial);

			
			if (Validacion.ValidarCredencial(credencial, this.infoPeersConectados))
			{
				//Si la credencial es correcta, cargamos la info del usuario y generamos la página.
				Peer usuario = infoPeersConectados.get(credencial);
				return GeneradorHtml.generaMenu(usuario,credencial);
			}else{
				
				return GeneradorHtml.generaNoConectado();
			}
				

		}else{
			///Si no pasan datos nick y clave, presentamos página de error de no conexión.
			return GeneradorHtml.generaNoConectado();
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
			System.out.println("Se intentan desconectar con credencial " + credencial);

			
			if (Validacion.ValidarCredencial(credencial, this.infoPeersConectados))
			{
				//Si la credencial existe, borramos al usuario de ambas tablas
				
				//Necesitamos recuperar el nick, para borrarlo de infoCredenciales
				Peer peer = this.infoPeersConectados.get(credencial);
				this.infoCredenciales.remove(peer.getNick());
				
				//Y le borramos tambien de infoPeersConectados
				this.infoPeersConectados.remove(credencial);
				
				//Y le devolvemos a la página raíz
				return GeneradorHtml.generaRaiz(0);
			}else{
				
				return GeneradorHtml.generaNoConectado();
			}
				

		}else{
			///Si no pasan datos nick y clave, presentamos página de error de no conexión
			return GeneradorHtml.generaNoConectado();
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
			System.out.println("Se intentan conectar al dir_local con credencial " + credencial);

			
			if (Validacion.ValidarCredencial(credencial, this.infoPeersConectados))
			{
				//Si la credencial existe, tenemos que generar una página con los contenidos del directorio exportado
				Peer usuario = infoPeersConectados.get(credencial);
				//Y le devolvemos la página con los ficheros.
				return GeneradorHtml.generaDirLocal(usuario.getDirectorioExportado(), credencial);
			}else{
				
				return GeneradorHtml.generaNoConectado();
			}
				

		}else{
			///Si no pasan datos nick y clave, presentamos página de error de no conexión
			return GeneradorHtml.generaNoConectado();
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
