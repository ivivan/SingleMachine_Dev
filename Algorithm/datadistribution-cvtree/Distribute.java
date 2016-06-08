import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Distribute {
	
	
	/*
	 * copy file
	 */
	long forChannel(File f1,File f2) throws Exception{   
        long time=new Date().getTime();   
        

        int length=2097152;   
         FileInputStream in=new FileInputStream(f1);   
         FileOutputStream out=new FileOutputStream(f2);   
         FileChannel inC=in.getChannel();   
         FileChannel outC=out.getChannel();   
         ByteBuffer b=null;   
        while(true){   
            if(inC.position()==inC.size()){   
                 inC.close();   
                 outC.close();   
                return new Date().getTime()-time;   
             }   
            if((inC.size()-inC.position())<length){   
                 length=(int)(inC.size()-inC.position());   
             }else  
                 length=2097152;   
             b=ByteBuffer.allocateDirect(length);   
             inC.read(b);   
             b.flip();   
             outC.write(b);   
             outC.force(false);   
         }   
     }  
	
	/* files for each node, node start from 3 (same as HPC), file represents as 1,2,3 */
	HashMap<Integer,ArrayList<Integer>> generatedistributioninfo(ArrayList<ArrayList<Integer>> showdata){
		
		
		HashMap<Integer,ArrayList<Integer>> distributioninfo = new HashMap<Integer,ArrayList<Integer>>();
		
		for(int i=0;i<showdata.size();i++)
			distributioninfo.put(i+3, showdata.get(i));
		
		return distributioninfo;
		
	}
	
	/*divide all the data to M parts, 03 folder store files allocated to Node 3 
	 * all the data save in folder All, each node has a folder as 03, 04
	 * FileList contains all the file name and ordered. Therefore, the order will not be changed and can be connected with 1 ,2 ,3*/
	void ditributionfiles(HashMap<Integer,ArrayList<Integer>> distributioninfo, List<String> FileList, Test testone){
		long time;
		
		
			Iterator<Integer> iterator = distributioninfo.keySet().iterator(); 			
	        
	        while(iterator.hasNext()) { 
	  
	        	Integer key = iterator.next();
	        	
	        	for(int j=0;j<distributioninfo.get(key).size();j++)
	        		
	        	{        		
	        		try {	        			
	        			time=testone.forChannel(new File("/home/ivan/Documents/All/"+FileList.get(distributioninfo.get(key).get(j))),new File("/home/ivan/Documents/0"+key+"/"+FileList.get(distributioninfo.get(key).get(j))));
		    		} catch (Exception e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}	        		
	        	}	   
	        }		
		}
		
	
	/* save tasklist, each line contains all the tasks belongs to one node,
	 *  1 3 4 5  means task 1*3 task 4*5
	 */
	   public void SaveTasks(ArrayList<ArrayList<ArrayList<Integer>>> showcomparisons, String path)
	    {

	    	File filename; 
	    	FileWriter fw = null;
	    	
	    	try {

	    		filename = new File(path);
	    		filename.createNewFile();  		
	    		fw = new FileWriter(path);  		
	    		
	    		for(int i=0;i<showcomparisons.size();i++)
	    		{	
	    			for(int j=0;j<showcomparisons.get(i).size();j++)
	    			{
	    				for(int z=0;z<showcomparisons.get(i).get(j).size();z++)
	    				{
	    					fw.write(showcomparisons.get(i).get(j).get(z).toString()+" ");
	    				}
	    			}
	    			fw.write("\n");			
	    		}	  	
	    		fw.close();		    
	    	  } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	    }
	
	   
	   /*
	    * save filename for all the input files and ordered. only name
	    */
	List<String> SaveFileList(String inputpath)
	{
		
		File inputfiles = new File(inputpath);
        String[] filename = inputfiles.list();
        
        List<String> filenamelist = java.util.Arrays.asList(filename);
        Collections.sort(filenamelist);
        
        return filenamelist;	
		
	}
	
	
	/*
	 * save all the file info for all the input files, save as /xx/xx/xx.txt and ordered
	 * now never use
	 */
/*	List<File> SaveFile(String inputpath)
	{
		
		File inputfiles = new File(inputpath);
        File[] filename = inputfiles.listFiles();
        
        List<File> filelist = java.util.Arrays.asList(filename);
        Collections.sort(filelist);
        
        return filelist;
	}*/
	
	
	/*
	 * 
	 */
	
	  HashMap<Integer,List<List<File>>> uncodetaskmapimproveforfile(HashMap<Integer,String> taskmap,List<File> FileList ){
			
			HashMap<Integer,List<List<File>>> uncodedtaskmap = new HashMap<Integer,List<List<File>>>();
			

			Iterator<Integer> iterator = taskmap.keySet().iterator(); 
	               
	        while(iterator.hasNext()) { 
	        	
	        	
	        	
	        	List<File> stringtolist = new ArrayList<File>();
	        	List<List<File>> splitlist = new ArrayList<List<File>>();
	        	
	        	Integer key = iterator.next();
	        
			    String[] strarray=taskmap.get(key).split(" ");   
			    for (int i = 0; i < strarray.length; i++)   
			        stringtolist.add(FileList.get(Integer.parseInt(strarray[i]))); 
			    
			    splitlist=splitList(stringtolist, 2);
			    

			
			    uncodedtaskmap.put(key,splitlist);
			
		}
	        
		return uncodedtaskmap;
		}
	
	
	  /*
	   * save all the file, with path
	   * no use here
	   */
	/*  void FilesRecorder(List<File> filelist, String path)
		{
			
			File filename; 
	    	FileWriter fw = null;
	    	
	    	try {

	    		filename = new File(path);
	    		filename.createNewFile();  		
	    		fw = new FileWriter(path);  		
	    		
	    		for(int i=0;i<filelist.size();i++)
	    		{
	    			fw.write(filelist.get(i)+"\n");	
	    		}
	  	
	    		fw.close();	
	    
	    	  } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}*/
	

	
	
	/*
	 * generate a file include all the file (each file name one line)
	 */
	
	
	void FilesRecord(List<String> filenamelist, String path)
	{
		
		File filename; 
    	FileWriter fw = null;
    	
    	try {

    		filename = new File(path);
    		filename.createNewFile();  		
    		fw = new FileWriter(path);  		
    		
    		for(int i=0;i<filenamelist.size();i++)
    		{
    			fw.write(filenamelist.get(i)+"\n");	
    		}
  	
    		fw.close();	
    
    	  } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	

	/*
	 * read tasklist and save it in a map,like<0, 1 2 4 5 6 >
	 * means node 0, have tasks 1*2 4*5 ...   
	 */
	HashMap<Integer,String> ReadTaskmap(String path)
	{
	
		FileReader reader;
		HashMap<Integer,String> taskmap = new HashMap<Integer,String>();
	
		try {
			
			int i=0;
			reader = new FileReader(path);
			BufferedReader br = new BufferedReader(reader);
		    String s = null;
		    while((s = br.readLine()) != null) {	
		    	
		    	taskmap.put(i, s);
		    	i++;	
		    	
		  }			
		 br.close();
		 reader.close();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskmap;		
	}
	
	/*
	 * split list method, useful
	 */
	  public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
	        
	        int listSize = list.size();                                                          
	        int page = (listSize + (pageSize-1))/ pageSize;                     
	        
	        List<List<T>> listArray = new ArrayList<List<T>>();         
	        for(int i=0;i<page;i++) {                                                  
	            List<T> subList = new ArrayList<T>();                               
	            for(int j=0;j<listSize;j++) {                                                 
	                int pageIndex = ( (j + 1) + (pageSize-1) ) / pageSize;   
	                if(pageIndex == (i + 1)) {                                               
	                    subList.add(list.get(j));                                               
	                }
	                
	                if( (j + 1) == ((i + 1) * pageSize) ) {                               
	                    break;
	                }
	            }
	            listArray.add(subList);                                                         
	        }
	        return listArray;
	    }
	
	
	/*
	 * turn task list from number to file name, each file pairs represent one task, each line for one node
	 */
	
	  HashMap<Integer,List<List<String>>> uncodetaskmapimprove(HashMap<Integer,String> taskmap,List<String> FileList ){
			
			HashMap<Integer,List<List<String>>> uncodedtaskmap = new HashMap<Integer,List<List<String>>>();
			

			Iterator<Integer> iterator = taskmap.keySet().iterator(); 
	               
	        while(iterator.hasNext()) { 	        		        	
	        	
	        	List<String> stringtolist = new ArrayList<String>();
	        	List<List<String>> splitlist = new ArrayList<List<String>>();
	        	
	        	Integer key = iterator.next();
	        
			    String[] strarray=taskmap.get(key).split(" ");   
			    for (int i = 0; i < strarray.length; i++)   
			        stringtolist.add(FileList.get(Integer.parseInt(strarray[i]))); 
			    
			    splitlist=splitList(stringtolist, 2);	
			    uncodedtaskmap.put(key,splitlist);
			
		}       
		return uncodedtaskmap;
		}
  
	  
	
	/*
	 * turn task map from number to file name; while all the file name still in the same line for each node;
	 * no use here
	 */
	
/*	HashMap<Integer,List<String>> uncodetaskmap(HashMap<Integer,String> taskmap,List<String> FileList ){
		
		HashMap<Integer,List<String>> uncodedtaskmap = new HashMap<Integer,List<String>>();
		

		Iterator<Integer> iterator = taskmap.keySet().iterator(); 
               
        while(iterator.hasNext()) { 
      	
        	List<String> stringtolist = new ArrayList<String>();       	
        	Integer key = iterator.next();
        
		    String[] strarray=taskmap.get(key).split(" ");   
		    for (int i = 0; i < strarray.length; i++)   
		        stringtolist.add(FileList.get(Integer.parseInt(strarray[i]))); 
		
		    uncodedtaskmap.put(key, stringtolist);		
	}       
	return uncodedtaskmap;
	}*/
	

	
	public static void main(String[] args){
		
		ArrayList<ArrayList<ArrayList<Integer>>> showcomparisons;
		ArrayList<ArrayList<Integer>> showdata;
		ArrayList<Integer> shownumbersofcomparison;
		ArrayList<Integer> shownumbersofdata;
		HashMap<Integer,ArrayList<Integer>> distributioninfo;
		
		List<String> FileList;
		
		HashMap<Integer,String> Taskmap;
		
		HashMap<Integer,List<String>> UncodeTaskmap;
		
		HashMap<Integer,List<List<String>>> UncodeTaskmapimprove;
		HashMap<Integer,List<List<File>>> Uncodetaskmapimproveforfile;
		
//		List<File> Filelisttwo;
		
		
		int min;
		
		int M=4;
		
		Test testone = new Test();
		long time=0;
		
		String path = "/home/ivan/Downloads/AllTasks.txt";
	

		
		
		ImprovedAlgrithm improvealgrithm = new ImprovedAlgrithm();
		
		improvealgrithm.Runit();
		
		showcomparisons=improvealgrithm.getshowcomparisons();
		showdata=improvealgrithm.getshowdata();
		shownumbersofcomparison=improvealgrithm.getshownumbersofcomparison();
		shownumbersofdata=improvealgrithm.getshownumbersofdata();
		min=improvealgrithm.getmaxnumberamongnodes();
		
		
	//	System.out.print(showdata.size());
		
		
		distributioninfo=testone.generatedistributioninfo(showdata);
		
		
		
		
		
		
	
		
		
		System.out.println("max number among nodes");
		System.out.println(min);
		
		
		System.out.println("numbers of data in each node");
		for(int i=0;i<M;i++)
			System.out.println(shownumbersofdata.get(i));
		
		System.out.println("numbers of comparisons in each node");
		for(int i=0;i<M;i++)
			System.out.println(shownumbersofcomparison.get(i));
		
		
		System.out.println("data details for each node");
		for(int i=0;i<M;i++)
		{
			System.out.println("___________");
			for(int j=0;j<showdata.get(i).size();j++)
			System.out.println(showdata.get(i).get(j));
		}
			
		
		System.out.println("compare details for each node");
		
		
		for(int i=0;i<M;i++)
		{
			System.out.println(" ");
			System.out.println("the allocated comparisons to node"+i);
			for(int j=0;j<showcomparisons.get(i).size();j++)
			{
				System.out.println("   ");
				for(int z=0;z<showcomparisons.get(i).get(j).size();z++)
					System.out.print(showcomparisons.get(i).get(j).get(z)+"   ");
			}		
		}	
		
		
		
		
		
		
		
		testone.SaveTasks(showcomparisons, path);
		
		
		Taskmap=testone.ReadTaskmap(path);
		
		
		System.out.println("showmap:");
		
		
		System.out.println(Taskmap.get(3));
		
		
		
		System.out.println(" ");
		FileList=testone.SaveFileList("/home/ivan/Documents/All");
		
//		Filelisttwo=testone.SaveFile("/home/ivan/Documents/All");
		
		
		testone.ditributionfiles(distributioninfo,FileList, testone);
		
		testone.FilesRecord(FileList, "/home/ivan/Documents/allfiles.txt");
		
//		testone.FilesRecorder(Filelisttwo, "/home/ivan/Documents/allfilestwo.txt");
		
		for(int i=0;i<FileList.size();i++)
			System.out.println(FileList.get(i));
		
		System.out.println(FileList.size());
		

		
		
		
		UncodeTaskmap=testone.uncodetaskmap(Taskmap, FileList);
		
		
		for(int i=0;i<UncodeTaskmap.get(3).size();i++)
			System.out.println(UncodeTaskmap.get(3).get(i));
		
		
		
		UncodeTaskmapimprove=testone.uncodetaskmapimprove(Taskmap, FileList);
		
//		System.out.println(UncodeTaskmapimprove.get(2).size());
		
		
		
		for(int i=0;i<UncodeTaskmapimprove.get(3).size();i++)
		{
			
			for(int j=0;j<UncodeTaskmapimprove.get(3).get(i).size();j++)
			{
				
					System.out.print(UncodeTaskmapimprove.get(3).get(i).get(j)+"   ");
			}	
		}
		
	
		
/*		Uncodetaskmapimproveforfile=testone.uncodetaskmapimproveforfile(Taskmap, Filelisttwo);
		
		
		for(int i=0;i<Uncodetaskmapimproveforfile.get(3).size();i++)
		{
			
			for(int j=0;j<Uncodetaskmapimproveforfile.get(3).get(i).size();j++)
			{
				
					System.out.print(Uncodetaskmapimproveforfile.get(3).get(i).get(j)+"   ");
			}	
		}*/
		
		
		
		
		

		
		
		
		
		
		
		
		
		
		
	/*
	 * run linux scripts to scp files to HPC	
	 */
		


	String commands = "/home/ivan/Downloads/sample.script";
	String line = "";     
	
	Process process=null;
	try {
		process = Runtime.getRuntime().exec(commands);
	
        
	           
	InputStreamReader ir=new
	InputStreamReader(process.getInputStream());
	           
	BufferedReader input = new BufferedReader (ir);
	           

	           
	while ((line = input.readLine ()) != null){
	               
	System.out.println(line);
	       
	
	
	}
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	System.out.println("");
	System.out.println("Files are distributed");
	
	
}
	
	
	
}