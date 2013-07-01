package es.urjc.escet.gsyc.p2p.mensajes;

/**
 * 
 * Clase con funcionalidades para mensajes de petición de ficheros bajo nuestro
 * protocolo p2p.
 * 
 * @author drozas
 *
 */
public class PeticionFichero extends PeticionP2P {
	
	//Añadimos serialVerisonUID, generado con serialver
	private static final long serialVersionUID = -3007735967019205875L;
	
	private String nombreFichero;
	private int puertoReceptor;

	/**
	 * 
	 * Constructor
	 * 
	 * @param emisor
	 * @param nombreFichero
	 * @param puertoReceptor
	 */
	public PeticionFichero(String emisor, String nombreFichero,	int puertoReceptor)
	{
		super(emisor);
		this.nombreFichero = nombreFichero;
		this.puertoReceptor = puertoReceptor;
	}
	
	/**
	 * 
	 * Devuelve el nombre del fichero
	 * 
	 * @return
	 */
	public String getNombreFichero(){
		return nombreFichero;
	}
	
	/**
	 * 
	 * Devuelve el puerto del receptor del fichero.
	 * 
	 * @return
	 */
	public int getPuertoReceptor(){
		return puertoReceptor;
	}
}