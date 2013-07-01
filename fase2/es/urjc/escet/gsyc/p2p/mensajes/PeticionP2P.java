package es.urjc.escet.gsyc.p2p.mensajes;


/**
 * 
 * Clase abstracta para peticiones en nuestro protocolo p2p
 * 
 * @author drozas
 *
 */
public abstract class PeticionP2P extends MensajeP2P {
	
	//Añadimos serialVerisonUID, generado con serialver
	private static final long serialVersionUID = -3007735967019205875L;

	
	private String emisor;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param emisor
	 */
	public PeticionP2P(String emisor)
	{
		this.emisor = emisor;
	}
	
	/**
	 * 
	 * Devuelve el emisor de la petición.
	 * 
	 * @return
	 */
	public String getEmisor()
	{
		return emisor;
	}
}