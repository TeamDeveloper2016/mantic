package mx.org.kaana.libs.echarts.pic;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2019
 *@time 12:19:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class BaseAttr implements Serializable {

	private static final long serialVersionUID=-4271194453055348485L;
	
	private boolean show;

	public BaseAttr() {
	}

	public BaseAttr(boolean show) {
		this.show=show;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show=show;
	}

	@Override
	public String toString() {
		return "BaseAttr{"+"show="+show+'}';
	}
	
}
