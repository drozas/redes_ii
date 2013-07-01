package persona;

import java.io.Serializable;

//Si no le decimos que implementa esa interfaz, cascara la parte que lo serializa
public class Persona implements Serializable{
	public String DNI;
	public String nombre;
	public String apellidos;
	public int edad;
	
	public Persona()
	{
	}
	
	public Persona (String DNI, String nombre, String apellidos, int edad) 
	{
		this.DNI = DNI;
		this.apellidos = apellidos;
		this.nombre = nombre;
		this.edad = edad;
	}
}