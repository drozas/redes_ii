package es.urjc.escet.gsyc.p2p.tipos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

//TODO: Esta lista de usuarios, será del tipo UsuarioBasic, con informacion limitada

public class ListaUsuarios implements Serializable 
{
    private ArrayList<Usuario> lista;

    public ListaUsuarios(ArrayList<Usuario> lista)
    {
         if(lista == null)
              this.lista = new ArrayList<Usuario>();
         this.lista = lista;
    }

    /*Constructor vacío*/
    public ListaUsuarios()
    {
    	this.lista = new ArrayList<Usuario>();
    }
    
    public int size()
    {
       return lista.size();
    }
    
    public Usuario getUsuario(int i)
    {
       if(i < 0 || i >= lista.size())
            return null;
       else
            return lista.get(i);
    }
    
    public void addUsuario(Usuario usuario)
    {
    	this.lista.add(usuario);
    }
    
    public String toString()
    {
    	String res = "";
    	Iterator<Usuario> iterador = this.lista.iterator();
    	
    	while (iterador.hasNext())
    		res += iterador.next().toString();
    	
    	return res;
    }
}
