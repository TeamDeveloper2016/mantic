package mx.org.kaana.mantic.ws.test;

import java.io.Serializable;
import mx.org.kaana.mantic.ws.consumible.reglas.Conteos;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 23/01/2024
 * @time 10:24:45 PM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Remote implements Serializable {

  private static final long serialVersionUID = -1430227883528578119L;

  public static void main(String ... args) throws Exception {
    Object[] params= {1976L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"};
//    Conteos conteos= new Conteos("mercurio", params);
//    System.out.println(conteos.getBeanRespuesta());
    Conteos conteos= new Conteos("sol", params);
    System.out.println(conteos.getBeanRespuesta());
    
//    Object[] params= {"/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2", 1976L};
//    Conteos conteos= new Conteos("neptuno", params);
//    System.out.println(conteos.getBeanRespuesta());
  }
  
}
