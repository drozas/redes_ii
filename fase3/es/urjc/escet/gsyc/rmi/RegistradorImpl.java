package es.urjc.escet.gsyc.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import es.urjc.escet.gsyc.p2p.tipos.DescriptorFichero;
import es.urjc.escet.gsyc.p2p.tipos.ListaFicheros;
import es.urjc.escet.gsyc.p2p.tipos.ListaUsuarios;
import es.urjc.escet.gsyc.p2p.tipos.Usuario;

//TODO: Cambiar el tipo de Usuario a un UsuarioBasic, con menos info

public class RegistradorImpl extends UnicastRemoteObject implements Registrador 
{
	
	//Estructuras de datos que mapean nick->usuario, clave->usuario
	private HashMap<String, Usuario> usuarios;
	private HashMap<String, String> claves;
	
	//Estructura de datos que mapea nick->directorioCompartido
	private HashMap<String, ListaFicheros> ficherosCompartidos;

	/**
	 * 
	 * Constructor
	 * 
	 * @throws RemoteException
	 */
	public RegistradorImpl() throws RemoteException 
	{
		usuarios = new HashMap<String, Usuario>();
		claves = new HashMap<String, String>();
		ficherosCompartidos = new HashMap<String, ListaFicheros>();
	}
	
	/**
	 * 
	 * 
	 * 
	 */
	public synchronized String registrar(Usuario usuario, String clave) throws RemoteException 
	{
		//Comprobación de usuario ya registrado
		if(usuarios.containsKey(usuario.getNick()))
		{
			String claveGuardada = claves.get(usuario.getNick());
			if(claveGuardada != null && claveGuardada.contentEquals(clave))
				return null;
			else
				return "Ya existe un usuario registrado con el nick: " + usuario.getNick();
		}else{
			//Si no está registrado, agregamos su info a nuestras tablas hash
			this.usuarios.put(usuario.getNick(), usuario);
			this.claves.put(usuario.getNick(), clave);
			System.out.println("Se ha registrado correctamente " + usuario.getNick());
			
			System.out.println("--- Lista de usuarios registrados ----");
			//Cargamos todos los valores de nuestra tabla hash en la lista
			Collection<Usuario> c = this.usuarios.values();
			Iterator<Usuario> iterador = c.iterator();

			while (iterador.hasNext())		
				System.out.println(iterador.next().getClave());
			
			
			return null;
		}
		
		
	}
	
	
	/**
	 * 
	 * 
	 * 
	 */
	public synchronized String exportarDirectorioCompartido(String nick, String clave, ListaFicheros ficheros) throws RemoteException 
	{
		
		//Comprobamos que el usuario exista, y su clave sea válida
		if(usuarios.get(nick) == null)
			return "No existe un usuario registrado con el nick: "+ nick + ". Imposible registrar tus ficheros.";
		if(claves.get(nick).contentEquals(clave))
		{
			//Si está en nuestras estructuras de datos, almacenamos su directorio compartido
			
			this.ficherosCompartidos.put(nick, ficheros);
			System.out.println("Se han agregado los ficheros compartidos por " + nick);
			
			Iterator<String> it = this.ficherosCompartidos.keySet().iterator();
			while (it.hasNext())
			{
				String nickKey = it.next();
				ListaFicheros listaFicheros = this.ficherosCompartidos.get(nickKey);
				System.out.println("--------Ficheros compartidos por" + nickKey + "-----------");
				System.out.println(listaFicheros.toString());
			}
			

			
			return null;
			
		} else {
			return "La clave especificada no es válida. Imposible registrar tus ficheros.";
		}
	}
	
	/**
	 * 
	 * 
	 * 
	 */
	public synchronized ListaUsuarios buscarArchivo(String busqueda) throws RemoteException 
	{
		
		//Creamos una lista de usuarios vacia, que contendrá los resultados de la búsqueda
		ListaUsuarios resultados = new ListaUsuarios();

		Iterator<String> it = this.ficherosCompartidos.keySet().iterator();
		while (it.hasNext())
		{

			String nickKey = it.next();
			ListaFicheros listaFicheros = this.ficherosCompartidos.get(nickKey);

			System.out.println("------- Buscando en los ficheros de " + nickKey);

			Boolean encontrado = false;
			int i = 1;
			while (i<listaFicheros.getNumFicheros() && (!encontrado))
			{
				DescriptorFichero fd = listaFicheros.getFichero(i);
				System.out.println("Comparando " + busqueda + " con " + listaFicheros.getFichero(i).getNombreFichero());

				if (hasAny(busqueda, listaFicheros.getFichero(i).getNombreFichero()))
				{
					//Si lo encontramos, añadimos el usuario y salimos del bucle
					resultados.addUsuario(this.usuarios.get(nickKey));
					encontrado = true;
				}


				i++;
			}
		}



		return resultados;

	}
	
	
	/**
	 * Devuelve true si la cadena tiene alguno de los elementos de la otra cadena
	 */
	 private static boolean hasAny(String elementos, String cadena)
	 {
	 
		 int length = elementos.length();
	  
		 for(int i=0; i<length; i++)
		 {
			 if(cadena.indexOf(elementos.charAt(i)) != -1)
				 return true;
		 }
	  
		 return false;
	 }
	
	
	
	/**
	 * 
	 * Método remoto que elimina al usuario de nuestras tablas hash.
	 * Devuelve null si todo fue correcto, o un string informando del error en
	 * caso contrario. 
	 * 
	 */
	public synchronized String darDeBaja(String nick, String clave) throws RemoteException 
	{
		//Comprobamos que el usuario exista, y su clave sea válida
		if(usuarios.get(nick) == null)
			return "No existe un usuario registrado con el nick: "+ nick;
		if(claves.get(nick).contentEquals(clave))
		{
			//Lo eliminamos de nuestras estructuras de datos.
			this.usuarios.remove(nick);
			this.claves.remove(nick);
			
			
			System.out.println("Se ha dado de baja correctamente " + nick);
			
			System.out.println("--- Lista de usuarios registrados ----");
			//Cargamos todos los valores de nuestra tabla hash en la lista
			Collection<Usuario> c = this.usuarios.values();
			Iterator<Usuario> iterador = c.iterator();

			while (iterador.hasNext())		
				System.out.println(iterador.next().getNick());
			
			
			return null;
			
		} else {
			return "La clave especificada no es válida";
		}
	}
	
	/**
	 * 
	 * Devuelve una lista de usuarios
	 * 
	 */
	public synchronized ListaUsuarios getTodos() throws RemoteException 
	{
		//Creamos la lista a devolver
		ListaUsuarios listaUsuarios = new ListaUsuarios();
		System.out.println("Me acaban de pedir lista de usuarios");

		//Cargamos todos los valores de nuestra tabla hash en la lista
		Collection<Usuario> c = this.usuarios.values();
		Iterator<Usuario> iterador = c.iterator();

		while (iterador.hasNext())		
			listaUsuarios.addUsuario(iterador.next());
		
		System.out.println(listaUsuarios.toString());
		
		return listaUsuarios;	
	
	}
	
	/**
	 * 
	 * Devuelve la clase usuario asociada a un nick
	 * 
	 */
	public synchronized Usuario getUsuario(String nick) throws RemoteException 
	{
		return usuarios.get(nick);
	}
	
	/**
	 * 
	 * Comprueba si el usuario existe en nuestras estructuras de datos
	 * 
	 */
	public synchronized Boolean existeNick(String nick) throws RemoteException 
	{
		return usuarios.containsKey(nick);
	}
}
