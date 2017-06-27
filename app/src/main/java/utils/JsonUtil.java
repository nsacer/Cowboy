package utils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class JsonUtil {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.getSerializationConfig().setSerializationInclusion(
				Inclusion.NON_NULL);
		// 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
		mapper.getDeserializationConfig()
				.set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
						false);
	}

	public static String Object2Json(Object o) {
		String s = null;
		try {
			if (o != null) {
				s = mapper.writeValueAsString(o);
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static <T> T Json2Object(String json, Class<T> c) {
		T t = null;
		try {
			if (json != null) {
				t = mapper.readValue(json, c);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}

	public static <T> T Json2Object(String json, TypeReference<T> tr) {
		T t = null;
		try {
			if (json != null) {
				t = (T) mapper.readValue(json, tr);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (T) t;
	}

	/**
	 * 此方法用于json串中二级json串的解析,如:{"type":1,"data":{"roomId":539,"dataBankTitle":
	 * "xxx"}},data就是指的二级json串 jackson在解析字符串的时候，会把二级json串解析成LinkedHashMap对象
	 * 所以此时要调用此方法来实例化我们的类
	 * 
	 * @param map
	 * @param object
	 */
	public static void jacksonMapToObj(Map<String, Object> map, Object object) {
		if (null != map && map.size() > 0) {
			Class<?> className = object.getClass();
			Field[] fields = className.getDeclaredFields();
			int size = fields.length;
			for (int i = 0; i < size; i++) {
				try {
					Field f = fields[i];
					// 属性名称
					String proper_name = f.getName();
					// 是同一个属性（直接设置值）
					if (map.containsKey(proper_name)) {
						// 拼接各个属性的set方法
						String method_name = "set"
								+ String.valueOf(proper_name.charAt(0))
										.toUpperCase()
								+ proper_name.substring(1);
						// 获取到set方法
						Method method = className.getMethod(method_name,
								f.getType());
						// 进行设置值
						method.invoke(object, map.get(proper_name));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
