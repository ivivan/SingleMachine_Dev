#include <cstdlib>
#include <string.h>
#include <algorithm>
#include <sys/stat.h>
#include "count.h"
#include "config.h"

const char* CountH::Nucleotide="ACGT";
const char* CountH::Aminoacid="ACDEFGHIKLMNPQRSTVWY";

CountH::CountH(Parm& p)
{
	parm=p;
	init();
}
void CountH::init()
{
	for(int i=0;i<128;++i)
	{
		aamap[i]=(char)i;
		aa2int[i]=0;
		int2aa[i]='A';
	}
	if(!parm.is_dna)
	{
		charset_num=20;
		aamap[(int)'B']='D';
		aamap[(int)'U']='C';
		aamap[(int)'X']='G';
		aamap[(int)'Z']='E';
		for(int i=0;i<charset_num;++i)
		{
			aa2int[(int)Aminoacid[i]]=i;
			int2aa[i]=Aminoacid[i];
		}
	}
	else
	{
		charset_num=4;
		aamap[(int)'R']='A';//AG
		aamap[(int)'Y']='T';//CT
		aamap[(int)'M']='A';//AC
		aamap[(int)'K']='T';//TG
		aamap[(int)'S']='C';//CG
		aamap[(int)'W']='A';//AT

		aamap[(int)'B']='T';//TCG
		aamap[(int)'D']='A';//ATG
		aamap[(int)'H']='A';//ATC
		aamap[(int)'V']='A';//ACG

		aamap[(int)'N']='A';//ACGT
		aamap[(int)'X']='A';//ACGT
		for(int i=0;i<charset_num;++i)
		{
			aa2int[(int)Nucleotide[i]]=i;
			int2aa[i]=Nucleotide[i];
		}
	}
	//note other cells in POW are not init, be careful
	int max_k_len=(int)(sizeof(mlong)*8*log(2)/log(charset_num));
	POW[charset_num][0]=1;
	for(int i=1;i<=max_k_len;++i)
		POW[charset_num][i] = charset_num*POW[charset_num][i-1];
}
inline size_t CountH::k2n(const char* s,const int len) const
{
	size_t h=0;
	for(int i=0;i<len;++i)
  		h = h*charset_num + *(s+i);
  	return h;
}
string CountH::n2k(mlong key, int len) const
{
	string ret;
	for(int i=len-1;i>=0;--i)
	{
		mlong stage= POW[charset_num][i];
		mlong base=key/stage;
		key -= base*stage;
		ret += int2aa[ base ];
	}
	return ret;
}
int CountH::get_k1seeds(size_t k1l, vector< pair<mlong,mlong> >& ret, const TMap& kt1)
{
	mlong stage=POW[charset_num][_k_len-2];
	mlong first_num=(mlong)(k1l/stage);
	mlong k2_num=k1l-first_num*stage;

	int ret_i=0;
	for(int i=0;i<charset_num;++i)
	{
		if(kt1.find(k2_num*charset_num+i)==kt1.end())//this seed is useless
			continue;
		ret[ret_i++]=make_pair(k2_num,i);
	}
	return ret_i;
}
bool by_first( const pair<mlong,double>& a1, const pair<mlong,double>& b1)
{
	return a1.first<b1.first;
}
double CountH::count_tuple(const char* f1, const string& fname, const mlong k_len,
	vector< pair<mlong,double> >& cv, vector<string>& single_file_seqs)
{
	_k_len=k_len;

	struct stat sbuf;
	stat(f1,&sbuf);//get file size
	//TMap kt((size_t)(sbuf.st_blocks*512*1.5));//k-tuple,store k
	//TMap kt1((size_t)(sbuf.st_blocks*512*1.5));//k-1
	//TMap kt2((size_t)(sbuf.st_blocks*512*2));//store k-2
	TMap kt,kt1,kt2;

	seq_t sequence;
    sequence.s = NULL;
    sequence.m = 0;
    char name[4096];
    mlong total_seq=0;
    mlong total_tuple=0;//count k tuple
    mlong total_tuple1=0;//count k-1 tuple
    mlong total_tuple2=0;//count k-2 tuple
    if(!parm.quiet)
    	cout<<"read&count seq: "<<f1<<endl;
    if(single_file_seqs.size())
    {
    	for(unsigned int k=0;k<single_file_seqs.size();++k)
    	{
    		char* pseq = new char[single_file_seqs[k].length()+1];
    		memmove(pseq,single_file_seqs[k].c_str(),single_file_seqs[k].length());
    		pseq[single_file_seqs[k].length()]='\0';

    		total_seq++;
    		sequence.s=(uchar*)pseq;
    		sequence.l=single_file_seqs[k].length();
	    	if(sequence.s[sequence.l-1] == '*')//stop codon
	    	{
	    		sequence.l--;//del that '*'
	    	}
	    	for(int i=0;i<sequence.l;++i)
	    	{
	    		//map aa to regular char set, and then to int
	    		sequence.s[i]= (char)aa2int[(int)aamap[toupper(sequence.s[i])]];
	    	}

	    	if((mlong)sequence.l>=k_len)
	    	{
	    		total_tuple+=sequence.l-k_len+1;
	    		if(!parm.load_ub)
	    		{
	    			total_tuple1+=sequence.l-k_len+2;
	    			total_tuple2+=sequence.l-k_len+3;
	    		}
	    		for(mlong i=0;i<(mlong)sequence.l-k_len+1;++i)
	    		{
	    			mlong key=k2n((char*)sequence.s+i,k_len-2);
	    			if(!parm.load_ub)
	    				++kt2[key];
	    			key=key*charset_num + *(sequence.s+i+k_len-2);
	    			if(!parm.load_ub)
	    				++kt1[key];
	    			key=key*charset_num + *(sequence.s+i+k_len-1);
	    			++kt[key];
	    		}
	    		if(!parm.load_ub)
	    		{
	    			++kt1[k2n((char*)sequence.s+sequence.l-k_len+1,k_len-1)];
	    			++kt2[k2n((char*)sequence.s+sequence.l-k_len+1,k_len-2)];
	    			++kt2[k2n((char*)sequence.s+sequence.l-k_len+2,k_len-2)];
	    		}
	    	}
	    	else
	    	{
				if(!parm.quiet)
	    			cerr<<"seq too short:"<<name<<" len:"<<sequence.l<<endl;
	    	}
	    	delete [] pseq;
	    }
    }
    else
    {
    	FILE* infile = fopen(f1, "r");
	    if (infile == NULL)
	    {
	        cerr << "open file error:" << f1 << endl;
	        exit(1);
	    }
	    while ( -1 != read_fasta(infile, &sequence, name, 0))
	    {
	    	total_seq++;
	    	if(sequence.s[sequence.l-1] == '*')//stop codon
	    	{
	    		sequence.l--;//del that '*'
	    	}
	    	for(int i=0;i<sequence.l;++i)
	    	{
	    		//map aa to regular char set, and then to int
	    		sequence.s[i]= (char)aa2int[(int)aamap[toupper(sequence.s[i])]];
	    	}

	    	if((mlong)sequence.l>=k_len)
	    	{
	    		total_tuple+=sequence.l-k_len+1;
	    		if(!parm.load_ub)
	    		{
	    			total_tuple1+=sequence.l-k_len+2;
	    			total_tuple2+=sequence.l-k_len+3;
	    		}
	    		for(mlong i=0;i<(mlong)sequence.l-k_len+1;++i)
	    		{
	    			mlong key=k2n((char*)sequence.s+i,k_len-2);
	    			if(!parm.load_ub)
	    				++kt2[key];
	    			key=key*charset_num + *(sequence.s+i+k_len-2);
	    			if(!parm.load_ub)
	    				++kt1[key];
	    			key=key*charset_num + *(sequence.s+i+k_len-1);
	    			++kt[key];
	    		}
	    		if(!parm.load_ub)
	    		{
	    			++kt1[k2n((char*)sequence.s+sequence.l-k_len+1,k_len-1)];
	    			++kt2[k2n((char*)sequence.s+sequence.l-k_len+1,k_len-2)];
	    			++kt2[k2n((char*)sequence.s+sequence.l-k_len+2,k_len-2)];
	    		}
	    	}
	    	/* we omit sequence whose length less than k_len,
	    	// otherwise, uncomment these codes
	    	else if(sequence.l>=k_len-1)
	    	{
	    		...
	    		total_tuple1++;
	    		total_tuple2+=2;
	    	}
	    	else if(sequence.l>=k_len-2)
	    	{
	    		...
	    		total_tuple2++;
	    	}
	    	*/
	    	else
	    	{
				if(!parm.quiet)
	    			cerr<<"seq too short:"<<name<<" len:"<<sequence.l<<endl;
	    	}
	    }
	    fclose(infile);
	    free (sequence.s);
	}

  	if(parm.load_ub)//input bg file
  	{
  		ifstream bfile(parm.bfile.c_str(), ios::in | ios::binary);
    	if(!bfile.good())
    	{
    		cerr<<"open input bg file error: "<<parm.bfile<<endl;
    		exit(1);
    	}
    	mlong cur_k;
    	bfile.read((char*)&cur_k, sizeof(mlong));
    	if(cur_k != k_len-1)
    	{
    		cout<<"background file is not match with current K. It is for K="<<cur_k+1<<endl;
    		exit(1);
    	}
    	cout<<"read k="<<cur_k+1<<endl;
    	bfile.read((char*)&total_tuple1, sizeof(mlong));
    	bfile.read((char*)&total_tuple2, sizeof(mlong));
    	cout<<" read total_tuple1="<<total_tuple1<<endl;
    	cout<<" read total_tuple2="<<total_tuple2<<endl;
    	mlong size=0;
    	bfile.read((char*)&size, sizeof(mlong));
    	//kt1.resize(size);
    	vector< pair<mlong,mlong> > tmp;
    	tmp.resize(size);
    	bfile.read((char*)&(*tmp.begin()), sizeof( pair<mlong,mlong> )*size);
    	//cvfile.read((char*)&(*cv.begin()), sizeof( pair<mlong,double> )*len);
    	for(mlong t=0;t<size;++t)
    	{
    		kt1[tmp[t].first]=tmp[t].second;
    		//cout<<" read: ="<<tmp[t].first<<"->"<<tmp[t].second<<endl;
    		//getchar();
    	}
    	cout<<" read size1="<<size<<endl;
    	cout<<" read first="<<tmp.begin()->first<<"->"<<tmp.begin()->second<<endl;

    	//mlong cur_k=k_len-2;
    	//bfile.write((char*)&cur_k, sizeof(mlong));
    	size=0;
    	bfile.read((char*)&size, sizeof(mlong));
    	tmp.resize(size);
    	bfile.read((char*)&(*tmp.begin()), sizeof( pair<mlong,mlong> )*size);
    	for(mlong t=0;t<size;++t)
    		kt2[tmp[t].first]=tmp[t].second;
    	cout<<" read size2="<<size<<endl;
    	cout<<" read first="<<tmp.begin()->first<<"->"<<tmp.begin()->second<<endl;

    	bfile.close();
    }

  	if(parm.Bfile.length())//output bg file
  	{
  		ofstream Bfile(parm.Bfile.c_str(), ios::out | ios::binary);
    	if(!Bfile.good())
    	{
    		cerr<<"open output bg file error: "<<parm.Bfile<<endl;
    		exit(1);
    	}
    	mlong cur_k=k_len-1;
    	cout<<" write k="<<cur_k+1<<endl;
    	Bfile.write((char*)&cur_k, sizeof(mlong));
    	Bfile.write((char*)&total_tuple1, sizeof(mlong));
    	Bfile.write((char*)&total_tuple2, sizeof(mlong));
    	cout<<" write total_tuple1="<<total_tuple1<<endl;
    	cout<<" write total_tuple2="<<total_tuple2<<endl;
    	mlong size=kt1.size();
    	Bfile.write((char*)&size, sizeof(mlong));
    	vector< pair<mlong,mlong> > tmp;
    	for(TMap::iterator t=kt1.begin();t!=kt1.end();++t)
    	{
    		tmp.push_back( *t );
    	}
    	Bfile.write((char*)&(*tmp.begin()), sizeof( pair<mlong,mlong> )*size);
    	cout<<" write size1="<<size<<endl;
    	cout<<" write first="<<kt1.begin()->first<<"->"<<kt1.begin()->second<<endl;


    	//mlong cur_k=k_len-2;
    	//Bfile.write((char*)&cur_k, sizeof(mlong));
    	size=kt2.size();
    	Bfile.write((char*)&size, sizeof(mlong));
    	tmp.clear();
    	for(TMap::iterator t=kt2.begin();t!=kt2.end();++t)
    	{
    		tmp.push_back( *t );
    	}
    	Bfile.write((char*)&(*tmp.begin()), sizeof( pair<mlong,mlong> )*size);
    	cout<<" write size2="<<size<<endl;
    	cout<<" write first="<<kt2.begin()->first<<"->"<<kt2.begin()->second<<endl;
    	//for(TMap::iterator t=kt2.begin();t!=kt2.end();++t)
    	//{
    		//cout<<" write: ="<<t->first<<"->"<<t->second<<endl;
    		//getchar();
    	//}

    	Bfile.close();
    	exit(0);
    }

    double inner=0;
    double shift=(double)total_tuple * total_tuple2 / ((double)total_tuple1*total_tuple1);
    //cout<<" calc shift="<<shift<<endl;
    if(!parm.quiet)
    	cout<<"calculate cv: ";
    TMap::iterator it=kt1.begin();
    vector< pair<mlong,mlong> > k1seeds;//store: k2_key,which char added to the end
    k1seeds.resize(charset_num);
    //if(!parm.count_only)
    //	cv.reserve(16777216*2);//16M

    mlong count_1=0,count_non0=0;

    for(;it!=kt1.end();++it)//for each every kt1, make charset_num k-tuples from each kt1
    {
    	//k2seeds.clear();
    	int nseeds=get_k1seeds(it->first,k1seeds,kt1);
    	for(int i=0;i<nseeds;++i)
    	{
    		//size_t k1l=it->first;
    		//.second save the last char in k-tuple
    		size_t k1r=k1seeds[i].first*charset_num+k1seeds[i].second;
    		size_t k2 =k1seeds[i].first;
    		size_t k  =it->first*charset_num+k1seeds[i].second;
			/* uncomment if you want use f0 outside the 'k exists' condition
			int ck1l=it->second;
			int ck1r=kt1[k1r];
			int ck2=kt2[k2];
    		double f0=(double)ck1l*ck1r/ck2*shift;
    		*/
			double V=0;

			if( kt.find(k)!=kt.end() )//k exists
    		{
    			int ck=kt[k];
				int ck1l=it->second;
				int ck1r=kt1[k1r];
				int ck2=kt2[k2];
	    		double f0=(double)ck1l*ck1r/ck2*shift;
    			// (f-f0)/f0
    			if(parm.subt)
					V =(ck-f0)/f0;
				else
					V = ck;
    		}
    		else//k not exists, but k1l and k1r do
    		{
    			if(parm.subt)
    			{
    				V =-1;
    				count_1++;
    			}
    			else
    				V =0;

    		}

    		if(V!=0)
    		{
    			count_non0++;
    			inner += V*V;
    			if(!parm.count_only)
    				cv.push_back(make_pair(k,V));
    		}
    	}
    }

    kt.clear();
    kt1.clear();
    kt2.clear();
    mlong count_0 = POW[charset_num][_k_len]-count_non0;//check if we need use file type 2
    if(!parm.quiet)
		cout<<"CV non-zero count:"<<count_non0<<"( -1 count:"<<count_1<<", 0 count:"<<count_0<<")"<<endl;

	bool use_file_type_1 = true;
	if(count_0 < count_1*2)
	{
		use_file_type_1 = false;
	}
	if(parm.cv_type==1)
	{
		cout<<"Use type 1 cv file format"<<endl;
		use_file_type_1 = true;
	}
	else if(parm.cv_type==2)
	{
		cout<<"Use type 2 cv file format"<<endl;
		use_file_type_1 = false;
	}
	else
	{
		parm.cv_type=0;
		cout<<"Auto-select cv file format"<<endl;
	}

    sort(cv.begin(),cv.end(),by_first);

    //output CV file, if filename was given
    if(parm.write_cvfile.length())
    {
    	//use this section to do real work
    	// bin out
    	string f2=parm.write_cvfile+"/"+fname+".cv";
    	if(!parm.quiet)
    		cout<<"write binary cv file: "<<f2<<endl;
    	ofstream cvfile(f2.c_str(), ios::out | ios::binary);
    	if(!cvfile.good())
    	{
    		cerr<<"open binary cv file error: "<<f2<<endl;
    		exit(1);
    	}
    	cvfile.write((char*)&k_len, sizeof(mlong));
    	cvfile.write((char*)&inner, sizeof(double));
    	mlong cvsize=cv.size();

    	if(use_file_type_1)
    	{
    		if(parm.cv_type==0)
    			cout<<"Use type 1 cv file format\n";
    		cvfile.write((char*)&cvsize, sizeof(mlong));
    		cvfile.write((char*)&(*cv.begin()), sizeof( pair<mlong,double> )*cvsize);
    	}
    	else //use file type 2
    	{
    		if(parm.cv_type==0)
    			cout<<"Use type 2 cv file format\n";
    		mlong flag=-1;
    		cvfile.write((char*)&flag, sizeof(mlong));
    		flag=charset_num;
    		cvfile.write((char*)&flag, sizeof(mlong));//write AA/DNA in cv file

    		vector< pair<mlong,double> > non0;
    		vector< mlong > is0;
    		mlong total=POW[charset_num][_k_len];
    		mlong i=0,j=0;
    		for(;i<total && j<cv.size();)
    		{
    			if(i == cv[j].first)//non0
    			{
    				if( fabs(cv[j].second+1) > 0.00000000001 )// != -1
    				{
    					non0.push_back(cv[j]);
    				}
    				++j;
    				++i;
    			}
    			else if(i < cv[j].first)//some 0 here
    			{
    				for(mlong m=i;m<cv[j].first;++m)
    				{
    					is0.push_back(m);
    				}
    				i=cv[j].first;
    			}
    			else if(i > cv[j].first)// error?
    			{
    				cerr<<"Error, in write CV\n";
    			}
    		}
    		if(i<total)
    		{
    			for(mlong m=i;m<total;++m)
    			{
    				is0.push_back(m);
    			}
    		}
    		mlong non0_size=non0.size();
    		cvfile.write((char*)&non0_size, sizeof(mlong));
    		cvfile.write((char*)&(*non0.begin()), sizeof( pair<mlong,double> )*non0_size);
    		mlong is0_size=is0.size();
    		cvfile.write((char*)&is0_size, sizeof(mlong));
    		cvfile.write((char*)&(*is0.begin()), sizeof( mlong )*is0_size);
    	}

    	cvfile.close();
    }
    else if(parm.write_txt_cvfile.length())
    {
    	// txt out
    	string filename=parm.write_txt_cvfile+"/"+fname+".cv.txt";
    	if(!parm.quiet)
    		cout<<"write txt cv file: "<<filename<<endl;;
	    ofstream ofile;
	    ofile.open(filename.c_str());
	    if(ofile.good())
	    {
	    	ofile<<k_len<<endl;
	    	ofile<<inner<<endl;
	    	ofile<<cv.size()<<endl;
	    	//ofile<<setprecision(12);
	    	for(size_t i=0;i<cv.size();++i)
	    		ofile<<cv[i].first<<" "<<cv[i].second<<endl;
	    	ofile.close();
	    }
	    else
	    {
	    	cerr<<"open txt cv file error: "<<filename<<endl;
	    	exit(1);
	    }
	}

    return inner;
}
