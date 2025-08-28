package mx.org.kaana.mantic.solicitudes.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasPersonasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/08/2025
 *@time 11:49:27 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Persona extends TcManticTransferenciasPersonasDto implements Serializable {

  private static final long serialVersionUID = -6119742635547643149L;

	private UISelectEntity ikPersona;
  private String nombre;
  private String celular;
  private ESql sql;

  public Persona() {
    super(new Long((int)(Math.random()* -10000)));
    this.sql= ESql.INSERT;
  }

  public UISelectEntity getIkPersona() {
    return ikPersona;
  }

  public void setIkPersona(UISelectEntity ikPersona) {
    this.ikPersona = ikPersona;
    if(!Objects.equals(ikPersona, null))
      this.setIdPersona(ikPersona.getKey());
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getCelular() {
    return celular;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public ESql getSql() {
    return sql;
  }

  public void setSql(ESql sql) {
    this.sql = sql;
  }

  @Override
  public int hashCode() {
    int hash= 8;
    hash = 58 * hash + Objects.hashCode(this.getIdPersona());
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) 
      return true;
    if (obj == null) 
      return false;
    if (getClass() != obj.getClass()) 
      return false;
    final Persona other = (Persona) obj;
    if (!Objects.equals(this.getIdPersona(), other.getIdPersona())) 
      return false;
    return true;
  }
  
  @Override
  public Class toHbmClass() {
    return TcManticTransferenciasPersonasDto.class;
  }
  
}
