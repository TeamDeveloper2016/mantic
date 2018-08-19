package mx.org.kaana.mantic.catalogos.cajas.backing;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EBooleanos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.catalogos.cajas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.ventas.caja.cierres.beans.Denominacion;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Cierre;
import org.primefaces.context.RequestContext;

@Named(value = "manticCatalogosCajasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 8635741239546845369L;
  private List<Denominacion> fondos;
	private EAccion accion;
  private TcManticCierresCajasDto cierreCaja;
  private TcManticCierresDto cierre;

	public List<Denominacion> getFondos() {
		return fondos;
	}
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idCaja", JsfBase.getFlashAttribute("idCaja"));
			this.attrs.put("activa", false);
      if(JsfBase.getFlashAttribute("accion")== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
      this.accion = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			this.attrs.put("limite", 0D);
			this.attrs.put("efectivo", 0D);
			this.attrs.put("disponible", 0D);
			this.doLoad();   					
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	public void doLoad() {
    EAccion eaccion = null;
    Long idCaja     = -1L;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
  		this.attrs.put("idEfectivo", 2);
      switch (eaccion) {
        case AGREGAR:
					this.attrs.put("caja", new TcManticCajasDto());     
          this.attrs.put("idCierre", -1);
          this.fondos= (List<Denominacion>)DaoFactory.getInstance().toEntitySet(Denominacion.class, "TcManticMonedasDto", "denominacion", this.attrs);
          this.cierre= new TcManticCierresDto("", -1L, 2L, JsfBase.getIdUsuario(), 1L, "ESTA CAJA SE APERTURO DESDE EL ALTA DE CAJAS", 1L, new Long(Fecha.getAnioActual()));
          break;
        case MODIFICAR:
        case CONSULTAR:
          idCaja = Long.valueOf(this.attrs.get("idCaja").toString());
          this.attrs.put("caja", DaoFactory.getInstance().findById(TcManticCajasDto.class, idCaja));
					this.attrs.put("activa", ((TcManticCajasDto)this.attrs.get("caja")).getIdActiva().equals(EBooleanos.SI.getIdBooleano()));
          this.attrs.put("estatusAbierto", 1);
          Entity cierreInicial = (Entity)DaoFactory.getInstance().toEntity("VistaCierresCajasDto", "cierreVigente", attrs);
          this.cierre = (TcManticCierresDto) DaoFactory.getInstance().findById(TcManticCierresDto.class, cierreInicial.getKey());
          this.attrs.put("idCierre", this.cierre.getIdCierre());
          this.cierreCaja = (TcManticCierresCajasDto) DaoFactory.getInstance().findFirst(TcManticCierresCajasDto.class,  "cierreCajaEfectivo",this.attrs);
          this.attrs.put("disponible", this.cierreCaja.getDisponible());		
          this.fondos= (List<Denominacion>)DaoFactory.getInstance().toEntitySet(Denominacion.class, "TcManticMonedasDto", "denominacionActual", this.attrs);
          break;
      } // switch 			
      this.doCalculate();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {
    Transaccion transaccion = null;
		TcManticCajasDto caja   = null;
    String regresar         = null;
    try {
			caja= (TcManticCajasDto) this.attrs.get("caja");
			caja.setIdActiva(Boolean.valueOf(this.attrs.get("activa").toString()) ? EBooleanos.SI.getIdBooleano() : EBooleanos.NO.getIdBooleano());
			caja.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			caja.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
      transaccion = new Transaccion(caja,caja.getIdCaja(),(Double)this.attrs.get("disponible"), this.cierre, null, this.fondos);
      if(((EAccion) this.attrs.get("accion")).equals(EAccion.MODIFICAR)){
        this.cierreCaja.setDisponible((Double)this.attrs.get("disponible"));
        transaccion.setCierreCaja(this.cierreCaja);
      }
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro la caja de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al registrar la caja.", ETipoMensaje.ERROR);      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doAccion
  
  	public void doCalculate() {
		Double sum  = 0D;
		for (Denominacion fondo: this.fondos) {
			fondo.setImporte(Numero.toRedondearSat(fondo.getDenominacion()* fondo.getCantidad()));
			sum+= fondo.getImporte();
		} // for
    this.attrs.put("disponible", Numero.toRedondearSat(sum));		
    this.attrs.put("efectivo", Numero.toRedondearSat(sum));		
	}
}
