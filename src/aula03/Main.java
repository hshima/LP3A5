package aula03;

public class Main {
	public volatile static int produtos = 0;
	private static Object lock = new Object();
	
	public static void main(String[] args) {
		Produtor p1 = new Produtor(1, lock);
		Consumidor c1 = new Consumidor(2, lock);
		p1.start();
		c1.start();
	}
}

class Produtor extends Thread {
	int nProdutor = 0;
	private static volatile Object lock;// = new Object();
	Produtor(int num, Object lock){
		this.nProdutor = num;
		Produtor.lock = lock;
	}
	@Override
	public void run() {
		for(int i = 0; i <= 20; i++) {
			synchronized( lock ) {
				if (Main.produtos < 100) {
					Main.produtos++;
//					System.out.println("Produzido!");
					lock.notifyAll();
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("Produtor " + nProdutor + ": " + Main.produtos);
		}
	}
}

class Consumidor extends Thread {
	int nConsumidor = 0;
	private static volatile Object lock;// = new Object();
	Consumidor(int num, Object lock){
		this.nConsumidor = num;
		Consumidor.lock = lock;
	}
	@Override
	public void run() {
		for(int i = 0; i <= 20; i++) {
			synchronized( lock ) 
			{	
				if(Main.produtos == 0) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if ( Main.produtos > 0) {
					Main.produtos--;	
//					System.out.println("Consumido!");
					lock.notifyAll();
				}
			}
			System.out.println("Consumidor " + nConsumidor + ": " + Main.produtos);
		}
	}
}