package work.sajor.crap.core.mybatis.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import work.sajor.crap.core.util.AESUtil;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * AES加密处理器
 * </p>
 *
 * @author Sajor
 * @since 2022-11-27
 */
@MappedTypes({String.class})
public class AESEncryptHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if(StringUtils.isNotBlank(parameter)){
            ps.setString(i, AESUtil.encrypt(parameter));
        }else{
            ps.setString(i, null);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        return AESUtil.decrypt(columnValue);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return AESUtil.decrypt(columnValue);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String columnValue = cs.getString(columnIndex);
        return AESUtil.decrypt(columnValue);
    }
}
