package com.sherna;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

/*
 * @author sherna
 * @created 18/04/2020
 * @project CZ2001-Lab4B
 */
public class Graph {
    private String fileName;
    private int numOfCities;
    private List<String> cityList;
    private ArrayList<Integer>[] vertexList;
    private int numFlightsTaken = 0;

    /**
     * Constructor
     *
     * @param fileName    name of the text file it is reading from
     * @param numOfCities number of cities specified in the text file
     */
    public Graph(String fileName, int numOfCities) {
        this.fileName = fileName;
        this.numOfCities = numOfCities;
        this.cityList = new ArrayList<>();
        // init vertexList with new arrays
        vertexList = new ArrayList[numOfCities];
        for (int i = 0; i < this.numOfCities; i++) {
            vertexList[i] = new ArrayList<Integer>();
        }
        initGraph();
    }

    /**
     * Initialize the adjacency matrix of the graph.
     */
    public void initGraph() {
        // Edges are represented in matrix form (i.e. matrix[a][b] where a and b are cities)
        // This is where we insert each of these edges into the Graph
        int[][] matrixOfEdges = initAdjacencyMatrix();
        int a, b;
        for (a = 0; a < numOfCities; a++) {
            for (b = 0; b < numOfCities; b++) {
                if (matrixOfEdges[a][b] == 1) {
                    addEdge(a, b);
                } else {
                    continue;
                }
            }
        }
    }

    public int[][] initAdjacencyMatrix() {
        int[][] adjacencyMatrix = new int[numOfCities][numOfCities];
        Random rand = new Random();
        int a, b;

        /* This generates a random noOfVertices x noOfVertices matrix filled with 0 or 1
         * 0 = edge is absent (there is no flight connecting the two cities)
         * 1 = edge is present (there is a flight connecting the two cities)
         */
        for (a = 0; a < numOfCities; a++) {
            for (b = 0; b < numOfCities; b++) {
                adjacencyMatrix[a][b] = rand.nextInt(2);
            }
        }

        /*
         * The graph we are simulating is an undirected graph, hence we take one half of the matrix and reflect that
         * half across the diagonal to get a matrix that is symmetrical with respect to the main diagonal. In this
         * graph, it also does not make sense to have a self-edge (i.e. an edge from one node back to itself) and hence
         * all [a][a] vertices in the matrix are by default set to 0.
         *
         * */
        for (a = 0; a < numOfCities; a++) {
            for (b = 0; b < a; b++) {
                adjacencyMatrix[b][a] = adjacencyMatrix[a][b]; // Reflect matrix w.r.t. main diagonal
            }
            adjacencyMatrix[a][a] = 0; // No self-edges
        }
        return adjacencyMatrix;
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
     * Gets the number of flights taken.
     *
     * @return number of flights taken
     */
    public int getNumFlightsTaken() {
        return this.numFlightsTaken;
    }

    /**
     * Prints the list of all cities.
     */
    public void printCities() {
        System.out.println("Index\tCity");
        for (int i = 0; i < numOfCities; i++) {
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

    /**
     * Breadth First Search algorithm method
     *
     * @param srcCity  source city
     * @param destCity destination city
     */
    public void BFS(int srcCity, int destCity) {
        // Flag to be set when a shortest path to destination is found
        boolean shortestPathFound = false;

        // By default, all vertices start off as not visited (i.e. false)
        boolean visitedCities[] = new boolean[numOfCities];

        // Queue for BFS Algorithm
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Maps every new adjVertex to the one that was visited before it
        // Using this, we can trace back a path (shortest path) to the source
        // In this map: key is adjVertex (after) while currentVertex (before) is the value (see below)
        Map<Integer, Integer> previousCities = new HashMap<Integer, Integer>();

        // Visit srcCity, mark it in visitedCities as true, then add it to queue
        visitedCities[srcCity] = true;
        queue.add(srcCity);

        // currentVertex is used to iterate through the while loop below
        int currentVertex = srcCity;

        while (queue.size() != 0) {
            if (currentVertex == destCity) {
                shortestPathFound = true;
                break;
            }

            currentVertex = queue.poll();

            // For all adjacent vertices (adjVertex) of the dequeued currentVertex:
            // If an adjVertex has not been visited, then mark it as visited and queue it
            Iterator<Integer> iter = vertexList[currentVertex].listIterator();
            while (iter.hasNext()) {
                Integer adjVertex = iter.next();
                if (visitedCities[adjVertex] == false) {
                    visitedCities[adjVertex] = true;
                    queue.add(adjVertex);
                    previousCities.put(adjVertex, currentVertex);
                    if (adjVertex == destCity) {
                        currentVertex = adjVertex;
                        shortestPathFound = true;
                        break;
                    }
                }
            }
        }

        if (shortestPathFound) {
            ArrayList<Integer> pathToTrace = new ArrayList<Integer>();
            Integer cityToTrace = currentVertex;
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
        } else {
            System.out.println("Path not found");
        }
    }

    public void outputExcelFile() {
        // Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("CPU Computation Time");

        int rownum = 0;
        int cellnum = 0;
        Row row = null;
        Cell cell = null;

        // write header
        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Graph Size");
        cell = row.createCell(cellnum++);
        cell.setCellValue("No. of flights taken to reach destination");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Average Execution time (ms)");

//        // write no. of nodes for k=0 (degree of 0)
//        cellnum = 0;
//        row = sheet.createRow(rownum++);
//        cell = row.createCell(cellnum++);
//        cell.setCellValue(0);
//        cell = row.createCell(cellnum++);
//        cell.setCellValue(noOfNodes);
//        // ln(0) is undefined
//        cell = row.createCell(cellnum++);
//        cell.setCellValue(Math.log(0)); // ln k
//        cell = row.createCell(cellnum++);
//        cell.setCellValue(Math.log(noOfNodes)); // ln P
//        cell = row.createCell(cellnum++);
//        cell.setCellValue(adjacencyMap.size()); // total no. of nodes
//        noOfNodes = 0; // reset

        try {
            FileOutputStream out = new FileOutputStream(new File("data/output-" + this.fileName + ".xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Done. Output file name is " + "data/output-" + this.fileName + ".xlsx");
        } catch (Exception e) {
            e.printStackTrace();
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

        Graph read(String fileName, int noOfCities) {
            Graph g = null;
            try {
                g = scanGraph(fileName, noOfCities);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return g;
        }

        Graph scanGraph(String fileName, int noOfCities) throws FileNotFoundException {
            String line = null;
            graph = new Graph(fileName, noOfCities);
            Scanner sc = new Scanner(new File(txtFileName));
            while (sc.hasNextLine()) {
                if (count == noOfCities) {
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
