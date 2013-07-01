public class EjemploExcepciones {
	
	public static void main(String[] args) {
		if (args.length != 1)
			System.out.println("Debe introducir un entero a la entrada");

		try{
			int enteroLeido = stringToInt(args[0]);
			System.out.println("Ha introducido el entero " + enteroLeido);
		}catch (NumberFormatException e){
			System.out.println("La cadena no era un entero. Se capturó la excepción.");
		}
		
		System.out.println("El programa sigue su ejecución");


}

private static int stringToInt(String entero) {
	int resultado = Integer.parseInt(entero);
	return resultado;
}
}