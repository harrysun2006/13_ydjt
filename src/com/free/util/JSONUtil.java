package com.free.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

public class JSONUtil {

  public static String getSafeJSON(Object obj) {
    String r = "", t;
    try {
      r = getJSON(obj);
    } catch (Exception e) {
      t = e.getMessage();
      t = t.replace("\"", "\\\\\"");
      r = "{\"error\": \"" + t + "\"}";
    }
    return r;
  }

  public static String getJSON(Object obj) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.getSerializationConfig().disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
    mapper.getSerializationConfig().disable(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES);
    mapper.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
    Writer sw = new StringWriter();
    mapper.writeValue(sw, obj);
    sw.flush();
    sw.close();
    return sw.toString();
  }

  public static <T> T getObject(String s, Class<T> clazz) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    // 跳过未知(无对应)的属性
    mapper.getDeserializationConfig().disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
    T obj = mapper.readValue(s, clazz);
    return obj;
  }

  public static <T> List<T> getObjects(String s, Class<?> T) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.getDeserializationConfig().disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
    List<T> list = mapper.readValue(s, new TypeReference<List<T>>() {
    });
    return list;
  }
}
