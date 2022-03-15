package com.sougu.maileage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class MileageUtils {

    public static void main(String[] args) throws IOException {
        List<String> files = getFiles("./static");
        for (String filePath : files) {
            System.out.println("*************************************************************************************************************************");
            runDistance(filePath);
        }
    }

    public static void runDistance(String filePath) {
        long start = System.currentTimeMillis();
        File file = new File(filePath);
        List<Point> track = getTrackByTxt(file);
        track.sort(Comparator.comparing(Point :: getWn).thenComparing(Point :: getSow));
        BigDecimal  mileage = getMileage(track);
        Long timeLength = timeLength(track);
        System.out.println("轨迹数据（文件名称）" + file.getName());
        System.out.println("轨迹数据（点位数量）" + track.size());
        System.out.println("载体轨迹时长（秒）" + timeLength + "s");
        System.out.println("载体轨迹时长（分钟）" + timeLength/60 + "mins");
        System.out.println("载体轨迹里程（米）" + mileage.setScale(2, RoundingMode.HALF_UP).toPlainString() + "m");
        System.out.println("载体轨迹路径（千米）" + mileage.divide(new BigDecimal(1000)).setScale(2, RoundingMode.HALF_UP) + "km");
        System.out.println("任务用时（秒）" + (System.currentTimeMillis() - start)/1000 + "s");
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

    private static long timeLength(List<Point> track) {
        if(track.size() <= 1) {
            return 0L;
        }
        return track.get(track.size()-1).sow.subtract(track.get(0).sow).abs().longValue();
    }

    private static List<Point> getTrackByTxt(File trackFile) {
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
                    Point point = txtTransferPoint(txt_arrays);
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

    private static Point txtTransferPoint(String[] txt_arrays) {
        return new Point(new BigDecimal(txt_arrays[1]), new BigDecimal(txt_arrays[2]), new BigDecimal(txt_arrays[3]), new BigDecimal(txt_arrays[4]), new BigDecimal(txt_arrays[5]));
//        return new Point(new BigDecimal(txt_arrays[3]).multiply(new BigDecimal(10000000000L)), new BigDecimal(txt_arrays[4]).multiply(new BigDecimal(10000000000L)), new BigDecimal(txt_arrays[5]).multiply(new BigDecimal(10000000000L)));
    }

    private static BigDecimal getMileage(List<Point> points) {
        BigDecimal allMileage = BigDecimal.ZERO;
        for (int i = 1; i < points.size() ; i++) {
            Point a = points.get(i-1);
            Point b = points.get(i);
            BigDecimal abMileage = getMileageFromAtoB(a, b);
            allMileage = allMileage.add(abMileage);
        }
        return allMileage;
    }

    /**
     * version : 1.0
     * solution  : 三维欧式距离公式
     * @param a
     * @param b
     * @return
     */
    private static BigDecimal getMileageFromAtoB(Point a, Point b) {
        BigDecimal x2 = addSquare(a.r_cvr_x, b.r_cvr_x);
        BigDecimal y2 = addSquare(a.r_cvr_y, b.r_cvr_y);
        BigDecimal z2 = addSquare(a.r_cvr_z, b.r_cvr_z);
        BigDecimal xyz2 = x2.add(y2).add(z2);
//        System.out.println(x2 + " = " + x2.toPlainString() + ", y2 = "+ y2.toPlainString() + ", z2 = " + z2.toPlainString());
        BigDecimal sq = sqrt(xyz2, 20);
//        System.out.println("a->b  " + sq.toPlainString());
        return sq;
    }

    private static BigDecimal addSquare(BigDecimal a, BigDecimal b) {
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
        deviation = deviation.setScale(scale, RoundingMode.HALF_UP);
        return deviation;
    }

}
