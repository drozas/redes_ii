package es.urjc.escet.gsyc.p2p.mensajes;

import es.urjc.escet.gsyc.p2p.tipos.ListaFicheros;

/**
 * 
 * Clases con funcionalidades necesarias para los mensajes de respuesta al listado de 
 * ficheros, bajo nuestro protocolo p2p
 * 
 * @author drozas
 *
 */
public final class RespuestaListaFicheros extends RespuestaP2P {
	
	//Añadimos serialVerisonUID, generado con serialver
	private static final long serialVersionUID = 7983320916066974597L;
	
	private ListaFicheros lista;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param codigoError
	 * @param descripcion
	 * @param lista
	 */
	public RespuestaListaFicheros(int codigoError, String descripcion, ListaFicheros lista){
		super(codigoError, descripcion);
		this.lista = lista;
	}
	
	/**
	 * 
	 * Devuelve la lista de ficheros
	 * 
	 * @return
	 */
	public ListaFicheros getListaFicheros(){
		return lista;
	}
}