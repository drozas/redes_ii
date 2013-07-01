package es.urjc.escet.gsyc.p2p.mensajes;


import es.urjc.escet.gsyc.p2p.tipos.Mensaje;

/**
 * 
 * Clase con funcionalidades para las peticiones de la entrega de Mensajes
 * 
 * @author drozas
 *
 */
public class PeticionEntregaMensaje extends PeticionP2P {
	
	//Añadimos serialVerisonUID, generado con serialver
	private static final long serialVersionUID = -2511324523553006518L;
	
	private Mensaje mensaje;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param emisor
	 * @param mensaje
	 */
	public PeticionEntregaMensaje(String emisor, Mensaje mensaje){
		super(emisor);
		this.mensaje = mensaje;
	}
	
	/**
	 * 
	 * Devuelve el mensaje
	 * 
	 * @return
	 */
	public Mensaje getMensaje(){
		return mensaje;
	}
}