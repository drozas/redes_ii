package es.urjc.escet.gsyc.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import es.urjc.escet.gsyc.html.GeneradorHtml;

public class GestorDePeticionesHttp extends Thread {
	private Socket socket;
	
	public GestorDePeticionesHttp(Socket socket){
		this.socket = socket;
	}
	
	private StringBuilder procesaPeticionPaginaDesconocida(){
		StringBuilder page = new StringBuilder("");
		page.append("<html><body>El recurso solicitado no se encuentra en el servidor </body></html>");
		return page;
	}
	
	private StringBuilder procesaPeticionPaginaRaiz(){
		return GeneradorHtml.generaRaiz();
	}
		
	private StringBuilder generaMensaje200 (StringBuilder htmlPage) {
		StringBuilder respuesta = new StringBuilder("");
		respuesta.append("HTTP/1.1 200 OK\r\n");
		respuesta.append("Connection: close\r\n");
		respuesta.append("Content-Type: text/html\r\n");
		respuesta.append("Content-Length: " + htmlPage.length() + "\r\n");
		respuesta.append("\r\n");
		respuesta.append(htmlPage);
		return respuesta;
	}

	private StringBuilder generaMensaje404(StringBuilder htmlPage) {
		StringBuilder respuesta = new StringBuilder("");
		respuesta.append("HTTP/1.1 404 Not Found\r\n");
		respuesta.append("Connection: close\r\n");
		respuesta.append("Content-Type: text/html\r\n");
		respuesta.append("Content-Length: " + htmlPage.length() + "\r\n");
		respuesta.append("\r\n");
		respuesta.append(htmlPage);
		return respuesta;
	}
	
	private StringBuilder generaRespuesta(String path, Map<String, String> vars){
		
		if(path.contentEquals(Constantes.RAIZ_PATH))
			return generaMensaje200(procesaPeticionPaginaRaiz());
		
		return generaMensaje404(procesaPeticionPaginaDesconocida());
	}
		
	public void run(){
		try{
			//Recuperamos la petición del cliente
			BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			
			//Leemos la primera línea (línea de petición HTTP) que es obligatoria
			String firstLine = in.readLine();
			System.out.println(firstLine);
						
			//Leemos resto de cabeceras, por si nos interesan. Sólo trabajamos con GET, por lo que no hay cuerpo
			String line = null;
			while( (line=in.readLine()) != null){
				//Si la línea está en blanco, entonces se han terminado las cabeceras
				if(line.contentEquals(""))
					break;
			}
				
			//Recuperamos el recuros solicitado y las variables GET presentes
			String path = AnalizadorHttpGet.getPath(firstLine);
			Map<String, String> vars = AnalizadorHttpGet.getVars(firstLine);
			
			//Procedemos a construir la respuesta
			StringBuilder respuesta = generaRespuesta(path, vars);
			
			PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			out.print(respuesta.toString());
			out.close();
			in.close();
			
		} catch (IOException e) {
			System.out.println("AVISO: ha habido un problema leyendo o escribiendo en el socket especificado");
			System.out.println("Los detalles del problema son los siguientes");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
	}

}
