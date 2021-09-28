package aula01;

public class VolatileThreads {
	public static void main(String[] args) {
		new Thread(new Test(1)).start();
		new Thread(new Test(2)).start();
		new Thread(new Test(3)).start();
	}
}

class Test extends Thread {
	public volatile static Integer somaThread = 0;
	public volatile static Object lock = new Object();
	private Integer numThread = 0;

	Test(Integer numThread) {
		this.numThread = numThread;
	}

	@Override
	public void run() {
		for (int i = 0; i < 10000000; i++) {
			synchronized (lock) {
//			synchronized (somaThread) {
				somaThread++;
			}
		}
		try {
			Thread.currentThread().sleep(17l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Contador thread: " + numThread + ", Soma thread: " + somaThread);
	}
}