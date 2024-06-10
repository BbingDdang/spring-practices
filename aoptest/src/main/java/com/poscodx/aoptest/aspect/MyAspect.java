package com.poscodx.aoptest.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MyAspect {
	
	@Before("execution(public com.poscodx.aoptest.vo.ProductVo com.poscodx.aoptest.service.ProductService.find(String)) ")
	public void adviceBefore() {
		System.out.println("--- Before Advice ---");
	}
	
	@After("execution(com.poscodx.aoptest.vo.ProductVo com.poscodx.aoptest.service.ProductService))")
	public void adviceAfter() {
		System.out.println("--- After Advice ---");
	}
	
	@AfterReturning("execution(* com.poscodx.aoptest.service.ProductService.find(..))")
	public void adviceAfterReturning() {
		System.out.println("--- AfterReturning Advice ---");
	}
	
	@AfterThrowing(value="execution(* *..*.ProductService.*(..))", throwing="ex")
	public void adviceAfterThrwoing(Throwable ex) {
		System.out.println("--- AfterThrowing Advice " + ex + "---");
	}
	
	@Around("execution(* *..*.ProductService.*(..))")
	public Object adviceAround(ProceedingJoinPoint pjp) throws Throwable{
		// before joinpoint
		System.out.println("--- Around(Before) ---");
		
		// point cut method execute
		Object[] params = {"Camera"};
		Object result = pjp.proceed(params);
		
		// after joinpoint
		System.out.println("--- Around(After) ---");
		
		return result;
	}
}
