import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class EscribeEnteros {
	
	public static void main(String[] args) {

		if(args.length != 1){
			System.out.println("Debe especificar un nombre de fichero");
			System.exit(-1);
		}

		File miFichero = new File(args[0]);

		try{

			FileOutputStream salida = new FileOutputStream(miFichero);
			
			// Conectamos el FileOutputStream con un DataOuputStream
			DataOutputStream dos = new DataOutputStream(salida);
			
			// Creamos un generador de numeros aleatorios
			Random generador = new Random();
			for(int i = 0; i < 100; i++){
				dos.writeInt(generador.nextInt(100));
				//salida.flush();
			}

			dos.close();

		} catch(IOException e) {

			System.out.println("Ha habido un problema de entrada y salidad producido por la excepción siguiente:");
			e.printStackTrace();

		}

	}
}