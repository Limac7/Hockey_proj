package com.example.bebe.hockey;


public class SetPodataka {
    private double x_gyro;
    private double y_gyro;
    private double z_gyro;
    private double x_accel;
    private double y_accel;
    private double z_accel;
    private double force;
    private double spin;
    private double acc;
    private int id;
    private long timestamp;

    public SetPodataka(double x_gyro, double y_gyro, double z_gyro, double x_accel, double y_accel, double z_accel, double force, double spin, double acc, int id) {
        this.setX_gyro(x_gyro);
        this.setY_gyro(y_gyro);
        this.setZ_gyro(z_gyro);
        this.setX_accel(x_accel);
        this.setY_accel(y_accel);
        this.setZ_accel(z_accel);
        this.setForce(force);
        this.setSpin(spin);
        this.setAcc(acc);
        this.setId(id);
    }

    public double getAcc() {
        return acc;
    }

    public void setAcc(double acc) {
        this.acc = acc;
    }

    public double getForce() {
        return force;
    }

    public void setForce(double force) {
        this.force = force;
    }

    public double getSpin() {
        return spin;
    }

    public void setSpin(double spin) {
        this.spin = spin;
    }

    public double getX_gyro() {
        return x_gyro;
    }

    public void setX_gyro(double x_gyro) {
        this.x_gyro = x_gyro;
    }

    public double getY_gyro() {
        return y_gyro;
    }

    public void setY_gyro(double y_gyro) {
        this.y_gyro = y_gyro;
    }

    public double getZ_gyro() {
        return z_gyro;
    }

    public void setZ_gyro(double z_gyro) {
        this.z_gyro = z_gyro;
    }

    public double getX_accel() {
        return x_accel;
    }

    public void setX_accel(double x_accel) {
        this.x_accel = x_accel;
    }

    public double getY_accel() {
        return y_accel;
    }

    public void setY_accel(double y_accel) {
        this.y_accel = y_accel;
    }

    public double getZ_accel() {
        return z_accel;
    }

    public void setZ_accel(double z_accel) {
        this.z_accel = z_accel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
