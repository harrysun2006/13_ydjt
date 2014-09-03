package com.free.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JDBCUtil {

  private final static int[] EMPTY_INTS = {};
  private final static Object[] EMPTY_OBJECTS = {};

  private JDBCUtil() {
  }

  public static Object[] query(Connection conn, String sql) throws Exception {
    return query(conn, sql, null);
  }

  public static Object[] query(Connection conn, String sql, Object[] params) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<Object> data = new LinkedList<Object>();
    try {
      ps = conn.prepareStatement(sql);
      ResultSetMetaData rsmd;
      int i, k;
      Map<String, Object> cell;
      if (params == null)
        params = EMPTY_OBJECTS;
      for (i = 1; i <= params.length; i++)
        ps.setObject(i, params[i - 1]);
      rs = ps.executeQuery();
      rsmd = rs.getMetaData();
      while (rs.next()) {
        cell = new HashMap<String, Object>();
        for (k = 1; k <= rsmd.getColumnCount(); k++)
          cell.put(rsmd.getColumnName(k), rs.getObject(k));
        data.add(cell);
      }
    } catch (Exception e1) {
      throw e1;
    } finally {
      if (rs != null)
        try {
          rs.close();
        } catch (Exception e2) {
        }
      if (ps != null)
        try {
          ps.close();
        } catch (Exception e3) {
        }
    }
    return data.toArray(EMPTY_OBJECTS);
  }

  public static Object[] call(Connection conn, String sql) throws Exception {
    return call(conn, sql, null, null);
  }

  public static Object[] call(Connection conn, String sql, Object[] params, int[] types) throws Exception {
    CallableStatement cs = null;
    ResultSet rs = null;
    List<Object> data = new LinkedList<Object>();
    try {
      cs = conn.prepareCall(sql);
      ResultSetMetaData rsmd;
      int i, k;
      List<Object> row;
      Map<String, Object> cell;
      if (params == null)
        params = EMPTY_OBJECTS;
      for (i = 1; i <= params.length; i++)
        cs.setObject(i, params[i - 1]);
      if (types == null)
        types = EMPTY_INTS;
      for (i = 1; i <= types.length; i++)
        cs.registerOutParameter(params.length + i, types[i - 1]);
      cs.execute();
      for (i = 1; i <= types.length; i++) {
        if (types[i - 1] == Types.INTEGER || types[i - 1] == Types.VARCHAR) {
          data.add(cs.getObject(params.length + i));
        } else if (types[i - 1] == Types.OTHER) {
          row = new LinkedList<Object>();
          data.add(row);
          rs = (ResultSet) cs.getObject(params.length + i);
          rsmd = rs.getMetaData();
          while (rs.next()) {
            cell = new HashMap<String, Object>();
            for (k = 1; k <= rsmd.getColumnCount(); k++)
              cell.put(rsmd.getColumnName(k), rs.getObject(k));
            row.add(cell);
          }
        }
      }
    } catch (Exception e1) {
      throw e1;
    } finally {
      if (rs != null)
        try {
          rs.close();
        } catch (Exception e2) {
        }
      if (cs != null)
        try {
          cs.close();
        } catch (Exception e3) {
        }
    }
    return data.toArray(EMPTY_OBJECTS);
  }

}
