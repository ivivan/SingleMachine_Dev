import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.Math;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;



/*
 * Users write this class
 * @INDEX TYPE: String;
 * @CRUDEDATA TYPE: String;
 * @INTERMEDIATEDATA TYPE: List<Object>;
 * @RESULTDATA TYPE: Double;
 */

public class Cvtreesystem extends ComputingSystem<String,String,List<Object>,Double>
{
	/*
	 * Read all the files in folder hdfs://192.168.11.188:9000/root/cvtree/input1  and generate a Map saving all the data;
	 * the keys are file name, the values are the whole file content
	 * @see ComputingSystem#Reader()
	 */
	
	public HashMap<String,String> Reader()
	{
		Configuration conf = new Configuration();
		HashMap<String,String> pairsforpreprocess = new HashMap<String,String>();
		try {
		
		FileSystem fs = FileSystem.get(conf);
		FSDataInputStream hdfsInStream;
		FileStatus[] stats = fs.listStatus(new Path("hdfs://192.168.11.188:9000/root/cvtree/input1"));
		String[] filename = new String[stats.length];
		
		for(int i = 0; i < stats.length; ++i)
		{
			filename[i]=stats[i].getPath().getName();
			hdfsInStream = fs.open(stats[i].getPath());
			byte[] buffer = new byte[Integer.parseInt(String.valueOf(stats[i].getLen()))];
	        hdfsInStream.readFully(0, buffer);
	        String wholeContent = new String(buffer);
			pairsforpreprocess.put(filename[i],wholeContent);
			fs.close();    
		}
		
	    	} catch (IOException e) {
				e.printStackTrace();
			}
		return pairsforpreprocess;
	}	 
	
    /*
     * the following methods are belonging to the Preprocess part, including the Preprocess method and other methods
     * other methods are used to do the preprocess;
     */
    
	void Init()
	{
		M2 = 1;
		for (int i=0; i<LEN-2; i++)	// M2 = AA_NUMBER ^ (LEN-2);
			M2 *= AA_NUMBER; 
		M1 = M2 * AA_NUMBER;		// M1 = AA_NUMBER ^ (LEN-1);
		M  = M1 *AA_NUMBER;			// M  = AA_NUMBER ^ (LEN);
	}
	
    void InitVectors()
	{
		vector = new int[M];
		second = new int[M1];
		one_l= new int[AA_NUMBER];
		total = 0;
		total_l = 0;
		complement = 0;
	}
    
    void init_buffer(char[] buffer)
	{
		complement++;
		indexs = 0;
		for (int i=0; i<LEN-1; i++)
		{
			int enc = encode(buffer[i]);
			one_l[enc]++;
			total_l++;
			indexs = indexs * AA_NUMBER + enc;
		}
		second[indexs]++;
	}
    
    void cont_buffer(char ch)
	{
		int enc = encode(ch);
		one_l[enc]++;
		total_l++;
		int index = indexs * AA_NUMBER + enc;
		vector[index]++;
		total++;
		indexs = (indexs % M2) * AA_NUMBER + enc;
		second[indexs]++;
	} 
    
    int encode(char ch)
    {
    	return code[ch-'A'];
    }
    
    /*
     * Preprocess method, generate a list including all the values, values can be different type;
     * @see ComputingSystem#Preprocess(java.lang.Object)
     */
    public List<Object> Preprocess(String content)
    {
    	Init();
        InitVectors();
        StringReader in = new StringReader(content);
		
		
		try
		{
        int ch=0;
        
        
		while ((ch = in.read())!=-1)
		{
			if (ch == '>')
			{
				while ((in.read()) != '\n'); // skip rest of line
				char[] buffer = new char[LEN-1];
				in.read(buffer,0,LEN-1);
				init_buffer(buffer);
			}
			else if (ch != '\r' && ch != '\n')
				cont_buffer((char)ch);
		}
		in.close();
		}
		catch(IOException e)
		{
			e.printStackTrace(); 
		}
		
        
        int total_plus_complement = total + complement;
		double total_div_2 = total * 0.5;
		int i_mod_aa_number = 0;
		int i_div_aa_number = 0;
		int i_mod_M1 = 0;
		int i_div_M1 = 0;

		double[] one_l_div_total = new double[AA_NUMBER];
		for (int i=0; i<AA_NUMBER; i++)
			one_l_div_total[i] = (double)one_l[i] / total_l;
		
		double[] second_div_total = new double[M1];
		for (int i=0; i<M1; i++)
			second_div_total[i] = (double)second[i] / total_plus_complement;

		
		second = null;
		count = 0;
		double[] t = new double[M];

		for(int i=0; i<M; i++)
		{
			double p1 = second_div_total[i_div_aa_number];
			double p2 = one_l_div_total[i_mod_aa_number];
			double p3 = second_div_total[i_mod_M1];
			double p4 = one_l_div_total[i_div_M1];
			double stochastic =  (p1 * p2 + p3 * p4) * total_div_2;

			if (i_mod_aa_number == AA_NUMBER-1)
			{
				i_mod_aa_number = 0;
				i_div_aa_number++;
			}
			else
				i_mod_aa_number++;

			if (i_mod_M1 == M1-1)
			{
				i_mod_M1 = 0;
				i_div_M1++;
			}
			else
				i_mod_M1++;

			if (stochastic > EPSILON) 
			{
				t[i] = (vector[i] - stochastic) / stochastic;
				count++;
			}
			else
				t[i] = 0;
		}
		

		second_div_total=null;
		vector=null;
		
		tv = new double[count];
		ti = new int[count];

		int pos = 0;
		for (int i=0; i<M; i++)
		{
			if (t[i] != 0)
			{
				tv[pos] = t[i];
				ti[pos] = i;
				pos++;
			}
		}
		
		t=null;
        
        List<Object> intermediateData = new ArrayList<Object>();
        
        intermediateData.add(count);
        for(int m=0;m<count;m++)
            intermediateData.add(tv[m]);
        for(int n=0;n<count;n++)
            intermediateData.add(ti[n]);
    
        return intermediateData;  
	}
    
    
    /*
     * the following methods are belonging to the Compare part; 
     * first few methods define how to deal the List and get separate values;
     */

    int getcount(List<Object> list)
    {
        int thecount;
        thecount = (int)list.get(0);
        return thecount;
    }

    double[] gettv(List<Object> list)
    {
        int thecount;
        thecount = getcount(list);
        double[] tv = new double[thecount];
    
        for(int i=0;i<thecount;i++)
            tv[i]=(double)list.get(i+1);
        return tv;
    }

    int[] getti(List<Object> list)
    {
        int thecount;
        thecount = getcount(list);
        int[] ti = new int[thecount];
    
        for(int i=0;i<thecount;i++)
            ti[i]=(int)list.get(i+thecount+1);
        return ti;
    }

    /*
     * Compare method, compare two different intermediate value and get the result
     * @see ComputingSystem#Compare(java.lang.Object, java.lang.Object)
     */
    public Double Compare(List<Object> intermediatedataa,List<Object> intermediatedatab)
    {
        counta=getcount(intermediatedataa);
        countb=getcount(intermediatedatab);
        tva=gettv(intermediatedataa);
        tvb=gettv(intermediatedatab);
        tia=getti(intermediatedataa);
        tib=getti(intermediatedatab);
        
        double correlation = 0;
	    double vector_len1=0;
	    double vector_len2=0;
	    int p1 = 0;
	    int p2 = 0;
        
	while (p1 < counta && p2 < countb)
	{
		int n1 = tia[p1];
		int n2 = tib[p2];
		if (n1 < n2)
		    {
			    double t1 = tva[p1];
		    	vector_len1 += (t1 * t1);
		    	p1++;
		    }
		else if (n2 < n1)
		{
			double t2 = tvb[p2];
			p2++;
			vector_len2 += (t2 * t2);
		}
		else
		{
			double t1 = tva[p1++];
			double t2 = tvb[p2++];
			vector_len1 += (t1 * t1);
			vector_len2 += (t2 * t2);
			correlation += t1 * t2;
		}
	}
	while (p1 < counta)
	{
		double t1 = tva[p1++];
		vector_len1 += (t1 * t1);
	}
	while (p2 < countb)
	{
		double t2 = tvb[p2++];
		vector_len2 += (t2 * t2);
	}

	return correlation / (Math.sqrt(vector_len1) * Math.sqrt(vector_len2));
    }

    /*
     * Users use Writer function to operate the final result
     * here just print all the index and result;
     * @see ComputingSystem#Writer(Matrix)
     */
    
    public void Writer(Matrix<String,Double> matrix)
    {
       
    	Map<List<String>,Double> resultpairs = matrix.Getallresult();
    	
    	Iterator<Entry<List<String>, Double>> resultiterator = resultpairs.entrySet().iterator();
 	    
 	    while(resultiterator.hasNext())
 	    {
 	        Map.Entry<List<String>, Double> entry = resultiterator.next();
 	        
 	        System.out.println("Key:"+entry.getKey().toString());
 	        System.out.println("Value:"+entry.getValue().toString());
 	        
 	    }
     
    }
    
    int number_bacteria;
    char[][] bacteria_name;
    int M, M1, M2;
    static int code[] = new int[] {0, 2, 1, 2, 3, 4, 5, 6, 7, -1, 8, 9, 10, 11, -1, 12, 13, 14, 15, 16, 1, 17, 18, 5, 19, 3};
    private static final int LEN=6;
    private static final int AA_NUMBER=20;
    private static final double EPSILON=1e-010;
    int counta;
    double[] tva;
    int[] tia;
    int countb;
    double[] tvb;
    int[] tib;  
    int[] vector;
	int[] second;
	int[] one_l;
	int indexs;
	int total;
	int total_l;
	int complement;
    int count;
	double[] tv;
	int[] ti;
    List<Object> intermediateData;
}
