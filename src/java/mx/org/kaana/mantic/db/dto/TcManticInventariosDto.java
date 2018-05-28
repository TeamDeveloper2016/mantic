package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Lob;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tc_mantic_inventarios")
public class TcManticInventariosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="entradas")
  private Long entradas;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_inventario")
  private Long idInventario;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="inicial")
  private Long inicial;
  @Column (name="stock")
  private Long stock;
  @Column (name="salidas")
  private Long salidas;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticInventariosDto() {
    this(new Long(-1L));
  }

  public TcManticInventariosDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticInventariosDto(Long idUsuario, Long idAlmacen, Long entradas, Long idInventario, Long idArticulo, Long inicial, Long stock, Long salidas, Long ejercicio) {
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setEntradas(entradas);
    setIdInventario(idInventario);
    setIdArticulo(idArticulo);
    setInicial(inicial);
    setStock(stock);
    setSalidas(salidas);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setEntradas(Long entradas) {
    this.entradas = entradas;
  }

  public Long getEntradas() {
    return entradas;
  }

  public void setIdInventario(Long idInventario) {
    this.idInventario = idInventario;
  }

  public Long getIdInventario() {
    return idInventario;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setInicial(Long inicial) {
    this.inicial = inicial;
  }

  public Long getInicial() {
    return inicial;
  }

  public void setStock(Long stock) {
    this.stock = stock;
  }

  public Long getStock() {
    return stock;
  }

  public void setSalidas(Long salidas) {
    this.salidas = salidas;
  }

  public Long getSalidas() {
    return salidas;
  }

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getEjercicio() {
    return ejercicio;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdInventario();
  }

  @Override
  public void setKey(Long key) {
  	this.idInventario = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEntradas());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdInventario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getInicial());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getStock());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSalidas());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("entradas", getEntradas());
		regresar.put("idInventario", getIdInventario());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("inicial", getInicial());
		regresar.put("stock", getStock());
		regresar.put("salidas", getSalidas());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getIdAlmacen(), getEntradas(), getIdInventario(), getIdArticulo(), getInicial(), getStock(), getSalidas(), getEjercicio(), getRegistro()
    };
    return regresar;
  }

  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("|");
    regresar.append("idInventario~");
    regresar.append(getIdInventario());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdInventario());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticInventariosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdInventario()!= null && getIdInventario()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticInventariosDto other = (TcManticInventariosDto) obj;
    if (getIdInventario() != other.idInventario && (getIdInventario() == null || !getIdInventario().equals(other.idInventario))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdInventario() != null ? getIdInventario().hashCode() : 0);
    return hash;
  }

}


