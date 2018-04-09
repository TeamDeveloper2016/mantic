package mx.org.kaana.kajool.db.dto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/09/2015
 * @time 03:22:17 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

@Entity
@Table(name = "tc_janal_definiciones_avances")
public class TcJanalDefinicionesAvancesDto implements IBaseDto, Serializable {

	private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idDefinicionAvance_sequence")
	//@SequenceGenerator(name = "idDefinicionAvance_sequence", sequenceName = "SEQ_TC_JANAL_DEFINICIONES_AVA", allocationSize = 1)
	@Column(name = "ID_DEFINICION_AVANCE")
	private Long idDefinicionAvance;
	@Column(name = "DESCRIPCION")
	private String descripcion;
	@Column(name = "ACTIVO")
	private Long activo;
	@Column(name = "CAMPO")
	private String campo;
	@Column(name = "TABLA")
	private String tabla;
	@Column(name = "ESTATUS_INDICADOR")
	private String estatusIndicador;
	@Column(name = "ESTATUS_APLICAN")
	private String estatusAplican;
	@Column(name = "ID_GRUPO")
	private Long idGrupo;	
	@Column(name = "CORTE")
	private String corte;
	@Column(name = "ORDEN")
	private Long orden;

	public TcJanalDefinicionesAvancesDto() {
		this(new Long(-1L));
	}

	public TcJanalDefinicionesAvancesDto(Long key) {
		this(null, null, null, null, null, null, null, null, null, null);
		setKey(key);
	}

	public TcJanalDefinicionesAvancesDto(Long idDefinicionAvance, String descripcion, Long activo, String campo, String tabla, String estatusIndicador, String estatusAplican, Long idGrupo, String corte, Long orden) {
		setIdDefinicionAvance(idDefinicionAvance);
		setDescripcion(descripcion);
		setActivo(activo);
		setCampo(campo);
		setTabla(tabla);
		setEstatusIndicador(estatusIndicador);
		setEstatusAplican(estatusAplican);
		setIdGrupo(idGrupo);
		setCorte(corte);
		setOrden(orden);
	}

	public Long getIdDefinicionAvance() {
		return idDefinicionAvance;
	}

	public void setIdDefinicionAvance(Long idDefinicionAvance) {
		this.idDefinicionAvance=idDefinicionAvance;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion=descripcion;
	}

	public Long getActivo() {
		return activo;
	}

	public void setActivo(Long activo) {
		this.activo=activo;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo=campo;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla=tabla;
	}

	public String getEstatusIndicador() {
		return estatusIndicador;
	}

	public void setEstatusIndicador(String estatusIndicador) {
		this.estatusIndicador=estatusIndicador;
	}

	public String getEstatusAplican() {
		return estatusAplican;
	}

	public void setEstatusAplican(String estatusAplican) {
		this.estatusAplican=estatusAplican;
	}

	public Long getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Long idGrupo) {
		this.idGrupo=idGrupo;
	}

	public String getCorte() {
		return corte;
	}

	public void setCorte(String corte) {
		this.corte=corte;
	}

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden=orden;
	}

	@Transient
	@Override
	public Long getKey() {
		return getIdDefinicionAvance();
	}

	@Override
	public void setKey(Long key) {
		this.idDefinicionAvance=key;
	}

	@Override
	public String toString() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("[");
		regresar.append(getIdDefinicionAvance());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getActivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCampo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTabla());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEstatusIndicador());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEstatusAplican());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGrupo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCorte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append("]");
		return regresar.toString();
	}

	@Override
	public Map toMap() {
		Map regresar=new HashMap();
		regresar.put("idDefinicionAvance", getIdDefinicionAvance());
		regresar.put("descripcion", getDescripcion());
		regresar.put("activo", getActivo());
		regresar.put("campo", getCampo());
		regresar.put("tabla", getTabla());
		regresar.put("estatusIndicador", getEstatusIndicador());
		regresar.put("estatusAplican", getEstatusAplican());
		regresar.put("idGrupo", getIdGrupo());
		regresar.put("corte", getCorte());
		regresar.put("orden", getOrden());
		return regresar;
	}

	@Override
	public Object[] toArray() {
		Object[] regresar=new Object[]{
			getIdDefinicionAvance(), getDescripcion(), getActivo(), getCampo(), getTabla(), getEstatusIndicador(), getEstatusAplican(), getIdGrupo(), getCorte(), getOrden()
		};
		return regresar;
	}

	@Override
	public Object toValue(String name) {
		return Methods.getValue(this, name);
	}

	@Override
	public String toAllKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("|");
		regresar.append("idDefinicionAvance~");
		regresar.append(getIdDefinicionAvance());
		regresar.append("|");
		return regresar.toString();
	}

	@Override
	public String toKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append(getIdDefinicionAvance());
		return regresar.toString();
	}

	@Override
	public Class toHbmClass() {
		return TcJanalDefinicionesAvancesDto.class;
	}

	@Override
	public boolean isValid() {
		return getIdDefinicionAvance()!=null&&getIdDefinicionAvance()!=-1L;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		} // if
		if (getClass()!=obj.getClass()) {
			return false;
		} // if
		final TcJanalDefinicionesAvancesDto other=(TcJanalDefinicionesAvancesDto) obj;
		if (getIdDefinicionAvance()!=other.idDefinicionAvance&&(getIdDefinicionAvance()==null||!getIdDefinicionAvance().equals(other.idDefinicionAvance))) {
			return false;
		} // if
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=67*hash+(getIdDefinicionAvance()!=null ? getIdDefinicionAvance().hashCode() : 0);
		return hash;
	}
}
