package mx.org.kaana.mantic.productos.beans;

import java.io.Serializable;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticProductosCategoriasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/10/2021
 *@time 07:07:05 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Categoria extends TcManticProductosCategoriasDto implements Serializable {

  private static final long serialVersionUID = 7661687020981200497L;
  
  private Importado importado;
  private Long idPadre;
  private String texto;
  private Long valor;
  private String separador;

  public Importado getImportado() {
    return importado;
  }

  public void setImportado(Importado importado) {
    this.importado = importado;
  }
  
  public String getSeparador() {
    return separador;
  }

  public void setSeparador(String separador) {
    this.separador = separador;
  }

  public Categoria() {
    super(-1L);
  }

  public Categoria(String padre, Long ultimo, Long idActivo, Long idUsuario, Long idProductoCategoria, Long porcentaje, String nombre, Long nivel, Long orden, Long idProductoCategoriaArchivo) {
    this(padre, ultimo, idActivo, idUsuario, idProductoCategoria, porcentaje, nombre, nivel, orden, -1L, Constantes.SEPARADOR, idProductoCategoriaArchivo);
  }

  public Categoria(String padre, Long ultimo, Long idActivo, Long idUsuario, Long idProductoCategoria, Long porcentaje, String nombre, Long nivel, Long orden, Long idPadre, String separador, Long idProductoCategoriaArchivo) {
    super(padre, ultimo, idActivo, idUsuario, idProductoCategoria, porcentaje, nombre, nivel, orden, idProductoCategoriaArchivo);
    this.idPadre= idPadre;
    this.separador= separador;
    this.texto= nombre;
    this.valor= porcentaje;
  }

  public Long getIdPadre() {
    return idPadre;
  }

  public void setIdPadre(Long idPadre) {
    this.idPadre = idPadre;
  }

  public Long getValor() {
    return valor;
  }

  public void setValor(Long valor) {
    this.valor = valor;
  }

  public String getTexto() {
    return texto;
  }

  public void setTexto(String texto) {
    this.texto = texto;
  }
  
  @Override
  public Class toHbmClass() {
    return TcManticProductosCategoriasDto.class;
  }
  
  @Override
  public Categoria clone() throws CloneNotSupportedException {
    return new Categoria(
      this.getPadre(), // String padre, 
      this.getUltimo(), // Long ultimo, 
      this.getIdActivo(), // Long idActivo, 
      this.getIdUsuario(), // Long idUsuario, 
      -1L, // Long idProductoCategoria, 
      this.getPorcentaje(), // Long porcentaje, 
      this.getNombre(), // String nombre, 
      this.getNivel(), // Long nivel, 
      this.getOrden(), // Long orden
      this.getIdPadre(),  // Long idPadre
      this.getSeparador(), // String separador
      this.getIdProductoCategoriaArchivo()
    );
  }
}
