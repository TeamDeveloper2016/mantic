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
//    String nombre= BouncyEncryption.encrypt("Estas");
    System.out.println(planetas.urano(1L, "{\"idConteo\":13,\"idUsuario\":30,\"nombre\":\"dA++H9+7Mojx3kauKPxe7uMxTVtAN1S7\",\"registro\":\"20240216014726954\",\"idEmpresa\":1,\"idAlmacen\":1,\"semilla\":\"20240216015732085\",\"productos\":[{\"idProducto\":237,\"cantidad\":50,\"codigo\":\"ALP30\",\"descripcion\":\"CONTEO_2\",\"registro\":\"20240216014810600\"},{\"idProducto\":238,\"cantidad\":50,\"codigo\":\"ALP30\",\"descripcion\":\"CONTEO_2\",\"registro\":\"20240216014810600\",\"version\":\"0.0.0.5\"}]}", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
//    System.out.println(planetas.pluton(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
//    System.out.println(planetas.sol(1L, "20240223", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));

//    Conteo conteo= new Conteo(1L, 1L, "Hola", "20240123215312");
//    conteo.getProductos().add(new Cantidad(1L, 997D, ""));
//    conteo.getProductos().add(new Cantidad(2L, 998D, ""));
//    conteo.getProductos().add(new Cantidad(3L, 999D, ""));
//    System.out.println(Decoder.toJson(conteo));
  }
  
}
