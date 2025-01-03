package mx.org.kaana.libs.facturama.test;

import java.util.Calendar;
import java.util.List;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.facturama.models.response.Cfdi;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 30/10/2018
 * @time 10:29:39 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Descargas {
	
	private static final Log LOG=LogFactory.getLog(Descargas.class);

	private static Calendar toCalendar(String date, String time) {
		Calendar regresar= Calendar.getInstance();
		regresar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		regresar.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7))- 1);
		regresar.set(Calendar.DATE, Integer.parseInt(date.substring(8, 10)));
		regresar.set(Calendar.HOUR, Integer.parseInt(time.substring(0, 2)));
		regresar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3, 5)));
		regresar.set(Calendar.SECOND, Integer.parseInt(time.substring(6, 8)));
		return regresar;
	}
  
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
//		List<CfdiSearchResult> cfdis= CFDIFactory.getInstance().getCfdis();
//		int count= 0;
//		for (CfdiSearchResult cfdi : cfdis) {
//			// 0123456789012345678
//			// 2018-11-06T18:25:03
//			//            01234567
//			Calendar calendar= toCalendar(cfdi.getDate().substring(0, 10), cfdi.getDate().substring(11, 19));
//   		LOG.info(++count+ ".- Descargando la factura ["+ cfdi.getFolio()+ "] "+ cfdi.getDate());	
//   		// Descarga de los archivos de la factura
//	  	String path = "d:/temporal/archivos/facturama/"+ calendar.get(Calendar.YEAR)+ "/"+ Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase()+"/"+ cfdi.getRfc().concat("/");
//		  // CFDIFactory.getInstance().download(path, cfdi.getRfc().concat("-").concat(cfdi.getFolio()), cfdi.getId());
//			break;
//		} // for
//		LOG.info("Ok.");

    String idFacturama= "GG7m00wbqXO5sujvo_i60A2";
    List<CfdiSearchResult> cfdis= CFDIFactory.getInstance().getCfdis();
		int index= cfdis.indexOf(new CfdiSearchResult(idFacturama));
		if(index>= 0) {
      CfdiSearchResult cfdi= cfdis.get(index);
	  	Calendar calendar= Fecha.toCalendar(cfdi.getDate().substring(0, 10), cfdi.getDate().substring(11, 19));
  		String path = Configuracion.getInstance().getPropiedadSistemaServidor("facturama").concat(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString()).concat("/")+ calendar.get(Calendar.YEAR)+ "/"+ Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase()+"/"+ cfdi.getRfc().concat("/");
		  CFDIFactory.getInstance().download(path, cfdi.getRfc().concat("-").concat(cfdi.getFolio()), cfdi.getId());
    } // if  
		else
			throw new RuntimeException("La factura con id "+ idFacturama+ " no se encuentra generada !");

	}

}
