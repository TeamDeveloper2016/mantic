/*
 * Code write by user.admin
 * Date 13/05/2009
 */

package mx.org.kaana.kajool.db.comun.page;

/**
 *
 * @author alejandro.jimenez
 */
public final class LinkPage {

	private int index;
	private int record;

	public LinkPage(int index, int record) {
		setIndex(index);
		setRecord(record);
	}

	public int getIndex() {
		return index;
	}

	private void setIndex(int index) {
		this.index = index;
	}

	public int getRecord() {
		return record;
	}

	private void setRecord(int record) {
		this.record = record;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(getIndex());
		sb.append(",");
		sb.append(getRecord());
		sb.append("]");
		return sb.toString();
	}
	
}
