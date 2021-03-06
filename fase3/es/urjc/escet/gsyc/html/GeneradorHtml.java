package es.urjc.escet.gsyc.html;

import java.util.ArrayList;
import java.util.Iterator;


import es.urjc.escet.gsyc.html.internal.*;
import es.urjc.escet.gsyc.html.internal.HtmlTableTd.AlignStyle;
import es.urjc.escet.gsyc.http.Constantes;
import es.urjc.escet.gsyc.p2p.tipos.ListaFicheros;
import es.urjc.escet.gsyc.p2p.tipos.ListaUsuarios;
import es.urjc.escet.gsyc.p2p.tipos.Usuario;
import es.urjc.escet.gsyc.p2p.tipos.Peer;

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
		case -3:
			p2.addText("<b>" + Constantes.E_EXPORTAR_FICHEROS +"</b>");
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
		
		//Preparamos los enlaces específicos a esta página
		ArrayList<EnlaceHtml> enlacesEspecificos = new ArrayList<EnlaceHtml>();
		
		//Enlace a continuar
		EnlaceHtml enlaceContinuar = new EnlaceHtml(Constantes.MENU + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial,
										"Continuar");
		
		enlacesEspecificos.add(enlaceContinuar);
		
		anadirPiePagina(page, enlacesEspecificos);
		
		
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
	public static StringBuilder generaMenu(Usuario usuario, String credencial){
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
		p2.addText("Conexión P2P: " + usuario.getHost() + ":" + usuario.getPuertoP2P());
		p2.addBr();
		
		
		//Preparamos los enlaces específicos a esta página
		ArrayList<EnlaceHtml> enlacesEspecificos = new ArrayList<EnlaceHtml>();
		
		//Enlaces específicos
		EnlaceHtml enlaceBusqueda = new EnlaceHtml(Constantes.BUSQUEDA + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial,
				"Buscar ficheros");
		
		EnlaceHtml enlaceDirLocal = new EnlaceHtml(Constantes.DIR_LOCAL + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial,
										"Ver fichero compartidos en " + usuario.getDirectorioExportado());
		
		EnlaceHtml enlaceListaTodos = new EnlaceHtml(Constantes.LISTA_TODOS + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial,
										"Ver todos los peers");
		
		EnlaceHtml enlaceMensajesRecibidos = new EnlaceHtml(Constantes.VER_MENSAJES + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial,
											"Ver mensajes recibidos");
		
		EnlaceHtml enlaceDesconexion = new EnlaceHtml(Constantes.DESCONEXION + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial,
										"Desconexión");

		enlacesEspecificos.add(enlaceBusqueda);
		enlacesEspecificos.add(enlaceDirLocal);
		enlacesEspecificos.add(enlaceListaTodos);
		enlacesEspecificos.add(enlaceMensajesRecibidos);
		enlacesEspecificos.add(enlaceDesconexion);

		anadirPiePagina(page, enlacesEspecificos);
		
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
	/*public static StringBuilder generaDirLocal(String directorio, String credencial){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "Directorio local");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("Ficheros compartidos en " + directorio);
		p.setAlign(HtmlP.AlignType.CENTER);
		
		//Modificación respecto a la fase 1, utilizamos la clase ListaFicheros
		ListaFicheros ficherosLocales = new ListaFicheros();
		ficherosLocales.CargarListaFicherosDirectorio(directorio);
		ficherosLocales.AddListadoFicheros(page);
		
		anadirPiePagina(page, credencial);
		
		return page.getPage();
	}*/	
	
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
	public static StringBuilder generaDirCompartido(String credencial, ListaFicheros listadoRemoto)
	{
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "Listado de ficheros compartidos");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("Listado de ficheros compartidos");
		p.setAlign(HtmlP.AlignType.CENTER);

		/*HtmlP p2 = page.addP();
		p2.setAlign(HtmlP.AlignType.LEFT);*/

		//Agregamos el litado remoto	
		listadoRemoto.AddListadoFicheros(page);
		
		/*//Preparamos los enlaces específicos a esta página
		ArrayList<EnlaceHtml> enlacesEspecificos = new ArrayList<EnlaceHtml>();
		
		//Enlace a lista todos
		EnlaceHtml enlace = new EnlaceHtml(Constantes.LISTA_TODOS + "?" + Constantes.CREDENCIAL_VAR
						+ "=" + credencial, "Volver a listado de todos los peers");

		enlacesEspecificos.add(enlace);*/
		
		anadirPiePagina(page, credencial);
		
		return page.getPage();
	}	
	

	/**
	 * 
	 * Genera la página en la que informa de que se ha hecho una petición en la 
	 * que alguno de los parámetros es incorrecto.
	 * 
	 * @return	Página HTML informando de que no se está conectado.
	 * 
	 */
	public static StringBuilder generaErrorObjetoRemoto(String descripcionError){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "¡Error!");
		page.addBr();
		HtmlP p = page.addP();
		p.setAlign(HtmlP.AlignType.CENTER);
		p.addText("Error: Hubo un error con el servidor central");
		p.addBr();
		p.addText("Descripción: " + descripcionError);
		anadirPiePaginaError(page);

		return page.getPage();
	}		
	
	/**
	 * 
	 * Genera la página en la que informa de que se ha hecho una petición en la 
	 * que alguno de los parámetros es incorrecto.
	 * 
	 * @return	Página HTML informando de que no se está conectado.
	 * 
	 */
	public static StringBuilder generaErrorParametros(){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "¡Error!");
		page.addBr();
		HtmlP p = page.addP();
		p.setAlign(HtmlP.AlignType.CENTER);
		p.addText("Error: faltan parámetros en la petición.");
		p.addBr();
		p.addText("Recuerda que para acceder a este recurso debes estar registrado");
		anadirPiePaginaError(page);

		return page.getPage();
	}	
	
	/**
	 * 
	 * Genera la página en la que informa de que se ha hecho una petición en la 
	 * que alguno de los parámetros es incorrecto.
	 * 
	 * @return	Página HTML informando de que no se está conectado.
	 * 
	 */
	public static StringBuilder generaErrorCredencial(){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "¡Error!");
		page.addBr();
		HtmlP p = page.addP();
		p.setAlign(HtmlP.AlignType.CENTER);
		p.addText("Error: la credencial utilizada no es válida.");
		p.addBr();
		p.addText("Recuerda que para acceder a este recurso debes estar registrado correctamente");
		anadirPiePaginaError(page);
		
		return page.getPage();
	}
	
	
	/**
	 * 
	 * Genera la página en la que informa de que el nick remoto no existe
	 * 
	 * @return	Página HTML informando de que no se está conectado.
	 * 
	 */
	public static StringBuilder generaErrorUsuarioRemotoNoValido(String credencial){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "¡Error!");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("El usuario remoto con el que quieres realizar la conexión no existe");
		p.setAlign(HtmlP.AlignType.CENTER);	
		
		//Preparamos los enlaces específicos a esta página
		ArrayList<EnlaceHtml> enlacesEspecificos = new ArrayList<EnlaceHtml>();
		
		//Enlace a lista todos
		EnlaceHtml enlace = new EnlaceHtml(Constantes.LISTA_TODOS + "?" + Constantes.CREDENCIAL_VAR
						+ "=" + credencial, "Volver a listado de todos los peers");

		enlacesEspecificos.add(enlace);
		
		anadirPiePagina(page, credencial, enlacesEspecificos);
		
		return page.getPage();
	}

	
	/**
	 * 
	 * Genera la página en la que informa de que el nick remoto no existe
	 * 
	 * @return	Página HTML informando de que no se está conectado.
	 * 
	 */
	public static StringBuilder generarErrorMensaje(String credencial){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "¡Error!");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("Has intentado escribir un mensaje sin asunto o sin texto.");
		p.setAlign(HtmlP.AlignType.CENTER);	
		
		//Preparamos los enlaces específicos a esta página
		ArrayList<EnlaceHtml> enlacesEspecificos = new ArrayList<EnlaceHtml>();
		
		//Enlace a lista todos
		EnlaceHtml enlace = new EnlaceHtml(Constantes.LISTA_TODOS + "?" + Constantes.CREDENCIAL_VAR
						+ "=" + credencial, "Volver a listado de peers");

		enlacesEspecificos.add(enlace);
		
		anadirPiePagina(page, credencial, enlacesEspecificos);
		
		return page.getPage();
	}
	
	/**
	 * 
	 * Genera la página informando de que el peer elevó una excepción
	 * 
	 * @param credencial	Credencial del usuario
	 * @param mensajeError	Mensaje de error generado por la excepción
	 * 
	 * @return	Página HTML informando del error
	 */
	public static StringBuilder generaErrorExcepcion(String credencial, String mensajeError){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "¡Error!");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("El peer elevó una excepción");
		p.addBr();
		p.addText("Detalles: " + mensajeError);
		p.setAlign(HtmlP.AlignType.CENTER);	
		
		//Preparamos los enlaces específicos a esta página
		ArrayList<EnlaceHtml> enlacesEspecificos = new ArrayList<EnlaceHtml>();
		
		//Enlace a lista todos
		EnlaceHtml enlace = new EnlaceHtml(Constantes.LISTA_TODOS + "?" + Constantes.CREDENCIAL_VAR
						+ "=" + credencial, "Volver a listado de todos los peers");

		enlacesEspecificos.add(enlace);
		
		anadirPiePagina(page, credencial, enlacesEspecificos);
		
		return page.getPage();
	}
	
	/**
	 * 
	 * Genera la página en la que se muestran todos los peers conectados
	 * 
	 * @param credencial	Credencial del usuario, necesaria para agregarla en los enlaces
	 * 						que se crearán en la página.
	 * 
	 * @return	Página HTML con peers conectados

	 * 
	 */
	public static StringBuilder generaListaTodos(String credencial,	ListaUsuarios listaUsuariosRemotos, String title, String descripcion){
		//TODO: Modificaciones fase 3, mostramos datos de ListaUsuarios que 
		// proviene de método remoto
		
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + title);
		page.addBr();
		HtmlP p = page.addP();
		p.addText(descripcion);
		p.setAlign(HtmlP.AlignType.CENTER);
		page.addHr();
		
	
		//Presentación de peers conectados con tabla
		if (listaUsuariosRemotos.size()>0)
		{
			
			int nFilas = listaUsuariosRemotos.size();
			HtmlTable table = page.addTable();

			
			HtmlTableTr[] tr = new HtmlTableTr[nFilas]; 
			HtmlTableTd[][] td = new HtmlTableTd[nFilas][3];
			
			//Creamos los trs
			for(int i = 0; i < nFilas; i++)
				tr[i] = table.addTr();
			
			//Creamos los tds con colores
			for(int i = 0; i < nFilas; i++)
				for(int j = 0; j < 3; j++)
				{
					td[i][j] = tr[i].addTd();
					td[i][j].setAlign(AlignStyle.CENTER);
					
					if (i%2!=0)
						td[i][j].setBgColor("F0E68C");
					
					else
						td[i][j].setBgColor("FFFFE0");
	
				}
			
			//Formato de la tabla
			table.setBorder(1);
			table.setBorderColor("00008B");
			table.setWidth(50);

			//Porcentaje de total de tabla de cada columna
			td[0][0].setWidth(40);
			
			td[0][1].setWidth(30);
			
			td[0][2].setWidth(30);
			
			//Escribimos tabla de peers
			//Iterator<Usuario> iterador = listaUsuariosRemotos.
			//TODO:¿Por qué no puedo a través de iterador?
			
			//int i = 0;
			//while (iterador.hasMoreElements())
			for(int i = 0; i<listaUsuariosRemotos.size(); i++)
			{
				//Peer peer = iterador.nextElement();
				Usuario usuario = listaUsuariosRemotos.getUsuario(i);
				//Escribrimos la información de usuario en columna 0
				td[i][0].addText(usuario.getNombreCompleto());
				td[i][0].addText(" ("+ usuario.getNick() + ")");
				td[i][0].addText(" en " + usuario.getHost() + ":" + usuario.getPuertoP2P());
				
				//Y los enlaces en 1 y 2				
				td[i][1].addA(Constantes.DIR_REMOTO + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial 
						+ "&" + Constantes.USUARIO_REMOTO_VAR + "=" + usuario.getNick(),
				"[Listar ficheros]");
				td[i][2].addA(Constantes.ESCRIBIR_MENSAJE + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial + 
						"&" + Constantes.USUARIO_REMOTO_VAR + "=" + usuario.getNick(),
					"[Escribir mensaje]");
			}
		
		}else{
			p.addBr();
			
			p.addText("No se halló ningún resultado");
			p.setAlign(HtmlP.AlignType.CENTER);
		}
	
		
	

		anadirPiePagina(page, credencial);

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
	public static StringBuilder generaDirRemoto(String credencial, ListaFicheros listadoRemoto,
					String nick){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "Acceso correcto");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("Ficheros compartidos por " + nick);
		p.setAlign(HtmlP.AlignType.CENTER);

		HtmlP p2 = page.addP();
		p2.setAlign(HtmlP.AlignType.LEFT);

		//Agregamos el litado remoto	
		listadoRemoto.AddListadoFicheros(page);
		
		//Preparamos los enlaces específicos a esta página
		ArrayList<EnlaceHtml> enlacesEspecificos = new ArrayList<EnlaceHtml>();
		
		//Enlace a lista todos
		EnlaceHtml enlace = new EnlaceHtml(Constantes.LISTA_TODOS + "?" + Constantes.CREDENCIAL_VAR
						+ "=" + credencial, "Volver a listado de todos los peers");

		enlacesEspecificos.add(enlace);
		
		anadirPiePagina(page, credencial, enlacesEspecificos);
		
		return page.getPage();
	}	
	
	/**
	 * 
	 * Genera la página que presenta el formulario de envío de mensajes
	 * 
	 * @param credencial	Credencial del usuario, necesaria para agregarla en los enlaces
	 * 						que se crearán en la página.
	 * @param nickRemoto	Nick del usuario remoto al que vamos a enviar el mensaje
	 * 
	 * @return	Página HTML con formulario de envío
	 * 
	 */
	public static StringBuilder generaEscribirMensaje(String credencial, String nickRemoto){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "Enviar mensaje a " + nickRemoto);
		page.addBr();
		HtmlP p = page.addP();
		p.addText("Para enviar un mensaje a "+ nickRemoto + ", escribe un asunto y un tema y pincha Enviar");
		p.setAlign(HtmlP.AlignType.CENTER);
		page.addHr();

		
		/*HtmlP p2 = page.addP();
		p2.setAlign(HtmlP.AlignType.CENTER);*/
		
		
		/**********************************************/
		/***** Nuevo formulario en tabla */
		
/*		int nFilas = 6;
		int nCol = 1;
		HtmlTable table = page.addTable();

		
		HtmlTableTr[] tr = new HtmlTableTr[nFilas]; 
		HtmlTableTd[][] td = new HtmlTableTd[nFilas][nCol];
		
		//Creamos los trs
		for(int i = 0; i < nFilas; i++)
			tr[i] = table.addTr();
		
		//Creamos los tds con colores
		for(int i = 0; i < nFilas; i++)
			for(int j = 0; j < nCol; j++)
			{
				td[i][j] = tr[i].addTd();
				td[i][j].setAlign(AlignStyle.CENTER);
			}
		
		
		HtmlForm f = td[0][0].addForm(Constantes.ENVIAR_MENSAJE);
		td[0][0].addText("Asunto:");
		f.addInputHidden(Constantes.CREDENCIAL_VAR, credencial);
		f.addInputHidden(Constantes.USUARIO_REMOTO_VAR, nickRemoto);
		td[1][0].setWidth(20);
		td[1][0].setHeight(30);
		td[1][0].addInputText(Constantes.ASUNTO_VAR);

		td[2][0].addText("Cuerpo:");
		td[3][0].setWidth(40);
		td[3][0].setHeight(80);
		td[3][0].addInputText(Constantes.CUERPO_VAR);
		//f.addTextArea(Constantes.CUERPO_VAR);

		td[4][0].addInputSubmit("Enviar");
		td[5][0].addInputReset("Borrar");*/
		
		
		
		
		
		/***************** Forma inicial      ***************/

/*		HtmlForm f = page.addForm(Constantes.ENVIAR_MENSAJE);
	
		//Añadimos los campos de formulario
		//TODO: Poner esto más bonito
		f.addInputHidden(Constantes.CREDENCIAL_VAR, credencial);
		f.addInputHidden(Constantes.USUARIO_REMOTO_VAR, nickRemoto);
		
		f.addText("Asunto: ");
		f.addInputText(Constantes.ASUNTO_VAR);
		f.addBr();
		f.addText("Cuerpo: ");
		f.addTextArea(Constantes.CUERPO_VAR);
		f.addBr();
		f.addInputSubmit("Enviar");*/
		/***************************************************/
		
		//TODO: Ver como encajar campos como textArea en tabla
		/********** Apaño intermedio ****************/
		int nFilas = 6;
		int nCol = 1;
		HtmlTable table = page.addTable();

		
		HtmlTableTr[] tr = new HtmlTableTr[nFilas]; 
		HtmlTableTd[][] td = new HtmlTableTd[nFilas][nCol];
		
		//Creamos los trs
		for(int i = 0; i < nFilas; i++)
			tr[i] = table.addTr();
		
		//Creamos los tds con colores
		for(int i = 0; i < nFilas; i++)
			for(int j = 0; j < nCol; j++)
			{
				td[i][j] = tr[i].addTd();
				td[i][j].setAlign(AlignStyle.CENTER);
			}
		
		
		HtmlForm f = td[0][0].addForm(Constantes.ENVIAR_MENSAJE);		
		f.addInputHidden(Constantes.CREDENCIAL_VAR, credencial);
		f.addInputHidden(Constantes.USUARIO_REMOTO_VAR, nickRemoto);
		
		f.addText("Asunto: ");
		f.addInputText(Constantes.ASUNTO_VAR);
		f.addBr();
		f.addText("Cuerpo: ");
		f.addBr();
		f.addTextArea(Constantes.CUERPO_VAR).setSize(60,20);
		
		f.addBr();
		f.addInputSubmit("Enviar");
		/***************************************************/
		
		
		
		//Preparamos los enlaces específicos a esta página
		ArrayList<EnlaceHtml> enlacesEspecificos = new ArrayList<EnlaceHtml>();
		
		//Enlace a lista todos
		EnlaceHtml enlace = new EnlaceHtml(Constantes.LISTA_TODOS + "?" + Constantes.CREDENCIAL_VAR
						+ "=" + credencial, "Volver a listado de todos los peers");

		enlacesEspecificos.add(enlace);
	
		anadirPiePagina(page, credencial, enlacesEspecificos);
		
		return page.getPage();
	}	
	
	
	
	/**
	 * 
	 * Genera la página con el resultado del envío del mensaje
	 * 
	 * @param credencial	Credencial del usuario, necesaria para agregarla en los enlaces
	 * 						que se crearán en la página.
	 * 
	 * @return	Página HTML con resultado de envío

	 * 
	 */
	public static StringBuilder generaEnviarMensaje(String credencial, String resultado){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "Resultado de envío de mensaje");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("Resultado de envío de mensaje: " + resultado);
		p.setAlign(HtmlP.AlignType.CENTER);

		//Preparamos los enlaces específicos a esta página
		ArrayList<EnlaceHtml> enlacesEspecificos = new ArrayList<EnlaceHtml>();
		
		//Enlace a lista todos
		EnlaceHtml enlace = new EnlaceHtml(Constantes.LISTA_TODOS + "?" + Constantes.CREDENCIAL_VAR
						+ "=" + credencial, "Volver a listado de todos los peers");

		enlacesEspecificos.add(enlace);
		
		anadirPiePagina(page, credencial, enlacesEspecificos);
		
		return page.getPage();
	}	
	
	
	
	/**
	 * 
	 * Genera la página que presenta el formulario de envío de mensajes
	 * 
	 * @param credencial	Credencial del usuario, necesaria para agregarla en los enlaces
	 * 						que se crearán en la página.
	 * @param nickRemoto	Nick del usuario remoto al que vamos a enviar el mensaje
	 * 
	 * @return	Página HTML con formulario de envío
	 * 
	 */
	public static StringBuilder generaBusqueda(String credencial){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "Realizar una búsqueda");
		page.addBr();
		HtmlP p = page.addP();
		p.setAlign(HtmlP.AlignType.CENTER);
		p.addText("Buscar ficheros en todos los peers conectados al servidor central");
		page.addHr();
		
		HtmlForm f = page.addForm(Constantes.RESULTADOS);		
		f.addInputHidden(Constantes.CREDENCIAL_VAR, credencial);
		
		f.addText("Buscar: ");
		f.addInputText(Constantes.PALABRA_BUSQUEDA);
		f.addBr();
		f.addInputSubmit("Ok");
		/***************************************************/
		
		
		
		//Preparamos los enlaces específicos a esta página
		ArrayList<EnlaceHtml> enlacesEspecificos = new ArrayList<EnlaceHtml>();
		
		//Enlace a lista todos
		EnlaceHtml enlace = new EnlaceHtml(Constantes.LISTA_TODOS + "?" + Constantes.CREDENCIAL_VAR
						+ "=" + credencial, "Volver a listado de todos los peers");

		enlacesEspecificos.add(enlace);
	
		anadirPiePagina(page, credencial, enlacesEspecificos);
		
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
	public static StringBuilder generaVerMensajes(String credencial, Peer peer){
		Html page = new Html();
		
		page.setTile(Constantes.TERMINAL + "Mensajes recibidos:");
		page.addBr();
		HtmlP p = page.addP();
		p.addText("Lista de mensajes recibidos por " + peer.getUsuario().getNombreCompleto() 
				+ " (" + peer.getUsuario().getNick() + ")");
		p.setAlign(HtmlP.AlignType.CENTER);
		page.addHr();
		peer.addListaMensajes(page);

		anadirPiePagina(page, credencial);
		
		return page.getPage();
	}	
	
	
	/**
	 * 
	 * Añade el pie de página estándar a una página con vuelta a menu y desconexión.
	 * (Método sobrecargado)
	 * 
	 * @param page	Página a la que añadiremos el pie
	 * @param credencial Credencial a añadir
	 */
	public static void anadirPiePagina(Html page, String credencial)
	{
		page.addHr();	
		HtmlP p = page.addP();
		p.setAlign(HtmlP.AlignType.CENTER);
		p.addA(Constantes.MENU + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial, "Volver al menú");
		p.addBr();
		p.addA(Constantes.DESCONEXION + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial, "Desconexión");
		page.addHr();
	}

	/**
	 * 
	 * Añade el pie de página estándar a una página, con menu, desconexión y enlaces
	 * específicos especificados en lista.
	 * (Método sobrecargado)
	 * 
	 * @param page	Página a la que añadiremos el pie
	 * @param credencial Credencial a añadir
	 * @param enlacesEspecificos Lista de enlaces específicos a añadir
	 * 
	 */
	public static void anadirPiePagina(Html page, String credencial, ArrayList<EnlaceHtml> enlacesEspecificos)
	{
		page.addHr();	
		HtmlP p = page.addP();
		p.setAlign(HtmlP.AlignType.CENTER);
		
		//Añadimos enlaces específicos a esa página, recorriendo la tabla hash
		if(enlacesEspecificos.size()>0)
		{
			//Necesitamos recorrerla, y obtener keys y valores
			Iterator<EnlaceHtml> iterador = enlacesEspecificos.iterator();
			
			while (iterador.hasNext())
			{
				EnlaceHtml enlace = iterador.next();
				p.addA(enlace.getEnlace(), enlace.getTextoEnlace());
				p.addBr();
			}

		}
		
		p.addA(Constantes.MENU + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial, "Volver al menú");
		p.addBr();
		p.addA(Constantes.DESCONEXION + "?"+ Constantes.CREDENCIAL_VAR + "=" + credencial, "Desconexión");
		page.addHr();
	}
	
	/**
	 * 
	 * Añade el pie de página estándar a una página, con enlaces  específicos
	 * especificados en lista.
	 * (Método sobrecargado)
	 * 
	 * @param page	Página a la que añadiremos el pie
	 * @param enlacesEspecificos Lista de enlaces específicos a añadir
	 * 
	 */
	public static void anadirPiePagina(Html page, ArrayList<EnlaceHtml> enlacesEspecificos)
	{
		page.addHr();	
		HtmlP p = page.addP();
		p.setAlign(HtmlP.AlignType.CENTER);
		
		//Añadimos enlaces específicos a esa página, recorriendo la tabla hash
		if(enlacesEspecificos.size()>0)
		{
			//Necesitamos recorrerla, y obtener keys y valores
			Iterator<EnlaceHtml> iterador = enlacesEspecificos.iterator();
			
			while (iterador.hasNext())
			{
				EnlaceHtml enlace = iterador.next();
				p.addA(enlace.getEnlace(), enlace.getTextoEnlace());
				p.addBr();
			}

		}
		
		page.addHr();
	}
	
	/**
	 * 
	 * Añade el pie de página estándar de páginas de errores con enlace de vuelta a raíz
	 * 
	 * @param page	Página a la que añadiremos el pie
	 * @param credencial Credencial a añadir
	 */
	public static void anadirPiePaginaError(Html page)
	{
		page.addHr();
		HtmlP p = page.addP();
		p.addA(Constantes.RAIZ_PATH, "Volver a página de registro");
		p.setAlign(HtmlP.AlignType.CENTER);	
		page.addHr();
		
	}
}
