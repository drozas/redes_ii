import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import persona.Persona;


public class EscribePersona {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Escritura y lectura posterior de un objeto
		Persona mi_persona = new Persona("47456048X", "David", "Rozas Domingo", 23);
		
		try
		{
			//Creamos un fos, y se lo enchufamos a un ObjectOutputStream
			FileOutputStream fos = new FileOutputStream(args[0]);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			//Escribimos el objeto, y cerramos
			oos.writeObject(mi_persona);
			oos.close();
			
		}catch(FileNotFoundException ex)
		{
			System.out.println("No existe el fichero.");
		}catch(IOException ex)
		{
			System.out.println("Saltó una IOException");
		}
	}

}
