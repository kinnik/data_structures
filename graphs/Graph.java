import java.util.*;

// Implementation of a Graph data structure using adjacency list representation with LinkedList

public class Graph
{
    private int V;
    private int E;
    private LinkedList<Integer>[] adjList;

    public Graph(int V)
    {
        this.V = V;
        E = 0;
        adjList = (LinkedList<Integer>[]) new LinkedList[V];

        for (int i = 0; i < V; ++i)
            adjList[i] = new LinkedList<Integer>();
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
