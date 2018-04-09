package mx.org.kaana.kajool.procesos.usuarios.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 2/09/2015
 * @time 11:23:30 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TrJanalPerfilesJerarquiasDto;
import mx.org.kaana.kajool.procesos.usuarios.reglas.formato.Rangos;

public class UIClavePerfil implements Serializable {

	private static final long serialVersionUID=1430239435570744843L;
	private List<UISelectItem> perfiles;
	private List<UISelectItem> claves;
	private Long idPerfil;
	private Long idOrganizacionAmbito;
	private Long idOrganizacionGrupo;
	private String clave;

	public UIClavePerfil(UIOrganizacionAmbito organizacionAmbito) {
		this.idOrganizacionGrupo=organizacionAmbito.getOrganizacionGrupo().getKey();
		setPerfiles(new ArrayList<UISelectItem>());
		setClaves(new ArrayList<UISelectItem>());
		init();
	}

	public UIClavePerfil(Long idPerfil) {
		this.idPerfil=idPerfil;
		loadClaves();
	}

	public void setClaves(List<UISelectItem> claves) {
		this.claves=claves;
	}

	public List<UISelectItem> getClaves() {
		return claves;
	}

	public void setIdPerfil(Long idPerfil) {
		this.idPerfil=idPerfil;
	}

	public Long getIdPerfil() {
		return idPerfil;
	}

	private void init() {
		loadPerfiles();
		loadClaves();
	}

	public void setPerfiles(List<UISelectItem> perfiles) {
		this.perfiles=perfiles;
	}

	public List<UISelectItem> getPerfiles() {
		return perfiles;
	}

	public void setClave(String clave) {
		this.clave=clave;
	}

	public String getClave() {
		return clave;
	}

	public Long getIdOrganizacionAmbito() {
		return idOrganizacionAmbito;
	}

	public void loadPerfiles() {
		Map<String, Object> params=new HashMap<String, Object>();
		try {
			params=getCondicionPerfiles();
			List<Entity> list=DaoFactory.getInstance().toEntitySet("VistaPerfilesDto", "row", params);
			if (list!=null&&list.size()>0) {
				setIdPerfil(list.get(0).toLong("idPerfil"));
			} // if
			for (Entity vista : list) {
				getPerfiles().add(new UISelectItem(vista.toLong("idPerfil"), vista.toString("descripcion").toUpperCase()));
			} // for
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}

	public void loadClaves() {
		String rango=null;
		Map<String, Object> params=new HashMap<String, Object>();
		Entity vistaPerfilesDto=null;
		List<String> listaClaves=null;
		String display="";
		try {
			params.put("idPerfil", this.idPerfil);
			vistaPerfilesDto=(Entity) DaoFactory.getInstance().toEntity("VistaPerfilesDto", params);
			if (vistaPerfilesDto!=null) {
				rango=vistaPerfilesDto.toString("rango");
			} // if
			if (rango!=null) {
				if (getClaves()==null) {
					this.claves=new ArrayList<UISelectItem>();
				} // if
				this.claves.clear();
				Rangos rangos=new Rangos(rango, this.idOrganizacionGrupo, getIdPerfil());
				listaClaves=rangos.getSerie();
				if (listaClaves!=null) {
					for (int i=0; i<listaClaves.size(); i++) {
						getClaves().add(new UISelectItem(listaClaves.get(i), Methods.ajustar(listaClaves.get(i))));
					} // for
					setClave(listaClaves.get(0));
				} // if
				else {
					setClave(null);
				} // else
			} // if rango
			else {
				if (getClaves()!=null) {
					getClaves().clear();
				} // if
				setClave(null);
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}

	public void loadClaves(UIOrganizacionAmbito organizacionAmbito) {		
		this.idOrganizacionGrupo=organizacionAmbito.getOrganizacionGrupo().getKey();
		loadClaves();
	}

	private Map<String, Object> getCondicionPerfiles() {
		Map<String, Object> regresar=new HashMap<String, Object>();
		Map<String, Object> params=new HashMap<String, Object>();
		StringBuilder sb=new StringBuilder();
		try {
			params.put(Constantes.SQL_CONDICION, "id_perfil = ".concat(JsfBase.getAutentifica().getEmpleado().getIdPerfil().toString()));
			List<TrJanalPerfilesJerarquiasDto> trJanalPerfilesJerarquiasDto=(List<TrJanalPerfilesJerarquiasDto>) DaoFactory.getInstance().findViewCriteria(TrJanalPerfilesJerarquiasDto.class, params);
			for (TrJanalPerfilesJerarquiasDto trJanalPerfilJerarquiaDto : trJanalPerfilesJerarquiasDto) {
				sb.append(trJanalPerfilJerarquiaDto.getIdPerfilAlta());
				sb.append(",");
			} // for	
			if (sb.length()!=0) {
				sb.deleteCharAt(sb.length()-1);
			} // if
			regresar.put("idPerfil", sb);
			regresar.put("idGrupo", JsfBase.seekParameter("idGrupo").toString());
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
}
