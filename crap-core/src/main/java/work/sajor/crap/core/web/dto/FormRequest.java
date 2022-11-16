package work.sajor.crap.core.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 表单请求DTO
 * </p>
 *
 * @author Sajor
 * @since 2022-11-15
 *
 */
@Data
public class FormRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 数据签名
     */
    private String sign;
    
    /**
     * 请求时间戳
     */
    private Long timestamp;
    
    /**
     * 表单数据
     */
    private Map<String, Object> data;
}
