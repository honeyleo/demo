package cn.huizhi.guice;

import javax.inject.Inject;

public class RealBillingService implements BillingService {

	private final CreditCardProcessor processor;
	private final TransactionLog transactionLog;
	
	@Inject
	public RealBillingService(CreditCardProcessor processor, TransactionLog transactionLog) {
		this.processor = processor;
	    this.transactionLog = transactionLog;
	}
	@Override
	@NotOnWeekends
	public Receipt chargeOrder(PizzaOrder order, CreditCard creditCard) {
		System.out.println("CreditCardProcessor =" + processor);
		System.out.println("TransactionLog =" + transactionLog);
		System.out.println("Order =" + order);
		System.out.println("CreditCard =" + creditCard);
		return new Receipt();
	}

}
