package tr.com.edu.ktu;

import java.util.Timer;
import java.util.TimerTask;

public class TestMatrix {
	static Timer timer; 			//�arpma i�lemi s�rerken ge�en s�reyi yazd�rmak i�in
	static long startTime, endTime; //�arpma i�leminin s�resini hesaplamak i�in
	static Matrix m1, m2, result;   //1. matris: m1, 2.matris: m2 ve �arp�m sonucu �retilen matris: result

	//Rastgele flaot de�erlerden olu�an matrisleri boyutuna g�re olu�turuyor.
	public static void initMatrices(int size) {
		System.out.println("Creating matrices...");
		m1 = Matrix.createRandomMatrix(size, size);
		m2 = Matrix.createRandomMatrix(size, size);
	}

	//Matisleri yazd�rmak i�in
	public static void printMatrices() {
		System.out.println("M1 = ");
		m1.print();
		System.out.println("M2 = ");
		m2.print();
	}

	//Ge�en s�reyi yazan(saniye t�r�nden) zamanlay�c�y� ba�lat�r.
	public static void initTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int timePassedInSeconds = 0;
			@Override
			public void run() {
				System.out.println("Timer : " + (++timePassedInSeconds) + "s" + " passed...");
			}
		}, 1000, 1000);
	}

	//Ge�en s�reyi yazd�rmay� durdurur.
	public static void cancelTimer() {
		timer.cancel();
	}

	//1.Versiyon : Normal matris �arp�m�, tek thread i�eriyor
	public static void commonMatrixMultiplicationTest(int matrixSize) {
		initMatrices(matrixSize);

		initTimer();
		System.out.println("Common matrix multiplication started...");
		startTime = System.currentTimeMillis();
		result = Matrix.multiply(m1, m2);
		endTime = System.currentTimeMillis();
		System.out.println("Common matrix multiplication finished...");
		System.out.print("Common matrix multiplication results : ");
		System.out.println("multiplication took " + (endTime - startTime) + "ms" + ", matrix size = " + matrixSize + "x" + matrixSize);
		cancelTimer();

		System.out.println("--------------------------------------------------------------------");
	}

	//2.Versiyon : m1 x m2 i�leminde m2'nin transpozu al�nm�� gibi varsay�larak �arp�m yap�l�yor.
	public static void linearMatrixMultiplicationTest(int matrixSize) {
		initMatrices(matrixSize);

		initTimer();
		System.out.println("Linear matrix multiplication started...");
		startTime = System.currentTimeMillis();
		result = Matrix.linearMultiply(m1, m2);
		endTime = System.currentTimeMillis();
		System.out.println("Linear matrix multiplication finished...");
		System.out.print("Linear matrix multiplication results : ");
		System.out.println("multiplication took "  + (endTime - startTime) + "ms" + ", matrix size = " + matrixSize + "x" + matrixSize);
		cancelTimer();

		System.out.println("--------------------------------------------------------------------");
	}

	//3.Versiyon : m1 x m2 i�leminde m1 matrisinin sat�r�n� 4 thread aras�nda payla�t�rarak �arp�m yap�yor.
	public static void matrixMultiplicationWithThreadsTest1(int matrixSize) {
		initMatrices(matrixSize);

		initTimer();
		System.out.println("Common matrix multiplication started with 4 threads...");
		startTime = System.currentTimeMillis();
		MatrixMultiplier multiplier = new MatrixMultiplier(m1, m2, matrixSize);
		multiplier.initThreads1();
		multiplier.startMultiplication();
		endTime = System.currentTimeMillis();
		System.out.println("Common matrix multiplication with 4 threads finished...");
		System.out.print("Common matrix multiplication with 4 threads results : ");
		System.out.println("multiplication took " + (endTime - startTime) + "ms" + ", matrix size = " + matrixSize + "x" + matrixSize);
		cancelTimer();

		System.out.println("--------------------------------------------------------------------");
	}

	//4.Versiyon : Matris �arp�m�nda sat�r ve s�tunu �arpt�ktan sonra i�i biten thread s�radaki sat�r veya s�tuna ge�erek
	//�arp�m� tamaml�yor.
	public static void matrixMultiplicationWithThreadsTest2(int matrixSize) {
		initMatrices(matrixSize);

		initTimer();
		System.out.println("Common matrix multiplication started with 4 threads...");
		startTime = System.currentTimeMillis();
		MatrixMultiplier multiplier = new MatrixMultiplier(m1, m2, matrixSize);
		multiplier.initThreads2();
		multiplier.startMultiplication();
		endTime = System.currentTimeMillis();
		System.out.println("Common matrix multiplication with 4 threads finished...");
		System.out.print("Common matrix multiplication with 4 threads results : ");
		System.out.println("multiplication took " + (endTime - startTime) + "ms" + ", matrix size = " + matrixSize + "x" + matrixSize);
		cancelTimer();

		System.out.println("--------------------------------------------------------------------");
	}


	public static void main(String[] args) {
		System.out.println("Matrix multiplication test is starting...");

		int size = 1000; //Matris boyutu 1000 x 1000, de�i�tirilebilir 2000, 3000 vs...
		commonMatrixMultiplicationTest(size); 		//Odev 1.Versiyon, normal matris �arp�m�
		linearMatrixMultiplicationTest(size);		//Odev 2.Versiyon, ikince matrisin transpozu al�nm�� gibi �arp�l�yor.
		matrixMultiplicationWithThreadsTest1(size);	//Odev 3.Versiyon, 4 threadli sat�r payla��ml� matris �arp�m�
		matrixMultiplicationWithThreadsTest2(size);	//Odev 4.Versiyon, 4 threadli i�i biten threadin eni sat�r-s�tune ge�ti�i matris �arp�m�

	}
}
