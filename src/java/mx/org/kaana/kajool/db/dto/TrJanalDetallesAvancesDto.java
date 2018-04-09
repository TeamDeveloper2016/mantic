package mx.org.kaana.kajool.db.dto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 22/09/2015
 * @time 10:53:40 AM
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
@Table(name = "tr_janal_detalles_avances")
public class TrJanalDetallesAvancesDto implements IBaseDto, Serializable, Cloneable {

	private static final long serialVersionUID=1L;
	@Column(name = "ID_DETALLE_AVANCE")
	private Long idDetalleAvance;
	@Column(name = "ID_BITACORA_AVANCE")
	private Long idBitacoraAvance;
	@Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idDetalleAvance_sequence")
	//@SequenceGenerator(name = "idDetalleAvance_sequence", sequenceName = "SEQ_TR_JANAL_DETALLES_AVANCES", allocationSize = 1)
	@Column(name = "TOTAL")
	private Long total;
	@Column(name = "PORCENTAJE")
	private Long porcentaje;
	@Column(name = "ID_ORGANIZACION")
	private Long idOrganizacion;
	@Column(name = "ID_ENTIDAD")
	private Long idEntidad;
	@Column(name = "ID_MUNICIPIO")
	private Long idMunicipio;
	@Column(name = "ID_ORGANIZACION_GRUPO")
	private Long idOrganizacionGrupo;
	@Column(name = "ID_CAMPO")
	private Long idCampo;
	@Column(name = "NIVEL")
	private Long nivel;

	public TrJanalDetallesAvancesDto() {
		this(new Long(-1L));
	}

	public TrJanalDetallesAvancesDto(Long key) {
		this(null, null, null, null, null, null, null, null, null, null);
		setKey(key);
	}

	public TrJanalDetallesAvancesDto(Long idDetalleAvance, Long idBitacoraAvance, Long total, Long porcentaje, Long idOrganizacion, Long idEntidad, Long idMunicipio, Long idOrganizacionGrupo, Long idCampo, Long nivel) {
		setIdDetalleAvance(idDetalleAvance);
		setIdBitacoraAvance(idBitacoraAvance);
		setTotal(total);
		setPorcentaje(porcentaje);
		setIdOrganizacion(idOrganizacion);
		setIdEntidad(idEntidad);
		setIdMunicipio(idMunicipio);
		setIdOrganizacionGrupo(idOrganizacionGrupo);
		setIdCampo(idCampo);
		setNivel(nivel);
	}

	public Long getIdDetalleAvance() {
		return idDetalleAvance;
	}

	public void setIdDetalleAvance(Long idDetalleAvance) {
		this.idDetalleAvance=idDetalleAvance;
	}

	public Long getIdBitacoraAvance() {
		return idBitacoraAvance;
	}

	public void setIdBitacoraAvance(Long idBitacoraAvance) {
		this.idBitacoraAvance=idBitacoraAvance;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total=total;
	}

	public Long getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Long porcentaje) {
		this.porcentaje=porcentaje;
	}

	public Long getIdOrganizacion() {
		return idOrganizacion;
	}

	public void setIdOrganizacion(Long idOrganizacion) {
		this.idOrganizacion=idOrganizacion;
	}

	public Long getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(Long idEntidad) {
		this.idEntidad=idEntidad;
	}

	public Long getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Long idMunicipio) {
		this.idMunicipio=idMunicipio;
	}

	public Long getIdOrganizacionGrupo() {
		return idOrganizacionGrupo;
	}

	public void setIdOrganizacionGrupo(Long idOrganizacionGrupo) {
		this.idOrganizacionGrupo=idOrganizacionGrupo;
	}

	public Long getIdCampo() {
		return idCampo;
	}

	public void setIdCampo(Long idCampo) {
		this.idCampo=idCampo;
	}

	public Long getNivel() {
		return nivel;
	}

	public void setNivel(Long nivel) {
		this.nivel=nivel;
	}

	@Transient
	@Override
	public Long getKey() {
		return getIdDetalleAvance();
	}

	@Override
	public void setKey(Long key) {
		this.idBitacoraAvance=key;
	}

	@Override
	public String toString() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("[");
		regresar.append(getIdDetalleAvance());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBitacoraAvance());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPorcentaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrganizacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEntidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMunicipio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrganizacionGrupo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCampo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNivel());
		regresar.append("]");
		return regresar.toString();
	}

	@Override
	public Map toMap() {
		Map regresar=new HashMap();
		regresar.put("idDetalleAvance", getIdDetalleAvance());
		regresar.put("idBitacoraAvance", getIdBitacoraAvance());
		regresar.put("total", getTotal());
		regresar.put("porcentaje", getPorcentaje());
		regresar.put("idOrganizacion", getIdOrganizacion());
		regresar.put("idEntidad", getIdEntidad());
		regresar.put("idMunicipio", getIdMunicipio());
		regresar.put("idOrganizacionGrupo", getIdOrganizacionGrupo());
		regresar.put("idCampo", getIdCampo());
		regresar.put("nivel", getNivel());
		return regresar;
	}

	@Override
	public Object[] toArray() {
		Object[] regresar=new Object[]{
			getIdDetalleAvance(), getIdBitacoraAvance(), getTotal(), getPorcentaje(), getIdOrganizacion(), getIdEntidad(), getIdMunicipio(), getIdOrganizacionGrupo(), getIdCampo(), getNivel()
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
		regresar.append("idDetalleAvance~");
		regresar.append(getIdDetalleAvance());
		regresar.append("|");
		return regresar.toString();
	}

	@Override
	public String toKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append(getIdDetalleAvance());
		return regresar.toString();
	}

	@Override
	public Class toHbmClass() {
		return TrJanalDetallesAvancesDto.class;
	}

	@Override
	public boolean isValid() {
		return getIdDetalleAvance()!=null&&getIdDetalleAvance()!=-1L;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		} // if
		if (getClass()!=obj.getClass()) {
			return false;
		} // if
		final TrJanalDetallesAvancesDto other=(TrJanalDetallesAvancesDto) obj;
		if (getIdDetalleAvance()!=other.idDetalleAvance&&(getIdDetalleAvance()==null||!getIdDetalleAvance().equals(other.idDetalleAvance))) {
			return false;
		} // if
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=67*hash+(getIdDetalleAvance()!=null ? getIdDetalleAvance().hashCode() : 0);
		return hash;
	}

	@Transient
	@Override
	public Object clone() {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException e) {
			// This should never happen
			throw new InternalError(e.toString());
		}
	}
}
