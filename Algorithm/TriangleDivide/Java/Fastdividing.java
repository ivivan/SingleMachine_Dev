import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Fastdividing {
	
	public HashSet<?>[] files;
	public int[] allocated;
	public int N, M, max_files, max_compares, target_compares, rows, cols;
	public int[][] compare;

	int min(int a, int b){
		if (a < b)
			return a;
		else
			return b;
	}

	int max(int a, int b){
		if (a > b)
			return a;
		else
			return b;
	}

	public int Tile(){
		max_compares = (int) Math.ceil(N * (N - 1) / 2.0 / M);
		int P1 = (int)Math.ceil(Math.sqrt(max_compares));
		int P2 = (int)Math.ceil(max_compares * 1.0 / P1);
		max_files = P1 + P2;
		rows = P2;

		cols = (int)Math.floor(max_compares * 1.0 / rows);
		
//		System.out.println(rows);
//		System.out.println(cols);

		TryTile();
		return max_files;
	}

	public void TryTile(){
		target_compares = rows * cols;

		proc = 0;
		compare_count = 0;
		used_files = new HashSet<Integer>();
//		files = new HashSet<Integer>[M+10];
				
		files = new HashSet<?>[M+10];
		for (int i = 0; i < M+10; i++)
			files[i] = new HashSet<Integer>();	
		
		
		allocated = new int[M+10];
		for (int m = 0; m < M+10; m++){
			allocated[m] = 0;
		}

		compare = new int[N][N];
		for (int y = 0; y < N; y++)
			for (int x = 0; x < N; x++)
				compare[x][y] = -1;

		for (int y = 0; y < N; y += rows)
			R2L(y);

		List<boolean[]> options = new ArrayList<boolean[]>();
		int unallocated = 0;
		for (int y = 0; y < N; y++)
			for (int x = y + 1; x < N; x++)
				if (compare[x][y] == -1){
					compare[x][y] = 0;
					boolean[] option = new boolean[M];
					for (int m = 0; m<M; m++)
						option[m] = allocated[m] < max_compares && files[m].contains(x) && files[m].contains(y);
					options.add(option);
				}

		// can improve
		boolean[] consider = new boolean[M];
		for (int m = 0; m<M; m++){
			consider[m] = false;
			for (boolean[] option: options)
				if (option[m]){
					consider[m] = true;
					break;
				}
		}

/*		for (int m=0; m<M; m++)
			if (consider[m]){
			//	Console.Write("{0}: ", m);
				int demand = 0;
				for (boolean[] option:options){
			//		Console.Write("{0},", option[m] ? 1 : 0);
					if (option[m])
						demand++;
				}
			//	Console.WriteLine(" capacity = {0}, demand = {1}", max_compares - allocated[m], demand);
			}*/

	//	Console.WriteLine();
	}



	int proc = 0;
	int compare_count = 0;
	HashSet<Integer> used_files;

	void Add(int x, int y){
		if (!fits(x, y))
			StartNewProc();

		used_files.add(x);
		used_files.add(y);
		compare[x][y] = proc;
		compare_count++;
	}

	void StartNewProc(){
		((HashSet<Integer>)files[proc]).addAll(used_files);
//		files[proc] = used_files;
		allocated[proc] = compare_count;
		proc++;
		
		compare_count = 0;
		used_files = new HashSet<Integer>();
	}


	boolean fits(int x, int y){
		if (compare_count < target_compares){
			int extra_needed = 0;
			if (!used_files.contains(x))
				extra_needed++;
			if (!used_files.contains(y))
				extra_needed++;

			return used_files.size() + extra_needed <= max_files;
		}
		else
			return false;
	}

	void R2L(int Y){
		for (int x = N - 1; x > Y + rows; x--)
			for (int y = Y; y < x && y < Y + rows && y < N; y++)
				Add(x, y);

		if (used_files.size() > 0){
			for (int x = min(Y + rows, N - 1); x > Y; x--)
				for (int y = Y; y < x && y < Y + rows && y < N; y++)
					if (fits(x, y))
						Add(x, y);
					else{
						StartNewProc();
						return;
					}
			StartNewProc();
		}
	}

	public static void main(String[] args){

		int answer=0;

		Fastdividing instance = new Fastdividing();

		instance.M= 3;
		instance.N= 60;

		answer=instance.Tile();

		System.out.println(answer);

	}	

}