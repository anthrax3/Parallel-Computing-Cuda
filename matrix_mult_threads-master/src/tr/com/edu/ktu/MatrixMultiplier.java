package tr.com.edu.ktu;

import java.util.Arrays;
import java.util.Stack;

public class MatrixMultiplier {
	Matrix m1, m2;
	Matrix result;
	final int size;
	final int threadCount = 4;
	Thread[] threads = new Thread[threadCount];
	final int BLM;
	volatile int str = 0;
	volatile int stn = 0;

	public MatrixMultiplier(Matrix m1, Matrix m2, int size) {
		this.m1   = m1;
		this.m2   = m2;
		this.result = new Matrix(m1.rowNum, m2.colNum);
		this.size = size;
		this.BLM  = size / threadCount;
	}

	//3.Versiyon için gerekli threadleri oluþturuyor.
	public void initThreads1() {
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					multiplyTest1();
				}
			});
		}
	}

	//4.Versiyon için gerekli threadleri oluþturuyor.
	public void initThreads2() {
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					multiplyTest2();
				}
			});
		}
	}

	//Çarpma iþlemini baþlatýyor.
	public void startMultiplication() {
		for (Thread thread : threads) {
			thread.start();
			System.out.println(thread.getName() + " started working...");
		}
		waitForMultiplication();
	}

	//Tüm threadlerin iþini bitirmesi için bekleniyor. Süreyi hesaplamak için kullanýlýyor.
	private void waitForMultiplication() {
		Stack<Thread> stack = new Stack<>();
		stack.addAll(Arrays.asList(threads));
		while (!stack.isEmpty()) {
			Thread thread = stack.peek();
			if (!thread.isAlive()) {
				stack.remove(thread);
			}
		}
	}

	private void finishThread() {
		System.out.println(Thread.currentThread().getName() + " finished working...");
		Thread.currentThread().interrupt();
	}

	//m1 x m2 için m1'in satýrlarýný threadler arasý paylaþtýrarak çarpým gerçeleþtirir.
	public void multiplyTest1() {
		while (str < size) {
			int t_str;
			t_str = str;
			str += BLM;
			System.out.println(Thread.currentThread().getName() + " started at row = " + t_str + ", to = " + (t_str + BLM));
			Matrix.multiply(m1, m2, result, t_str, t_str + BLM);
			finishThread();
		}
	}

	//m1 x m2'de satýr ve sütunu çarpan her thread iþi bitikten sonra yeni satýr veya sütuna geçer.
	public void multiplyTest2() {
		while (str < size) {
			int t_str, t_stn;
			t_str = str;
			t_stn = stn++;
//			System.out.println(Thread.currentThread().getName() + " started at row = " + t_str + ", col = " + t_stn);
			if (t_stn >= size) {
				t_stn = 0;
				stn = 0;
				str++;
				t_str = str;
				if (t_str >= size) {
					finishThread();
					return;
				}
			}
			Matrix.multiply2(m1, m2, result, t_str, t_stn);
		}
		finishThread();
	}

}
