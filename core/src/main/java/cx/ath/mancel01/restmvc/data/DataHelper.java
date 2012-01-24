/*
 *  Copyright 2012 Mathieu ANCELIN.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package cx.ath.mancel01.restmvc.data;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * JPA helper with basic JPA requests.
 * 
 * @author Mathieu ANCELIN
 */
public class DataHelper<T, K> {

    private final Class<T> clazz;
    private final Class<K> key;
    private final String name;

    private DataHelper(Class<T> clazz, Class<K> key) {
        this.clazz = clazz;
        this.key = key;
        this.name = clazz.getSimpleName();
    }

    public static <T, K> DataHelper<T, K> forType(Class<T> clazz, Class<K> key) {
        return new DataHelper<T, K>(clazz, key);
    }

    public List<T> all(final EntityManager em) {
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(clazz);
        query.from(clazz);
        return em.createQuery(query).getResultList();
    }

    public long count(final EntityManager em) {
        return Long.parseLong(em.createQuery("select count(e) from " + name + " e").getSingleResult().toString());
    }

    public int deleteAll(final EntityManager em) {
        return em.createQuery("delete from " + name).executeUpdate();
    }

    public T delete(final EntityManager em, T o) {
        em.remove(o);
        return (T) o;
    }

    public T findById(final EntityManager em, K primaryKey) {
        return em.find(clazz, primaryKey);
    }

    public Query execDo(final EntityManager em, String criteria, Object... args) {
        final String queryName = name + "." + criteria;
        final Query query = em.createNamedQuery(queryName);
        for (int i = 0; i < args.length; i++) {
            query.setParameter(i + 1, args[i]);
        }
        return query;
    }

    public T refresh(final EntityManager em, Object o) {
        em.refresh(o);
        return (T) o;
    }

    public T save(final EntityManager em, T o) {
        if (em.contains(o)) {
            return em.merge(o);
        }
        em.persist(o);
        return (T) o;
    }

    public T deleteById(final EntityManager em, final K id) {
        T object = findById(em, id);
        delete(em, object);
        return object;
    }

    public <T> List<T> findByNamedQuery(final EntityManager em, final String namedQueryName) {
        return em.createNamedQuery(namedQueryName).getResultList();
    }

    public <T> List<T> findByNamedQuery(final EntityManager em, final String namedQueryName, final Object... params) {
        Query query = em.createNamedQuery(namedQueryName);
        int i = 1;
        for (Object p : params) {
            query.setParameter(i++, p);
        }
        return query.getResultList();
    }

    public <T> T findUniqueByNamedQuery(final EntityManager em, final String namedQueryName)  {
        return (T) em.createNamedQuery(namedQueryName).getSingleResult();
    }

    public <T> T findUniqueByNamedQuery(final EntityManager em, final String namedQueryName, final Object... params) {
        Query query = em.createNamedQuery(namedQueryName);
        int i = 1;
        for (Object p : params) {
            query.setParameter(i++, p);
        }
        return (T) query.getSingleResult();
    }
}
