#ifndef COUNT_H
#define COUNT_H
#include <string>
#include <iostream>
#include <cmath>
#include <vector>
#include <map>
#include <fstream>
#include <iomanip>
#include <tr1/unordered_map>
#include "seq.h"
#include "config.h"
using namespace std;
using namespace tr1;

const int aa_num = 20;
const int nt_num = 4;

class Parm
{
public:
	bool count_only;//count_only or generate CV
	bool is_dna;//dna or aa
	string write_cvfile;
	string write_txt_cvfile;//.cv.txt
	string bfile,Bfile;//input bg file,output bg file
	bool calc_dist;//calc_dist pairwise or needn't calc
	bool quiet;
	bool load_ub;//need load background file
	int cv_type;
	bool is_single_input;
	bool subt;//subtraction?

	Parm():count_only(true),is_dna(false),calc_dist(false),quiet(false),load_ub(false),cv_type(1),is_single_input(false),subt(true){};
	void display(void)
	{
		cerr<<"[count_only]  ="<<count_only<<endl;
		cerr<<"[is_dna]      ="<<is_dna<<endl;
		cerr<<"[write_cvfile]="<<write_cvfile<<endl;
		cerr<<"[write_txt_cv]="<<write_txt_cvfile<<endl;
		cerr<<"[calc_dist]   ="<<calc_dist<<endl;
	};
};

typedef unordered_map<mlong,mlong> TMap;
//typedef map<KEY,mlong> TMap;

class CountH
{
	static const char* Nucleotide;
	static const char* Aminoacid;
	char aamap[128];
	char aa2int[128];
	char int2aa[128];
	mlong POW[21][15];//only use POW[4]. and POW[20].
	int  charset_num;//20 for aa and 4 for dna
	Parm parm;
	mlong _k_len;//k-tuple's len
	void init();
	size_t k2n(const char* s,const int len) const;
	int get_k1seeds(size_t k1l, vector< pair<mlong,mlong> >& ret, const TMap& kt1);
public:
	CountH(Parm&);
	string n2k(mlong key,int len) const;
	double count_tuple(const char* f1, const string& fname, const mlong k_len, vector< pair<mlong,double> >& cv, vector<string>& single_file_seqs);
};

#endif
