package COMMON.SeventhPackage;

import java.util.LinkedList;

class WriteThread extends Thread{
	//同步队列类变量
	private SynchronizedQuery synQuery;
	//该构造函数初始化同步队列类变量
	public WriteThread (SynchronizedQuery synQuery){
		this.synQuery = synQuery;
		
	}
	//线程运行主体
	public void run(){
		//给同步队列添加字符串变量
		synQuery.addElement("新元素");
	}
}

class ReadThread extends Thread {
	//同步队列类变量
	private SynchronizedQuery synQuery;
	//初始化同步列
	public ReadThread(SynchronizedQuery synQuery){
		this.synQuery = synQuery;
	}
	public void run(){
		System.out.println((String)synQuery.popElement());
	}
}

class SynchronizedQuery {
	//存储元素的linkList
	private LinkedList linkedList = new LinkedList();
	//添加元素的同步方法
	public synchronized void addElement(Object obj){
		
		while(true){
			linkedList.add(obj);
			System.out.println("添加了一个元素");
			
			/*
			 * 1、wait()、notify/notifyAll() 方法是Object的本地final方法，无法被重写。
			 * 
			 * 2、wait()使当前线程阻塞，前提是 必须先获得锁，一般配合synchronized关键字使用，
			 * 即，一般在synchronized 同步代码块里使用 wait()、notify/notifyAll() 方法。 
			 * 
			 * 3、由于 wait()、notify/notifyAll() 在synchronized 代码块执行，说明当前线程一定是获取了锁的。
			 * 当线程执行wait()方法时候，会释放当前的锁，然后让出CPU，进入等待状态。 只有当 notify/notifyAll()
			 * 被执行时候，才会唤醒一个或多个正处于等待状态的线程，然后继续往下执行，直到执行完synchronized
			 * 代码块的代码或是中途遇到wait() ，再次释放锁。 也就是说，notify/notifyAll() 的执行只是唤醒沉
			 * 睡的线程，而不会立即释放锁，锁的释放要看代码块的具体执行情况。所以在编程中，尽量在使用了notify/
			 * notifyAll() 后立即退出临界区，以唤醒其他线程
			 *  
			 * 4、wait() 需要被trycatch包围，中断也可以使wait等待\的线程唤醒。\
			 * 
			 * 5、notify 和wait的顺序不能错，如果A线程先执行notify方法，B线程在执行wait方法，那么B线程是无法被唤醒的。 
			 * 
			 * 6、notify 和 notifyAll的区别notify方法只唤醒一个等待（对象的）线程并使该线程开始执行。所以如果有多个线程等待一个对象，
			 * 这个方法只会唤醒其中一个线程，选择哪个线程取决于操作系统对多线程管理的实现。notifyAll会唤醒所有等待(对象的)线程，
			 * 尽管哪一个线程将会第一个处理取决于操作系统的实现。如果当前情况下有多个线程需要被唤醒,推荐使用notifyAll
			 * 方法。比如在生产者-消费者里面的使用，每次都需要唤醒所有的消费者或是生产者，以判断程序是否可以继续往下执行。
			 * 
			 * 7、在多线程中要测试某个条件的变化，使用if 还是while?要注意，notify唤醒沉睡的线程后，线程会接着上次的执行继续往下执行。
			 * 所以在进行条件判断时候，可以先把 wait语句忽略不计来进行考虑，显然，要确保程序一定要执行，并且要保证程序直到满足一定的
			 * 条件再执行，要使用while来执行，以确保条件满足和一定执行。
			 */
			
			// 唤醒其他线程
			notify();
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//去除元素的同步方法
	public synchronized Object popElement(){
		for(int i = 0;i<20;i++){
			while(linkedList.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			linkedList.removeLast();
			System.out.println("读取并移走一个元素");
			notify();
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return linkedList.removeLast();
	}
			
}
