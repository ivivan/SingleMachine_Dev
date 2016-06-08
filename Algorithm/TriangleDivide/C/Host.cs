using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ConsoleApplication6
{
    public class Host
    {
        int host;
        public List<int> files = new List<int>();
        
        public Host(int host, int N)
        {
            this.host = host;
            for (int i = 0; i < N; i++)
                files.Add(i);
        }

        public override string  ToString()
        {
            StringBuilder builder = new StringBuilder();
            builder.AppendFormat("{0}: ", host);
            foreach (var file in files)
                builder.AppendFormat("{0} ", file);
            return builder.ToString();
        }
    }
}
