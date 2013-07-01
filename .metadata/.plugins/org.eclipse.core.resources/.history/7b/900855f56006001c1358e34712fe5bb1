package es.urjc.escet.gsyc.aux;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * Clase con los métodos referentes a la encriptación con MD5.
 * 
 * @author drozas
 *
 */
public class Md5{

	/**
	 * 
	 * @param claveEnClaro	Clave en claro a codificar.
	 * 
	 * @return	Clave codificada en MD5
	 * 
	 * @throws NoSuchAlgorithmException
	 */
    public static String Encriptar(String claveEnClaro) throws NoSuchAlgorithmException{
    	
    	//Creamos una instancia de la clase que nos permite hacer "resúmenes" en MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        //Pasamos la clave en claro a bytes
        byte[] b = md.digest(claveEnClaro.getBytes());

        int size = b.length;
        
        //Y hacemos una conversión, mediate un string buffer
        StringBuffer h = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            int u = b[i]&255; // unsigned conversion
            if (u<16) {
                h.append("0"+Integer.toHexString(u));
            } else {
                h.append(Integer.toHexString(u));
            }
        }
        return h.toString();
    }

}  