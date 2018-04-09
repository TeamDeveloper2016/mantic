package mx.org.kaana.kajool.procesos.acceso.ayudas.backing;

import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.db.dto.TcJanalAyudasDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 18/09/2015
 * @time 03:19:45 PM
 * @author Team Developer 2016 <team.developerkaana.org.mx>
 */
@ManagedBean(name = "kajoolAccesoAyudasFiltro")
@ViewScoped
public class Filtro extends IBaseFilter {
  private static final Log LOG = LogFactory.getLog(Filtro.class);	

  @Override
  public void doLoad() {
    try {
      this.lazyModel = new FormatLazyModel("VistaContadorAyudasDto", "row", this.attrs, Collections.EMPTY_LIST);
    } //try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    }//catch
  }
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put(Constantes.SQL_CONDICION,Constantes.SQL_VERDADERO);
      this.attrs.put("ayudas", UISelect.build(TcJanalAyudasDto.class, this.attrs, "clave"));
      doLoad();
     }// try // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    }// catch

  }
  public void doMostrar(){
    this.attrs.put(Constantes.SQL_CONDICION, "tc_kajool_ayudas.id_ayuda=".concat(this.attrs.get("idAyuda").toString()));
    doLoad();
  }// doMostrar


}
