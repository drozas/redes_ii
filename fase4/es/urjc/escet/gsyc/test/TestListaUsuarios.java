package es.urjc.escet.gsyc.test;

import es.urjc.escet.gsyc.p2p.tipos.ListaUsuarios;
import es.urjc.escet.gsyc.p2p.tipos.Usuario;

public class TestListaUsuarios {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Usuario us1 = new Usuario(1234, "localhost", "/patatin/patatan", "pepe", "pepe del bosque", "lala@lala.net", "lllll");
		Usuario us2 = new Usuario(1234, "localhost", "/patatin/patatan", "pepe2", "pepe del bosque", "lala@lala.net", "lllll");
		
		ListaUsuarios lista = new ListaUsuarios();
		lista.addUsuario(us1);
		lista.addUsuario(us2);
		System.out.println(lista.toString());

	}

}
