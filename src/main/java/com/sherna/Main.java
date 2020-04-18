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
        int numOfCities = 2; // accepted range=[2,50]
        int src, dest;

        // get user input on the size of the graph
        System.out.print("Enter the number of cities (min 2, max 100): ");
        numOfCities = sc.nextInt();

        while(numOfCities <= 1 || numOfCities > 100){
            System.out.println("Invalid input.");
            System.out.print("Enter the number of cities (min 1, max 100): ");
            numOfCities = sc.nextInt();
        }

        System.out.println("Initializing a graph with " + numOfCities + " cities, please wait...");
        Graph g = new Graph.Reader("data/cities.txt").read("cities", numOfCities);
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
                    System.out.print("Enter the source city (-1 to quit): ");
                    src = sc.nextInt();
                    sc.nextLine(); // Consume \n
                    if (src == -1) {
                        break;
                    } else if (src > numOfCities - 1) {
                        System.out.println("Input not valid. Outside of valid range.");
                        System.out.println("=================================================================");
                        continue;
                    }
                    System.out.print("Enter the destination city (type -1 to quit): ");
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
                    g.outputExcelFile();
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
