package work.sajor.crap.core.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import work.sajor.crap.core.mybatis.support.Wrapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拓展Mapper
 * </p>
 *
 * @author Sajor
 * @since 2022-11-10
 */
public interface Mapper<T> extends BaseMapper<T> {


    /**
     * 字段自增
     *
     * increaseFields  : key 字段名 => value 自增量
     */
    Integer increase(@Param(Constants.COLUMN_MAP) Map<String, Integer> increaseFields, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 动态查询
     * 查询字段 wrapper.fields
     * 连接条件 wrapper.join
     */
    List<Map<String, Object>> selectMapList(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 动态查询(分页)
     * 查询字段 wrapper.fields
     * 连接条件 wrapper.join
     */
    <E extends IPage<T>> IPage<Map<String, Object>> selectMapPage(@Param(Constants.WRAPPER) Wrapper<T> wrapper, E page);

    /**
     * 模板查询
     */
    List<Map<String, Object>> selectSqlList(@Param(Constants.WRAPPER) Wrapper<T> wrapper);
}
