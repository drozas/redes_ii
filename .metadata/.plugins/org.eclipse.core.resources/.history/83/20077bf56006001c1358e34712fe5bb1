package es.urjc.escet.gsyc.p2p.tipos;

import java.io.Serializable;

/**
 * 
 * Clase con funcionalidades para mensajes de intercambio entre peers.
 * 
 * @author drozas
 *
 */
public class Mensaje implements Serializable {
	private String emisor;
	private String receptor;
	private String asunto;
	private String mensaje;
	private long instante;
	
	//Añadimos serialVerisonUID, generado con serialver
	private static final long serialVersionUID = 3431169770122723125L;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param emisor
	 * @param receptor
	 * @param asunto
	 * @param mensaje
	 */
	public Mensaje(String emisor, String receptor, String asunto, String mensaje)
	{
		this.emisor = emisor;
		this.receptor = receptor;
		this.asunto = asunto;
		this.mensaje = mensaje;
		this.instante = System.currentTimeMillis();
	}
	
	/**
	 * 
	 * Devuelve el emisor del mensaje
	 * 
	 * @return
	 */
	public String getEmisor(){
		return emisor;
	}
	
	/**
	 * 
	 * Devuelve el receptor del mensaje
	 * 
	 * @return
	 */
	public String getReceptor(){
		return receptor;
	}
	
	/**
	 * 
	 * Devuelve el asunto del mensaje
	 * 
	 * @return
	 */
	public String getAsunto(){
		return asunto;
	}
	
	/**
	 * 
	 * Devuelve el mensaje
	 * 
	 * @return
	 */
	public String getMensaje(){
		return mensaje;
	}
	
	/**
	 * 
	 * Devuelve el momento de envío
	 * 
	 * @return
	 */
	public long getInstanteEnvio(){
		return instante;
	}
	
	public String toString()
	{
		String mensajeString = new String();
		
		mensajeString += "Emisor: " + this.getEmisor() + "\n";
		mensajeString += "Receptor: " + this.getReceptor()+ "\n";
		mensajeString += "Instante de envio: " + this.getInstanteEnvio()+ "\n";
		mensajeString += "Asunto: " + this.getAsunto()+ "\n";
		mensajeString += "Contenido: " + this.getMensaje()+ "\n";
		
		return mensajeString;
	
	}
}