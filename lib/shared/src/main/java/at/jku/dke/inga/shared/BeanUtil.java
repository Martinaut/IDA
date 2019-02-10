package at.jku.dke.inga.shared;

import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.stereotype.Service;

/**
 * A helper class to instantiate Spring Interfaces, Beans from classes not managed by spring.
 */
@Service
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext ctx;

    /**
     * Set the ApplicationContext that this object runs in.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    /**
     * Instantiates a new bean.
     *
     * @param beanClass The class of the requested bean.
     * @param <T>       The type of the requested bean.
     * @return Instantiated bean
     */
    public static <T> T getBean(Class<T> beanClass) {
        return ctx.getBean(beanClass);
    }
}
