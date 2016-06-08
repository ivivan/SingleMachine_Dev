import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class ImprovedAlgrithm {
	
	
	
	static void initialization(int m, int n)
	{
		
		//calculate Q
		if(n*(n-1)%(2*m)==0)
			maxcomparison=n*(n-1)/(2*m);
		else
			maxcomparison=n*(n-1)/(2*m)+1;
		
		//List saves all the input data
        alltheinputdata= new ArrayList<Integer>();
		
		for(int i=0;i<n;i++)
			alltheinputdata.add(i);				
		
		//List saves all the comparison tasks
		allthecomparison = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> comparisonitem;
		
		for(int j=0;j<n;j++)
			for(int k=j+1;k<n;k++)
			{
				
				comparisonitem=new ArrayList<Integer>();
				comparisonitem.add(j);
				comparisonitem.add(k);
				allthecomparison.add(comparisonitem);
	
			}
		
		//List saves the allocated comparison tasks for each node
        detailsofcomparisons = new ArrayList<ArrayList<ArrayList<Integer>>>();
		
		for(int i=0;i<m;i++)
			detailsofcomparisons.add(new ArrayList<ArrayList<Integer>>());
		
		//List saves the allocated data for each node
		dataitems = new ArrayList<ArrayList<Integer>>();
		
		for(int i=0;i<m;i++)
		{
			dataitems.add(new ArrayList<Integer>());
		}
		
        //List saves the number of comparison tasks allocated to each node
		numbersofcomparison = new ArrayList<Integer>();
		
		for(int i=0;i<m;i++)
		{
			numbersofcomparison.add(0);
			
		}
		
		//List saves the number of data saved in each node
		numbersofdata = new ArrayList<Integer>();
		
		for(int i=0;i<m;i++)
		{
			numbersofdata.add(0);
			
		}
		
	}
	
	
	static void DistributionStrategy(int m, int n)
	{
		
		initialization(m, n);
		
		
		int timer=0;	
		int size;	
		size=allthecomparison.size();
		
		
		while(size>0)
		{
			int delitemnumber;
			
			Random randomitem = new Random();

			//random choose the data to add				
			delitemnumber=randomitem.nextInt(n);
					
			
			int firstminIndex=0;
			int lastminIndex=0;
			int temp=0;
			int position=0;
			
		    firstminIndex = numbersofdata.indexOf(Collections.min(numbersofdata));
		    lastminIndex = numbersofdata.lastIndexOf(Collections.min(numbersofdata));
		    
		    if (firstminIndex==lastminIndex)
		    {
		    	
		    	if(numbersofdata.get(firstminIndex)==0)
		    	{
		    		dataitems.get(firstminIndex).add(delitemnumber);
		    		
		    		numbersofdata.set(firstminIndex,dataitems.get(firstminIndex).size());
		    		
		    	}
		    	else
		    	{
		    		if(!dataitems.get(firstminIndex).contains(delitemnumber))
		    		{
		    			
		    			if(numbersofcomparison.get(firstminIndex)>=maxcomparison)
		    			{
		    				
		    				dataitems.get(firstminIndex).add(delitemnumber);
		    				numbersofdata.set(firstminIndex,dataitems.get(firstminIndex).size());
		    				
		    			}
		    			
		    			else
		    			{
		    			
			    			boolean rollback=true;
			    			
			    			ArrayList<ArrayList<Integer>> addcomparison = new ArrayList<ArrayList<Integer>>();
			    			
			    			for(int i=0;i<numbersofdata.get(firstminIndex);i++)
			    			{
			    				ArrayList<Integer> temppair = new ArrayList<Integer>();
			    				
			    				if(dataitems.get(firstminIndex).get(i)<delitemnumber)
			    				{
			    					temppair.add(dataitems.get(firstminIndex).get(i));
				    				temppair.add(delitemnumber);
			    				}
			    				else
			    				{
			    					temppair.add(delitemnumber);
			    					temppair.add(dataitems.get(firstminIndex).get(i));
			    				}
			    				
			    				addcomparison.add(temppair);
			    			}
			    			
		
			    			
			    			for(int i=0;i<addcomparison.size();i++)
			    			{
			    				
			    				if(numbersofcomparison.get(firstminIndex)<maxcomparison)
			    				{
			    						if(allthecomparison.contains(addcomparison.get(i)))
			    						{
			    							
			    							detailsofcomparisons.get(firstminIndex).add(addcomparison.get(i));						
			    					        allthecomparison.remove(addcomparison.get(i));
			    				        	numbersofcomparison.set(firstminIndex, numbersofcomparison.get(firstminIndex)+1);
			    					        rollback=false;
			    							
			    						}	
			    				}
			    			}
			    			
			    			if (!rollback)
			    			{
			    				dataitems.get(firstminIndex).add(delitemnumber);
			    				numbersofdata.set(firstminIndex,dataitems.get(firstminIndex).size());
			    			}
			    			else
			    			{
			    				timer++;
			    				if(timer>2*n)
				    			{
				    				dataitems.get(firstminIndex).add(delitemnumber);
				    				numbersofdata.set(firstminIndex,dataitems.get(firstminIndex).size());
				    				timer=0;
				    			}	
			    			}				    				
		    			}		    			
		    		}	    		
		    	}
		    }
		    
		    else
		    {
		    	if(numbersofdata.get(firstminIndex)==0)
		    	{
		    		dataitems.get(firstminIndex).add(delitemnumber);
		    		
		    		numbersofdata.set(firstminIndex,dataitems.get(firstminIndex).size());	
		    	}
		    	
		    	else
		    	{
		    		
		    		temp=numbersofcomparison.get(firstminIndex);
			    	position=firstminIndex;
			    	
			    	for(int index=firstminIndex+1; index<lastminIndex+1;index++)
			    	{
			    		if(numbersofdata.get(index)==numbersofdata.get(firstminIndex))
			    			if(numbersofcomparison.get(index)<temp)
			    			{
			    				temp=numbersofcomparison.get(index);
			    				position=index;
			    			}
			    		
			    	}
		    		
		    		if(!dataitems.get(position).contains(delitemnumber))
		    		{
		    			
                        boolean rollback=true;
		    			
		    			ArrayList<ArrayList<Integer>> addcomparison = new ArrayList<ArrayList<Integer>>();
		    			
		    			for(int i=0;i<numbersofdata.get(position);i++)
		    			{
		    				ArrayList<Integer> temppair = new ArrayList<Integer>();
		    				
		    				if(dataitems.get(position).get(i)<delitemnumber)
		    				{
		    					temppair.add(dataitems.get(position).get(i));
			    				temppair.add(delitemnumber);
		    				}
		    				else
		    				{
		    					temppair.add(delitemnumber);
		    					temppair.add(dataitems.get(position).get(i));
		    				}
		    				
		    				addcomparison.add(temppair);
		    			}
		    			
		    			
		    			for(int i=0;i<addcomparison.size();i++)
		    			{
		    				
		    				if(numbersofcomparison.get(position)<maxcomparison)
		    				{
		    				
		    				if(allthecomparison.contains(addcomparison.get(i)))
		    				{
		    					
		    					detailsofcomparisons.get(position).add(addcomparison.get(i));		
		    					allthecomparison.remove(addcomparison.get(i));
		    					numbersofcomparison.set(position, numbersofcomparison.get(position)+1);
		    					rollback=false;
		    				}
		    				
		    				}
		    				
		    			}
		    			
		    			if (!rollback)
		    			{
		    				dataitems.get(position).add(delitemnumber);
		    				numbersofdata.set(position,dataitems.get(position).size());
		    			}
		    			
		    			else
		    			{	 			
		    				
		    				timer++;
		    				if(timer>2*n)	  
			    			{
			    				dataitems.get(position).add(delitemnumber);
			    				numbersofdata.set(position,dataitems.get(position).size());
			    				timer=0;		
			    			}	
		    			}
		    		}
		    	}
		    }
		
			size=allthecomparison.size();

		}
	}	
	
    static ArrayList<ArrayList<ArrayList<Integer>>> detailsofcomparisons;
	static ArrayList<ArrayList<Integer>> dataitems;
	static ArrayList<ArrayList<Integer>> allthecomparison;
	static ArrayList<Integer> numbersofcomparison;
	static ArrayList<Integer> numbersofdata;
	static ArrayList<Integer> alltheinputdata;
	static int maxcomparison;
	
	public static void main(String[] args)
	{

		
/*		
		for (int N = 1; N <= 256; N*=2)
            for (int M = 1; M <= N*(N-1)/2; M*=2)
            {
            	DistributionStrategy(M,N);
            	
            	System.out.println(N+"\t"+M+"\t"+Collections.max(numbersofdata)+"\t"+Collections.min(numbersofdata)+"\t"+Collections.max(numbersofcomparison)+"\t"+Collections.min(numbersofcomparison));
//            	System.out.println(N+"\t"+M+"\t"+Collections.max(numbersofcomparison)+"\t"+Collections.min(numbersofcomparison));
            }
		

	*/	
		
		
		int M=16;
		int N=32;
		int min=32;
		ArrayList<ArrayList<ArrayList<Integer>>> showcomparisons = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<ArrayList<Integer>> showdata= new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> shownumbersofcomparison= new ArrayList<Integer>();
		ArrayList<Integer> shownumbersofdata = new ArrayList<Integer>();
		
   
		for (int i = 0; i <= 5000; i++)
            
            {
			
			    DistributionStrategy(M,N);
            	
//            	System.out.println(N+"\t"+M+"\t"+Collections.max(numbersofdata)+"\t"+Collections.min(numbersofdata)+"\t"+Collections.max(numbersofcomparison)+"\t"+Collections.min(numbersofcomparison));
//            	System.out.println(N+"\t"+M+"\t"+Collections.max(numbersofcomparison)+"\t"+Collections.min(numbersofcomparison));
            	if(Collections.max(numbersofdata)<min)
            	{
            		min=Collections.max(numbersofdata);  		
            		showcomparisons=detailsofcomparisons;
            		showdata=dataitems;
            		shownumbersofcomparison=numbersofcomparison;
            		shownumbersofdata=numbersofdata;		
            	}   	
            }
		
		
		//ordertheshowlist;
		for(int i=0;i<M;i++)
			Collections.sort(showdata.get(i));
		
		
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
	}
}
