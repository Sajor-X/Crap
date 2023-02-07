package work.sajor.crap.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * <p>
 * 修改密码
 * </p>
 *
 * @author Sajor
 * @since 2023-02-07
 */
@Data
public class SystemPasswordDTO {
    /**
     * 原密码
     */
    @JsonProperty("password")
    String password;

    /**
     * 新密码
     */
    @JsonProperty("new_password")
    String newPassword;
}
