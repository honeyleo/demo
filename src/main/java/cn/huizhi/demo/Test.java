package cn.huizhi.demo;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Test {

	public static void main(String[] args) {
		ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2, new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable arg0) {
				Thread thread = new Thread(arg0);
				return thread;
			}
		});
		scheduled.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(new Date().toString() + "5秒任务");
				
			}
		}, 5, 5, TimeUnit.SECONDS);
		scheduled.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(new Date().toString() + "scheduleWithFixedDelay 5秒任务");
				
			}
		}, 5, 5, TimeUnit.SECONDS);
		System.out.println(System.currentTimeMillis());
	}
}
