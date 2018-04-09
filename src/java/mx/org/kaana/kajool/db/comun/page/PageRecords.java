/*
 * Code write by user.admin
 * Date 12/05/2009
 */

package mx.org.kaana.kajool.db.comun.page;

import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *
 * @author alejandro.jimenez
 */
public final class PageRecords {

	private final int SEGMENT= 15;
	
	private int page;
	private int records;
	private int count;
	private int top;
	private List<IBaseDto> list;
	private List<LinkPage> pages;

	public PageRecords() {
		this(0, 0, 0, null);
	}

	public PageRecords(int page, int records, List<IBaseDto> list) {
		this(page, records, 0, list);
	}
	
	public PageRecords(int page, int records, int count, List<IBaseDto> list) {
		this.page   = page;
		this.records= records;
		this.list   = list;
		this.pages  = new ArrayList<>();
		setCount(count);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		if(getRecords()> 0) {
		  this.top= (int)(getCount()/ getRecords())+ (getCount()% getRecords()== 0? 0: 1);
		  if(getList()!= null && getList().size()> 0 && getTop()> 1)
  		  calculate(false);
		} // if	
	}

	public List<IBaseDto> getList() {
		return list;
	}

	public int getPage() {
		return page;
	}

	public int getRecords() {
		return records;
	}

	public List<LinkPage> getPages() {
		return pages;
	}

	public int getTop() {
		return top;
	}
	
	public int getSegment() {
		return this.SEGMENT;
	}

	private int getStart() {
		int middle  = (int)((this.SEGMENT- 1)/ 2)+ ((this.SEGMENT% 2== 0? 1: 0));
		int regresar= getPage()- middle;
	  return regresar< 1 || getTop()<= this.SEGMENT? 1: regresar+ middle> getTop()? getTop()- this.SEGMENT: regresar;	
	}
	
	public void calculate(boolean all) {
		this.pages.clear();
		this.page= getPage()> getTop()? getTop(): getPage();
		int start= getStart();
		int index= start;
		int inc  = (index- 1)* getRecords()+ 1;
		while(index<= getTop() && getRecords()< getCount() && (all || index< (start+ this.SEGMENT)) && inc<= getCount()) {
			this.pages.add(new LinkPage(index, inc));
			index++;
			inc+= getRecords();
		} // while
	}

	@Override
	protected void finalize() throws Throwable {
		if(this.list!= null)
			this.list.clear();
		this.list= null;
		if(this.pages!= null)
			this.pages.clear();
		this.pages= null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(getPage());
		sb.append(",");
		sb.append(getRecords());
		sb.append(",");
		sb.append(getCount());
		sb.append(",");
		sb.append(getList());
		sb.append(",");
		sb.append(getPages());
		sb.append("]");
		return sb.toString();
	}
	
}
