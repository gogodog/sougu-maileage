package com.sougu.maileage;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class MileageUtils2 {

    public static void main(String[] args) throws IOException {
        List<String> files = Utils.getFiles("./static/source");
        for (String filePath : files) {
            System.out.println("*************************************************************************************************************************");
            runDistance(filePath);
        }
    }

    public static void runDistance(String filePath) {
        long start = System.currentTimeMillis();
        File file = new File(filePath);
        List<Point> track1 = Utils.getTrackByTxt(file);
        track1.sort(Comparator.comparing(Point :: getWn).thenComparing(Point :: getSow));
        List<Point> track = filterStop(track1);
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

    private static List<Point> filterV(List<Point> points) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 1; i < points.size() ; i++) {

        }


        return null;
    }

    private static List<Point> filterStop(List<Point> points) {
        int start = 0;
        int end = points.size()-1;
        for (int i = 1; i < points.size() ; i++) {
            Point a = points.get(i-1);
            Point b = points.get(i);
            BigDecimal abMileage = getMileageFromAtoB(a, b);
            if (abMileage.compareTo(BigDecimal.ZERO) != 0) {
                start = i;
                break;
            }
        }
        for (int i = points.size() - 1; i > 0 ; i--) {
            Point a = points.get(i);
            Point b = points.get(i-1);
            BigDecimal abMileage = getMileageFromAtoB(a, b);
            if (abMileage.compareTo(BigDecimal.ZERO) != 0) {
                end = i;
                break;
            }
        }
        if(start >= end) {
            return null;
        }
        return points.subList(start, end);
    }

    private static long timeLength(List<Point> track) {
        if(track.size() <= 1) {
            return 0L;
        }
        return track.get(track.size()-1).sow.subtract(track.get(0).sow).abs().longValue();
    }



    private static BigDecimal getMileage(List<Point> points) {
        BigDecimal allMileage = BigDecimal.ZERO;
        List<BigDecimal> abMax = new ArrayList<BigDecimal>();
        for (int i = 1; i < points.size() ; i++) {
            Point a = points.get(i-1);
            Point b = points.get(i);
            BigDecimal abMileage = getMileageFromAtoB(a, b);
            if(abMileage.compareTo(new BigDecimal(0.28)) <= 0) {
                continue;
            }
            allMileage = allMileage.add(abMileage);
            abMax.add(abMileage);
        }
        for (int i = 0; i < abMax.size(); i++) {
            if(i % 500 == 0) {
                System.out.println();
            }
            System.out.print(abMax.get(i).setScale(2, RoundingMode.HALF_UP) + ", ");
        }
        System.out.println("--------------------------------------------------------------------------------------");
        abMax.sort(Utils.comp);
        for (int i = 0; i < abMax.size(); i++) {
            if(i % 500 == 0) {
                System.out.println();
            }
            System.out.print(abMax.get(i).setScale(2, RoundingMode.HALF_UP) + ", ");
        }
        System.out.println("--");
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
        BigDecimal x2 = Utils.addSquare(a.r_cvr_x, b.r_cvr_x);
        BigDecimal y2 = Utils.addSquare(a.r_cvr_y, b.r_cvr_y);
        BigDecimal z2 = Utils.addSquare(a.r_cvr_z, b.r_cvr_z);
        BigDecimal xyz2 = x2.add(y2).add(z2);
        return Utils.sqrt(xyz2, 20);
    }



}
