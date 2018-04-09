package mx.org.kaana.kajool.test;

import java.util.List;
import mx.org.kaana.kajool.db.dto.TrJanalVisitasDto;
import mx.org.kaana.kajool.procesos.beans.Cuestionario;
import mx.org.kaana.kajool.servicios.ws.publicar.beans.VisitasMovil;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.libs.formato.Error;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 16/08/2016
 * @time 12:03:45 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class JSon {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Cuestionario cuestionario = null;
    TrJanalVisitasDto[] visitasMovil = null;
    try {
      String cue= "{\"trJanalCaratulaDto\":{\"id_caratula\":1,\"id_muestra\":227,\"id_usuario\":1,\"consecutiv\":\"28630\",\"cve_upm\":\"1\",\"ent\":\"2\",\"mun\":\"2\",\"loc\":\"103\",\"folio_cuest\":1,\"t_cuest\":1,\"nom_tit\":\"ELISA\",\"pat_tit\":\"FLORES\",\"mat_tit\":\"RAMOS\",\"calle\":null,\"num_ext\":null,\"num_int\":null,\"colonia\":null,\"cp\":null,\"telef\":\"6862280372\",\"referencia\":\"JHBJJ\",\"entre_ca1\":null,\"entre_ca2\":null,\"calle_atra\":\"LIC ADOLFO LOPEZ MATEOS\",\"creacion\":\"2016-11-25 22:14:06\",\"modificacion\":null,\"inicio_captura\":\"2016-11-25 22:14:06\",\"fin_captura\":null,\"status\":null,\"filter_1\":null,\"filter_2\":null,\"filter_3\":null,\"fuente\":2},\"personas\":[{\"id_persona\":1,\"id_caratula\":1,\"id_muestra\":227,\"id_usuario\":1,\"folio_cuest\":null,\"consecutiv\":\"028630\",\"cve_upm\":\"001\",\"rg\":\"0\",\"a_paterno\":\"FLORES\",\"a_materno\":\"RAMOS\",\"nombre\":\"ELISA\",\"c_resid\":\"1\",\"sexo\":null,\"edad\":null,\"esc\":null,\"parent\":null,\"v_resid\":null,\"alim\":\"1\",\"salud\":null,\"educ\":null,\"otros\":null,\"ninguno\":null,\"a_apoyo\":null,\"activo\":null,\"filter_1\":null,\"filter_2\":null,\"filter_3\":null,\"ver_ini\":1,\"ver_fin\":1},{\"id_persona\":2,\"id_caratula\":1,\"id_muestra\":227,\"id_usuario\":1,\"folio_cuest\":null,\"consecutiv\":\"028630\",\"cve_upm\":\"001\",\"rg\":\"1\",\"a_paterno\":\"MENDOZA\",\"a_materno\":\"BAUTISTA\",\"nombre\":\"ALONDRA\",\"c_resid\":\"1\",\"sexo\":null,\"edad\":null,\"esc\":null,\"parent\":null,\"v_resid\":null,\"alim\":null,\"salud\":null,\"educ\":\"3\",\"otros\":null,\"ninguno\":null,\"a_apoyo\":null,\"activo\":null,\"filter_1\":null,\"filter_2\":null,\"filter_3\":null,\"ver_ini\":1,\"ver_fin\":1},{\"id_persona\":3,\"id_caratula\":1,\"id_muestra\":227,\"id_usuario\":1,\"folio_cuest\":null,\"consecutiv\":\"028630\",\"cve_upm\":\"001\",\"rg\":\"2\",\"a_paterno\":\"COVARRUBIAS\",\"a_materno\":\"BAUTISTA\",\"nombre\":\"OLGAMARIA\",\"c_resid\":\"2\",\"sexo\":null,\"edad\":null,\"esc\":null,\"parent\":null,\"v_resid\":null,\"alim\":null,\"salud\":null,\"educ\":null,\"otros\":null,\"ninguno\":null,\"a_apoyo\":null,\"activo\":null,\"filter_1\":null,\"filter_2\":null,\"filter_3\":null,\"ver_ini\":1,\"ver_fin\":1},{\"id_persona\":4,\"id_caratula\":1,\"id_muestra\":227,\"id_usuario\":1,\"folio_cuest\":null,\"consecutiv\":\"028630\",\"cve_upm\":\"001\",\"rg\":\"3\",\"a_paterno\":\"COVARRUBIAS\",\"a_materno\":\"BAUTISTA\",\"nombre\":\"YULIZA ARLETH\",\"c_resid\":\"1\",\"sexo\":null,\"edad\":null,\"esc\":null,\"parent\":\"1\",\"v_resid\":null,\"alim\":null,\"salud\":null,\"educ\":null,\"otros\":\"4\",\"ninguno\":null,\"a_apoyo\":null,\"activo\":null,\"filter_1\":null,\"filter_2\":null,\"filter_3\":null,\"ver_ini\":1,\"ver_fin\":1},{\"id_persona\":5,\"id_caratula\":1,\"id_muestra\":227,\"id_usuario\":1,\"folio_cuest\":null,\"consecutiv\":\"028630\",\"cve_upm\":\"001\",\"rg\":\"4\",\"a_paterno\":\"COVARRUBIAS\",\"a_materno\":\"BAUTISTA\",\"nombre\":\"VICTOR YOEL\",\"c_resid\":\"2\",\"sexo\":null,\"edad\":null,\"esc\":null,\"parent\":null,\"v_resid\":\"2\",\"alim\":null,\"salud\":null,\"educ\":\"3\",\"otros\":null,\"ninguno\":null,\"a_apoyo\":null,\"activo\":null,\"filter_1\":null,\"filter_2\":null,\"filter_3\":null,\"ver_ini\":1,\"ver_fin\":1}],\"trJanalFamiliasDto\":{\"id_familia\":1,\"id_usuario\":1,\"id_muestra\":227,\"folio_cuest\":null,\"consecutiv\":\"028630\",\"cve_upm\":\"001\",\"mp_fr_re\":null,\"mp_asist\":null,\"mp_inf\":null,\"mp_tmpo\":null,\"mp_trto\":null,\"a_mejora\":null,\"a_sat\":null,\"a_oprt\":null,\"a_sat_t\":null,\"a_otorga\":1,\"a_esc_be\":\"2\",\"s_c_fv\":null,\"s_pl_fv_a\":null,\"s_sat_c\":null,\"s_oprt_m\":null,\"s_s_t_c\":2,\"s_s_pla\":null,\"s_s_t_pl\":null,\"s_otroga\":null,\"s_p_fv_s\":null,\"s_p_emb\":null,\"s_p_adic\":null,\"s_esc_c\":\"1\",\"s_esc_m\":\"2\",\"s_esc_as\":\"2\",\"am_alim\":1,\"am_oport\":2,\"am_salud\":1,\"am_otorg\":1,\"am_sat\":1,\"am_esc\":\"1\",\"e_nivel\":null,\"e_cont\":null,\"e_conc\":null,\"e_trto_t\":3,\"e_oport\":3,\"e_otorga\":1,\"e_sat\":2,\"e_empleo\":1,\"e_esc_e\":\"2\",\"at_pp\":null,\"at_pp_sat\":null,\"at_pp_cap\":null,\"cap_t\":null,\"cap_t_sat\":1,\"cap_t_cap\":null,\"t_salud\":null,\"t_alim\":null,\"t_adic\":null,\"t_planfa\":null,\"t_edu\":null,\"t_progra\":null,\"t_otros\":null,\"t_especi\":null,\"or_fi\":null,\"or_fi_sat\":null,\"or_fi_pf\":\"1\",\"ap_soc\":\"1\",\"ap_soc_sat\":\"2\",\"ap_soc_nv\":\"3\",\"filter_1\":null,\"filter_2\":null,\"filter_3\":null},\"trJanalModuloDto\":{\"id_modulo\":1,\"id_muestra\":227,\"id_usuario\":1,\"id_caratula\":1,\"folio_cuest\":null,\"consecutiv\":\"028630\",\"cve_upm\":\"001\",\"pro_des\":1,\"gr_trab\":1,\"gr_apo\":1,\"gr_pro_opo\":1,\"gr_apo_mej\":1,\"filter_1\":null,\"filter_2\":null,\"filter_3\":null}}";
      String vis= "[{\"id_visita\":1,\"id_muestra\":227,\"id_usuario\":1,\"visita\":1,\"nom_ent\":\"BC.0201\",\"fecha\":\"2016-11-25 22:14:06\",\"hr_ini\":\"2016-11-25 22:14:06\",\"hr_ter\":\"2016-11-25 22:19:41\",\"id_resultado\":0,\"observaciones\":\"PRUEBA DE CAPTURA\",\"registro\":\"2016-11-25 22:14:06\"},{\"id_visita\":2,\"id_muestra\":227,\"id_usuario\":1,\"visita\":2,\"nom_ent\":\"BC.0201\",\"fecha\":\"2016-11-25 22:14:06\",\"hr_ini\":\"2016-11-25 22:14:06\",\"hr_ter\":\"2016-11-25 22:19:41\",\"id_resultado\":0,\"observaciones\":\"PRUEBA DE CAPTURA\",\"registro\":\"2016-11-25 22:14:06\"}]";
      cuestionario = (Cuestionario) Decoder.toSerialSqllite(Cuestionario.class, cue);
      visitasMovil = (TrJanalVisitasDto[]) Decoder.toSerialSqllite(TrJanalVisitasDto[].class, vis);
      System.out.println(cuestionario);
      for (TrJanalVisitasDto visita : visitasMovil) {
        System.out.println(visita);  
      }
    } // try	
    catch (Exception e) {
      Error.mensaje(e);
    } // catch    
  }
}
