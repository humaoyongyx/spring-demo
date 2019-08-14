package issac.study.springdemo.core.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * <p>Title: MessageUtils</p>
 *
 * @author Xuly
 */
@Component
public class MessageUtils {

    private static MessageSource messageSource;

    @Autowired
    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * 获取单个国际化翻译值
     */
    public static String get(String msgKey) {
        try {
            String message = messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return msgKey;
        }
    }

    /**
     * 获取单个国际化翻译值,带参数
     */
    public static String get(String msgKey,Object...objects) {
        try {
            return messageSource.getMessage(msgKey, objects, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return msgKey;
        }
    }
    /**
     * 获取单个国际化翻译值
     * @param msgKey
     * @param defaultMsg 如果获取不到值，返回默认给的值
     * @return
     */
    public static String get(String msgKey,String defaultMsg) {
        try {
            return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return defaultMsg;
        }
    }


}
