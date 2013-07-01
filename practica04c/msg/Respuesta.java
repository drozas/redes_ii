package msg;
import java.io.Serializable;

public class Respuesta implements Serializable {

    public static int OK = 0;
    public static int ERROR = -1;

    private int codigo;
    private String descripcion;

    public Respuesta(int codigo, String descripcion){
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo(){
        return codigo;
    }

    public String getDescripcion(){
        return descripcion;
    }

}