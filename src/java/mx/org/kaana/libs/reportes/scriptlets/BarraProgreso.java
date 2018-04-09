package mx.org.kaana.libs.reportes.scriptlets;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 08:36:21 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import ar.com.fdvs.dj.core.DJDefaultScriptlet;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.imageio.ImageIO;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reportes.CodigoBarras;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import net.sf.jasperreports.engine.JRScriptletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BarraProgreso extends DJDefaultScriptlet implements Serializable {

  private static final Log LOG              = LogFactory.getLog(BarraProgreso.class);
	private static final long serialVersionUID= 3047108539345686663L;
	
	private boolean percentage;
	private long total;

  protected Object checkParameter(String parameter, Object defaultValue) {
    try {
      return this.getParameterValue(parameter);
    }
    catch (Exception e) {
      Error.mensaje(e);
      return defaultValue;
    } // try
  } // checkParameter

  protected Object checkParameter(String parameter) {
    return checkParameter(parameter, null);
  }

  protected Object checkVariable(String variable, Object defaultValue) {
    try {
      return this.getVariableValue(variable);
    }
    catch (Exception e) {
        Error.mensaje(e);
      return defaultValue;
    } // try
  } // checkParameter

  protected Object checkVariable(String variable) {
    return checkVariable(variable, null);
  }

  protected Object checkField(String field, Object defaultValue) {
    try {
      return this.getFieldValue(field);
    }
    catch (Exception e) {
      Error.mensaje(e);
      return defaultValue;
    } // try
  } // checkField

  protected Object checkField(String parameter) {
    return checkField(parameter, null);
  }

	@Override
  public void beforeReportInit() throws JRScriptletException {
    try {
			this.total= 0L;
      if (checkParameter(Constantes.REPORTE_REGISTROS)!= null)
        this.total = ((Number)this.getParameterValue(Constantes.REPORTE_REGISTROS)).longValue();
			else
				this.percentage= true;
      if (this.total== 0)
				if(checkParameter(Constantes.REPORTE_SQL)!= null)
				  this.total= DaoFactory.getInstance().toSize((String)checkParameter(Constantes.REPORTE_SQL));
				else
				  this.total= 1L;
			if(this.total> 0) {
  		  Monitoreo monitoreo= JsfBase.getAutentifica().getMonitoreo();
			  if(monitoreo!= null)
	  	    monitoreo.setTotal(total);
			}
    }  // try
		catch (Exception e) {
      Error.mensaje(e);
			throw new RuntimeException(e);
    } // catch
  }

	@Override
  public void afterDetailEval() throws JRScriptletException {
	  if(this.total> 0) {
			Monitoreo monitoreo= JsfBase.getAutentifica().getMonitoreo();
			if(monitoreo!= null) {
				long current       = ((Number)this.getVariableValue("REPORT_COUNT")).longValue();
				if(this.percentage) {
					monitoreo.setProgreso((long)(current* 100/ monitoreo.getTotal()));
				}	// if
				else
					monitoreo.setProgreso(current);
				LOG.debug("Registro [" + current + " de " + monitoreo.getTotal()+ "] porcentaje " + (current * 100/ monitoreo.getTotal()));
			} // if	
  	} // if	
  }

  public String getFecha(int formato, Timestamp registro) {
    java.util.Date date = new java.util.Date(registro.getTime());
    return Fecha.formatear(formato, date);
  }

  public String getFecha(Timestamp registro) {
    return getFecha(Fecha.FECHA_LARGA, registro);
  }

  public String getFecha(int formato, java.sql.Date registro) {
    return Fecha.formatear(formato, registro);
  }

  public String getFecha(Date registro) {
    return getFecha(Fecha.FECHA_LARGA, registro);
  }

  public String getFecha(int formato, java.util.Date registro) {
    return Fecha.formatear(formato, registro);
  }

  public String getFecha(java.util.Date registro) {
    return getFecha(Fecha.FECHA_LARGA, registro);
  }

  public String getFecha() {
    return getFecha(Fecha.FECHA_LARGA, new java.util.Date());
  }

  public String getFecha(int formato) {
    return Fecha.formatear(formato, new java.util.Date());
  }

  public String getNumeroRedondea(double valor){
    return Numero.formatear(Numero.MONEDA_CON_DECIMALES, Double.parseDouble(Numero.redondear(valor)));
  }

  public String getLetraCapital(String nombre) {
    return Cadena.letraCapital(nombre);
  }

  public String getNombrePersona(String nombre) {
    return Cadena.nombrePersona(nombre);
  }

  private InputStream fotografia(Blob imagen) throws IOException, SQLException, JRScriptletException {
    ByteArrayInputStream regresar= null;
    InputStream in               = null;
    ByteArrayOutputStream im     = null;
    try {
      in= imagen.getBinaryStream();
      im= new ByteArrayOutputStream();
      int c;
      while ((c = in.read()) != -1)
        im.write(c);
      regresar= new ByteArrayInputStream(im.toByteArray());
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      if(in!= null)
        in.close();
      if(im!= null)
        im.close();
    } // finally
    return (InputStream)regresar;
  }

  public InputStream fotografia() throws FileNotFoundException, JRScriptletException {
    File file = new File(this.checkParameter("REPORTE_IMAGENES")+ "imagen-default.png");
    FileInputStream in = new FileInputStream(file);
    return (InputStream) in;
  }

   public InputStream codigoBarras(String cadena) throws IOException, FileNotFoundException, JRScriptletException {
    InputStream regresar       = null;
    CodigoBarras codigoBarras  = new CodigoBarras(cadena);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BufferedImage bufferedImage=codigoBarras.codigoMemoria(cadena);
    try {
     ImageIO.write(bufferedImage, "jpg", baos);
     regresar = new ByteArrayInputStream(baos.toByteArray());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  } // codigoMemoria

}
