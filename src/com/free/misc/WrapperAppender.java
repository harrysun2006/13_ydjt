package com.free.misc;

import java.util.Properties;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.CyclicBuffer;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.spi.TriggeringEventEvaluator;
import org.apache.log4j.xml.UnrecognizedElementHandler;
import org.w3c.dom.Element;

public class WrapperAppender extends AppenderSkeleton implements UnrecognizedElementHandler {

  private int bufferSize = 512;
  private boolean locationInfo = true;
  private boolean submitOnClose = false;
  private WrapperLogger logger;

  protected CyclicBuffer cb = new CyclicBuffer(bufferSize);
  protected TriggeringEventEvaluator evaluator;

  public WrapperAppender() {
    this(new DefaultEvaluator());
  }

  public WrapperAppender(TriggeringEventEvaluator evaluator) {
    this.evaluator = evaluator;
  }

  public int getBufferSize() {
    return bufferSize;
  }

  public void setBufferSize(int bufferSize) {
    this.bufferSize = bufferSize;
    cb.resize(bufferSize);
  }

  public boolean getLocationInfo() {
    return locationInfo;
  }

  public void setLocationInfo(boolean locationInfo) {
    this.locationInfo = locationInfo;
  }

  public boolean isSubmitOnClose() {
    return submitOnClose;
  }

  public void setSubmitOnClose(boolean submitOnClose) {
    this.submitOnClose = submitOnClose;
  }

  public String getLoggerClass() {
    return logger == null ? null : logger.getClass().getName();
  }

  public void setLoggerClass(String value) {
    logger = (WrapperLogger) OptionConverter.instantiateByClassName(value, WrapperLogger.class, logger);
  }

  public WrapperLogger getLogger() {
    return logger;
  }

  public void setLogger(WrapperLogger logger) {
    if (logger == null) {
      throw new NullPointerException("logger");
    }
    this.logger = logger;
  }

  /**
   * Returns value of the <b>EvaluatorClass</b> option.
   */
  public String getEvaluatorClass() {
    return evaluator == null ? null : evaluator.getClass().getName();
  }

  /**
   * The <b>EvaluatorClass</b> option takes a string value representing the name
   * of the class implementing the {@link TriggeringEventEvaluator} interface. A
   * corresponding object will be instantiated and assigned as the triggering
   * event evaluator for the WrapperAppender.
   */
  public void setEvaluatorClass(String value) {
    evaluator = (TriggeringEventEvaluator) OptionConverter.instantiateByClassName(value,
        TriggeringEventEvaluator.class, evaluator);
  }

  /**
   * Get triggering evaluator.
   * 
   * @return triggering event evaluator.
   * @since 1.2.15
   */
  public final TriggeringEventEvaluator getEvaluator() {
    return evaluator;
  }

  /**
   * Sets triggering evaluator.
   * 
   * @param trigger
   *          triggering event evaluator.
   * @since 1.2.15
   */
  public final void setEvaluator(final TriggeringEventEvaluator trigger) {
    if (trigger == null) {
      throw new NullPointerException("trigger");
    }
    this.evaluator = trigger;
  }

  /**
   * {@inheritDoc}
   * 
   * @since 1.2.15
   */
  public boolean parseUnrecognizedElement(final Element element, final Properties props) throws Exception {
    if ("triggeringPolicy".equals(element.getNodeName())) {
      Object triggerPolicy = org.apache.log4j.xml.DOMConfigurator.parseElement(element, props,
          TriggeringEventEvaluator.class);
      if (triggerPolicy instanceof TriggeringEventEvaluator) {
        setEvaluator((TriggeringEventEvaluator) triggerPolicy);
      }
      return true;
    }
    return false;
  }

  public void activateOptions() {
    if (evaluator instanceof OptionHandler) {
      ((OptionHandler) evaluator).activateOptions();
    }
  }

  public boolean requiresLayout() {
    return true;
  }

  protected void append(LoggingEvent event) {
    if (!checkEntryConditions()) {
      return;
    }

    event.getThreadName();
    event.getNDC();
    event.getMDCCopy();
    if (locationInfo) {
      event.getLocationInformation();
    }
    event.getRenderedMessage();
    event.getThrowableStrRep();
    cb.add(event);
    if (evaluator.isTriggeringEvent(event)) {
      commitBuffer();
    }

  }

  protected boolean checkEntryConditions() {
    if (this.logger == null) {
      errorHandler.error("Logger object not configured.");
      return false;
    }

    if (this.evaluator == null) {
      errorHandler.error("No TriggeringEventEvaluator is set for appender [" + name + "].");
      return false;
    }

    if (this.layout == null) {
      errorHandler.error("No layout set for appender named [" + name + "].");
      return false;
    }
    return true;
  }

  synchronized public void close() {
    this.closed = true;
    if (submitOnClose && cb.length() > 0) {
      commitBuffer();
    }
  }

  protected void commitBuffer() {
    StringBuffer sbuf = new StringBuffer();
    String t = layout.getHeader();
    String f = "";
    if (t != null)
      sbuf.append(t);
    int len = cb.length();
    LoggingEvent event;
    for (int i = 0; i < len; i++) {
      event = cb.get();
      if (layout.ignoresThrowable()) {
        String[] s = event.getThrowableStrRep();
        if (s != null) {
          for (int j = 0; j < s.length; j++) {
            if (s[j].length() > 0 && f.length() == 0)
              f = s[j];
            sbuf.append(s[j]);
            sbuf.append(Layout.LINE_SEP);
          }
        }
      }
      t = layout.getFooter();
      if (t != null)
        sbuf.append(t);
      try {
        logger.append(event, sbuf.toString(), f);
      } catch (Throwable tt) {
        LogLog.error("Error occured while using WrapperAppender to log the event!", tt);
      }
    }
  }

}

class DefaultEvaluator implements TriggeringEventEvaluator {

  public boolean isTriggeringEvent(LoggingEvent event) {
    return event.getLevel().isGreaterOrEqual(Level.ERROR);
  }
}
