import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EscribeBytes {
	
	private static byte[] lista ={1, 2, 10, 60};
	public static void main(String[] args) {

		if(args.length != 1){
			System.out.println("Debe especificar un nombre de fichero");
			System.exit(-1);
		}

		File miFichero = new File(args[0]);

		try{

			FileOutputStream salida = new FileOutputStream(miFichero);

			for(int i = 0; i < lista.length; i++){
				salida.write(lista[i]);
				salida.flush();
			}

			salida.close();

		} catch(IOException e) {

			System.out.println("Ha habido un problema de entrada y salidad producido por la excepción siguiente:");

			e.printStackTrace();

		}

	}
}