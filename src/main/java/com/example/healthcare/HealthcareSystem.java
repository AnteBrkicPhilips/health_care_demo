package com.example.healthcare;

import com.example.healthcare.metrics.CustomMetrics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HealthcareSystem {

    public static void main(String[] args) {
        HealthcareSystem healthcareSystem = new HealthcareSystem();
        CustomMetrics metricPrototype = new CustomMetrics();
        while (true){
            healthcareSystem.run();
            metricPrototype.getRequestCounter().add(1);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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