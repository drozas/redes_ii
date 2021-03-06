package es.urjc.escet.gsyc.p2p.tipos;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import es.urjc.escet.gsyc.html.internal.*;


/**
 * 
 * Clase que alberga un listado de ficheros
 * 
 * @author drozas
 *
 */
public class ListaFicheros implements Serializable{
	
	//Añadimos serialVerisonUID, generado con serialver
	private static final long serialVersionUID = 7834942390715969313L;
	
	private ArrayList<DescriptorFichero> listaFicheros;
	
	/**
	 * 
	 * Constructor vacío. Instancia la lista internamente.
	 * 
	 */
	public ListaFicheros()
	{
		this.listaFicheros = new ArrayList<DescriptorFichero>();
	}
	
	/**
	 * 
	 * Constructor. Permite que le pasemos una lista dada.
	 * 
	 * @param listaFicheros
	 */
	public ListaFicheros(ArrayList<DescriptorFichero> listaFicheros){
		if(listaFicheros == null)
			this.listaFicheros = new ArrayList<DescriptorFichero>();
		else
			this.listaFicheros = listaFicheros;
		
		Collections.sort(this.listaFicheros);
	}
	
	/**
	 * 
	 * Devuelve el nº de ficheros de ese directorio
	 * 
	 * @return	Número de ficheros
	 */
	public int getNumFicheros(){
		return this.listaFicheros.size();
	}
	
	/**
	 * 
	 * Devuelve el descriptor de fichero que está en la posición que le indicamos
	 * 
	 * @param pos
	 * @return	Fichero
	 */
	public DescriptorFichero getFichero(int pos){
		if(pos < 0 || pos >= this.listaFicheros.size())
			return null;
		return this.listaFicheros.get(pos);
	}
	
	/**
	 * 
	 * Carga en la lista de ficheros los ficheros del directorio que le indicamos.
	 * 	- Sólo carga archivos no ocultos
	 * 	- Borra el contenido anterior
	 * 
	 * @param directorio	Directorio del que queremos cargar los archivos
	 */
	public void CargarListaFicherosDirectorio(String directorio) throws ListaFicherosException
	{
		File dir = new File(directorio);
		//Si hay algo en la lista, lo borramos
		if (!this.listaFicheros.isEmpty())
			this.listaFicheros.clear();
		
		//Verificamos permisos
		if (dir.isDirectory() && dir.canRead())
		{
			File[] ficheros = dir.listFiles();
			
			//Cargamos todos los ficheros no oculotos
			if (ficheros != null)
			{
				for(int i=0; i< ficheros.length; i++)
				{
					if (ficheros[i].isFile() && !ficheros[i].isHidden())
					{
						DescriptorFichero descriptorAux = new DescriptorFichero(ficheros[i].getName(),
								ficheros[i].length(), ficheros[i].lastModified());		
						this.listaFicheros.add(descriptorAux);

					}
				}	
				
				//Y ordenamos
				Collections.sort(this.listaFicheros);
			}
		}else{
			//Si no tiene permisos o no es directorio, elevamos excepción.
			ListaFicherosException e = new ListaFicherosException();
			e.setMensajeDeError("Imposible acceder al directorio. Comprueba los permisos");
			
			throw e;
		}
		
	}
	
	/**
	 * 
	 * Devuelve una cadena con el contenido de la lista de ficheros con el siguiene formato:
	 * 		nombre	\t	tamaño	\t	ultimaModificacion \n
	 *
	 */
	public String toString()
	{
			
		Iterator<DescriptorFichero> iterador = listaFicheros.iterator();
		String listado = new String();
		
		while (iterador.hasNext())
		{
			DescriptorFichero descriptorAux = iterador.next();
			listado+= descriptorAux.getNombreFichero() + "\t";
			listado+= descriptorAux.getBytesFichero() + "\t";
			listado+= descriptorAux.getUltimaModificacion() + "\n";
		}
			
		return listado;

	}
	
	/**
	 * 
	 * Añade en la página Html el contenido del directorio en forma de tabla
	 * 
	 * @param page	Página a la que añadiremos el pie
	 * 
	 */
	public void AddListadoFicheros(Html page)
	{
		
		if (this.listaFicheros.size()>0)
		{
			//Crearemos una tabla con tantas filas como ficheros
			HtmlTable table = page.addTable();

			
			HtmlTableTr[] tr = new HtmlTableTr[this.listaFicheros.size() + 1]; 
			HtmlTableTd[][] td = new HtmlTableTd[this.listaFicheros.size() + 1][3];
			
			//Creamos los trs
			for(int i = 0; i < this.listaFicheros.size() + 1; i++)
				tr[i] = table.addTr();
			
			//Creamos los tds con colores
			for(int i = 0; i < this.listaFicheros.size() + 1; i++)
				for(int j = 0; j < 3; j++)
				{
					td[i][j] = tr[i].addTd();
					
					if (i==0)
						td[i][j].setBgColor("BDB76B");
					else
						td[i][j].setBgColor("EEE8AA");
				}
			
			//Formato de la tabla
			table.setBorder(1);
			table.setBorderColor("BC8F8F");

			//Cabecera de títulos
			td[0][0].addText("Nombre");
			td[0][0].setWidth(30);
			
			td[0][1].addText("Tamaño (en bytes)");
			td[0][1].setWidth(20);
			
			td[0][2].addText("Última modificación");
			td[0][2].setWidth(20);
			
			
			//Escribimos tabla de ficheros
			Iterator<DescriptorFichero> iterador = this.listaFicheros.iterator();
			int i = 1;
			while (iterador.hasNext())	
			{
				DescriptorFichero fichero = iterador.next();
				td[i][0].addText(fichero.getNombreFichero());
				td[i][1].addText(fichero.getBytesFichero() + "B");
				td[i][2].addText(fichero.getUltimaModificacion() +" ");
				i++;
			}
				

		}else{
			HtmlP p = page.addP();
			p.addText("No hay ficheros compartidos");
			p.setAlign(HtmlP.AlignType.CENTER);
		}
	}
	
	
}