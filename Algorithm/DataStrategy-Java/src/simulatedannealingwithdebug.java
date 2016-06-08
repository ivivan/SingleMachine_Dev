


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class simulatedannealing {
	
 //	ArrayList<Integer> computingpowerforeachnode;
	
	static HashMap<Integer,Integer> maxtaskpernode;
	static HashMap<Integer,ArrayList<Integer>> allcomparisontasks;
	
	
    static HashMap<Integer,ArrayList<Integer>> bestresulttask;
    static HashMap<Integer,ArrayList<Integer>> bestresultdata;
	
    static ArrayList<ArrayList<ArrayList<Integer>>> initasks;
	  static ArrayList<ArrayList<Integer>> inidata;
	
	
	//set processingpower
	
	public  ArrayList<Integer> Processpowerset(int n)
	{

		ArrayList<Integer> ppset = new ArrayList<Integer>();
		
		
		for(int z=0;z<n;z++){
			
			if(z%2==0)
				ppset.add(2);
			else
				ppset.add(1);		
		
		}
		
		return ppset;
	}
	
	

	
	// calculate maxmum number of comparison tasks per node.
	public  HashMap<Integer,Integer> MaxmamTaskPerNode(ArrayList<Integer> processingpower, int m, int n)
	{

		HashMap<Integer,Integer> pp = new HashMap<Integer,Integer>();
		
		int totalpower=0;
		int eachnumber=0;
		
		for(int z=0;z<n;z++){
			totalpower+=processingpower.get(z);
		}
		

		
		
		for(int i=0;i<n;i++){
			
			pp.put(i, processingpower.get(i)*m*(m-1)%(2*totalpower)==0 ? processingpower.get(i)*m*(m-1)/(2*totalpower):processingpower.get(i)*m*(m-1)/(2*totalpower)+1);
			}
		return pp;
	}
	
	
	
	// inital all comparison tasks
	
	public HashMap<Integer,ArrayList<Integer>> AllTaskList(int m,int n){
		
		HashMap<Integer, ArrayList<Integer>> tl = new HashMap<Integer,ArrayList<Integer>>();		
		ArrayList<ArrayList<Integer>> alltask = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> eachtask = new ArrayList<Integer>();		
		int tasknumber=0;
		
		tasknumber=m*(m-1)/2;	
		
		//List saves all the comparison tasks		 
		for(int j=0;j<m;j++)
			for(int k=j+1;k<m;k++){
				
				eachtask=new ArrayList<Integer>();
				eachtask.add(j);
				eachtask.add(k);
				alltask.add(eachtask);
				
			}
		
		
		for(int i=0;i<tasknumber;i++){
			
			tl.put(i, alltask.get(i));
			
			}
		
		return tl;
	}
	
	 
	
	public void initial(int m, int n){
		
		maxtaskpernode=MaxmamTaskPerNode(Processpowerset(n),m,n);
		
		allcomparisontasks=AllTaskList(m,n);
		
		}
		
	
	
	
	
	
	/* .........................................................   */
	
	
	
	
	public ArrayList<Integer> GenerateRandomNumberInRange(int number){

		Integer[] arr = new Integer[number]; 
		ArrayList<Integer> resultlist = new ArrayList<Integer>();

		for (int i = 0; i < arr.length; i++) { 
		    arr[i] = i; 
		} 

		Collections.shuffle(Arrays.asList(arr)); 

		for (int i = 0; i < arr.length; i++) { 
		    resultlist.add(arr[i]); 
		}
		
		return resultlist;
		
	}

	
	
	
	
	
	public HashMap<Integer,ArrayList<Integer>> InitialAllocateTask (HashMap<Integer,Integer> maxtaskpernode, int m, int n){
		
		ArrayList<Integer> randomlist = new ArrayList<Integer>();
		ArrayList<Integer> eachtasklist = new ArrayList<Integer>();
		HashMap<Integer,ArrayList<Integer>> initialsolution = new HashMap<Integer,ArrayList<Integer>>();
		int sublistlength=0;

		randomlist=GenerateRandomNumberInRange(m*(m-1)/2);

		for(int i=0;i<n-1;i++){

//			eachtasklist=new ArrayList<Integer>();

			sublistlength=maxtaskpernode.get(i);

			eachtasklist=new ArrayList<Integer>(randomlist.subList(0,sublistlength));

			randomlist=new ArrayList<Integer>(randomlist.subList(sublistlength,randomlist.size()));

			initialsolution.put(i,eachtasklist);


		}

		initialsolution.put(n-1,randomlist);
		return initialsolution;		
		
	}
	

	public HashMap<Integer,ArrayList<Integer>> GetAllocatedData(HashMap<Integer,ArrayList<Integer>> subtasklist, HashMap<Integer,ArrayList<Integer>> alltasklist, int m, int n){

		HashMap<Integer,ArrayList<Integer>> datapernode = new HashMap<Integer,ArrayList<Integer>>();
		ArrayList<Integer> taskoneachnode = new ArrayList<Integer>();
		ArrayList<Integer> dataoneachnode = new ArrayList<Integer>();
		ArrayList<Integer> tempdatapernode = new ArrayList<Integer>();

		for(int i=0;i<n;i++){

			taskoneachnode= new ArrayList<Integer>();
			tempdatapernode = new ArrayList<Integer>();

			
			
			taskoneachnode.addAll(subtasklist.get(i));

			for(int j=0;j<taskoneachnode.size();j++){

				tempdatapernode.add(alltasklist.get(taskoneachnode.get(j)).get(0));
				tempdatapernode.add(alltasklist.get(taskoneachnode.get(j)).get(1));

			}

			HashSet<Integer> hs = new HashSet<Integer>();
			hs.addAll(tempdatapernode);
            tempdatapernode.clear();
            tempdatapernode.addAll(hs);

            datapernode.put(i,tempdatapernode);

		}

		return datapernode;


	}


	public ArrayList<Integer> CalNumberofData(HashMap<Integer,ArrayList<Integer>> dataitemoneachnode, int n){
		ArrayList<Integer> dataitemnumber = new ArrayList<Integer>();

		for(int i=0;i<n;i++){
			dataitemnumber.add(dataitemoneachnode.get(i).size());
		}

		return dataitemnumber;
	}




	public int EvaluateFitness(ArrayList<Integer> datanumber){
	
		return Collections.max(datanumber);
	}



	public int FitnessFunction(HashMap<Integer,ArrayList<Integer>> onesolution, HashMap<Integer,ArrayList<Integer>> alltasklist, int m, int n){
		ArrayList<Integer> datanumberpernode = new ArrayList<Integer>();
		HashMap<Integer,ArrayList<Integer>> datadetailpernode = new HashMap<Integer,ArrayList<Integer>>();

		datadetailpernode= GetAllocatedData(onesolution,alltasklist,m,n);
		
		
		
//		System.out.println("data1----------------");
//		
//		for(int i=0;i<datadetailpernode.get(0).size();i++){
//			System.out.println(datadetailpernode.get(0).get(i));
//		}
//		System.out.println("data2----------------");
//		for(int i=0;i<datadetailpernode.get(1).size();i++){
//			System.out.println(datadetailpernode.get(1).get(i));
//		}
//		System.out.println("data3----------------");
//		for(int i=0;i<datadetailpernode.get(2).size();i++){
//			System.out.println(datadetailpernode.get(2).get(i));
//		}	
//		System.out.println("dataend----------------");		
//		
		
		
		
		
		
		
		

		datanumberpernode= CalNumberofData(datadetailpernode,n);

		return EvaluateFitness(datanumberpernode);
	}


	
		
	public HashMap<Integer,ArrayList<Integer>> GenerateSolution(HashMap<Integer,ArrayList<Integer>> lastsolution, int m, int n){
		ArrayList<Integer> randomtwo = new ArrayList<Integer>();
		ArrayList<Integer> leftpick = new ArrayList<Integer>();
		ArrayList<Integer> rightpick = new ArrayList<Integer>();
		ArrayList<Integer> leftpicktask = new ArrayList<Integer>();
		ArrayList<Integer> rightpicktask = new ArrayList<Integer>();
		HashMap<Integer,ArrayList<Integer>> newsolution = new HashMap<Integer,ArrayList<Integer>>();


		int randomk=0;
		int left=0;
		int right=0;
		int minlength=0;
		int leftlength=0;
		int rightlength=0;


		randomtwo=GenerateRandomNumberInRange(n);
		left=randomtwo.get(0);
		right=randomtwo.get(1);
		
//		System.out.println(left);
//		System.out.println(right);		
		
		

		leftlength=lastsolution.get(left).size();
		rightlength=lastsolution.get(right).size();
		
//		System.out.println(leftlength);
//		System.out.println(rightlength);

		minlength= leftlength<rightlength ? leftlength:rightlength;
		
//		System.out.println(minlength);

		Random rand = new Random();

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
        randomk = rand.nextInt((minlength - 1) + 1) + 1;
        
 //       System.out.println(randomk);

        leftpick=GenerateRandomNumberInRange(randomk);
        rightpick=GenerateRandomNumberInRange(randomk);


        for(int i=0;i<randomk;i++){

        	leftpicktask.add(lastsolution.get(left).get(leftpick.get(i)));
        	rightpicktask.add(lastsolution.get(right).get(rightpick.get(i)));

        }
        
        


 //       	leftpicktask.add(lastsolution.get(left).get(leftpick.get(0)));
  //      	rightpicktask.add(lastsolution.get(right).get(rightpick.get(0)));

       



        
        
        
        
        
//        for(int i=0;i<randomk;i++){
//
//        	System.out.println(leftpicktask.get(i));
//
//        }
//        
//        System.out.println("-----------");
//        
//        for(int i=0;i<randomk;i++){
//
//        	System.out.println(rightpicktask.get(i));
//
//        }
//        
        
        
        
        
        
        ArrayList<Integer> leftremain = new ArrayList<Integer>();
        ArrayList<Integer> rightremain = new ArrayList<Integer>();        
        
        ArrayList<Integer> updateleft = new ArrayList<Integer>();
        ArrayList<Integer> updateright = new ArrayList<Integer>();
        
        
        for(int i=0;i<lastsolution.get(left).size();i++){
        	if (!leftpicktask.contains(lastsolution.get(left).get(i)))
        	leftremain.add(lastsolution.get(left).get(i));
        }
        
        for(int i=0;i<lastsolution.get(right).size();i++){
        	if (!rightpicktask.contains(lastsolution.get(right).get(i)))
        		rightremain.add(lastsolution.get(right).get(i));
        }
        
        
        
        
        
        updateleft.addAll(rightpicktask);
        updateleft.addAll(leftremain);
        
        
        
        
        updateright.addAll(leftpicktask);
        updateright.addAll(rightremain);
        
        
        
        
        
        
        
        
        
        Map<Integer, ArrayList<Integer>> tmp = new HashMap<Integer, ArrayList<Integer>>(lastsolution);
        tmp.keySet().removeAll(newsolution.keySet());
        newsolution.putAll(tmp);
        
        
        
        

        for(int i=0;i<randomk;i++){
        	
        	
        	
        	newsolution.put(left,updateleft);
        	
 //       	lastsolution.put(left,lastsolution.get(left));
        	
        	newsolution.put(right,updateright);

        }

        return newsolution;
	
	}
		
		

		
	/* ..............................................................................*/
	
	
	
	


	
	
	
	public int annealing(int m,int n) throws IOException {

		maxtaskpernode = new HashMap<Integer,Integer>();
	    allcomparisontasks = new HashMap<Integer,ArrayList<Integer>>();

		initial(m,n);
		
		
		
//		Iterator<Entry<Integer, Integer>> crudeiterator = maxtaskpernode.entrySet().iterator();
//            
//        while(crudeiterator.hasNext())
//        {
//            Entry<Integer, Integer> entry = crudeiterator.next(); 
//            System.out.println(entry.getKey()+"@"+entry.getValue());
//        
//        }
//        
//        System.out.println(allcomparisontasks.size());
//        System.out.println(allcomparisontasks.get(35).get(0));
//        System.out.println(allcomparisontasks.get(35).get(1));
//        System.out.println("-------------");

		
		
		
		
		

		double initialT = 1.0;
		double finishT = 0.000000001;
		double delta=0;
		int innerloop;
		int innerlength=5000;
		double rate = 0.99;
		Random r = new Random();
		int bestvalue=0;

		HashMap<Integer,ArrayList<Integer>> startsolution = new HashMap<Integer,ArrayList<Integer>>();
		HashMap<Integer,ArrayList<Integer>> neighboursolution = new HashMap<Integer,ArrayList<Integer>>();
		HashMap<Integer,ArrayList<Integer>> relateddata = new HashMap<Integer,ArrayList<Integer>>();




		startsolution=InitialAllocateTask(maxtaskpernode,m,n);
		
		
		
//		System.out.println(startsolution.size());
//		System.out.println(startsolution.get(0).size());
//		System.out.println(startsolution.get(1).size());		
//		System.out.println(startsolution.get(2).size());
//		
//		System.out.println("1----------------");
//		
//		for(int i=0;i<startsolution.get(0).size();i++){
//			System.out.println(startsolution.get(0).get(i));
//		}
//		System.out.println("2----------------");
//		for(int i=0;i<startsolution.get(1).size();i++){
//			System.out.println(startsolution.get(1).get(i));
//		}
//		System.out.println("3----------------");
//		for(int i=0;i<startsolution.get(2).size();i++){
//			System.out.println(startsolution.get(2).get(i));
//		}	
//		System.out.println("end----------------");		
		
		
		
		
//		System.out.println(bestvalue);
		
		System.out.println("----------");
		int count = 0;

		while (initialT > finishT) {
		
		    innerloop =1;
		    
		    while(innerloop<=innerlength){

		    	neighboursolution=GenerateSolution(startsolution,m,n);
		    	
//				System.out.println(neighboursolution.size());
//				System.out.println("*********");
//				System.out.println(neighboursolution.get(0).size());
//				System.out.println(neighboursolution.get(1).size());		
//				System.out.println(neighboursolution.get(2).size());
		    	
//				System.out.println("1----------------");
//				
//				for(int i=0;i<neighboursolution.get(0).size();i++){
//					System.out.println(neighboursolution.get(0).get(i));
//				}
//				System.out.println("2----------------");
//				for(int i=0;i<neighboursolution.get(1).size();i++){
//					System.out.println(neighboursolution.get(1).get(i));
//				}
//				System.out.println("3----------------");
//				for(int i=0;i<neighboursolution.get(2).size();i++){
//					System.out.println(neighboursolution.get(2).get(i));
//				}	
//				System.out.println("end----------------");	
//				
				
				
				
				
		    	
		    	
		    	
		    	

		    	
		    	delta=FitnessFunction(neighboursolution, allcomparisontasks, m, n)-FitnessFunction(startsolution, allcomparisontasks, m, n);

		  //  	System.out.println(delta);
		    	
		    	if (delta <= 0 || (delta > 0 && Math.exp(-delta/initialT) > r.nextDouble())) {
		    		bestvalue=FitnessFunction(neighboursolution, allcomparisontasks, m, n);
					startsolution = new HashMap<Integer,ArrayList<Integer>>(neighboursolution);
					
				//	System.out.println(bestvalue+"!!!!!!!!!");
				}
		    	
		    	innerloop++;
		    }
			

			
			
			initialT *= rate;

			count++;
			System.out.println(count);
		}
		
		bestresulttask= new HashMap<Integer, ArrayList<Integer>>();
        Map<Integer, ArrayList<Integer>> temp = new HashMap<Integer, ArrayList<Integer>>(neighboursolution);
        temp.keySet().removeAll(bestresulttask.keySet());
        bestresulttask.putAll(temp);
		
		
		

		System.out.println(count);
		return bestvalue;
	}
	
	
	
	public static void main(String[] args) throws IOException{
		simulatedannealing sa = new simulatedannealing();

	    maxtaskpernode = new HashMap<Integer,Integer>();
	    allcomparisontasks = new HashMap<Integer,ArrayList<Integer>>();
	    
	    
	    bestresultdata = new HashMap<Integer,ArrayList<Integer>>();
	    
	    int m=31;
	    int n=4;
	    
//	    m=Integer.parseInt(args[0]);
//	    n=Integer.parseInt(args[1]);
	    
	    
	    

	    int result=0;

	    result=sa.annealing(m,n);
	    
/*	    for(int i=0;i<n;i++){
	    	for(int j=0;j<bestresulttask.get(i).size();j++){
	    		System.out.println(bestresulttask.get(i).get(j)+" ");
	    	}
	    	System.out.println("--------");
	    }*/
	    
	    
	    for(int i=0;i<n;i++){
	    	for(int j=0;j<bestresulttask.get(i).size();j++){
	    		System.out.println(allcomparisontasks.get(bestresulttask.get(i).get(j)).get(0)+" "+allcomparisontasks.get(bestresulttask.get(i).get(j)).get(1));
	    	}
	    	System.out.println("--------");
	    }
	    
	    

	    
	    
	    
	    bestresultdata=sa.GetAllocatedData(bestresulttask,allcomparisontasks,m,n);
	    
	    System.out.println("+++++++");
	    
	    for(int i=0;i<n;i++){
	    	for(int j=0;j<bestresultdata.get(i).size();j++){
	    		System.out.println(bestresultdata.get(i).get(j)+" ");
	    	}
	    	System.out.println("--------");
	    }
	    
	    
	    System.out.println(result+"!!!");

	}
	
	
	
	
	

}


