package fase3.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*Los métodos de un objeto remoto en Java tienen que ser definidos en una interfaz*/

public interface AlmacenRmi extends Remote 
//Heredamos de Remote, con esto indicamos que las clases que implementen esta interfaz tendrán como instancias objetos remotos
{

	public void pon(Integer num) throws RemoteException;
	
	public Integer lee(Integer pos) throws RemoteException;

}
