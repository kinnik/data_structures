require_relative 'UndirectedGraph'

describe UndirectedGraph do
  let(:graph) { UndirectedGraph.new(13) }

  it 'should create edges between the given graph vertices' do
  	graph.addEdge(0, 6)
  	graph.addEdge(0, 2)
  	graph.addEdge(0, 1)
  	graph.addEdge(0, 5)

  	graph.addEdge(1, 0)

  	graph.addEdge(2, 0)

  	graph.addEdge(3, 5)
  	graph.addEdge(3, 4)

  	graph.addEdge(4, 5)
  	graph.addEdge(4, 6)
  	graph.addEdge(4, 3)

  	graph.addEdge(5, 3)
  	graph.addEdge(5, 4)
  	graph.addEdge(5, 0)

  	graph.addEdge(6, 0)
  	graph.addEdge(6, 4)

  	graph.addEdge(7, 8)

  	graph.addEdge(8, 7)

  	graph.addEdge(9, 11)
  	graph.addEdge(9, 10)
  	graph.addEdge(9, 12)

  	graph.addEdge(10, 9)

	graph.addEdge(11, 9)
	graph.addEdge(11, 12)

	graph.addEdge(12, 11)
	graph.addEdge(12, 9)

	graph.adj(0).should include(6, 2, 1, 5)
	graph.adj(1).should include(0)
	graph.adj(2).should include(0)
	graph.adj(3).should include(5, 4)
	graph.adj(4).should include(5, 6, 3)
	graph.adj(5).should include(3, 4, 0)
	graph.adj(6).should include(0, 4)
	graph.adj(7).should include(8)
	graph.adj(8).should include(7)
	graph.adj(9).should include(11, 10, 12)
	graph.adj(10).should include(9)
	graph.adj(11).should include(9, 12)
	graph.adj(12).should include(11, 9)

  end
 end