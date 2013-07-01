class ExcepcionChecked extends Exception{}
class ExcepcionUnchecked extends RuntimeException{}

public class EjemploExcepciones {
	public static void main(String[] args) {
		
		metodoUno();
	
		try{
			metodoDos();
	
		} catch(ExcepcionChecked e){
		
			/*System.out.println(e.getMessage());
			e.printStackTrace();*/
			System.out.println("Se capturó excepción Checked (prog/hay q indicar) que lanza el método 2 desde la clase");
			
	}
		try{
			metodoTres();
		} catch (ExcepcionUnchecked eu){
			System.out.println("Se capturó excepción Unchecked (no prog/no hay q indicar) que lanza el método 3 desde la clase");
			
			
		}
	}
	
	public static void metodoUno(){
	
		try{
			puedeLanzarChecked();
		} catch (ExcepcionChecked e) {
			/*System.out.println(e.getMessage());
			e.printStackTrace();*/
			System.out.println("Se capturó excepción Checked (prog/hay q indicar) desde el método uno");
		}
	}
	
	public static void metodoDos() throws ExcepcionChecked{
		puedeLanzarChecked();
	}
	
	public static void metodoTres(){
		puedeLanzarUnchecked();
	}
	
	public static void puedeLanzarChecked() throws ExcepcionChecked{
		throw new ExcepcionChecked();
	}

	public static void puedeLanzarUnchecked() {
		throw new ExcepcionUnchecked();
	}
}