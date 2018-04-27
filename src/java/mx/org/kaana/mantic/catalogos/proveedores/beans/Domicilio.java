/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.mantic.catalogos.proveedores.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.josql.FactoryJoSql;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorDomicilioDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Domicilio extends TrManticProveedorDomicilioDto implements Serializable {
  
  private static final Log LOG = LogFactory.getLog(Domicilio.class);
  private static final long serialVersionUID = 3459865616223422089L;
  
  private UISelectEntity entidad;
  private UISelectEntity municipio;
  private UISelectEntity localidad;
  private UISelectEntity detalleCalle;
  private TcManticDomiciliosDto tcManticDomicilioDto;  
  private ESql accion;
  
  public Domicilio () {
   this(ESql.UPDATE);  
  }
  
  public Domicilio (ESql accion) {
    this.accion = accion;
    this.tcManticDomicilioDto = new TcManticDomiciliosDto();
  }  

  public TcManticDomiciliosDto getTcManticDomicilioDto() {
    return tcManticDomicilioDto;
  }  

  public ESql getAccion() {
    return accion;
  }

  public void setAccion(ESql accion) {
    this.accion = accion;
  }

  public UISelectEntity getDetalleCalle() {
    return detalleCalle;
  }

  public void setDetalleCalle(UISelectEntity detalleCalle) {
    this.detalleCalle = detalleCalle;
  } 
  

  public UISelectEntity getEntidad() {
    return entidad;
  }

  public void setEntidad(UISelectEntity entidad) {
    this.entidad = entidad;
  }

  public UISelectEntity getMunicipio() {
    return municipio;
  }

  public void setMunicipio(UISelectEntity municipio) {
    this.municipio = municipio;
  }

  public UISelectEntity getLocalidad() {
    return localidad;
  }

  public void setLocalidad(UISelectEntity localidad) {
    this.localidad = localidad;
  }
  
  
  public static void main (String args[]) {
    List<Domicilio>  domicilio  = new ArrayList<>();
    Map params = new HashMap();
    try {
       domicilio.add(new Domicilio(ESql.INSERT));
       domicilio.add(new Domicilio(ESql.INSERT));
       domicilio.add(new Domicilio(ESql.INSERT));
       domicilio.add(new Domicilio(ESql.INSERT));
       domicilio.add(new Domicilio(ESql.UPDATE));
       domicilio.add(new Domicilio(ESql.UPDATE));
       params.put("accion", "UPDATE");
       System.out.println(FactoryJoSql.getInstance().toRecords(Domicilio.class, params, domicilio));
    } // try
    catch (Exception e) {
    
    }// catch
  }
}
