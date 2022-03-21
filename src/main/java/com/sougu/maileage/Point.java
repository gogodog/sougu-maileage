package com.sougu.maileage;

import java.math.BigDecimal;

public class Point {
    BigDecimal wn = BigDecimal.ZERO;
    BigDecimal sow = BigDecimal.ZERO;
    BigDecimal r_cvr_x = BigDecimal.ZERO;
    BigDecimal r_cvr_y = BigDecimal.ZERO;
    BigDecimal r_cvr_z = BigDecimal.ZERO;
    BigDecimal r_cvr_v_x = BigDecimal.ZERO;
    BigDecimal r_cvr_v_y = BigDecimal.ZERO;
    BigDecimal r_cvr_v_z = BigDecimal.ZERO;

    public BigDecimal getWn() {
        return wn;
    }

    public void setWn(BigDecimal wn) {
        this.wn = wn;
    }

    public BigDecimal getSow() {
        return sow;
    }

    public void setSow(BigDecimal sow) {
        this.sow = sow;
    }

    public BigDecimal getR_cvr_x() {
        return r_cvr_x;
    }

    public void setR_cvr_x(BigDecimal r_cvr_x) {
        this.r_cvr_x = r_cvr_x;
    }

    public BigDecimal getR_cvr_y() {
        return r_cvr_y;
    }

    public void setR_cvr_y(BigDecimal r_cvr_y) {
        this.r_cvr_y = r_cvr_y;
    }

    public BigDecimal getR_cvr_z() {
        return r_cvr_z;
    }

    public void setR_cvr_z(BigDecimal r_cvr_z) {
        this.r_cvr_z = r_cvr_z;
    }

    Point(BigDecimal wn, BigDecimal sow, BigDecimal r_cvr_x, BigDecimal r_cvr_y, BigDecimal r_cvr_z, BigDecimal r_cvr_v_x, BigDecimal r_cvr_v_y, BigDecimal r_cvr_v_z) {
        this.wn = wn;
        this.sow = sow;
        this.r_cvr_x = r_cvr_x;
        this.r_cvr_y = r_cvr_y;
        this.r_cvr_z = r_cvr_z;
        this.r_cvr_v_x = r_cvr_v_x;
        this.r_cvr_v_y = r_cvr_v_y;
        this.r_cvr_v_z = r_cvr_v_z;
    }

    Point(BigDecimal sow, BigDecimal r_cvr_x, BigDecimal r_cvr_y, BigDecimal r_cvr_z, BigDecimal r_cvr_v_x, BigDecimal r_cvr_v_y, BigDecimal r_cvr_v_z) {
        this.sow = sow;
        this.r_cvr_x = r_cvr_x;
        this.r_cvr_y = r_cvr_y;
        this.r_cvr_z = r_cvr_z;
        this.r_cvr_v_x = r_cvr_v_x;
        this.r_cvr_v_y = r_cvr_v_y;
        this.r_cvr_v_z = r_cvr_v_z;
    }
}
