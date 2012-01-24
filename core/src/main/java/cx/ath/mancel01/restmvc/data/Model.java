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

import java.io.Serializable;
import javax.persistence.*;

/**
 * Helper for entity with Long PK and some instance methods.
 * 
 * @author Mathieu ANCELIN
 */
@MappedSuperclass
public abstract class Model<T> implements Serializable {
    
    @Transient
    private transient DataHelper helper;

    @Transient
    private transient Class clazz;
    
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    public Model() {
        if (helper == null) {
            clazz = getClass();
            helper = DataHelper.forType(clazz, Long.class);
        }
    }

    public T delete(final EntityManager em) {
        return (T) helper.delete(em, this);
    }

    public T refresh(final EntityManager em) {
        return (T) helper.refresh(em, this);
    }

    public T save(final EntityManager em) {
        return (T) helper.save(em, this);
    }
}
