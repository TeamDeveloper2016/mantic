package mx.org.kaana.mantic.productos.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticProductosDetallesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 1/10/2021
 *@time 07:27:19 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Partida extends TcManticProductosDetallesDto implements Serializable {

  private static final long serialVersionUID = 245348724560955248L;
  
  private String propio;
	private String codigo;  
	private String nombre;
	private String archivo;
  private ESql action;
  private ESql anterior;
  private Boolean principal;
  private Long idImagen;
  private UISelectEntity ikArticuloCodigo;
  private List<UISelectEntity> codigos;
  private String precio;
  private String cliente;
  private Long idUnico;

  public Partida() {
    this(new Random().nextLong(), null, null, null, -1L, null, "menudeo");
  }

  public Partida(Long idArticulo) {
    this(idArticulo, null, null, null, -1L, null, "menudeo");
  }
  
  public Partida(Long idArticulo, String codigo, String propio, String nombre, Long idImagen, String archivo, String tipoVenta){
    super(new Random().nextLong());
    this.codigo  = codigo;
    this.propio  = propio;
    this.nombre  = nombre;
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
    this.toLoadCodigos(Boolean.TRUE, tipoVenta);
    this.idUnico= idArticulo;
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

  public UISelectEntity getIkArticuloCodigo() {
    return ikArticuloCodigo;
  }

  public void setIkArticuloCodigo(UISelectEntity ikArticuloCodigo) {
    this.ikArticuloCodigo = ikArticuloCodigo;
    if(ikArticuloCodigo!= null)
      this.setIdArticuloCodigo(ikArticuloCodigo.getKey());
  }

  public List<UISelectEntity> getCodigos() {
    return codigos;
  }

  public String getPrecio() {
    return precio;
  }

  public String getCliente() {
    return cliente;
  }

  @Override
  public Class toHbmClass() {
    return TcManticProductosDetallesDto.class;
  }

  protected void toLoadCodigos(Boolean first, String tipoVenta) {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {      
			columns= new ArrayList<>();
      columns.add(new Columna("menudeo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("medioMayoreo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("mayoreo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      params = new HashMap<>();      
      params.put("idArticulo", this.getIdArticulo());      
      this.codigos= UIEntity.build("VistaProductosDto", "codigos", params, columns, Constantes.SQL_TODOS_REGISTROS);
      if(codigos!= null && !codigos.isEmpty()) {
        if(first)
          this.setIkArticuloCodigo(this.codigos.get(0));
        else {
          int index= this.codigos.indexOf(new UISelectEntity(this.getIdArticuloCodigo()));
          if(index>= 0)
            this.setIkArticuloCodigo(this.codigos.get(index));
          else
            this.setIkArticuloCodigo(this.codigos.get(0));
        } // if  
        this.codigo = this.getIkArticuloCodigo().toString("codigo");
        this.precio = this.getIkArticuloCodigo().toString("menudeo");
        this.cliente= this.getIkArticuloCodigo().toString(tipoVenta);
      } // if  
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 11 * hash + Objects.hashCode(this.idUnico);
    return hash;
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
    final Partida other = (Partida) obj;
    if (!Objects.equals(this.idUnico, other.idUnico)) {
      return false;
    }
    return true;
  }
  
}
