package jantarDosFilosofos;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<Object> list = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			list.add(new Object());
		}
		List<Thread> tList = List.of(
				new Filosofo(list.get(0), list.get(4)),
				new Filosofo(list.get(1), list.get(0)),
				new Filosofo(list.get(2), list.get(1)),
				new Filosofo(list.get(3), list.get(2)),
				new Filosofo(list.get(4), list.get(3))
				);
		tList.forEach(Thread::start);
	}
}

class Filosofo extends Thread {

	private Object lock;
	private Object fLock;

	public Filosofo(Object lock, Object fLock) {
		this.lock = lock;
		this.fLock = fLock;
	}

	public Object getLock() {
		return lock;
	}

	public void run() {
		System.out.println("ThreadName: " + Thread.currentThread().getName());
		while(true) {
			
//			synchronized (lock) {
				System.out.println("Lock: " + lock.toString());
				try {
					Thread.sleep(1l);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Ao travar um recurso, torna-se impossível que outra instância acesse o recurso, causando aguardo para sempre da liberação de um recurso
				synchronized (fLock) {
					System.out.println("tLock: " + fLock.toString());
				}
			}
//		}
	}
}