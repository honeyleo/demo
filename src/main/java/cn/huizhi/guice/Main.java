package cn.huizhi.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

	public static void main(String[] args) {
	    Injector injector = Guice.createInjector(new BillingModule(), new NotOnWeekendsModule());
	    BillingService billingService = injector.getInstance(BillingService.class);
	    billingService.chargeOrder(new PizzaOrder(), new CreditCard());
	}
}
