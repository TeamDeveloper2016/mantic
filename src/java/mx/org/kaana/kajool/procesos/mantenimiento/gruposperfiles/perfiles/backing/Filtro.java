package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.perfiles.backing;
/*
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 31, 2015
 *@time 2:58:11 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.perfiles.reglas.Agregar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalGruposDto;
import mx.org.kaana.kajool.db.dto.TcJanalPerfilesDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.perfiles.reglas.Transaccion;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;

@ManagedBean(name="kajoolMantenimientoGruposperfilesPerfilesFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {
  private static final long serialVersionUID=-6536425406528049407L;

  @PostConstruct
  @Override
  protected void init() {
    Long idGrupo  = null;
    try {
      idGrupo = (Long) JsfUtilities.getFlashAttribute("idGrupo");
      this.attrs.put("idGrupo", idGrupo);
      this.attrs.put("descripcion", "");
      this.attrs.put("grupoDto", DaoFactory.getInstance().findById(TcJanalGruposDto.class, idGrupo));
      this.attrs.put("dto", new TcJanalPerfilesDto());
      this.attrs.put("modificaDescripcion","");
      this.attrs.put("sortOrder"," order by tc_janal_perfiles.descripcion");
      ((TcJanalPerfilesDto)this.attrs.get("dto")).setIdGrupo(idGrupo);
      doLoad();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  }// init

    @Override
  public void doLoad() {
    List<Columna> columns= null;
    try {
      columns= new ArrayList();
      columns.add(new Columna("usuarios",EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      this.lazyModel = new FormatCustomLazy("VistaPerfilesDto", "row", this.attrs, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    finally{
      Methods.clean(columns);
    } // finally
  }// doLoad

  public String doEvento(String proceso) {
    String regresar                      = null;
    EAccion accion                       = null;
    Agregar agregar                      = null;
    TcJanalPerfilesDto tcJanalPerfilesDto= null;
    try {
      accion = EAccion.valueOf(proceso);
      switch (accion) {
        case ELIMINAR:
          Transaccion transaccion = new Transaccion(new TcJanalPerfilesDto(((Entity) this.attrs.get("seleccionado")).getKey()));
          if (transaccion.ejecutar(accion)) {
            JsfBase.addMessage("Perfil eliminado");
          }// if
          break;
        case AGREGAR:
          agregar= new Agregar((TcJanalPerfilesDto)this.attrs.get("dto"));
          agregar.agregar(accion);
          break;
        case MODIFICAR:
          tcJanalPerfilesDto= (TcJanalPerfilesDto) this.attrs.get("TcJanalPerfilesDto");
          tcJanalPerfilesDto.setDescripcion(this.attrs.get("modificaDescripcion").toString());
          agregar= new Agregar(tcJanalPerfilesDto);
          agregar.agregar(accion);
          break;
      }//switch
      doLoad();
    }//try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doEvento

  public void doEliminar(){
    doEvento("ELIMINAR");
  } //doEliminAR

 public void doSeleccion(){
   try {
     this.attrs.put("TcJanalPerfilesDto",(TcJanalPerfilesDto)DaoFactory.getInstance().findById(TcJanalPerfilesDto.class, ((Entity)this.attrs.get("seleccionado")).getKey()));
     this.attrs.put("modificaDescripcion",((TcJanalPerfilesDto) this.attrs.get("TcJanalPerfilesDto")).getDescripcion());
   } // try
   catch (Exception e) {
     JsfBase.addMessageError(e);
     Error.mensaje(e);
   } // catch
 }// doSeleccion

  public String doJerarquia(){
    JsfBase.setFlashAttribute("idGrupo", (Long)this.attrs.get("grupoDto"));
    JsfBase.setFlashAttribute("idPerfil",((TcJanalPerfilesDto)this.attrs.get("grupoDto")).getIdPerfil());
	JsfBase.setFlashAttribute("descripcion",((TcJanalGruposDto)this.attrs.get("grupoDto")).getDescripcion());
    return "jerarquia";
   }

   public String doIrOpcionesMenu(){
    JsfBase.setFlashAttribute("idGrupo", (Long)this.attrs.get("idGrupo"));
    JsfBase.setFlashAttribute("idPerfil", ((Entity)this.attrs.get("seleccionado")).getKey());
	return "/Paginas/Mantenimiento/GruposPerfiles/OpcionesMenu/filtro".concat(Constantes.REDIRECIONAR);
  }

   public String doIrOpcionesEncabezado(){
    JsfBase.setFlashAttribute("idGrupo", (Long)this.attrs.get("idGrupo"));
    JsfBase.setFlashAttribute("idPerfil", ((Entity)this.attrs.get("seleccionado")).getKey());
	return "/Paginas/Mantenimiento/GruposPerfiles/OpcionesEncabezado/filtro".concat(Constantes.REDIRECIONAR);
  } // doIrOpcionesEncabezado
	
	 public String doIrPaginaInicial(){
    JsfBase.setFlashAttribute("idPerfil", ((Entity)this.attrs.get("seleccionado")).getKey());
    JsfBase.setFlashAttribute("idGrupo", ((TcJanalGruposDto)this.attrs.get("grupoDto")).getIdGrupo());
	return "paginaInicial";
  }

  public String doOpcionUsuarios(){
    JsfBase.setFlashAttribute("idPerfil", ((TcJanalPerfilesDto)this.attrs.get("grupoDto")).getIdPerfil());
    return "/Paginas/Mantenimiento/GruposPerfiles/Perfiles/Usuarios/filtro";
  }		

}
