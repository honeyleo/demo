package cn.huizhi.guice;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class WeekendBlocker implements MethodInterceptor {
	  public Object invoke(MethodInvocation invocation) throws Throwable {
	    Calendar today = new GregorianCalendar();
	    if (today.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH).startsWith("S")) {
	      throw new IllegalStateException(
	          invocation.getMethod().getName() + " not allowed on weekends!");
	    }
	    return invocation.proceed();
	  }
	}
