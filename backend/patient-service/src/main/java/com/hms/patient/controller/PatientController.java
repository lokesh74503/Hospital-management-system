package com.hms.patient.controller;

import com.hms.patient.dto.PatientDto;
import com.hms.patient.model.Patient;
import com.hms.patient.service.PatientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/patients")
@CrossOrigin(origins = "*")
public class PatientController {
    
    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);
    
    private final PatientService patientService;
    
    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    
    /**
     * Create a new patient
     */
    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientDto patientDto) {
        logger.info("Creating new patient: {}", patientDto.getFirstName() + " " + patientDto.getLastName());
        
        try {
            PatientDto createdPatient = patientService.createPatient(patientDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating patient: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error creating patient", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get patient by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        logger.debug("Fetching patient by ID: {}", id);
        
        Optional<PatientDto> patient = patientService.getPatientById(id);
        return patient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get patient by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<PatientDto> getPatientByUserId(@PathVariable Long userId) {
        logger.debug("Fetching patient by user ID: {}", userId);
        
        Optional<PatientDto> patient = patientService.getPatientByUserId(userId);
        return patient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get all patients with pagination
     */
    @GetMapping
    public ResponseEntity<Page<PatientDto>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.debug("Fetching all patients with pagination: page={}, size={}, sortBy={}, sortDir={}", 
                    page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<PatientDto> patients = patientService.getAllPatients(pageable);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Update patient
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable Long id, 
                                                   @Valid @RequestBody PatientDto patientDto) {
        logger.info("Updating patient with ID: {}", id);
        
        Optional<PatientDto> updatedPatient = patientService.updatePatient(id, patientDto);
        return updatedPatient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Delete patient
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        logger.info("Deleting patient with ID: {}", id);
        
        boolean deleted = patientService.deletePatient(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    /**
     * Search patients by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<PatientDto>> searchPatientsByName(@RequestParam String name) {
        logger.debug("Searching patients by name: {}", name);
        
        List<PatientDto> patients = patientService.searchPatientsByName(name);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Get patient by phone number
     */
    @GetMapping("/phone/{phone}")
    public ResponseEntity<PatientDto> getPatientByPhone(@PathVariable String phone) {
        logger.debug("Fetching patient by phone: {}", phone);
        
        Optional<PatientDto> patient = patientService.getPatientByPhone(phone);
        return patient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get patients by blood group
     */
    @GetMapping("/blood-group/{bloodGroup}")
    public ResponseEntity<List<PatientDto>> getPatientsByBloodGroup(@PathVariable String bloodGroup) {
        logger.debug("Fetching patients by blood group: {}", bloodGroup);
        
        List<PatientDto> patients = patientService.getPatientsByBloodGroup(bloodGroup);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Get patients by gender
     */
    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<PatientDto>> getPatientsByGender(@PathVariable Patient.Gender gender) {
        logger.debug("Fetching patients by gender: {}", gender);
        
        List<PatientDto> patients = patientService.getPatientsByGender(gender);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Get patients by insurance provider
     */
    @GetMapping("/insurance-provider/{provider}")
    public ResponseEntity<List<PatientDto>> getPatientsByInsuranceProvider(@PathVariable String provider) {
        logger.debug("Fetching patients by insurance provider: {}", provider);
        
        List<PatientDto> patients = patientService.getPatientsByInsuranceProvider(provider);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Get patient by insurance number
     */
    @GetMapping("/insurance-number/{insuranceNumber}")
    public ResponseEntity<PatientDto> getPatientByInsuranceNumber(@PathVariable String insuranceNumber) {
        logger.debug("Fetching patient by insurance number: {}", insuranceNumber);
        
        Optional<PatientDto> patient = patientService.getPatientByInsuranceNumber(insuranceNumber);
        return patient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get patients with allergies containing specific text
     */
    @GetMapping("/allergies")
    public ResponseEntity<List<PatientDto>> getPatientsByAllergies(@RequestParam String allergy) {
        logger.debug("Fetching patients by allergies containing: {}", allergy);
        
        List<PatientDto> patients = patientService.getPatientsByAllergies(allergy);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Get patients with medical history containing specific text
     */
    @GetMapping("/medical-history")
    public ResponseEntity<List<PatientDto>> getPatientsByMedicalHistory(@RequestParam String condition) {
        logger.debug("Fetching patients by medical history containing: {}", condition);
        
        List<PatientDto> patients = patientService.getPatientsByMedicalHistory(condition);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Get patients created in a date range
     */
    @GetMapping("/created-date-range")
    public ResponseEntity<List<PatientDto>> getPatientsByCreatedDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        logger.debug("Fetching patients created between {} and {}", startDate, endDate);
        
        List<PatientDto> patients = patientService.getPatientsByCreatedDateRange(startDate, endDate);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Get patients born in a specific year
     */
    @GetMapping("/birth-year/{year}")
    public ResponseEntity<List<PatientDto>> getPatientsByBirthYear(@PathVariable int year) {
        logger.debug("Fetching patients born in year: {}", year);
        
        List<PatientDto> patients = patientService.getPatientsByBirthYear(year);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Get patients born in a date range
     */
    @GetMapping("/birth-date-range")
    public ResponseEntity<List<PatientDto>> getPatientsByBirthDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        logger.debug("Fetching patients born between {} and {}", startDate, endDate);
        
        List<PatientDto> patients = patientService.getPatientsByBirthDateRange(startDate, endDate);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Get patient statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<PatientService.PatientStatistics> getPatientStatistics() {
        logger.debug("Fetching patient statistics");
        
        PatientService.PatientStatistics statistics = patientService.getPatientStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * Check if patient exists by user ID
     */
    @GetMapping("/exists/user/{userId}")
    public ResponseEntity<Boolean> existsByUserId(@PathVariable Long userId) {
        logger.debug("Checking if patient exists by user ID: {}", userId);
        
        boolean exists = patientService.existsByUserId(userId);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Check if patient exists by phone
     */
    @GetMapping("/exists/phone/{phone}")
    public ResponseEntity<Boolean> existsByPhone(@PathVariable String phone) {
        logger.debug("Checking if patient exists by phone: {}", phone);
        
        boolean exists = patientService.existsByPhone(phone);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Check if patient exists by insurance number
     */
    @GetMapping("/exists/insurance-number/{insuranceNumber}")
    public ResponseEntity<Boolean> existsByInsuranceNumber(@PathVariable String insuranceNumber) {
        logger.debug("Checking if patient exists by insurance number: {}", insuranceNumber);
        
        boolean exists = patientService.existsByInsuranceNumber(insuranceNumber);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Patient Service is running");
    }
}
