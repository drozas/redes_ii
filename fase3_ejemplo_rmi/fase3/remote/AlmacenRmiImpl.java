package fase3.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/*Después creamos la clase remota, que implementa la interfaz definida*/

public class AlmacenRmiImpl extends UnicastRemoteObject implements AlmacenRmi
{
	/* El objeto extiende la clase UnicastRemoteObject, que es la que implementa
	 * la mecánica de comunicación que requiere el objeto RMI
	 */
	
	List<Integer> lista;

	
	public AlmacenRmiImpl() throws RemoteException {
		lista = new ArrayList<Integer>();
	}
	
	/*Los métodos pon y lee son synchronized ya que la máquina virtual de java 
	 * no garantiza nada sobre concurrencia.
	 * Así que para no dejar los datos en un estado inconsistente, evitamos el acceso
	 * a lecturas y escrituras.
	 */
	public synchronized void pon(Integer num) throws RemoteException{
		lista.add(num);
	}
	
	public synchronized Integer lee(Integer pos) throws RemoteException{
		if(pos < 0 || pos >= lista.size())
			return null;
		return lista.get(pos);
	}
}