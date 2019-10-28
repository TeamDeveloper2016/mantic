package mx.org.kaana.libs.echarts.json;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 28/10/2019
 *@time 01:19:41 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class ItemSelected implements Serializable {

	private static final long serialVersionUID=6510083618373014298L;

	private Integer idKey;
	private String chart;
	private String color;
	private String name;
	private Double value;
	private Double percent;
	private Integer dataIndex;
	private String dataType;
	private String seriesId;
	private Integer seriesIndex;
	private String seriesName;
	private String seriesType;
	private String type;

	public ItemSelected() {
		this(new Random().nextInt(), "", "", "", 0D, 0D, 0, "", "", 0, "", "", "");
	}

	public ItemSelected(Integer idKey, String chart, String color, String name, Double value, Double percent, Integer dataIndex, String dataType, String seriesId, Integer seriesIndex, String seriesName, String seriesType, String type) {
		this.idKey=idKey;
		this.chart=chart;
		this.color=color;
		this.name=name;
		this.value=value;
		this.percent=percent;
		this.dataIndex=dataIndex;
		this.dataType=dataType;
		this.seriesId=seriesId;
		this.seriesIndex=seriesIndex;
		this.seriesName=seriesName;
		this.seriesType=seriesType;
		this.type=type;
	}

	public Integer getIdKey() {
		return idKey;
	}

	public void setIdKey(Integer idKey) {
		this.idKey=idKey;
	}

	public String getChart() {
		return chart;
	}

	public void setChart(String chart) {
		this.chart=chart;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color=color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value=value;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent=percent;
	}

	public Integer getDataIndex() {
		return dataIndex;
	}

	public void setDataIndex(Integer dataIndex) {
		this.dataIndex=dataIndex;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType=dataType;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId=seriesId;
	}

	public Integer getSeriesIndex() {
		return seriesIndex;
	}

	public void setSeriesIndex(Integer seriesIndex) {
		this.seriesIndex=seriesIndex;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName=seriesName;
	}

	public String getSeriesType() {
		return seriesType;
	}

	public void setSeriesType(String seriesType) {
		this.seriesType=seriesType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}

	@Override
	public int hashCode() {
		int hash=3;
		hash=59*hash+Objects.hashCode(this.idKey);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final ItemSelected other=(ItemSelected) obj;
		if (!Objects.equals(this.idKey, other.idKey)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ItemSelected{"+"chart="+chart+", color="+color+", name="+name+", value="+value+", percent="+percent+", dataIndex="+dataIndex+", dataType="+dataType+", seriesId="+seriesId+", seriesIndex="+seriesIndex+", seriesName="+seriesName+", seriesType="+seriesType+", type="+type+'}';
	}
	
}
