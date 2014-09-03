package com.free.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

public class CommonUtil {

  public static final DateFormatSymbols NUMBER_FORMAT_SYMBOLS = new DateFormatSymbols();
  public static final byte[] DUMMY_BYTES = new byte[]{};

  private static final String[] WEEKDAYS = { "6", "7", "1", "2", "3", "4", "5", "6" };

  static {
    NUMBER_FORMAT_SYMBOLS.setWeekdays(WEEKDAYS);
    NUMBER_FORMAT_SYMBOLS.setShortWeekdays(WEEKDAYS);
  }

  private CommonUtil() {
  }

  public static Date getDate(String format, String time) throws ParseException {
    DateFormat df = new SimpleDateFormat(format);
    return df.parse(time);
  }

  public static String leftPadding(String pattern, int i) {
    return leftPadding(pattern, String.valueOf(i));
  }

  public static String leftPadding(String pattern, Object obj) {
    return (obj == null) ? leftPadding(pattern, "") : leftPadding(pattern, obj.toString());
  }

  public static String leftPadding(String pattern, String text) {
    return padding(pattern, text, true);
  }

  public static String rightPadding(String pattern, int i) {
    return rightPadding(pattern, String.valueOf(i));
  }

  public static String rightPadding(String pattern, Object obj) {
    return (obj == null) ? rightPadding(pattern, "") : rightPadding(pattern, obj.toString());
  }

  public static String rightPadding(String pattern, String text) {
    return padding(pattern, text, false);
  }

  private static String padding(String pattern, String text, boolean left) {
    if (pattern == null)
      return text;
    if (text == null)
      text = "";
    int tl = (text == null) ? 0 : text.getBytes().length;
    int pl = (pattern == null) ? 0 : pattern.getBytes().length;
    StringBuilder sb = new StringBuilder(pl);
    if (pl > tl) {
      if (left)
        sb.append(pattern.substring(0, pl - tl)).append(text);
      else
        sb.append(text).append(pattern.substring(tl));
    } else {
      sb.append(text);
    }
    return sb.toString();
  }

  public static String formatCalendar(String format, Calendar cal) {
    return formatDate(format, (cal == null) ? (Date) null : cal.getTime(), NUMBER_FORMAT_SYMBOLS);
  }

  public static String formatCalendar(String format, Calendar cal, DateFormatSymbols symbols) {
    return formatDate(format, (cal == null) ? (Date) null : cal.getTime(), symbols);
  }

  public static String formatDate(String format, Date date) {
    return formatDate(format, date, NUMBER_FORMAT_SYMBOLS);
  }

  public static String formatDate(String format, Date date, DateFormatSymbols symbols) {
    if (format == null || date == null)
      return null;
    SimpleDateFormat df = new SimpleDateFormat(format);
    if (symbols != null)
      df.setDateFormatSymbols(symbols);
    return df.format(date);
  }

  public static String formatNumber(String format, int i) {
    return formatNumber(format, new Integer(i));
  }

  public static String formatNumber(String format, long l) {
    return formatNumber(format, new Long(l));
  }

  public static String formatNumber(String format, double d) {
    return formatNumber(format, new Double(d));
  }

  public static String formatNumber(String format, Number number) {
    if (format == null || number == null)
      return null;
    NumberFormat nf = new DecimalFormat(format);
    return nf.format(number);
  }

  public static String repeat(CharSequence s, int count) {
    if (s == null || s.length() == 0)
      return s.toString();
    StringBuilder sb = new StringBuilder(count * s.length());
    for (int i = 0; i < count; i++)
      sb.append(s);
    return sb.toString();
  }

  public static String repeat(char c, int count) {
    StringBuilder sb = new StringBuilder(count);
    for (int i = 0; i < count; i++)
      sb.append(c);
    return sb.toString();
  }

  public static int getInt(Object value) {
    return getInt(value, Number.class);
  }

  public static int getInt(Object value, @SuppressWarnings("rawtypes") Class clazz) {
    if (value == null) {
      return 0;
    } else if (clazz != null && Number.class.isAssignableFrom(clazz)) {
      Number n = (Number) value;
      return n.intValue();
    } else {
      return getInt(value.toString());
    }
  }

  public static int getInt(String value) {
    return getInt(value, 0);
  }

  public static int getInt(String value, int def) {
    if (isBlank(value))
      return def;
    int r = def;
    try {
      r = Integer.parseInt(value);
    } catch (NumberFormatException nfe) {
    }
    return r;
  }

  public static Integer getInteger(String value) {
    return getInteger(value, null);
  }

  public static Integer getInteger(String value, Integer def) {
    if (isBlank(value))
      return def;
    Integer r = def;
    try {
      r = Integer.parseInt(value);
    } catch (NumberFormatException nfe) {
    }
    return r;
  }

  public static Long getLong(String value) {
    return Long.parseLong(value);
  }

  public static Long getLong(String value, Long def) {
    if (isBlank(value))
      return def;
    Long r = def;
    try {
      r = Long.parseLong(value);
    } catch (NumberFormatException nfe) {
    }
    return r;
  }

  public static double getDouble(String value) {
    return Double.parseDouble(value);
  }

  public static double getDouble(String value, double defaultValue) {
    if (isBlank(value))
      return defaultValue;
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException nfe) {
      return defaultValue;
    }
  }

  public static boolean getBoolean(Object value) {
    return getBoolean(value, Boolean.class);
  }

  @SuppressWarnings("rawtypes")
  public static boolean getBoolean(Object value, Class clazz) {
    if (value == null) {
      return false;
    } else if (clazz != null && Boolean.class.isAssignableFrom(clazz)) {
      Boolean b = (Boolean) value;
      return b.booleanValue();
    } else {
      return getBoolean(value.toString());
    }
  }

  public static boolean getBoolean(String value) {
    return (value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("T")
        || value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("1")));
  }

  public static boolean getBoolean(String value, boolean defaultValue) {
    if (isBlank(value))
      return defaultValue;
    try {
      return Boolean.parseBoolean(value);
    } catch (NumberFormatException nfe) {
      return defaultValue;
    }
  }

  public static Long bytes2Long(byte[] b) {
    if (b == null || b.length <= 0)
      return null;
    long l = 0;
    for (int i = 0; i < b.length; i++) {
      l = (l << 8) | (b[i] < 0 ? b[i]+256 : b[i]);
    }
    return new Long(l);
  }

  public static Calendar bytes2Cal(byte[] b) {
    // HHmmssMdyyyy? or HHmmssMMddyy?
    return Calendar.getInstance();
  }

  /**
   * get filtered properties from given properties
   * 
   * @param props
   * @param fregex
   *          used to filter keys
   * @param kregex
   *          get the renamed keys
   * @return
   */
  public static Properties getProperties(Properties props, String fregex, String kregex) {
    if (props == null)
      return null;
    Pattern fp = (fregex == null) ? null : Pattern.compile(fregex);
    Pattern kp = (kregex == null) ? null : Pattern.compile(kregex);
    Matcher fm, km;
    Properties r = new Properties();
    Set<Object> keys = props.keySet();
    Iterator<Object> it = keys.iterator();
    String key, value;
    while (it.hasNext()) {
      key = (String) it.next();
      value = props.getProperty(key);
      if (fp != null) {
        fm = fp.matcher(key);
        // filter keys using the fregex
        if (fm.matches()) {
          // rename keys using the kregex
          if (kp != null) {
            km = kp.matcher(key);
            if (km.find()) {
              if (km.groupCount() > 0)
                key = km.group(1);
              else
                key = km.group(0);
            }
          }
          r.put(key, value);
        }
      }
    }
    return r;
  }

  public static Throwable getRootCause(Throwable t) {
    Throwable r = t;
    while (r.getCause() != null)
      r = r.getCause();
    return r;
  }

  public static byte[] readFile(File f) throws IOException {
    BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
    ByteArrayOutputStream out = new ByteArrayOutputStream(102400);
    byte[] b = new byte[1024];
    int size = 0;
    while ((size = in.read(b)) != -1) {
      out.write(b, 0, size);
    }
    in.close();
    return out.toByteArray();
  }

  /**
   * 返回对象的属性值, 支持多级属性, 如:id.branch.id
   * 
   * @param obj
   * @param name
   * @return
   * @throws RuntimeException
   */
  public static Object getValue(Object obj, String name) throws RuntimeException {
    try {
      String[] names = name.split("\\.");
      Field f;
      for (int i = 0; i < names.length; i++) {
        f = obj.getClass().getDeclaredField(names[i]);
        f.setAccessible(true);
        obj = f.get(obj);
        if (obj == null)
          return null;
      }
      return obj;
    } catch (Throwable t) {
      throw new RuntimeException(t.getMessage(), t);
    }
  }

  /**
   * 设置对象的属性值, 支持多级属性, 如:id.branch.id
   * 
   * @param obj
   * @param name
   * @param value
   * @throws RuntimeException
   */
  public static void setValue(Object obj, String name, Object value) throws RuntimeException {
    try {
      String[] names = name.split("\\.");
      Field f;
      Object v;
      for (int i = 0; i < names.length; i++) {
        f = obj.getClass().getDeclaredField(names[i]);
        f.setAccessible(true);
        if (i < names.length - 1) {
          v = f.get(obj);
          if (v == null) {
            v = f.getType().newInstance();
            f.set(obj, v);
          }
          obj = v;
        } else
          f.set(obj, value);
      }
    } catch (Throwable t) {
      throw new RuntimeException(t.getMessage(), t);
    }
  }

  public static boolean isBlank(String text) {
    return (text == null) || text.trim().length() <= 0;
  }

  public static String getDataSourceInfo(DataSource ds) throws SQLException {
    return getDataSourceInfo(ds, null);
  }

  public static String getDataSourceInfo(DataSource ds, String name) throws SQLException {
    Connection conn = null;
    StringBuilder sb = new StringBuilder();
    try {
      conn = ds.getConnection();
      DatabaseMetaData dm = conn.getMetaData();
      sb.append("DataSource[");
      if (name != null)
        sb.append("name: ").append(name).append("\n\t");
      sb.append("class: ").append(ds.getClass().getName()).append("\n\tjdbc: ").append(dm.getJDBCMajorVersion())
          .append(".").append(dm.getJDBCMinorVersion()).append("\n\tdriver: ").append(dm.getDriverName())
          .append(" ")
          .append(dm.getDriverVersion())
          // .append(" ").append(dm.getDriverMajorVersion()).append(".").append(dm.getDriverMinorVersion())
          .append("\n\tdatabase: ").append(dm.getDatabaseProductName()).append(" ")
          .append(dm.getDatabaseProductVersion().replace('\n', ' '))
          // .append(" ").append(dm.getDatabaseMajorVersion()).append(".").append(dm.getDatabaseMinorVersion())
          .append("\n\turl: ").append(dm.getURL()).append("\n\tusername: ").append(dm.getUserName())
          .append("\n\tschema: ").append(conn.getCatalog()).append("]");
    } catch (SQLException e) {
      throw e;
    } finally {
      if (conn != null)
        try {
          conn.close();
        } catch (Exception e2) {
        }
    }
    return sb.toString();
  }

  public static String bytes2hex(byte[] data) {
    if (data == null || data.length <= 0)
      return "";
    StringBuilder sb = new StringBuilder(data.length * 2);
    for (byte b : data) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  // 5749e281-4423-492c-b07f-33e262d9b131
  public static byte[] hex2bytes(String hex) {
    if (hex == null || hex.length() == 0) return DUMMY_BYTES;
    return hex2bytes(hex.toCharArray());
  }

  public static byte[] hex2bytes(char[] data) {
    byte[] r = new byte[data.length >> 1];
    int f = 0, i = 0;
    for (int j = 0; j < data.length; i++) {
      while (data[j] == '-' || data[j] == ' ') j++;
      if (isHex(data[j])) f = toDigit(data[j], j) << 4;
      j++;
      if (isHex(data[j])) f = f | toDigit(data[j], j);
      j++;
      r[i] = (byte) (f & 0xFF);
    }
    byte[] b = new byte[i];
    System.arraycopy(r, 0, b, 0, i);
    return b;
  }

  public static boolean isHex(char c) {
    return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
  }

  protected static int toDigit(char ch, int index) throws IllegalArgumentException {
    int digit = Character.digit(ch, 16);
    if (digit == -1) {
      throw new IllegalArgumentException("Illegal hexadecimal charcter " + ch + " at index " + index);
    }
    return digit;
  }
  
  public static String getURL(HttpServletRequest request) {
    String scheme = request.getScheme();
    String name = request.getLocalName();
    int port = request.getLocalPort();
    String p = (("http".equals(scheme) && port == 80) || "https".equals(scheme) && port == 443) ? "" : ":" + port;
    String cp = request.getContextPath();
    if (!cp.startsWith("/"))
      cp = "/" + cp;
    StringBuilder sb = new StringBuilder().append(scheme).append("://").append(name).append(p).append(cp);
    return sb.toString();
  }

  public static byte[] int2bytes(int i) {
    byte[] b = new byte[4];
    b[0] = (byte)((i & 0xFF000000) >> 24);
    b[1] = (byte)((i & 0x00FF0000) >> 16);
    b[2] = (byte)((i & 0x0000FF00) >> 8);
    b[3] = (byte)(i & 0x0000FF);
    return b;
  }

  public static void dump(Map<String, Object> data) {
    StringBuilder sb = new StringBuilder();
    String key;
    Object value;
    sb.append("{");
    for (Iterator<String> it = data.keySet().iterator(); it.hasNext(); ) {
      key = it.next();
      value = data.get(key);
      sb.append(key).append("=").append(value).append(",");
    }
    sb.delete(sb.length()-1, sb.length());
    sb.append("}");
    System.out.println(sb.toString());
  }
}
