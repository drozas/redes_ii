package es.urjc.escet.gsyc.p2p.tipos;

/**
 * 
 * Excepciones lanzadas por errores en el protocolo p2p
 * 
 * @author drozas
 *
 */
public class PeerException extends Exception {
	
	private static final long serialVersionUID = 3431169770122723125L;
	
	private String mensajeDeError;
	
	/**
	 * 
	 * Constructor
	 *
	 */
	public PeerException()
	{
		super();
		this.mensajeDeError = "";
	}
	
	/**
	 * 
	 * Permite escribir cuál fue el mensaje de error
	 * 
	 * @param mensajeDeError	Mensaje a escribir.
	 */
	public void setMensajeDeError(String mensajeDeError)
	{
		this.mensajeDeError = mensajeDeError;
	}
	
	/**
	 * 
	 * Devuelve el mensaje de error
	 * 
	 * @return	Mensaje de error
	 */
	public String getMensajeDeError()
	{
		return this.mensajeDeError;
	}
}