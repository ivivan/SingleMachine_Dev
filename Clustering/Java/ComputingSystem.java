
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/*
 * System interface, users need to implement all the four methods
 */


public class ComputingSystem<INDEX,CRUDEDATA,INTERMEDIATEDATA,RESULTDATA> 
{
	Map<INDEX,CRUDEDATA> Reader() throws IOException
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

    void Writer(Matrix<INDEX,RESULTDATA> matrix, int taskid)
    {
   
    }
    
    void Run(List<File> lista, List<File> listb, int TaskID, String indexname) throws IOException
    {
    	
    	Map<INDEX,CRUDEDATA> crudepairs;
		Map<INDEX,INTERMEDIATEDATA> intermediatepairs;
		Map<List<INDEX>,RESULTDATA> resultpairs;
		RESULTDATA resultdata;
		List<INDEX> listindex;
		Matrix<INDEX,RESULTDATA> resultmatrix;
		
		List<String> listaname = new ArrayList<String>();
		List<String> listbname = new ArrayList<String>();
		
		for(int i=0;i<lista.size();i++)
			listaname.add(lista.get(i).getName());
		
		for(int j=0;j<listb.size();j++)
			listbname.add(listb.get(j).getName());
		
		
		
		
		
		
		
		
		
    	
    	 /*
         * Running the Reader method providing by Users
         */
		crudepairs = Reader();
		
//		System.out.println(crudepairs.size());

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
        
       System.out.println(intermediatepairs.size());
      
        
        
        
        
        
        
        /*
         * Running Compare method providing by Users;
         */
        Iterator<Entry<INDEX,INTERMEDIATEDATA>> intermediateiterator = intermediatepairs.entrySet().iterator();
        listindex = new ArrayList<INDEX>();   
        resultpairs = new HashMap<List<INDEX>,RESULTDATA>();
        
    //    System.out.println(intermediatepairs.size());
        
//        while(intermediateiterator.hasNext())
//        {
//            Entry<INDEX, INTERMEDIATEDATA> entry = intermediateiterator.next();
//            listindex.add(entry.getKey());
//       //     System.out.println(entry.getValue());
//            System.out.println("!!!!!!!!!!!!");
//        }

     //       		resultdata = Compare(intermediatepairs.get(listindex.get(1)),intermediatepairs.get(listindex.get(0)));
            		
            		resultdata = Compare(intermediatepairs.get(indexname),intermediatepairs.get(indexname+"1"));
            		
            		System.out.println("dddd");
            		
            		System.out.println(resultdata);

   
        
        
  
        intermediatepairs=null;
        /*
         * Running Writer Method providing by Users;
         */
 //       resultmatrix = new Matrix<INDEX,RESULTDATA>(resultpairs,listindex);
  //      Writer(resultmatrix,TaskID);
 //       resultmatrix=null;
    }
}