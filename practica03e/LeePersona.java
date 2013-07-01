import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import persona.Persona;


public class LeePersona {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Creamos una instancia de persona vacía
		Persona mi_persona = new Persona();
		
		//Creamos un fis y se lo enchufamos a un ObjectInputStream
		try
		{
			FileInputStream fis = new FileInputStream(args[0]);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			mi_persona = (Persona)ois.readObject();
			
			System.out.println("DNI : "  + mi_persona.DNI);
			System.out.println("Nombre: " + mi_persona.nombre );
			System.out.println("Apellidos: " + mi_persona.apellidos);
			System.out.println("Edad: " + mi_persona.edad);
			
		}catch (FileNotFoundException ex)
		{
			System.out.println("El fichero no existe");
		}catch (IOException ex)
		{
			System.out.println("Saltó IOException");
		}catch (ClassNotFoundException ex)
		{
			System.out.println("No se encuentra la clase");
		}

	}

}
