
	
	package revolution;

	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.List;

	public class SecondInitial {
		
		static void initialization(int m, int n)
		{
			
			
			//calculate Q
					if(n*(n-1)%(2*m)==0)
						maxcomparison=n*(n-1)/(2*m);
					else
						maxcomparison=n*(n-1)/(2*m)+1;
			
			
			
			
			//compuitng power for each node
			computingpowerforeachnode = new ArrayList<Integer>();
			
			for(int i=0;i<m;i++)
			{
				if(i%2==0)
					computingpowerforeachnode.add(1);
				else
					computingpowerforeachnode.add(2);
				
			}
			
			totalcomputingpower=0;
			
			for(int i=0;i<m;i++)
				totalcomputingpower=totalcomputingpower+computingpowerforeachnode.get(i);
			
			
			//maxcomparison for each node;
			
			maxcomparisonforeachnode= new ArrayList<Integer>();
			
			for(int i=0;i<m;i++)
			{
				
				if(n*(n-1)*computingpowerforeachnode.get(i)%(2*totalcomputingpower)==0)
				{
					maxcomparisonforeachnode.add(n*(n-1)*computingpowerforeachnode.get(i)/(2*totalcomputingpower));
					
					
				}
				
				else
					
				{
					maxcomparisonforeachnode.add(n*(n-1)*computingpowerforeachnode.get(i)/(2*totalcomputingpower)+1);
					
				}
				
			
			}
			
			
			
			
			
			
			
			allthenodes = new ArrayList<Integer>();
			for(int i=0;i<m;i++)
				allthenodes.add(i);
		
			//List saves all the input data
	        alltheinputdata= new ArrayList<Integer>();
			
			for(int i=0;i<n;i++)
				alltheinputdata.add(i);				
			
			//List saves all the used numer of each file
			alltheinputdatausedtime= new ArrayList<Integer>();
			
			for(int i=0;i<n;i++)
				alltheinputdatausedtime.add(n-1);
				
			
			
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
			
			//List save all the comparison tasks----keep
			
			allthecomparisonstatic = new ArrayList<ArrayList<Integer>>();
			ArrayList<Integer> comparisonitems;
			
			for(int j=0;j<n;j++)
				for(int k=j+1;k<n;k++)
				{				
					comparisonitems=new ArrayList<Integer>();
					comparisonitems.add(j);
					comparisonitems.add(k);
					allthecomparisonstatic.add(comparisonitems);	
				}
			
			
			//List saves all the places that can do the comparison tasks
			
			
			allthecomparisonplace = new ArrayList<ArrayList<Integer>>();
			ArrayList<Integer> comparisonplace;
			
			for(int j=0;j<n;j++)
				for(int k=j+1;k<n;k++)
				{				
					comparisonplace=new ArrayList<Integer>();
					allthecomparisonplace.add(comparisonplace);	
				}
			
			
			//List save the place where do the tasks
			thecomparisonlocation = new ArrayList<Integer>();
			
			for(int j=0;j<n;j++)
				for(int k=j+1;k<n;k++)
				{				
					thecomparisonlocation.add(m);	
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
			
			int totalnumber=0;
			
			int timer=0;	
			int size;	
			size=allthecomparison.size();
			
//			Collections.shuffle(alltheinputdata);		
			
			while(size>0)
			{		
				
				ArrayList<Integer> remainingdata= new ArrayList<Integer>();
				List<Integer> inputdatausedtime= new ArrayList<Integer>();
				ArrayList<Integer> alltheinputdatausedtimecopy = new ArrayList<Integer>();

				
				for(int i=0;i<alltheinputdatausedtime.size();i++)
				{
					if(alltheinputdatausedtime.get(i)!=0)
						remainingdata.add(i);
				}
				
				
//				System.out.println(remainingdata.size()+"####");
				
				
				
				
				
				
				for(int i=0;i<alltheinputdatausedtime.size();i++)
				{
					alltheinputdatausedtimecopy.add(alltheinputdatausedtime.get(i));
				}									
				
	            for(int i=0;i<remainingdata.size();i++)
	            	inputdatausedtime.add(alltheinputdatausedtime.get(remainingdata.get(i)));
	            
	            
				
	//more first			
//	            Collections.reverse(inputdatausedtime);
	            
	//less first            
	            Collections.sort(inputdatausedtime);
	            
	            remainingdata= new ArrayList<Integer>();
	            for(int i=0;i<inputdatausedtime.size();i++)
	            {
	            	remainingdata.add(alltheinputdatausedtimecopy.indexOf(inputdatausedtime.get(i)));
	            	alltheinputdatausedtimecopy.set(remainingdata.get(i),n);
	            }  
	            
	            
	            
	            
	            
				

				int firstminIndex=0;
				int lastminIndex=0;
				int temp=0;
				int position=0;
				int newaddnumber=0;
				
				int firstmintasknumber=0;
				int lastmintasknumber=0;			
				int smallestdatanumber=0;
				int smallesttasknumber=0;					
				
				for (int d : remainingdata)
				{	
					
	                smallestnodes = new ArrayList<Integer>();				
	                suitablenodes = new ArrayList<Integer>();	
	                firstchoosennodes = new ArrayList<Integer>();	
	                choosennodes = new ArrayList<Integer>();		
	                
	                
	                ArrayList<Integer> remainningnode= new ArrayList<Integer>();
	                
	                for(int i=0;i<m;i++)
	                	if(!dataitems.get(i).contains(d))
	                	{
	                		remainningnode.add(i);
	 //               		System.out.println(i+"****");
	                		
	                	}
	                		
	                
	                
	                
	                
	                
	                
	                
	                
	                
	                
	                
	                
	                
	                
	                
	    /*            for(int j=0;j<remainningnode.size();j++)
	                {
	                	boolean dele=true;
	                	
	                	
	      //          	dataitems.get(node).remove(addcomparison.get(i).get(0));
	                	
	                	for(int z=0;z<remainingdata.size();z++)
	                	{
	                		if((remainingdata.get(z)!=d)&&dataitems.get(remainningnode.get(j)).contains(remainingdata.get(z))&&(numbersofdata.get(remainningnode.get(j))>0))
	                			dele=false;
	                		
	                	}
	                		
	                	if(dele)
	                	{
	                		remainningnode.remove(remainningnode.get(j));
	                		
	                	}
	                	
	                	
	                }*/
	                
	                
	                
	                
	                
	                
	/*                	
	                ArrayList<Integer> remainningnumbersofcomparison= new ArrayList<Integer>();
	                
	                for(int i=0;i<remainningnode.size();i++)
	                	remainningnumbersofcomparison.add(numbersofcomparison.get(remainningnode.get(i)));
	                
	                
	                
	                smallesttasknumber=Collections.min(remainningnumbersofcomparison);
					firstmintasknumber = remainningnumbersofcomparison.indexOf(smallesttasknumber);
					lastmintasknumber = remainningnumbersofcomparison.lastIndexOf(smallesttasknumber);
	                
	                
					for(int i=firstmintasknumber;i<lastmintasknumber+1;i++)
						if(remainningnumbersofcomparison.get(i)==smallesttasknumber)
							smallestnodes.add(i);               
	                
					for(int i=0;i<smallestnodes.size();i++)
						suitablenodes.add(numbersofdata.get(remainningnode.get(smallestnodes.get(i))));                
	                
					smallestdatanumber=Collections.min(suitablenodes);
					firstminIndex = suitablenodes.indexOf(smallestdatanumber);
				    lastminIndex = suitablenodes.lastIndexOf(smallestdatanumber);             
	              
	                
				    for(int i=firstminIndex;i<lastminIndex+1;i++)
						if((suitablenodes.get(i)==smallestdatanumber))
							choosennodes.add(remainningnode.get(smallestnodes.get(i)));
	                */
	    	        
				
	                
	                ArrayList<Integer> finishingrate= new ArrayList<Integer>();     
	                
	                
	                for(int i=0;i<remainningnode.size();i++)
	                {
	                	
	                	
	                	finishingrate.add(numbersofcomparison.get(remainningnode.get(i))*100/maxcomparisonforeachnode.get(remainningnode.get(i)));
	                	
	                	
	                	
	                	
	                }
	                
	                
	               for(int k=0;k<finishingrate.size();k++)
	                	System.out.println(finishingrate.get(k));
	                
	                
	                
	                smallesttasknumber=Collections.min(finishingrate);
	    			firstmintasknumber = finishingrate.indexOf(smallesttasknumber);
	    			lastmintasknumber = finishingrate.lastIndexOf(smallesttasknumber);
	                
	                
	                
	    			for(int i=firstmintasknumber;i<lastmintasknumber+1;i++)
	    				if(finishingrate.get(i)==smallesttasknumber)
	    					smallestnodes.add(i);               
	                
	    			for(int i=0;i<smallestnodes.size();i++)
	    				suitablenodes.add(numbersofdata.get(remainningnode.get(smallestnodes.get(i))));                
	                
	    			smallestdatanumber=Collections.min(suitablenodes);
	    			firstminIndex = suitablenodes.indexOf(smallestdatanumber);
	    		    lastminIndex = suitablenodes.lastIndexOf(smallestdatanumber);             
	              
	                
	    		    for(int i=firstminIndex;i<lastminIndex+1;i++)
	    				if((suitablenodes.get(i)==smallestdatanumber))
	    					choosennodes.add(remainningnode.get(smallestnodes.get(i)));
	    			    
	                
	     //           for(int k=0;k<choosennodes.size();k++)
	    //            	System.out.println(choosennodes.get(k)+"@@@");
	                
	                
	                
	                
	                
	                
	                
	                
	                
	                
	                
	                
	                
				    
				    
				    
				    
				    
					
	/*            	
	            ArrayList<Integer> remainningnumbersofcomparison= new ArrayList<Integer>();
	            
	            for(int i=0;i<remainningnode.size();i++)
	            	remainningnumbersofcomparison.add(numbersofdata.get(remainningnode.get(i)));
	            
	            
	            
	            smallesttasknumber=Collections.min(remainningnumbersofcomparison);
				firstmintasknumber = remainningnumbersofcomparison.indexOf(smallesttasknumber);
				lastmintasknumber = remainningnumbersofcomparison.lastIndexOf(smallesttasknumber);
	            
	            
				for(int i=firstmintasknumber;i<lastmintasknumber+1;i++)
					if(remainningnumbersofcomparison.get(i)==smallesttasknumber)
						smallestnodes.add(i);               
	            
				for(int i=0;i<smallestnodes.size();i++)
					suitablenodes.add(numbersofcomparison.get(remainningnode.get(smallestnodes.get(i))));                
	            
				smallestdatanumber=Collections.min(suitablenodes);
				firstminIndex = suitablenodes.indexOf(smallestdatanumber);
			    lastminIndex = suitablenodes.lastIndexOf(smallestdatanumber);             
	          
	            
			    for(int i=firstminIndex;i<lastminIndex+1;i++)
					if((suitablenodes.get(i)==smallestdatanumber))
						choosennodes.add(remainningnode.get(smallestnodes.get(i)));
				    */
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
				    
	                
					if (choosennodes.isEmpty())
					{
						
						System.out.println("none");
						continue;
					}
									
					else
					{
					
						Collections.shuffle(choosennodes);
					
					for  (int machine : choosennodes)
					{
						if (numbersofdata.get(machine)==0)
						{
							dataitems.get(machine).add(d);
				    		
				    		numbersofdata.set(machine,dataitems.get(machine).size());
				    		
				    		timer=0;
				    		
				    		break;
									
						}
						
					}
					
					ArrayList<Integer> newaddnumberlist= new ArrayList<Integer>();
					
					ArrayList<Integer> newaddnumberlistdata= new ArrayList<Integer>();
					
					for(int i=0;i<choosennodes.size();i++)
						newaddnumberlistdata.add(numbersofdata.get(choosennodes.get(i)));
					
					
	                for (int machine : choosennodes)
						
					{
	                	
	                	if (numbersofdata.get(machine)!=0)
						{
			    			
			    			ArrayList<ArrayList<Integer>> addcomparison = new ArrayList<ArrayList<Integer>>();
			    			
			    			for(int i=0;i<numbersofdata.get(machine);i++)
			    			{
			    				ArrayList<Integer> temppair = new ArrayList<Integer>();
			    				
			    				if(dataitems.get(machine).get(i)<d)
			    				{
			    					temppair.add(dataitems.get(machine).get(i));
				    				temppair.add(d);
			    				}
			    				else
			    		
			    				{
			    					temppair.add(d);
			    					temppair.add(dataitems.get(machine).get(i));
			    				}
			    				

			    				addcomparison.add(temppair);
			    			}
			    				
			    			int numbers=0;
			    			for(int i=0;i<addcomparison.size();i++)
			    			{
			    						if(allthecomparison.contains(addcomparison.get(i)))
			    						{
			    							numbers++;		    						
			    						}	
			    			}
	                	
			    			if((maxcomparisonforeachnode.get(machine)-numbersofcomparison.get(machine))>numbers)
			    			{
			    				newaddnumberlist.add(numbers);	    				
			    			}
			    			else
			    				newaddnumberlist.add(maxcomparisonforeachnode.get(machine)-numbersofcomparison.get(machine));
						}
	            
					}
					
								
	                int biggestnode =0;
	                int firstbiggestnode=0;
	                int lastbiggestnode=0;
	                int biggestdata=0;
	                
	                ArrayList<Integer> choosenmostincreasing= new ArrayList<Integer>();
	                ArrayList<Integer> choosenmostincreasingdata= new ArrayList<Integer>();
	                
	                biggestnode=Collections.max(newaddnumberlist);
	               
	                firstbiggestnode = newaddnumberlist.indexOf(biggestnode);
	                lastbiggestnode = newaddnumberlist.lastIndexOf(biggestnode);
	                
	                
	                for(int i=firstbiggestnode;i<lastbiggestnode+1;i++)
	                {
	                	if(newaddnumberlist.get(i)==biggestnode)
	                		choosenmostincreasing.add(i);
	                	
	                }
	                
	                for(int i=0;i<choosenmostincreasing.size();i++)
	                {
	                	
	                	choosenmostincreasingdata.add(newaddnumberlistdata.get(choosenmostincreasing.get(i)));
	                	
	                }
	                	
	 //               biggestdata=Collections.max(choosenmostincreasingdata);
	                
	              biggestdata=Collections.min(choosenmostincreasingdata);
					
					for (int machine : choosennodes)
						
					{
						
						if ((numbersofdata.get(machine)!=0)&&newaddnumberlist.get(choosennodes.indexOf(machine))==biggestnode&&(numbersofdata.get(machine)==biggestdata))
					//	if ((numbersofdata.get(machine)!=0)&&newaddnumberlist.get(choosennodes.indexOf(machine))==biggestnode)	
					//	if ((numbersofdata.get(machine)!=0))	
						{
							
							
							boolean rollback=true;
			    			
			    			ArrayList<ArrayList<Integer>> addcomparison = new ArrayList<ArrayList<Integer>>();
			    			
			    			for(int i=0;i<numbersofdata.get(machine);i++)
			    			{
			    				ArrayList<Integer> temppair = new ArrayList<Integer>();
			    				
			    				if(dataitems.get(machine).get(i)<d)
			    				{
			    					temppair.add(dataitems.get(machine).get(i));
				    				temppair.add(d);
			    				}
			    				else
			    		
			    				{
			    					temppair.add(d);
			    					temppair.add(dataitems.get(machine).get(i));
			    				}
			    				

			    				addcomparison.add(temppair);
			    			}
			    				
			    			
			    			for(int i=0;i<addcomparison.size();i++)
			    			{
			    				
			    				/*if(numbersofcomparison.get(machine)==maxcomparison)
			    				{
			    					
			    					System.out.println("???");
			    					
			    					boolean found=false;
			    					int tasknumber=0;
			    					int findone=0;
			    					int findnode=0;
			    					
			    					for(int j=0;j<numbersofcomparison.get(machine);j++)
			    					{
			    						ArrayList<Integer> temptaskonmachine= new ArrayList<Integer>();
			 
			    						
			    						tasknumber=allthecomparisonstatic.indexOf(detailsofcomparisons.get(machine).get(j));
			    						
			    						
			   // 						temptaskonmachine=allthecomparisonplace.get(tasknumber);
			    						
			    						
			    						
			    						
			    						
			    						for(int z=0;z<allthecomparisonplace.get(tasknumber).size();z++)
			    						{
			    							
			    							temptaskonmachine.add(numbersofcomparison.get(allthecomparisonplace.get(tasknumber).get(z)));
			   
			    						}
			    								
			    						findone=temptaskonmachine.indexOf(Collections.min(temptaskonmachine));
			    						
			    						findnode=allthecomparisonplace.get(tasknumber).get(findone);
			    						
			    						if(findnode!=machine)
			    						{
			    							detailsofcomparisons.get(machine).remove(detailsofcomparisons.get(machine).get(j));	
	    									numbersofcomparison.set(machine, numbersofcomparison.get(machine)-1);	
			    							
	    									detailsofcomparisons.get(findnode).add(detailsofcomparisons.get(machine).get(j));	
	    									numbersofcomparison.set(findnode, numbersofcomparison.get(findnode)+1);		    				        	
			    				        	thecomparisonlocation.set(allthecomparisonstatic.indexOf(detailsofcomparisons.get(machine).get(j)), findnode);
			    							
			    				        	System.out.println("@@@");
			    							break;
			    						}
			    						
			    					}
			    					
			    				}	
			    				
			    				
			    				else
			    					
			    				{
			    					if(allthecomparison.contains(addcomparison.get(i)))
		    						{
		    							
		    							detailsofcomparisons.get(machine).add(addcomparison.get(i));						
		    					        allthecomparison.remove(addcomparison.get(i));
		    				        	numbersofcomparison.set(machine, numbersofcomparison.get(machine)+1);		    				        	
		    				        	thecomparisonlocation.set(allthecomparisonstatic.indexOf(addcomparison.get(i)), machine);
		    				        			    				        	
		    				        	alltheinputdatausedtime.set(addcomparison.get(i).get(0), alltheinputdatausedtime.get(addcomparison.get(i).get(0))-1);
		    				        	alltheinputdatausedtime.set(addcomparison.get(i).get(1), alltheinputdatausedtime.get(addcomparison.get(i).get(1))-1);
			    				        	
		    					        rollback=false;
		    							
		    						}	
			    					*/
			    					
			    					
			    					
			    				
			    				
			    				
			    				
			    				
			    				
			    				
			    				
			    				
			    				
			    				if(numbersofcomparison.get(machine)<maxcomparisonforeachnode.get(machine))
			    				{
			    						if(allthecomparison.contains(addcomparison.get(i)))
			    						{
			    							
			    							detailsofcomparisons.get(machine).add(addcomparison.get(i));						
			    					        allthecomparison.remove(addcomparison.get(i));
			    				        	numbersofcomparison.set(machine, numbersofcomparison.get(machine)+1);		    				        	
			    				        	thecomparisonlocation.set(allthecomparisonstatic.indexOf(addcomparison.get(i)), machine);
			    				        			    				        	
			    				        	alltheinputdatausedtime.set(addcomparison.get(i).get(0), alltheinputdatausedtime.get(addcomparison.get(i).get(0))-1);
			    				        	alltheinputdatausedtime.set(addcomparison.get(i).get(1), alltheinputdatausedtime.get(addcomparison.get(i).get(1))-1);
				    				        	
			    					        rollback=false;
			    							
			    						}	
			    				}
			    				
			    				else
			    				{
			    					
			    			//		System.out.println("happen");
			    					
			    				}
			    				
			    				
			    			
			    				
			    				
			    				
			    				
			    				
			    				
			    				
			    			/*	else
			    				{
			    					boolean found=false;
			    					int tasknumber=0;
			    					int findone=0;
			    					int findnode=0;
			    					
			    					for(int j=0;j<numbersofcomparison.get(machine);j++)
			    					{
			    						ArrayList<Integer> temptaskonmachine= new ArrayList<Integer>();
			 
			    						
			    						tasknumber=allthecomparisonstatic.indexOf(detailsofcomparisons.get(machine).get(j));
			    						
			    						
			   // 						temptaskonmachine=allthecomparisonplace.get(tasknumber);
			    						
			    						
			    						
			    						
			    						
			    						for(int z=0;z<allthecomparisonplace.get(tasknumber).size();z++)
			    						{
			    							
			    							temptaskonmachine.add(numbersofcomparison.get(allthecomparisonplace.get(tasknumber).get(z)));
			   
			    						}
			    								
			    						findone=temptaskonmachine.indexOf(Collections.min(temptaskonmachine));
			    						
			    						findnode=allthecomparisonplace.get(tasknumber).get(findone);
			    						
			    						if(findnode!=machine)
			    						{
			    							detailsofcomparisons.get(machine).remove(detailsofcomparisons.get(machine).get(j));	
	    									numbersofcomparison.set(machine, numbersofcomparison.get(machine)-1);	
			    							
	    									detailsofcomparisons.get(findnode).add(detailsofcomparisons.get(machine).get(j));	
	    									numbersofcomparison.set(findnode, numbersofcomparison.get(findnode)+1);		    				        	
			    				        	thecomparisonlocation.set(allthecomparisonstatic.indexOf(detailsofcomparisons.get(machine).get(j)), findnode);
			    							
			    							break;
			    						}
			    						
			    					}
			    						
			    							
			    		
			    				}
			    					
			    					*/
			    					
			    					
			    					
			    			
			    				
			    				
			    			}
			    			
			    			
			    			if (!rollback)
			    			{
			    				
		//	    				dataitems.get(machine).add(d);
		//	    				numbersofdata.set(machine,dataitems.get(machine).size());
		//	    				size=allthecomparison.size();
			    				
			    				
			    				
			    				
			    				for(int i=0;i<addcomparison.size();i++)
				    			{
				    				int j=0;
				    				j=allthecomparisonstatic.indexOf(addcomparison.get(i));
				    				
				    				allthecomparisonplace.get(j).add(machine);			    				
				    			
				    			}		
			    				
			    				
			    				
			    				
			    			    
			    				if(numbersofcomparison.get(machine)<maxcomparisonforeachnode.get(machine))
			    				{
			    					
			    					for(int i=0;i<addcomparison.size();i++)
			    						{
			    						if(!detailsofcomparisons.get(machine).contains(addcomparison.get(i))&&(numbersofcomparison.get(machine)<maxcomparisonforeachnode.get(machine)))
			    							{
			    							int node=0;
			    							int nodetwo=0;
			    							
			    			//				node=thecomparisonlocation.get(allthecomparisonstatic.indexOf(addcomparison.get(i)));
			    							
			    							node=thecomparisonlocation.get(allthecomparisonstatic.indexOf(addcomparison.get(i)));
			    							
			    							ArrayList<Integer> allchoise = new ArrayList<Integer>();
			    							
			    							
			    							for(int t=0;t<allthecomparisonplace.get(allthecomparisonstatic.indexOf(addcomparison.get(i))).size();t++)
			    							{
			    								allchoise.add(allthecomparisonplace.get(allthecomparisonstatic.indexOf(addcomparison.get(i))).get(t));
			    		
			    							}
			    							
			    							
			    							
	/*		    							if(node!=m)
			    							{
			    							
			    							
			    							nodetwo=node;
			    				
			    							for(int p=0;p<allchoise.size();p++)
			    							{
			    								
			    							//	(numbersofcomparison.get(remainningnode.get(i))*100/maxcomparisonforeachnode.get(remainningnode.get(i)))
			    								
			    								
			    								if((numbersofcomparison.get(allchoise.get(p))*100/maxcomparisonforeachnode.get(allchoise.get(p)))<(numbersofcomparison.get(nodetwo)*100/maxcomparisonforeachnode.get(nodetwo)))
			    								
			    								
			    							//	if(numbersofcomparison.get(allchoise.get(p))<numbersofcomparison.get(nodetwo))
			    								{
			    									nodetwo=allchoise.get(p);
			    								}
			    								
			    								
			    								
			    							}
			    							
			    					//		System.out.println(nodetwo);
			    					//		System.out.println(node);
			    							
			    							
			    							
			    							if(nodetwo!=node)
			    							{
			    								detailsofcomparisons.get(node).remove(addcomparison.get(i));	
		    									numbersofcomparison.set(node, numbersofcomparison.get(node)-1);	
		    									
		    									
		    									
		    									
		    									
		    									
		    									
		    									if(allthecomparison.size()<100)
		    									{
		    									for(int k=0;k<allthecomparison.size();k++)
		    				    				{
		    										
		    					
	    				    					if((numbersofcomparison.get(node)<maxcomparisonforeachnode.get(node))&&((allthecomparisonplace.get(allthecomparisonstatic.indexOf(allthecomparison.get(k)))).contains(node))&&( thecomparisonlocation.get(allthecomparisonstatic.indexOf(allthecomparison.get(k)))==m))
		    				    							{
		    				    						
	    				    						
	    				    						
	    				    						
		    				    						detailsofcomparisons.get(node).add(allthecomparison.get(k));	
		    	    									numbersofcomparison.set(node, numbersofcomparison.get(node)+1);		    				        	
		    			    				        	thecomparisonlocation.set(allthecomparisonstatic.indexOf(allthecomparison.get(k)), node);
		    			    				        	allthecomparison.remove(allthecomparison.get(k));
		    			    			        	
		    			  
		    				    						
		    				    						break;
		    				    					//	
		    				    						
		    				    							}
		    				    					
		    				    					
		    				    				}
		    									
		    									
		    									}
		    									
		    									
		    									
		    									
		    									
		    									
		    									
		    									boolean movedataone=true;
		    									boolean movedatatwo=true;
		    									
		    									for(int j=0;j<numbersofcomparison.get(node);j++)
		    									{
		    										if(detailsofcomparisons.get(node).get(j).contains(addcomparison.get(i).get(0)))
		    											movedataone=false;
		    										
		    										if(detailsofcomparisons.get(node).get(j).contains(addcomparison.get(i).get(1)))
		    											movedatatwo=false;
		    									}
		    									
		    									
		    									if(movedataone)
		    									{
		    										dataitems.get(node).remove(addcomparison.get(i).get(0));
		    										
		    										numbersofdata.set(node,dataitems.get(node).size());
		    										System.out.println("!!");
		    										
		    									}
		    									
		    									if(movedatatwo)
		    									{
		    										
		    										dataitems.get(node).remove(addcomparison.get(i).get(1));
		    										numbersofdata.set(node,dataitems.get(node).size());
		    										System.out.println("!!!");
		    										
		    									}
		    								
		    									
		    									
		    									
		    									detailsofcomparisons.get(nodetwo).add(addcomparison.get(i));	
		    									numbersofcomparison.set(nodetwo, numbersofcomparison.get(nodetwo)+1);		    				        	
				    				        	thecomparisonlocation.set(allthecomparisonstatic.indexOf(addcomparison.get(i)), nodetwo);
		    									
		    								}
			    								
			    								
			    								
			    								
			    								
			    							}*/
			    							
			    							
			    						
			    							
			    							
			    							
			    							
			    								
			    								
			    				//			}
			    							
			    							
			    							
			    							
			    							
			    							
			    							
			    							
			    							
			    							
			    							
			    							
			    							
			    							
	/*		    							
			    							int z=0;
			    							z=allthecomparisonstatic.indexOf(addcomparison.get(i));		    										
			    							
			    							ArrayList<Integer> avaliblenodes = new ArrayList<Integer>();
			    									
			    									avaliblenodes=allthecomparisonplace.get(z);		    	
			    							
			    							ArrayList<Integer> avaliblenodesnumber = new ArrayList<Integer>();
			    							
			    							for(int p =0;p<avaliblenodes.size();p++)
			    								
			    								avaliblenodesnumber.add(numbersofcomparison.get(avaliblenodes.get(p)));
			    								
			    							int choosenleast=0;

			    							choosenleast=Collections.min(avaliblenodesnumber);
								
			    							nodetwo=avaliblenodes.get(avaliblenodesnumber.indexOf(choosenleast));
			    								

			    							if(node!=m)
			    										    										    								    								
			    							{
			    								
			    								if(node!=nodetwo)
			    								{
			    								//	System.out.println(node);
			    									detailsofcomparisons.get(node).remove(addcomparison.get(i));	
			    									numbersofcomparison.set(node, numbersofcomparison.get(node)-1);	
			    									
			    									
			    									
			    									
			    									
			    									boolean movedataone=true;
			    									boolean movedatatwo=true;
			    									
			    									for(int j=0;j<numbersofcomparison.get(node);j++)
			    									{
			    										if(detailsofcomparisons.get(node).get(j).contains(addcomparison.get(i).get(0)))
			    											movedataone=false;
			    										
			    										if(detailsofcomparisons.get(node).get(j).contains(addcomparison.get(i).get(1)))
			    											movedatatwo=false;
			    									}
			    									
			    									
			    									if(movedataone)
			    									{
			    										dataitems.get(node).remove(addcomparison.get(i).get(0));
			    										
			    										numbersofdata.set(node,dataitems.get(node).size());
			    										System.out.println("!!");
			    										
			    									}
			    									
			    									if(movedatatwo)
			    									{
			    										
			    										dataitems.get(node).remove(addcomparison.get(i).get(1));
			    										numbersofdata.set(node,dataitems.get(node).size());
			    										System.out.println("!!!");
			    										
			    									}
			    								
			    									
			    									
			    									
			    									detailsofcomparisons.get(nodetwo).add(addcomparison.get(i));	
			    									numbersofcomparison.set(nodetwo, numbersofcomparison.get(nodetwo)+1);		    				        	
					    				        	thecomparisonlocation.set(allthecomparisonstatic.indexOf(addcomparison.get(i)), nodetwo);
			    									
			    									
			    									
			    									
			    									
			    									
			    								}*/
			    								
			    								
			    								
			    								
			    								
			    								
			    							
			    								
			    								
			    								
			    								
			    								
			    								
			    								
			    								if(node!=m)
				    								
				    							{
				    									
			    								
			    								
			    								
			    								
			    								
			    								
			    								
			    								
			    								if(numbersofcomparison.get(node)>numbersofcomparison.get(machine))
			    								{
			    									detailsofcomparisons.get(node).remove(addcomparison.get(i));	
			    									numbersofcomparison.set(node, numbersofcomparison.get(node)-1);	
			    									
			    									
			    									
			    									
			    									
			    									boolean movedataone=true;
			    									boolean movedatatwo=true;
			    									
			    									for(int j=0;j<numbersofcomparison.get(node);j++)
			    									{
			    										if(detailsofcomparisons.get(node).get(j).contains(addcomparison.get(i).get(0)))
			    											movedataone=false;
			    										
			    										if(detailsofcomparisons.get(node).get(j).contains(addcomparison.get(i).get(1)))
			    											movedatatwo=false;
			    									}
			    									
			    									
			    									if(movedataone)
			    									{
			    										dataitems.get(node).remove(addcomparison.get(i).get(0));
		    										
			    										numbersofdata.set(node,dataitems.get(node).size());
			    										System.out.println("!!");
			    										
			    									}
			    									
			    									if(movedatatwo)
			    									{
			    										
			    										dataitems.get(node).remove(addcomparison.get(i).get(1));
		    										numbersofdata.set(node,dataitems.get(node).size());
			    										System.out.println("!!!");
			    										
			    									}
			    								
			    									
			    									
			    									
			    									detailsofcomparisons.get(machine).add(addcomparison.get(i));	
			    									numbersofcomparison.set(machine, numbersofcomparison.get(machine)+1);		    				        	
					    				        	thecomparisonlocation.set(allthecomparisonstatic.indexOf(addcomparison.get(i)), machine);
			    									
			    								}
			
			    							}
			    							
			    						
			    					        }
			    				
			    						}
			    		
			    				}
			    				
				
			    				dataitems.get(machine).add(d);
			    				numbersofdata.set(machine,dataitems.get(machine).size());
			    				size=allthecomparison.size();
			    				timer=0;
			    				break;
			    			}
			    				
			    			
			    			if(timer>n)
			    			{
			    				System.out.println(remainingdata.get(0));
			    				System.out.println(remainingdata.get(1));
			    				System.out.println(size);
			    				System.out.println(machine);
			    				System.out.println(numbersofcomparison.get(machine));
			//    				for(int u=0;u<numbersofdata.get(machine);u++)
			//    					System.out.println(dataitems.get(machine).get(u));
			    				
			    				
			    				
			    		//		min=Collections.max(numbersofdata);  		
			           // 		shownumbersofcomparison=numbersofcomparison;
			            //		shownumbersofdata=numbersofdata;	
			    				
			    				
			    			
			    				
			    				
			    				
			    				
			    				
			    				
			    				
			    				System.out.println("max number among nodes temp");
			    				System.out.println(Collections.max(numbersofdata));
			    				
			    				
			    				System.out.println("numbers of data in each node temp");
			    				for(int i=0;i<m;i++)
			    					System.out.println(numbersofdata.get(i));
			    				
			    				System.out.println("numbers of comparisons in each node temp");
			    				for(int i=0;i<m;i++)
			    					System.out.println(numbersofcomparison.get(i));
			    				
			    				
			    				System.out.println(d);
			    				
			    				dataitems.get(machine).add(d);
			    				numbersofdata.set(machine,dataitems.get(machine).size());
			    				timer=0;
			    				System.out.println("!!!!!!!!!!!!!!!!!!!");

			    			}	

						}	
					}	
					}

					

				}
	            timer++;
			}		
		}
		
		
		
		
		void GeneralDistribute(int m,int n,int k)
		{
			
				
			
			
			
		}
		
		
		
		static void initialization(int m, int n, int k)
		{
			
			//calculate Q
					if((n+k)*(n+k-1)%(2*m)==0)
						maxcomparison=(n+k)*(n+k-1)/(2*m);
					else
						maxcomparison=(n+k)*(n+k-1)/(2*m)+1;
					
					allthenodes = new ArrayList<Integer>();
					for(int i=0;i<m;i++)
						allthenodes.add(i);
				
					//List saves all the input data
			        alltheinputdata= new ArrayList<Integer>();
					
					for(int i=n;i<n+k;i++)
						alltheinputdata.add(i);				
					
					//List saves all the used number of each file
					alltheinputdatausedtime= new ArrayList<Integer>();
					
					for(int i=0;i<k;i++)
						alltheinputdatausedtime.add(n+k-1);
						
					
					
					//List saves all the comparison tasks
					allthecomparison = new ArrayList<ArrayList<Integer>>();
					ArrayList<Integer> comparisonitem;
					
					for(int j=n;j<n+k;j++)
						for(int z=0;z<n+k;z++)
						{
							if(z<j)
							{
								comparisonitem=new ArrayList<Integer>();
							    comparisonitem.add(z);
							    comparisonitem.add(j);
							    allthecomparison.add(comparisonitem);	
							}
							if(z>j)
							{
								comparisonitem=new ArrayList<Integer>();
							    comparisonitem.add(j);
							    comparisonitem.add(z);
							    allthecomparison.add(comparisonitem);	
							}
							
						}
					
					
					//List save all the comparison tasks----keep
					
					allthecomparisonstatic = new ArrayList<ArrayList<Integer>>();
					ArrayList<Integer> comparisonitems;
					
					for(int j=n;j<n+k;j++)
						for(int z=0;z<n+k;z++)
						{	
							if(z<j)
							{
								comparisonitems=new ArrayList<Integer>();
								comparisonitems.add(z);
								comparisonitems.add(j);
								allthecomparisonstatic.add(comparisonitems);	
							}
							
							if(z>j)
							{
								comparisonitems=new ArrayList<Integer>();
								comparisonitems.add(j);
								comparisonitems.add(z);
								allthecomparisonstatic.add(comparisonitems);	
							}																		
						}
					
					
					//List saves all the places that can do the comparison tasks
					
					
					allthecomparisonplace = new ArrayList<ArrayList<Integer>>();
					ArrayList<Integer> comparisonplace;
					
					for(int j=n;j<n+k;j++)
						for(int z=0;z<n+k;z++)
						{
							if(j!=z)
							{
								comparisonplace=new ArrayList<Integer>();
							    allthecomparisonplace.add(comparisonplace);								
							}						
						}
					
					
					//List save the place where do the tasks
					thecomparisonlocation = new ArrayList<Integer>();
					
					for(int j=n;j<n+k;j++)
						for(int z=0;z<n+k;z++)
						{
							if(j!=z)
							{
								thecomparisonlocation.add(m);
							}						
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
		
		
		
		
		
		
		
		
		static int sumup(ArrayList<Integer> dataitem)
		{
			int sum=0;
			
			for(int i=0;i<dataitem.size();i++)
			{
				sum=sum+dataitem.get(i);
				
				
			}
			
			
			
			
			return sum;
			
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		    static ArrayList<ArrayList<ArrayList<Integer>>> detailsofcomparisons;
			static ArrayList<ArrayList<Integer>> dataitems;
			static ArrayList<ArrayList<Integer>> allthecomparison;
			static ArrayList<ArrayList<Integer>> allthecomparisonplace;
			static ArrayList<ArrayList<Integer>> allthecomparisonstatic;
			
			
			static ArrayList<Integer> computingpowerforeachnode;
			static ArrayList<Integer> maxcomparisonforeachnode;
			static int totalcomputingpower;
			
			
			static ArrayList<Integer> thecomparisonlocation;
			static ArrayList<Integer> alltheinputdatausedtime;
			static ArrayList<Integer> numbersofcomparison;
			static ArrayList<Integer> numbersofdata;
			static ArrayList<Integer> alltheinputdata;
			static ArrayList<Integer> allthenodes;
			static int maxcomparison;
			static ArrayList<Integer> smallestnodes;			
			static ArrayList<Integer> suitablenodes;
			static ArrayList<Integer> firstchoosennodes;
			static ArrayList<Integer> choosennodes;
		
			
			
			
			
			
			
			SecondInitial(){
				
				int M=4;
				int N=31;
				int min=1000;
				int sum=0;
				int minsum=1000;

				ArrayList<Integer> shownumbersofcomparison= new ArrayList<Integer>();
				ArrayList<Integer> shownumbersofdata = new ArrayList<Integer>();
				
//				ArrayList<ArrayList<ArrayList<Integer>>> showcomparisons = new ArrayList<ArrayList<ArrayList<Integer>>>();
//				ArrayList<ArrayList<Integer>> showdata= new ArrayList<ArrayList<Integer>>();
			

			for (int i = 0; i <1; i++)
		            
		            { 
					
		//		System.out.println("begin");
					    DistributionStrategy(M,N);	  
					    sum=sumup(numbersofdata);
	     //       System.out.println("end");
		            	if(Collections.max(numbersofdata)<min)
		            	{
		            		min=Collections.max(numbersofdata);  		
		            		
		            		if(sum<minsum)
		            		{
		            			minsum=sum;
		            		}
		            		
		            		
	//	            		shownumbersofcomparison=numbersofcomparison;
	//	            		shownumbersofdata=numbersofdata;	
		            //		System.out.println(i);
		            		
		            		
		            		
		            		
		            	
	//	            		showcomparisons=detailsofcomparisons;
	//	            		showdata=dataitems;
		            		
		            		
		            		
		            		
		            		
		            	}
		            	
	            	
		            	
		            }
				
				
				
			}
			
			



	}



