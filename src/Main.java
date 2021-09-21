public class Main {
	private static int s = 999999;
	private static volatile int highest;

	public static void main(String[] args) {
		MinhaThread mT1 = new MinhaThread(1, new Main());
		MinhaThread mT2 = new MinhaThread(2, new Main());
		MinhaThread mT3 = new MinhaThread(3, new Main());

		mT1.setPriority(Thread.MIN_PRIORITY);
		mT2.setPriority(Thread.MAX_PRIORITY);
		mT1.start();
		mT2.start();
		mT3.start();
		for (int i = 0; i < 99999; i++) {

		}
		System.err.println(
				"Terminou thread principal. P:" + Thread.currentThread().getPriority() + ". Highest: " + highest);
	}

	public void setHighest(int highest) {
		Main.highest = highest;
	}

	public int getHighest() {
		return highest;
	}

	public int getS() {
		return s;
	}

}

class MinhaThread extends Thread {
	int numThread = 0;
	private Main main;

	MinhaThread(int num, Main main) {
		this.numThread = num;
		this.main = main;
	}

	public void run() {
		for (int i = 0; i < 999999; i++) {
			// System.out.println("contador thread " + numThread + ": " + i);
			main.setHighest(largestPrimeFactor(main.getS()));
		}
		System.err.println("Terminou thread " + numThread + ". P:" + Thread.currentThread().getPriority()
				+ ". highest: " + main.getHighest());
	}

	int largestPrimeFactor(int n) {
		if (n <= 1) {
			return 0;
		} else {
			int div = 2;
			while (div < n) {
				if (n % div != 0) {
					div++;
				} else {
					n = n / div;
					div = 2;
				}
			}
			return n;
		}
	}
}