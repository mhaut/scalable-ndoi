package com.cloud.sources;

import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;


public class cloud {
    public static void main(String[] args) {

        String path = args[0];
        String dest = args[1];

        // Read csv as String
        List<String[]> rowList = readFile(path);
        
        // Transform str to int matrix
        int[][] matrix = transformStr2Int(rowList);

        // Apply index
        IndexList indexes = obtainIndex(matrix);
        float[] ndvi_value = indexes.getNdvi();
        float[] ndoi_value = indexes.getNdoi();
        float[] fi_value = indexes.getFi();
        float[] hi_value = indexes.getHi();
        float[] osi_value = indexes.getOsi();
        float[] cdom_value = indexes.getCdom();
        float[] s211_value = indexes.getS211();
        float[] ndwi_value = indexes.getNdwi();
        float[] waf_value = indexes.getWaf();

        int n = ndvi_value.length;
        
        // Save data
        try{
            FileWriter writer = new FileWriter(dest);
                for (int j = 0; j < n; j++) {
                    writer.append(Float.toString(ndvi_value[j]));
                    writer.append(";");
                    writer.append(Float.toString(ndoi_value[j]));
                    writer.append(";");
                    writer.append(Float.toString(fi_value[j]));
                    writer.append(";");
                    writer.append(Float.toString(hi_value[j]));
                    writer.append(";");
                    writer.append(Float.toString(osi_value[j]));
                    writer.append(";");
                    writer.append(Float.toString(cdom_value[j]));
                    writer.append(";");
                    writer.append(Float.toString(s211_value[j]));
                    writer.append(";");
                    writer.append(Float.toString(ndwi_value[j]));
                    writer.append(";");
                    writer.append(Float.toString(waf_value[j]));
                    writer.append("\n");
                }
            writer.close();
        } catch(java.io.IOException e){
            System.out.println("createDirectory failed:" + e);
        }
        
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

    public static int[][] transformStr2Int(List<String[]> rowList){
        // Transform str to int matrix
        int[][] matrix = new int[rowList.size()][rowList.get(0).length];
        for (int i = 0; i < rowList.size(); i++) {
            String[] row = rowList.get(i);
            for (int j = 0; j < row.length; j++) {
                matrix[i][j] = Integer.valueOf(row[j]);
            }
        }
        return matrix;
    }

    public static float ndvi(int red, int nir){
        // Normalized Division Vegetation Index
        float ndvi = ((float)nir - red)/(nir + red);
        return ndvi;
    }

    public static float ndoi(int green, int nir){
        float ndoi = ((float)green - nir)/(green + nir);
        return ndoi;
    }

    public static float fi(int red, int blue){
        float fi = ((float)blue - red)/(blue + red);
        return fi;
    }

    public static float hi(int ra, int rb, int rc){
        float hi = 30*((float)rc - ra)/40 + (float)ra - (float)rb;
        return hi;
    }

    public static float osi(int r675, int r743) {
        float osi = ((float)r743 - r675)/70;
        return osi;
    }

    public static float cdom(int r565, int r660){
        float cdom = (float)r565/r660;
        return cdom;
    }

    public static float s211(int blue, int r1610){
        float b2b11 = (float)blue/r1610;
        return b2b11;
    }

    public static float ndwi(int r860, int r1240){
        float ndwi = ((float)r860 - r1240)/(r860 + r1240);
        return ndwi;
    }

    public static float waf(int r1343, int r1453, int r1563){
        float waf = ((float)r1343 - r1563)/2 - r1453;
        return waf;
    }
    /* 
    public static float chl(int r433, int r490, int r510, int r555){
        // Invent index
        float ndoi = -((float)green - nir)/(green + nir);
        return ndoi;
    }
    public static float rai (int blue, int r889){
        for (int i=0; i < blue.length; i++) {
            mean_b = mean_b + blue[i];
        }
        mean_b = mean_b / blue.length;


        float modulo = Math.sqrt(25);
        float ndoi = -((float)green - nir)/(green + nir);
        return ndoi;
    }
    */

    public static IndexList obtainIndex(int[][] matrix){
        // vector to save the indices
        float ndvi_value[] = new float[matrix.length];
        float ndoi_value[] = new float[matrix.length];
        float fi_value[] = new float[matrix.length];
        float hi_value[] = new float[matrix.length];
        float osi_value[] = new float[matrix.length];
        float cdom_value[] = new float[matrix.length];
        float s211_value[] = new float[matrix.length];
        float ndwi_value[] = new float[matrix.length];
        float waf_value[] = new float[matrix.length];
        // Apply to pixels
        for (int i = 0; i < matrix.length; i++) {
            ndvi_value[i] = ndvi(matrix[i][31], matrix[i][56]);
            ndoi_value[i] = ndoi(matrix[i][25], matrix[i][55]);
            fi_value[i] = fi(matrix[i][31], matrix[i][13]);
            hi_value[i] = hi(matrix[i][141], matrix[i][144], matrix[i][145]);
            osi_value[i] = osi(matrix[i][34], matrix[i][41]);
            cdom_value[i] = cdom(matrix[i][20], matrix[i][30]);
            s211_value[i] = s211(matrix[i][13], matrix[i][132]);
            ndwi_value[i] = ndwi(matrix[i][53], matrix[i][93]);
            waf_value[i] = waf(matrix[i][105], matrix[i][116], matrix[i][127]);
        }
        return new IndexList(ndvi_value, ndoi_value, fi_value, hi_value, osi_value, cdom_value, s211_value, ndwi_value, waf_value);
    }
}

final class IndexList {
    private final float[] ndvi;
    private final float[] ndoi;
    private final float[] fi;
    private final float[] hi;
    private final float[] osi;
    private final float[] cdom;
    private final float[] s211;
    private final float[] ndwi;
    private final float[] waf;

    public IndexList(float[] ndvi, float[] ndoi, float[] fi, float[] hi, float[] osi, float[] cdom, float[] s211, float[] ndwi, float[] waf) {
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

    public float[] getNdoi() {
        return ndoi;
    }

    public float[] getNdvi() {
        return ndvi;
    }

    public float[] getFi() {
        return fi;
    }

    public float[] getHi() {
        return hi;
    }

    public float[] getOsi() {
        return osi;
    }

    public float[] getCdom() {
        return cdom;
    }

    public float[] getS211() {
        return s211;
    }

    public float[] getNdwi() {
        return ndwi;
    }

    public float[] getWaf() {
        return waf;
    }
}
