package es.urjc.escet.gsyc.p2p.tipos;

/**
 * 
 * Excepciones lanzadas por errores en la gestión de ficheros
 * 
 * @author drozas
 *
 */
public class ListaFicherosException extends Exception {
	
	//Añadimos serialVerisonUID, generado con serialver
	private static final long serialVersionUID = -2626499377179815998L;
	
	private String mensajeDeError;
	
	/**
	 * 
	 * Constructor
	 *
	 */
	public ListaFicherosException()
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