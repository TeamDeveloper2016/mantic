package mx.org.kaana.kajool.db.comun.dto;

import java.util.Map;

public interface IBaseDto {

    public Long getKey();
    public void setKey(Long key);
    @Override
    public String toString();
    public Map<String, Object> toMap();
    public Object[] toArray();
    public boolean isValid();
    public Object toValue(String name);
    public String toAllKeys();
    public String toKeys();
    public Class toHbmClass();

}
