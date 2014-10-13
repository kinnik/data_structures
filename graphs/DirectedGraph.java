import java.io.*;
import java.util.*;

// Implementation of a directed graph data structure using adjacency list 
// representation with HashSet

public class DirectedGraph
{
    private int V;
    private int E;
    private HashSet<Integer>[] adjList;

    // creates a graph with given number of vertices which have no edges
    public DirectedGraph(int V)
    {
        this.V = V;
        this.E = 0;
        init();
    }

    private void init()
    {
        adjList = (HashSet<Integer>[]) new HashSet[V];

        for (int i = 0; i < V; ++i)
            adjList[i] = new HashSet<Integer>();
    }

    public DirectedGraph(String filename)
    {
        try
        {
            final File file = new File(filename);
            if (file.exists())
            {
                Scanner scanner = new Scanner(file);
                this.V = scanner.nextInt();
                this.E = scanner.nextInt();
                init();

                for (int e = 0; e < E; ++e)
                {
                    final int v = scanner.nextInt();
                    final int w = scanner.nextInt();
                    addEdge(v, w);
                }
            }      
        }
        catch (IOException ioe)
        {
            System.err.println("Could not open " + filename);
        }

    }

    public int V() { return V; }
    public int E() { return E; }

    public void addEdge(int v, int w)   {   adjList[v].add(w);  }

    public Iterable<Integer> adj(int V) {   return adjList[V];  }

    public String toString()
    {
        final StringBuffer sb = new StringBuffer();
        for (int v = 0; v < V; ++v)
        {
            sb.append(v).append(": ");
            for (int w: adjList[v])
                sb.append(w).append(" ");
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args)
    {
        DirectedGraph dg = new DirectedGraph(args[0]);
        System.out.println(dg.toString());
    }
}
