using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ConsoleApplication6
{
    class Program
    {
        int M;
        int N;
        Host[] hosts;
        List<Host>[,] comparisions;
        Random random;

        public Program(int M, int N)
        {
            this.M = M;
            this.N = N;
            this.hosts = new Host[M];
            this.comparisions = new List<Host>[N, N];
            this.random = new Random();
        }

        void Check()
        {
            for (int j = 0; j < N; j++)
                for (int k = 0; k < N; k++)
                {
                    System.Diagnostics.Debug.Assert(comparisions[j, k].Count > 0);
                    foreach (Host host in comparisions[j, k])
                    {
                        System.Diagnostics.Debug.Assert(host.files.Contains(j));
                        System.Diagnostics.Debug.Assert(host.files.Contains(k));
                    }
                }
        }

        bool Reduce(int iteration)
        {
            bool removed = false;
            for (int i = 0; i < M; i++)
            {
                //Check();
                Host h = hosts[i];
                if (h.files.Count > 0)
                {
                    int pos = random.Next(h.files.Count);
                    int f1 = h.files[pos];
                    bool remove = true;
                    foreach (int f2 in h.files)
                    {
                        System.Diagnostics.Debug.Assert(comparisions[f1, f2].Contains(h));
                        if (comparisions[f1, f2].Count <= 1)
                            remove = false;
                    }
                    if (remove)
                    {
                        foreach (int f2 in h.files)
                        {
                            comparisions[f1, f2].Remove(h);
                            comparisions[f2, f1].Remove(h);
                            System.Diagnostics.Debug.Assert(comparisions[f1, f2].Count > 0);
                        }
                        h.files.Remove(f1);
                        removed = true;
                        //Check();
                    }
                }
            }            
            return removed;
        }

        static void Main(string[] args)
/*        {
            for (int N = 1; N <= 256; N*=2)
                for (int M = 1; M <= N*(N-1)/2; M*=2)
                {
                    Program program = new Program(M, N);
//                    Console.WriteLine("N = {0}, M = {1}, |P| = {2}", N, M, program.Compute());
                    Console.WriteLine("{0} {1} {2}", N, M, program.Compute());
                }
        }
*/
        {
            int N=16;
            int M=8;
            for (int i=0;i<100;i++)
            {
                
               
                    Program program = new Program(M, N);
//                    Console.WriteLine("N = {0}, M = {1}, |P| = {2}", N, M, program.Compute());
                    Console.WriteLine("{0} {1} {2}", N, M, program.Compute());
                }
            
        }







        int Compute()
        {
            for (int j = 0; j < N; j++)
                for (int k = 0; k < N; k++)
                    comparisions[j, k] = new List<Host>();

            for (int i = 0; i < M; i++)
            {
                hosts[i] = new Host(i, N);

                for (int j = 0; j < N; j++)
                    for (int k = 0; k < N; k++)
                        comparisions[j, k].Add(hosts[i]);
            }

            while (Reduce(0));

            for (int zz=0; zz<1000; zz++)
                Reduce(zz);

            int max = 0;
            foreach (var host in hosts)
                if (host.files.Count > max)
                    max = host.files.Count;
            return max;
        }
    }
}