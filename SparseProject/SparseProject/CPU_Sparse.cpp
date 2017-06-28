#include <fstream>
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include <time.h>

using namespace std;

int ReadNumbers(const string & s, vector <double> & v);
vector<double> matrixRead(const char* filename_X, vector <double>& v, int& rows, int& cols);
void cpu(vector<double>& row_C, vector<double>& column_C, vector<double>& data, vector<double>& x);
int main(){

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

	vector <double> x(10000);
	for (int i = 0; i <10000; i++){
		x[i] = 1;
	}
	//cout << vector_D[0] << endl;
	//cout << vector_D[1] << endl;
	//cout << vector_D[2] << endl;
	//cout << vector_D[3] << endl;

	clock_t tStart = clock();
	cpu(vector_Rp, vector_C, vector_D, x);
	printf("Gecen sure: %.5fs\n", (double)(clock() - tStart) / CLOCKS_PER_SEC);
	system("pause");

	return 0;
}
void cpu(vector<double>& row_C, vector<double>& column_C, vector<double>& data, vector<double>& x)
{
	vector<double> y(10000);
	for (int i = 0; i <10000; i++)	{
		//static int xx = 0;//hata kontrolu için 10000'e kadar sayýyor mu
		if (i < 10000)//son satýra girmemek için
		{
			float sonuc = 0;

			int satriBasi = row_C[i];
			int satirSonu = row_C[i + 1];
			for (int k = satriBasi; k < satirSonu; k++){
				sonuc += data[k - 1]*x[column_C[i]];
			}
			y[i] = sonuc;
			cout << y[i] << endl;
			//xx++;
			//cout << x << endl;
		}
	}		
	//cout << y.size() << endl;
	//for (int i = 0; i <50; i++)
		//cout << y[i] << endl;

	

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
