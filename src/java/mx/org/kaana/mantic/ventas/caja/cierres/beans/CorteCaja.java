package mx.org.kaana.mantic.ventas.caja.cierres.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.CargarCorte;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/03/2019
 *@time 01:14:28 AM 
 *@author Six Developer 2016 <six.developer@kaana.org.mx>
 */

public class CorteCaja implements Serializable {
  
  private static final long serialVersionUID= 1578963354789632541L;
	private Long idCierre;
	private Long idCierreNuevo;
	private Entity resumenCorte;
  private List<Entity> garantias;
  private List<Entity> abonos;
  private List<Entity> retiros;
  private List<Entity> fondoInicial;
  private List<Entity> fondoEfectivoFinal;
  private List<Entity> aperturaCaja;
  private List<Entity> cajerosCierre;
  private List<Entity> diferenciasMediosPagos;

  public CorteCaja() {
    this(new Long(-1L),new Long(-1L));
  }
  
  public CorteCaja(Long idCierre, Long idCierreNuevo) {
    this(idCierre,idCierreNuevo,null,new ArrayList<Entity>(),new ArrayList<Entity>(),new ArrayList<Entity>(),new ArrayList<Entity>(),new ArrayList<Entity>(), new ArrayList<Entity>(), new ArrayList<Entity>(), new ArrayList<Entity>());
  }

  public CorteCaja(Long idCierre, Long idCierreNuevo,Entity resumenCorte, List<Entity> garantias, List<Entity> abonos, List<Entity> retiros, List<Entity> fondoInicial, List<Entity> fondoEfectivoFinal, List<Entity> aperturaCaja, List<Entity> cajerosCierre, List<Entity> diferenciasMediosPagos) {
    this.idCierre = idCierre;
    this.idCierreNuevo = idCierreNuevo;
    this.resumenCorte = resumenCorte;
    this.garantias = garantias;
    this.abonos = abonos;
    this.retiros = retiros;
    this.fondoInicial = fondoInicial;
    this.fondoEfectivoFinal = fondoEfectivoFinal;
    this.aperturaCaja = aperturaCaja;
    this.cajerosCierre = cajerosCierre;
    this.diferenciasMediosPagos = diferenciasMediosPagos;
    init();		
  }

  public Long getIdCierre() {
    return idCierre;
  }

  public void setIdCierre(Long idCierre) {
    this.idCierre = idCierre;
  }

  public Long getIdCierreNuevo() {
    return idCierreNuevo;
  }

  public void setIdCierreNuevo(Long idCierreNuevo) {
    this.idCierreNuevo = idCierreNuevo;
  }

  public Entity getResumenCorte() {
    return resumenCorte;
  }

  public void setResumenCorte(Entity resumenCorte) {
    this.resumenCorte = resumenCorte;
  }

  public List<Entity> getGarantias() {
    return garantias;
  }

  public void setGarantias(List<Entity> garantias) {
    this.garantias = garantias;
  }

  public List<Entity> getAbonos() {
    return abonos;
  }

  public void setAbonos(List<Entity> abonos) {
    this.abonos = abonos;
  }

  public List<Entity> getRetiros() {
    return retiros;
  }

  public void setRetiros(List<Entity> retiros) {
    this.retiros = retiros;
  }

  public List<Entity> getFondoInicial() {
    return fondoInicial;
  }

  public void setFondoInicial(List<Entity> fondoInicial) {
    this.fondoInicial = fondoInicial;
  }

  public List<Entity> getFondoEfectivoFinal() {
    return fondoEfectivoFinal;
  }

  public void setFondoEfectivoFinal(List<Entity> fondoEfectivoFinal) {
    this.fondoEfectivoFinal = fondoEfectivoFinal;
  }

  public List<Entity> getCajerosCierre() {
    return cajerosCierre;
  }

  public void setCajerosCierre(List<Entity> cajerosCierre) {
    this.cajerosCierre = cajerosCierre;
  }

  public List<Entity> getAperturaCaja() {
    return aperturaCaja;
  }

  public void setAperturaCaja(List<Entity> aperturaCaja) {
    this.aperturaCaja = aperturaCaja;
  }

  public List<Entity> getDiferenciasMediosPagos() {
    return diferenciasMediosPagos;
  }

  public void setDiferenciasMediosPagos(List<Entity> diferenciasMediosPagos) {
    this.diferenciasMediosPagos = diferenciasMediosPagos;
  }
    
  private void init(){
		CargarCorte corte                = null;
		try {
			corte= new CargarCorte(this.idCierre, this.idCierreNuevo);
			this.resumenCorte= corte.toCargaResumen();
			this.garantias = corte.toGarantias();
      this.abonos = corte.toAbonos();
      this.retiros = corte.toRetiros();
      this.fondoInicial = corte.tofondoInicial();
      this.fondoEfectivoFinal = corte.toFondoEfectivoFinal();
      this.aperturaCaja = corte.toAperturaCaja();
      this.cajerosCierre = corte.toCajerosCierre();
      this.diferenciasMediosPagos = corte.toDiferenciasVsCapturado();
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // init
  
}
