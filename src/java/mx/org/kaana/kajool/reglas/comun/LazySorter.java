package mx.org.kaana.kajool.reglas.comun;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazySorter<T> implements Comparator<T> {

	private String sortField;
	private SortOrder sortOrder;

	public LazySorter(String sortField, SortOrder sortOrder) {
		this.sortField=sortField;
		this.sortOrder=sortOrder;
	}

	public int compare(T object1, T object2) {
		try {
			Object value1=String.valueOf(object1.getClass().getMethod("get"+this.sortField.substring(0, 1).toUpperCase()+this.sortField.substring(1)).invoke(object1));
			Object value2=String.valueOf(object2.getClass().getMethod("get"+this.sortField.substring(0, 1).toUpperCase()+this.sortField.substring(1)).invoke(object2));
			//Object value2 = TrUsuariosDto.class.getField(this.sortField).get(user2);
			int value=((Comparable) value1).compareTo(value2);
			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1*value;
		} // try
		catch (Exception e) {
			throw new RuntimeException("Error al comparar", e);
		} // catch
	}
}
