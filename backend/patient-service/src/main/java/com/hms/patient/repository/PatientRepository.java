package com.hms.patient.repository;

import com.hms.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    /**
     * Find patient by user ID
     */
    Optional<Patient> findByUserId(Long userId);
    
    /**
     * Find patients by first name containing (case-insensitive)
     */
    List<Patient> findByFirstNameContainingIgnoreCase(String firstName);
    
    /**
     * Find patients by last name containing (case-insensitive)
     */
    List<Patient> findByLastNameContainingIgnoreCase(String lastName);
    
    /**
     * Find patients by phone number
     */
    Optional<Patient> findByPhone(String phone);
    
    /**
     * Find patients by blood group
     */
    List<Patient> findByBloodGroup(String bloodGroup);
    
    /**
     * Find patients by gender
     */
    List<Patient> findByGender(Patient.Gender gender);
    
    /**
     * Find patients by insurance provider
     */
    List<Patient> findByInsuranceProvider(String insuranceProvider);
    
    /**
     * Find patients by insurance number
     */
    Optional<Patient> findByInsuranceNumber(String insuranceNumber);
    
    /**
     * Search patients by name (first name or last name)
     */
    @Query("SELECT p FROM Patient p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Patient> searchByName(@Param("name") String name);
    
    /**
     * Find patients with allergies containing specific text
     */
    @Query("SELECT p FROM Patient p WHERE p.allergies IS NOT NULL AND LOWER(p.allergies) LIKE LOWER(CONCAT('%', :allergy, '%'))")
    List<Patient> findByAllergiesContaining(@Param("allergy") String allergy);
    
    /**
     * Find patients with medical history containing specific text
     */
    @Query("SELECT p FROM Patient p WHERE p.medicalHistory IS NOT NULL AND LOWER(p.medicalHistory) LIKE LOWER(CONCAT('%', :condition, '%'))")
    List<Patient> findByMedicalHistoryContaining(@Param("condition") String condition);
    
    /**
     * Count patients by gender
     */
    long countByGender(Patient.Gender gender);
    
    /**
     * Count patients by blood group
     */
    long countByBloodGroup(String bloodGroup);
    
    /**
     * Find patients created in a specific date range
     */
    @Query("SELECT p FROM Patient p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Patient> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                        @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find patients born in a specific year
     */
    @Query("SELECT p FROM Patient p WHERE YEAR(p.dateOfBirth) = :year")
    List<Patient> findByBirthYear(@Param("year") int year);
    
    /**
     * Find patients born in a specific date range
     */
    @Query("SELECT p FROM Patient p WHERE p.dateOfBirth BETWEEN :startDate AND :endDate")
    List<Patient> findByDateOfBirthBetween(@Param("startDate") java.time.LocalDate startDate, 
                                          @Param("endDate") java.time.LocalDate endDate);
    
    /**
     * Check if patient exists by user ID
     */
    boolean existsByUserId(Long userId);
    
    /**
     * Check if patient exists by phone number
     */
    boolean existsByPhone(String phone);
    
    /**
     * Check if patient exists by insurance number
     */
    boolean existsByInsuranceNumber(String insuranceNumber);
}
