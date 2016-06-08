

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 * System interface, users need to implement all the four methods
 */


public abstract class ComputingSystem<INDEX,CRUDEDATA,INTERMEDIATEDATA,RESULTDATA> 
{
	Map<INDEX,CRUDEDATA> Reader()
	{
		return null;
	}
    
    INTERMEDIATEDATA Preprocess(CRUDEDATA crudedata)
    {
    	return null;
    }

    RESULTDATA Compare(INTERMEDIATEDATA intermediatedataa,INTERMEDIATEDATA intermediatedatab)
    {
    	return null;
    }

    void Writer(Matrix<INDEX,RESULTDATA> matrix)
    {
   
    }
    
    void Run()
    {
    	
    	Map<INDEX,CRUDEDATA> crudepairs;
		Map<INDEX,INTERMEDIATEDATA> intermediatepairs;
		Map<List<INDEX>,RESULTDATA> resultpairs;
		RESULTDATA resultdata;
		List<INDEX> listindex;
		Matrix<INDEX,RESULTDATA> resultmatrix;
    	
    	 /*
         * Running the Reader method providing by Users
         */
		crudepairs = Reader();

        /*
         * Running the Preprocess method providing by Users
         */
        Iterator<Entry<INDEX, CRUDEDATA>> crudeiterator = crudepairs.entrySet().iterator();
        intermediatepairs = new HashMap<INDEX,INTERMEDIATEDATA>();
            
        while(crudeiterator.hasNext())
        {
            Entry<INDEX, CRUDEDATA> entry = crudeiterator.next(); 
            intermediatepairs.put(entry.getKey(),Preprocess(entry.getValue()));
        
        }
        crudepairs=null;
        /*
         * Running Compare method providing by Users;
         */
        Iterator<Entry<INDEX,INTERMEDIATEDATA>> intermediateiterator = intermediatepairs.entrySet().iterator();
        listindex = new ArrayList<INDEX>();   
        resultpairs = new HashMap<List<INDEX>,RESULTDATA>();
        
        while(intermediateiterator.hasNext())
        {
            Entry<INDEX, INTERMEDIATEDATA> entry = intermediateiterator.next();
            listindex.add(entry.getKey());
        }
        
        for(int i=0;i<listindex.size();i++)
            for(int j=i+1;j<listindex.size();j++)
            {  	        	
        		List<INDEX> list = new ArrayList<INDEX>();
				resultdata = Compare(intermediatepairs.get(listindex.get(i)),intermediatepairs.get(listindex.get(j)));
				list.add(listindex.get(i));
                list.add(listindex.get(j));
                resultpairs.put(list,resultdata);
			}
  
        intermediatepairs=null;
        /*
         * Running Writer Method providing by Users;
         */
        resultmatrix = new Matrix<INDEX,RESULTDATA>(resultpairs,listindex);
        Writer(resultmatrix);	
    }
}