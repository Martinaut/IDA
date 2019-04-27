package at.jku.dke.ida.shared.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

/**
 * A helper class to instantiate Spring Beans from classes not managed by spring.
 */
@Service
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext ctx;

    /**
     * Instantiates a new instance of class {@linkplain BeanUtil}.
     */
    public BeanUtil() {
    }

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
     * @throws IllegalStateException If the application context is not set.
     */
    public static <T> T getBean(Class<T> beanClass) {
        if (ctx == null) throw new IllegalStateException("Application Context not available");
        return ctx.getBean(beanClass);
    }

    /**
     * Instantiates a new bean, if present.
     * If more than one beans of the specified type are found, the first one will be returned.
     *
     * @param beanClass The class of the requested bean.
     * @param <T>       The type of the requested bean.
     * @return Instantiated bean or {@code null}
     * @throws IllegalStateException If the application context is not set.
     */
    public static <T> T getOptionalBean(Class<T> beanClass) {
        if (ctx == null) throw new IllegalStateException("Application Context not available");
        var beans = ctx.getBeansOfType(beanClass);
        if (beans.isEmpty())
            return null;

        return beans.values().iterator().next();
    }
}
