package es.urjc.escet.gsyc.http;

import java.util.Map;
import java.util.Random;

import es.urjc.escet.gsyc.config.ConfigException;
import es.urjc.escet.gsyc.config.ConfiguracionGeneral;
import es.urjc.escet.gsyc.peer.Peer;


/**
 * 
 * Clase con los métodos necesarios para la validación del usuario a través de la raíz
 * o de las credenciales.
 * 
 * @author drozas
 *
 */
public class Validacion {
	
	/**
	 * 
	 * Clase que realiza la validación del usuario a través de la lectura de los ficheros
	 * de configuración de usuario.
	 * Si la validación se produce correctamente, se cargan en memoria los datos de usuario.
	 * 
	 * @param 	nick	Nick de usuario procedente de la petición.
	 * @param 	clave	Clave de usuario procedente de la petición.
	 * @param 	infoPeer	Objeto usuario en el que se cargará su información procedente del fichero
	 * 					si la validación es correcta.
	 * @param 	infoCredenciales	Tabla hash con la asociación credencial-usuario
	 * 
	 * @return	0: Si el proceso fue correcto, y hay que generar credencial.
	 * 			1: Si el proceso fue correcto, pero ya estaba registrado
	 * 			-1: Si falló la contraseña.
	 * 			-2: Si el problema está en el fichero de configuración.
	 */
	public static int ValidarUsuario(String nick, String clave, Peer infoPeer, Map<String,String> infoCredenciales){
		
		String path_config_usuario = ConfiguracionGeneral.getDirectorioDeUsuarios() + "/" +  nick + ".cfg";
			
		try
		{
			//Intentamos cargar los datos de usuario. Si salta la excepción, es que hubo un problema con el fichero
			infoPeer.CargarConfiguracionPeer(path_config_usuario);

			//Y comprobamos que la clave sea la misma que la de la variable de la uri
			///Comprobación de clave
			///TODO: Comprobar que ese usuario no se conecta varias veces???
			if (clave.contentEquals(infoPeer.getClave()))
			{
				//Comprobada que la clave es correcta, comprobamos que no estuviera validado ya
				
				System.out.println("El usuario se ha validado con éxito");
				if (infoCredenciales.containsKey(nick))
				{
					System.out.println("El usuario " + nick + " ya estaba conectado. Le damos la misma credencial.");
					return 1;
				}else{
					System.out.println("El usuario " + nick + " no estaba conectado, le daremos una nueva credencial.");
					return 0;
				}
			}else{
				System.out.println("Contraseña incorrecta");
				return -1;
			}

		}catch(ConfigException e)
		{
			System.out.println("El fichero de configuración del usuario tiene un formato inválido o no existe.");
			return -2;
		}
	
		
	}
	
	/**
	 * 
	 * Genera una número aleatorio positivo que se usa como credencial.
	 * 
	 * @return credencial
	 */
	public static String ObtenerCredencial()
	{
		Random generador = new Random ();
		Long credencial = generador.nextLong();
		
		while (credencial<=0)
			credencial =  generador.nextLong();
		
		return credencial.toString();
	}
	
	/**
	 * 
	 * Mira si la credencial está en nuestra tabla hash credencial-usuario
	 * 
	 * @param 	credencial	Credencial procedente de la petición.
	 * @param 	usuarios	Tabla de usuarios conectados actualmente, cuya clave es la credencial.
	 * 
	 * @return	true: Si existe algún usuario en nuestra tabla con dicha credencial.
	 * 			false: En caso contrario.
	 */
	public static Boolean ValidarCredencial(String credencial, Map<String,Peer> usuarios){

		return (credencial != null) && (usuarios.containsKey(credencial));
		
	}
	
	

}
