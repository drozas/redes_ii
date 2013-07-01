import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class LeeDoubles {
	
public static void main (String[] args) throws IOException{

	if(args.length != 1){
		System.out.println("Debe especificar un nombre de fichero");
		System.exit(-1);
	}

	FileInputStream entrada = new FileInputStream(args[0]);
	DataInputStream dis = new DataInputStream(entrada);

	double num;
	
	try{
		
	
		while(true)
		{
			num = dis.readDouble();
			System.out.println("Se ha leido el double " + num);
		}
	}catch(EOFException ex)
	{
		System.out.println("------------ Fin de lectura ------------");
	}

}

}