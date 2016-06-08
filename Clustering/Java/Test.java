import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;




public class Test extends ComputingSystem<String,ArrayList<String>,ArrayList<String>,Double> {

	public static List<String> alllines;
	public static double[][] A = new double[2000][2000];
	
	HashMap<String,ArrayList<String>> Reader() throws IOException
	{//need a parameter to tell the lower index, i and j, for A_ij, W_i and H_j
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,ArrayList<String>> finalmap = new HashMap<String,ArrayList<String>>();
		
		
		

		
		
		
		//call getAllLines to initialize alllines
		String curline = "";
		if(!alllines.isEmpty()){
			curline = alllines.get(0);
			alllines.remove(0);
			System.out.println(curline);
			
		}else{
			System.out.println("All A are finished!!");
		}
		
		System.out.println(curline);
		
		String contentA = "";//is not used for sparse format
		String contentW = "";
		String contentH = "";
		int itr = 1;
		
		//read content A
		/*BufferedReader br = new BufferedReader(new FileReader("E:/research/subspace clustering/SEF discipline project/code/index.txt"));
		String line;
		
		int lastrow = 0;
		int filecount = 0;

		while ((line = br.readLine()) != null) {
			//System.out.println(line);
			
		}
		br.close();
		*/
		
		//inside while{}
		String[] tmp = curline.split("-");//line A index
		//System.out.println(tmp[0] + "  " + tmp[1]);//tmp[0] column index, tmp[1] row index
		
		/////////////////////sparse format input of A////////////////////////////
		
		//construct matrix A
		//double[][] A = new double[2000][2000];
		//read content A
		String filea = "/usr/local/blocklargesynmatrix" + curline;//E:/synmatrix/blocklargesynmatrix/
		File file = new File(filea);
		if (file.exists()) {
			BufferedReader brA = new BufferedReader(new FileReader(file));
			String lineA;
			while ((lineA = brA.readLine()) != null) {
				//System.out.println(lineA);
				String[] str = lineA.split(",");
				int row = Integer.parseInt(str[0]);
				int column = Integer.parseInt(str[1]);
				double entry = Double.parseDouble(str[2]);
				int highrow = row/50;//50 is row block number
				int highcolumn = column/10;//10 is column block number
				int startrow = highrow*50;
				int startcolumn = highcolumn*10;
				A[row-startrow][column-startcolumn] = entry;
				//contentA += lineA + ";"; //; is the line break mark and within one line, the element is separated by " " or Tab
			}
			brA.close();
		}else{
			System.out.println("File " + filea + " does not exist");
		}
		
		//////////////////////dense format input ofA//////////////////////////////////////
		
		//read content W
		String filew = "/usr/local/largesynmatrixresult/wmatrix/" + (itr-1) + "/w" + tmp[1];//E:/NMFmesos/W/
		file = new File(filew);
		if (file.exists()) {
			BufferedReader brW = new BufferedReader(new FileReader(file));
			String lineW;
			while ((lineW = brW.readLine()) != null) {
				//System.out.println(lineW);
				contentW += lineW + ";"; //; is the line break mark and within one line, the element is separated by " " or Tab 
			}
			brW.close();
		}else{
			System.out.println("File " + filew + " does not exist");
		}
		
		//read content H
		String fileh = "/usr/local/largesynmatrixresult/hmatrix/" + (itr-1) + "/h" + tmp[0];//E:/NMFmesos/H/
		file = new File(fileh);
		if (file.exists()) {
			//check if one row is finished, i.e. tmp[1] is changed or not
			//if(filecount == 0 || lastrow != Integer.parseInt(tmp[1])){
			//}
			BufferedReader brH = new BufferedReader(new FileReader(file));
			String lineH;
			while ((lineH = brH.readLine()) != null) {
				//System.out.println(lineH);
				contentH += lineH + ";"; //; is the line break mark and within one line, the element is separated by " " or Tab 
			}
			brH.close();
		}else{
			System.out.println("File " + fileh + " does not exist");
		}
		
		//update row index at the end
		//lastrow =Integer.parseInt(tmp[1]);
		//filecount++;
		////////////////end of while{}
		
		//concatenate content of A_ij, W_i and H_j for the value of HashMap<String,String> map
		//to be noted, there is only one set of <k,v> in this map
		String value = contentW + "/+" + contentH;//contentA + "+" + 
		map.put(curline, value);
		
		
		
		
		/*                     */
		
		ArrayList<String> firstline =  new ArrayList<String>();
		firstline.add(value);
		
		ArrayList<String> secondline =  new ArrayList<String>();
		secondline.add(curline);
		
		
		
		finalmap.put(curline,firstline);
	    finalmap.put(curline+"1",secondline);	
	    
	    
//    System.out.println(firstline.size());
	    
	    
	    
	    
		
		
//		List<String> ll = new ArrayList<String>();
//		ll.add(map.get("0-0-0"));
//		Compare(ll, new ArrayList<String>(map.keySet()));
		
		
		
		
		//////////////////testing purpose/////////////////////////
//		String v = map.get(curline);
//		System.out.println(v);
		//String[] str = v.split("-");
		//System.out.println(str[0]);
		//System.out.println(str[1]);
		//System.out.println(str[2]);
		/////////////////////////////////////////////////////////
		
	//	return map; 
		return finalmap;       
	}
	

	
	
	
    
	public ArrayList<String> Preprocess(ArrayList<String> map){
		
	//	System.out.println(map.size());
		

		

		return map;
	}
	
	
	
	
	
	
	
	
	
	
	public Double Compare(ArrayList<String> lines, ArrayList<String> fileIndex)//input is one line of map, the content of A_ij, W_i and H_j
    {
		
		
		
		
		////////////////////construct A, W and H////////////////////////////
		//let X be m by n, W m by k, H n by k
		String v = lines.get(0);
		
		
		
		
		String[] str = v.split("/+");//sparse format only contains two: str[0] W, str[1] H, dense format has three: str[0] A, str[1] W, str[0] H
		
		//generate matrix A, W and H
		//construct matrix A
		//double[][] A = new double[2000][2000];
		//String[] wline = str[0].split(";");//; line break
		//for(int i = 0; i < A.length; i++)
		//{
		//	String[] temp = wline[i].split(",");//, element break
		//	for(int j = 0; j < A[0].length; j++){
		//		A[i][j] = Double.parseDouble(temp[j]);
		//	}
		//}
		//construct matrix W
		double[][] W = new double[2000][10];//10 is the value of k
		String[] wline = str[0].split(";");//; line break
		for(int i = 0; i < W.length; i++)
		{
			String[] temp = wline[i].split(",");//, element break
			for(int j = 0; j < W[0].length; j++){
				//W[i][j] = Double.parseDouble(temp[j]);
				//System.out.println(temp[j]);
				W[i][j] = new BigDecimal(temp[j]).doubleValue();
			}
		}
		//get W'
		double[][] WT = transpose(W);		
		//construct matrix H
		double[][] H = new double[2000][10];//10 is the value of k
		wline = str[1].split(";");//; line break
		for(int i = 0; i < H.length; i++)
		{
			String[] temp = wline[i].split(",");//, element break
			for(int j = 0; j < H[0].length; j++){
				H[i][j] = Double.parseDouble(temp[j]);
			}
		}
		//get H'
		double[][] HT = transpose(H);
		
		////////////////////update of H//////////////////////////////////////
		//define intermediate matrices
		double[][] S = new double[10][10];// k by k
		double[][] X = new double[10][2000];//k by n
		double[][] Y = new double[10][2000];//k by n
		double[][] HTnew = new double[10][2000];//n by k
		
		//calculate S=w'w
		S = multiply(WT,W);
		
		//calculate X=w'A
		X = multiply(WT,A);
		
		//calculate Y=SHT
		Y = multiply(S,HT);
		
		//caluculate HT = HT*(X/Y) entry-wise
		for(int i = 0; i < HT.length; i++)
		{
			for(int j = 0; j < HT[0].length; j++){
				if(Y[i][j] == (double) 0){
					HTnew[i][j] = (double) 0;
				}else{
					HTnew[i][j] = HT[i][j]*(X[i][j]/Y[i][j]);
				}	
			}
		}
		
		double[][] Hnew = transpose(HTnew);
		////////////////////update of W//////////////////////////////////////
		//define intermediate matrices
		double[][] P = new double[10][10];// k by k
		double[][] U = new double[10][2000];//k by n
		double[][] V = new double[10][2000];//k by n
		double[][] Wnew = new double[2000][10];//n by k
		
		//calculate P=hh'
		P = multiply(HTnew,H);//use HTnew instead of HT
		
		//calculate U=Ah'
		U = multiply(A,Hnew);
		
		//calculate V=wP
		V = multiply(W,P);
		
		//caluculate W = W*(U/V) entry-wise
		for(int i = 0; i < W.length; i++)
		{
			for(int j = 0; j < W[0].length; j++){
				if(V[i][j] == (double) 0){
					Wnew[i][j] = (double) 0;
				}else{
					Wnew[i][j] = W[i][j]*(U[i][j]/V[i][j]);
				}	
			}
		}
		//////////////////////END of Update/////////////////////////////////////////
		
		////////////////////write Hnew and Wnew into Disk/////////////////////////
		//need the value of itr to write into folder W/itr/ or H/itr/
		//also need the key of map, curline, to write as the W_i and H_j 
		String[] index = fileIndex.get(0).split("-");//index[0] is i and index[1] is j
		System.out.println(index[0]+"!!!!!!!!!!");
		////////////////////////
		
    	return 123.00;
    }
    
	public static void cacheWH(int itr) throws IOException{
		//int itr = 1;
		String[] w = new String[50];
		String[] h = new String[10];
		File file;
		File folder = new File("E:/research/subspace clustering/SEF discipline project/code/W/" + itr);
		File[] listOfFiles = folder.listFiles();		
		
		//read content W
	    for (int i = 0; i < listOfFiles.length; i++) {
	        if (listOfFiles[i].isFile()) {
	        	//System.out.println("File " + listOfFiles[i].getName());
	  			String filew = listOfFiles[i].getName();
	  			file = new File(filew);
				if (file.exists()) {
					BufferedReader brW = new BufferedReader(new FileReader(file));
					String lineW;String contentW = "";
					while ((lineW = brW.readLine()) != null) {
						System.out.println(lineW);
						contentW += lineW; 
					}
					brW.close();
					w[i] = contentW;//content of w_i
				}else{
					System.out.println("File " + filew + " does not exist");
				}
	        } else if (listOfFiles[i].isDirectory()) {
	          System.out.println("Directory " + listOfFiles[i].getName());
	        }
	      }
		
		//read content H
	    folder = new File("E:/research/subspace clustering/SEF discipline project/code/H/" + itr);
	    for (int i = 0; i < listOfFiles.length; i++) {
	        if (listOfFiles[i].isFile()) {
	        	//System.out.println("File " + listOfFiles[i].getName());
	    		String fileh = listOfFiles[i].getName();
	    		file = new File(fileh);
	    		if (file.exists()) {
    				BufferedReader brH = new BufferedReader(new FileReader(file));
    				String lineH;String contentH = "";
    				while ((lineH = brH.readLine()) != null) {
    					System.out.println(lineH);
    					contentH += lineH; 
    				}
    				brH.close();
    				h[i] = contentH;//content of h_i
	    		}else{
	    			System.out.println("File " + fileh + " does not exist");
	    		}
	        } else if (listOfFiles[i].isDirectory()) {
	          System.out.println("Directory " + listOfFiles[i].getName());
	        }
	      }
		 
	    //at the end we get content of all W and H block matrices in two array
	    //we can set two global array for this and update them at the end of each iteration
	}
	
//	private static void checkPath(Path fileout1, FileSystem fs)
//			throws IOException {
//		if (fs.exists(fileout1))
//			fs.delete(fileout1, true);
//	}
	public static void getAllLine() throws IOException{
		//F:\NMFmesos
		//E:/research/subspace clustering/SEF discipline project/code/index.txt
		List<String> lines = Files.readAllLines(Paths.get("/usr/local/index.txt"), StandardCharsets.UTF_8);//E:/NMFmesos/index.txt
		alllines = lines;
		//System.out.println(lines.size());
		//System.out.println(lines.get(0));
		//lines.remove(0);
		//System.out.println(lines.size());
		//System.out.println(lines.get(0));
		//lines.clear();
		//System.out.println(lines.isEmpty());
	}
//	public static void main(String[] args) throws IOException {
//		// TODO Auto-generated method stub
//		//Reader();
//		//getAllLine();
//		//for(int i =0; i<10; i++){
//		//	Reader();
//		//}
//		//Reader();
//		
//		//double a = new BigDecimal("1.9648812243244773E-4").doubleValue();
//		//System.out.println(a);
//		
//		long start = System.currentTimeMillis();
//		long end = 0;
//		long time = 0;
//		double totaltime = 0.0;
//		
//		getAllLine();
//		HashMap<String,String> map = Reader();
//		List<String> ll = new ArrayList<String>();
//		ll.add(map.get("0-0-0"));
//		Compare(ll, new ArrayList<String>(map.keySet()));
//		
//		end = System.currentTimeMillis();
//		time = end - start;
//		totaltime = (double)time/1000;
//		System.out.println("1 passed time: " + totaltime);
//		
//		
//	}

	//matrix operation functions
	
    // return C = A * B
    public static double[][] multiply(double[][] A, double[][] B) {
        int mA = A.length;
        int nA = A[0].length;
        int mB = B.length;
        int nB = B[0].length;
        if (nA != mB) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] C = new double[mA][nB];
        for (int i = 0; i < mA; i++)
            for (int j = 0; j < nB; j++)
                for (int k = 0; k < nA; k++)
                    C[i][j] += (A[i][k] * B[k][j]);
        return C;
    }
    // return C = A^T
    public static double[][] transpose(double[][] A) {
        int m = A.length;
        int n = A[0].length;
        double[][] C = new double[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[j][i] = A[i][j];
        return C;
    }
}
