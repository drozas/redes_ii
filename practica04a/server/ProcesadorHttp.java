package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//Existen dos formas de crear threads:
// - Implementando una interfaz (por si queremos heredar)
// - Heredando de la clase thread, como en este caso. Para este caso basta con
//		- Crear una clase que redefina el método Run
//		- Se le llamará con start, en el "pp"
public class ProcesadorHttp extends Thread{

	//Nuestra clase que hereda de thread. Sólo contiene un atributo privado conn de tipo Socket,
	// que es el que crea la clase ServerSocket y que nos pasarán
    private Socket conn;

    public ProcesadorHttp(Socket conn){
        this.conn = conn;
    }

    public void run(){
        try{
        	
        	//El resto, es similar al proceso anterior...
        	
        	//Enchufamos el Socket a un InputStreamReader, y este a un BufferedReader
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            //De esta forma, leemos la petición del cliente
            String line;
            while( (line = entrada.readLine()) != null){
                System.out.println(line);
                if(line.equals(""))
                    break;
            }

            //Construimos una página, con el número de peticiones
            StringBuilder page = new StringBuilder("");
            page.append("<html>");
            page.append("<head>");
            page.append("<title>PAGINA DE PRUEBA</title>");
            page.append("</head>");
            page.append("<body>");
            page.append("<hr><p align=\"center\"> Hola, has solicitado la pagina " +
                    ServidorMultiThread.getNumPeticiones()+ " veces</p><hr>");
            ServidorMultiThread.incrNumPeticiones();
            page.append("<body>");
            page.append("</html>");

            //Y para enviarla al cliente, conectamos nuestro socket de datos a un PrintWriter
            PrintWriter salida = new PrintWriter(conn.getOutputStream());
            salida.print("HTTP/1.0 200 OK\r\n");
            salida.print("Content-type: text/html\r\n");
            salida.print("Content-length: " + page.length() + "\r\n");
            salida.print("\r\n");
            salida.print(page);

            //Cerramos los streams
            salida.close();
            entrada.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}