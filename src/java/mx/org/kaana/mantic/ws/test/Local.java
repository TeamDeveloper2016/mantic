package mx.org.kaana.mantic.ws.test;

import java.io.Serializable;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.mantic.db.dto.TcManticDispositivosDto;
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
//    System.out.println(planetas.mercurio(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.marte(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.jupiter(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.saturno(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.venus(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.neptuno("/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2", 1L));
    //String nombre= BouncyEncryption.encrypt("Estas");
    //System.out.println(planetas.urano(1L, "{\"idConteo\":13,\"idUsuario\":30,\"nombre\":\"dA++H9+7Mojx3kauKPxe7uMxTVtAN1S7\",\"registro\":\"20240216014726954\",\"idEmpresa\":1,\"idAlmacen\":1,\"semilla\":\"20240216015732085\",\"productos\":[{\"idProducto\":237,\"cantidad\":50,\"codigo\":\"ALP30\",\"descripcion\":\"CONTEO_2\",\"registro\":\"20240216014810600\"},{\"idProducto\":238,\"cantidad\":50,\"codigo\":\"ALP30\",\"descripcion\":\"CONTEO_2\",\"registro\":\"20240216014810600\",\"version\":\"0.0.0.5\"}]}", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
//    System.out.println(planetas.pluton(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
//    System.out.println(planetas.sol(1L, "20240223", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));

    //String name= BouncyEncryption.encrypt("juan.perez");
    //System.out.println(name);
    //System.out.println(planetas.sistema("/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2", "IPU57KFfBZ/gP8F4k6vOFt2dYATwrzNN", "0.0.1.2"));

//    TcManticDispositivosDto dipositivo= new TcManticDispositivosDto(
//      null, // Long idActivo, 
//      null, // String original, 
//      "SAMSUNG GALAXY FE20, ANDROID 13", // String dispositivo, 
//      "20240216014726954", // String semilla, 
//      null, // Long idDispositivo, 
//      "mbct6u8dE7wFMux4h73jFA==", // String cuenta, 
//      "012345678901234567890", // String imei, 
//      null, // Long idVersion, 
//      "IPU57KFfBZ/gP8F4k6vOFt2dYATwrzNN", // String nombre, 
//      "0.0.1.2" // String version
//    );
//    System.out.println(Decoder.toJson(dipositivo));
//    String hola= "{\"dispositivo\": \"SAMSUNG GALAXY FE20, ANDROID 13\",\"semilla\": \"20240216014726954\",\"cuenta\": \"mbct6u8dE7wFMux4h73jFA\\u003d\\u003d\",\"imei\": \"012345678901234567890\",\"nombre\": \"IPU57KFfBZ/gP8F4k6vOFt2dYATwrzNN\",\"version\": \"0.0.1.0\"}";
//    System.out.println(planetas.luna(1L, hola, "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
//    Conteo conteo= new Conteo(1L, 1L, "Hola", "20240123215312");
//    conteo.getProductos().add(new Cantidad(1L, 997D, ""));
//    conteo.getProductos().add(new Cantidad(2L, 998D, ""));
//    conteo.getProductos().add(new Cantidad(3L, 999D, ""));
//    System.out.println(Decoder.toJson(conteo));

//    System.out.println(planetas.eclipse(1L, "{\"idConteo\":7,\"idUsuario\":30,\"nombre\":\"dA++H9+7Mojx3kauKPxe7uMxTVtAN1S7\",\"registro\":\"20240216014726954\",\"idEmpresa\":1,\"idAlmacen\":1,\"idFuente\":2,\"semilla\":\"20240216015732085\",\"productos\":[{\"a\":237,\"b\":50,\"c\":\"ALP31\",\"d\":\"\",\"e\":\"20240216014810600\"},{\"a\":238,\"b\":50,\"c\":\"ALP30\",\"d\":\"\",\"e\":\"20240216014810600\"}],\"version\":\"0.0.1.5\"}", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
//    System.out.println(planetas.galaxia("/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2", "012345678901234567890", "20240216014726954"));

//    System.out.println(planetas.asteroide(1L, "1", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2", 2L));
//    System.out.println(planetas.cometa(1L, "1", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
  }

}
