package mx.org.kaana.kajool.procesos.usuarios.reglas.formato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 2/09/2015
 * @time 12:10:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Rangos {

	private String valores;
	private Long idOrganizacinoGrupo;
	private Long idPerfil;

	public Rangos(String valores, Long idOrganizacinoGrupo, Long idPerfil)
		throws Exception {
		this.idOrganizacinoGrupo=idOrganizacinoGrupo;
		this.idPerfil=idPerfil;
		this.valores=valores;
	}

	public List<String> getSerie() throws Exception {
		List<String> serieTmp=new ArrayList<String>();
		String[] grupos=this.valores.split("\\,");
		for (int i=0; i<grupos.length; i++) {
			String[] rango=grupos[i].split("\\.");
			Rango serie=null;
			if (Character.isLetter(rango[0].charAt(0))) {
				serie=new Letras(rango[0].charAt(0),
					rango.length==1 ? rango[0].charAt(0) : rango[1].charAt(0));
			} // if
			else {
				serie=new Numeros(Integer.parseInt(rango[0]),
					Integer.parseInt(rango[1]));
			} // else
			serieTmp.addAll(serie.getSerie());
		} // for
		return depurar(serieTmp);
	} // getSerie

	private List<String> depurar(List<String> serie) {
		int x=0;
		List<Entity> existen=null;
		List<String> listaClaves=null;
		if (hasPadre()) {
			// clavePadre= readClavePadre(); // Se modifico para que se dieran claves
			// unicas,
			// existen= getExisten(clavePadre); // funciona cuando solo se maneja en
			// un solo ambito
			existen=getExisten();
		} // if
		else {
			existen=getExisten();
		} // else
		if (existen!=null) {
			listaClaves=getLista(existen);
			while (x<serie.size()) {
				if (listaClaves.contains(String.valueOf(serie.get(x)))) {
					serie.remove(x);
				} // if
				else {
					x++;
				} // else
			} // while
		}// if
		return serie;
	}// depurar

	public List<Entity> getExisten() {
		List<Entity> regresar=null;
		Map<String, Object> params=new HashMap<String, Object>();
		try {
			params.put("idOrganizacinoGrupo", this.idOrganizacinoGrupo);
			params.put("idPerfil", this.idPerfil);
			regresar=(List<Entity>) DaoFactory.getInstance().toEntitySet("TrUsuariosDto", "claveUsuario", params, -1L);
		}// try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // getExisten

	public List<String> getLista(List<Entity> existen) {
		List<String> lista=new ArrayList<String>();
		for (Entity reg : existen) {
			lista.add(reg.toString("claveUsuario"));
		}// for
		return lista;
	}// getLista

	private boolean hasPadre() {
		boolean regresar=false;
		String idPerfiles=null;
		Autentifica autentifica=JsfBase.getAutentifica();
		idPerfiles=TcConfiguraciones.getInstance().getPropiedad(JsfUtilities.getFlashAttribute("idGrupo").toString().concat(".idPerfiles.claveCompuesta"));
		Long idPerfilAcceso=Numero.getLong(JsfBase.seekParameter("idPerfil").toString());
		if (idPerfiles!=null) {
			if (idPerfiles.indexOf(this.idPerfil.toString())!=-1) {
				if (!idPerfilAcceso.equals(this.idPerfil)) {
					regresar=true;
				} // if
			} // if
		} // no hay perfiles con clave compuesta
		return regresar;
	}
}
