package mx.org.kaana.mantic.respaldos.reglas;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.faces.context.FacesContext;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EBooleanos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.db.dto.TcManticControlRespaldosDto;
import mx.org.kaana.mantic.db.dto.TcManticDescargasDto;
import mx.org.kaana.mantic.db.dto.TcManticRespaldosDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx implements Serializable {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
	private static final long serialVersionUID=-8321704151351839833L;
	
	private String observacion;
	private String messageError;
	private TcManticDescargasDto descarga;
	private TcManticRespaldosDto respaldo;

	public Transaccion() {
	 this("GENERACION DEL RESPALDO AUTOMATICO");
	}

	public Transaccion(String observacion) {
		this.observacion= observacion;
	}

	public Transaccion(TcManticDescargasDto descarga) {
		this.descarga = descarga;
	}
	
	public Transaccion(TcManticRespaldosDto respaldo) {
		this.respaldo = respaldo;
	}
	
	protected void setMessageError(String messageError) {
		this.messageError=messageError;
	}

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el respaldo de la base de datos.");
			switch(accion) {
				case AGREGAR:
					TcManticRespaldosDto dto= this.toBackup();
					if(dto!= null){						
					  regresar= DaoFactory.getInstance().insert(sesion, dto)>= 1L;
						if(regresar)
							depurarRespaldos(sesion);
					} // if
					break;
				case REGISTRAR:
					regresar= DaoFactory.getInstance().insert(sesion, this.descarga)>= 1L;
					if(regresar)
						depurarDescargas(sesion);					
					break;
				case BAJAR:
					TcManticControlRespaldosDto control= new TcManticControlRespaldosDto(JsfBase.getIdUsuario(), this.respaldo.getIdRespaldo(), -1L);
					regresar= DaoFactory.getInstance().insert(sesion, control)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		LOG.info("Se genero de forma el respaldo de la base de datos !");
		return regresar;
	}	// ejecutar


	private TcManticRespaldosDto toBackup() throws Exception {
		TcManticRespaldosDto regresar= null;
		StringBuilder path= new StringBuilder();  
		StringBuilder sb  = new StringBuilder();
		StringBuilder name= new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		sb.append(Configuracion.getInstance().getPropiedadSistemaServidor("respaldos"));
		//sb.append(JsfBase.getAutentifica().getEmpresa().getNombreCorto().replaceAll(" ", ""));
		//sb.append("/");
		sb.append(Calendar.getInstance().get(Calendar.YEAR));
		sb.append("/");
		sb.append(Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase());
		sb.append("/");
		path.append(sb.toString());
		File result= new File(path.toString());		
		if (!result.exists())
			result.mkdirs();
		LOG.info("Ruta generada: "+ path.toString());
		name.append("mantic");
    name.append(Constantes.ARCHIVO_PATRON_SEPARADOR);
    name.append(Fecha.formatear("yyyyMMddhhmmssS", Calendar.getInstance().getTime()));
    name.append(".");
    path.append(name.toString().concat(EFormatos.SQL.name().toLowerCase()));
		// C:\Software\Server\MariaDB-10_1\bin\mysqldump -h 127.0.0.1 -u mantic --password=mantic --compact --databases mantic --add-drop-table --complete-insert --extended-insert --skip-comments -r d:/temporal/hola.sql
		String server= "";
		switch(Configuracion.getInstance().getEtapaServidor()) {
			case DESARROLLO:
  			server= "C:/Software/Server/MariaDB-10_1/bin/mysqldump -h 127.0.0.1 -u mantic --password=mantic --databases mantic ";
				break;
			case PRUEBAS:
        server= "mysqldump -h localhost -u bonanzaj_tester --password=tester2018 --databases bonanzaj_training ";
				break;
			case CAPACITACION:
        server= "mysqldump -h localhost -u ferreter_track --password=track2018 --databases ferreter_testing ";
				break;
			case PRODUCCION:
        server= "mysqldump -h localhost -u ferreter_super --password=super2018 --databases ferreter_production ";
				break;
		} // swtich
		LOG.info("Proceso a generar: "+ server.concat(" --compact --add-drop-table --complete-insert --extended-insert -r ").concat(path.toString()));
		Process runtimeProcess = Runtime.getRuntime().exec(server.concat(" --compact --add-drop-table --complete-insert --extended-insert -r ").concat(path.toString()));
		LOG.info("Proceso en ejecucion ...");
		int processComplete = runtimeProcess.waitFor();
		LOG.info("Resultado del proceso: "+ processComplete);
		/*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
		if (processComplete== 0) {
			String[] files= new String[1];
			files[0]= path.toString();
			Zip zip= new Zip();
			zip.setDebug(true);
			zip.setEliminar(false);
			int token= Configuracion.getInstance().getPropiedadSistemaServidor("respaldos").length();
   		LOG.info("Compactar archivo: "+ sb.toString().concat(name.toString()).concat(EFormatos.ZIP.name().toLowerCase()));
			zip.compactar(sb.toString().concat(name.toString()).concat(EFormatos.ZIP.name().toLowerCase()), token, files);
			File file= new File(zip.getNombre());
			regresar= new TcManticRespaldosDto(sb.toString().substring(token), file.getTotalSpace(), FacesContext.getCurrentInstance()== null || FacesContext.getCurrentInstance().getExternalContext()== null || FacesContext.getCurrentInstance().getExternalContext().getRequest()== null || JsfBase.getAutentifica()== null? 
				1L: JsfBase.getIdUsuario(), this.observacion, -1L, zip.getNombre(), name.toString().concat(EFormatos.ZIP.name().toLowerCase()));
			file= new File(files[0]);
			file.delete();
   		LOG.info("Eliminar archivo: "+ files[0]);
		} // if
		else
		  new RuntimeException("Ocurrio un error al realizar el resplado de la base de datos");
		return regresar;
	}
	
	private void depurarRespaldos(Session sesion) throws Exception{
		List<Entity> respaldos= null;
		try {
			respaldos= toAllRespaldos(sesion);
			if(!respaldos.isEmpty()){
				for(int count=0; count< respaldos.size(); count++){
					if(count>= 15){
						if(desactivarRespaldo(sesion, respaldos.get(count)))
							deleteFile(respaldos.get(count));
					} // if
				} // for
			} // if
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
	} // depurarRespaldos
	
	private void depurarDescargas(Session sesion) throws Exception{
		List<Entity> descargas= null;
		try {
			descargas= toAllDescargas(sesion);
			if(!descargas.isEmpty()){
				for(int count=0; count< descargas.size(); count++){
					if(count>= 30){
						if(desactivarDescarga(sesion, descargas.get(count)))
							deleteFileDescarga(descargas.get(count));
					} // if
				} // for
			} // if
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
	} // depurarRespaldos
	
	private List<Entity> toAllRespaldos(Session sesion) throws Exception{
		List<Entity>regresar= null;
		try {
			regresar= DaoFactory.getInstance().toEntitySet(sesion, "TcManticRespaldosDto", "historial", Collections.EMPTY_MAP, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // toAllRespaldos
	
	private List<Entity> toAllDescargas(Session sesion) throws Exception{
		List<Entity>regresar= null;
		try {
			regresar= DaoFactory.getInstance().toEntitySet(sesion, "TcManticDescargasDto", "historial", Collections.EMPTY_MAP, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // toAllRespaldos
	
	private boolean desactivarRespaldo(Session sesion, Entity entity) throws Exception{
		boolean regresar             = false;
		TcManticRespaldosDto respaldo= null;
		try {
			respaldo= (TcManticRespaldosDto) DaoFactory.getInstance().findById(sesion, TcManticRespaldosDto.class, entity.getKey());
			respaldo.setActivo(EBooleanos.NO.getIdBooleano());
			respaldo.setEliminado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			regresar= DaoFactory.getInstance().update(sesion, respaldo)>= 1L;
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // desactivarRespaldo
	
	private boolean desactivarDescarga(Session sesion, Entity entity) throws Exception{
		boolean regresar                   = false;
		TcManticDescargasDto recordDescarga= null;
		try {
			recordDescarga= (TcManticDescargasDto) DaoFactory.getInstance().findById(sesion, TcManticRespaldosDto.class, entity.getKey());
			recordDescarga.setActivo(EBooleanos.NO.getIdBooleano());
			recordDescarga.setEliminado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			regresar= DaoFactory.getInstance().update(sesion, recordDescarga)>= 1L;
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // desactivarRespaldo
	
	private void deleteFile(Entity respaldo){
		File respaldoFile= null;
		try {
			respaldoFile= new File(Configuracion.getInstance().getPropiedadSistemaServidor("respaldos").concat(respaldo.toString("ruta")).concat(respaldo.toString("nombre")));
			if(respaldoFile.exists())
				respaldoFile.delete();
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
	} // deleteFile
	
	private void deleteFileDescarga(Entity descarga){
		File descargaFile= null;
		try {
			descargaFile= new File(Configuracion.getInstance().getPropiedadSistemaServidor("descargas").concat(descarga.toString("ruta")).concat(descarga.toString("nombre")));
			if(descargaFile.exists())
				descargaFile.delete();
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
	} // deleteFile
	
	public static void  main(String ... args) throws Exception {
		Transaccion transaccion= new Transaccion();
		transaccion.ejecutar(EAccion.AGREGAR);
	}	
} 