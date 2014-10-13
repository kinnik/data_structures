import java.io.*;
import java.util.*;

public class SymbolDigraph
{
	private final TreeMap<String, Integer> indices;
	private final String[] names;
	private final DirectedGraph digraph;
	private Scanner scanner;

	public SymbolDigraph(String filename, String delimiter)
	{
		indices = new TreeMap<String, Integer>();

    	// read the file and insert name-index pairs
    	initScanner(filename);
        while (scanner.hasNextLine())
        {
        	final String[] a = scanner.nextLine().split(delimiter);
        	for (int i = 0; i < a.length; ++i)
            	if (!indices.containsKey(a[i]))
            		indices.put(a[i], indices.size());
        }
        // build up name indices
        names = new String[indices.size()];
        for (String name: indices.keySet())
        	names[indices.get(name)] = name;

        // build directed graph by reading in the file once more
        digraph = new DirectedGraph(indices.size());
        
        // re-initialise the scanner to build the graph
        initScanner(filename);
        while (scanner.hasNextLine())
        {
        	final String[] a = scanner.nextLine().split(delimiter);
        	final int v = indices.get(a[0]);
        	for (int w = 1; w < a.length; ++w)
        		digraph.addEdge(v, indices.get(a[w]));
        }
	}

	private void initScanner(String filename)
	{
		final File file = new File(filename);
        if (file.exists())
        {
	        try {	scanner = new Scanner(file);	}
	        catch (IOException ioe)	{	System.err.println("Could not open " + filename);	}
    	}
    	else
    	{
    		scanner = null;
    	}
	}

	public boolean contains(String key)	{	return indices.containsKey(key);	}

	public int index(String key)	{	return indices.get(key);	}

	public String name(int v)	{	return names[v];	}

	public DirectedGraph G()	{	return digraph;	}

	public static void main(String[] args)
	{
        final String  filename = args[0];
        final String delimiter = args[1];

		final SymbolDigraph sdg = new SymbolDigraph(filename, delimiter);
        final DirectedGraph G   = sdg.G();

		System.out.println(sdg.G().toString());

        final Scanner in = new Scanner(System.in);
        while (in.hasNextLine())
        {
            final String t = in.nextLine();
            for (int v: G.adj(sdg.index(t)))
                System.out.println("   " + sdg.name(v));
        }
	}
}
