package es.urjc.escet.gsyc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import es.urjc.escet.gsyc.p2p.tipos.ListaFicheros;
import es.urjc.escet.gsyc.p2p.tipos.ListaUsuarios;
import es.urjc.escet.gsyc.p2p.tipos.Usuario;

/**
 * 
 * Interfaz de la clase Registrador
 * 
 * @author drozas
 *
 */
public interface Registrador extends Remote 
{
    public String registrar(Usuario usuario, String clave) throws RemoteException;
    
    public String darDeBaja(String nick, String clave) throws RemoteException;
    
    public ListaUsuarios getTodos() throws RemoteException;
    
    public Usuario getUsuario(String nick) throws RemoteException;
    
	public Boolean existeNick(String nick) throws RemoteException;
	
	public String exportarDirectorioCompartido(String nick, String clave, ListaFicheros ficheros) throws RemoteException;
	
	public ListaUsuarios buscarArchivo(String busqueda) throws RemoteException; 

}
