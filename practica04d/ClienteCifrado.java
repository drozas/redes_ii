import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import msg.Peticion;
import msg.Respuesta;

public class ClienteCifrado {
	public static void main(String[] args) {
		try{
			int port = 6789;
			String hostname = "localhost";
			
			//Creamos una factoría SSL
			SocketFactory socketFactory = SSLSocketFactory.getDefault();
			//Y a partir de ella creamos el socket
			Socket socket = socketFactory.createSocket(hostname, port);
			
			//El resto, es como en el ejemplo anterior
			System.out.println("Socket connected to " + hostname + ":" + port);
			ObjectOutputStream out = new
			ObjectOutputStream(socket.getOutputStream());
			Peticion peticion = new Peticion("El Cliente", "Mensaje dePrueba", "Este es un mensaje que viaja sobre una conexión cifrada y cuyos contenidos has sido firmados digitalmente");
			out.writeObject(peticion);
			out.flush();
			System.out.println("The message has been sent ...");
			ObjectInputStream in = new
			ObjectInputStream(socket.getInputStream());
			Respuesta respuesta = (Respuesta)in.readObject();
			System.out.println("Response received");
			System.out.println("Response code" + respuesta.getCodigo());
			System.out.println("Response description " +
					respuesta.getDescripcion());
			in.close();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
