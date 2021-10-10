package jantarDosFilosofos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
	
	volatile static List<Lock> locksTaken = new ArrayList<>(); 

	public static void main(String[] args) {
		List<Lock> list = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			list.add(new ReentrantLock());
		}
		List.of(
				new Filosofo(list.get(0), list.get(4), new Main()), 
				new Filosofo(list.get(1), list.get(0), new Main()),
				new Filosofo(list.get(2), list.get(1), new Main()),
				new Filosofo(list.get(3), list.get(2), new Main()),
				new Filosofo(list.get(4), list.get(3), new Main())
				).forEach(Thread::start);
	}
}

class Filosofo extends Thread {

	private Lock lock;
	private Lock fLock;
	private Main main;

	public Filosofo(Lock lock, Lock fLock, Main main) {
		this.lock = lock;
		this.fLock = fLock;
		this.main = main;
	}

	public void run() {
		System.out.println("ThreadName: " + Thread.currentThread().getName());
		while (true) {
//			 Ao utilizar uma lista de controle de locks, é possível manter controle dos objetos que estão bloqueados. 
//			 Ainda assim, ocorrem travas pontuais, mas que pode ser controlado com ajustes no código.
			try {
				if(lock.tryLock(50, TimeUnit.MILLISECONDS)) {
					System.out.println("LocksTaken: " + main.locksTaken.size());
					if(main.locksTaken.size()>=4) {
						lock.unlock();
						main.locksTaken.remove(main.locksTaken.indexOf(lock));
						Thread.sleep(10l);
						continue;
					}
					main.locksTaken.add(lock);
					System.out.println("Lock: " + lock.toString());
				} else {
					System.out.println("lock sem sucesso");
					continue;
				}
				System.out.println("LocksTaken: " + main.locksTaken.size());
				if(fLock.tryLock(50, TimeUnit.MILLISECONDS)) {
					main.locksTaken.add(fLock);
					System.out.println("fLock com sucesso: " + fLock.toString());
				} else {
//					lock.unlock();
//					main.locksTaken.remove(main.locksTaken.indexOf(lock));
					System.out.println("fLock sem sucesso, lock liberado: " +  lock.toString());
					continue;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch(IndexOutOfBoundsException e) {
				continue;
			}
			lock.unlock();
			main.locksTaken.remove(main.locksTaken.indexOf(lock));
			fLock.unlock();
			main.locksTaken.remove(main.locksTaken.indexOf(fLock));
		}
	}
}