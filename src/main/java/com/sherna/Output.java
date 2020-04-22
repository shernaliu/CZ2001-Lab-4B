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
        XSSFSheet sheet = workbook.createSheet("Experiment 1");

        int rownum = 0;
        int cellnum = 0;
        Row row = null;
        Cell cell = null;

        // write header
        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellValue("No. of Cities");
        cell = row.createCell(cellnum++);
        cell.setCellValue("No. of Edges");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Average Execution Time (ms)");

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

        // Sherna's note: Depending on your experiment size and computer, it may take a while.

        // EXPERIMENT 1 - Vary the number of cities, fix fraction of number of edges
        // compute average computation time for graph size = 100, 200, 300, ..., 1000 with selected number of edges
        for (int i = 100; i <= 1000; i += 100) {
            // calculate fraction of maximum number of edges for n cities
            numOfSelectedEdges = (i * i - i) / 4;

            // reset duration_record
            duration_record = new ArrayList<>();

            // generate graph of n cities 100 times to do record execution time 100 times for the experiment
            for (int j = 0; j < 100; j++) {
                graph = new Graph.Reader("data/cities.txt").read("cities", i, numOfSelectedEdges);
                srcIndex = 0; // randomly select index for src and dest
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
            average_duration = computeAverage(duration_record, duration_record.size());

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

        // EXPERIMENT 2 - Fix number of cities, vary the fraction of max edges
        // Create a blank sheet
        XSSFSheet sheet2 = workbook.createSheet("Experiment 2");

        rownum = 0;
        cellnum = 0;
        row = null;
        cell = null;

        // write header
        row = sheet2.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellValue("No. of Cities");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Fraction of Max. Edges");
        cell = row.createCell(cellnum++);
        cell.setCellValue("No. of Edges");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Average Execution Time (ms)");

        graph = null;
        start_time = 0;
        end_time = 0;
        time_duration = 0;
        duration_record = new ArrayList<>();
        average_duration = 0;
        srcIndex = 0;
        destIndex = 0;
        rand = new Random();
        numOfSelectedEdges = 0;
        // Sherna's note: if u use double, when u reach 0.8, it becomes 0.79999999999 and calculation for the number of nodes is wrong :(
        float fractionOfMaxEdges = 0.1f;

        // EXPERIMENT 2 - Fix the number of cities n=1000, vary the fraction of maximum number of edges
        // compute average computation time for graph size = 1000 with selected number of edges
        for (int i = 0; i < 10; i += 1) {
            // calculate fraction of maximum number of edges for n cities
            numOfSelectedEdges = (int) ((1000 * 1000 - 1000) / 2 * fractionOfMaxEdges);

            // reset duration_record
            duration_record = new ArrayList<>();

            // generate graph of n cities 100 times to do record execution time 100 times for the experiment
            for (int j = 0; j < 100; j++) {
                graph = new Graph.Reader("data/cities.txt").read("cities", 1000, numOfSelectedEdges);
                srcIndex = 0; // randomly select index for src and dest
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
            average_duration = computeAverage(duration_record, duration_record.size());

            // output to CSV
            cellnum = 0;
            row = sheet2.createRow(rownum++);
            cell = row.createCell(cellnum++);
            cell.setCellValue(1000); // graph size
            cell = row.createCell(cellnum++);
            cell.setCellValue(String.format("%.1f", fractionOfMaxEdges));
            cell = row.createCell(cellnum++);
            cell.setCellValue(numOfSelectedEdges);
            cell = row.createCell(cellnum++);
            cell.setCellValue(average_duration);

            // increment fractionOfMaxEdges by 0.1
            fractionOfMaxEdges += 0.1;
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

    public double computeAverage(List<Double> records, int recordsSize) {
        double ans = 0;
        for (Double record : records) {
            ans += record;
        }
        return ans / recordsSize;
    }

}
