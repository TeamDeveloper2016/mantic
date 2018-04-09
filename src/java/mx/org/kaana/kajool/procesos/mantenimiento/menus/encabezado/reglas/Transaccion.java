package mx.org.kaana.kajool.procesos.mantenimiento.menus.encabezado.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/08/2015
 * @time 04:04:26 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.db.dto.TcJanalMenusEncabezadoDto;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.BAJAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import static mx.org.kaana.kajool.enums.EAccion.PROCESAR;
import static mx.org.kaana.kajool.enums.EAccion.REGISTRAR;
import static mx.org.kaana.kajool.enums.EAccion.SUBIR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private TcJanalMenusEncabezadoDto menu;

	public Transaccion(TcJanalMenusEncabezadoDto menu) {
		this.menu=menu;
	}

	public Transaccion(String clavePadre, Long nivel) {
		menu=new TcJanalMenusEncabezadoDto();
		menu.setClave(clavePadre);
		menu.setNivel(nivel);
	}

	public TcJanalMenusEncabezadoDto getMenu() {
		return menu;
	}

	public void setMenu(TcJanalMenusEncabezadoDto menu) {
		this.menu=menu;
	}

	@Override
	public boolean ejecutar(Session session, EAccion accion) throws RuntimeException {
		boolean regresar =false;
		MenuEncabezado menuEncabezado=new MenuEncabezado();
		try {
			switch (accion) {
				case AGREGAR:
					DaoFactory.getInstance().insert(session, this.menu);
					break;
				case MODIFICAR:
					DaoFactory.getInstance().update(session, this.menu);
					break;
				case ELIMINAR:
					if (menuEncabezado.isChild(this.menu.getClave(), this.menu.getNivel().intValue())) {
						if (verificarPerfiles(session)) {
							DaoFactory.getInstance().delete(session, this.menu);
							this.menu=menuEncabezado.getFather(this.menu.getClave());
							if (this.menu!=null) {
								int count=menuEncabezado.toCountChildren(this.menu.getClave(), this.menu.getNivel().intValue());
								if (count==0) {
									this.menu.setUltimo(1L);
									DaoFactory.getInstance().update(session, this.menu);
								} // if	
							} // if	
						} // if
						else {
							JsfBase.addMsgProperties("error_eliminar_menu_perfiles");
							throw new RuntimeException();
						} // else
					}	// if
					else {
						JsfBase.addMsgProperties("error_eliminar_menu_dependientes");
						throw new RuntimeException();
					} // else
					break;
				case SUBIR:
				case BAJAR:
					int index=-1;
					List<TcJanalMenusEncabezadoDto> list=menuEncabezado.toChildren(this.menu.getClave(), this.menu.getNivel().intValue());
					if (EAccion.SUBIR.equals(accion)) {
						index=list.indexOf(this.menu)-1;
					} // if
					else {
						index=list.indexOf(this.menu)+1;
					} // else
					if (index>=0&&index<list.size()) {
						TcJanalMenusEncabezadoDto change=list.get(index);
						List<TcJanalMenusEncabezadoDto> allMenu=menuEncabezado.toAllChildren(this.menu.getClave(), this.menu.getNivel().intValue()+1);
						List<TcJanalMenusEncabezadoDto> allChange=menuEncabezado.toAllChildren(change.getClave(), change.getNivel().intValue()+1);
						updateChildren(session, menuEncabezado.toKey(change.getClave(), change.getNivel().intValue()), allMenu);
						updateChildren(session, menuEncabezado.toKey(this.menu.getClave(), this.menu.getNivel().intValue()), allChange);
						String newKey=this.menu.getClave();
						this.menu.setClave(change.getClave());
						change.setClave(newKey);
						DaoFactory.getInstance().update(session, this.menu);
						DaoFactory.getInstance().update(session, change);
					} // if
					else {
						throw new RuntimeException("No se puede mover esta opción del menú");
					} // else
					break;
				case REGISTRAR:
					DaoFactory.getInstance().insert(session, this.menu);
					this.menu=menuEncabezado.getFather(this.menu.getClave());
					this.menu.setUltimo(2L);
					DaoFactory.getInstance().update(session, this.menu);
					break;
				case PROCESAR:
					reasignarClaves(session);
					break;
			} // switch
			regresar=true;
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			regresar=false;
			throw new RuntimeException(e);
		} // catch
		return regresar;
	}

	private void updateChildren(Session session, String newKey, List<TcJanalMenusEncabezadoDto> allChildren) throws Exception {
		for (TcJanalMenusEncabezadoDto dto : allChildren) {
			String key=dto.getClave().substring(newKey.length());
			dto.setClave(newKey.concat(key));
			DaoFactory.getInstance().update(session, dto);
		} // for
	}

	private boolean verificarPerfiles(Session session) throws Exception {
		Map<String, Object> params=new HashMap<>();
		params.put("idMenu", this.menu.getIdMenuEncabezado());
		Value val=DaoFactory.getInstance().toField(session, "TrJanalMenusPerfilesDto", "sizePerfilMenu", params, "total");
		return val==null||(Numero.getLong(val.getData().toString()).equals(0L));
	}

	public void reasignarClaves(Session session) throws Exception {
		MenuEncabezado menuEncabezado=null;
		Map<String, Object> params      =null;
		List<TcJanalMenusEncabezadoDto> menus     =null;
		List<TcJanalMenusEncabezadoDto> menusHijos=null;
		int indiceClave                 =1;
		String nuevaClave               ="";
		int nivelKey                    =0;
		try {
			menuEncabezado=new MenuEncabezado();
			params=new HashMap<>();
			if (Cadena.isVacio(this.menu.getClave())) {
				params.put(Constantes.SQL_CONDICION, "nivel = "+this.menu.getNivel());
			} // if
			else {
				String clave=menuEncabezado.toOnlyKey(this.menu.getClave(), this.menu.getNivel().intValue()+1);
				params.put(Constantes.SQL_CONDICION, "clave like '"+clave+"%' and nivel = "+(this.menu.getNivel().intValue()+1));
			} // else
			menus=DaoFactory.getInstance().findViewCriteria(TcJanalMenusEncabezadoDto.class, params, Constantes.SQL_TODOS_REGISTROS);
			if (menus==null||menus.isEmpty()) {
				throw new Exception("No se puede realizar la reasignación de claves de menú.");
			} // if
			nivelKey=Cadena.isVacio(this.menu.getClave()) ? this.menu.getNivel().intValue()-1 : this.menu.getNivel().intValue();
			for (TcJanalMenusEncabezadoDto menuPadre : menus) {
				menusHijos=menuEncabezado.toAllChildren(menuPadre.getClave(), menuPadre.getNivel().intValue()+1);
				nuevaClave=Cadena.rellenar(""+indiceClave, 2, '0', true);
				menuEncabezado.toCode(menuPadre.getClave());
				menuEncabezado.setKeyLevel(nuevaClave, nivelKey);
				menuPadre.setClave(menuEncabezado.toCode());
				for (TcJanalMenusEncabezadoDto menuHijo : menusHijos) {
					menuEncabezado.toCode(menuHijo.getClave());
					menuEncabezado.setKeyLevel(nuevaClave, nivelKey);
					menuHijo.setClave(menuEncabezado.toCode());
					DaoFactory.getInstance().update(session, menuHijo);
				} // for
				DaoFactory.getInstance().update(session, menuPadre);
				indiceClave+=1;
			} // for
		} // catch
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
	}
}
