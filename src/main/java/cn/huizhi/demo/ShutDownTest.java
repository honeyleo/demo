package cn.huizhi.demo;

public class ShutDownTest {  
	 
    public static void main(String[] args) {  
        //注册第一个钩子  
        Runtime.getRuntime().addShutdownHook(new Thread() {  
 
            public void run() {  
                try {  
                    Thread.currentThread().sleep(5000);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                System.out.println("clean task1 completed.");  
            }  
        });  
        //注册第二个钩子  
        Runtime.getRuntime().addShutdownHook(new Thread() {  
 
            public void run() {  
                try {  
                    Thread.currentThread().sleep(10000);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                System.out.println("clean task2 completed");  
            }  
        });  
        //启动子线程  
        new Thread() {  
 
            public void run() {  
                while (true) {  
                    try {  
                        Thread.currentThread().sleep(1000);  
                        System.out.println("sub thread is running");  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }.start();  
        //程序退出  
//        System.exit(0);  
    }  
 
} 