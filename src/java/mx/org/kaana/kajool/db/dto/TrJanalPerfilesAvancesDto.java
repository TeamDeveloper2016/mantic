package mx.org.kaana.kajool.db.dto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 24/09/2015
 * @time 04:24:40 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
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
@Table(name = "tr_janal_perfiles_avances")
public class TrJanalPerfilesAvancesDto implements IBaseDto, Serializable {

	private static final long serialVersionUID=1L;
	@Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idPerfilAvance_sequence")
	//@SequenceGenerator(name = "idPerfilAvance_sequence", sequenceName = "SEQ_TR_JANAL_PERFILES_AVANCES", allocationSize = 1)
	@Column(name = "ID_PERFIL_AVANCE")
	private Long idPerfilAvance;
	@Column(name = "ID_PERFIL")
	private Long idPerfil;
	@Column(name = "ID_DEFINICION_AVANCE")
	private Long idDefinicionAvance;
	@Column(name = "REGISTRO")
	private Timestamp registro;

	public TrJanalPerfilesAvancesDto() {
		this(new Long(-1L));
	}

	public TrJanalPerfilesAvancesDto(Long key) {
		this(null, null, null);
		setKey(key);
	}

	public TrJanalPerfilesAvancesDto(Long idPerfilAvance, Long idPerfil, Long idDefinicionAvance) {
		setIdPerfilAvance(idPerfilAvance);
		setIdPerfil(idPerfil);
		setIdDefinicionAvance(idDefinicionAvance);
		setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
	}

	public Long getIdPerfilAvance() {
		return idPerfilAvance;
	}

	public void setIdPerfilAvance(Long idPerfilAvance) {
		this.idPerfilAvance=idPerfilAvance;
	}

	public Long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Long idPerfil) {
		this.idPerfil=idPerfil;
	}

	public Long getIdDefinicionAvance() {
		return idDefinicionAvance;
	}

	public void setIdDefinicionAvance(Long idDefinicionAvance) {
		this.idDefinicionAvance=idDefinicionAvance;
	}

	public Timestamp getRegistro() {
		return registro;
	}

	public void setRegistro(Timestamp registro) {
		this.registro=registro;
	}

	@Transient
	@Override
	public Long getKey() {
		return getIdPerfilAvance();
	}

	@Override
	public void setKey(Long key) {
		this.idPerfilAvance=key;
	}

	@Override
	public String toString() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("[");
		regresar.append(getIdPerfilAvance());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPerfil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDefinicionAvance());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append("]");
		return regresar.toString();
	}

	@Override
	public Map toMap() {
		Map regresar=new HashMap();
		regresar.put("idPerfilAvance", getIdPerfilAvance());
		regresar.put("idPerfil", getIdPerfil());
		regresar.put("idDefinicionAvance", getIdDefinicionAvance());
		regresar.put("registro", getRegistro());
		return regresar;
	}

	@Override
	public Object[] toArray() {
		Object[] regresar=new Object[]{
			getIdPerfilAvance(), getIdPerfil(), getIdDefinicionAvance(), getRegistro()
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
		regresar.append("idPerfilAvance~");
		regresar.append(getIdPerfilAvance());
		regresar.append("|");
		return regresar.toString();
	}

	@Override
	public String toKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append(getIdPerfilAvance());
		return regresar.toString();
	}

	@Override
	public Class toHbmClass() {
		return TrJanalPerfilesAvancesDto.class;
	}

	@Override
	public boolean isValid() {
		return getIdPerfilAvance()!=null&&getIdPerfilAvance()!=-1L;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		} // if
		if (getClass()!=obj.getClass()) {
			return false;
		} // if
		final TrJanalPerfilesAvancesDto other=(TrJanalPerfilesAvancesDto) obj;
		if (getIdPerfilAvance()!=other.idPerfilAvance&&(getIdPerfilAvance()==null||!getIdPerfilAvance().equals(other.idPerfilAvance))) {
			return false;
		} // if
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=67*hash+(getIdPerfilAvance()!=null ? getIdPerfilAvance().hashCode() : 0);
		return hash;
	}
}
