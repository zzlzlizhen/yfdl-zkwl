package com.remote;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanFactory implements ApplicationContextAware {

    private  ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    public <T> T getBean(Class<T> clazz){
        try{
            return applicationContext.getBean(clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getBeanById(String beanId){
        try{
            return (T)applicationContext.getBean(beanId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}