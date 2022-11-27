package work.sajor.crap.server;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * 加密
 * </p>
 *
 * @author Sajor
 * @since 2022-11-24
 */
public class JasyptTest {

    String password = "b1ncjbS<AYq'/iH";
    @Test
    public void testEncrypt() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");          // 加密的算法，这个算法是默认的
        config.setPassword(password);                       // 加密的密钥，必须为ASCll码
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "123456";
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptedText);
    }

    @Test
    public void testDecrypt() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword(password);
        standardPBEStringEncryptor.setConfig(config);
        String encryptedText = "PlN23RnLACpu7quB0QRlnA==";
        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
        System.out.println(plainText);
    }
}
