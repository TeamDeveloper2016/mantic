package mx.org.kaana.kajool.procesos.explotacion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.beans.Variable;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.procesos.enums.EExportarDatos;
import mx.org.kaana.kajool.procesos.explotacion.reglas.Variables;
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.kajool.procesos.reportes.beans.Exportar;
import mx.org.kaana.kajool.procesos.reportes.beans.Field;
import mx.org.kaana.kajool.procesos.reportes.enums.ETypeField;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolExplotacionFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID= 3223631042822589507L;  
  private List<Modelo> definiciones;
  private EExportarDatos tablas;
  private List<UISelectItem> listaTablas;
  private Reporte reporte;  
  private List<Variable> selecteds;	
	private static List<List<Field>> tablasCampos;
	private List<UISelectItem> resultados;	  
  
  @PostConstruct
  @Override
	protected void init() {
    try {					
      this.attrs.put("upm", "");
      loadFields();
      loadEntidades();
      loadResultados();
      this.attrs.put("idEntidad", -1L);
			this.attrs.put("idResultado", -1L);
      this.reporte = (Reporte) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("kajoolTemplateReporte");
      this.listaTablas= new ArrayList<>();
      for(EExportarDatos tabla: EExportarDatos.values())
        this.listaTablas.add(new UISelectItem(tabla.ordinal(), Cadena.reemplazarCaracter(tabla.name(), '_', ' ')));
      this.attrs.put("cuestionario", UIBackingUtilities.toFirstKeySelectItem(this.listaTablas));
      doLoad();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  public List<UISelectItem> getListaTablas() {
    return listaTablas;
  }  

	public List<UISelectItem> getResultados() {
		return resultados;
	}
	
  public List<Variable> getSelecteds() {
    return selecteds;
  }

  public void setSelecteds(List<Variable> selecteds) {
    this.selecteds = selecteds;
  }  
  
  public Reporte getReporte() {
    return reporte;
  }

  private void loadFields() {
    this.tablasCampos= new ArrayList<>();		
		this.tablasCampos.add(new ArrayList<Field>());
		this.tablasCampos.get(EExportarDatos.VISITAS.ordinal()- 1).add(new Field("id_familia", ETypeField.INTEGER, 11, 0));
		this.tablasCampos.get(EExportarDatos.VISITAS.ordinal()- 1).add(new Field("consecutiv", ETypeField.VARCHAR, 6, 0));
		this.tablasCampos.get(EExportarDatos.VISITAS.ordinal()- 1).add(new Field("cve_upm", ETypeField.VARCHAR, 3, 0));
		this.tablasCampos.get(EExportarDatos.VISITAS.ordinal()- 1).add(new Field("visita", ETypeField.INTEGER, 1, 0));		
		this.tablasCampos.get(EExportarDatos.VISITAS.ordinal()- 1).add(new Field("nom_ent", ETypeField.VARCHAR, 120, 0));
		this.tablasCampos.get(EExportarDatos.VISITAS.ordinal()- 1).add(new Field("fecha", ETypeField.DATE, 0, 0));
		this.tablasCampos.get(EExportarDatos.VISITAS.ordinal()- 1).add(new Field("hr_ini", ETypeField.VARCHAR, 5, 0));
		this.tablasCampos.get(EExportarDatos.VISITAS.ordinal()- 1).add(new Field("hr_ter", ETypeField.VARCHAR, 5, 0));
		this.tablasCampos.get(EExportarDatos.VISITAS.ordinal()- 1).add(new Field("res", ETypeField.INTEGER, 1, 0));
		this.tablasCampos.get(EExportarDatos.VISITAS.ordinal()- 1).add(new Field("recertific", ETypeField.VARCHAR, 4, 0));
		this.tablasCampos.add(new ArrayList<Field>());
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("id_familia", ETypeField.INTEGER, 11, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("consecutiv", ETypeField.VARCHAR, 6, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("cve_upm", ETypeField.VARCHAR, 3, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("folio_cuest ", ETypeField.INTEGER, 1, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("t_cuest", ETypeField.INTEGER, 1, 0));		
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("nom_tit", ETypeField.VARCHAR, 70, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("pat_tit", ETypeField.VARCHAR, 70, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("mat_tit", ETypeField.VARCHAR, 70, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("calle", ETypeField.VARCHAR, 150, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("num_ext", ETypeField.VARCHAR, 40, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("num_int", ETypeField.VARCHAR, 40, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("colonia", ETypeField.VARCHAR, 150, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("cp", ETypeField.VARCHAR, 5, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("telef", ETypeField.VARCHAR, 50, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("referencia", ETypeField.VARCHAR, 200, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("entre_ca1", ETypeField.VARCHAR, 150, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("entre_ca2", ETypeField.VARCHAR, 150, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("calle_atra", ETypeField.VARCHAR, 150, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("ent", ETypeField.VARCHAR, 2, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("mun", ETypeField.VARCHAR, 3, 0));
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("loc", ETypeField.VARCHAR, 4, 0));		
		this.tablasCampos.get(EExportarDatos.CARATULA.ordinal()- 1).add(new Field("recertific", ETypeField.VARCHAR, 4, 0));
		this.tablasCampos.add(new ArrayList<Field>());
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("id_familia", ETypeField.INTEGER, 11, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("consecutiv", ETypeField.VARCHAR, 6, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("cve_upm", ETypeField.VARCHAR, 3, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("id_per", ETypeField.INTEGER, 8, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("folio_cuest ", ETypeField.INTEGER, 1, 0));		
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("rg", ETypeField.VARCHAR, 2, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("a_paterno", ETypeField.VARCHAR, 90, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("a_materno", ETypeField.VARCHAR, 90, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("nombre", ETypeField.VARCHAR, 90, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("c_resid", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("sexo", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("edad", ETypeField.INTEGER, 2, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("esc", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("parent", ETypeField.VARCHAR, 2, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("v_resid", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("alim", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("salud", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("educ", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("otros", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("ninguno", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("a_apoyo", ETypeField.INTEGER, 2, 0)); //*
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("activo", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("informante", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.PERSONAS.ordinal()- 1).add(new Field("recertific", ETypeField.VARCHAR, 4, 0));		
		this.tablasCampos.add(new ArrayList<Field>());
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("id_familia", ETypeField.INTEGER, 11, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("consecutiv", ETypeField.VARCHAR, 6, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("cve_upm", ETypeField.VARCHAR, 3, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("folio_cuest ", ETypeField.INTEGER, 1, 0));		
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("mp_fr_re", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("mp_asist", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("mp_inf", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("t_salud", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("t_alim", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("t_adic", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("t_planfa", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("t_edu", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("t_progra", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("t_otros", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("t_especi", ETypeField.VARCHAR, 60, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("mp_tmpo", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("mp_trto", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("a_mejora", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("a_sat", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("a_oprt", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("a_sat_t", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("a_otorga", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("a_esc_be", ETypeField.INTEGER, 2, 0)); //*
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_c_fv", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_pl_fv_a", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_sat_c", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_oprt_m", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_s_t_c", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_s_pla", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_s_t_pl", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_otorga", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_p_fv_s", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_p_emb", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_p_adic", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_esc_c", ETypeField.INTEGER, 2, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_esc_m", ETypeField.INTEGER, 2, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("s_esc_as", ETypeField.INTEGER, 2, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("am_alim", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("am_oport", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("am_salud", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("am_otorg", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("am_sat", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("am_esc", ETypeField.INTEGER, 2, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("e_nivel", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("e_cont", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("e_conc", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("e_trto_t", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("e_oport", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("e_otorga", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("e_sat", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("e_empleo", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("e_esc_e", ETypeField.INTEGER, 2, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("at_pp", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("at_pp_sat", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("at_pp_cap", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("cap_t", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("cap_t_sat", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("cap_t_cap", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("or_fi", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("or_fi_sat", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("or_fi_pf", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("ap_soc", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("ap_soc_sat", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("ap_soc_nv", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.FAMILIA.ordinal()- 1).add(new Field("recertific", ETypeField.VARCHAR, 4, 0));
		this.tablasCampos.add(new ArrayList<Field>());
		this.tablasCampos.get(EExportarDatos.MODULO.ordinal()- 1).add(new Field("id_familia", ETypeField.INTEGER, 11, 0));
		this.tablasCampos.get(EExportarDatos.MODULO.ordinal()- 1).add(new Field("consecutiv", ETypeField.VARCHAR, 6, 0));
		this.tablasCampos.get(EExportarDatos.MODULO.ordinal()- 1).add(new Field("cve_upm", ETypeField.VARCHAR, 3, 0));
		this.tablasCampos.get(EExportarDatos.MODULO.ordinal()- 1).add(new Field("folio_cuest ", ETypeField.INTEGER, 1, 0));		
		this.tablasCampos.get(EExportarDatos.MODULO.ordinal()- 1).add(new Field("pro_des", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.MODULO.ordinal()- 1).add(new Field("gr_trab", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.MODULO.ordinal()- 1).add(new Field("gr_apo", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.MODULO.ordinal()- 1).add(new Field("gr_pro_opo", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.MODULO.ordinal()- 1).add(new Field("gr_apo_mej", ETypeField.VARCHAR, 1, 0));
		this.tablasCampos.get(EExportarDatos.MODULO.ordinal()- 1).add(new Field("recertific", ETypeField.VARCHAR, 4, 0));
		this.tablasCampos.add(new ArrayList<Field>());
		this.tablasCampos.get(EExportarDatos.ENTIDADES.ordinal()- 1).add(new Field("ent", ETypeField.VARCHAR, 2, 0));
		this.tablasCampos.get(EExportarDatos.ENTIDADES.ordinal()- 1).add(new Field("descripcion", ETypeField.VARCHAR, 200, 0));
		this.tablasCampos.add(new ArrayList<Field>());
		this.tablasCampos.get(EExportarDatos.MUNICIPIOS.ordinal()- 1).add(new Field("ent", ETypeField.VARCHAR, 2, 0));
		this.tablasCampos.get(EExportarDatos.MUNICIPIOS.ordinal()- 1).add(new Field("mun", ETypeField.VARCHAR, 3, 0));
		this.tablasCampos.get(EExportarDatos.MUNICIPIOS.ordinal()- 1).add(new Field("descripcion", ETypeField.VARCHAR, 200, 0));
		this.tablasCampos.add(new ArrayList<Field>());
		this.tablasCampos.get(EExportarDatos.LOCALIDADES.ordinal()- 1).add(new Field("ent", ETypeField.VARCHAR, 2, 0));
		this.tablasCampos.get(EExportarDatos.LOCALIDADES.ordinal()- 1).add(new Field("mun", ETypeField.VARCHAR, 3, 0));
		this.tablasCampos.get(EExportarDatos.LOCALIDADES.ordinal()- 1).add(new Field("loc", ETypeField.VARCHAR, 4, 0));
		this.tablasCampos.get(EExportarDatos.LOCALIDADES.ordinal()- 1).add(new Field("descripcion", ETypeField.VARCHAR, 200, 0));    
  }
  
  @Override
  public void doLoad() {    
    List<Columna> columnas= null;	
    try {
      toColumns();
      columnas= new ArrayList<>();
      for (Variable item : this.selecteds)
        columnas.add(new Columna(item.getProperty(), item.getFormat()));			
      this.lazyModel= new FormatLazyModel("VistaCapturaCuestionariosDto", this.attrs.get("idXml").toString(), this.attrs, columnas);			
      UIBackingUtilities.resetDataTable();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doLoad

  private void toColumns(){
    Integer seleccion   = -1;
    EExportarDatos datos= null;
    try {
      seleccion= Integer.valueOf(this.attrs.get("cuestionario").toString()) + 1;
      datos= EExportarDatos.fromOrdinal(Long.valueOf(seleccion));
      switch(datos){
        case TODOS:
          this.selecteds= Variables.getTODOS();
          this.attrs.put("idXml", "todos");
          break;
        case CARATULA:
          this.selecteds= Variables.getCARATULA();
          this.attrs.put("idXml", "trJanalCaratula");
          break;
        case PERSONAS:
          this.selecteds= Variables.getINTEGRANTES();
          this.attrs.put("idXml", "trJanalPersonas");
          break;
        case FAMILIA:
          this.selecteds= Variables.getFAMILIA();
          this.attrs.put("idXml", "trJanalFamilias");
          break;
        case MODULO:
          this.selecteds= Variables.getMODULO();
          this.attrs.put("idXml", "trJanalModulo");
          break;
        case VISITAS:
          this.selecteds= Variables.getVISITAS();
          this.attrs.put("idXml", "trJanalVisitas");
          break;
				case ENTIDADES:
          this.selecteds= Variables.getENTIDADES();
          this.attrs.put("idXml", "tcJanalEntidades");
          break;
				case MUNICIPIOS:
          this.selecteds= Variables.getMUNICIPIOS();
          this.attrs.put("idXml", "tcJanalMunicipios");
          break;
				case LOCALIDADES:
          this.selecteds= Variables.getLOCALIDADES();
          this.attrs.put("idXml", "tcJanalLocalidades");
          break;
      } // switch
    } // try
    catch (Exception e) {      
      throw e;
    } // catch    
  } // toColumns
  
  public String doExportarFdTexto() {
    String regresar= null;		
    try {						
      this.definiciones=new ArrayList<>();
      this.tablas = EExportarDatos.values()[Integer.valueOf((String)this.attrs.get("cuestionario"))];
      for (int count=0; count< this.tablas.getIdXml().length; count++) {			 
        definiciones.add(new Modelo((Map<String, Object>) ((HashMap)this.attrs).clone(), this.tablas.getProceso(), this.tablas.getIdXml()[count], this.tablas.getDbfFile()[count], this.tablasCampos.get(Integer.valueOf(this.tablas.getIndice()[count]))));
      } // for
      JsfBase.setFlashAttribute(Constantes.REPORTE_REFERENCIA, new Exportar(this.tablas, this.definiciones, Boolean.FALSE, Boolean.TRUE));
      regresar = "/Paginas/Reportes/texto".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e){
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		return regresar;
	} // doExportarFdTexto
  
  public String doExportarFdDbf() {
		String regresar= null;		
		try {			
			this.definiciones= new ArrayList<>();
			this.tablas= EExportarDatos.values()[Integer.valueOf((String)this.attrs.get("cuestionario"))];
      for (int count=0; count< this.tablas.getIdXml().length; count++) 
				this.definiciones.add(new Modelo((Map<String, Object>) ((HashMap)this.attrs).clone(), this.tablas.getProceso(), this.tablas.getIdXml()[count], this.tablas.getDbfFile()[count], this.tablasCampos.get(Integer.valueOf(this.tablas.getIndice()[count]))));
			JsfBase.setFlashAttribute(Constantes.REPORTE_REFERENCIA, new Exportar(this.tablas, this.definiciones, Boolean.TRUE, Boolean.TRUE));
			regresar = "/Paginas/Reportes/foxpro".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e){
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		return regresar;
	} // doExportarFdDbf  
	
	private void loadResultados() {
		try {						
			this.resultados= UISelect.build("TcJanalResultadosDto", "row", this.attrs, "descripcion", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);				      
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
	}
  
}
