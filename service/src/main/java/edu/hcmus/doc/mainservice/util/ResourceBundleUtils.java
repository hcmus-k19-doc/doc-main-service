package edu.hcmus.doc.mainservice.util;

import edu.hcmus.doc.mainservice.model.enums.MESSAGE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import java.util.Locale;
@Component
public class ResourceBundleUtils {
  private static ResourceBundleMessageSource messageSource;

  @Autowired
  public void setMessageSource(ResourceBundleMessageSource messageSource) {
    ResourceBundleUtils.messageSource = messageSource;
  }

  /**
   * Get locale from request header
   */
  public static Locale getLocale() {
    return LocaleContextHolder.getLocale();
  }

  public static String getContent(MESSAGE messageKey) {
    String msgKey = messageKey.name();
    String content;
    try {
      Locale locale = getLocale();
      content = messageSource.getMessage(msgKey, null, locale);
    } catch (NoSuchMessageException e) {
      System.out.println(e.getMessage());
      content = getDefaultContent(msgKey);
    }
    return content;
  }

  private static String getDefaultContent(String msgKey) {
    String content;
    try {
      Locale defaultLocale = new Locale("vi", "VN");
      content = messageSource.getMessage(msgKey, null, defaultLocale);
    } catch (NoSuchMessageException e) {
      content = msgKey;
    }
    return content;
  }
}
