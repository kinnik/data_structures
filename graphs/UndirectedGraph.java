import java.util.*;

// Implementation of an undirected graph data structure using adjacency list representation with HashSet

public class UndirectedGraph
{
    private int V;
    private int E;
    private HashSet<Integer>[] adjList;

    public UndirectedGraph(int V)
    {
        this.V = V;
        E = 0;
        adjList = (HashSet<Integer>[]) new HashSet[V];

        for (int i = 0; i < V; ++i)
            adjList[i] = new HashSet<Integer>();
    }

    public int V() { return V; }
    public int E() { return E; }

    public void addEdge(int v, int w)
    {
        adjList[v].add(w);
        adjList[w].add(v);
    }

    public Iterable<Integer> adj(int V)
    {
        return adjList[V];
    }
}
