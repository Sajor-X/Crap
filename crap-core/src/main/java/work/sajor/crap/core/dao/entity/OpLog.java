package work.sajor.crap.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import work.sajor.crap.core.mybatis.facade.FieldEnum;
import work.sajor.crap.core.dao.entity.base.OpLogBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
// import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 数据变更日志
 *
 * 实体扩展
 *
 * @author Sajor
 * @since 2022-12-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "crap_op_log", autoResultMap = true)
// @Document(indexName = "crap_op_log")
public class OpLog extends OpLogBase {

}