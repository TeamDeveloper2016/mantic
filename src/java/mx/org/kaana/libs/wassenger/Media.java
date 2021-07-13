package mx.org.kaana.libs.wassenger;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 24/06/2021
 *@time 03:28:29 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Media extends ArrayList<Data> implements Serializable {
  
  private static final Log LOG = LogFactory.getLog(Media.class);
  private static final long serialVersionUID = 99375904456190963L;
  
  public static void main(String ... args) throws Exception {
    String tokens= "[{\"id\":\"60d4ba052739feb85874b575\",\"url\":\"https://cafu.jvmhost.net/Temporal/Pdf/CAFU_2021062410492325_orden_de_compra_detalle.pdf\",\"format\":\"native\",\"filename\":\"CAFU_2021062410492325_orden_de_compra_detalle.pdf\",\"size\":258187,\"origin\":\"remote\",\"mime\":\"application/pdf\",\"ext\":\"pdf\",\"kind\":\"document\",\"sha2\":\"23fdeb0fdd59ad0be05d111a0e0d24bfd6076ce9812c81537216f00f30420500\",\"tags\":[],\"status\":\"active\",\"mode\":\"default\",\"createdAt\":\"2021-06-24T16:59:49.283Z\",\"expiresAt\":\"2021-10-22T16:59:49.271Z\",\"stats\":{\"downloads\":0,\"deliveries\":0}}]";
    Gson gson = new Gson();
    Media media = gson.fromJson(tokens, Media.class);
    for (Data item : media) {
      LOG.info(item);
    } // for
  }
  
}
