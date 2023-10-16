package com.cloud.sources;

import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.Arrays;

import weka.core.Instance;
import weka.core.Instances;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.EvalFunc;



public class cloud extends EvalFunc<String>{
    
    @Override  
	public String exec(Tuple input) throws IOException {
		try{
            Integer aa = (Integer) input.get(input.size()-1);
            int[] matrix = transformObj2Int(input);
            String a = "";
            // Apply index
            IndexList indexes = obtainIndex(matrix);
            double ndvi_value = indexes.getNdvi();
            double ndoi_value = indexes.getNdoi();
            double fi_value = indexes.getFi();
            double hi_value = indexes.getHi();
            double osi_value = indexes.getOsi();
            double cdom_value = indexes.getCdom();
            double s211_value = indexes.getS211();
            double ndwi_value = indexes.getNdwi();
            double waf_value = indexes.getWaf();

            
            a = Double.toString(ndvi_value) + " " + Double.toString(ndoi_value) + " " + Double.toString(fi_value) + " " + Double.toString(hi_value) + " " + Double.toString(osi_value) + " " + Double.toString(cdom_value) + " " + Double.toString(s211_value) + " " + Double.toString(ndwi_value) + " " + Double.toString(waf_value);
            
            return a;
            
            } catch (Exception e) {
                throw new IOException("Caught exception processing input row ", e);
	        }
		    
    }
    
    public static double[] removeLastElement(double[] arr) {
        return Arrays.copyOf(arr, arr.length - 1);
    }
    
    public static List<String[]> readFile(String path) {
        // Read csv as String
        List<String[]> rowList = new ArrayList<String[]>();
        
        try{
        	FileReader p = new FileReader(path);
        	BufferedReader br = new BufferedReader(p);
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] lineItems = line.split(",");
                rowList.add(lineItems);
            }
            br.close();
        }
        catch(Exception e){
            System.out.println("Exception");
        }
        return rowList;
    }
/*
    public static int[][] transformStr2Int(List<double[]> rowList){
        // Transform str to int matrix
        int[][] matrix = new int[rowList.size()][rowList.get(0).length];
        for (int i = 0; i < rowList.size(); i++) {
            double[] row = rowList.get(i);
            for (int j = 0; j < row.length; j++) {
                matrix[i][j] = (int) row[j];
            }
        }
        return matrix;
    }
*/
    public int[] transformObj2Int(Tuple input) throws IOException {
    try{
        int size = input.size()-1;
        int objs[] = new int[size];
        for(int i=0; i<size; i++)
        {
            objs[i] = (Integer) input.get(i);
        }
        return objs;
        }catch (Exception e) {
                throw new IOException("Caught exception processing input row ", e);
        }
    }
    public static double ndvi(int red, int nir){
        // Normalized Division Vegetation Index
        double ndvi = ((double)nir - red)/(nir + red);
        return ndvi;
    }

    public static double ndoi(int green, int nir){
        double ndoi = ((double)green - nir)/(green + nir);
        return ndoi;
    }

    public static double fi(int red, int blue){
        double fi = ((double)blue - red)/(blue + red);
        return fi;
    }

    public static double hi(int ra, int rb, int rc){
        double hi = 30*((double)rc - ra)/40 + (double)ra - (double)rb;
        return hi;
    }

    public static double osi(int r675, int r743) {
        double osi = ((double)r743 - r675)/70;
        return osi;
    }

    public static double cdom(int r565, int r660){
        double cdom = (double)r565/r660;
        return cdom;
    }

    public static double s211(int blue, int r1610){
        double b2b11 = (double)blue/r1610;
        return b2b11;
    }

    public static double ndwi(int r860, int r1240){
        double ndwi = ((double)r860 - r1240)/(r860 + r1240);
        return ndwi;
    }

    public static double waf(int r1343, int r1453, int r1563){
        double waf = ((double)r1343 - r1563)/2 - r1453;
        return waf;
    }
    /* 
    public static double chl(int r433, int r490, int r510, int r555){
        // Invent index
        double ndoi = -((double)green - nir)/(green + nir);
        return ndoi;
    }
    public static double rai (int blue, int r889){
        for (int i=0; i < blue.length; i++) {
            mean_b = mean_b + blue[i];
        }
        mean_b = mean_b / blue.length;


        double modulo = Math.sqrt(25);
        double ndoi = -((double)green - nir)/(green + nir);
        return ndoi;
    }
    */

    public static IndexList obtainIndex(int[] matrix){
        // vector to save the indices
        double ndvi_value = 0.0;
        double ndoi_value = 0.0;
        double fi_value = 0.0;
        double hi_value = 0.0;
        double osi_value = 0.0;
        double cdom_value = 0.0;
        double s211_value = 0.0;
        double ndwi_value = 0.0;
        double waf_value = 0.0;
        // Apply to pixels
            ndvi_value = ndvi(matrix[31], matrix[56]);
            ndoi_value = ndoi(matrix[25], matrix[55]);
            fi_value = fi(matrix[31], matrix[13]);
            hi_value = hi(matrix[141], matrix[144], matrix[145]);
            osi_value = osi(matrix[34], matrix[41]);
            cdom_value = cdom(matrix[20], matrix[30]);
            s211_value = s211(matrix[13], matrix[132]);
            ndwi_value = ndwi(matrix[53], matrix[93]);
            waf_value = waf(matrix[105], matrix[116], matrix[127]);
        return new IndexList(ndvi_value, ndoi_value, fi_value, hi_value, osi_value, cdom_value, s211_value, ndwi_value, waf_value);
    }
}

final class IndexList {
    private final double ndvi;
    private final double ndoi;
    private final double fi;
    private final double hi;
    private final double osi;
    private final double cdom;
    private final double s211;
    private final double ndwi;
    private final double waf;

    public IndexList(double ndvi, double ndoi, double fi, double hi, double osi, double cdom, double s211, double ndwi, double waf) {
        this.ndvi = ndvi;
        this.ndoi = ndoi;
        this.fi = fi;
        this.hi = hi;
        this.osi = osi;
        this.cdom = cdom;
        this.s211 = s211;
        this.ndwi = ndwi;
        this.waf = waf;
    }

    public double getNdoi() {
        return ndoi;
    }

    public double getNdvi() {
        return ndvi;
    }

    public double getFi() {
        return fi;
    }

    public double getHi() {
        return hi;
    }

    public double getOsi() {
        return osi;
    }

    public double getCdom() {
        return cdom;
    }

    public double getS211() {
        return s211;
    }

    public double getNdwi() {
        return ndwi;
    }

    public double getWaf() {
        return waf;
    }
}
