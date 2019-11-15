package mx.org.kaana.libs.echarts.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 29/10/2019
 *@time 01:56:38 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class StringTypeAdapter extends TypeAdapter<String> {

	private static final String FUNCTION_NAMES= "function";
	
	@Override
	public void write(JsonWriter writer, String value) throws IOException {
    if (value == null) 
      writer.nullValue();
		else
			if(value.trim().toLowerCase().startsWith(FUNCTION_NAMES))
        writer.jsonValue(value);		
		  else
				writer.value(value);
	}

	@Override
	public String read(JsonReader reader) throws IOException {
		String regresar= null;
    if (reader.peek() == JsonToken.NULL) 
      reader.nextNull();
		else 
       regresar= reader.nextString();
     return regresar;
	}

}
