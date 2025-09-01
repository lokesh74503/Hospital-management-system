package com.hms.patient.service;

import com.hms.patient.dto.PatientDto;
import com.hms.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    
    /**
     * Create a new patient
     */
    PatientDto createPatient(PatientDto patientDto);
    
    /**
     * Get patient by ID
     */
    Optional<PatientDto> getPatientById(Long id);
    
    /**
     * Get patient by user ID
     */
    Optional<PatientDto> getPatientByUserId(Long userId);
    
    /**
     * Get all patients with pagination
     */
    Page<PatientDto> getAllPatients(Pageable pageable);
    
    /**
     * Update patient
     */
    Optional<PatientDto> updatePatient(Long id, PatientDto patientDto);
    
    /**
     * Delete patient
     */
    boolean deletePatient(Long id);
    
    /**
     * Search patients by name
     */
    List<PatientDto> searchPatientsByName(String name);
    
    /**
     * Get patients by phone number
     */
    Optional<PatientDto> getPatientByPhone(String phone);
    
    /**
     * Get patients by blood group
     */
    List<PatientDto> getPatientsByBloodGroup(String bloodGroup);
    
    /**
     * Get patients by gender
     */
    List<PatientDto> getPatientsByGender(Patient.Gender gender);
    
    /**
     * Get patients by insurance provider
     */
    List<PatientDto> getPatientsByInsuranceProvider(String insuranceProvider);
    
    /**
     * Get patients by insurance number
     */
    Optional<PatientDto> getPatientByInsuranceNumber(String insuranceNumber);
    
    /**
     * Get patients with allergies containing specific text
     */
    List<PatientDto> getPatientsByAllergies(String allergy);
    
    /**
     * Get patients with medical history containing specific text
     */
    List<PatientDto> getPatientsByMedicalHistory(String condition);
    
    /**
     * Get patients created in a date range
     */
    List<PatientDto> getPatientsByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get patients born in a specific year
     */
    List<PatientDto> getPatientsByBirthYear(int year);
    
    /**
     * Get patients born in a date range
     */
    List<PatientDto> getPatientsByBirthDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get patient statistics
     */
    PatientStatistics getPatientStatistics();
    
    /**
     * Check if patient exists by user ID
     */
    boolean existsByUserId(Long userId);
    
    /**
     * Check if patient exists by phone
     */
    boolean existsByPhone(String phone);
    
    /**
     * Check if patient exists by insurance number
     */
    boolean existsByInsuranceNumber(String insuranceNumber);
    
    /**
     * Patient statistics class
     */
    class PatientStatistics {
        private long totalPatients;
        private long malePatients;
        private long femalePatients;
        private long otherGenderPatients;
        private long patientsWithInsurance;
        private long patientsWithAllergies;
        
        // Constructors
        public PatientStatistics() {}
        
        public PatientStatistics(long totalPatients, long malePatients, long femalePatients, 
                               long otherGenderPatients, long patientsWithInsurance, long patientsWithAllergies) {
            this.totalPatients = totalPatients;
            this.malePatients = malePatients;
            this.femalePatients = femalePatients;
            this.otherGenderPatients = otherGenderPatients;
            this.patientsWithInsurance = patientsWithInsurance;
            this.patientsWithAllergies = patientsWithAllergies;
        }
        
        // Getters and Setters
        public long getTotalPatients() {
            return totalPatients;
        }
        
        public void setTotalPatients(long totalPatients) {
            this.totalPatients = totalPatients;
        }
        
        public long getMalePatients() {
            return malePatients;
        }
        
        public void setMalePatients(long malePatients) {
            this.malePatients = malePatients;
        }
        
        public long getFemalePatients() {
            return femalePatients;
        }
        
        public void setFemalePatients(long femalePatients) {
            this.femalePatients = femalePatients;
        }
        
        public long getOtherGenderPatients() {
            return otherGenderPatients;
        }
        
        public void setOtherGenderPatients(long otherGenderPatients) {
            this.otherGenderPatients = otherGenderPatients;
        }
        
        public long getPatientsWithInsurance() {
            return patientsWithInsurance;
        }
        
        public void setPatientsWithInsurance(long patientsWithInsurance) {
            this.patientsWithInsurance = patientsWithInsurance;
        }
        
        public long getPatientsWithAllergies() {
            return patientsWithAllergies;
        }
        
        public void setPatientsWithAllergies(long patientsWithAllergies) {
            this.patientsWithAllergies = patientsWithAllergies;
        }
        
        @Override
        public String toString() {
            return "PatientStatistics{" +
                    "totalPatients=" + totalPatients +
                    ", malePatients=" + malePatients +
                    ", femalePatients=" + femalePatients +
                    ", otherGenderPatients=" + otherGenderPatients +
                    ", patientsWithInsurance=" + patientsWithInsurance +
                    ", patientsWithAllergies=" + patientsWithAllergies +
                    '}';
        }
    }
}
