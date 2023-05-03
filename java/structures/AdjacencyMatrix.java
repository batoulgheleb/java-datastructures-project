package structures;

public class AdjacencyMatrix {

    private int[][] matrix;
    private int size;
    private int[] array;

    //takes in an array as an argument and uses the size of that array to create the adjancy node; more memory efficient than predicting the size.
    public AdjacencyMatrix(int[] array) {
        this.array = array;
        this.size = array.length;
        this.matrix = new int[size][size];
    }

    //returns a 2d array to represent the matrix 
    public int[][] getMatrix() {
        return matrix;
    }

    //creates an edge between two node on the graph
    public void addConnection(int from, int to) {
        matrix[from][to]++;
    }

    //uses a queue and breadth first, searches the list, and then when visiting the same node twice, the node is removed from the queue. Leaves the shortest path between two nodes.
    public CustomList<Integer> findShortestPath(int[][] graph, int source, int target) {
        int n = graph.length;
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        CustomQueue<Integer> queue = new CustomQueue<>();
        
        for (int i = 0; i < parent.length; i++) {
            parent[i] = -1;
        }
        
        queue.enqueue(source);
        visited[source] = true;
        while (!queue.isEmpty()) {
            int node = queue.dequeue();
            if (node == target) {
                CustomList<Integer> path = new CustomList<>();
                int current = node; 
                while (current != -1) {
                    path.add(current);
                    current = parent[current];
                }
                path.reverse();
                return path;
            }
            for (int neighbor = 0; neighbor < n; neighbor++) {
                if (graph[node][neighbor] >= 1 && !visited[neighbor]) {
                    visited[neighbor] = true;
                    parent[neighbor] = node;
                    queue.enqueue(neighbor);
                }
            }
        }
        // If the target node was not reached, return an empty list
        return new CustomList<>();
    }
    
    //find all the nodes that have at least 2 edges, and therefore at least 2 connections with two other nodes.
    public int[] findNodesWithMultipleConnections() {
        CustomList<Integer> nodes = new CustomList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] >= 2) {
     
                    nodes.add(i);
                    break;
                    
                }
            }
        }

        int[] result = new int[nodes.size()];
        int i = 0;
        for (Integer node : nodes ) {
            result[i] = node;
            i++;
        }
        return result;
    }

    

}

