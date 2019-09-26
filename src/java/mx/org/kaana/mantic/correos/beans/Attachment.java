package mx.org.kaana.mantic.correos.beans;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.libs.pagina.JsfBase;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/03/2019
 *@time 07:46:52 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Attachment implements Serializable {

	private static final long serialVersionUID=-6482879638910606700L;
  
	private String id;
	private String name;
	private String path;
	private String absolute;
	private File file;
	private Boolean cid;
			
	
	public Attachment(String absolute, Boolean cid) {
		this(JsfBase.getRealPath(""), absolute, cid, true);
	}
	
	public Attachment(String path, String absolute, Boolean cid, Boolean principal) {
		this.absolute= path.concat(absolute);
		this.file= new File(this.absolute);
		this.init(cid);
	} 

	public Attachment(String id, String absolute, Boolean cid) {
		this(absolute, cid);
		this.id= id;
	}
	
	public Attachment(String path, String id, String absolute, Boolean cid) {
		this(path, absolute, cid, true);
		this.id= id;
	}

	public Attachment(File file, Boolean cid) {
		this.absolute= file.getAbsolutePath();
		this.file= file;
		this.init(cid);
	}

	public Attachment(String id, File file, Boolean cid) {
		this.file= file;
		this.init(cid);
		this.id= id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id=id;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public File getFile() {
		return file;
	}

	public Boolean getCid() {
		return cid;
	}

	public String getAbsolute() {
		return absolute;
	}
	
	private void init(Boolean cid) {
		this.id  = file.getName();
		if(this.id.contains("."))
			this.id= this.id.substring(0, this.id.indexOf("."));
		this.name= file.getName();
		this.cid = cid;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=17*hash+Objects.hashCode(this.id);
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
		final Attachment other=(Attachment) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Attachment{"+"id="+id+", name="+name+", path="+path+", file="+file+'}';
	}
	
}
