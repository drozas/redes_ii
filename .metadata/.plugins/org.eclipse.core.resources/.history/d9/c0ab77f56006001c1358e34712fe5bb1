package es.urjc.escet.gsyc.p2p.tipos;

import java.io.Serializable;

/**
 * 
 * Clase que almacena información acerca de un fichero
 * 
 * @author drozas
 *
 */
public class DescriptorFichero implements Serializable, Comparable {
	
	//Añadimos serialVerisonUID, generado con serialver
	private static final long serialVersionUID = -5011334117180501248L;
	
	private String nombreFichero;
	private long bytesFichero;
	private long ultimaModificacion;
	
	
	/**
	 * Método que compara descriptores 
	 */
	public int compareTo(Object other){
		DescriptorFichero df = (DescriptorFichero)other;
		return nombreFichero.compareTo(df.nombreFichero);
	}
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param nombreFichero
	 * @param bytesFichero
	 * @param ultimaModificcacion
	 */
	public DescriptorFichero ( String nombreFichero, long bytesFichero,	long ultimaModificcacion)
	{
		this.nombreFichero = nombreFichero;
		this.bytesFichero = bytesFichero;
		this.ultimaModificacion = ultimaModificcacion;
	}
	
	/**
	 * 
	 * Devuelve el nombre del fichero
	 * 
	 * @return
	 */
	public String getNombreFichero(){
		return this.nombreFichero;
	}
	
	/**
	 * 
	 * Devuelve el nº de bytes del fichero
	 * 
	 * @return
	 */
	public long getBytesFichero(){
		return this.bytesFichero;
	}
	
	/**
	 * 
	 * Devuelve la fecha de última modificación del fichero
	 * 
	 * @return
	 */
	public long getUltimaModificacion(){
		return this.ultimaModificacion;
	}
}