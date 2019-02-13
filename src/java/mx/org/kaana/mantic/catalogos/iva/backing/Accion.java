package mx.org.kaana.mantic.catalogos.iva.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.mantic.catalogos.iva.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticHistorialIvaDto;


@Named(value = "manticCatalogosIvaAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
	private TcManticHistorialIvaDto iva;

	public TcManticHistorialIvaDto getIva() {
		return this.iva;
	}

	public void setIva(TcManticHistorialIvaDto iva) {
		this.iva = iva;
	}	

	@PostConstruct
  @Override
  protected void init() {		
    try {
      if(JsfBase.getFlashAttribute("accion")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("aplicar", false);
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idHistorialIva", JsfBase.getFlashAttribute("idHistorialIva"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    EAccion eaccion    = null;
    Long idHistorialIva= -1L;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
          this.iva= new TcManticHistorialIvaDto(JsfBase.getAutentifica().getPersona().getIdUsuario(), -1L, "", JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), 16.00);
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          idHistorialIva= (Long)this.attrs.get("idHistorialIva");
          this.iva= (TcManticHistorialIvaDto)DaoFactory.getInstance().findById(TcManticHistorialIvaDto.class, idHistorialIva);
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
			transaccion = new Transaccion(this.iva, (Boolean)this.attrs.get("aplicar"));
			if (transaccion.ejecutar(eaccion)) {
				regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
				UIBackingUtilities.execute("janal.alert('Se aplico el IVA al catalogo de articulos');");
				JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" el registro del IVA de forma correcta."), ETipoMensaje.INFORMACION);
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el IVA.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
    return (String)this.attrs.get("retorno");
  } // doAccion
	
}