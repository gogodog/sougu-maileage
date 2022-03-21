package com.sougu.maileage;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @className: Utils
 * @description: TODO 类描述
 * @author: niaonao
 * @date: 2022/3/17
 **/
public class Utils {
    public static Point txtTransferPoint(String[] txt_arrays) {
        BigDecimal x1 = new BigDecimal(txt_arrays[1]);
        return new Point(new BigDecimal(txt_arrays[1]), new BigDecimal(txt_arrays[2]), new BigDecimal(txt_arrays[3]), new BigDecimal(txt_arrays[4]), new BigDecimal(txt_arrays[5]), new BigDecimal(txt_arrays[6]), new BigDecimal(txt_arrays[7]), new BigDecimal(txt_arrays[8]));
//        return new Point(new BigDecimal(txt_arrays[3]).multiply(new BigDecimal(10000000000L)), new BigDecimal(txt_arrays[4]).multiply(new BigDecimal(10000000000L)), new BigDecimal(txt_arrays[5]).multiply(new BigDecimal(10000000000L)));
    }

    public static Point txtTransferUMTPoint(String[] txt_arrays) {
        BigDecimal x3 = new BigDecimal(txt_arrays[3]);
        BigDecimal x4 = new BigDecimal(txt_arrays[4]);
        BigDecimal x5 = new BigDecimal(txt_arrays[5]);
        BigDecimal x6 = new BigDecimal(txt_arrays[6]);
        BigDecimal x7 = new BigDecimal(txt_arrays[7]);
        BigDecimal x8 = new BigDecimal(txt_arrays[8]);
        BigDecimal x0 = new BigDecimal(Double.valueOf(txt_arrays[0]));
        return new Point(x0, x3, x4, x5, x6, x7, x8);
    }

    public static Comparator<BigDecimal> comp = (BigDecimal a, BigDecimal b) -> {
        return a.compareTo(b);
    };

    public static List<Point> getTrackByTxt(File trackFile) {
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        List<Point> points = new LinkedList<>();
        try {
            if (trackFile.isFile() && trackFile.exists()) { //判断文件是否存在
                reader = new InputStreamReader(new FileInputStream(trackFile), "UTF-8");
                bufferedReader = new BufferedReader(reader);
                String lineTxt = null;
                long index = 0;
                long start = 1;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String txt = lineTxt;
                    long line = index ++;
                    String[] txt_arrays = lineTxt.split("\\s+");
                    if(txt_arrays == null|| txt_arrays.length <10) {
                        continue;
                    }
                    if(line < start ) {
                        continue;
                    }
                    Point point = Utils.txtTransferPoint(txt_arrays);
                    points.add(point);

                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
                if(bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return points;
    }

    public static List<Point> getUMTTrackByTxt(File trackFile) {
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        List<Point> points = new LinkedList<>();
        try {
            if (trackFile.isFile() && trackFile.exists()) { //判断文件是否存在
                reader = new InputStreamReader(new FileInputStream(trackFile), "UTF-8");
                bufferedReader = new BufferedReader(reader);
                String lineTxt = null;
                long index = 0;
                long start = 0;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String txt = lineTxt;
                    long line = index ++;
                    String[] txt_arrays = lineTxt.split(",");
                    if(txt_arrays == null|| txt_arrays.length <10) {
                        continue;
                    }
                    if(line < start ) {
                        continue;
                    }
                    Point point = Utils.txtTransferUMTPoint(txt_arrays);
                    points.add(point);

                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
                if(bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return points;
    }

    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
            }
        }
        return files;
    }

    public static BigDecimal addSquare(BigDecimal a, BigDecimal b) {
        BigDecimal sub = a.subtract(b);
        return  sub.pow(2);
    }

    public static BigDecimal sqrt(BigDecimal value, int scale){
        if(value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if(value.compareTo(BigDecimal.ZERO) < 0) {
            value = value.abs();
        }
        BigDecimal num2 = BigDecimal.valueOf(2);
        int precision = 100;
        MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
        BigDecimal deviation = value;
        int cnt = 0;
        while (cnt < precision) {
            deviation = (deviation.add(value.divide(deviation, mc))).divide(num2, mc);
            cnt++;
        }
        return deviation.setScale(scale, RoundingMode.HALF_UP);
    }
}
