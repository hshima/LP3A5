package jantarDosFilosofos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

	public static void main(String[] args) {
		List<Lock> list = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			list.add(new ReentrantLock());
		}
		List<Thread> tList = List.of(new Filosofo(list.get(0), list.get(4)), new Filosofo(list.get(1), list.get(0)),
				new Filosofo(list.get(2), list.get(1)), new Filosofo(list.get(3), list.get(2)),
				new Filosofo(list.get(4), list.get(3)));
		tList.forEach(Thread::start);
	}
}

class Filosofo extends Thread {

	private Lock lock;
	private Lock fLock;

	public Filosofo(Lock lock, Lock fLock) {
		this.lock = lock;
		this.fLock = fLock;
	}

	public void run() {
		System.out.println("ThreadName: " + Thread.currentThread().getName());
		while (true) {
			// Ao utilizar retentativas temporizadas que liberam um lock, é possível realizar muitas das operações, mas mesmo assim, podem ocorrer muitas falhas
			try {
				if(lock.tryLock(50, TimeUnit.MILLISECONDS))
					System.out.println("Lock: " + lock.toString());
				else{
					System.out.println("lock sem sucesso, lock liberado: " +  lock.toString());
					continue;
				}
				if(fLock.tryLock(50, TimeUnit.MILLISECONDS))
					System.out.println("fLock com sucesso: " + fLock.toString());
				else {
					lock.unlock();
					System.out.println("fLock sem sucesso, lock liberado: " +  lock.toString());
					continue;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lock.unlock();
			fLock.unlock();
		}
	}
}