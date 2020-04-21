package com.sherna;

import java.io.*;
import java.util.*;

/*
 * @author sherna
 * @created 18/04/2020
 * @project CZ2001-Lab4B
 */
public class Graph {
    private String fileName;
    private int n; // number of nodes/cities
    private int e; // number of edges/fights
    private List<String> cityList; // list of available nodes/cities
    private ArrayList<Integer>[] vertexList;
    private int numFlightsTaken = 0;

    /**
     * Constructor
     *
     * @param fileName   name of the text file it reads from
     * @param numOfNodes number of nodes/cities
     * @param numOfEdges number of edges/flights
     */
    public Graph(String fileName, int numOfNodes, int numOfEdges) {
        this.fileName = fileName;
        this.n = numOfNodes;
        this.e = numOfEdges;
        this.cityList = new ArrayList<>();
        vertexList = new ArrayList[numOfNodes];
        for (int i = 0; i < this.n; i++) {
            vertexList[i] = new ArrayList<Integer>();
        }
        initGraph(numOfEdges);
    }

    /**
     * Gets the name of the text file it read from
     *
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the number of flights taken.
     *
     * @return number of flights taken
     */
    public int getNumFlightsTaken() {
        return this.numFlightsTaken;
    }


    /**
     * Initialize the graph with the desired number of edges/flights.
     * The edges/flights are represented using Adjacency Matrix.
     * If city a has a flight to city b, then M[a][b] == 1.
     * If city c does not a flight to city e, then M[c][d] == 0.
     * If M[i][j] is 1, then add the edge.
     *
     * @param numOfEdges number of edges/flights
     */
    public void initGraph(int numOfEdges) {
        int[][] M = initAdjacencyMatrix(numOfEdges);
        int i, j;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (M[i][j] == 1) {
                    addEdge(i, j);
                } else {
                    continue;
                }
            }
        }
    }

    /**
     * Initialize the adjacency matrix of size n x n with the desired number of edges/flights.
     *
     * @param desiredNumEdges
     * @return
     */
    public int[][] initAdjacencyMatrix(int desiredNumEdges) {
        // initialize a matrix of size n x n; all cells are set to 0
        int[][] M = new int[n][n];
        int edges_counter = 0; // counter to keep track the number of edges
        Random rand = new Random();
        int i, j;

        // Repeatedly populate the cells in lower triangle with 1 randomly until we have the desired number of edges
        while (edges_counter != desiredNumEdges) {
            // generate random number i = j = [0, n-1] (e.g. if n=3, it should generate a number within [0,2])
            i = rand.nextInt(n); // The nextInt(int n) returns a random number between 0(inclusive) and and n (exclusive).
            j = rand.nextInt(n);

            // Focus on the cells on the lower triangle
            if (i > j) {
                // if cell M[i][j] is 0, assign 1 & increment edges_counter
                if (M[i][j] == 0) {
                    M[i][j] = 1;
                    edges_counter += 1;
                }
            }
        }

        // Matrix is symmetric; reflect M[i][j] to M[j][i]
        for (i = 0; i < n; i++) {
            for (j = 0; i > j; j++) {
                M[j][i] = M[i][j];
            }
        }
        printMatrix(M); // debug
        return M;
    }

    /**
     * Prints a visual representation of the matrix
     * @param M the matrix you wish to print
     */
    public void printMatrix(int[][] M){
        int i,j;
        System.out.println("The matrix structure: ");
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                System.out.format("[%d]", M[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Adds the edge of v1 & v2 into the vertexList.
     *
     * @param v1 first vertex
     * @param v2 second vertex
     */
    public void addEdge(int v1, int v2) {
        vertexList[v1].add(v2);
    }

    /**
     * Prints the list of all cities.
     */
    public void printCities() {
        System.out.println("Index\tCity");
        for (int i = 0; i < n; i++) {
            System.out.format("[%2d]\t%s\n", i, cityList.get(i));
        }
    }

    /**
     * Prints the structure of the graph.
     *
     * @param isShort
     */
    public void printGraph(boolean isShort) {
        System.out.println("Index\t\tCity");
        for (int i = 0; i < vertexList.length; i++) {
            System.out.format("[%2d] %15s ->", i, cityList.get(i)); // Prints the city name of the vertex
            for (Integer cityIndex : vertexList[i]) {
                if (isShort) {
                    System.out.format(" [%2s]", cityIndex, cityList.get(cityIndex)); // Prints adjacent cities
                } else {
                    System.out.format(" [%s-%s]", cityIndex, cityList.get(cityIndex)); // Prints adjacent cities
                }
            }
            System.out.println();
        }
    }

    public void BFS(int srcCity, int destCity) {
        boolean isShortestPath = false; // set to true if a shortest path is found
        boolean visitedCities[] = new boolean[n]; // all visited cities are initially set to false as its not visited yet
        LinkedList<Integer> queue = new LinkedList<Integer>();
        Map<Integer, Integer> previousCities = new HashMap<Integer, Integer>(); // trace a shortest path to the source. <adjVertex, currentCity>
        visitedCities[srcCity] = true; // visit & mark srcCity as true and add to the queue
        queue.add(srcCity);
        int currentCity = srcCity; // used to iterate thru while loop
        // loop thru the queue, if currentCity is the destination, then a shortest path is found & break out of loop
        while (queue.size() != 0) {
            if (currentCity == destCity) {
                isShortestPath = true;
                break;
            }
            currentCity = queue.poll(); // returns & remove the city at the front of the queue (dequeue)
            // iterate thru the adjacent cities of the dequeued currentCity, if an adjCity is not visited yet, then mark visit and enqueue
            Iterator<Integer> iter = vertexList[currentCity].listIterator();
            while (iter.hasNext()) {
                Integer adjCity = iter.next();
                if (visitedCities[adjCity] == false) {
                    visitedCities[adjCity] = true;
                    queue.add(adjCity);
                    previousCities.put(adjCity, currentCity);
                    if (adjCity == destCity) {
                        currentCity = adjCity;
                        isShortestPath = true;
                        break;
                    }
                }
            }
        }
        // if a shortest path is found, trace the path and count the number of flights taken
        if (isShortestPath) {
            ArrayList<Integer> pathToTrace = new ArrayList<Integer>();
            Integer cityToTrace = currentCity;
            while (cityToTrace != null) {
                pathToTrace.add(cityToTrace);
                cityToTrace = previousCities.get(cityToTrace);
            }
            Collections.reverse(pathToTrace);
            System.out.println("Shortest path flight route: ");
            numFlightsTaken = 0;
            for (Integer cityIndex : pathToTrace) {
                System.out.format("%2d : %s\n", cityIndex, cityList.get(cityIndex));
                numFlightsTaken++;
            }
        }
    }

    /**
     * Breadth First Search algorithm method
     *
     * @param srcCity  source city
     * @param destCity destination city
     */
    public void BFS(int srcCity, int destCity, boolean printResult) {
        boolean isShortestPath = false;
        boolean visitedCities[] = new boolean[n];
        LinkedList<Integer> queue = new LinkedList<Integer>();
        Map<Integer, Integer> previousCities = new HashMap<Integer, Integer>();
        visitedCities[srcCity] = true;
        queue.add(srcCity);
        int currentCity = srcCity;
        while (queue.size() != 0) {
            if (currentCity == destCity) {
                isShortestPath = true;
                break;
            }
            currentCity = queue.poll();
            Iterator<Integer> iter = vertexList[currentCity].listIterator();
            while (iter.hasNext()) {
                Integer adjCity = iter.next();
                if (visitedCities[adjCity] == false) {
                    visitedCities[adjCity] = true;
                    queue.add(adjCity);
                    previousCities.put(adjCity, currentCity);
                    if (adjCity == destCity) {
                        currentCity = adjCity;
                        isShortestPath = true;
                        break;
                    }
                }
            }
        }
        if (isShortestPath) {
            ArrayList<Integer> pathToTrace = new ArrayList<Integer>();
            Integer cityToTrace = currentCity;
            while (cityToTrace != null) {
                pathToTrace.add(cityToTrace);
                cityToTrace = previousCities.get(cityToTrace);
            }
            Collections.reverse(pathToTrace);
            numFlightsTaken = 0;
            for (Integer cityIndex : pathToTrace) {
                numFlightsTaken++;
            }
        }
    }

    /**
     * Static Reader class to read from the text file and initialize the graph.
     */
    static class Reader {
        int count = 0;
        String txtFileName;
        Graph graph;

        Reader(String txtFileName) {
            this.txtFileName = txtFileName;
        }

        Graph read(String fileName, int noOfCities, int noOfEdges) {
            Graph g = null;
            try {
                g = scanGraph(fileName, noOfCities, noOfEdges);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return g;
        }

//        Graph scanGraph(String fileName, int noOfCities) throws FileNotFoundException {
//            String line = null;
//            graph = new Graph(fileName, noOfCities);
//            Scanner sc = new Scanner(new File(txtFileName));
//            while (sc.hasNextLine()) {
//                if (count == noOfCities) {
//                    break;
//                }
//                line = sc.nextLine();
//                graph.cityList.add(line);
//                count++;
//            }
//            return graph;
//        }

        Graph scanGraph(String fileName, int noOfNodes, int noOfEdges) throws FileNotFoundException {
            String line = null;
            graph = new Graph(fileName, noOfNodes, noOfEdges);
            Scanner sc = new Scanner(new File(txtFileName));
            while (sc.hasNextLine()) {
                if (count == noOfNodes) {
                    break;
                }
                line = sc.nextLine();
                graph.cityList.add(line);
                count++;
            }
            return graph;
        }
    }
}
