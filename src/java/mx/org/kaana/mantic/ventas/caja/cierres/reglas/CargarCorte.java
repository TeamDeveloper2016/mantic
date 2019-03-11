package mx.org.kaana.mantic.ventas.caja.cierres.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/03/2019
 *@time 12:55:33 AM 
 *@author Six Developer 2016 <six.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.Constantes;

public class CargarCorte implements Serializable{

	private Long idCierre;
  protected Map<String, Object> attrs;

  public Map<String, Object> getAttrs() {
    return attrs;
  }

  public void setAttrs(Map<String, Object> attrs) {
    this.attrs = attrs;
  }

	public CargarCorte(Long idCierre, Long idCierreNuevo) {
		this.idCierre = idCierre;
    this.attrs= new HashMap<>();
    this.attrs.put("idCierre", this.idCierre);
    this.attrs.put("idCierreNuevo", idCierreNuevo);
	}

	public Entity toCargaResumen() throws Exception {
		Entity regresar= null;
		try {
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaCorteCierreCajaDto","resumenCorte", this.attrs);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCargaResumen

	public List<Entity> toGarantias() throws Exception {
		List<Entity> regresar= null;
		try {
			regresar= DaoFactory.getInstance().toEntitySet("VistaCorteCierreCajaDto", "garantias", this.attrs, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		return regresar;
	} // toGarantias
	
	public List<Entity> toAbonos() throws Exception {
		List<Entity> regresar= null;
		try {
			regresar= DaoFactory.getInstance().toEntitySet("VistaCorteCierreCajaDto", "abonosCaja", this.attrs, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		return regresar;
	} // toAbonos
	
	public List<Entity> toRetiros() throws Exception {
		List<Entity> regresar= null;
		try {
			regresar= DaoFactory.getInstance().toEntitySet("VistaCorteCierreCajaDto", "retirosCaja", this.attrs, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		return regresar;
	} // toRetiros
	
	public List<Entity> tofondoInicial() throws Exception {
		List<Entity> regresar= null;
		try {
			regresar= DaoFactory.getInstance().toEntitySet("VistaCorteCierreCajaDto", "fondoInicial", this.attrs, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		return regresar;
	} // tofondoInicial
	
	public List<Entity> toFondoEfectivoFinal() throws Exception {
		List<Entity> regresar= null;
		try {
			regresar= DaoFactory.getInstance().toEntitySet("VistaCorteCierreCajaDto", "efectivoFinal", this.attrs, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		return regresar;
	} // toFondoEfectivoFinal
  
	public List<Entity> toCajerosCierre() throws Exception {
		List<Entity> regresar= null;
		try {
			regresar= DaoFactory.getInstance().toEntitySet("VistaCorteCierreCajaDto", "cajerosCierre", this.attrs, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		return regresar;
	} // toCajerosCierre
  
	public List<Entity> toAperturaCaja() throws Exception {
		List<Entity> regresar= null;
		try {
			regresar= DaoFactory.getInstance().toEntitySet("VistaCorteCierreCajaDto", "aperturaCaja", this.attrs, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		return regresar;
	} // toAperturaCaja
  
	public List<Entity> toDiferenciasVsCapturado() throws Exception {
		List<Entity> regresar= null;
		try {
			regresar= DaoFactory.getInstance().toEntitySet("VistaCorteCierreCajaDto", "diferenciaVsCapturado", this.attrs, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		return regresar;
	} // toDiferenciasVsCapturado
	
}
