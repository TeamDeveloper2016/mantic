package mx.org.kaana.kajool.procesos.usuarios.delegar.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 05:27:25 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.util.List;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EMenusPersona;
import mx.org.kaana.kajool.procesos.acceso.reglas.GeneradorCuentas;
import mx.org.kaana.kajool.procesos.usuarios.reglas.GestorSQL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class DatosDelega extends IBaseAttribute implements Serializable {

	private static final Log LOG= LogFactory.getLog(DatosDelega.class);
	private static final long serialVersionUID= -5803723011852182995L;
		
	protected List<UISelectItem> paises;
	protected List<UISelectItem> entidades;
	protected List<UISelectItem> entidadesResidencia;
	protected List<UISelectItem> municipios;
	protected List<UISelectItem> codigosPostales;
	protected List<UISelectItem> sexo;;	

	public List<UISelectItem> getPaises() {
		return paises;
	}

	public List<UISelectItem> getEntidades() {
		return entidades;
	}

	public List<UISelectItem> getEntidadesResidencia() {
		return entidadesResidencia;
	}

	public List<UISelectItem> getMunicipios() {
		return municipios;
	}

	public List<UISelectItem> getCodigosPostales() {
		return codigosPostales;
	}

	public List<UISelectItem> getSexo() {
		return sexo;
	}

	protected void initDelega() {
		Entity detallePersona= null;
		GestorSQL gestor     = null;
		Long idPersona       = -1L;
		Long idCodigoPostal  = -1L;
		try {									
			idPersona= (Long) this.attrs.get("idPersona");			
			gestor= new GestorSQL(idPersona);
			//this.persona= idPersona > 0 ? gestor.toPersona() : new TcKajoolPersonasDto();
			initMenu(true);
			//if (this.persona.getIdPersona()!= -1L) {
				//idCodigoPostal= this.persona.getIdCodigoPostal();				
				detallePersona= gestor.toMunicipioEntidad(idCodigoPostal);
				this.attrs.put("idCodigoPostal", idCodigoPostal);				
				this.attrs.put("idMunicipio", detallePersona.get("idMunicipio").toLong());
				initMenu(false);
				this.attrs.put("idEntidad", detallePersona.get("idEntidad").toLong());
			//} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // init
	
	private void initMenu(boolean inicial) throws Exception{
		for(EMenusPersona menuPersona: EMenusPersona.values()){
			if(inicial && (menuPersona.equals(EMenusPersona.PAIS) || menuPersona.equals(EMenusPersona.SEXO)))				
				loadMenus(menuPersona);			
			else if (!inicial && (!menuPersona.equals(EMenusPersona.PAIS) || !menuPersona.equals(EMenusPersona.SEXO)))
				loadMenus(menuPersona);
		} // for
	} // initMenu
	
	public void doLoadMenus(String menu){
		EMenusPersona menuPersona= null;
		try {
			menuPersona= EMenusPersona.getMenuPersona(menu);		
			loadMenus(menuPersona);
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch				
	} // doLoadMenus
	
	private void loadMenus(EMenusPersona menuPersona) throws Exception{		
		this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		switch(menuPersona){
			case PAIS:
				this.paises= UISelect.build("TcJanalPaisesDto", "comboPais", this.attrs, "descripcion", EFormatoDinamicos.MAYUSCULAS);
				break;
			case ENTIDAD:
				//this.attrs.put("idPais", this.persona.getIdPaisNacimiento());
				this.entidades= UISelect.build("TcJanalEntidadesDto", "comboEntidades", this.attrs, "descripcion", EFormatoDinamicos.MAYUSCULAS);
				break;
			case MUNICIPIO:
				this.municipios= UISelect.build("TcJanalMunicipiosDto", "comboMunicipios", this.attrs, "descripcion", EFormatoDinamicos.MAYUSCULAS);
				break;
			case RESIDENCIA:
				//this.attrs.put("idPais", this.persona.getIdPaisResidencia());
				this.entidadesResidencia= UISelect.build("TcJanalEntidadesDto", "comboEntidades", this.attrs, "descripcion", EFormatoDinamicos.MAYUSCULAS);
				this.attrs.put("idEntidad", "");
				break;
			case CODIGO_POSTAL:
				this.codigosPostales= UISelect.build("TcKajoolCodigosPostalesDto", "comboCodigosPostales", this.attrs, "descripcion");
				break;
			case SEXO: 					
				this.sexo= UISelect.build("TcKajoolSexosDto", "comboSexo", this.attrs, "descripcion", EFormatoDinamicos.MAYUSCULAS);
				break;
		} // switch		
	} // doLoadMenus

	protected void generarCuenta(){		
		//generarCuenta(this.persona.getNombres(), this.persona.getPrimerApellido(), this.persona.getSegundoApellido());
	} // generarCuenta
	
	protected void generarCuenta(String nombre, String paterno, String materno){		
		String login              = null;
		GeneradorCuentas generador= null;
		try {
			generador= new GeneradorCuentas(nombre, paterno, materno);
			login= generador.getCuentaGenerada();
			this.attrs.put("login", login);
			this.attrs.put("contrasenia", "");
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // generarCuenta
	
	protected String getDescripcionSeleccion(EMenusPersona menuPersona, Long key){
		String regresar       = null;		
		List<UISelectItem>list= null;
		try {
			switch(menuPersona){
				case PAIS:
					list= this.paises;
					break;
				case ENTIDAD:
					list= this.entidades;
					break;
				case MUNICIPIO:
					list= this.municipios;
					break;
				case RESIDENCIA:
					list= this.entidadesResidencia;
					break;
				case CODIGO_POSTAL:
					list= this.codigosPostales;
					break;				
			} // switch
			for(UISelectItem item: list){
				if(Long.valueOf(item.getValue().toString()).equals(key))
					regresar= item.getLabel();
			} // for
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar== null ? "" : regresar;
	} // getDescripcionSeleccion
	
	@Override
	protected void finalize() throws Throwable {
		Methods.clean(this.paises);
		Methods.clean(this.entidades);
		Methods.clean(this.entidadesResidencia);
		Methods.clean(this.municipios);
		Methods.clean(this.codigosPostales);
		Methods.clean(this.sexo);
		super.finalize();
	}	 // finalize
}
