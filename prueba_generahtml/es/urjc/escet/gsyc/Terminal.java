package es.urjc.escet.gsyc;

import es.urjc.escet.gsyc.config.*;
import es.urjc.escet.gsyc.http.ServidorHttpDelTerminal;

public class Terminal {
		
	public static void main(String[] args) {

		if (args.length != 1){
			System.out.println("Modo de uso:");
			System.out.println("> java Terminal <fichero.cfg>");
			System.exit(-1);
		}
		
		try{
			LectorDeConfiguracion.LeeConfiguracionGeneral(args[0]);
		} catch (ConfigException e){
			System.out.println(e.getMensajeDeError());
			System.exit(-1);
		}
		
		ServidorHttpDelTerminal httpServer = 
			new ServidorHttpDelTerminal(ConfiguracionGeneral.getPuertoHttp());
		httpServer.start();
		
		while(true){
			try{
				Thread.sleep(1000);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
