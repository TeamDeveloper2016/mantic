package mx.org.kaana.kajool.db.comun.hibernate.fuentes;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 3, 2012
 *@time 10:01:34 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;

import mx.org.kaana.libs.formato.Error;



import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.init.Settings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

public  class LectorPlantilla {

  private static final Log LOG = LogFactory.getLog(LectorPlantilla.class);

	private Boolean jndi;		
		
	public LectorPlantilla(boolean jndi) { 	
	  this.jndi=jndi;	
	}
    	
	
	public Boolean isJndi() {
		return jndi;
	}	

  private StringBuffer leerPlantilla() throws Exception {
    StringBuffer regresar       = new StringBuffer();
    BufferedReader bufferReader = null;
    String linea                = null;
    InputStream inputStream     = null;
		LOG.info("Construyendo archivo hibernate cfg JNDI[".concat(Boolean.toString(this.jndi)).concat("]"));
    try {
			if (this.jndi)
        inputStream = this.getClass().getResourceAsStream(Settings.getInstance().getDefaultHibernateJndi());
			else
				 inputStream = this.getClass().getResourceAsStream(Settings.getInstance().getDefaultHibernateDinamic());
      bufferReader = new BufferedReader(new InputStreamReader(inputStream));
      while ((linea = bufferReader.readLine()) != null) {
        regresar.append(linea);
      } // while
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    finally {
      if (inputStream != null)
        inputStream.close();
      inputStream = null;
    } // finally
    return regresar;
  }

   public Document construir(Map<String,String> parametros) {
     Document regresar       = null;
		 String contenidoArchivo = null;
		 DocumentBuilderFactory factory = null;
		 DocumentBuilder builder        = null;
     try  {
       contenidoArchivo = Cadena.replaceParams(leerPlantilla().toString(), parametros, true);
       contenidoArchivo = Cadena.replaceParams(contenidoArchivo, parametros);
       factory = DocumentBuilderFactory.newInstance();
       builder = factory.newDocumentBuilder();
       regresar= builder.parse(new ByteArrayInputStream(contenidoArchivo.getBytes("iso-8859-1")));
     } // try
     catch (Exception e)  {
       Error.mensaje(e);
     } // catch
     return regresar;
   }
}


