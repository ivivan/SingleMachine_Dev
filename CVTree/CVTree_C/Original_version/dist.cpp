#include <string>
#include <time.h>
#include <iostream>
#include <fstream>
#include <iomanip>
#include <unistd.h>
#include "dist.h"
#include "config.h"
using namespace std;

void usage()
{
	cerr<<"dist -d db_file1.cv,db_file2.cv q_file1.cv q_file2.cv > distance \n";
	cerr<<"VERSION: "<<XZ_VERSION<<endl;
	cerr<<"   -n  num   number of query CVs, and load query CV from stdin\n";
	cerr<<"   -d  str   load database CV, seperate by ','\n";
	cerr<<"   -c        calculate distance between database CVs\n";
	cerr<<"   -i        input two file(db_file and q_file) from stdin\n";
	cerr<<"             note: -s,-d,-c,-n will be ignored\n";
	cerr<<"   -s        output f1-f2_k.xls file for statistics\n";
	cerr<<"   -f  str   statistics file's name, used with -i(coz -s will be disabled)\n";
	cerr<<"   -t  str   sequence type: dna or aa (affect -s/-f)\n";
	cerr<<"   -h        print help info\n";
	cerr<<"   examples:\n";
	cerr<<"       dist -d file1.cv file2.cv\n";
	cerr<<"       cat file1.cv file2.cv | dist -i\n";
	cerr<<"       cat qfile1 qfile2 | dist -d dfile1,dfile2 -n2\n";

}

int main(int argc, char* argv[])
{
	vector<string> dfiles;
	vector<string> qfiles;
	bool calc_self=false;
	bool do_stat=false;
	bool display_usage=false;
	bool read_stdin_only=false;
	bool is_dna=false;
	int query_num=0;
	char* osfile=NULL;
	bool qli_meth = false;

	char c;
	while ((c = getopt (argc, argv, "hcsin:d:f:t:")) > 0)
	{
		switch (c)
		{
		case 'h':
			display_usage = true;
			break;
		case 'n':
			query_num = atoi (optarg);
			break;
        case 'c':
            calc_self=true;
            break;
        case 's':
            do_stat=true;
            break;
        case 'f':
            osfile=optarg;
            break;
        case 'i':
            read_stdin_only=true;
            break;
        case 'd':
            dfiles=tokenize(optarg,",");
            break;
        case 't':
            if(optarg[0] == 'd')//dna
            {
            	is_dna=true;
            }
            else
            {
            	is_dna=false;
            }
            break;
		case 'q':
		    qli_meth = true;
		    break;
        default:
            display_usage = true;
		}
	}
	if (  (optind>=argc  //no more argument in command line
			&& query_num==0 //no input from stdin
			&& !read_stdin_only //no input from stdin
			&& (!dfiles.size() || !calc_self)) //no dbfiles || needn't self calculate between dbfiles
			|| display_usage)
	{
		usage();
		exit(1);
	}

	if(query_num==0)//read from command line
	{
		for(int i=optind;i<argc;++i)
			qfiles.push_back(argv[i]);
	}
	else//read from stdin
	{
		if(optind<argc)
		{
			cerr<<"Warn: find -n option, q_files in command line will be ignored.\n";
		}
		qfiles.reserve(query_num);
		for(int i=0;i<query_num;++i)
			qfiles.push_back("/dev/stdin");
	}

	if(read_stdin_only)
	{
		dfiles.clear();
		qfiles.clear();
		do_stat=false;
		calc_self=true;
		dfiles.push_back("/dev/stdin");
		dfiles.push_back("/dev/stdin");
		if(osfile!=NULL)
		{
			do_stat=true;
		}
	}

	vector< vector< pair<mlong,double> > > dbcv(dfiles.size());
	vector< double > dbinner(dfiles.size());
	mlong dbk_len;
	for(size_t i=0;i<dfiles.size();++i)
		read_cv(dfiles[i],dbcv[i],dbk_len,dbinner[i]);
	if(calc_self)//do db files pairwise
	{
		for(size_t i=0;i<dfiles.size();++i)
		{
			for(size_t j=i+1;j<dfiles.size();++j)
			{
				cout<<dfiles[i]<<"\t"<<dfiles[j]<<"\t";
				cout<<fixed<<setprecision(10)<<
				    count_dist(dbcv[i],dbcv[j],dbinner[i],dbinner[j],qli_meth)<<endl;
				if(do_stat)
					stat_dist(dbcv[i],dbcv[j],dfiles[i],dfiles[j],dbk_len,osfile,is_dna);//do statistics work
			}
		}
	}

	mlong qk_len=0;
	for(size_t i=0;i<qfiles.size();++i)
	{
		double qinner;
		vector< pair<mlong,double> > qcv;
		read_cv(qfiles[i],qcv,qk_len,qinner);
		for(size_t j=0;j<dfiles.size();++j)
		{
			if(qk_len!=dbk_len)
		    {
		    	cerr<<"different K_len: qk_len="<<qk_len<<", dbk_len="<<dbk_len<<endl;
		    	exit(1);
		    }

		    if(query_num!=0)//get qfiles from stdin
				cout<<"stdin_"<<i+1<<"\t"<<dfiles[j]<<"\t";
			else
				cout<<qfiles[i]<<"\t"<<dfiles[j]<<"\t";

			cout<<fixed<<setprecision(10)<<
			    count_dist(qcv,dbcv[j],qinner,dbinner[j], qli_meth)<<endl;

			if(do_stat)
			{
				string qstring=qfiles[i];
				char buf[64];
				sprintf(buf,"_%ld",(mlong)i);
				qstring+=buf;
				stat_dist(qcv,dbcv[j],dfiles[j],qstring,qk_len,osfile,is_dna);//do statistics work
			}
		}
	}
	return 0;
}
