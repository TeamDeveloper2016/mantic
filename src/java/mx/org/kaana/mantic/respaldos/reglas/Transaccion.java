package mx.org.kaana.mantic.respaldos.reglas;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
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

	public Transaccion() {
	 this("GENERACION DEL RESPALDO AUTOMATICO");
	}

	public Transaccion(String observacion) {
		this.observacion= observacion;
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
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la nota de entrada.");
			switch(accion) {
				case AGREGAR:
					TcManticRespaldosDto dto= toBackup();
					if(dto!= null)
					  regresar= DaoFactory.getInstance().insert(sesion, dto)>= 1L;
					break;
				case ELIMINAR:
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
		} // catch		
		LOG.info("Se genero de forma correcta la nota de entrada: ");
		return regresar;
	}	// ejecutar


	private TcManticRespaldosDto toBackup() throws Exception {
		TcManticRespaldosDto regresar= null;
		StringBuilder path= new StringBuilder();  
		StringBuilder sb  = new StringBuilder();
		StringBuilder name= new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		sb.append(Configuracion.getInstance().getPropiedadSistemaServidor("respaldos"));
		sb.append(JsfBase.getAutentifica().getEmpresa().getNombreCorto().replaceAll(" ", ""));
		sb.append("/");
		sb.append(Calendar.getInstance().get(Calendar.YEAR));
		sb.append("/");
		sb.append(Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase());
		sb.append("/");
		path.append(sb.toString());
		File result= new File(path.toString());		
		if (!result.exists())
			result.mkdirs();
		name.append("mantic");
    name.append(Constantes.ARCHIVO_PATRON_SEPARADOR);
    name.append(Fecha.formatear("yyyyMMddhhmmssS", Calendar.getInstance().getTime()));
    name.append(".");
    path.append(name.toString().concat(EFormatos.SQL.name().toLowerCase()));
		// C:\Software\Server\MariaDB-10_1\bin\mysqldump -h 127.0.0.1 -u mantic --password=mantic --compact --databases mantic --add-drop-table --complete-insert --extended-insert --skip-comments -r d:/temporal/hola.sql
		String server= "";
		switch(Configuracion.getInstance().getEtapaServidor()) {
			case DESARROLLO:
  			server= "C:/Software/Server/MariaDB-10_1/bin/mysqldump -h 127.0.0.1 -u mantic --password=mantic";
				break;
			case PRUEBAS:
        server= "mysqldump -h cpanel.bonanza.jvmhost.net -u bonanzaj_tester --password=tester2018";
				break;
			case PRODUCCION:
        server= "mysqldump -h cpanel.bonanza.jvmhost.net -u bonanzaj_master --password=master2018";
				break;
		} // swtich
		Process runtimeProcess = Runtime.getRuntime().exec(server.concat(" --compact --databases mantic --add-drop-table --complete-insert --extended-insert --skip-comments -r ").concat(path.toString()));
		int processComplete = runtimeProcess.waitFor();
		/*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
		if (processComplete== 0) {
			String[] files= new String[1];
			files[0]= path.toString();
			Zip zip= new Zip();
			zip.setDebug(true);
			zip.setEliminar(false);
			int token= Configuracion.getInstance().getPropiedadSistemaServidor("respaldos").length();
			zip.compactar(sb.toString().concat(name.toString()).concat(EFormatos.ZIP.name().toLowerCase()), token, files);
			File file= new File(zip.getNombre());
			regresar= new TcManticRespaldosDto(sb.toString().substring(token), file.getTotalSpace(), JsfBase.getIdUsuario(), this.observacion, -1L, zip.getNombre(), name.toString().concat(EFormatos.ZIP.name().toLowerCase()));
			file= new File(files[0]);
			file.delete();
		} // if
		else
		  new RuntimeException("Ocurrio un error al realizar el resplado de la base de datos");
		return regresar;
	}
	
	public static void  main(String ... args) throws Exception {
		Transaccion transaccion= new Transaccion();
		transaccion.ejecutar(EAccion.AGREGAR);
	}
	
} 