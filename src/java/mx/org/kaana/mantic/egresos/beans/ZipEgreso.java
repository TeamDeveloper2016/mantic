package mx.org.kaana.mantic.egresos.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ZipEgreso implements Serializable{

	private static final long serialVersionUID = -9189957005027629976L;	
	private String carpeta;
	private List<String> files;

	public ZipEgreso() {
		this("", new ArrayList<>());
	}
	
	public ZipEgreso(String carpeta, List<String> files) {
		this.carpeta = carpeta;
		this.files = files;
	}

	public String getCarpeta() {
		return carpeta;
	}

	public void setCarpeta(String carpeta) {
		this.carpeta = carpeta;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}	
}
