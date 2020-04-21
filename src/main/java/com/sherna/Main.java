package com.sherna;

import java.util.Scanner;

/*
 * @author sherna
 * @created 18/04/2020
 * @project CZ2001-Lab4B
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double start_time = 0;
        double end_time = 0;
        double time_duration = 0;
        int numOfCities = 3; // accepted range=[3,100]
        int numOfFlights = 1;
        int src, dest;
        int maxNumEdges = 0;

        // get user input on the size of the graph
        System.out.print("Enter the number of cities (min 3, max 100): ");
        numOfCities = sc.nextInt();

        // check if valid numOfCities
        while (numOfCities < 3 || numOfCities > 100) {
            System.out.println("Invalid input.");
            System.out.print("Enter the number of cities (min 3, max 100): ");
            numOfCities = sc.nextInt();
        }

        // calculate max. number of edges allowed for user selected numOfCities aka (n^2-n)/2
        maxNumEdges = (int) (Math.pow(numOfCities, 2) - numOfCities) / 2;

        // get user input on the desired number of edges/flights in the graph
        System.out.print("Enter the number of flights (min 1, max " + maxNumEdges + "): ");
        numOfFlights = sc.nextInt();

        // check if valid numOfFlights
        while (numOfFlights < 1 || numOfFlights > maxNumEdges) {
            System.out.println("Invalid input.");
            System.out.print("Enter the number of flights (min 1, max " + maxNumEdges + "): ");
            numOfFlights = sc.nextInt();
        }

        System.out.println("Initializing a graph with " + numOfCities + " cities and " + numOfFlights + " flights, please wait...");
        Graph g = new Graph.Reader("data/cities.txt").read("cities", numOfCities, numOfFlights);
        System.out.println("Initialization complete.");
        printMenu();

        // get user input
        System.out.print("Enter input: ");
        int userInput = sc.nextInt();

        while (userInput != 0) {
            switch (userInput) {
                case 1:
                    System.out.println("(1) - Print list of cities");
                    g.printCities();
                    break;
                case 2:
                    System.out.println("(2) - Print graph (short)");
                    g.printGraph(true);
                    break;
                case 3:
                    System.out.println("(3) - Print graph (long)");
                    g.printGraph(false);
                    break;
                case 4:
                    System.out.println("(4) - Find flight route between 2 cities");
                    System.out.print("Enter the source city (Enter -1 to exit): ");
                    src = sc.nextInt();
                    sc.nextLine();
                    if (src == -1) {
                        break;
                    } else if (src > numOfCities - 1) {
                        System.out.println("Invalid input. ");
                        continue;
                    }
                    System.out.print("Enter the destination city (Enter -1 to exit): ");
                    dest = sc.nextInt();
                    if (dest == -1) {
                        break;
                    } else if (dest > numOfCities - 1) {
                        System.out.println("Input not valid. Outside of valid range.");
                        System.out.println("=================================================================");
                        continue;
                    }
                    sc.nextLine(); // Consume \n

                    start_time = System.nanoTime();
                    g.BFS(src, dest);
                    end_time = System.nanoTime();
                    time_duration = (end_time - start_time) / 1000000.0;

                    System.out.println("Total execution time: " + (time_duration) + "ms");
                    System.out.println("Number of Cities: " + numOfCities);
                    System.out.println("Number of Flights taken to fly from source to destination: " + (g.getNumFlightsTaken() - 1));
                    break;
                case 5:
                    System.out.println("(5) - Output Excel File");
                    Output output = new Output();
                    output.outputExcelFile();
                    break;
                default:
                    System.out.println("Invalid input.");
            }
            printMenu();
            System.out.print("Enter input: ");
            userInput = sc.nextInt();
        }
        System.out.println("Goodbye!");
    }

    static void printMenu() {
        System.out.println("==================================");
        System.out.println("============== MENU ==============");
        System.out.println("Select a function:");
        System.out.println("(1) - Print cities");
        System.out.println("(2) - Print graph (short)");
        System.out.println("(3) - Print graph (long)");
        System.out.println("(4) - Find a route between 2 cities with minimum no. of stops");
        System.out.println("(5) - Output Excel File");
        System.out.println("(0) - Exit");
        System.out.println("==================================");
    }
}
