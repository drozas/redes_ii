package es.urjc.escet.gsyc.p2p;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import es.urjc.escet.gsyc.p2p.tipos.Peer;

/**
 * 
 * 
 * 
 * @author drozas
 *
 */
public class ServidorDeFicherosDelPeer extends Thread 
{
	private ServerSocket serverSocket;
	private String fileNameFullPath;
	
	/**
	 * 
	 * @param peer	Peer local, en el que esperamos recibir el fichero
	 * @param serverSocket	Socket creado por el peer local
	 * @param fileName	Nombre de fichero a descargar
	 */
	public ServidorDeFicherosDelPeer(Peer peer,	ServerSocket serverSocket, String fileName)
	{
		this.serverSocket = serverSocket;
		this.fileNameFullPath =	peer.getUsuario().getDirectorioExportado()+ "/" + fileName;
		
	}
	public void run(){
		//Aceptamos una sola conexión y, a través de ella, el fichero
		
		try
		{
			Socket conn = serverSocket.accept();
			FileOutputStream os = new FileOutputStream(this.fileNameFullPath);
			DataInputStream is = new DataInputStream(conn.getInputStream());
			
			//Leemos en trozos de 1 kb
			byte[] buffer = new byte[1048];
			int num;
			
			while( (num = is.read(buffer)) > 0 )
			{
				os.write(buffer, 0, num);
			}
			
			os.close();
			is.close();
			serverSocket.close();
		}catch (IOException e){
			//Problemas en el establecimiento de la conexión
			//Quizás otras conexiones puedan funcionar,	no hacemos exit
			//De todos modos, informamos de que algo no ha ido bien
			System.out.println("AVISO: Se ha producido un error en la transmisión del fichero");
			System.out.println("Detalles del problema:");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}