import java.util.*;

public class TopologicalSort
{
	private boolean[] visited;
	// Use Deque instead of Stack because the iterator of Stack is broken.
	// http://docs.oracle.com/javase/7/docs/api/index.html?java/util/Deque.html
	// Deques can also be used as LIFO (Last-In-First-Out) stacks. This 
	// interface should be used in preference to the legacy Stack class. When a 
	// deque is used as a stack, elements are pushed and popped from the 
	// beginning of the deque.
	private Deque<Integer> reversePost;

	public TopologicalSort(DirectedGraph G)
	{
		    visited = new boolean[G.V()];
		reversePost = new LinkedList<Integer>();

		// TO-DO: check if there is a cycle
		for (int v = 0; v < G.V(); ++v)
			if (!visited[v]) dfs(G, v);
	}

	private void dfs(DirectedGraph G, int v)
	{
		visited[v] = true;
		for (int adj: G.adj(v))
			if (!visited[adj]) dfs(G, adj);

		reversePost.addFirst(v);
	}

	private Iterable<Integer> order()
	{
		return reversePost;
	}

	public static void main(String[] args)
	{
		final String filename  = args[0];
		final String separator = args[1];

		final SymbolDigraph  sdg = new SymbolDigraph(filename, separator);
		final TopologicalSort ts = new TopologicalSort(sdg.G());

		//System.out.println(sdg.G().toString());

		for (int v: ts.order())
			System.out.println(sdg.name(v));
	}
}