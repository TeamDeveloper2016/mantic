package mx.org.kaana.kajool.procesos.mantenimiento.menus.encabezado.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/09/2015
 *@time 03:35:51 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.cfg.Configuracion;
import mx.org.kaana.libs.cfg.Detalle;
import mx.org.kaana.libs.cfg.IArbol;
import mx.org.kaana.libs.cfg.Maestro;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.page.PageRecords;
import mx.org.kaana.kajool.db.dto.TcJanalMenusEncabezadoDto;

public class MenuEncabezado extends Maestro implements IArbol, Serializable {

  private static final long serialVersionUID=-6669091050872534236L;

  public MenuEncabezado() {
    super(
      new Configuracion("Configuración del menu", 10),
      new ArrayList<Detalle>(
      Arrays.asList(
      new Detalle(1, 2, 2, "0", "[1..99]", ""),
      new Detalle(2, 2, 2, "0", "[1..99]", ""),
      new Detalle(3, 2, 2, "0", "[1..99]", ""),
      new Detalle(4, 2, 2, "0", "[1..99]", ""),
      new Detalle(5, 2, 2, "0", "[1..99]", ""))));
  }

  @Override
  public List<TcJanalMenusEncabezadoDto> toFather(String value) throws Exception {
    List<TcJanalMenusEncabezadoDto> regresar=new ArrayList<>();
    Map<String, Object> params    =new HashMap<>();
    String[] list=uniqueKey(toCodeAll(value), value);
    for (String clave : list) {
      params.put("clave", clave);
      TcJanalMenusEncabezadoDto dto=(TcJanalMenusEncabezadoDto) DaoFactory.getInstance().findIdentically(TcJanalMenusEncabezadoDto.class, params);
      if (dto!=null&&dto.getKey()!=-1L) {
        regresar.add(dto);
      } // if
    } // for
    return regresar;
  }

  private List<TcJanalMenusEncabezadoDto> toChildren(String value, int level, int child) throws Exception {
    Map<String, Object> params=new HashMap<>();
    value=toOnlyKey(value, level+child);
    params.put(Constantes.SQL_CONDICION, "clave like '".concat(value).concat("%'".concat(" and nivel="+(level+child))).concat(" order by clave"));
    List<TcJanalMenusEncabezadoDto> regresar=(List) DaoFactory.getInstance().findViewCriteria(TcJanalMenusEncabezadoDto.class, params, Constantes.SQL_TODOS_REGISTROS);
    params.clear();
    return regresar;
  }

  @Override
  public List<TcJanalMenusEncabezadoDto> toChildren(String value, int level) throws Exception {
    return toChildren(value, level, 0);
  }

  @Override
  public boolean isChild(String value, int level) throws Exception {
    List<TcJanalMenusEncabezadoDto> list=toChildren(value, level, 1);
    boolean regresar          =list.isEmpty();
    list.clear();
    return regresar;
  }

  private PageRecords toChildren(String value, int level, int child, int first, int records) throws Exception {
    Map<String, Object> params=new HashMap<>();
    value=toOnlyKey(value, level+child);
    params.put(Constantes.SQL_CONDICION, "clave like '".concat(value).concat("%'".concat(" and nivel="+(level+child))).concat(" "));
    PageRecords regresar=DaoFactory.getInstance().findPage(TcJanalMenusEncabezadoDto.class, params, first, records);
    params.clear();
    return regresar;
  }

  public PageRecords toChildren(String value, int level, int first, int records) throws Exception {
    return toChildren(value, level, 0, first, records);
  }

  public int toCountChildren(String value, int level) throws Exception {
    int regresar              =0;
    List<TcJanalMenusEncabezadoDto> list=toChildren(value, level, 0);
    if (list!=null&&!list.isEmpty()) {
      TcJanalMenusEncabezadoDto dto=list.get(list.size()-1);
      String key=toValueKey(dto.getClave(), dto.getNivel().intValue());
      regresar=Numero.getInteger(key);
      // falta validar si aun se permite un hijo mas en este nivel
      // verificar si el total de hijos es menor a la longitud del nivel
      // y realizar una reclasificacion de la llave
    } // if		
    return regresar;
  }

  public String toNextKey(String value, int level, int increment) throws Exception {
    int child=toCountChildren(value, level);
    String tK=toOnlyKey(value, level);
    return toCode(tK+(child+increment));
  }

  public String toNextKey(String value, int level) throws Exception {
    return toNextKey(value, level, 1);
  }

  public String toForceNextKey(String value, int level) throws Exception {
    StringBuilder regresar = new StringBuilder();
    String parteIzq        = null;
    String parteDer        = null;
    String parteCen        = null;
    int incremento         = 0;
    parteIzq = value.substring(0, (level*2)-2);
    parteCen = value.substring((level*2)-2, (level*2));
    parteDer = value.substring((level*2));
    incremento = Integer.parseInt(parteCen);
    incremento++;
    regresar.append(parteIzq);
    if(incremento<10)
      regresar.append("0");
    regresar.append(String.valueOf(incremento));
    regresar.append(parteDer);
    return regresar.toString();
  }

  private List<TcJanalMenusEncabezadoDto> toAllChildren(String value, int level, int child) throws Exception {
    Map<String, Object> params=new HashMap<>();
    value=toOnlyKey(value, level+child);
    params.put(Constantes.SQL_CONDICION, "clave like '".concat(value).concat("%'".concat(" and nivel>="+(level+child))).concat(" "));
    List<TcJanalMenusEncabezadoDto> regresar=(List) DaoFactory.getInstance().findViewCriteria(TcJanalMenusEncabezadoDto.class, params, Constantes.SQL_TODOS_REGISTROS);
    params.clear();
    return regresar;
  }

  public List<TcJanalMenusEncabezadoDto> toAllChildren(String value, int level) throws Exception {
    return toAllChildren(value, level, 0);
  }

  public TcJanalMenusEncabezadoDto getFather(String value) throws Exception {
    TcJanalMenusEncabezadoDto regresar=null;
    Map<String, Object> params=new HashMap<>();
    String[] list=uniqueKey(toCodeAll(value), value);
    if (list!=null&&list.length>0) {
      if (list.length==1) {
        //la clave es Padre y es única por lo que solo debe de eliminarse
        regresar=null;
      } // if
      else {
        params.put("clave", list[list.length-2]);
        regresar=(TcJanalMenusEncabezadoDto) DaoFactory.getInstance().findIdentically(TcJanalMenusEncabezadoDto.class, params);
      } // else
    } // if	
    return regresar;
  }
}
