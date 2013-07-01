package es.urjc.escet.gsyc.config;

public class ConfigException extends Exception {
	private static final long serialVersionUID = 3431169770122723125L;
	private String mensajeDeError;
	public ConfigException(){
		super();
		this.mensajeDeError = "";
	}
	public void setMensajeDeError(String mensajeDeError){
		this.mensajeDeError = mensajeDeError;
	}
	public String getMensajeDeError(){
		return this.mensajeDeError;
	}
}
