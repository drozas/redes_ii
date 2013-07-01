package msg;

import java.io.Serializable;

public class Peticion implements Serializable {
    private String emisor;
    private String asunto;
    private String cuerpo;

    public Peticion(String emisor, String asunto, String cuerpo){
        this.emisor = emisor;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
    }

    public String getEmisor(){
        return emisor;
    }
    public String getAsunto(){
        return asunto;
    }
    public String getCuerpo(){
        return cuerpo;
    }
}
