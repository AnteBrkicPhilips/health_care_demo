package com.example.healthcare;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.Meter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.healthcare.DatabaseUtil.closeConnection;

public class HealthcareSystem {

    public static void main(String[] args) {
        runActiveConnectionExample();
    }

    public static void runDBExample() {
        HealthcareSystem healthcareSystem = new HealthcareSystem();
        while (true) {
            healthcareSystem.run();
            healthcareSystem.emulateError();
            try {
                TimeUnit.SECONDS.sleep(3);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void runActiveConnectionExample() {
        Meter meter = GlobalOpenTelemetry.getMeter("db.connections.meter");
        meter.gaugeBuilder("custom.db.connections.active")
                .setDescription("Number of active database connections")
                .setUnit("connections")
                .buildWithCallback(measurement -> {
                    measurement.record(DatabaseUtil.getActiveConnections());
                });

        Runnable dbTask = () ->        {
            Connection connection = DatabaseUtil.getConnection();
            try {
                Thread.sleep(new Random().nextInt(20000));
                closeConnection(connection);
            }
            catch (InterruptedException | SQLException e) {
                throw new RuntimeException(e);
            }
        };
        IntStream.range(1, 5).forEach(e -> new Thread(dbTask).start());
    }

    public void run() {
    	DatabaseUtil.truncateTable();
    	
        insertPatient(new Patient("John Doe", 30, "Flu"));
        insertPatient(new Patient("Jane Smith", 25, "Cold"));
        insertPatient(new Patient("Emily Johnson", 40, "Diabetes"));
        insertPatient(new Patient("Michael Brown", 35, "Flu"));

        List<Patient> patients = getAllPatients();

        // Display all patients
        displayAllPatients(patients);

        // Filter and display patients with the diagnosis "Flu"
        List<Patient> fluPatients = filterPatientsByDiagnosis(patients, "Flu");
        displayFilteredPatients(fluPatients);
    }

    public void emulateError() {
        String insertSQL = "INSERT INTO dem2.patients2 (name, age, diagnosis) VALUES (1, 2, 3)";

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPatient(Patient patient) {
        String insertSQL = "INSERT INTO dem1.patients (name, age, diagnosis) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, patient.getName());
            pstmt.setInt(2, patient.getAge());
            pstmt.setString(3, patient.getDiagnosis());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String selectSQL = "SELECT * FROM dem1.patients";

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String diagnosis = rs.getString("diagnosis");
                patients.add(new Patient(name, age, diagnosis));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    public void displayAllPatients(List<Patient> patients) {
        System.out.println("All patients:\n");
        patients.forEach(System.out::println);
    }

    public List<Patient> filterPatientsByDiagnosis(List<Patient> patients, String diagnosis) {
        return patients.stream()
                .filter(patient -> diagnosis.equals(patient.getDiagnosis()))
                .collect(Collectors.toList());
    }

    public void displayFilteredPatients(List<Patient> patients) {
        System.out.println("\nPatients with " + (patients.isEmpty() ? "no diagnosis" : patients.get(0).getDiagnosis()) + ":\n");
        patients.forEach(System.out::println);
    }
    

}