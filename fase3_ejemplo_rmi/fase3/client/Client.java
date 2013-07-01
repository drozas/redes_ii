package fase3.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import fase3.remote.AlmacenRmi;

public class Client {
	
	public static void main(String[] args)throws RemoteException {
		if(args.length != 1){
			System.out.println("Debe especificar un número entero como parámetro");
					System.exit(-1);
		}
		
		Integer num = null;
		
		try{
			num = Integer.parseInt(args[0]);
		
		}catch(Exception e){
		
			System.out.println("El parámetro " + args[0] + " no es un entero válido");
					System.exit(-1);
		}
		
		//Preparamos el localizador
		String location = "//127.0.0.1:40000/RemoteObjectStringId";
		//Y una instancia nula
		AlmacenRmi ro = null;
		
		try{
			//Recuperamos el objeto remoto utilizando el nombre con el que lo
			//inscribió el servidor
			ro = (AlmacenRmi)Naming.lookup(location);
		}catch(Exception e){
			System.out.println("No ha sido posible recuperar el objeto dado por " +	location);
			e.printStackTrace();
			System.exit(-1);
		}
		
		//Y después lo utilizamos como un objeto cualquiera
		
		//Escribo un número, y veo los que tiene
		ro.pon(num);
		System.out.println("Los números puestos son los siguientes:");
		int pos = 0;
		
		while((num = ro.lee(pos)) != null)
		{
			pos ++;
			System.out.println("AlmacenRmi[" + pos +"]="+ num);
		}
	}
}