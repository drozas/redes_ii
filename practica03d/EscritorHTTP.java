import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
public class EscritorHTTP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub 

		/// Tenemos que capturar posibles excepciones
		try
		{
			// La clase FileWriter está especializada en ficheros de texto
			FileWriter fw = new FileWriter(args[0]);
			// Le enchufamos la clase PrintWriter, que tiene métodos tipo print, println, etc.
			PrintWriter salida = new PrintWriter(fw);
		
			salida.print("GET " + args[1] + " HTTP/1.1\r\n");
			salida.print("Host: " + args[2] + "\r\n");
			salida.print("Connection: close\r\n");
			salida.print("\r\n");
			
			//Hay que cerrar para hacer efectivos los cambios
			salida.close();
			
		}catch(IOException ex)
		{
			System.out.println("Ocurrión una excepción de entrada/salida");
		}
	}

}
