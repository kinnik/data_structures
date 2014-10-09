require 'set'

class UndirectedGraph
	attr_reader :V, :E

	def initialize(num_vertices)
		@adjList = Array.new(num_vertices) { |i| i = Set.new }
		@V = num_vertices
		@E = 0
	end

	def addEdge(v, w)
		@adjList[v].add(w)
		@adjList[w].add(v)
	end

	def adj(v)
		return @adjList[v]
	end
end