package mx.org.kaana.mantic.productos.beans;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.mantic.db.dto.TcManticProductosDetallesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 1/10/2021
 *@time 07:27:19 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Partida extends TcManticProductosDetallesDto implements Serializable {

  private static final long serialVersionUID = 245348724560955248L;
  
  private String propio;
	private String codigo;  
	private String nombre;
	private String archivo;
  private ESql action;
  private ESql anterior;
  private Boolean principal;
  private Long idImagen;

  public Partida() {
    this(new Random().nextLong(), null, null, null, -1L, null);
  }

  public Partida(Long idArticulo, String codigo, String propio, String nombre, Long idImagen, String archivo) {
    super(new Random().nextLong());
    this.codigo= codigo;
    this.propio= propio;
    this.nombre= nombre;
    this.idImagen= idImagen;
    this.archivo = archivo;
    this.setIdArticulo(idArticulo);
    if(Objects.equals(this.getKey(), 0))
      this.setKey(new Random().nextLong());
    if(this.getKey()> 0)
      this.setKey(this.getKey()* -1L);
    this.action  = ESql.INSERT;    
    this.anterior= ESql.INSERT;   
    if(!Cadena.isVacio(this.nombre)) {
      int index= this.nombre.lastIndexOf(" ");
      if(index< 0) {
        this.setDescripcion(this.nombre);
        this.setMedida("");
      } // if
      else {
        this.setDescripcion(this.nombre.substring(0, index));
        this.setMedida(this.nombre.substring(index+ 1, this.nombre.length()));
      } // else
    } // if
    this.principal= Boolean.FALSE;
  }

  public String getPropio() {
    return propio;
  }

  public void setPropio(String propio) {
    this.propio = propio;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getArchivo() {
    return archivo;
  }

  public void setArchivo(String archivo) {
    this.archivo = archivo;
  }
  
  public ESql getAction() {
    return action;
  }

  public void setAction(ESql action) {
    this.action = action;
  }
  
  public ESql getAnterior() {
    return anterior;
  }

  public void setAnterior(ESql anterior) {
    this.anterior = anterior;
  }

  public Boolean getPrincipal() {
    return principal;
  }

  public void setPrincipal(Boolean principal) {
    this.principal = principal;
  }

  public Long getIdImagen() {
    return idImagen;
  }

  public void setIdImagen(Long idImagen) {
    this.idImagen = idImagen;
  }

  @Override
  public Class toHbmClass() {
    return TcManticProductosDetallesDto.class;
  }

}
