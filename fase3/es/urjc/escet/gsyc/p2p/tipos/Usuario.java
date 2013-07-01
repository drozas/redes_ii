package es.urjc.escet.gsyc.p2p.tipos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.urjc.escet.gsyc.config.ConfigException;

/*
 * Clase Usuario. Proporciona los métodos 
 */
public class Usuario implements Serializable{
	
	/* Atributos de la clase*/
	private int puertoP2P;
	private String directorioExportado;
	private String nick;
	private String nombreCompleto;
	private String correoElectronico;
	//TODO: Eliminar acceso a clave
	private String clave;
	
	private String host;

	
	/* Constantes para nombres de fichero de configuración*/
	private static String DIRECTORIO_EXPORTADO = "DIRECTORIO_EXPORTADO";
	private static String PUERTO_P2P_KEY = "PUERTO_P2P";
	private static String NICK= "NICK";
	private static String NOMBRE_COMPLETO = "NOMBRE_COMPLETO";
	private static String CORREO_ELECTRONICO = "CORREO_ELECTRONICO";
	private static String CLAVE= "CLAVE";
	
	/**
	 * 
	 * Constructor vacío
	 * 
	 */
	public Usuario()
	{
		
	}
	
	/**
	 * 
	 * @param puertoP2P
	 * @param directorioExportado
	 * @param nick
	 * @param nombreCompleto
	 * @param correoElectronico
	 * @param clave
	 */
	public Usuario(int puertoP2P, String host, String directorioExportado, String nick, 
			String nombreCompleto, String correoElectronico, String clave)
	{
		this.puertoP2P = puertoP2P;
		this.host = host;
		this.directorioExportado = directorioExportado;
		this.nick = nick;
		this.nombreCompleto = nombreCompleto;
		this.correoElectronico = correoElectronico;
		this.clave = clave;
	}

	/**
	 * 
	 * @return
	 */
	public int getPuertoP2P(){
		return this.puertoP2P;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHost(){
		return this.host;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDirectorioExportado(){
		return this.directorioExportado;
	}
	/**
	 * 
	 * @return
	 */
	public String getNick(){
		return this.nick;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNombreCompleto(){
		return this.nombreCompleto;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCorreoElectronico(){
		return this.correoElectronico;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClave(){
		return this.clave;
	}
	
	/**
	 * 
	 * @param config
	 * @param parametro
	 * @return
	 * @throws ConfigException
	 */
	private static String leeParametro(BufferedReader config, String parametro) throws ConfigException {
		try{
			String line = config.readLine();				  
			String regEx = "^" + parametro + "\\s+\"([^\"]*)\"$";
			
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(line);
			if(!m.matches()){
				ConfigException ce = new ConfigException();
				ce.setMensajeDeError("El parámetro " + parametro + " no está definido");
				throw ce;
			}
			
			return m.group(1);
			
		} catch (IOException e) {
			ConfigException ce = new ConfigException();
			ce.setMensajeDeError("No se puede leer el parámetro " + parametro + " del fichero de configuracion");
			throw ce;
		}	
	}
	
	/**
	 * 
	 * @param filename
	 * @throws ConfigException
	 */
	public void CargarConfiguracionPeer(String filename, String host) throws ConfigException {

		FileInputStream fis = null;
		try{
			fis = new FileInputStream(filename);
		} catch(IOException e){
			ConfigException ce = new ConfigException();
			ce.setMensajeDeError("No se puede abrir el fichero de configuración " + filename);
			ce.initCause(e);
			throw ce;
		}
		
		BufferedReader lector = new BufferedReader(new InputStreamReader(fis));

		/* Ejemplo de formato del fichero, hay que respetar el orden indicado
			PUERTO_P2P "3456"
			DIRECTORIO_EXPORTADO "/home/llopez/tmp/redes-II/luis"
			NICK "luis"
			NOMBRE_COMPLETO "Luis López"
			CORREO_ELECTRONICO "neo@iies.es"
			CLAVE "elgato"
		*/
		
		String puertoP2P = leeParametro(lector, PUERTO_P2P_KEY);
		try{
			this.puertoP2P = Integer.parseInt(puertoP2P);
		} catch (NumberFormatException e) {
			ConfigException ce = new ConfigException();
			ce.setMensajeDeError("En el fichero de configuración " + 
							filename + 
							" el parámetro " + 
							PUERTO_P2P_KEY + 
							" debe ser un entero");
			throw ce;
		}
		
		this.directorioExportado = leeParametro(lector, DIRECTORIO_EXPORTADO);
		File dir = new File(this.directorioExportado);
		if(!dir.isDirectory() || !dir.canRead()){
			ConfigException ce = new ConfigException();
			ce.setMensajeDeError("En el fichero de configuración " +
							filename + 
							" el valor " + this.directorioExportado + 
							" del parámetro " + DIRECTORIO_EXPORTADO +
							" no es un directorio válido");
			throw ce;
		}
		
		this.nick = leeParametro(lector, NICK);
		this.nombreCompleto = leeParametro(lector, NOMBRE_COMPLETO);
		this.correoElectronico = leeParametro(lector, CORREO_ELECTRONICO);
		this.clave = leeParametro(lector, CLAVE);
		
		//drozas: el host proviene de la información de conexión con el socket
		this.host = host;
		

		try{
			lector.close();
		} catch(IOException e){
			ConfigException ce = new ConfigException();
			ce.setMensajeDeError("No se puede cerrar el fichero de configuración de usuario " + filename);
			throw ce;
		}
	}
	
	public String toString()
	{
		String res ="";
		
		res += "Nombre: " + this.getNombreCompleto() +"\n";
		res += "Nick: " + this.getNick()+"\n";
		res += "Correo: " + this.getCorreoElectronico()+"\n";
		res += "Clave: " + this.getClave()+"\n";
		res += "Directorio: " + this.getDirectorioExportado()+"\n";
		res += "Direccion: " + this.getHost() + ":" + this.getPuertoP2P()+"\n";
		
		return res;
		
	}

}
