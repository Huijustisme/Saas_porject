package com.itheima.web.utils;

import com.itheima.domain.system.SysLog;
import com.itheima.domain.system.User;
import com.itheima.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

/**
 * 日志切面类
 *   切面=切入点+通知
 */
@Component // 创建对象
@Aspect // 把该类声明为切面类： <aop:aspect/>
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    /**
     * 声明切入点
     *    把Controller的所有方法进入切入
     */
    @Pointcut("execution(* com.itheima.web.controller.*.*.*(..))")
    public void pointcut(){
    }

    /**
     * 定义环绕通知处理日志业务
     *  返回值： 目标方法执行后的返回值，必须要返回
     *  参数：ProceedingJoinPoint 连接点
     *        ProceedingJoinPoint作用：
     *             1）调用目标对象的方法
     *             2）获取目标对象的信息（方法名，目标对象，方法参数等）
     */
    @Around("pointcut()")
    public Object writeLog(ProceedingJoinPoint joinPoint){

        try{
            //调用目标对象的方法
            Object result = joinPoint.proceed(joinPoint.getArgs());

            //记录日志
            SysLog sysLog = new SysLog();
            sysLog.setId(UUID.randomUUID().toString());
            //时间
            sysLog.setTime(new Date());
            //获取当前登录用户
            User user = (User)session.getAttribute("loginUser");
            if(user!=null){
                sysLog.setUserName(user.getUserName());
                sysLog.setCompanyId(user.getCompanyId());
                sysLog.setCompanyName(user.getCompanyName());
            }
            //IP : getRemoteAddr(): 获取远程客户端的IP地址
            sysLog.setIp(request.getRemoteAddr());
            //Action：执行类
            String className = joinPoint.getTarget().getClass().getName();
            sysLog.setAction(className);
            //method: 执行方法
            String methodName = joinPoint.getSignature().getName();
            sysLog.setMethod(methodName);

            sysLogService.save(sysLog);

            return result;
        }catch (Throwable e){
            e.printStackTrace();
            //必须把异常抛出给统一异常处理类去处理
            throw new RuntimeException(e);
        }finally {

        }
    }

}