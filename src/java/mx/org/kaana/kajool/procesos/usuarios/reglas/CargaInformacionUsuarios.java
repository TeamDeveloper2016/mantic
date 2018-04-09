package mx.org.kaana.kajool.procesos.usuarios.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 08:11:30 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.db.dto.TrJanalPerfilesJerarquiasDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EPerfiles;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.usuarios.reglas.beans.CriteriosBusqueda;
import org.hibernate.Session;

public class CargaInformacionUsuarios {

	private CriteriosBusqueda criteriosBusqueda;

	public CargaInformacionUsuarios() {
	}

	public CargaInformacionUsuarios(CriteriosBusqueda criteriosBusqueda) {
		this.criteriosBusqueda=criteriosBusqueda;
	}

	private CriteriosBusqueda getCriteriosBusqueda() {
		return criteriosBusqueda;
	}

	public void initPerfiles() {
		try {
			cargarPerfiles();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
	}

	public void init() {
		try {
			cargarEntidades();
			initPerfiles();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
	}

	/**
	 * *
	 * Carga las los perfiles de usuario disponibles para seleccionar en el filtro
	 *
	 * @throws Exception
	 */
	private void cargarPerfiles() throws Exception {
		Map<String, Object> params= null;
		try {
			params=getCondicionPerfiles();
			getCriteriosBusqueda().getListaPerfiles().addAll(UISelect.build("VistaMantenimientoPerfilesDto", "jerarquiaMostrarAsignados", params, "descripcion", EFormatoDinamicos.MAYUSCULAS));
			if (!getCriteriosBusqueda().getListaPerfiles().isEmpty()) {
				getCriteriosBusqueda().setCriterioPerfil((Long) UIBackingUtilities.toFirstKeySelectItem(getCriteriosBusqueda().getListaPerfiles()));
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw new Exception("Ocurrió un error al cargar las entidades", e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}

	/**
	 * Obtiene las condiciones para realizar la consulta hacia los perfiles
	 *
	 * @return Devuelve las condiciones en un Map
	 */
	private Map<String, Object> getCondicionPerfiles() throws Exception {
		Map<String, Object> regresar=null;
		try {
			regresar=new HashMap<>();
			regresar.put("idPerfil", JsfBase.getAutentifica().getEmpleado().getIdPerfil());;
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw new Exception("Ocurrió un error al recuperar la condición de los perfiles.");
		} // catch
		return regresar;
	}

	private void cargarEntidades() throws Exception {
		List<UISelectEntity> listaEntidades=null;
		Map<String, Object> params         =null;				
		try {
			params= new HashMap<>();		
			params.put("idPais", "1");
			if(!((JsfBase.getAutentifica().getEmpleado().getIdPerfil().equals(EPerfiles.ADMINISTRADOR_ENCUESTA.getIdPerfil())) ||
				(JsfBase.getAutentifica().getEmpleado().getIdPerfil().equals(EPerfiles.ADMINISTRADOR.getIdPerfil()))))
				params.put(Constantes.SQL_CONDICION, "id_entidad=".concat(JsfBase.getAutentifica().getEmpleado().getIdEntidad().toString()));			
			else
				params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			listaEntidades=UIEntity.build("TcJanalEntidadesDto", "comboEntidades", params);
			if (listaEntidades!=null&&!listaEntidades.isEmpty()) {
				getCriteriosBusqueda().getListaEntidades().addAll(listaEntidades);
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw new Exception("Ocurrió un error al recuperar las entidades", e);
		} // catch
		finally {
			Methods.clean(params);
			Methods.clean(listaEntidades);
		}
	}

	private String condicionEmpleado(String alias, String cadena) {
		StringBuilder like=new StringBuilder();
		if (!cadena.equals("")) {
			String[] busqueda=cadena.split(" ");
			int contador=0;
			StringBuilder elemento=new StringBuilder();
			for (int i=0; i<busqueda.length; i++) {
				elemento.append(busqueda[i]);
				contador=i;
				while (contador+1<busqueda.length&&busqueda[i].equals(busqueda[contador+1])) {
					contador++;
					elemento.append(" ").append(busqueda[contador]);
				} // while contador
				i=contador;
				if (i==busqueda.length-1) {
					like.append("  ((upper(").append(alias).append("primer_apellido) like upper('%").append(elemento.toString()).append("%')) or (upper(").append(alias).append("segundo_apellido) like upper('%").append(elemento.toString()).append("%')) or (upper(").append(alias).append("nombres) like upper('%").append(elemento.toString()).append("%'))) ");
				} // if
				else {
					like.append("  ((upper(").append(alias).append("primer_apellido) like upper('%").append(elemento.toString()).append("%')) or (upper(").append(alias).append("segundo_apellido) like upper('%").append(elemento.toString()).append("%')) or (upper(").append(alias).append("nombres) like upper('%").append(elemento.toString()).append("%'))) and ");
				} // else
				elemento.delete(0, elemento.length());
			}// for i
			elemento=null;
			busqueda=null;
		}//if cadena
		return like.toString();
	} //buscarEmpleado

	/**
	 * *
	 * Recupera los id de los perfiles permitidos para la operacion. Estoso
	 * perfiles son recuperados de la tabla de configuraciones
	 *
	 * @return Devuele los id de los perfiles encontrados en una cadena separada
	 * por comas
	 */
	private String recuperarIdPerfiles() throws Exception {
		Map<String, Object> params=new HashMap<>();
		StringBuilder regresar=new StringBuilder();
		try {
			params.put(Constantes.SQL_CONDICION, "id_perfil = ".concat(JsfBase.getAutentifica().getEmpleado().getIdPerfil().toString()));
			List<TrJanalPerfilesJerarquiasDto> trJanalPerfilesJerarquiasDto=(List<TrJanalPerfilesJerarquiasDto>) DaoFactory.getInstance().findViewCriteria(TrJanalPerfilesJerarquiasDto.class, params);
			for (TrJanalPerfilesJerarquiasDto trJanalPerfilJerarquiaDto : trJanalPerfilesJerarquiasDto) {
				regresar.append(trJanalPerfilJerarquiaDto.getIdPerfilAlta());
				regresar.append(",");
			} // for	
			if (regresar.length()!=0) {
				regresar.deleteCharAt(regresar.length()-1);
			} // if
			if (regresar.length()==0) {
				JsfBase.addMessage("No existen perfiles a los que pueda consultar, favor de veriifcarlo con el administrador del sistema", ETipoMensaje.ERROR);		
				throw new RuntimeException("No existen perfiles a los que pueda consultar, favor de veriifcarlo con el administrador del sistema");				
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar.toString();
	}

	/**
	 * *** Metodos publicos **
	 */
	/**
	 * *
	 * Construye la condicion y realiza la consulta a la base de datos para los
	 * resultados que se mostraran en pantalla; esta busqueda solo aplica cuando
	 * se realiza por nombre
	 *
	 * @return Devuele los resultados en una lista de tipo Entity
	 */
	public String busquedaPorNombre() throws Exception {
		StringBuilder regresar=new StringBuilder();
		String idPerfiles=null;
		String condicionemp=null;
		try {
			idPerfiles=recuperarIdPerfiles();			
			condicionemp=condicionEmpleado("", getCriteriosBusqueda().getCriterioNombre().toUpperCase());
			if (!Cadena.isVacio(condicionemp)) {
				regresar.append(condicionemp);
				regresar.append(" and ");
			} // if			
			regresar.append(" tc_janal_perfiles.id_perfil in (");
			regresar.append(idPerfiles);
			regresar.append(")");			
		}// try
		catch (Exception e) {					
			throw e;
		} // catch
		return regresar.toString();
	} // busquedaPorNombre

	/**
	 * *
	 * Realiza con consulta a partir de entidad seleccionada
	 *
	 * @return devuela lista lista de resultados
	 */
	public String busquedaPorEntidad() throws Exception {
		StringBuilder regresar=null;
		String idPerfiles=null;
		try {
			idPerfiles=recuperarIdPerfiles();
			regresar=new StringBuilder();
			regresar.append("tc_janal_entidades.id_entidad=");
			regresar.append(getCriteriosBusqueda().getCriterioEntidad());					
			regresar.append(" and tc_janal_perfiles.id_perfil in (");
			regresar.append(idPerfiles);
			regresar.append(")");			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		return regresar.toString();
	} // busquedaPorPerfil

	/**
	 * *
	 * Realizar la busqueda por el perfil seleccionado
	 *
	 * @return Devuelve la lista de resultados como tipo Entity
	 */
	public String busquedaPorPerfil() throws Exception {
		String regresar=null;
		try {
			recuperarIdPerfiles();
			regresar="tc_janal_perfiles.id_perfil=".concat(getCriteriosBusqueda().getCriterioPerfil().toString());
		}// try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} //  catch
		return regresar;
	}//busquedaPorOficina

	public String busquedaPorEntidadPerfilNombre() {
		StringBuilder regresar=new StringBuilder();
		String idPerfiles="";
		String condicionEmp="";
		try {
			if (getCriteriosBusqueda().getCriterioEntidad().getKey().equals(-1L)) {
				regresar.append("tc_janal_entidades.id_entidad in (");
				regresar.append(recuperarEntidades());
				regresar.append(")");
			} // if
			else {
				regresar.append("tc_janal_entidades.id_entidad=");
				regresar.append(getCriteriosBusqueda().getCriterioEntidad());
			} // else
			if (getCriteriosBusqueda().getCriterioPerfil().longValue()==-1L) {
				idPerfiles=recuperarIdPerfiles();
				regresar.append(" and tr_perfiles.id_perfil in (");
				regresar.append(idPerfiles);
				regresar.append(")");
			} // if
			else {
				regresar.append(" and tr_perfiles.id_perfil = ");
				regresar.append(getCriteriosBusqueda().getCriterioPerfil().toString());
			} // else
			condicionEmp=condicionEmpleado("", getCriteriosBusqueda().getCriterioNombre().toUpperCase());
			if (!Cadena.isVacio(condicionEmp)) {
				regresar.append(" and ");
				regresar.append(condicionEmp);
			} // if
			regresar.append(regresar.toString().length()== 0? " ".concat(Constantes.SQL_VERDADERO): "");
		}// try
		catch (Exception e) {
			Error.mensaje(e);
		} //  catch
		return regresar.toString();
	} // busquedaPorOficinaPerfilNombre

	/**
	 * *
	 * Cambia el estatus del usuario. Si el parametro "activo" es true, entonces
	 * cambiará el estatus a activo (1)
	 *
	 * @param activo
	 */
	public void cambiarEstatusUsuario(Map<String, Object> attrs) {		
		Transaccion transaccion= null;
		try {			
			transaccion= new Transaccion(attrs);						
			if(transaccion.ejecutar(EAccion.ACTIVAR)) 				
				JsfBase.addMessage((boolean)attrs.get("activo")? "Se activó el usuario con éxito.": "Se desactivó el usuario con éxito.");												
		} // try
		catch (Exception e) {
			Error.mensaje(e);			
		} // catch
	}

	private String recuperarEntidades() {
		StringBuilder regresar=new StringBuilder();
		try {
			for (UISelectEntity entity : getCriteriosBusqueda().getListaEntidades()) {
				regresar=regresar.append(entity.getKey().toString());
				regresar=regresar.append(",");
			} // for
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		return regresar.toString().substring(3, regresar.length()-1);
	} // recuperarEntidades
}
