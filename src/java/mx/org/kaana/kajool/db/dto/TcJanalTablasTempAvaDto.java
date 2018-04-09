package mx.org.kaana.kajool.db.dto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/09/2015
 * @time 04:00:13 PM
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
@Table(name = "tc_janal_tablas_temp_ava")
public class TcJanalTablasTempAvaDto implements IBaseDto, Serializable {

	private static final long serialVersionUID=1L;
	@Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idTablaTempAva_sequence")
	//@SequenceGenerator(name = "idTablaTempAva_sequence", sequenceName = "SEQ_TC_JANAL_TABLAS_TEMP_AVA", allocationSize = 1)
	@Column(name = "ID_TABLA_TEMP_AVA")
	private Long idTablaTempAva;
	@Column(name = "ID_DEFINICION_AVANCE")
	private Long idDefinicionAvance;
	@Column(name = "NOMBRE_TABLA")
	private String nombreTabla;
	@Column(name = "CAMPOS")
	private String campos;
	@Column(name = "PROCESO")
	private String proceso;
	@Column(name = "ID_XML")
	private String idXml;
	@Column(name = "ORDEN")
	private Long orden;
	@Column(name = "PRINCIPAL")
	private Long principal;

	public TcJanalTablasTempAvaDto() {
		this(new Long(-1L));
	}

	public TcJanalTablasTempAvaDto(Long key) {
		this(null, null, null, null, null, null, null, null);
		setKey(key);
	}

	public TcJanalTablasTempAvaDto(Long idTablaTempAva, Long idDefinicionAvance, String nombreTabla, String campos, String proceso, String idXml, Long orden, Long principal) {
		setIdTablaTempAva(idTablaTempAva);
		setIdDefinicionAvance(idDefinicionAvance);
		setNombreTabla(nombreTabla);
		setCampos(campos);
		setProceso(proceso);
		setIdXml(idXml);
		setOrden(orden);
		setPrincipal(principal);
	}

	public Long getIdTablaTempAva() {
		return idTablaTempAva;
	}

	public void setIdTablaTempAva(Long idTablaTempAva) {
		this.idTablaTempAva=idTablaTempAva;
	}

	public Long getIdDefinicionAvance() {
		return idDefinicionAvance;
	}

	public void setIdDefinicionAvance(Long idDefinicionAvance) {
		this.idDefinicionAvance=idDefinicionAvance;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla=nombreTabla;
	}

	public String getCampos() {
		return campos;
	}

	public void setCampos(String campos) {
		this.campos=campos;
	}

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso=proceso;
	}

	public String getIdXml() {
		return idXml;
	}

	public void setIdXml(String idXml) {
		this.idXml=idXml;
	}

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden=orden;
	}

	public Long getPrincipal() {
		return principal;
	}

	public void setPrincipal(Long principal) {
		this.principal=principal;
	}

	@Transient
	@Override
	public Long getKey() {
		return getIdTablaTempAva();
	}

	@Override
	public void setKey(Long key) {
		this.idTablaTempAva=key;
	}

	@Override
	public String toString() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("[");
		regresar.append(getIdTablaTempAva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDefinicionAvance());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombreTabla());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCampos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getProceso());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdXml());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrincipal());
		regresar.append("]");
		return regresar.toString();
	}

	@Override
	public Map toMap() {
		Map regresar=new HashMap();
		regresar.put("idTablaTempAva", getIdTablaTempAva());
		regresar.put("idDefinicionAvance", getIdDefinicionAvance());
		regresar.put("nombreTabla", getNombreTabla());
		regresar.put("campos", getCampos());
		regresar.put("proceso", getProceso());
		regresar.put("idXml", getIdXml());
		regresar.put("orden", getOrden());
		regresar.put("principal", getPrincipal());

		return regresar;
	}

	@Override
	public Object[] toArray() {
		Object[] regresar=new Object[]{
			getIdTablaTempAva(), getIdDefinicionAvance(), getNombreTabla(), getCampos(), getProceso(), getIdXml(), getOrden(), getPrincipal()
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
		regresar.append("idTablaTempAva~");
		regresar.append(getIdTablaTempAva());
		regresar.append("|");
		return regresar.toString();
	}

	@Override
	public String toKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append(getIdTablaTempAva());
		return regresar.toString();
	}

	@Override
	public Class toHbmClass() {
		return TcJanalTablasTempAvaDto.class;
	}

	@Override
	public boolean isValid() {
		return getIdTablaTempAva()!=null&&getIdTablaTempAva()!=-1L;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		} // if
		if (getClass()!=obj.getClass()) {
			return false;
		} // if
		final TcJanalTablasTempAvaDto other=(TcJanalTablasTempAvaDto) obj;
		if (getIdTablaTempAva()!=other.idTablaTempAva&&(getIdTablaTempAva()==null||!getIdTablaTempAva().equals(other.idTablaTempAva))) {
			return false;
		} // if
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=67*hash+(getIdTablaTempAva()!=null ? getIdTablaTempAva().hashCode() : 0);
		return hash;
	}
}
