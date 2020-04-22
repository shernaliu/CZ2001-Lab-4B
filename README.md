# CZ2001-Lab-4B - Application of BFS to Flight Scheduling

This project is a Java console application developed by shernaliu for CZ2001 Algorithms Lab 4B.

Functionalities:

- Read & initialize a `Graph` with cities name from a text file
- Generate a graph of n cities and e edges
- Print cities
- Print graph (short)
- Print graph (long)
- Find a route between 2 cities with minimum no. of stops
- Output Excel File to visualize data

### Graph Representation

The graph is represented using an adjacency matrix. 

A cell in the matrix stores 1 if an edge exists from Source to Destination city and 0 if it does not. 

The adjacency matrix is undirected and symmetric. 

The cells in the main diagonal of the matrix stores 0 as a flight cannot have the same source and destination city.

### Reason for choosing adjacency matrix:

- Able to easily generate a graph of n cities and e edges instead of reading from a static text file
- Able to evenly randomize placement of 1 in adjacency matrix
- Able to automate Experiment 1 and 2 much more easily for large number of times

### Experiment 1 - Vary the number of cities but fix the fraction of maximum number of edges

- For each graph of size n = 100, 200, 300, …, up to 1000:
1. Generate a new graph with n cities and e edges
2. Record the time taken to execute BFS in milliseconds (ms)
3. Store the time taken in the list “duration_record”
4. Perform steps 1-3 for 100 times
5. Compute the average execution time (sum of all 100 records/100)
6. Output results to Excel file

![CZ2001-Lab-4B-1](https://res.cloudinary.com/shernaliu/image/upload/v1587590748/github-never-delete/CZ2001-LAB-4B-1.png)

![CZ2001-Lab-4B-2](https://res.cloudinary.com/shernaliu/image/upload/v1587590748/github-never-delete/CZ2001-LAB-4B-2.png)

### Experiment 2 – Fix the number of cities n=1000 and vary the fraction of maximum number of edges

- For each fraction f = 0.1, 0.2, …, up to 1.0:
1. Generate a new graph with n=1000 cities and e edges
2. Record the time taken to execute BFS in milliseconds (ms)
3. Store time taken in the list “duration_record”
4. Perform steps 1-4 for 100 times
5. Compute the average execution time (Sum of all 100 records/100)
6. Output results to Excel file

![CZ2001-Lab-4B-3](https://res.cloudinary.com/shernaliu/image/upload/v1587590748/github-never-delete/CZ2001-LAB-4B-3.png)

![CZ2001-Lab-4B-4](https://res.cloudinary.com/shernaliu/image/upload/v1587590748/github-never-delete/CZ2001-LAB-4B-4.png)

## Installing CZ2001-Lab-4B

```
# clone this project
git clone https://github.com/shernaliu/CZ2001-Lab-4B.git
```
Run the project in IntelliJ IDEA (using any other IDE is fine too).

All dependencies are maintained in pom.xml file which is managed automatically with Maven.

This project uses Apache POI to output the computed data and results to Excel spreadsheet `.xlsx` file.

## Related Repositories

There are 3 repositories created for both projects of Lab 4.

1. [CZ2001-Lab-4A - Statistical & Topological Analysis of Social Networks](https://github.com/shernaliu/CZ2001-Lab-4A)

2. [CZ2001-Lab-4B - Application of BFS to Flight Scheduling](https://github.com/shernaliu/CZ2001-Lab-4B) :point_left: You are here!

3. [CZ2001-Lab-4B-Graph - Visualization of a graph using Python Jupyter notebook](https://github.com/shernaliu/CZ2001-Lab-4B-Graph)
