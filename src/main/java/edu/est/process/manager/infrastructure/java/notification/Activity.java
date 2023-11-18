package edu.est.process.manager.infrastructure.java.notification;

public class Activity {
    private String nombre;
    private long tiempoRestante; // Tiempo restante en milisegundos

    public Activity(String nombre, long tiempoRestante) {
        this.nombre = nombre;
        this.tiempoRestante = tiempoRestante;
    }

    public void disminuirTiempo(long tiempo) {
        this.tiempoRestante -= tiempo;
    }

    public String getNombre() {
        return nombre;
    }

    public long getTiempoRestante() {
        return tiempoRestante;
    }
}
