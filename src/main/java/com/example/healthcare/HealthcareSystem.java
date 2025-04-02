package com.example.healthcare;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HealthcareSystem {

    public static void main(String[] args) {
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient("John Doe", 30, "Flu"));
        patients.add(new Patient("Jane Smith", 25, "Cold"));
        patients.add(new Patient("Emily Johnson", 40, "Diabetes"));
        patients.add(new Patient("Michael Brown", 35, "Flu"));

        // Display all patients
        displayAllPatients(patients);

        // Filter and display patients with the diagnosis "Flu"
        List<Patient> fluPatients = filterPatientsByDiagnosis(patients, "Flu");
        displayFilteredPatients(fluPatients);
    }

    private static void displayAllPatients(List<Patient> patients) {
        System.out.println("All patients:");
        patients.forEach(System.out::println);
    }

    private static List<Patient> filterPatientsByDiagnosis(List<Patient> patients, String diagnosis) {
        return patients.stream()
                .filter(patient -> diagnosis.equals(patient.getDiagnosis()))
                .collect(Collectors.toList());
    }

    private static void displayFilteredPatients(List<Patient> patients) {
        System.out.println("\nPatients with " + (patients.isEmpty() ? "no diagnosis" : patients.get(0).getDiagnosis()) + ":");
        patients.forEach(System.out::println);
    }
}