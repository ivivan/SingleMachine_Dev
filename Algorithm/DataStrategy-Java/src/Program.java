import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;




public class Program {
	
	
	static LinkedHashMap<Integer,ArrayList<Integer>> initialmap(int n)
	{
		int t=0;

		ArrayList<Integer> comparisonitem;
		
		LinkedHashMap<Integer,ArrayList<Integer>> initialmap = new LinkedHashMap<Integer,ArrayList<Integer>>();
		
		
		
		for(int j=0;j<n;j++)
			for(int k=j+1;k<n;k++)
			{
				
				comparisonitem=new ArrayList<Integer>();
				comparisonitem.add(j);
				comparisonitem.add(k);
				initialmap.put(t,comparisonitem);
				t++;
			}
		
		return initialmap;
	}
	
	
	static ArrayList<Integer> generaterandomnumbers(int m,int n)
	{
		int index;
		Random randomGenerator = new Random();
		ArrayList<Integer> randomnumbers = new ArrayList<Integer>();
		for(int i=0;i<m;i++)
		{
			index = randomGenerator.nextInt(n*(n-1)/2);
			randomnumbers.add(index);
		}
		
		return randomnumbers;
		
	}
	
	
	
	
	static LinkedHashMap<Integer,ArrayList<Integer>> initialwantedmap(ArrayList<Integer> randomnumbers,int m,int n)
	{
		int t=0;
		
		LinkedHashMap<Integer,ArrayList<Integer>> initialmap = initialmap(n);
		LinkedHashMap<Integer,ArrayList<Integer>> initialwantedmap = new LinkedHashMap<Integer,ArrayList<Integer>>();
		
		
		
		
		for(int i=0;i<m;i++)
		{
			initialwantedmap.put(t, initialmap.get(randomnumbers.get(i)));
			t++;
		}
		
		return initialwantedmap;
	}
	
	
	
	
	
	
	static LinkedHashMap<Integer,Integer> numbersofcomparisonpernode(ArrayList<Integer> randomnumbers, int m)
	{
		int t=0;

		
		LinkedHashMap<Integer,Integer> numbersofcomparisonpernode = new LinkedHashMap<Integer,Integer>();
		
		for(int i=0;i<m;i++)
		{
			numbersofcomparisonpernode.put(t, 1);
			t++;
		}
		
		return numbersofcomparisonpernode;
		
	}
	
	
	
	static LinkedHashMap<Integer,Integer> numbersofdatapernode(ArrayList<Integer> randomnumbers, int m)
	{
		int t=0;

		
		LinkedHashMap<Integer,Integer> numbersofdatapernode = new LinkedHashMap<Integer,Integer>();
		
		for(int i=0;i<m;i++)
		{
			numbersofdatapernode.put(t, 2);
			t++;
		}
		
		return numbersofdatapernode;
		
	}
	
	
	

	
	
	
	
	
	
	
	
	
	static void Delextranode(int m, int n)
	{
		
//		initialmap = new LinkedHashMap<Integer,ArrayList<Integer>>();
		
//		initialmap = initialmap(n);
		
		
		alltheinputdata= new ArrayList<Integer>();
		
		for(int i=0;i<n;i++)
			alltheinputdata.add(i);
		
		
		int initialnumber=0;
		
		initialnumber=n/m;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		ArrayList<Integer> comparisonitem;
		
		allthecomparison = new ArrayList<ArrayList<Integer>>();
		
		for(int j=0;j<n;j++)
			for(int k=j+1;k<n;k++)
			{
				
				comparisonitem=new ArrayList<Integer>();
				comparisonitem.add(j);
				comparisonitem.add(k);
				allthecomparison.add(comparisonitem);
	
			}
		
		

		
		
	
		
		dataitems = new ArrayList<ArrayList<Integer>>();
		
		for(int i=0;i<m;i++)
		{
//			dataitems.add(allthecomparison.get(randomnumbers.get(i)));
			dataitems.add(new ArrayList<Integer>());
		}
		
		
/*		for(int i=0;i<m;i++)
		{
			allthecomparison.remove(dataitems.get(i));
//			initialmap.remove(randomnumbers.get(i));
		}
*/		
		
		numbersofcomparison = new ArrayList<Integer>();
		
		for(int i=0;i<m;i++)
		{
			numbersofcomparison.add(0);
			
		}
		
		
		numbersofdata = new ArrayList<Integer>();
		
		for(int i=0;i<m;i++)
		{
			numbersofdata.add(0);
			
		}
		
		
//		initialwantedmap = new LinkedHashMap<Integer,ArrayList<Integer>>();
		
//		initialwantedmap = initialwantedmap(randomnumbers,m,n);
		
		
//		numbersofcomparisonpernode = new LinkedHashMap<Integer,Integer>();
		
//		numbersofcomparisonpernode = numbersofcomparisonpernode(randomnumbers,m);
		
		
		
//	    numbersofdatapernode = new LinkedHashMap<Integer,Integer>();
		
//		numbersofdatapernode = numbersofdatapernode(randomnumbers,m);
		
		
		int size;
//		size=initialmap.size();
		
		size=allthecomparison.size();
		
		
		while(size>0)
		{
			int delitemnumber;
//			int minIndex=0;
			
			
			
			Random randomitem = new Random();
			Random randomhosts = new Random();
			
			
			ArrayList<Integer> delpairs = new ArrayList<Integer>();
			

			//random choose the data to add
			
			
			delitemnumber=randomitem.nextInt(size);
			
			

			
			delpairs=allthecomparison.get(delitemnumber);
			
			allthecomparison.remove(delitemnumber);
			

			
			
			// based on the order to add data
/*			
			int t=0;
            delitemnumber=t;
			
			

			
			delpairs=allthecomparison.get(delitemnumber);
			
			allthecomparison.remove(delitemnumber);
			
*/			
			
			
			
			
			
/*			
			
			
	//prefer to the node with smallest numbers of data
			
			minIndex = numbersofdata.indexOf(Collections.min(numbersofdata));
			
			numbersofcomparison.set(minIndex, numbersofcomparison.get(minIndex)+1);
			
			ArrayList<Integer> newdataitems = dataitems.get(minIndex);
			newdataitems.addAll(delpairs);
			
			
			Set<Integer> set = new HashSet<Integer>();
			set.addAll(newdataitems);
			newdataitems.clear();
			newdataitems.addAll(set);
*/			


			
/*			
			
		//prefer to the node with smallest numbers of tasks	
            minIndex = numbersofcomparison.indexOf(Collections.min(numbersofcomparison));
			
			numbersofcomparison.set(minIndex, numbersofcomparison.get(minIndex)+1);
			
			ArrayList<Integer> newdataitems = dataitems.get(minIndex);
			newdataitems.addAll(delpairs);
			
			
			Set<Integer> set = new HashSet<Integer>();
			set.addAll(newdataitems);
			newdataitems.clear();
			newdataitems.addAll(set);
			
			dataitems.set(minIndex, newdataitems);
		
			numbersofdata.set(minIndex,newdataitems.size());
			
			
*/			
			int firstminIndex=0;
			int lastminIndex=0;
			int temp=0;
			int position=0;
			
		    firstminIndex = numbersofcomparison.indexOf(Collections.min(numbersofcomparison));
		    lastminIndex = numbersofcomparison.lastIndexOf(Collections.min(numbersofcomparison));
		    
		    if (firstminIndex==lastminIndex)
		    {
		    	
		    	numbersofcomparison.set(firstminIndex, numbersofcomparison.get(firstminIndex)+1);
				
				ArrayList<Integer> newdataitems = dataitems.get(firstminIndex);
				newdataitems.addAll(delpairs);
						
						
				Set<Integer> set = new HashSet<Integer>();
				set.addAll(newdataitems);
				newdataitems.clear();
				newdataitems.addAll(set);			
				
				dataitems.set(firstminIndex, newdataitems);
				
				numbersofdata.set(firstminIndex,newdataitems.size());
		    	
		    }
		    
		    else
		    {
		    	temp=numbersofdata.get(firstminIndex);
		    	position=firstminIndex;
		    	
		    	for(int index=firstminIndex+1; index<lastminIndex+1;index++)
		    	{
		    		if(numbersofcomparison.get(index)==numbersofcomparison.get(firstminIndex))
		    			if(numbersofdata.get(index)<temp)
		    			{
		    				temp=numbersofdata.get(index);
		    				position=index;
		    				
		    			}
		    		
		    	}
		    	
		    	
		    	
                numbersofcomparison.set(position, numbersofcomparison.get(position)+1);
				
				ArrayList<Integer> newdataitems = dataitems.get(position);
				newdataitems.addAll(delpairs);
						
						
				Set<Integer> set = new HashSet<Integer>();
				set.addAll(newdataitems);
				newdataitems.clear();
				newdataitems.addAll(set);			
				
				dataitems.set(position, newdataitems);
				
				numbersofdata.set(position,newdataitems.size());
		    	
		    	
		    	
		    }
		    
		    
		    
			
			
			
			
			
			
			
			
			
			
			
//			dataitems.set(minIndex, newdataitems);
		
//			numbersofdata.set(minIndex,newdataitems.size());
			
			
			
			
			
			
			
			
			
			
//			size=initialmap.size();
			
			
			size=allthecomparison.size();
			
			
			
			
		}
		
		
		
		
		
		
		
		
	}
	
	

//	static LinkedHashMap<Integer,ArrayList<Integer>> initialmap;
	
	static ArrayList<Integer> randomnumbers;
	static ArrayList<ArrayList<Integer>> dataitems;
	static ArrayList<Integer> numbersofcomparison;
	static ArrayList<Integer> numbersofdata;
	static ArrayList<ArrayList<Integer>> allthecomparison;
	
//	static LinkedHashMap<Integer,ArrayList<Integer>> initialwantedmap;
//	static LinkedHashMap<Integer,Integer> numbersofcomparisonpernode;
//	static LinkedHashMap<Integer,Integer> numbersofdatapernode;
	
	
	static ArrayList<Integer> alltheinputdata;
	
	
	
	
	
	public static void main(String[] args)
	{

		
		
		for (int N = 1; N <= 256; N*=2)
            for (int M = 1; M <= N*(N-1)/2; M*=2)
            {
            	Delextranode(M,N);
            	
            	System.out.println(N+"\t"+M+"\t"+Collections.max(numbersofdata)+"\t"+Collections.min(numbersofdata)+"\t"+Collections.max(numbersofcomparison)+"\t"+Collections.min(numbersofcomparison));
//            	System.out.println(N+"\t"+M+"\t"+Collections.max(numbersofcomparison)+"\t"+Collections.min(numbersofcomparison));
            }
		

		

		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	

	
	
	
	
	
	
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	


