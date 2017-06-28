package tr.com.edu.ktu;

import java.util.Timer;
import java.util.TimerTask;

public class TestMatrix {
	static Timer timer; 			//Çarpma iþlemi sürerken geçen süreyi yazdýrmak için
	static long startTime, endTime; //Çarpma iþleminin süresini hesaplamak için
	static Matrix m1, m2, result;   //1. matris: m1, 2.matris: m2 ve çarpým sonucu üretilen matris: result

	//Rastgele flaot deðerlerden oluþan matrisleri boyutuna göre oluþturuyor.
	public static void initMatrices(int size) {
		System.out.println("Creating matrices...");
		m1 = Matrix.createRandomMatrix(size, size);
		m2 = Matrix.createRandomMatrix(size, size);
	}

	//Matisleri yazdýrmak için
	public static void printMatrices() {
		System.out.println("M1 = ");
		m1.print();
		System.out.println("M2 = ");
		m2.print();
	}

	//Geçen süreyi yazan(saniye türünden) zamanlayýcýyý baþlatýr.
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

	//Geçen süreyi yazdýrmayý durdurur.
	public static void cancelTimer() {
		timer.cancel();
	}

	//1.Versiyon : Normal matris çarpýmý, tek thread içeriyor
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

	//2.Versiyon : m1 x m2 iþleminde m2'nin transpozu alýnmýþ gibi varsayýlarak çarpým yapýlýyor.
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

	//3.Versiyon : m1 x m2 iþleminde m1 matrisinin satýrýný 4 thread arasýnda paylaþtýrarak çarpým yapýyor.
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

	//4.Versiyon : Matris çarpýmýnda satýr ve sütunu çarptýktan sonra iþi biten thread sýradaki satýr veya sütuna geçerek
	//çarpýmý tamamlýyor.
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

		int size = 1000; //Matris boyutu 1000 x 1000, deðiþtirilebilir 2000, 3000 vs...
		commonMatrixMultiplicationTest(size); 		//Odev 1.Versiyon, normal matris çarpýmý
		linearMatrixMultiplicationTest(size);		//Odev 2.Versiyon, ikince matrisin transpozu alýnmýþ gibi çarpýlýyor.
		matrixMultiplicationWithThreadsTest1(size);	//Odev 3.Versiyon, 4 threadli satýr paylaþýmlý matris çarpýmý
		matrixMultiplicationWithThreadsTest2(size);	//Odev 4.Versiyon, 4 threadli iþi biten threadin eni satýr-sütune geçtiði matris çarpýmý

	}
}
