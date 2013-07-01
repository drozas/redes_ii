package fase3.server;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import fase3.remote.*;

public class Server {

	public static void main(String argv[])
	{
		System.out.println("Starting the server");
	
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new RMISecurityManager());
		}
		try{
			//El servidor se encarga de crear un objeto RMI, de forma que pueda ser
			//accesible por los clientes
			
			//Creamos una instancia del objeto remoto
			AlmacenRmi rm = new AlmacenRmiImpl();
			
			//Y la registramos en el servicio de nombres (si no ponemos puerto, 1099, x defecto)
			//De esta forma el objeto queda asociado a un nombre, que es por el que lo 
			//recuperarán los clientes. No pueden existir dos objetos con el mismo identificador
			Naming.rebind("//127.0.0.1:4567/RemoteObjectStringId",rm);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}