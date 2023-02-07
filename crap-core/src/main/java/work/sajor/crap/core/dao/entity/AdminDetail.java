package work.sajor.crap.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import work.sajor.crap.core.mybatis.facade.FieldEnum;
import work.sajor.crap.core.dao.entity.base.AdminDetailBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
// import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 
 *
 * 实体扩展
 *
 * @author Sajor
 * @since 2023-02-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "crap_admin_detail", autoResultMap = true)
// @Document(indexName = "crap_admin_detail")
public class AdminDetail extends AdminDetailBase {

}