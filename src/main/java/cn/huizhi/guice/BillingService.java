package cn.huizhi.guice;

public interface BillingService {

	Receipt chargeOrder(PizzaOrder order, CreditCard creditCard);
}
