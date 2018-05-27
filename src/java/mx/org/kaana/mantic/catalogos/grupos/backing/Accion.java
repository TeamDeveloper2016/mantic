package mx.org.kaana.mantic.catalogos.grupos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.grupos.beans.Cliente;
import mx.org.kaana.mantic.catalogos.grupos.reglas.AdminGrupo;
import mx.org.kaana.mantic.catalogos.grupos.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticGruposDto;


@Named(value = "manticCatalogosGruposAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
	private AdminGrupo adminGrupo;

	public AdminGrupo getAdminGrupo() {
		return adminGrupo;
	}

	public void setAdminGrupo(AdminGrupo adminGrupo) {
		this.adminGrupo=adminGrupo;
	}
	
	@PostConstruct
  @Override
  protected void init() {		
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idGrupo", JsfBase.getFlashAttribute("idGrupo")== null? -1L: JsfBase.getFlashAttribute("idGrupo"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			this.attrs.put("clientes", (List<UISelectEntity>) UIEntity.build("TcManticClientesDto", "sucursales", params, columns));
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
  } // init

  public void doLoad() {
    EAccion eaccion= null;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
          this.adminGrupo= new AdminGrupo(new TcManticGruposDto(-1L, JsfBase.getAutentifica().getPersona().getIdUsuario(), "", ""));
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          this.adminGrupo= new AdminGrupo((TcManticGruposDto)DaoFactory.getInstance().findById(TcManticGruposDto.class, (Long)this.attrs.get("idGrupo")));
          break;
      } // switch
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;
    try {			
			eaccion= (EAccion) this.attrs.get("accion");
			transaccion = new Transaccion(this.adminGrupo.getGrupo(), this.adminGrupo.getClientes());
			if (transaccion.ejecutar(eaccion)) {
				if(eaccion.equals(EAccion.AGREGAR))
				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
				JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" el grupo de clientes de forma correcta."), ETipoMensaje.INFORMACION);
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el grupo de clientes.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
    return (String)this.attrs.get("retorno");
  } // doCancelar
	
	public void doAddCliente() {
		this.adminGrupo.add();
	}
	
	public void doRemoveCliente() {
		this.adminGrupo.remove((Cliente)this.attrs.get("seleccionado"));
	}
	
}