package es.urjc.escet.gsyc.html;

import es.urjc.escet.gsyc.html.internal.*;
import es.urjc.escet.gsyc.http.Constantes;

public class GeneradorHtml {
	public static StringBuilder generaRaiz(){
		Html page = new Html();
		
		page.setTile("PÁGINA DE REGISTRO DE LA APLICACIÓN P2P DE REDES-II");
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
		
		page.addHr();
		
		return page.getPage();
	}
}
