package mx.org.kaana.jobs;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 22-sep-2015
 * @time 9:11:42
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.reportes.reglas.IReporte;
import mx.org.kaana.kajool.seguridad.quartz.Especial;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.correos.beans.Attachment;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import mx.org.kaana.mantic.correos.reglas.IBaseAttachment;
import mx.org.kaana.mantic.enums.EEstatusFacturas;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.reglas.Transferir;
import mx.org.kaana.mantic.ventas.caja.beans.Facturacion;
import mx.org.kaana.mantic.ventas.caja.reglas.Transaccion;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Timbrado implements Job, Serializable {

	private static final Log LOG=LogFactory.getLog(Timbrado.class);
	private static final long serialVersionUID=1809037806413388478L;
	private String nameFacturaPdf;
	private Reporte reporte;
	private IReporte ireporte;

	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		List<Facturacion> pendientes=null;
		Transaccion transaccion=null;
		String correos=null;
		try {
			if (!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion() && validateHora()) {
				pendientes= this.toFacturasPendientes();
				for (Facturacion factura: pendientes) {
					try {
						correos=toCorreosCliente(factura.getIdCliente());
						if (!Cadena.isVacio(correos)) {
							factura.setCorreos(correos);
							transaccion=new Transaccion(factura);
							if (transaccion.ejecutar(EAccion.GENERAR)) {
								this.doSendMail(factura);
								LOG.info("Se realizo la facturación de forma correcta");
							} // if
							else {
								LOG.error("Ocurrio un error al realizar la facturación");
							}
						} // if
					}
					catch (Exception ex) {
						LOG.error("Ocurrio un error al realizar la facturación "+ ex);
					} // catch
				} // for
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			LOG.error("Ocurrio un error al realizar el respaldo de la BD "+ e);
		} // catch	
	} // execute

	private boolean validateHora() {
		boolean regresar=true;
		Calendar calendario=null;
		try {
			calendario=Calendar.getInstance();
			regresar=calendario.get(Calendar.HOUR_OF_DAY)>8&&calendario.get(Calendar.HOUR_OF_DAY)<21;
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // validateHora

	private List<Facturacion> toFacturasPendientes() throws Exception {
		List<Facturacion> regresar=null;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idEstatus", EEstatusFacturas.AUTOMATICO.getIdEstatusFactura());
			regresar=DaoFactory.getInstance().toEntitySet(Facturacion.class, "VistaVentasDto", "facturacion", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toFacturasPendientes

	private String toCorreosCliente(Long idCliente) throws Exception {
		StringBuilder regresar=null;
		MotorBusqueda motor=null;
		List<ClienteTipoContacto> contactos=null;
		try {
			regresar=new StringBuilder("");
			motor=new MotorBusqueda(-1L, idCliente);
			contactos=motor.toClientesTipoContacto();
			for (ClienteTipoContacto contacto : contactos) {
				if (contacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey())) {
					regresar.append(contacto.getValor()).append(",");
				} // if
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return Cadena.isVacio(regresar) ? regresar.toString() : regresar.substring(0, regresar.length()-1);
	} // toCorreosCliente

	public void doSendMail(Facturacion facturacion) {
		Map<String, Object> params=null;
		List<Attachment> files=null;
		String[] emails=null;
		File factura=null;
		try {
			this.reporte=new Reporte();
			params=new HashMap<>();
			params.put("header", "...");
			params.put("footer", "...");
			params.put("empresa", facturacion.getNombreEmpresa());
			params.put("tipo", "Factura");
			params.put("razonSocial", facturacion.getRazonSocial());
			params.put("correo", ECorreos.FACTURACION.getEmail());
			factura=toXml(facturacion.getIdFactura());
			this.doReporte("FACTURAS_FICTICIAS_DETALLE", facturacion);
			Attachment attachments=new Attachment(Especial.getInstance().getPath().substring(0, Especial.getInstance().getPath().length()-1), this.reporte.getNombre(), Boolean.FALSE, true);
			files=new ArrayList<>();
			files.add(attachments);
			files.add(new Attachment(factura, Boolean.FALSE));
			files.add(new Attachment(Especial.getInstance().getPath(), "logo", ECorreos.FACTURACION.getImages().concat("logo.png"), Boolean.TRUE));
			params.put("attach", attachments.getId());
			emails=new String[]{facturacion.getCorreos()};
			for (String item : emails) {
				try {
					if (!Cadena.isVacio(item)) {
						IBaseAttachment notificar=new IBaseAttachment(ECorreos.FACTURACION, ECorreos.FACTURACION.getEmail(), item, "controlbonanza@gmail.com,jorge.alberto.vs.10@gmail.com", "Ferreteria Bonanza - Factura", params, files);
						LOG.info("Enviando correo a la cuenta: "+item);
						notificar.send();
					} // if	
				} // try
				finally {
					if (attachments.getFile().exists()) {
						LOG.info("Eliminando archivo temporal: "+attachments.getAbsolute());
						// user.getFile().delete();
					} // if	
				} // finally	
			} // for
			LOG.info("Se envio el correo de forma exitosa");
			if (facturacion.getCorreos().length()>0) {
				LOG.info("Se envió el correo de forma exitosa.");
			}
		} // try // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(files);
		} // finally
	} // doSendMail	

	protected File toXml(Long idFactura) throws Exception {
		File regresar=null;
		List<Entity> facturas=null;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idFactura", idFactura);
			facturas=DaoFactory.getInstance().toEntitySet("VistaFicticiasDto", "importados", params);
			for (Entity factura : facturas) {
				if (factura.toLong("idTipoArchivo").equals(1L)) {
					regresar=new File(factura.toString("ruta").concat(factura.toString("nombre")));
				}
				else {
					this.nameFacturaPdf=factura.toString("nombre");
				}
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toXml

	protected void doReporte(String nombre, Facturacion facturacion) throws Exception {
		Parametros comunes=null;
		Map<String, Object> params=null;
		Map<String, Object> parametros=null;
		EReportes reporteSeleccion=null;
		String path=null;
		try {
			if (facturacion.getIdFacturama()!=null&&facturacion.getSelloSat()==null) {
				Transferir transferir=null;
				try {
					transferir=new Transferir(facturacion.getIdFacturama());
					transferir.ejecutar(EAccion.PROCESAR);
				} // try
				catch (Exception e) {
					LOG.warn("La factura ["+facturacion.getIdFactura()+"] presento un problema al recuperar el sello digital ["+facturacion.getIdFacturama()+"]");
					Error.mensaje(e);
				} // catch
				finally {
					transferir=null;
				} // finally
			} // if      
			params=new HashMap<>();
			params.put("sortOrder", "order by tc_mantic_ventas.id_empresa, tc_mantic_clientes.id_cliente, tc_mantic_ventas.ejercicio, tc_mantic_ventas.orden");
			params.put("idFicticia", facturacion.getIdVenta());
			comunes=new Parametros(facturacion.getIdEmpresa(), -1L, -1L, facturacion.getIdCliente());
			parametros=comunes.getComunes();
			reporteSeleccion=EReportes.valueOf(nombre);
			parametros.put("ENCUESTA", facturacion.getNombreEmpresa().toUpperCase());
			parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
			parametros.put("REPORTE_ICON", Especial.getInstance().getPath().concat("resources/iktan/icon/acciones/"));
			this.ireporte=new ParametrosReporte(reporteSeleccion, params, parametros);
			this.reporte.toAsignarReporte(this.ireporte, this.nameFacturaPdf.replaceFirst(".pdf", ""));
			path=Especial.getInstance().getPath();
			this.reporte.setPrevisualizar(Boolean.FALSE);
			this.reporte.doAceptarSimple(path.substring(0, path.length()-2).concat(this.ireporte.getJrxml()).concat(".jasper"), path.substring(0, path.length()-2).concat(Constantes.RUTA_IMAGENES).concat(File.separator), path.substring(0, path.length()-1));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch	
	} // doReporte	
}
