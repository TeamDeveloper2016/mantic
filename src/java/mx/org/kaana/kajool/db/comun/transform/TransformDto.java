package mx.org.kaana.kajool.db.comun.transform;



import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class TransformDto extends Transformer {

  private static final Log LOG= LogFactory.getLog(TransformDto.class);
	private static final long serialVersionUID=8126932408512312555L;

  public TransformDto(Class<? extends IBaseDto> dto) {
    super(dto);
  }

  @Override
  public Object tuple(Object[] data, String[] fields, String[] bdNames) throws InstantiationException, IllegalAccessException {
    Object regresar        = toNewDto();
    // StringBuilder sb       = new StringBuilder();
    BeanUtilsBean utilsBean= new BeanUtilsBean(new ConvertUtilsBean(), new PropertyUtilsBean());
    for (int x=0; x<data.length; x++) {
      try {
				if(data[x]!= null)
          utilsBean.setProperty(regresar, fields[x], data[x]);
      } // try
      catch(Exception e) {
        Error.mensaje(e, " Class: "+ regresar.getClass());
        //sb.append(fields[x]).append("[").append(" Class: "+ regresar.getClass()).append("],");
        LOG.warn(fields[x]+ "[".concat(" Class: ").concat(regresar.getClass().getSimpleName()).concat("] Error: ")+ e);
      } // catch
    } // for
//    if(sb.length()> 0)
//      throw new FieldNotFoundException(getDto().getName(), sb.substring(0, sb.length()- 1));
//    sb= null;
    return regresar;
  }

}
