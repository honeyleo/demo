package cn.huizhi.guice;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class NotOnWeekendsModule extends AbstractModule {
	  protected void configure() {
		  WeekendBlocker weekendBlocker = new WeekendBlocker();
		    requestInjection(weekendBlocker);
		    bindInterceptor(Matchers.any(), Matchers.annotatedWith(NotOnWeekends.class), 
		       weekendBlocker);
	  }
	}
