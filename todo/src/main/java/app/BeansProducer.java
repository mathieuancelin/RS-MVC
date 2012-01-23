package app;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class BeansProducer {
    
    @PersistenceContext(unitName="applicationPU")
    @Produces
    private EntityManager em;
    
}

