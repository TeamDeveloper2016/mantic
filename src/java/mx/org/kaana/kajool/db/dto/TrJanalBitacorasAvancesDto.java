package mx.org.kaana.kajool.db.dto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/09/2015
 * @time 04:15:11 PM
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
@Table(name = "tr_janal_bitacoras_avances")
public class TrJanalBitacorasAvancesDto implements IBaseDto, Serializable, Cloneable {

	private static final long serialVersionUID=1L;
	@Column(name = "ID_BITACORA_AVANCE")
	private Long idBitacoraAvance;
	@Column(name = "ID_DEFINICION_AVANCE")
	private Long idDefinicionAvance;
	@Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idBitacoraAvance_sequence")
	//@SequenceGenerator(name = "idBitacoraAvance_sequence", sequenceName = "SEQ_TR_JANAL_BITACORAS_AVANC", allocationSize = 1)
	@Column(name = "INICIO")
	private Timestamp inicio;
	@Column(name = "FIN")
	private Timestamp fin;
	@Column(name = "DESCRIPCION_PROCESO")
	private String descripcionProceso;
	@Column(name = "ESTATUS")
	private Long estatus;

	public TrJanalBitacorasAvancesDto() {
		this(new Long(-1L));
	}

	public TrJanalBitacorasAvancesDto(Long key) {
		this(null, null, null, null);
		setKey(key);
	}

	public TrJanalBitacorasAvancesDto(Long idBitacoraAvance, Long idDefinicionAvance, String descripcionProceso, Long estatus) {
		setIdBitacoraAvance(idBitacoraAvance);
		setIdDefinicionAvance(idDefinicionAvance);
		setDescripcionProceso(descripcionProceso);
		setEstatus(estatus);
		setInicio(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setFin(new Timestamp(Calendar.getInstance().getTimeInMillis()));
	}

	public Long getIdBitacoraAvance() {
		return idBitacoraAvance;
	}

	public void setIdBitacoraAvance(Long idBitacoraAvance) {
		this.idBitacoraAvance=idBitacoraAvance;
	}

	public Long getIdDefinicionAvance() {
		return idDefinicionAvance;
	}

	public void setIdDefinicionAvance(Long idDefinicionAvance) {
		this.idDefinicionAvance=idDefinicionAvance;
	}

	public Timestamp getInicio() {
		return inicio;
	}

	public void setInicio(Timestamp inicio) {
		this.inicio=inicio;
	}

	public Timestamp getFin() {
		return fin;
	}

	public void setFin(Timestamp fin) {
		this.fin=fin;
	}

	public String getDescripcionProceso() {
		return descripcionProceso;
	}

	public void setDescripcionProceso(String descripcionProceso) {
		this.descripcionProceso=descripcionProceso;
	}

	public Long getEstatus() {
		return estatus;
	}

	public void setEstatus(Long estatus) {
		this.estatus=estatus;
	}

	@Transient
	@Override
	public Long getKey() {
		return getIdBitacoraAvance();
	}

	@Override
	public void setKey(Long key) {
		this.idBitacoraAvance=key;
	}

	@Override
	public String toString() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("[");
		regresar.append(getIdBitacoraAvance());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDefinicionAvance());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcionProceso());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getInicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFin());
		regresar.append("]");
		return regresar.toString();
	}

	@Override
	public Map toMap() {
		Map regresar=new HashMap();
		regresar.put("idBitacoraAvance", getIdBitacoraAvance());
		regresar.put("idDefinicionAvance", getIdDefinicionAvance());
		regresar.put("descripcionProceso", getDescripcionProceso());
		regresar.put("estatus", getEstatus());
		regresar.put("inicio", getInicio());
		regresar.put("fin", getFin());
		return regresar;
	}

	@Override
	public Object[] toArray() {
		Object[] regresar=new Object[]{
			getIdBitacoraAvance(), getIdDefinicionAvance(), getDescripcionProceso(), getEstatus(), getInicio(), getFin()
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
		regresar.append("idBitacoraAvance~");
		regresar.append(getIdBitacoraAvance());
		regresar.append("|");
		return regresar.toString();
	}

	@Override
	public String toKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append(getIdBitacoraAvance());
		return regresar.toString();
	}

	@Override
	public Class toHbmClass() {
		return TrJanalBitacorasAvancesDto.class;
	}

	@Override
	public boolean isValid() {
		return getIdBitacoraAvance()!=null&&getIdBitacoraAvance()!=-1L;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		} // if
		if (getClass()!=obj.getClass()) {
			return false;
		} // if
		final TrJanalBitacorasAvancesDto other=(TrJanalBitacorasAvancesDto) obj;
		if (getIdBitacoraAvance()!=other.idBitacoraAvance&&(getIdBitacoraAvance()==null||!getIdBitacoraAvance().equals(other.idBitacoraAvance))) {
			return false;
		} // if
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=67*hash+(getIdBitacoraAvance()!=null ? getIdBitacoraAvance().hashCode() : 0);
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
