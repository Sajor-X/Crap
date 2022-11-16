package work.sajor.crap.core.web.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.experimental.Accessors;
import work.sajor.crap.core.web.WebDict;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 列表响应 DTO
 * </p>
 *
 * @author Sajor
 * @since 2022-11-15
 */
@Data
@Accessors(chain = true)
public class TableResponse {

    /**
     * 字典
     */
    WebDict dict = new WebDict();

    /**
     * 列表
     */
    Map<String, Object> data = Collections.emptyMap();

    /**
     * 合计
     */
    Map<String, Object> summary = Collections.emptyMap();

    /**
     * 其他数据
     */
    Object extra;

    public TableResponse() {
    }

    /**
     * mybatis 分页
     */
    public TableResponse(IPage page) {
        data = new HashMap<>();
        data.put("records", page.getRecords());
        data.put("total", page.getTotal());
        data.put("current", page.getCurrent());
        data.put("size", page.getSize());
    }

    /**
     * es 分页
     */
    public TableResponse(org.springframework.data.domain.Page page) {
        data = new HashMap<>();
        data.put("records", page.getContent());
        data.put("total", page.getTotalElements());
        data.put("current", page.getNumber());
        data.put("size", page.getSize());
    }

    public TableResponse(List list) {
        data = new HashMap<>();
        data.put("records", list);
        data.put("total", list.size());
        data.put("current", 1);
        data.put("size", list.size());
    }
}
