public class AccesoConcurrente {

    public static void main(String[] args) {
        Compartido c = new Compartido();
        HiloPonedor ponedor = new HiloPonedor(c);
        HiloLector lector = new HiloLector(c);

        Thread thPonedor = new Thread(ponedor);
        Thread thLector = new Thread(lector);

        thPonedor.start();
        thLector.start();

    }

}

class Mensaje {

    public static final String EJEMPLO_EMISOR = "E";
    public static final String EJEMPLO_RECEPTOR = "R";
    public static final String EJEMPLO_ASUNTO = "A";
    public static final String EJEMPLO_CUERPO ="C";

    private String emisor;
    private String receptor;
    private String asunto;
    private String cuerpo;

    public synchronized void setEmisor(String emisor){
        this.emisor = emisor;
    }
    public synchronized void setReceptor(String receptor){
        this.receptor = receptor;
    }
    public synchronized void setAsunto(String asunto){
        this.asunto = asunto;
    }
    public synchronized void setCuerpo(String cuerpo){
        this.cuerpo = cuerpo;
    }

    public synchronized String getEmisor(){
        return emisor;
    }
    public synchronized String getReceptor(){
        return receptor;
    }
    public synchronized String getAsunto(){
        return asunto;
    }
    public synchronized String getCuerpo(){
        return cuerpo;
    }

    public boolean equals(Mensaje m){

        if(emisor == null || receptor == null || asunto == null || cuerpo == null)
            return false;

        if(emisor.contentEquals(m.emisor) &&
           receptor.contentEquals(m.receptor) &&
           asunto.contentEquals(m.asunto) &&
           cuerpo.contentEquals(m.cuerpo))
            return true;
        else
            return false;
    }

    public String toString(){
        return emisor + " " + receptor + " " + asunto + " " + cuerpo;
    }
}

class Compartido {
    private Mensaje mensaje;
    public synchronized void ponMensaje(Mensaje m){
        this.mensaje = new Mensaje();
        this.mensaje.setEmisor(m.getEmisor());
        this.mensaje.setReceptor(m.getReceptor());
        this.mensaje.setAsunto(m.getAsunto());
        this.mensaje.setCuerpo(m.getCuerpo());
    }

    public Mensaje leeMensaje(){
        Mensaje m = new Mensaje();
        m.setEmisor(mensaje.getEmisor());
        m.setReceptor(mensaje.getReceptor());
        m.setAsunto(mensaje.getAsunto());
        m.setCuerpo(mensaje.getCuerpo());
        return m;
    }
}

class HiloPonedor implements Runnable {
    private Compartido c;

    public HiloPonedor(Compartido c){
        this.c = c;
    }
    public synchronized void run(){
        Mensaje ejemplo = new Mensaje();
        ejemplo.setEmisor(Mensaje.EJEMPLO_EMISOR);
        ejemplo.setReceptor(Mensaje.EJEMPLO_RECEPTOR);
        ejemplo.setAsunto(Mensaje.EJEMPLO_ASUNTO);
        ejemplo.setCuerpo(Mensaje.EJEMPLO_CUERPO);

        while(true){
            c.ponMensaje(ejemplo);
        }
    }
}

class HiloLector implements Runnable {
    private Compartido c;

    public HiloLector(Compartido c){
        this.c = c;
    }

    public synchronized void run(){
        Mensaje ejemplo = new Mensaje();
        ejemplo.setEmisor(Mensaje.EJEMPLO_EMISOR);
        ejemplo.setReceptor(Mensaje.EJEMPLO_RECEPTOR);
        ejemplo.setAsunto(Mensaje.EJEMPLO_ASUNTO);
        ejemplo.setCuerpo(Mensaje.EJEMPLO_CUERPO);
        int total = 0;
        int erroneo = 0;
        while(true){
            total++;
            Mensaje mensaje = c.leeMensaje();
            if(!mensaje.equals(ejemplo)){
                erroneo++;
                System.out.println("Leido:    " + mensaje.toString());
                System.out.println("Esperado: " + ejemplo.toString());
                double ratio = (double)erroneo / (double)total;
                System.out.println("Tasa: " + ratio);
                System.out.println();
            }
        }
    }
}