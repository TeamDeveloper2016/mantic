package mx.org.kaana.kajool.db.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 09:57:44 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tr_janal_indicadores_perfil")
public class TrJanalIndicadoresPerfilDto implements IBaseDto, Serializable{

  private static final long serialVersionUID = -1454174921993724624L;

  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column (name="ID_INDICADOR_PERFIL")
  private Long idIndicadorPerfil;
  @Column (name="ID_PERFIL")
  private Long idPerfil;
  @Column (name="TITULO_GENERAL")
  private String tituloGeneral;
  @Column (name="TITULO_LADOX")
  private String tituloLadox;
  @Column (name="ALIAS_LADOX")
  private String aliasLadox;
  @Column (name="TITULO_LADOY")
  private String tituloLadoy;
  @Column (name="ALIAS_LADOY")
  private String aliasLadoy;
  @Column (name="VISTA")
  private String vista;
  @Column (name="ID_VISTA")
  private String idVista;
  @Column (name="DESCRIPCION_CONTEO")
  private String descripcionConteo;

  public TrJanalIndicadoresPerfilDto() {
    this(-1L);
  }

  public TrJanalIndicadoresPerfilDto(Long idIndicadorPerfil) {
    this(idIndicadorPerfil, null, null, null, null, null, null, null, null, null);
  }

  public TrJanalIndicadoresPerfilDto(Long idIndicadorPerfil, Long idPerfil, String tituloGeneral, String tituloLadox, String aliasLadox, String tituloLadoy, String aliasLadoy, String vista, String idVista, String descripcionConteo) {
    setIdIndicadorPerfil(idIndicadorPerfil);
    setIdPerfil(idPerfil);
    setTituloGeneral(tituloGeneral);
    setTituloLadox(tituloLadox);
    setAliasLadox(aliasLadox);
    setTituloLadoy(tituloLadoy);
    setAliasLadoy(aliasLadoy);
    setVista(vista);
    setIdVista(idVista);
    setDescripcionConteo(descripcionConteo);
  }

  public Long getIdIndicadorPerfil() {
    return idIndicadorPerfil;
  }

  public void setIdIndicadorPerfil(Long idIndicadorPerfil) {
    this.idIndicadorPerfil= idIndicadorPerfil;
  }

  public Long getIdPerfil() {
    return idPerfil;
  }

  public void setIdPerfil(Long idPerfil) {
    this.idPerfil= idPerfil;
  }

  public String getTituloGeneral() {
    return tituloGeneral;
  }

  public void setTituloGeneral(String tituloGeneral) {
    this.tituloGeneral= tituloGeneral;
  }

  public String getTituloLadox() {
    return tituloLadox;
  }

  public void setTituloLadox(String tituloLadox) {
    this.tituloLadox= tituloLadox;
  }

  public String getAliasLadox() {
    return aliasLadox;
  }

  public void setAliasLadox(String aliasLadox) {
    this.aliasLadox= aliasLadox;
  }

  public String getTituloLadoy() {
    return tituloLadoy;
  }

  public void setTituloLadoy(String tituloLadoy) {
    this.tituloLadoy= tituloLadoy;
  }

  public String getAliasLadoy() {
    return aliasLadoy;
  }

  public void setAliasLadoy(String aliasLadoy) {
    this.aliasLadoy= aliasLadoy;
  }

  public String getVista() {
    return vista;
  }

  public void setVista(String vista) {
    this.vista= vista;
  }

  public String getIdVista() {
    return idVista;
  }

  public void setIdVista(String idVista) {
    this.idVista= idVista;
  }

  public String getDescripcionConteo() {
    return descripcionConteo;
  }

  public void setDescripcionConteo(String descripcionConteo) {
    this.descripcionConteo = descripcionConteo;
  }

  @Override
  public Long getKey() {
    return this.idIndicadorPerfil;
  }

  @Override
  public void setKey(Long key) {
    this.idIndicadorPerfil= key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdIndicadorPerfil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPerfil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTituloGeneral());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTituloLadox());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAliasLadox());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTituloLadoy());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAliasLadoy());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVista());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVista());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcionConteo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map<String, Object> toMap() {
    Map regresar = new HashMap();
		regresar.put("idIndicadorPerfil", getIdIndicadorPerfil());
		regresar.put("idperfil", getIdPerfil());
		regresar.put("tituloGeneral", getTituloGeneral());
		regresar.put("tituloLadox", getTituloLadox());
		regresar.put("aliasLadox", getAliasLadox());
		regresar.put("tituloLadoy", getTituloLadoy());
		regresar.put("aliasLadoy", getAliasLadoy());
		regresar.put("vista", getVista());
		regresar.put("idVista", getIdVista());
		regresar.put("descripcionConteo", getDescripcionConteo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdIndicadorPerfil(), getIdPerfil(), getTituloGeneral(), getTituloLadox(), getAliasLadox(), getTituloLadoy(), getAliasLadoy(), getVista(), getIdVista(), getDescripcionConteo()
    };
    return regresar;
  }

  @Override
  public boolean isValid() {
    return getIdIndicadorPerfil()!= null && getIdIndicadorPerfil()!=-1L;
  }

  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("|");
    regresar.append("idIndicadorPerfil~");
    regresar.append(getIdIndicadorPerfil());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdIndicadorPerfil());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalIndicadoresPerfilDto.class;
  }
}
