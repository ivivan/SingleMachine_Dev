#include <unistd.h>
#include "count.h"
#include "dist.h"
#include "config.h"
using namespace std;

void usage()
{
	int max_k_len=(int)(sizeof(mlong)*8*log(2)/log(20));
	int max_k_len_dna=(int)(sizeof(mlong)*8*log(2)/log(4));
	cerr<<"cvtree -i list.file -d seq.dir/ -k num -c out.cvfile.dir/ -o out.dist.file\n";
	cerr<<"VERSION: "<<XZ_VERSION<<endl;
	cerr<<"   -i  str   input list file name, contains sequences' name(without .faa/.ffn)\n";
	cerr<<"   -d  str   input sequences' dir\n";
	cerr<<"   -s  str   single input mutil-FASTA file (will ignore -i and -d)\n";
	cerr<<"   -c  str   output CV files' dir\n";
	cerr<<"   -C  str   output TXT CV files' dir(for human read)\n";
	cerr<<"   -k  num   k-tuple's length, AA range: [3,"<<max_k_len<<"], DNA range: [3,"<<max_k_len_dna<<"]\n";
	cerr<<"   -t  str   sequence type: dna or aa\n";
	cerr<<"   -S  1/0   Turn on/off subtraction(default is on)\n";
	cerr<<"   -o  str   output distance matrix filename(PHYLIP matrix)\n";
	cerr<<"   -l  str   output distance matrix filename(MEGA lower-left matrix)\n";

//hide these, only for inner testing. Copy right reversed.
//	cerr<<"   -Q        Turn on Q method(default is off)\n";
//	cerr<<"   -b  str   pre-load background file filename\n";
//	cerr<<"   -B  str   output background file filename\n";
//	cerr<<"   -a  num   cv file type, 0/1/2 means auto-choose/type1/type2, default=1 \n";

	cerr<<"   -q        be quiet\n";
	cerr<<"   -h        print help info\n";
}

int main(int argc, char* argv[])
{
	Parm parm;
	char *list=NULL,*idir=NULL,*mfile=NULL;
	mlong k_len = 0;
	bool display_usage=false;
	bool use_q_method=false;
	int output_format=0;//0-phylip 1-MEGA-lower-left 2...
	string file_ext=".faa";//default use faa

	char c;
	while ((c = getopt (argc, argv, "hqi:d:s:c:C:k:o:l:t:S:Q")) > 0) //B:b:a:
	{
		switch (c)
		{
		case 'h':
			display_usage = true;
			break;
		case 'q':
			parm.quiet = true;
			break;
		case 'S':
			parm.subt = (0!=atoi(optarg));
			break;
		case 'Q':
			use_q_method = true;
			break;
		case 's':
			parm.is_single_input = true;
			list = optarg;
			break;
		case 'i':
			if(! parm.is_single_input)
				list = optarg;
			break;
		case 'B':
			parm.Bfile = optarg;
			break;
		case 'b':
			parm.load_ub = true;
			parm.bfile = optarg;
			break;
        case 'd':
            idir = optarg;
            break;
        case 'c':
            parm.write_cvfile = optarg;
            parm.count_only=false;
            break;
        case 'C':
            parm.write_txt_cvfile = optarg;
            parm.count_only=false;
            break;
        case 'k':
            k_len = atoi(optarg);
            break;
        case 'a':
            parm.cv_type = atoi(optarg);
            break;
        case 'o':
            mfile = optarg;
            parm.calc_dist=true;
            parm.count_only=false;
            break;
        case 'l':
            mfile = optarg;
            parm.calc_dist=true;
            parm.count_only=false;
            output_format=1;//MEGA
            break;
        case 't':
            if(optarg[0] == 'd')//dna
            {
            	parm.is_dna=true;
            	file_ext=".ffn";
            }
            else
            {
            	parm.is_dna=false;
            	file_ext=".faa";
            }
            break;
        default:
            display_usage = true;
		}
	}
	mlong max_k_len=0;
	if(parm.is_dna)
		max_k_len=(mlong)(sizeof(mlong)*8*log(2)/log(4));
	else
		max_k_len=(mlong)(sizeof(mlong)*8*log(2)/log(20));

	if (  (list==NULL || (idir==NULL && !parm.is_single_input) || k_len==0)
			|| display_usage || (k_len<3 || k_len>max_k_len) )
	{
		usage();
		exit(1);
	}
	/*
	cerr<<"list:"<<list<<endl;
	cerr<<"idir:"<<idir<<endl;
	cerr<<"klen:"<<k_len<<endl;
	parm.display();
	*/
	CountH kt(parm);//use hash_map
	vector< vector< pair<mlong,double> > > cv;
	vector<double> cv_inner;
	vector<string> files;//files, or names(in single fasta file)

	//read in list first
	ifstream ifile(list);
	if(!ifile.good())
	{
		cerr<<"open list file error: "<<list<<endl;
		exit(1);
	}
	int total=0;
	int max_name_len=0;
	map < string, vector<string> > fasta_seq;
	if(parm.is_single_input)// single FASTA file
	{
		string line,name,seq;
		while(getline(ifile,line))
		{
			if( line[0]=='>' )// format:  >SeqID | Organism name
			{
				size_t pos=line.find("|");
				if( string::npos != pos )
				{
					if(seq.length() && name.length())
					{
						if(!fasta_seq.count(name))
							files.push_back(name);
						fasta_seq[name].push_back(seq);
						seq="";
					}
					name = line.substr(pos+1);
					size_t pb=name.find_first_not_of(" \t\n\r");
					size_t pe=name.find_last_not_of(" \t\n\r");
					if(pb==string::npos || pe==string::npos)
					{
						cerr<<"empty organism name: '"<<name<<"'"<<endl;
						exit(1);
					}
					name = name.substr(pb,pe-pb+1);
					for(unsigned int i=0;i<name.length();++i)
					{
						if(name[i] == ' ')
							name[i]='_';
					}
					if(max_name_len<(int)name.length())
						max_name_len=name.length();

				}
				else
				{
					cerr<<"no '|' in description line, I can't find organism name\n";
					exit(1);
				}
			}
			else
			{
				seq += line;
			}
		}
		if(seq.length() && name.length())
		{
			if(!fasta_seq.count(name))
				files.push_back(name);
			fasta_seq[name].push_back(seq);
			seq="";
		}
		total = fasta_seq.size();
	}
	else
	{
		ifile>>total;
		files.resize(total);
		string line;
		string tmpstr;
		for(int i=0;i<total;++i)
		{
			ifile>>tmpstr;
			getline(ifile,line);//trim line
			files[i]=tmpstr;
			if(max_name_len<(int)tmpstr.length())
				max_name_len=tmpstr.length();
		}
	}
	if(max_name_len<9)
		max_name_len=9;
	ifile.close();
	//cerr<<"total="<<total<<endl;
	//cerr<<"file size="<<files.size()<<endl;
	cv.resize(total);
	//cerr<<"resize cv ok\n";
	cv_inner.resize(total);
	//cerr<<"resize cv inner ok\n";
	for(int i=0;i<total;++i)
	{
		string pathname;
		if(idir)
		{
			pathname+=idir;
			pathname+="/";
		}
		pathname+=files[i]+file_ext;

		if(!parm.quiet)
			cout<<"["<<i+1<<"/"<<total<<"]"<<endl;

		if(!parm.calc_dist || parm.count_only)
		{
			int zero=0;
			cv_inner[zero]=kt.count_tuple(pathname.c_str(),files[i],k_len,cv[zero],fasta_seq[files[i]]);
			cv[zero].clear();
		}
		else
			cv_inner[i]=kt.count_tuple(pathname.c_str(),files[i],k_len,cv[i],fasta_seq[files[i]]);
	}

	if(mfile==NULL || !parm.calc_dist)
		return 0;//program end, if do not need matrix file.

	//count dist
	cout<<"calculate distance pairwise\n";
	vector< vector<double> > dist;
	dist.resize(total);
	for(int i=0;i<total;++i)
	{
		dist[i].resize(total);
		dist[i][i]=0;//init diagonal
	}
	//calculate symmetrical dist matrix
	for(int i=0;i<total;++i)
		for(int j=i+1;j<total;++j)
			dist[j][i]=dist[i][j]=count_dist(cv[i],cv[j],cv_inner[i],cv_inner[j],use_q_method);

	ofstream ofile;
	ofile.open(mfile);
	streambuf *cout_buf=cout.rdbuf();
	if(!ofile.good())
	{
		cerr<<"open matrix file: '"<<mfile<<"' error, ";
		//try a lot time to compute the matrix but die here? no way!
		cerr<<"change output to stdout.\n";
	}
	else
	{
		cout<<"write matrix file: "<<mfile<<endl;
		cout.rdbuf(ofile.rdbuf());
	}
	if(output_format==0)
	{
		cout<<total<<endl;
		for(int i=0;i<total;++i)
		{
			cout<<left<<setw(max_name_len)<<files[i];
			for(int j=0;j<total;++j)
			{
				cout<<" "<<fixed<<setprecision(10)<<dist[i][j];
			}
			cout<<endl;
		}
	}
	else if(output_format==1)
	{
		cout<<"#mega"<<endl;
		cout<<"Title: Lower-left triangular matrix"<<endl<<endl;
		for(int i=0;i<total;++i)
			cout<<"#"<<files[i]<<endl;
		cout<<endl;
		for(int i=1;i<total;++i)
		{
			for(int j=0;j<i;++j)
				cout<<fixed<<setprecision(10)<<dist[i][j]<<" ";
			cout<<endl;
		}
	}
	else
	{
		cerr<<"unkown output format!\n";
		exit(1);
	}
	cout.rdbuf(cout_buf);
	ofile.close();
	return 0;
}
/*
//cv file format(binary file):
        k_len (mlong)
        inner (double)
        vector_len (mlong)
        index1(mlong) ele1(double)
        index2(mlong) ele2    .
        ...     .    ...     .
        indexN  .    eleN    .
*/
