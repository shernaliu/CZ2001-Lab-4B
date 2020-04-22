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
        cell.setCellValue("No. of edges");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Average Execution Time (ms)");

        // compute average CPU time for 10 cities
        Graph graph = null;
        double start_time = 0;
        double end_time = 0;
        double time_duration = 0;
        List<Double> duration_record = new ArrayList<>();
        double average_duration = 0;
        int srcIndex = 0;
        int destIndex = 0;
        Random rand = new Random();
        int numOfSelectedEdges = 0;

        // EXPERIMENT 1 - Vary the number of cities, keep number of edges constant.
        // compute average computation time for graph size = 10, 20, 30, ..., 100 with selected number of edges = 45
        numOfSelectedEdges = 45;
        for (int i = 10; i <= 100; i += 10) {
            // reset duration_record
            duration_record = new ArrayList<>();

            // generate graph of n cities 100 times to do record time 100 times for the experiment
            for (int j = 0; j < 1000; j++) {
                graph = new Graph.Reader("data/cities.txt").read("cities", i, numOfSelectedEdges);

                // TODO: WHAT RANGE?
                // generate random number=[0,9]
                srcIndex = 0;
                destIndex = 1;

                // generate new random number for destIndex if destIndex == srcIndex (they cannot be the same)
                while (destIndex == srcIndex) {
                    destIndex = rand.nextInt(i);
                }

                // call BFS & measure Execution Time
                start_time = System.nanoTime();
                graph.BFS(srcIndex, destIndex, false);
                end_time = System.nanoTime();
                time_duration = (end_time - start_time) / 1000000.0;
                // add to time_duration to duration_record
                duration_record.add(time_duration);
            }
            // compute average
            average_duration = computeAverage(duration_record);

            // output to CSV
            cellnum = 0;
            row = sheet.createRow(rownum++);
            cell = row.createCell(cellnum++);
            cell.setCellValue(i); // graph size
            cell = row.createCell(cellnum++);
            cell.setCellValue(numOfSelectedEdges);
            cell = row.createCell(cellnum++);
            cell.setCellValue(average_duration);
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
        return ans / 1000.0;
    }

}
