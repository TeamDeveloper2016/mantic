package mx.org.kaana.mantic.ventas.caja.cierres.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/03/2019
 *@time 01:14:28 AM 
 *@author Six Developer 2016 <sic.developer@kaana.org.mx>
 */

public class CorteCaja implements Serializable {
  
  private static final long serialVersionUID= 1578963354789632541L;
	private Long idKey;
	private Entity resumenCorte;
  private List<Entity> garantias;
  private List<Entity> abonos;
  private List<Entity> retiros;
  private List<Entity> fondoInicial;
  private List<Entity> fondoEfectivoFinal;

  public CorteCaja() {
    this(new Long(-1L));
  }
  
  public CorteCaja(Long idKey) {
    this(new Long(-1L),null,new ArrayList<Entity>(),new ArrayList<Entity>(),new ArrayList<Entity>(),new ArrayList<Entity>(),new ArrayList<Entity>());
  }

  public CorteCaja(Long idKey, Entity resumenCorte, List<Entity> garantias, List<Entity> abonos, List<Entity> retiros, List<Entity> fondoInicial, List<Entity> fondoEfectivoFinal) {
    this.idKey = idKey;
    this.resumenCorte = resumenCorte;
    this.garantias = garantias;
    this.abonos = abonos;
    this.retiros = retiros;
    this.fondoInicial = fondoInicial;
    this.fondoEfectivoFinal = fondoEfectivoFinal;
    init();		
  }
  
  public Long getIdKey() {
    return idKey;
  }

  public void setIdKey(Long idKey) {
    this.idKey = idKey;
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
  
  private void init(){
		//CargarCorte corte                = null;
		try {
			/*corte= new CargarCorte(this.idKey);
			this.resumenCorte= corte.toCargaResumen();
			this.garantias = corte.toGarantias();
      this.abonos = corte.toAbonos();
      this.retiros = corte.toRetiros();
      this.fondoInicial = corte.tofondoInicial();
      this.fondoEfectivoFinal = corte.toFondoEfectivoFinal();
      */
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // init
  
}
