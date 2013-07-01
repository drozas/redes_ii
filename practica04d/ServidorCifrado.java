import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import msg.Peticion;
import msg.Respuesta;

public class ServidorCifrado {
	public static void main(String[] args) {
		try{
			int port = 6789;
			//Creamos una "factoría" ssl
			ServerSocketFactory ssocketFactory =
				SSLServerSocketFactory.getDefault();
			
			//Y creamos un ServerSocket a partir de dicha factoría
			ServerSocket ssocket =
				ssocketFactory.createServerSocket(port);
			
			System.out.println("ServerSocket created in port " + port);
//			Listen for connections
			while(true){
				Socket socket = ssocket.accept();
				System.out.println("Connection established");
				Gestor gestor = new Gestor(socket);
				gestor.start();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}

//El resto, es el mismo ejemplo que el que hicimos de prueba
class Gestor extends Thread {
	private Socket sslSocket;
	public Gestor(Socket sslSocket){
		this.sslSocket=sslSocket;
	}
	public void run(){
		try{
//			Lee la petición del cliente
			ObjectInputStream in = new
			ObjectInputStream(sslSocket.getInputStream());
			Peticion peticion = (Peticion) in.readObject();
			System.out.println("Se ha recibido el mensaje siguiente:");
			System.out.println("Emisor: " + peticion.getEmisor());
			System.out.println("Asunto: " + peticion.getAsunto());
			System.out.println("Cuerpo: " + peticion.getCuerpo());
			Respuesta respuesta = new Respuesta(Respuesta.OK, "OK");
			ObjectOutputStream out = new
			ObjectOutputStream(sslSocket.getOutputStream());
			out.writeObject(respuesta);
			out.close();
			in.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}