package mx.org.kaana.libs.pagina;

import java.io.Serializable;
import java.util.Properties;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12-feb-2014
 *@time 16:36:09
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Message implements Serializable {

  private static final long serialVersionUID = 3254438703499285238L;

  private String name;
  private String resource;
  private Properties properties;

  public Message(String name, String resource, Properties properties) {
    this.name = name;
    this.resource = resource;
    this.properties = properties;
  }

  public String getName() {
    return name;
  }

  public String getResource() {
    return resource== null? "": resource.endsWith("/")? resource: resource.concat("/");
  }

  public Properties getProperties() {
    return properties;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 13 * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Message other = (Message) obj;
    if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Message{name=" + name + ", resource=" + resource + '}';
  }

}
