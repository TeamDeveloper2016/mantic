package mx.org.kaana.libs.correo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UIMessage;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 19/03/2015
 *@time 11:37:38 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class EditMail {
	 protected Map<String, Object> attrs;
	
	 public EditMail (Map<String, Object> attrs) {
		this.attrs = attrs;
	 }
	
	public void doNotificarCorreo(){
		Correo correo = null;
		StringBuilder titulo = new StringBuilder();	
		try {
		//	titulo.append(JsfBase.getAutentifica().getEncuesta().getNombreMayusculas());
			titulo.append(this.attrs.get("asuntoCorreo"));
			titulo.append("Esquema [ ");
			titulo.append(mx.org.kaana.libs.recurso.Configuracion.getInstance().getEtapaServidor().toUpperCase());
			titulo.append(" ]");
  		correo = new Correo(TcConfiguraciones.getInstance().getPropiedad("correo.user").concat(""),getCorreos(), titulo.toString());
			correo.setContenido(toMensajeHtml());
			correo.enviar();
			JsfUtilities.addMessage("Se realizo con exito el envio de la tabla");
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
	}
		
	private StringBuilder toMensajeHtml() throws Exception {
    StringBuilder regresar= new StringBuilder();
    regresar.append("<html><head>");
		regresar.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
		regresar.append("<title></title></head><body>");
		regresar.append(((String)this.attrs.get("textoCorreo")).replaceAll("ó", "&oacute;").replaceAll("á", "&aacute;").replaceAll("Á", "&Aacute;").replaceAll("É", "&Eacute;").replaceAll("é", "&eacute;").replaceAll("Í", "&Iacute;").replaceAll("í", "&iacute;").replaceAll("Ó", "&Oacute;").replaceAll("Ú", "&Uacute;").replaceAll("ú", "&uacute;").replaceAll("Ñ", "&Ntilde;"));
    regresar.append("<table> <tr>");
    regresar.append("<td text-align:center><br/>");
    regresar.append("Atte.");
    regresar.append("</td></tr>");
    regresar.append("<tr><td text-align:center><br/>");
    regresar.append("ADMINISTRADOR -WEB");
    regresar.append("</td></tr></table>");
    regresar.append("</body></html>");
    return regresar;
  }

	private String getCorreos() throws Exception {
		StringBuilder regresar = new StringBuilder();
		try {
			for(UISelectItem item : ((DualListModel<UISelectItem>)this.attrs.get("contactosDestinoCorreo")).getTarget()) {
				regresar.append(item.getLabel());
				regresar.append(",");
			} // for
			if(regresar.length()>0)
				regresar.deleteCharAt(regresar.length()-1);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar.toString();
	}
	
	public void doCargarContactosDestinoCorreo(){
		List<UISelectItem> agenda       = null;
		List<Entity> load               = null;
		List<UISelectItem> destinatarios= null;
		try {
			agenda = new ArrayList<>();
			this.attrs.put("asuntoCorreo","Total de claves operativas con cierre a nivel nacional");
			this.attrs.put("textoCorreo","<br><style type=\"text/css\">\n" +JsfUtilities.getExternalContext().getRequestParameterMap().get("styleText")+
				"</style>"+JsfUtilities.getExternalContext().getRequestParameterMap().get("text")+"<br>");
//			this.attrs.put(Constantes.SQL_CONDICION,"id_encuesta=".concat(JsfBase.getAutentifica().getEncuesta().getId().toString()));
			destinatarios = new ArrayList<>();
			load = DaoFactory.getInstance().toEntitySet("TrCorreosEncuestasDto",this.attrs);
			for(Entity item :load) {
        agenda.add(new UISelectItem(item.toString("nombre"),item.toString("correo")));
			} //
			this.attrs.put("contactosDestinoCorreo",new DualListModel<>(agenda,destinatarios));
			RequestContext.getCurrentInstance().execute("PF('dialogoCorreo').show();");
		  RequestContext.getCurrentInstance().update("dialogoCorreo");
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
	} // doCargarModelo
	
	public void doAgregarCorreo(){
		String correo = null;
		Map<String, Object> params = null;
		try {
			params = new HashMap<>();	
			correo = (String)this.attrs.get("agregaCorreo");
			if (correo != null && !correo.trim().equals("") ){
				UISelectItem selectionItem= new UISelectItem(correo,correo);
				if ((((DualListModel<UISelectItem>)this.attrs.get("contactosDestinoCorreo")).getSource()).indexOf(selectionItem) ==-1)
					((DualListModel<UISelectItem>)this.attrs.get("contactosDestinoCorreo")).getSource().add(selectionItem);
				else {
					params.put("correo",correo);
					UIMessage.toMessage("correo_duplicado",params);
				} // else
				//RequestContext.getCurrentInstance().update("dialogoCorreo");
			} // if
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // doCargarModelo

}
