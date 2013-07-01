package es.urjc.escet.gsyc.p2p.mensajes;

/**
 * 
 * Clase para mensajes de respuesta de nuestro protocolo p2p
 * 
 * @author drozas
 *
 */
public class RespuestaP2P extends MensajeP2P {
	
	//Añadimos serialVerisonUID, generado con serialver
	private static final long serialVersionUID = -5474477086660390343L;
	
	public static final int OK = 0;
	public static final int MENSAJE_DESCONOCIDO = -1;
	public static final int ERROR_DE_PARAMETRO = -2;
	public static final int ERROR_DE_COMUNICACION = -3;
	public static final int ERROR_GESTION_FICHEROS = -4;
	protected int codigoError;
	protected String descripcion;

	/**
	 * 
	 * Constructor
	 * 
	 * @param codigoError
	 * @param descripcion
	 */
	public RespuestaP2P(int codigoError, String descripcion){
		this.codigoError = codigoError;
		this.descripcion = descripcion;
	}
	
	/**
	 * 
	 * Devuelve el código de error.
	 * 
	 * @return
	 */
	public int getCodigoError(){
		return this.codigoError;
	}
	
	/**
	 * 
	 * Devuelve la descripción del error
	 * 
	 * @return
	 */
	public String getDescription(){
		return this.descripcion;
	}
}