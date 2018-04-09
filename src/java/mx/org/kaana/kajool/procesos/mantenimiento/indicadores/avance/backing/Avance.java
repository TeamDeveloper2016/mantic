package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 28, 2014
 *@time 10:53:11 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAmbitos;
import mx.org.kaana.kajool.enums.ECortes;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.beans.DefinicionAvance;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.reglas.Consultas;
import mx.org.kaana.kajool.procesos.tableros.beans.GraficaConsulta;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.directivos.beans.Columna;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.directivos.beans.Registro;

import org.primefaces.event.NodeExpandEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;


@ManagedBean(name="kajoolMantenimientoIndicadoresAvanceAvance")
@ViewScoped
public class Avance implements Serializable {

  private static final long serialVersionUID=-3237979485140684618L;
  private TreeNode lazyModel;
  private TreeNode seleccionado;
  private List<Columna> columnas;
	private Entity elementoSeleccionado;
  private DefinicionAvance definicionAvance;


  public TreeNode getLazyModel() {
    return lazyModel;
  }

  public void setLazyModel(TreeNode lazyModel) {
    this.lazyModel=lazyModel;
  }

  public TreeNode getSeleccionado() {
    return seleccionado;
  }

  public void setSeleccionado(TreeNode seleccionado) {
    this.seleccionado=seleccionado;
  }

  public List<Columna> getColumnas(){
    return this.columnas;
  }

  public DefinicionAvance getDefinicionAvance() {
    return definicionAvance;
  }

  @PostConstruct
	private void init() {
    if (JsfBase.getFlashAttribute("seleccionado")!= null) {
      this.definicionAvance= new DefinicionAvance(((Entity)JsfBase.getFlashAttribute("seleccionado")).toLong("idDefinicionAvance"),
																									((Entity)JsfBase.getFlashAttribute("seleccionado")).toLong("idBitacoraAvance"),
                                                  ((Entity)JsfBase.getFlashAttribute("seleccionado")).toString("tabla"),
                                                  ((Entity)JsfBase.getFlashAttribute("seleccionado")).toString("campo"),
                                                  ((Entity)JsfBase.getFlashAttribute("seleccionado")).toString("estatusIndicador"),
                                                  Cadena.letraCapital(((Entity)JsfBase.getFlashAttribute("seleccionado")).toString("descripcion")),
                                                  ((Entity)JsfBase.getFlashAttribute("seleccionado")).toString("estatusAplican"),
																									((Entity)JsfBase.getFlashAttribute("seleccionado")).toString("corte"),
                                                  Fecha.getFechaDiaAnterior(10, ((Entity)JsfBase.getFlashAttribute("seleccionado")).toTimestamp("fin")));
			this.elementoSeleccionado= (Entity)JsfBase.getFlashAttribute("seleccionado");
    } // if
    else {
      this.definicionAvance= new DefinicionAvance(Numero.getLong(JsfBase.getParametro("idSepDefinicionAvance")),
																									Numero.getLong(JsfBase.getParametro("idBitacoraAvance")),
                                                  JsfBase.getParametro("tablaDefinicion"),
                                                  JsfBase.getParametro("campoDefinicion"),
                                                  JsfBase.getParametro("estatusIndicador"),
                                                  JsfBase.getParametro("descripcion"),
                                                  JsfBase.getParametro("estatusAplican"),
																									JsfBase.getParametro("corte"),
                                                  JsfBase.getParametro("registroFin"));
    } // else
    load();
	}

  private void load(){
    TreeNode treeNacional = null;
    TreeNode treeUnidad   = null;
    Consultas consultas   = null;
    Registro avance       = null;
    Map params            = new HashMap();
    try {
			System.out.println("Corte: "+ this.definicionAvance.getCorte());
      this.lazyModel= new DefaultTreeNode("root", null);
      params.put("tabla", this.definicionAvance.getTabla());
      params.put("campo", this.definicionAvance.getCampo());
      params.put("idBitacoraAvance", this.definicionAvance.getIdBitacoraAvance());
      params.put("idSepDefinicionAvance", this.definicionAvance.getIdSepDefinicionAvance());
			params.put("nivel", ECortes.CENTRAL.getAmbito());
      consultas= new Consultas();
      avance= consultas.toValores(params, EAmbitos.CENTRAL, "nacionalDinamico", this.definicionAvance.getIndicador(), this.definicionAvance.getEstatusAplican()).get(0);
      treeNacional= new DefaultTreeNode(avance, this.lazyModel);
      treeUnidad= new DefaultTreeNode(new Registro("", EAmbitos.REGIONAL), treeNacional);
      this.columnas= avance.getColumnas();
    } // try
    catch(Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  }

  public void onNodeExpand(NodeExpandEvent event) {
    Registro avance= ((Registro)event.getTreeNode().getData());
		Map params            = new HashMap();
    try {
      params.put("tabla", this.definicionAvance.getTabla());
      params.put("campo", this.definicionAvance.getCampo());
      params.put("idSepDefinicionAvance", this.definicionAvance.getIdSepDefinicionAvance());
      params.put("idBitacoraAvance", this.definicionAvance.getIdBitacoraAvance());
      event.getTreeNode().getChildren().clear();
      switch (avance.getAmbito()) {
      case CENTRAL:
				params.put("nivel", ECortes.REGIONAL.getAmbito());
				loadTreeChildren(event, params, EAmbitos.REGIONAL, EAmbitos.ESTATAL, "organizacionDinamico");
        break;
      case REGIONAL:
				params.put("idOrganizacion", avance.getIdKey());
				params.put("nivel", ECortes.ESTATAL.getAmbito());
				loadTreeChildren(event, params, EAmbitos.ESTATAL, EAmbitos.MUNICIPIO, "entidadDinamico");
			break;
      case ESTATAL:
				if(this.definicionAvance.getCorte().equals("Municipio")){
					params.put("idEntidad", avance.getIdKey());
					params.put("nivel", ECortes.MUNICIPIO.getAmbito());
					loadTreeChildren(event, params, EAmbitos.MUNICIPIO, EAmbitos.LOCALIDAD, "municipioDinamico");
				} // if
        break;
        case MUNICIPIO:
				if(avance.getAmbito().getCortesAplican().contains(this.definicionAvance.getCorte())){
					params.put("idMunicipio", avance.getIdKey());
					params.put("nivel", ECortes.LOCALIDAD.getAmbito());
					loadTreeChildren(event, params, EAmbitos.LOCALIDAD, null, "oficinaDinamico");
				} // if
        break;
      } // switch
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessage("Consultar indicadores", e.getMessage(), ETipoMensaje.ERROR);
    } // catch
      finally {
      Methods.clean(params);
    }
  }

	
  public String toGrafica() {
    Registro avance= ((Registro)this.seleccionado.getData());
    Map params                      = new HashMap();
    GraficaConsulta graficaConsulta = null;
    params.put("tabla", this.definicionAvance.getTabla());
      params.put("campo", this.definicionAvance.getCampo());
      params.put("idSepDefinicionAvance", this.definicionAvance.getIdSepDefinicionAvance());
      params.put("idBitacoraAvance", this.definicionAvance.getIdBitacoraAvance());
    switch (avance.getAmbito()) {
      case CENTRAL:
				params.put("nivel", ECortes.CENTRAL.getAmbito());
        graficaConsulta= new GraficaConsulta("VistaIndicadoresDirectivos", "nacionalDinamico", params);
        break;
      case REGIONAL:
				params.put("nivel", ECortes.REGIONAL.getAmbito());
        params.put("idOrganizacion", avance.getIdKey());
        graficaConsulta= new GraficaConsulta("VistaIndicadoresDirectivos", "organizacion", params);
        break;
      case ESTATAL:
				params.put("nivel", ECortes.ESTATAL.getAmbito());
        params.put("idEntidad", avance.getIdKey());
        graficaConsulta= new GraficaConsulta("VistaIndicadoresDirectivos", "entidad", params);
        break;
      case MUNICIPIO:
				params.put("nivel", ECortes.MUNICIPIO.getAmbito());
        params.put("idMunicipio", avance.getIdKey());
        graficaConsulta= new GraficaConsulta("VistaIndicadoresDirectivos", "municipio", params);
        break;
      case LOCALIDAD:
				params.put("nivel", ECortes.LOCALIDAD.getAmbito());
        params.put("idOrganizacionGrupo", avance.getIdKey());
        graficaConsulta= new GraficaConsulta("VistaIndicadoresDirectivos", "organizacionGrupo", params);
        break;
    } // switch
    JsfBase.setFlashAttribute("graficaConsulta", graficaConsulta);
		JsfBase.setFlashAttribute("seleccionado", this.elementoSeleccionado);
    return "/Paginas/Tableros/Graficas/grafica".concat(Constantes.REDIRECIONAR);
  }

	private void loadTreeChildren(NodeExpandEvent event, Map<String, Object> params, EAmbitos ambito, EAmbitos ambitoHijos, String idXml){
		TreeNode tree         = null;
    TreeNode treeHijos    = null;
    Consultas consultas   = null;
    List<Registro> avances = null;
		try {
			consultas= new Consultas();
			avances= consultas.toValores(params, ambito, idXml, this.definicionAvance.getIndicador(), this.definicionAvance.getEstatusAplican());
			for(Registro registro: avances) {
				tree= new DefaultTreeNode(registro, event.getTreeNode());
				treeHijos= new DefaultTreeNode(new Registro("", ambitoHijos), tree);
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	}
	
	public String doRegresar(){
		return "filtro".concat(Constantes.REDIRECIONAR);
	}
	
}
