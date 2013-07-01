package es.urjc.escet.gsyc.p2p.mensajes;

/**
 * 
 * Clase con funcionalidades necesarias para los mensajes de petición de listado de
 * ficheros de nuestro protocolo p2p
 * 
 * @author drozas
 *
 */
public class PeticionListaFicheros extends PeticionP2P {
	
	//Añadimos serialVerisonUID, generado con serialver
	private static final long serialVersionUID = 4943874408078328381L;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param emisor
	 */
	public PeticionListaFicheros(String emisor)
	{
		super(emisor);
	}
}