package mx.org.kaana.mantic.ws.test;

import java.io.Serializable;
import mx.org.kaana.mantic.ws.publicar.Planetas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 23/01/2024
 * @time 10:24:45 PM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Local implements Serializable {

  private static final long serialVersionUID = -1430227883528578119L;

  public static void main(String ... args) throws Exception {
    Planetas planetas= new Planetas();
    //System.out.println(planetas.mercurio(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.marte(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.jupiter(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.saturno(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.venus(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.neptuno("/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2", 1L));
    System.out.println(planetas.urano(1L, "{\"idConteo\":1,\"idUsuario\": 1,\"nombre\":\"Hola\",\"productos\":[{ \"idProducto\": 1,\"cantidad\": 997.0,\"codigo\": \"4321\",\"descripcion\": \"\",\"registro\": \"20240123215540896\"},{\"idProducto\": 2,\"cantidad\": 998.0,\"codigo\": \"5432\",\"descripcion\": \"\",\"registro\": \"20240123215540927\"},{\"idProducto\": 3,\"cantidad\": 999.0,\"codigo\": \"6543\",\"descripcion\": \"\",\"registro\": \"20240123215540927\"}],\"registro\": \"20240123215312\",\"idEmpresa\": \"1\",\"idAlmacen\": \"1\"}", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.pluton(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.sol(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));

//    Conteo conteo= new Conteo(1L, 1L, "Hola", "20240123215312");
//    conteo.getProductos().add(new Cantidad(1L, 997D, ""));
//    conteo.getProductos().add(new Cantidad(2L, 998D, ""));
//    conteo.getProductos().add(new Cantidad(3L, 999D, ""));
//    System.out.println(Decoder.toJson(conteo));
  }
  
}
