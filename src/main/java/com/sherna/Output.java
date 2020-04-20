package com.sherna;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
 * @author sherna
 * @created 20/04/2020
 * @project CZ2001-Lab4B
 */
public class Output {


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
        cell.setCellValue("No. of cities");
        cell = row.createCell(cellnum++);
        cell.setCellValue("1-Stop Flight Average Execution Time (ms)");
        cell = row.createCell(cellnum++);
        cell.setCellValue("2-Stops Flight Average Execution Time (ms)");

        // compute average CPU time for 10 cities
        Graph graph = null;
        double start_time = 0;
        double end_time = 0;
        double time_duration = 0;
        List<Double> duration_record_1_flight = new ArrayList<>();
        List<Double> duration_record_2_flights = new ArrayList<>();
        int noFlightsTaken = 0;
        double average_1_flight = 0;
        double average_2_flights = 0;
        int srcIndex = 0;
        int destIndex = 0;
        Random rand = new Random();

        for (int i = 10; i <= 100; i += 10) {
            // init a graph of i cities
            graph = new Graph.Reader("data/cities.txt").read("cities", i);
            // reset record
            duration_record_1_flight = new ArrayList<>();
            duration_record_2_flights = new ArrayList<>();
            // record 100 times
            while (duration_record_1_flight.size() < 5 || duration_record_2_flights.size() < 5) {
                // generate random number=[0,10]
                srcIndex = rand.nextInt(10);
                destIndex = rand.nextInt(10);

                // generate new random number for destIndex if destIndex == srcIndex bc they cannot be the same.
                while (destIndex == srcIndex) {
                    destIndex = rand.nextInt(11);
                }

                start_time = System.nanoTime();
                graph.BFS(srcIndex, destIndex, false);
                end_time = System.nanoTime();
                time_duration = (end_time - start_time) / 1000000.0;

                noFlightsTaken = graph.getNumFlightsTaken() - 1;
                // do this for no. of flights = 1
                if (noFlightsTaken == 1 && duration_record_1_flight.size() < 5) {
                    duration_record_1_flight.add(time_duration);
                } else if (noFlightsTaken == 2 && duration_record_2_flights.size() < 5) {
                    duration_record_2_flights.add(time_duration);
                }
            }

            // compute average
            average_1_flight = computeAverage(duration_record_1_flight);
            average_2_flights = computeAverage(duration_record_2_flights);

            // output to CSV, k is noOfFlights=[1,2]
            for (int k = 1; k <= 2; k++) {
                cellnum = 0;
                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(i); // graph size
                cell = row.createCell(cellnum++);
                cell.setCellValue(average_1_flight);
                cell = row.createCell(cellnum++);
                cell.setCellValue(average_2_flights);

            }
        }
        try {
            FileOutputStream out = new FileOutputStream(new File("data/output-" + graph.getFileName() + ".xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Done. Output file name is " + "data/output-" + graph.getFileName() + ".xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double computeAverage(List<Double> records) {
        double ans = 0;
        for (Double record : records) {
            ans += record;
        }
        return ans / 5.0;
    }

}
