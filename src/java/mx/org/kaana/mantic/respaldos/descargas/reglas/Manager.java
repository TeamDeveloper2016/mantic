package mx.org.kaana.mantic.respaldos.descargas.reglas;

import java.io.File;
import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EEtapaServidor;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.libs.ftp.Ftp;
import mx.org.kaana.libs.ftp.FtpProperties;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.mantic.db.dto.TcManticDescargasDto;
import mx.org.kaana.mantic.enums.ERespuesta;
import mx.org.kaana.mantic.enums.EServicios;
import mx.org.kaana.mantic.respaldos.reglas.Transaccion;
import mx.org.kaana.mantic.ws.consumible.reglas.Peticion;
import mx.org.kaana.mantic.ws.publicar.beans.Respuesta;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Manager implements Serializable{
	
	private static final long serialVersionUID= 8070434683370448314L;
	private static final Log LOG              = LogFactory.getLog(Manager.class);
	private static final String USER          = "descargas.user.";
	private static final String PASSWORD      = "descargas.pass.";
	private String alias;
	private Long size;
	
	public boolean execute() throws Exception{
		boolean regresar   = false;		
		Respuesta respuesta= null;
		try {			
			respuesta= toPeticion(EServicios.ARGENTINA.getNombre());
			if(respuesta.getCodigo().equals(ERespuesta.CORRECTO.getCodigo()))
				regresar= validaUltimoRespaldo();			
			else
				LOG.info(respuesta.getDescripcion());
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	}	// execute
	
	private boolean validaUltimoRespaldo() throws Exception{
		boolean regresar   = false;		
		Respuesta respuesta= null;
		try {
			respuesta= toPeticion(EServicios.MEXICO.getNombre());
			if(respuesta.getCodigo().equals(ERespuesta.CORRECTO.getCodigo())){
				regresar= procesarDescarga(respuesta);
			} // if			
			LOG.info(respuesta.getDescripcion());
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch
		return regresar;		
	} // validaUltimoRespaldo
	
	private boolean procesarDescarga(Respuesta respuesta) throws Exception{
		boolean regresar       = true;
		Entity respaldo        = null;
		Transaccion transaccion= null;
		try {
			respaldo= (Entity) DaoFactory.getInstance().toEntity("TcManticDescargasDto", "identically", Variables.toMap("nombre~".concat(respuesta.getNombre())));
			if(respaldo== null){
				if(download(respuesta)){
					transaccion= new Transaccion(loadDescarga(respuesta));
					regresar= transaccion.ejecutar(EAccion.REGISTRAR);
				} // if
			} // if
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // procresarDescarga
	
	private TcManticDescargasDto loadDescarga(Respuesta respuesta){
		TcManticDescargasDto regresar= null;
		try {
			regresar= new TcManticDescargasDto();
			regresar.setIdUsuario(1L);
			regresar.setAlias(this.alias);
			regresar.setNombre(respuesta.getNombre());
			regresar.setObservaciones(respuesta.getDescripcion());
			regresar.setRuta(respuesta.getRuta());
			regresar.setTamanio(this.size);			
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // loadDescarga
	
	private boolean download(Respuesta respuesta){
		boolean regresar= true;
		Ftp ftp         = null;		
		File file       = null;		
		try {
			ftp= new Ftp(loadProperties());
			this.alias= Configuracion.getInstance().getPropiedadSistemaServidor("descargas").concat(respuesta.getRuta());				
			file= new File(this.alias.concat(respuesta.getNombre()));
			if(!file.exists())
				file.mkdirs();		
			file.getTotalSpace();
			regresar= ftp.download("/".concat(respuesta.getRuta().concat(respuesta.getNombre())), this.alias.concat(respuesta.getNombre()), null);								
			if(file.exists())
				this.size= file.getTotalSpace();
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			regresar= false;
		} // catch		
		return regresar;
	} // download
	
	private FtpProperties loadProperties(){
		FtpProperties regresar= null;
		String[] host         = null;		
		String user           = null;		
		try {
			user= TcConfiguraciones.getInstance().getPropiedad(USER.concat(EEtapaServidor.PRODUCCION.toLowerCase()));
			host= user.split("@");
			regresar= new FtpProperties();
			regresar.setHost("ftp.".concat(host[1]));
			regresar.setUser(user);
			regresar.setPassword(TcConfiguraciones.getInstance().getPropiedad(PASSWORD.concat(EEtapaServidor.PRODUCCION.toLowerCase())));
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // loadProperties
	
	private Respuesta toPeticion(String nombre) throws Exception{
		return toPeticion(nombre, new Object[]{});
	} // toPeticion
	
	private Respuesta toPeticion(String nombre, Object[] params) throws Exception{
		Respuesta regresar= null;
		Peticion peticion = null;
		try {
			peticion= new Peticion(nombre, params);
			regresar= peticion.getBeanRespuesta();
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch
		return regresar;
	} // toPeticion
}
