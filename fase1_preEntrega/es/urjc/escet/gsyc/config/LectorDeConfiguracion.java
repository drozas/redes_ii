package es.urjc.escet.gsyc.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LectorDeConfiguracion {
	
	private static String DIRECTORIO_DE_USUARIOS = "DIRECTORIO_DE_USUARIOS";
	private static String PUERTO_HTTP_KEY = "PUERTO_HTTP";
	private static String HOST_RMI_REGISTRY = "HOST_RMI_REGISTRY";
	private static String PUERTO_RMI_REGISTRY = "PUERTO_RMI_REGISTRY";
	private static String PATH_RMI_REGISTRADOR = "PATH_RMI_REGISTRADOR";
	
	
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
	
	public static void LeeConfiguracionGeneral(String filename) throws ConfigException {

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
		PUERTO_HTTP 			"3456"
		DIRECTORIO_DE_USUARIOS "/home/llopez/tmp/redes-II"
		HOST_RMI_NAMING 		"localhost"
		PUERTO_RMI_NAMING	"4567"
		PATH_RMI_REGISTRADOR "Registrador"
		*/
		
		String puertoHttp = leeParametro(lector, PUERTO_HTTP_KEY);
		try{
			ConfiguracionGeneral.puertoHttp = Integer.parseInt(puertoHttp);
		} catch (NumberFormatException e) {
			ConfigException ce = new ConfigException();
			ce.setMensajeDeError("En el fichero de configuración " + 
							filename + 
							" el parámetro " + 
							PUERTO_HTTP_KEY + 
							" debe ser un entero");
			throw ce;
		}
		
		ConfiguracionGeneral.directorioDeUsuarios = leeParametro(lector, DIRECTORIO_DE_USUARIOS);
		File dir = new File(ConfiguracionGeneral.directorioDeUsuarios);
		if(!dir.isDirectory() || !dir.canRead()){
			ConfigException ce = new ConfigException();
			ce.setMensajeDeError("En el fichero de configuración " +
							filename + 
							" el valor " + ConfiguracionGeneral.directorioDeUsuarios + 
							" del parámetro " + DIRECTORIO_DE_USUARIOS +
							" no es un directorio válido");
			throw ce;
		}
		
		ConfiguracionGeneral.hostRmiRegistry = leeParametro(lector, HOST_RMI_REGISTRY);

		String puertoRmiNamingStr = leeParametro(lector, PUERTO_RMI_REGISTRY);
		try{
			ConfiguracionGeneral.puertoRmiRegistry = Integer.parseInt(puertoRmiNamingStr);
		} catch (NumberFormatException e) {
			ConfigException ce = new ConfigException();
			ce.setMensajeDeError("En el fichero de configuración " + 
							filename + 
							" el parámetro " + 
							PUERTO_RMI_REGISTRY + 
							" debe ser un entero");
			throw ce;
		}
		
		ConfiguracionGeneral.pathRmiDelRegistrador = leeParametro(lector, PATH_RMI_REGISTRADOR);
			
		try{
			lector.close();
		} catch(IOException e){
			ConfigException ce = new ConfigException();
			ce.setMensajeDeError("No se puede cerrar el fichero de configuración " + filename);
			throw ce;
		}
	}
}
