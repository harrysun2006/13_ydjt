package com.free.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import com.free.util.JpaUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JpaDao extends JpaDaoSupport {

  /*
   * 通过Spring对bean注入entityManagerFactory即可 public JpaDao() { }
   * 
   * public JpaDao(EntityManagerFactory emf) { setEntityManagerFactory(emf); }
   * 
   * public JpaDao(EntityManagerFactoryInfo emfi) {
   * setEntityManagerFactory(emfi.getNativeEntityManagerFactory()); }
   */

  private static final Object[] NULL_OBJECTS = (Object[]) null;

  public void flush() {
    getJpaTemplate().flush();
  }

  protected Integer nextId(String seq) {
    if (seq == null)
      return null;
    Number result = (Number) searchSingle("SELECT " + seq + ".NEXTVAL FROM DUAL");
    return result.intValue();
  }

  public Integer nextId(Class<?> clazz) {
    return nextId(JpaUtil.getSequnceName(clazz));
  }

  public <T> T detach(final T entity) {
    return getJpaTemplate().execute(new JpaCallback<T>() {

      @Override
      public T doInJpa(EntityManager em) throws PersistenceException {
        em.detach(entity);
        return entity;
      }
    });
  }

  /**
   * 使用hql查询(em.createQuery), 返回单个值如COUNT(*), MAX(..), ...
   * 
   * @param clazz
   * @param hql
   * @return
   */
  public <T> T findSingle(Class<T> clazz, String hql) {
    return findSingle(clazz, hql, (Object[]) null);
  }

  public <T> T findSingle(Class<T> clazz, String hql, Object param) {
    return findSingle(clazz, hql, new Object[] { param });
  }

  public <T> T findSingle(final Class<T> clazz, final String hql, final Object... params) {
    return (T) getJpaTemplate().execute(new JpaCallback() {
      @Override
      public Object doInJpa(EntityManager em) throws DataAccessException {
        Query q = em.createQuery(hql);
        if (params != null) {
          for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
          }
        }
        return q.getSingleResult();
      }
    });
  }

  public <T> T findSingle(final Class<T> clazz, final String hql, final Map<String, Object> params)
      throws DataAccessException {
    return (T) getJpaTemplate().execute(new JpaCallback() {
      public Object doInJpa(EntityManager em) throws PersistenceException {
        Query q = em.createQuery(hql);
        if (params != null) {
          for (Object element : params.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) element;
            if (entry.getKey().startsWith("#")) continue;
            q.setParameter(entry.getKey(), entry.getValue());
          }
        }
        return q.getSingleResult();
      }
    });
  }

  /**
   * 使用hql查询(em.createQuery), 返回对象列表
   * 
   * @param clazz
   * @param hql
   * @return
   */
  public <T> List<T> find(Class<T> clazz) {
    return find(clazz, "select e from " + clazz.getName() + " e", -1, -1, NULL_OBJECTS);
  }

  public <T> List<T> find(Class<T> clazz, int skipResults, int maxResults) {
    return find(clazz, "select e from " + clazz.getName() + " e", skipResults, maxResults, NULL_OBJECTS);
  }

  public <T> List<T> find(Class<T> clazz, String hql) {
    return find(clazz, hql, -1, -1, NULL_OBJECTS);
  }

  public <T> List<T> find(Class<T> clazz, String hql, Object param) throws DataAccessException {
    return find(clazz, hql, -1, -1, new Object[] { param });
  }

  public <T> List<T> find(Class<T> clazz, String hql, Object... params) {
    return find(clazz, hql, -1, -1, params);
  }

  public <T> List<T> find(Class<T> clazz, String hql, int skipResults, int maxResults) throws DataAccessException {
    return find(clazz, hql, skipResults, maxResults, NULL_OBJECTS);
  }

  public <T> List<T> find(Class<T> clazz, String hql, int skipResults, int maxResults, Object param)
      throws DataAccessException {
    return find(clazz, hql, skipResults, maxResults, new Object[] { param });
  }

  public <T> List<T> find(final Class<T> clazz, final String hql, final int skipResults, final int maxResults,
      final Object... params) throws DataAccessException {
    return getJpaTemplate().executeFind(new JpaCallback() {
      @Override
      public Object doInJpa(EntityManager em) throws PersistenceException {
        Query q = em.createQuery(hql);
        if (skipResults >= 0)
          q.setFirstResult(skipResults);
        if (maxResults >= 0)
          q.setMaxResults(maxResults);
        if (params != null) {
          for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
          }
        }
        return q.getResultList();
      }
    });
  }

  public <T> List<T> find(Class<T> clazz, String hql, Map<String, Object> params) throws DataAccessException {
    return find(clazz, hql, -1, -1, params);
  }

  public <T> List<T> find(final Class<T> clazz, final String hql, final int skipResults, final int maxResults,
      final Map<String, Object> params) throws DataAccessException {
    return getJpaTemplate().executeFind(new JpaCallback() {
      public Object doInJpa(EntityManager em) throws PersistenceException {
        Query q = em.createQuery(hql);
        if (skipResults >= 0)
          q.setFirstResult(skipResults);
        if (maxResults >= 0)
          q.setMaxResults(maxResults);
        if (params != null) {
          for (Object element : params.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) element;
            if (entry.getKey().startsWith("#")) continue;
            q.setParameter(entry.getKey(), entry.getValue());
          }
        }
        return q.getResultList();
      }
    });
  }

  /**
   * 使用queryName查询(em.createNamedQuery)
   * 
   * @param name
   * @return
   */
  public Object querySingle(final String name) {
    return getJpaTemplate().execute(new JpaCallback() {
      public Object doInJpa(EntityManager em) throws PersistenceException {
        Query q = em.createNamedQuery(name);
        return q.getSingleResult();
      }
    });
  }

  public <T> List<T> query(Class<T> clazz, String name) {
    return query(clazz, name, -1, -1);
  }

  public <T> List<T> query(Class<T> clazz, String name, int skipResults, int maxResults) {
    return query(clazz, name, skipResults, maxResults, (Object[]) null);
  }

  public <T> List<T> query(Class<T> clazz, String name, Map<String, ? extends Object> params) {
    return query(clazz, name, -1, -1, params);
  }

  public <T> List<T> query(final Class<T> clazz, final String name, final int skipResults, final int maxResults,
      final Object... params) {
    return getJpaTemplate().executeFind(new JpaCallback() {
      public Object doInJpa(EntityManager em) throws PersistenceException {
        Query q = em.createNamedQuery(name);
        if (skipResults >= 0)
          q.setFirstResult(skipResults);
        if (maxResults >= 0)
          q.setMaxResults(maxResults);
        if (params != null) {
          for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
          }
        }
        return q.getResultList();
      }
    });
  }

  public <T> List<T> query(final Class<T> clazz, final String name, final int skipResults, final int maxResults,
      final Map<String, ? extends Object> params) {
    return getJpaTemplate().executeFind(new JpaCallback() {
      public Object doInJpa(EntityManager em) throws PersistenceException {
        Query q = em.createNamedQuery(name);
        if (skipResults >= 0)
          q.setFirstResult(skipResults);
        if (maxResults >= 0)
          q.setMaxResults(maxResults);
        if (params != null) {
          for (Object element : params.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) element;
            if (entry.getKey().startsWith("#")) continue;
            q.setParameter(entry.getKey(), entry.getValue());
          }
        }
        return q.getResultList();
      }
    });
  }

  /**
   * 使用native SQL查询(em.createNativeQuery)
   * 
   * @param sql
   * @return
   */
  public Object searchSingle(final String sql) {
    return getJpaTemplate().execute(new JpaCallback() {
      public Object doInJpa(EntityManager em) throws PersistenceException {
        Query q = em.createNativeQuery(sql);
        return q.getSingleResult();
      }
    });
  }

  public <T> List<T> search(Class<T> clazz, String sql) {
    return search(clazz, sql, -1, -1, NULL_OBJECTS);
  }

  public <T> List<T> search(Class<T> clazz, String sql, int skipResults, int maxResults) {
    return search(clazz, sql, skipResults, maxResults, NULL_OBJECTS);
  }

  public <T> List<T> search(final Class<T> clazz, final String sql, final int skipResults, final int maxResults,
      final Object... params) {
    return getJpaTemplate().executeFind(new JpaCallback() {
      public Object doInJpa(EntityManager em) throws PersistenceException {
        Query q = em.createNativeQuery(sql);
        if (skipResults >= 0)
          q.setFirstResult(skipResults);
        if (maxResults >= 0)
          q.setMaxResults(maxResults);
        if (params != null) {
          for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
          }
        }
        return q.getResultList();
      }
    });
  }

  public <T> List<T> search(Class<T> clazz, String sql, Map<String, ? extends Object> params) {
    return search(clazz, sql, -1, -1, params);
  }

  public <T> List<T> search(final Class<T> clazz, final String sql, final int skipResults, final int maxResults,
      final Map<String, ? extends Object> params) {
    return getJpaTemplate().executeFind(new JpaCallback() {
      public Object doInJpa(EntityManager em) throws PersistenceException {
        Query q = em.createNativeQuery(sql);
        if (skipResults >= 0)
          q.setFirstResult(skipResults);
        if (maxResults >= 0)
          q.setMaxResults(maxResults);
        if (params != null) {
          for (Object element : params.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) element;
            if (entry.getKey().startsWith("#")) continue;
            q.setParameter(entry.getKey(), entry.getValue());
          }
        }
        return q.getResultList();
      }
    });
  }

  /**
   * 查询单个对象
   * 
   * @param clazz
   * @param id
   * @return
   */
  public <T> T get(Class<T> clazz, Object id) {
    return getJpaTemplate().find(clazz, id);
    // 即使对象不存在也会返回Javassist包装对象(id == null), 使用时可能抛错
    // return getJpaTemplate().getReference(clazz, id);
  }

  public Object execute(final String hql) {
    return execute(hql, NULL_OBJECTS);
  }

  public Object execute(final String hql, Object param) {
    return execute(hql, new Object[] { param });
  }

  public Object execute(final String hql, final Object... params) {
    return getJpaTemplate().execute(new JpaCallback() {

      @Override
      public Object doInJpa(EntityManager em) throws PersistenceException {
        Query q = em.createQuery(hql);
        if (params != null) {
          for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
          }
        }
        return q.executeUpdate();
      }
    });
  }

  public Object execute(final String hql, final Map<String, Object> params) {
    return getJpaTemplate().execute(new JpaCallback() {

      @Override
      public Object doInJpa(EntityManager em) throws PersistenceException {
        Query q = em.createQuery(hql);
        if (params != null) {
          for (Object element : params.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) element;
            if (entry.getKey().startsWith("#")) continue;
            q.setParameter(entry.getKey(), entry.getValue());
          }
        }
        return q.executeUpdate();
      }
    });
  }

  public void add(Object entity) {
    getJpaTemplate().persist(entity);
  }

  public void add(Object... entities) {
    add(java.util.Arrays.asList(entities));
  }

  public void add(List<Object> entities) {
    for (Object entity : entities) {
      add(entity);
    }
  }

  // 可以自适应insert/update
  public <T> T save(final T entity) {
    return getJpaTemplate().merge(entity);
  }

  public <T> List<T> save(T... entities) {
    return save(java.util.Arrays.asList(entities));
  }

  public <T> List<T> save(List<T> entities) {
    List<T> result = new java.util.ArrayList<T>(entities.size());
    for (T entity : entities) {
      result.add(save(entity));
    }
    return result;
  }

  public <T> void delete(T entity) {
    getJpaTemplate().remove(entity);
  }

  public void delete(Class clazz) {
    execute("delete from " + clazz.getName(), NULL_OBJECTS);
  }

  public <T> void delete(final Class<T> clazz, final Object id) {
    T entity = get(clazz, id);
    delete(entity);
  }

  protected Query setParameters(Query query, Map params) {
    for (Object key : params.keySet()) {
      if (key instanceof String) {
        query.setParameter((String) key, params.get(key));
      } else if (key instanceof Integer) {
        query.setParameter(((Integer) key).intValue(), params.get(key));
      }
    }
    return query;
  }

  protected Query setParameters(Query query, Object... params) {
    for (int i = 0; i < params.length; i++) {
      query.setParameter(i + 1, params[i]);
    }
    return query;
  }

  protected Query setParameters(Query query, Object param) {
    if (param == null) {
      return query;
    }
    if (param instanceof Map) {
      return setParameters(query, (Map) param);
    }
    if (param.getClass().isArray()) {
      return setParameters(query, (Object[]) param);
    }
    return setParameters(query, new Object[] { param });
  }

}
