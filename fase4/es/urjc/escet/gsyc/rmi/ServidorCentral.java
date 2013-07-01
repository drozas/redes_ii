package es.urjc.escet.gsyc.rmi;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

import es.urjc.escet.gsyc.rmi.Registrador;
import es.urjc.escet.gsyc.rmi.RegistradorImpl;


/**
 * 
 * Servidor central
 * 
 * @author drozas
 *
 */
public class ServidorCentral 
{
	public static final String REGISTRADOR = "Registrador";
	
     public static void main(String[] args) 
     {
     
    	 if(args.length != 2)
    	 {
    		 System.out.println("Debe especificar el host y el puerto del servidor de nombres RMI");
    		 System.exit(-1);
    	 }
    	 
         
    	 String rmiHost = args[0];
         short rmiPort = (short)Integer.parseInt(args[1]);
         
    	 if(System.getSecurityManager()==null)
    	 {
         	 System.setSecurityManager(new RMISecurityManager());  
    	 }
         
    	 System.out.println("Lanzando el servidor central en " + rmiHost + ":" + rmiPort);
 			
    	 try{
			//El servidor se encarga de crear un objeto RMI, de forma que pueda ser
			//accesible por los clientes
			
			//Creamos una instancia del objeto remoto
			Registrador registrador = new RegistradorImpl();
			
			String pathRmiRegistry ="//" + rmiHost + ":" + rmiPort + "/" + REGISTRADOR;
			Naming.rebind(pathRmiRegistry,registrador);
			
		} catch(Exception e){
			e.printStackTrace();
		}
         
     }
}
