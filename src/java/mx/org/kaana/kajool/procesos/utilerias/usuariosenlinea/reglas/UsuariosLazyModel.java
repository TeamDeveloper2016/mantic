package mx.org.kaana.kajool.procesos.utilerias.usuariosenlinea.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.procesos.beans.Usuario;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/10/2016
 *@time 10:44:51 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class UsuariosLazyModel extends LazyDataModel<Usuario> implements Serializable {

  private static final long serialVersionUID = 5536376960802310226L;
  private List<Usuario> usuarios;

  public UsuariosLazyModel(List<Usuario> usuarios) {
    this.usuarios= usuarios;
  } // UsuariosLazyModel

  @Override
  public Object getRowKey(Usuario usuario) {
    return usuario.getSession();
  } // getRowKey

  @Override
  public Usuario getRowData(String rowKey) {
    for (Usuario usuario : usuarios) {
      if (usuario.getSession().equals(rowKey))
        return usuario;
    } //for
    return null;
  } // getRowData

  @Override
  public List<Usuario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
    int dataSize          = this.usuarios.size();
    List<Usuario> regresar= new ArrayList<>();
    this.setRowCount(dataSize);
    if (dataSize>pageSize) {
      try {
        regresar= this.usuarios.subList(first, first+pageSize);
      }// try
      catch (IndexOutOfBoundsException e) {
        regresar=regresar.subList(first, first+(dataSize%pageSize));
      } // catch
    }// if
    else
      regresar.addAll(this.usuarios);
    return regresar;
  } // load

	@Override
	public void setRowIndex(final int rowIndex) {
    if(rowIndex== -1||getPageSize()== 0)
      super.setRowIndex(-1);
		else
      super.setRowIndex(rowIndex % getPageSize());
	} // setRowIndex
}
