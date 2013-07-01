package es.urjc.escet.gsyc.html;

import java.io.File;

import es.urjc.escet.gsyc.html.internal.*;
import es.urjc.escet.gsyc.http.Constantes;
import es.urjc.escet.gsyc.peer.Peer;

/**
 * 
 * Clase que crea las páginas HTML a devolver.
 * 
 * @author drozas
 *
 */
public class GeneradorHtml {
	
	/**
	 * 
	 * Genera la página raíz en función del parámetro error proveniente de la validación
	 * del usuario.
	 * 
	 * @param 	0: Página raíz estándar.
	 * 			-1: Muestra que hubo un error con la contraseña.
	 * 			-2: Muestra que hubo un error con el fichero de configuración del usuario.
	 * 
	 * @return	Página HTML de la raíz.
	 */
	public static StringBuilder generaRaiz(int error){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "PÁGINA DE REGISTRO DE LA APLICACIÓN P2P DE REDES-II");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("Bienvenido a la aplicación P2P de Redes-II");
		p.setAlign(HtmlP.AlignType.CENTER);
		page.addHr();
		
		//Creamos una tabla de 3x3
		HtmlTable table = page.addTable();
		HtmlTableTr[] tr = new HtmlTableTr[3]; 
		HtmlTableTd[][] td = new HtmlTableTd[3][3];
		
		//Creamos los trs
		for(int i = 0; i < 3; i++)
			tr[i] = table.addTr();
		
		//Creamos los tds
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				td[i][j] = tr[i].addTd();
		
		//Damos formato
		//table.setBorder(1);
		tr[0].setHeight(100);
		tr[1].setHeight(100);
		tr[1].setValign(HtmlTableTr.ValignStyle.BOTTOM);
		tr[2].setHeight(50);
		td[0][0].setWidth(35);
		td[0][1].setWidth(25);
		td[0][2].setWidth(40);
		
		//Creamos el formulario en el td central
		HtmlForm f = td[1][1].addForm(Constantes.RAIZ_PATH);
	
		//Creamos la tabla interna
		HtmlTable itable = f.addTable();
		HtmlTableTr[] itr = new HtmlTableTr[3];
		for(int i = 0; i < 3; i++)
			itr[i] = itable.addTr();
		
		HtmlTableTd[][] itd = new HtmlTableTd[3][2];
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 2; j++)
				itd[i][j] = itr[i].addTd();
		
		//Damos formato a la tabla internat
		//itable.setBorder(1);
		
		itd[0][0].setWidth(25);
		//Añadimos los campos de formulario
		itd[0][0].addText("Nick P2P: ");
		itd[0][1].addInputText(Constantes.NICK_VAR);
		itd[1][0].addText("Contraseña: ");
		itd[1][1].addInputPassword(Constantes.CLAVE_VAR);
		
		itr[2].setHeight(40);
		itr[2].setValign(HtmlTableTr.ValignStyle.BOTTOM);
		itd[2][0].addInputSubmit("  Enviar  ");
		itd[2][0].setAlign(HtmlTableTd.AlignStyle.CENTER);
		itd[2][1].addInputReset("  Borrar  ");
		itd[2][1].setAlign(HtmlTableTd.AlignStyle.CENTER);
		
		//drozas: modifico para mostrar distintos tipos de errores
		HtmlP p2 = page.addP();
		p2.setAlign(HtmlP.AlignType.CENTER);

		switch (error) {
		case -1:
			p2.addText("<b>" + Constantes.E_CLAVE +"</b>");
			break;
		case -2:
			p2.addText("<b>" + Constantes.E_FICH_USUARIO +"</b>");
			break;
		}
		
		page.addHr();
		
		return page.getPage();
	}
	
	
	/**
	 * 
	 * Genera la página en la que se pide confirmación para el acceso al menú.
	 * 
	 * @param credencial	Credencial del usuario, necesaria para agregarla en los enlaces
	 * 						que se crearán en la página.
	 * 
	 * @return	Página HTML de confirmación del proceso de validación.
	 * 
	 * TODO: Redirigir automáticamente a página de menú transcurridos unos segundos.
	 * 
	 */
	public static StringBuilder generaConfirmacionValidacion(String credencial){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "Acceso correcto");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("El acceso es correcto. Pincha en continuar para acceder a la interfaz.");
		p.setAlign(HtmlP.AlignType.CENTER);
		page.addHr();
		HtmlP p2 = page.addP();
		p2.setAlign(HtmlP.AlignType.CENTER);
		p2.addA("/menu?credencial=" + credencial,"Continuar");
		page.addHr();
		return page.getPage();
	}	
	
	/**
	 * 
	 * Genera la página HTML con el menú.
	 * 
	 * @param usuario	Clase con toda la información del usuario.
	 * 
	 * @param credencial	Credencial del usuario, necesaria para agregarla en los enlaces
	 * 						que se crearán en la página.
	 * 
	 * @return	Página con el menú principal de la aplicación
	 */
	public static StringBuilder generaMenu(Peer usuario, String credencial){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "Menú de la aplicación");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("Bienvenido al menu de la aplicación");
		p.setAlign(HtmlP.AlignType.CENTER);
		page.addHr();
	
		HtmlP p2 = page.addP();
		p2.setAlign(HtmlP.AlignType.RIGHT);
		p2.addText("Información del usuario " + usuario.getNick());
		p2.addBr();		
		p2.addText("Nombre: " + usuario.getNombreCompleto());
		p2.addBr();
		p2.addText("E-mail: " +usuario.getCorreoElectronico());
		p2.addBr();
		/*p2.addText("Directorio exportado: " +usuario.getDirectorioExportado());
		p2.addBr();*/
		p2.addText("Puerto: " +usuario.getPuertoP2P());
		p2.addBr();
		page.addHr();
		page.addHr();
		
		HtmlP p3 = page.addP();
		p3.setAlign(HtmlP.AlignType.CENTER);
		p3.addA("/dir_local?credencial=" + credencial, "Ver fichero compartidos en " + usuario.getDirectorioExportado());
		p3.addBr();
		p3.addA("/desconexion?credencial=" + credencial, "Desconexión");
		page.addHr();
		return page.getPage();
	}	
	
	/**
	 * 
	 * Genera la página en la que se listan los ficheros compartidos por el usuario.
	 * 
	 * @param directorio	Directorio a exportar por el usuario
	 * 
	 * @param credencial	Credencial del usuario, necesaria para agregarla en los enlaces
	 * 						que se crearán en la página.
	 * 
	 * @return	Página con los ficheros compartidos.
	 */
	public static StringBuilder generaDirLocal(String directorio, String credencial){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "Directorio local");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("Ficheros compartidos en " + directorio);
		p.setAlign(HtmlP.AlignType.CENTER);
		page.addHr();
		HtmlP p2 = page.addP();
		p2.setAlign(HtmlP.AlignType.LEFT);
		File dir = new File(directorio);
		
		//Esto se verifica al cargar el fichero de configuración, pero por si acaso
		if (dir.isDirectory() && dir.canRead())
		{
			File[] ficheros = dir.listFiles();
			
			//Mostramos todos los ficheros no ocultos del directorio
			if (ficheros != null)
			{
				for(int i=0; i< ficheros.length; i++)
					if (ficheros[i].isFile() && !ficheros[i].isHidden())
					{
						p2.addText(ficheros[i].getName());
						p2.addBr();
					}	
			}
		}
		
		page.addHr();
		page.addHr();
		HtmlP p3 = page.addP();
		p3.setAlign(HtmlP.AlignType.CENTER);
		p3.addA("/menu?credencial=" + credencial, "Volver al menú");
		p3.addBr();
		p3.addA("/desconexion?credencial=" + credencial, "Desconexión");
		page.addHr();
		
		return page.getPage();
	}	
	
	
	/**
	 * 
	 * Genera la página en la que informa de que se requiere estar validado
	 * 
	 * @return	Página HTML informando de que no se está conectado.
	 * 
	 */
	public static StringBuilder generaNoConectado(){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "¡Error!");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("Para acceder a dicho recurso, debes registrarte.");
		p.setAlign(HtmlP.AlignType.CENTER);	
		return page.getPage();
	}	
	
}
