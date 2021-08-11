package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TcManticArticulosImagenesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/08/2021
 *@time 06:50:22 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class ArticuloImagen extends TcManticArticulosImagenesDto implements Serializable {

  private static final long serialVersionUID = 5607315492469342639L;
  
  private Long id;
  private Importado importado;
  private ESql sqlAccion;
  private ESql anterior;
	private Boolean nuevo;  
	private Boolean principal;  

  public ArticuloImagen() {
    this((new Random().nextLong())* -1);  
  }
  
  public ArticuloImagen(Importado importado) {
    this(new Random().nextLong(), importado);
  }
  
  public ArticuloImagen(Long id) {
    this(id, new Importado());
  }

  public ArticuloImagen(Long id, Importado importado) {
    super();
    this.id       = id;
    this.importado= new Importado();
    this.importado.setName("sin-foto.png");
    this.importado.setFileSize(100L);
    this.importado.setMedicion("KB");
    if(this.id % 2== 0)
      this.sqlAccion= ESql.SELECT;
    else
      this.sqlAccion= ESql.INSERT;
    this.nuevo    = Boolean.FALSE;
    this.anterior = this.sqlAccion;
    this.principal= Objects.equals(this.getIdPrincipal(), 1L);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public Importado getImportado() {
    return importado;
  }

  public void setImportado(Importado importado) {
    this.importado = importado;
  }

  public ESql getSqlAccion() {
    return sqlAccion;
  }

  public void setSqlAccion(ESql sqlAccion) {
    this.sqlAccion = sqlAccion;
  }

  public Boolean getNuevo() {
    return nuevo;
  }

  public void setNuevo(Boolean nuevo) {
    this.nuevo = nuevo;
  }

  public ESql getAnterior() {
    return anterior;
  }

  public void setAnterior(ESql anterior) {
    this.anterior = anterior;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + Objects.hashCode(this.id);
    return hash;
  }

  public Boolean getPrincipal() {
    return principal;
  }

  public void setPrincipal(Boolean principal) {
    this.principal = principal;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ArticuloImagen other = (ArticuloImagen) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }
  
  @Override
  public Class toHbmClass() {
    return TcManticArticulosImagenesDto.class;
  }
  
}
