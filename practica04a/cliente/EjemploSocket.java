package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EjemploSocket {
    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Debe especificar el host y el puerto del servidor");
            System.exit(-1);
        }
        String hostServidor = args[0];
        int puertoServidor = Integer.parseInt(args[1]);

        try{
            Socket socket = new Socket(hostServidor, puertoServidor);
            System.out.println("El cliente se ha conectado con éxito al servidor " + hostServidor + ":" + puertoServidor);
            PrintWriter salida = new PrintWriter(socket.getOutputStream());
            salida.print("GET /index.html HTTP/1.0\r\n");
            salida.println("\r\n");
            salida.flush();

            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            while( (line = entrada.readLine()) != null){
                System.out.println(line);
            }

            entrada.close();
            salida.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

