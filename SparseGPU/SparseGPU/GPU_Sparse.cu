#include <fstream>
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include "cuda_runtime.h"
#include "device_launch_parameters.h"

using namespace std;

int ReadNumbers(const string & s, vector <double> & v);
vector<double> matrixRead(const char* filename_X, vector <double>& v, int& rows, int& cols);
//cudaError gpuError(double* row_C, double* column_C, double* data, double* carpan, double* sonuc);
__global__ void gpu(double* row_C, double* column_C, double* data, double* x, double* y);
int main(){

	cudaEvent_t start, stop;
	cudaEventCreate(&start);
	cudaEventCreate(&stop);

	int rows = 0;
	int cols = 0;
	vector <double> vector_C;
	vector <double> vector_D;
	vector <double> vector_Rp;

	vector_C = matrixRead("C.txt", vector_C, rows, cols);
	//cout << "C vector :" << vector_C.size() << endl;
	vector_D = matrixRead("D.txt", vector_D, rows, cols);
	//cout << "D vector :" << vector_D.size() << endl;
	vector_Rp = matrixRead("Rp.txt", vector_Rp, rows, cols);
	//cout << "Rp vector :" << vector_Rp.size() << endl;

	vector <double> vector_carpan(10000);
	for (int i = 0; i <10000; i++)
		vector_carpan[i] = 1;
	//vector <double> gpuSonuc1(10000);

	double *vectorRow_p = &vector_Rp[0];
	double *vectorColumn_p = &vector_C[0];
	double *vectorData_p = &vector_D[0];
	double *vectorCarpan_p = &vector_carpan[0];
/*	double *gpuSonuc = &gpuSonuc1[0];

	cudaError_t cudaStatus = gpuError(vectorRow_p, vectorColumn_p, vectorData_p, vectorCarpan_p,gpuSonuc);
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "addWithCuda failed!");
		system("pause");
		return 1;
	}
	cudaStatus = cudaDeviceReset();
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaDeviceReset failed!");
		system("pause");
		return 1;
	}*/
	double *poutcome;
	poutcome = (double*)malloc(10000 * sizeof(double));
	memset(poutcome, 0, 10000 * sizeof(double));

	double *pRow, *pColumn, *pData, *pCarpan,*py;
	cudaError_t cudaStatus;
	cudaStatus = cudaSetDevice(0);

	cudaMalloc((void**)&pRow, 10001 * sizeof(double));
	cudaMemcpy(pRow, vectorRow_p, 10001 * sizeof(double), cudaMemcpyHostToDevice);

	cudaMalloc((void**)&pColumn, 99911 * sizeof(double));
	cudaMemcpy(pColumn, vectorColumn_p, 99911 * sizeof(double), cudaMemcpyHostToDevice);

	cudaMalloc((void**)&pData, 99911 * sizeof(double));
	cudaMemcpy(pData, vectorData_p, 99911 * sizeof(double), cudaMemcpyHostToDevice);

	cudaMalloc((void**)&pCarpan, 10000 * sizeof(double));
	cudaMemcpy(pCarpan,vectorCarpan_p, 10000 * sizeof(double), cudaMemcpyHostToDevice);

	cudaMalloc((void**)&py, 10000 * sizeof(double));

	
	cudaEventRecord(start);
	gpu<<<1, 10000>>>(pRow, pColumn, pData, pCarpan, py);
	cudaEventRecord(stop);
	cudaMemcpy(poutcome, py, sizeof(double)* 10000, cudaMemcpyDeviceToHost);
	cudaEventSynchronize(stop);
	float milliseconds = 0;
	cudaEventElapsedTime(&milliseconds, start, stop);
	cout << "Hesaplanan Sure(ms) : " << milliseconds << endl;


	free(poutcome);	
	cudaFree(pRow);
	cudaFree(pColumn);
	cudaFree(pData);
	cudaFree(pCarpan);
	cudaFree(py);
	cudaDeviceReset();
	
	system("pause");

	return 0;
}
/*cudaError gpuError(double* row_C, double* column_C, double* data, double* carpan, double* sonuc){
	double *vectorRow_p;
	double *vectorColumn_p ;
	double *vectorData_p ;
	double *vectorCarpan_p ;
	double *arraySonuc_p;
	double *gpuGelen_p = { 0 };
	cudaError_t cudaStatus;

	cudaStatus = cudaSetDevice(0);
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaSetDevice failed!  Do you have a CUDA-capable GPU installed?");
		exit(-1);
		system("pause");
	}
	cudaStatus = cudaMalloc((void**)&vectorRow_p, 10001 * sizeof(double));
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaMalloc failed!");
		exit(-1);
		system("pause");

	}
	cudaStatus = cudaMalloc((void**)&vectorColumn_p, 99911 * sizeof(double));
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaMalloc failed!");
		exit(-1);
		system("pause");

	}
	cudaStatus = cudaMalloc((void**)&vectorData_p, 99911 * sizeof(double));
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaMalloc failed!");
		exit(-1);
		system("pause");

	}
	cudaStatus = cudaMalloc((void**)&vectorCarpan_p, 10000 * sizeof(double));
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaMalloc failed!");
		exit(-1);
		system("pause");

	}
	cudaStatus = cudaMalloc((void**)&arraySonuc_p, 10000 * sizeof(double));
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaMalloc failed!");
		exit(-1);
		system("pause");

	}
	//-------------------------------------------------------------------------------------------//
	// Copy input vectors from host memory to GPU buffers.
	cudaStatus = cudaMemcpy(vectorRow_p, row_C, 10001 * sizeof(double), cudaMemcpyHostToDevice);
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaMemcpy failed!");
		exit(-1);
		system("pause");

	}
	// Copy input vectors from host memory to GPU buffers.
	cudaStatus = cudaMemcpy(vectorColumn_p, column_C, 99911 * sizeof(double), cudaMemcpyHostToDevice);
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaMemcpy failed!");
		exit(-1);
		system("pause");

	}
	// Copy input vectors from host memory to GPU buffers.
	cudaStatus = cudaMemcpy(vectorData_p, data, 99911 * sizeof(double), cudaMemcpyHostToDevice);
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaMemcpy failed!");
		exit(-1);
		system("pause");

	}
	// Copy input vectors from host memory to GPU buffers.
	cudaStatus = cudaMemcpy(vectorCarpan_p, carpan, 10000 * sizeof(double), cudaMemcpyHostToDevice);
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaMemcpy failed!");
		exit(-1);
		system("pause");

	}
	
	gpu << <1, 1000 >> >(vectorRow_p, vectorColumn_p, vectorData_p, vectorCarpan_p, arraySonuc_p);
	cudaStatus = cudaGetLastError();
	if (cudaStatus != cudaSuccess) {
		//fprintf(stderr, "addKernel launch failed: %s\n", cudaGetErrorString(cudaStatus));
		cout << "addKernel launch failed:" << cudaGetErrorString(cudaStatus) << endl;
		system("pause");

	}

	// cudaDeviceSynchronize waits for the kernel to finish, and returns
	// any errors encountered during the launch.
	cudaStatus = cudaDeviceSynchronize();
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaDeviceSynchronize returned error code %d after launching addKernel!\n", cudaStatus);
		exit(-1);
		system("pause");

	}
	cudaStatus = cudaMemcpy(gpuGelen_p, arraySonuc_p, sizeof(double)* 1000, cudaMemcpyDeviceToHost);
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaMemcpy failed!");
		exit(-1);
		system("pause");

	}
	// Copy input vectors from host memory to GPU buffers.
	cudaStatus = cudaMemcpy(arraySonuc_p, sonuc, 10000 * sizeof(double), cudaMemcpyHostToDevice);
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaMemcpy failed!");
		exit(-1);
		system("pause");

	}
	
	cudaFree(vectorRow_p);
	cudaFree(vectorColumn_p);
	cudaFree(vectorData_p);
	cudaFree(vectorCarpan_p);
	cudaFree(arraySonuc_p);

	return cudaStatus;
}*/
__global__ void gpu(double* row_C, double* column_C, double* data, double* x, double* y)
{
	int row = threadIdx.x + blockIdx.x*blockDim.x;
	if (row< 10000)//son satira girmemek için
		{
			float carpan = 0;

			int row_start = row_C[row];
			int row_end = row_C[row + 1];
			for (int i = row_start; i < row_end; i++)
				carpan += data[i - 1];// *x[column_C[i]];
			y[row] = carpan;
			//printf("hello Word");
			//printf("%d\n", y[row]);
		}
	//cout << y[0] << endl;
}
int ReadNumbers(const string & s, vector <double> & v) {
	istringstream is(s);
	double n;
	while (is >> n) {
		v.push_back(n);
	}
	return v.size();
}
vector<double> matrixRead(const char* filename_X, vector <double>& vector, int& rows, int& cols){

	ifstream fileTxt;
	string line;

	fileTxt.open(filename_X);
	if (fileTxt.is_open())
	{
		int i = 0;
		getline(fileTxt, line);


		cols = ReadNumbers(line, vector);
		//cout << "cols:" << cols << endl;


		for (i = 1; i<99912; i++){
			if (getline(fileTxt, line).end) break;
			ReadNumbers(line, vector);

		}

		rows = i;
		//cout << "rows :" << rows << endl;
		if (rows >99912) cout << "N must be smaller than MAX_INT";

		fileTxt.close();
	}
	else{
		cout << "file open failed";
	}

	//cout << "vector:"<<endl;
	for (int i = 0; i<rows; i++){
		for (int j = 0; j<cols; j++){
			//	cout << vector[i*cols + j] << "\t";
		}
		//cout << vector[0]<<endl;
		//cout << endl;
	}
	return vector;
}
