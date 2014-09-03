package com.free.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.ResourceUtils;

import com.free.Application;

public class FileUtil {

  public static final String[] SIZE_SYMBOLS = { "B", "KB", "MB", "GB", "TB" };
  public static final Map<String, String> MIME_TYPES = new HashMap<String, String>();

  static {
    MIME_TYPES.put("jpg", "image/jpeg");
    MIME_TYPES.put("jpeg", "image/jpeg");
    MIME_TYPES.put("png", "image/png");
    MIME_TYPES.put("gif", "image/gif");
    MIME_TYPES.put("bmp", "image/bmp");
    MIME_TYPES.put("tiff", "image/tiff");
  }

  /**
   * 返回文件名, 注意不能使用File.getName()方法, 上传的文件名在本地可能没有对应设备 X:\p1\p2\p3\ff.ext =>
   * ff.ext
   * 
   * @param name
   * @return
   */
  public static String getName(String name) {
    if (name == null)
      return null;
    int i = name.lastIndexOf('\\');
    if (i < 0)
      i = name.lastIndexOf('/');
    return i < 0 ? name : name.substring(i + 1);
  }

  // X:\p1\p2\p3\ff.ext => ff
  public static String getBody(String name) {
    String s = getName(name);
    int i = s.lastIndexOf('.');
    return i < 0 ? s : s.substring(0, i);
  }

  // X:\p1\p2\p3\ff.ext => ext
  public static String getExt(String name) {
    String s = getName(name);
    int i = s.lastIndexOf('.');
    return i < 0 ? "" : s.substring(i + 1);
  }

  public static String getSize(int size) {
    return getSize(size, SIZE_SYMBOLS);
  }

  public static String getSize(int size, String[] symbols) {
    if (symbols == null || symbols.length == 0)
      symbols = SIZE_SYMBOLS;
    int i = 0;
    double d = size;
    while (d > 1024 && i < symbols.length - 1) {
      d /= 1024;
      i++;
    }
    return String.format("%10.2f" + symbols[i], d);
  }

  public static boolean canRead(String name) {
    boolean r = true;
    try {
      File f = ResourceUtils.getFile(name);
      r = f.canRead();
    } catch (IOException e1) {
      r = false;
    }
    return r;
  }

  protected static File createTempFile(String prefix, String suffix, File dir) throws IOException {
    if (prefix.length() < 3)
      prefix += "___";
    if ("".equals(suffix) || suffix == null)
      suffix = ".tmp";
    if (!suffix.startsWith("."))
      suffix = "." + suffix;
    return File.createTempFile(prefix, suffix, dir);
  }

  public static void save(InputStream is, File f) throws IOException {
    byte[] buf = new byte[8192];
    File d = f.getParentFile();
    if (!d.exists())
      d.mkdirs();
    if (!f.canWrite())
      f.setWritable(true);
    OutputStream os = new FileOutputStream(f);
    int i;
    while ((i = is.read(buf)) > 0) {
      os.write(buf, 0, i);
    }
    os.flush();
    os.close();
    os = null;
  }

  public static String md5(String name) throws IOException, NoSuchAlgorithmException {
    return md5(new File(name));
  }

  public static String md5(File file) throws IOException, NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    FileInputStream in = new FileInputStream(file);
    FileChannel ch = in.getChannel();
    MappedByteBuffer buf = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
    md.update(buf);
    ch.close();
    in.close();
    return CommonUtil.bytes2hex(md.digest());
  }

  public static String md5(byte[] data) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(data);
    return CommonUtil.bytes2hex(md.digest());
  }

  public static String read(String name) throws IOException {
    return read(name, "UTF-8");
  }

  public static String read(String name, String charset) throws IOException {
    char[] cbuf = new char[1024];
    int size;
    InputStream is = ClassLoader.getSystemResourceAsStream(name);
    Reader r = charset == null ? new InputStreamReader(is) : new InputStreamReader(is, charset);
    StringBuilder sb = new StringBuilder();
    while ((size = r.read(cbuf)) > 0)
      sb.append(cbuf, 0, size);
    is.close();
    is = null;
    r.close();
    r = null;
    return sb.toString();
  }

  protected static String path(Integer id) {
    long l = CryptUtil.DJBHash(String.valueOf(id)) % 701819;
    String r = String.format("%06d", l);
    StringBuilder sb = new StringBuilder(10);
    for (int i = 0; i < r.length(); i += 2)
      sb.append(r.substring(i, i + 2)).append(java.io.File.separatorChar);
    return sb.toString();
  }

  public static void download(String fileName, HttpServletResponse response) throws IOException {
    download(fileName, response, "application/octet-stream");
  }

  public static void download(String fileName, HttpServletResponse response, String contentType) throws IOException {
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Content-Disposition", "attachment; filename=" + URLEncoder.encode(getName(fileName), "UTF8"));
    headers.put("Content-Type", contentType);
    download(fileName, response, headers);
  }

  public static void inline(String fileName, HttpServletResponse response) throws IOException {
    inline(fileName, response, "application/octet-stream");
  }

  public static void inline(String fileName, HttpServletResponse response, String contentType) throws IOException {
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Content-Disposition", "inline; filename=" + URLEncoder.encode(getName(fileName), "UTF8"));
    headers.put("Content-Type", contentType);
    download(fileName, response, headers);
  }

  public static void image(String fileName, HttpServletResponse response) throws IOException {
    image(fileName, response, null);
  }

  public static void image(String fileName, HttpServletResponse response, String mimeType) throws IOException {
    Map<String, String> headers = new HashMap<String, String>();
    if (mimeType == null) {
      String ext = getExt(fileName).toLowerCase();
      if (MIME_TYPES.containsKey(ext))
        headers.put("Content-Type", MIME_TYPES.get(ext));
    }
    download(fileName, response, headers);
  }

  protected static void download(String fileName, HttpServletResponse response, Map<String, String> headers)
      throws IOException {
    java.io.File f = new java.io.File(fileName);
    InputStream is = new FileInputStream(f);
    byte[] b = new byte[10240];
    int len;
    response.setCharacterEncoding("utf-8");
    if (headers.containsKey("Content-Length"))
      headers.put("Content-Length", String.valueOf(f.length()));
    for (String n : headers.keySet())
      response.addHeader(n, headers.get(n));
    OutputStream os = response.getOutputStream();
    while ((len = is.read(b)) > 0) {
      os.write(b, 0, len);
    }
    is.close();
    os.flush();
    os.close();
  }

  public static void upload(HttpServletRequest request, Visitor visitor) throws Exception {
    cfuUpload(request, visitor);
  }

  protected static void cfuUpload(HttpServletRequest request, Visitor visitor) throws Exception {
    // request.getHeader("Content-Type").startWith("multipart/form-data;")
    if (ServletFileUpload.isMultipartContent(request))
      cfuUploadForm(request, visitor);
    else
      cfuUploadXhr(request, visitor);
  }

  // AjaxUpload其他参数使用form field传递, qqFileUploader使用query string方式!!!
  @SuppressWarnings("unchecked")
  protected static void cfuUploadForm(HttpServletRequest request, Visitor visitor) throws Exception {
    File file;
    Integer id;
    String name, path;
    DiskFileItemFactory ff = new DiskFileItemFactory();
    ff.setSizeThreshold(Application.THRESHOLD_SIZE);
    ServletFileUpload upload = new ServletFileUpload(ff);
    upload.setSizeMax(Application.MAX_UPLOAD_SIZE);
    Map<String, Object> params = new HashMap<String, Object>();
    Enumeration<String> ns = request.getParameterNames();
    while (ns.hasMoreElements()) {
      name = ns.nextElement();
      if (request.getParameter(name) != null)
        params.put(name, request.getParameter(name));
      else if (request.getParameterValues(name) != null)
        params.put(name, request.getParameterValues(name));
    }
    List<FileItem> items = upload.parseRequest(request);
    Iterator<FileItem> iter = items.iterator();
    FileItem item;
    while (iter.hasNext()) {
      item = iter.next();
      if (!item.isFormField()) {
        name = getName(item.getName()); // C:\...\...\abc.ext -> abc.ext
        id = visitor.id(name);
        path = Application.UPLOAD_PATH + path(id);
        file = new File(path, name);
        if (file.exists())
          file = createTempFile(getBody(name), getExt(name), new File(path));
        save(item.getInputStream(), file);
        visitor.visit(id, file, item.getContentType(), params);
      } else {
        params.put(item.getFieldName(), item.getString());
      }
    }
  }

  @SuppressWarnings("unchecked")
  protected static void cfuUploadXhr(HttpServletRequest request, Visitor visitor) throws Exception {
    String name = request.getParameter("qqfile");
    if (name == null)
      throw new Exception("ajax upload error!");
    Integer id = visitor.id(name);
    String path = Application.UPLOAD_PATH + path(id);
    File file = new File(path, name);
    if (file.exists())
      file = createTempFile(getBody(name), getExt(name), new File(path));
    save(request.getInputStream(), file);
    visitor.visit(id, file, null, request.getParameterMap());
  }

  public static interface Visitor {

    public Integer id(String name);

    public void visit(Integer id, File file, String contentType, Map<String, Object> params) throws Exception;

  }

}
