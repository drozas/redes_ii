import java.io.FileInputStream;
import java.io.IOException;
public class LeeBytes {
	
public static void main (String[] args) throws IOException{

	if(args.length != 1){
		System.out.println("Debe especificar un nombre de fichero");
		System.exit(-1);
	}

	FileInputStream entrada = new FileInputStream(args[0]);

	int miByte;

	while( (miByte = entrada.read()) != -1){
		System.out.println("Se ha leido el byte " + (byte)miByte);
	}

}

}