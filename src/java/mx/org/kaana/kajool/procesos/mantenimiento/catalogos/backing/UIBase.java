package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.backing;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.ASIGNAR;
import static mx.org.kaana.kajool.enums.EAccion.CONSULTAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import static mx.org.kaana.kajool.enums.EAccion.LISTAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans.Busqueda;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans.Dinamico;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.Criterio;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/10/2015
 * @time 06:11:43 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public abstract class UIBase extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = -2231362253035978954L;

  private List<Criterio> criterios;
  private Dinamico dinamico;

  public List<Criterio> getCriterios() {
    return criterios;
  }

  public void setCriterios(List<Criterio> criterios) {
    this.criterios = criterios;
  }

  public Dinamico getDinamico() {
    return dinamico;
  }

  public void setDinamico(Dinamico dinamico) {
    this.dinamico = dinamico;
  }

  public void doBuscar(Criterio criterio) {
    if(this.criterios!= null)
      this.attrs.put("buscar", new Busqueda(criterio));
  }

  public String getNames() {
    StringBuilder regresar= new StringBuilder();
    if(!Cadena.isVacio(this.criterios))
      for (Criterio criterio: this.criterios) {
        regresar.append(" ");
        regresar.append(criterio.getId());
      } // for
    return regresar.toString();
  }

  public String getNamesFields() {
    StringBuilder regresar= new StringBuilder();
    if(!Cadena.isVacio(this.criterios))
      for (Criterio criterio: this.criterios) {
        regresar.append(" ");
        regresar.append("o".concat(criterio.getId()));
      } // for
    regresar.append(this.getNames());
    return regresar.toString();
  }

  public void doEvento(EAccion accion) {
    Busqueda buscar= null;
    switch (accion) {
      case MODIFICAR:
      case CONSULTAR:
        JsfBase.setFlashAttribute("idKey", ((Entity)this.attrs.get("selected")).getKey());
        JsfBase.setFlashAttribute("idCatalogo", this.attrs.get("idCatalogo"));
        break;
      case ELIMINAR:
        try {
          IBaseDto dto= DaoFactory.getInstance().loadDTO(this.dinamico.getNombre());
          dto.setKey(((Entity)this.attrs.get("selected")).getKey());
          Transaccion transaccion= new Transaccion(dto);
          transaccion.ejecutar(accion);
        } // try
        catch(Exception e) {
          mx.org.kaana.libs.formato.Error.mensaje(e);
          JsfUtilities.addMessageError(e);
        } // catch
        break;
      case LISTAR:
        buscar= (Busqueda)this.attrs.get("buscar");
        buscar.toLookUpItems();
        break;
      case ASIGNAR:
        buscar= (Busqueda)this.attrs.get("buscar");
        buscar.toSelected();
        break;
    } // switch accion
    JsfBase.setFlashAttribute("accion", accion);
  } // doEvento

  protected String toApplyParams(String sql) {
    // Aqui se tiene que aplicar los cambios de los parametros para la precondicion en caso de existir
    return sql;
  }

}
