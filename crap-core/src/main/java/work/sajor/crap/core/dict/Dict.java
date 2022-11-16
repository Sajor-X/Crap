package work.sajor.crap.core.dict;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import work.sajor.crap.core.mybatis.facade.FieldEnum;
import work.sajor.crap.core.util.CommonUtil;
import work.sajor.crap.core.web.WebException;

import java.util.*;
import java.util.Map.Entry;

/**
 * <p>
 * 字典
 * </p>
 *
 * @author Sajor
 * @since 2022-11-15
 */
@Slf4j
@Data
@Accessors(chain = true)
public class Dict {
    
    /**
     * key 字段
     */
    private String idField = "id";
    
    /**
     * label 字段
     */
    private String nameField = "name";
    
    /**
     * code 字段
     */
    private String codeField = "code";
    
    /**
     * 传输到前端的字段列表
     */
    private List<String> fields = new ArrayList<>();
    
    /**
     * 向前端传输所有字段
     */
    private boolean allFields = false;
    
    /**
     * 数据
     */
    private Map<Object, Object> source = new LinkedHashMap<>();
    
    public Dict(Object source) {
        this(source, "", "");
    }
    
    public Dict(Object source, String idField, String nameField) {
        this.idField = StrUtil.isNotEmpty(idField) ? idField : this.idField;
        this.nameField = StrUtil.isNotEmpty(nameField) ? idField : this.nameField;
        set(source);
    }
    
    /**
     * 设置数据
     */
    @SuppressWarnings({"unchecked"})
    public void set(Object sourceObj) {
        if (sourceObj == null) {
            source.clear();
            return;
        }
        
        if (sourceObj instanceof Map) {
            source = (Map<Object, Object>) sourceObj;
            return;
        }
        
        if (sourceObj instanceof List) {
            for (Object item : (List) sourceObj) {
                source.put(CommonUtil.getValue(item, idField), item);
            }
            return;
        }
        
        throw new WebException("字典数据类型错误 :" + sourceObj.getClass());
    }
    
    /**
     * 获取翻译 label
     */
    public String getName(Object id) {
        return getName(id, String.class);
    }
    
    /**
     * 获取翻译 label
     */
    public <T> T getName(Object id, Class<T> nameType) {
        return CommonUtil.getValue(getRaw(id), nameField, nameType);
    }
    
    /**
     * 获取翻译 label
     */
    public List<String> getName(List idList) {
        return getName(idList, String.class);
    }
    
    /**
     * 获取翻译 label
     */
    public <T> List<T> getName(List idList, Class<T> nameType) {
        ArrayList<T> labelList = new ArrayList<>();
        for (Object id : idList) {
            T label = getName(id, nameType);
            if (label != null) {
                labelList.add(label);
            }
        }
        return labelList;
    }
    
    /**
     * 获取原始数据
     */
    public Object getRaw(Object id) {
        return getRaw(id, Object.class);
    }
    
    /**
     * 获取原始数据
     */
    public <T> T getRaw(Object id, Class<T> rawType) {
        Object raw = source.get(id);
        return Convert.convert(rawType, raw);
    }
    
    /**
     * 反转
     */
    public Long getId(Object name) {
        return getId(name, Long.class);
    }
    
    /**
     * 反转
     */
    public <T> T getId(Object label, Class<T> nameType) {
        if (label == null) {
            return null;
        }
        for (Entry<Object, Object> item : source.entrySet()) {
            if (CommonUtil.getValue(item.getValue(), nameField).equals(label)) {
                return Convert.convert(nameType, item.getKey());
            }
        }
        return null;
    }
    
    /**
     * 反转
     */
    public List<Long> getId(List nameList) {
        return getId(nameList, Long.class);
    }
    
    /**
     * 反转
     */
    public <T> List<T> getId(List nameList, Class<T> nameType) {
        ArrayList<T> list = new ArrayList<>();
        for (Object name : nameList) {
            T id = getId(name, nameType);
            if (id != null) {
                list.add(id);
            }
        }
        return list;
    }
    
    
    /**
     * 传递到前端
     */
    @JsonValue
    public List<Object> jsonValue() {
        
        if (this.allFields) {                                                   // 返回原始数据
            return ListUtil.toList(this.source);
        }
        
        ArrayList<Object> jsonList = new ArrayList<>();
        this.source.forEach((id, value) -> {
            if (value == null) {
                return;
            }
            
            Map<String, Object> item = new HashMap<>();
            jsonList.add(item);
            
            if (id instanceof FieldEnum) {
                item.put("id", ((FieldEnum) id).getValue());
            } else {
                item.put("id", id);
            }
            
            if (CommonUtil.isSimpleType(value)) {                               // 基本类型直接作为 name
                item.put("name", value);
                return;
            }
            item.put("name", CommonUtil.getValue(value, nameField));
            item.put("code", CommonUtil.getValue(value, codeField));
            
            for (String field : fields) {
                try {
                    item.put(field, CommonUtil.getValue(value, field));
                } catch (Exception e) {
                    log.debug("转换前端字典错误 {} : {}", field, e.getMessage());
                }
            }
            
        });
        return jsonList;
    }
}
