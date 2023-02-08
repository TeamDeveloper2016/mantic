package mx.org.kaana.libs.facturama.models.request.complements.ine;

import java.util.*;

/**
 *
 * @author Facturama
 */
public class Ine {

  private List<IneEntidad> Entidad;
  private String Version;
  private String TipoProceso;
  private String TipoComite;
  private Boolean TipoComiteSpecified;
  private String IdContabilidad;
  private Boolean IdContabilidadSpecified;

  public String getVersion() {
    return Version;
  }

  public void setVersion(String Version) {
    this.Version = Version;
  }

  public String getTipoproceso() {
    return TipoProceso;
  }

  public void setTipoProceso(String TipoProceso) {
    this.TipoProceso = TipoProceso;
  }

  public String getTipoComite() {
    return TipoComite;
  }

  public void setTipoComite(String TipoComite) {
    this.TipoComite = TipoComite;
  }

  public Boolean getTipoComiteSpecified() {
    return TipoComiteSpecified;
  }

  public void setTipoComiteSpecified(Boolean TipoComiteSpecified) {
    this.TipoComiteSpecified = TipoComiteSpecified;
  }

  public String getIdContabilidad() {
    return IdContabilidad;
  }

  public void setIdContabilidad(String IdContabilidad) {
    this.IdContabilidad = IdContabilidad;
  }

  public Boolean getIdContabilidadSpecified() {
    return IdContabilidadSpecified;
  }

  public void setIdContabilidadSpecified(Boolean IdContabilidadSpecified) {
    this.IdContabilidadSpecified = IdContabilidadSpecified;
  }

  public List<IneEntidad> getEntidad() {
    return Entidad;
  }

  public void setEntidad(List<IneEntidad> Entidad) {
    this.Entidad = Entidad;
  }

}
