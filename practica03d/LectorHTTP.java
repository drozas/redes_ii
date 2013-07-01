import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
;
public class LectorHTTP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub 

		/// Tenemos que capturar posibles excepciones
		try
		{
			// La clase FileWriter está especializada en lectura de ficheros en modo texto
			FileReader fr = new FileReader(args[0]);
			// Le enchufamos la clase BufferedReader, que tiene métodos tipo read, readln, etc.
			BufferedReader lectura = new BufferedReader(fr);
			
			//Mientras la lectura de cadena sea nula, escribimos por consola
			String cadena = new String();
			
			while((cadena=lectura.readLine()) != null)
			{
				System.out.println(cadena);
			}
		}catch (IOException ex)
		{
			System.out.println("Capturada excepción");
		}
	}

}
