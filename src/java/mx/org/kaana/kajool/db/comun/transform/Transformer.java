package mx.org.kaana.kajool.db.comun.transform;



import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.libs.formato.Cadena;
import org.hibernate.transform.ResultTransformer;

public abstract class Transformer implements ResultTransformer {

  private Class dto;
  private boolean change;
  private String[] bdNames;

  public Transformer() {
    this(null);
  }

  public Transformer(Class<? extends IBaseDto> dto) {
    setDto(dto);
    setChange(true);
  }

  public void setDto(Class dto) {
    this.dto = dto;
  }

  public Class getDto() {
    return dto;
  }

  protected void setChange(boolean change) {
    this.change = change;
  }

  protected boolean isChange() {
    return change;
  }

  protected void setBdNames(String[] bdNames) {
    this.bdNames = bdNames;
  }

  public String[] getBdNames() {
    return bdNames;
  }

  public abstract Object tuple(Object[] data, String[] fields, String[] bdNames) throws InstantiationException, IllegalAccessException;

  @Override
  public Object transformTuple(Object[] data, String[] fields) {
    Object regresar= null;
    if (isChange()) {
      setBdNames(new String[fields.length]);
      for(int x= 0; x< fields.length; x++) {
        this.bdNames[x]= fields[x];
        fields[x]= Cadena.toBeanNameEspecial(fields[x]);
				// Se modifico al toBeanNameEspecial por los campos que traen una letra y enseguida guion bajo (x_) --fields[x]= Cadena.toBeanName(fields[x]);
      }
      setChange(false);
    } // if
    try {
      regresar= tuple(data, fields, this.bdNames);
    }
    catch(Exception e) {
      regresar= null;
    } // catch
    return regresar;
  }

  @Override
  public List transformList(List list) {
    return list;
  }

  protected Object toNewDto() throws InstantiationException, IllegalAccessException {
    return getDto()!= null? getDto().newInstance(): null;
  }

}
