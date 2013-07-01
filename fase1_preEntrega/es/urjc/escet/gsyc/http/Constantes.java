package es.urjc.escet.gsyc.http;

/**
 * 
 * Clase con las constantes que utiliza el servidor HTTP
 * 
 * @author drozas
 *
 */
public class Constantes {
	
	public static final String TERMINAL = " - ## Terminal P2P ## - ";
	
	//Contantes de variables que pasan por get
	public static final String NICK_VAR = "nick";
	public static final String CLAVE_VAR = "clave";
	public static final String CREDENCIAL_VAR  = "credencial";
	
	//drozas: Añadidas constantes para otras páginas
	public static final String RAIZ_PATH ="/";
	public static final String MENU = "/menu";
	public static final String DIR_LOCAL ="/dir_local";
	public static final String DESCONEXION = "/desconexion";
	
	//drozas: Constantes para mensajes de error
	public static final String E_CLAVE = "La clave introducida es incorrecta";
	public static final String E_FICH_USUARIO = "Ocurrió un error con el fichero de configuración de usuario. O no existe, o el formato es incorrecto";	
}
