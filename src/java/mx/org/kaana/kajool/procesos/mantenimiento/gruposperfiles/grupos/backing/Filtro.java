package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.grupos.backing;
/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 27, 2015
 *@time 3:05:29 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.grupos.reglas.Transaccion;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;

@ManagedBean(name="kajoolMantenimientoGruposperfilesGruposFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8753301293108510235L;

  @PostConstruct
  @Override
  protected void init() {
    attrs.put("descripcion", "");
    doLoad();
  }//init

   @Override
  public void doLoad() {
     List<Columna> columns= null;
     try {
       columns= new ArrayList();
       columns.add(new Columna("perfiles",EFormatoDinamicos.NUMERO_SIN_DECIMALES));
       this.lazyModel = new FormatCustomLazy("VistaGruposDto", "row",this.attrs, columns);
       UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
     finally{
       Methods.clean(columns);
     }//finally
   }// doLoad

 public String doEvento(String proceso){
   String regresar= null;
   EAccion accion = null;
   try {
     accion= EAccion.valueOf(proceso);
     switch(accion){
       case ELIMINAR:
        Transaccion transaccion = new Transaccion(((Entity)this.attrs.get("seleccionado")).getKey());
        if (transaccion.ejecutar(accion)) {
          JsfBase.addMessage("Grupo eliminado");
          doLoad();
        }// if
         break;
       case MODIFICAR:
         JsfBase.setFlashAttribute("idGrupo", ((Entity)this.attrs.get("seleccionado")).getKey());
       case AGREGAR:
         JsfBase.setFlashAttribute("accion", accion);
         regresar= "agregar".concat(Constantes.REDIRECIONAR);
       break;
     }//switch
   }//try
   catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
   } // catch
  return regresar;
 } // doEvento

  public String doIrPerfiles() {
    JsfBase.setFlashAttribute("idGrupo", ((Entity)this.attrs.get("seleccionado")).getKey());
    return "/Paginas/Mantenimiento/GruposPerfiles/Perfiles/filtro".concat(Constantes.REDIRECIONAR);
  }//doIrPerfiles

  public String doIrOpcionesMenu() {
    JsfBase.setFlashAttribute("idGrupo", ((Entity)this.attrs.get("seleccionado")).getKey());
    JsfBase.setFlashAttribute("idPerfil", null);
    return "/Paginas/Mantenimiento/Menus/opciones".concat(Constantes.REDIRECIONAR);
  }//doIrOpcionesMenu

  public void doEliminar(){
    doEvento("ELIMINAR");
  }// doEliminar
}
