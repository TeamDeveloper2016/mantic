package mx.org.kaana.mantic.respaldos.reglas;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
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
	
	private String messageError;

	public Transaccion() {
	}
	
	protected void setMessageError(String messageError) {
		this.messageError=messageError;
	}

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar                     = false;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la nota de entrada.");
			switch(accion) {
				case AGREGAR:
					break;
				case MODIFICAR:
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


	private void toBackup() throws Exception {
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
		name.append("mantic_dump");
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
			sb.append(name.toString().concat(EFormatos.ZIP.name().toLowerCase()));
			String[] files= new String[1];
			files[0]= path.toString();
			Zip zip= new Zip();
			zip.setDebug(true);
			zip.setEliminar(false);
			zip.compactar(sb.toString(), Configuracion.getInstance().getPropiedadSistemaServidor("respaldos").length(), files);
		} // if
		else
		  new RuntimeException("Ocurrio un error al realizar el resplado de la base de datos");
	}
	
	public static void  main(String ... args) throws Exception {
		Transaccion transaccion= new Transaccion();
		transaccion.ejecutar(EAccion.AGREGAR);
	}
	
} 