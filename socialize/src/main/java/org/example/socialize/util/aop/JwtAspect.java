package org.example.socialize.util.aop;

import io.lettuce.core.dynamic.support.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.example.socialize.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class JwtAspect {
    @Autowired
    private HttpServletRequest request;

    //@Pointcut("execution(* org.example.basic.controller.*.*(..))")
    @Pointcut("@annotation(org.example.socialize.util.aop.auth)")
    public void pointCut(){

    }

//    @Before("pointCut()")
    @Around("pointCut()")
    public Object before(ProceedingJoinPoint joinPoint)throws Throwable{
        Signature signature = joinPoint.getSignature();
        //获取包名
        String declaringTypeName = signature.getDeclaringTypeName();
        String name=signature.getName();
        log.info("用户请求的包名：{},方法名{}",declaringTypeName,name);


        Method method = getMethod(joinPoint);




        MethodSignature methodSignature=(MethodSignature)joinPoint.getSignature();
        auth auth = methodSignature.getMethod().getAnnotation(auth.class);
        log.info(request.getParameter("token"));
        if (auth.token()){
            if (!JwtUtils.verifyTokenOfUser(request.getParameter("token"))){
                Class<?> returnType = method.getReturnType();
                Object failVo = createFailVo(returnType, "用户token权限不正确");
                return failVo;
            }
        }

        return joinPoint.proceed();
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
    private Object createFailVo(Class<?> returnType, String statusMsg) {
        try {
            Object failVo = returnType.getDeclaredConstructor().newInstance();

            Method setStatusMsg = ReflectionUtils.findMethod(returnType, "setStatusMsg", String.class);
            Method setStatusCode = ReflectionUtils.findMethod(returnType, "setStatusCode", long.class);
            if (setStatusMsg != null) {
                ReflectionUtils.invokeMethod(setStatusMsg, failVo, statusMsg);
                ReflectionUtils.invokeMethod(setStatusCode, failVo, 1);
            }
            return failVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

