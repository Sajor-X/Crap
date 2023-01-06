package work.sajor.crap.core.json;

import lombok.Getter;

/**
 * <p>
 * 隐私枚举
 * </p>
 *
 * @author Sajor
 * @since 2023-01-06
 */
@Getter
public enum PrivacyTypeEnum {
    /**
     * 自定义（此项需设置脱敏的范围）
     */
    CUSTOMER,

    /**
     * 姓名
     */
    NAME,

    /**
     * 身份证号
     */
    ID_CARD,

    /**
     * 手机号
     */
    MOBILE,

    /**
     * 邮箱
     */
    EMAIL,

    /**
     * 秘密
     */
    SECRET,
}
