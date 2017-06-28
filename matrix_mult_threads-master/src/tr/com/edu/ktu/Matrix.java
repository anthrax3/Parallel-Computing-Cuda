package tr.com.edu.ktu;

import java.util.Random;

public class Matrix {
	float[][] data;
	int rowNum;
	int colNum;

	public Matrix(int rowNum, int colNum) {
		this.data   = new float[rowNum][colNum];
		this.rowNum = rowNum;
		this.colNum = colNum;
	}

	public static Matrix multiply(Matrix m1, Matrix m2) {
		Matrix result = new Matrix(m1.rowNum, m2.colNum);
		for (int i = 0; i < m1.rowNum; i++) {
		    for (int j = 0; j < m2.colNum; j++) {
		        for (int k = 0; k < m1.colNum; k++) {
		        	result.data[i][j] += m1.data[i][k] * m2.data[k][j];
		        }
		    }
		}
		return result;
	}

	public static void multiply(Matrix m1, Matrix m2, Matrix result, int rowLimit1, int rowLimit2) {
		if(result == null) {
			throw new IllegalArgumentException("result can't be null");
		}
		if(result.rowNum != m1.rowNum || result.colNum != m2.colNum) {
			throw new IllegalArgumentException("Matrices can't be multiply");
		}
		for (int i = rowLimit1; i < rowLimit2; i++) {
		    for (int j = 0; j < m2.colNum; j++) {
		        for (int k = 0; k < m1.colNum; k++) {
		        	result.data[i][j] += m1.data[i][k] * m2.data[k][j];
		        }
		    }
		}
	}

	public static void multiply2(Matrix m1, Matrix m2, Matrix result, int row, int col) {
		if(result == null) {
			throw new IllegalArgumentException("result can't be null");
		}
		if(result.rowNum != m1.rowNum || result.colNum != m2.colNum) {
			throw new IllegalArgumentException("Matrices can't be multiply, row and column counts must be the proper...");
		}
		for (int k = 0; k < m1.colNum; k++) {
            result.data[row][col] += m1.data[row][k] * m2.data[k][col];
        }
	}

	public static Matrix linearMultiply(Matrix m1, Matrix m2) {
		if((m1.rowNum != m2.rowNum) || (m1.colNum != m2.colNum)) {
			throw new IllegalArgumentException("Matrices can't be multiply, row and column counts must be the same...");
		}
		Matrix result = new Matrix(m1.rowNum, m2.colNum);
		for (int i = 0; i < m1.rowNum; i++) {
		    for (int k = 0; k < m2.rowNum; k++) {
		        for (int j = 0; j < m1.colNum; j++) {
		        	result.data[i][k] += m1.data[i][j] * m2.data[k][j];
		        }
		    }
		}
		return result;
	}

	public static void linearMultiply(Matrix m1, Matrix m2, Matrix result, int rowLimit1, int rowLimit2) {
		if(result == null) {
			throw new IllegalArgumentException("result can't be null");
		}
		if((m1.rowNum != m2.rowNum) || (m1.colNum != m2.colNum)) {
			throw new IllegalArgumentException("row and column number must be the same...");
		}
		for (int i = rowLimit1; i < rowLimit2; i++) {
		    for (int k = 0; k < m2.rowNum; k++) {
		        for (int j = 0; j < m1.colNum; j++) {
		        	result.data[i][k] += m1.data[i][j] * m2.data[k][j];
		        }
		    }
		}
	}

	public static Matrix transpose(Matrix m) {
		Matrix trans = new Matrix(m.colNum, m.rowNum);
		for (int i = 0; i < m.data.length; i++) {
			for (int j = 0; j < m.data[0].length; j++) {
				trans.data[j][i] = m.data[i][j];
			}
		}
		return trans;
	}

	public static Matrix createRandomMatrix(int rowNum, int colNum) {
		Random random = new Random();
		Matrix mat = new Matrix(rowNum, colNum);
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				mat.data[i][j] = random.nextFloat();
			}
		}
		return mat;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Matrix)) {
			return false;
		}
		Matrix other = (Matrix) obj;
		if (this.rowNum != other.rowNum) {
			return false;
		}
		if (this.colNum != other.colNum) {
			return false;
		}
		if(!this.data.equals(other.data)) {
			return false;
		}
		return true;
	}

	public void print() {
		StringBuilder builder = new StringBuilder();
		for (float[] fs : data) {
			for (float f : fs) {
				builder.append(f).append(" ");
			}
			builder.append("\n");
		}
		builder.append("--------------------------------------------------------");
		System.out.println(builder.toString());
	}

}
