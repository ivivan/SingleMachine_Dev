#ifndef DIST_H
#define DIST_H
#include <cmath>
#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include<cstdlib>		/* exit */
#include<algorithm>		/* remove_if, sort */
#include "count.h"
#include "config.h"
using namespace std;

typedef basic_string<char>::size_type S_T;
static const S_T npos = -1;
//split string by tok
vector<string> tokenize(const string src, string tok,
						bool trim=false, string null_subst="")
{
	if( src.empty() || tok.empty() )
		throw "tokenize: empty string\0";
	vector<string> v;
	S_T pre_index = 0, index = 0, len = 0;
	while( (index = src.find_first_of(tok, pre_index)) != npos )
	{
		if( (len = index-pre_index)!=0 )
			v.push_back(src.substr(pre_index, len));
		else if(trim==false)
			v.push_back(null_subst);
		pre_index = index+1;
	}
	string endstr = src.substr(pre_index);
	if( trim==false )
		v.push_back( endstr.empty()? null_subst:endstr );
	else if( !endstr.empty() )
		v.push_back(endstr);
	return v;
}

int read_cv(string filename,vector< pair<mlong,double> >& cv,mlong& k_len, double& inner, bool is_quick=false)
{
	int len=0;
	ifstream cvfile;
	cvfile.open(filename.c_str(),ios::in | ios::binary);
    if(!cvfile.good())
    {
    	cerr<<"open cv file error:"<<filename<<endl;
    	exit(1);
    }
    cvfile.read((char*)&k_len, sizeof(mlong));
    cvfile.read((char*)&inner, sizeof(double));
    cvfile.read((char*)&len, sizeof(mlong));
	if(is_quick)
		return len;
    if(len>0)//file type 1
    {
    	cv.resize(len);
    	cvfile.read((char*)&(*cv.begin()), sizeof( pair<mlong,double> )*len);
    }
    else if(len == -1)//file type 2
    {
    	cvfile.read((char*)&len, sizeof(mlong));
    	mlong char_set = len;

    	cvfile.read((char*)&len, sizeof(mlong));
    	vector< pair<mlong,double> > tmpcv(len);
    	cvfile.read((char*)&(*tmpcv.begin()), sizeof( pair<mlong,double> )*len);

    	cvfile.read((char*)&len, sizeof(mlong));
    	vector< mlong > is0(len);
    	cvfile.read((char*)&(*is0.begin()), sizeof( mlong )*len);

    	mlong total = (mlong)pow(char_set,(double)k_len);
    	cv.resize(total);
    	for(mlong i=0;i<total;++i)
    	{
    		cv[i]=make_pair(i,-1);
    	}
    	for(mlong i=0;i<tmpcv.size();++i)
    	{
    		cv[tmpcv[i].first]=tmpcv[i];
    	}
    	for(mlong i=0;i<is0.size();++i)
    	{
    		cv[is0[i]]=make_pair(0,0);//mark as to be del
    	}
    	pair<mlong,double> to_be_del=make_pair(0,0);
    	vector< pair<mlong,double> >::iterator it;
    	it=remove_if(cv.begin(),cv.end(), bind2nd(equal_to< pair<mlong,double> >(),to_be_del));
    	cv.erase(it,cv.end());
    }
    else
    {
    	cerr<<"Unknown CV file type: "<<len<<endl;
    	exit(1);
    }
    cvfile.close();
	return len;
}

bool by_first(const pair<double, mlong >& a, const pair<double, mlong >& b)
{
	return a.first<b.first;
}
void stat_dist(const vector< pair<mlong,double> >&  cva1, const vector< pair<mlong,double> >&  cva2,
			string f1,string f2,int k_len,char* osfilename, bool is_dna)
{
	int pos=f2.find_last_of("/");
	char buf[16];
	sprintf(buf,"-%d.xls",k_len);
	string ofilename=f1+"-"+f2.substr(pos+1)+buf;
	if(osfilename!=NULL)
	{
		ofilename=osfilename;
	}

	int x_len=1024;
	vector< pair<double,double> > stat[2];
	vector<double> cross;
	stat[0].resize(x_len+1);
	stat[1].resize(x_len+1);
	cross.resize(x_len+1);
	vector< pair<double, mlong > > maxmin_stat;
	//maxmin_stat.reserve(cva1.size());
	int charset=20;
	if(is_dna)
		charset=4;
	float sec=pow(charset,(double)k_len)/x_len;
	for(int i=0;i<x_len;++i)
		stat[0][i]=make_pair(0,0),stat[1][i]=make_pair(0,0),cross[i]=0;

	for(size_t i=0;i<cva1.size();++i)
	{
		if(cva1[i].second<-0.99999999)
		{
			stat[0][(mlong)(cva1[i].first/sec)].first+=1;
		}
		else
		{
			stat[0][(mlong)(cva1[i].first/sec)].second+=cva1[i].second*cva1[i].second;
		}
	}
	for(size_t i=0;i<cva2.size();++i)
	{
		if(cva2[i].second<-0.99999999)
		{
			stat[1][(mlong)(cva2[i].first/sec)].first+=1;
		}
		else
		{
			stat[1][(mlong)(cva2[i].first/sec)].second+=cva2[i].second*cva2[i].second;
		}
	}

	size_t len1=cva1.size();
	size_t len2=cva2.size();
	for(size_t p1=0,p2=0;p1<len1 && p2<len2;)
	{
		if(cva1[p1].first < cva2[p2].first)
		{
			p1++;
		}
		else if(cva1[p1].first > cva2[p2].first)
		{
			p2++;
		}
		else // same
		{
			maxmin_stat.push_back( make_pair( cva1[p1].second*cva2[p2].second , cva1[p1].first ) );
			cross[(mlong)(cva1[p1].first/sec)] += cva1[p1].second*cva2[p2].second;
			p1++;
			p2++;
		}
	}

	ofstream ofile;
	ofile.open(ofilename.c_str());
	if(!ofile.good())
	{
		cerr<<"open output file error:"<<ofilename<<endl;
		exit(1);
	}
	cout<<"write statistics file: "<<ofilename<<endl;
	ofile<<"#file1_-1\tfile1_>-1\tfile2_-1\tfile2_>-1\tmultip\n";
	for(int i=0;i<x_len;++i)
	{
		ofile <<stat[0][i].first<<"\t"<<stat[0][i].second<<"\t"
			  <<stat[1][i].first<<"\t"<<stat[1][i].second<<"\t"
			  <<cross[i]<<endl;
	}
	ofile.close();

	sort(maxmin_stat.begin(),maxmin_stat.end(),by_first);
	ofilename+=".sort";
	ofile.open(ofilename.c_str());
	if(!ofile.good())
	{
		cerr<<"open output file error:"<<ofilename<<endl;
		exit(1);
	}
	Parm parm;
	parm.is_dna=is_dna;
	CountH ct(parm);
	cout<<"write sorted file: "<<ofilename<<endl;
	ofile<<"#value\tindex\tstring\n";

	size_t i=0;
	for(;i<maxmin_stat.size() && i<200;++i)//output first 200
	{
		ofile <<maxmin_stat[i].first<<"\t"
			  <<(mlong)(maxmin_stat[i].second/sec)<<"\t"
			  <<ct.n2k(maxmin_stat[i].second,k_len)<<"\t"
			  <<maxmin_stat[i].second<<endl;
	}
	if(i>=200)//output end 200
	{
		size_t begin=i;
		if(begin < maxmin_stat.size()-200)
			begin=maxmin_stat.size()-200;
		for(i=begin;i<maxmin_stat.size();++i)
		{
			ofile <<maxmin_stat[i].first<<"\t"
				  <<(mlong)(maxmin_stat[i].second/sec)<<"\t"
				  <<ct.n2k(maxmin_stat[i].second,k_len)<<"\t"
				  <<maxmin_stat[i].second<<endl;
		}
	}
	ofile.close();
}

double count_dist(const vector< pair<mlong,double> >&  cva1, const vector< pair<mlong,double> >&  cva2,
				double inner1, double inner2, bool qli_meth = false)
{
	double cross_inner=0;
	//double inner1=0,inner2=0;
	size_t len1=cva1.size();
	size_t len2=cva2.size();
	for(size_t p1=0,p2=0;p1<len1 && p2<len2;)
	{
		if(cva1[p1].first < cva2[p2].first)
		{
			p1++;
		}
		else if(cva1[p1].first > cva2[p2].first)
		{
			p2++;
		}
		else // same
		{
			cross_inner += cva1[p1].second*cva2[p2].second;

			p1++;
			p2++;
		}
	}
		return 0.5*(1-cross_inner/sqrt(inner1*inner2));
	//return sqrt(cross_inner);
}

/* useless fun
double count_dist2(double* cva1,double* cva2,mlong len1,mlong len2)
{
    mlong v1_wait=0;
	mlong v2_wait=0;
	double cross_inner=0;
	double cv1,cv2;
	for(mlong p1=0,p2=0;p1<len1 && p2<len2;)
	{
		cv1=cva1[p1];
		if(cv1<-1)//gap
		{
			if(v1_wait<1)
			{
				v1_wait = (mlong)(-1-cv1);
			}

			cv1=0;
			if(v1_wait==1)
			{
				p1++;
			}
			v1_wait--;
		}
		else
		{
			p1++;
		}

		cv2=cva2[p2];
		if(cv2<-1)//gap
		{
			if(v2_wait<1)
			{
				v2_wait = (mlong)(-1-cv2);
			}

			cv2=0;
			if(v2_wait==1)
			{
				p2++;
			}
			v2_wait--;
		}
		else
		{
			p2++;
		}

		if(v1_wait>1 && v2_wait>1)
		{
			mlong small=v1_wait<v2_wait?v1_wait:v2_wait;
			--small;
			v1_wait -=small;
			v2_wait -=small;
		}
		cross_inner += cv1*cv2;
		//cerr<<"p1="<<p1<<" p2="<<p2<<endl;
		//cerr<<"calc:"<<cv[i][p1]<<" "<<cv[j][p2]<<endl;
		//getchar();
	}
	return cross_inner;
}
*/
#endif
