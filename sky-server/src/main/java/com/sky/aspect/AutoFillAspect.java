package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面：实现公共字段自动填充处理逻辑
 */
@Aspect//(标记这是一个aop切面类)
@Component//让这个类注册为spring 的bean(让spring管理它的创建注入)，aop需要spring的实例化才能够生效
@Slf4j
public class AutoFillAspect {
/**
 * 切入点
 */
    @Pointcut("execution(* com.sky.mapper.*.*(..))&&@annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
        public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段的填充...");
        //获取当前被拦截的方法上的数据库操作类型
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value();//获得数据库操作类型

        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length ==0){
            return ;
        }
        Object entity = args[0];


        //准备赋值数据（时间和id）
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据不同的操作类型，为对应的属性通过反射来进行赋值
        if(operationType == OperationType.INSERT){
            //为4个公共字段进行赋值
            //通过反射来进行赋值
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                //通过反射为对象属性赋值
                setCreateTime.invoke(entity,now);
                setUpdateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateUser.invoke(entity,currentId);

            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if (operationType == OperationType.UPDATE){
            //为两个公共字段来进行赋值
            try {
                  Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                //通过反射为对象属性赋值

                setUpdateTime.invoke(entity,now);

                setUpdateUser.invoke(entity,currentId);

            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        }

    }

