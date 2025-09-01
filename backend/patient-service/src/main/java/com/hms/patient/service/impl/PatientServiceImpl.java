package com.hms.patient.service.impl;

import com.hms.patient.dto.PatientDto;
import com.hms.patient.model.Patient;
import com.hms.patient.repository.PatientRepository;
import com.hms.patient.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    
    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);
    
    private final PatientRepository patientRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.patientRepository = patientRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @Override
    public PatientDto createPatient(PatientDto patientDto) {
        logger.info("Creating new patient: {}", patientDto.getFirstName() + " " + patientDto.getLastName());
        
        // Validate unique constraints
        if (patientRepository.existsByUserId(patientDto.getUserId())) {
            throw new IllegalArgumentException("Patient with user ID " + patientDto.getUserId() + " already exists");
        }
        
        if (patientDto.getPhone() != null && patientRepository.existsByPhone(patientDto.getPhone())) {
            throw new IllegalArgumentException("Patient with phone " + patientDto.getPhone() + " already exists");
        }
        
        if (patientDto.getInsuranceNumber() != null && patientRepository.existsByInsuranceNumber(patientDto.getInsuranceNumber())) {
            throw new IllegalArgumentException("Patient with insurance number " + patientDto.getInsuranceNumber() + " already exists");
        }
        
        Patient patient = patientDto.toEntity();
        Patient savedPatient = patientRepository.save(patient);
        
        // Send Kafka event
        kafkaTemplate.send("patient-events", "PATIENT_CREATED:" + savedPatient.getId());
        
        logger.info("Patient created successfully with ID: {}", savedPatient.getId());
        return new PatientDto(savedPatient);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PatientDto> getPatientById(Long id) {
        logger.debug("Fetching patient by ID: {}", id);
        return patientRepository.findById(id).map(PatientDto::new);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PatientDto> getPatientByUserId(Long userId) {
        logger.debug("Fetching patient by user ID: {}", userId);
        return patientRepository.findByUserId(userId).map(PatientDto::new);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PatientDto> getAllPatients(Pageable pageable) {
        logger.debug("Fetching all patients with pagination");
        return patientRepository.findAll(pageable).map(PatientDto::new);
    }
    
    @Override
    public Optional<PatientDto> updatePatient(Long id, PatientDto patientDto) {
        logger.info("Updating patient with ID: {}", id);
        
        return patientRepository.findById(id).map(existingPatient -> {
            // Update fields
            existingPatient.setFirstName(patientDto.getFirstName());
            existingPatient.setLastName(patientDto.getLastName());
            existingPatient.setDateOfBirth(patientDto.getDateOfBirth());
            existingPatient.setGender(patientDto.getGender());
            existingPatient.setPhone(patientDto.getPhone());
            existingPatient.setAddress(patientDto.getAddress());
            existingPatient.setEmergencyContact(patientDto.getEmergencyContact());
            existingPatient.setBloodGroup(patientDto.getBloodGroup());
            existingPatient.setAllergies(patientDto.getAllergies());
            existingPatient.setMedicalHistory(patientDto.getMedicalHistory());
            existingPatient.setInsuranceProvider(patientDto.getInsuranceProvider());
            existingPatient.setInsuranceNumber(patientDto.getInsuranceNumber());
            
            Patient updatedPatient = patientRepository.save(existingPatient);
            
            // Send Kafka event
            kafkaTemplate.send("patient-events", "PATIENT_UPDATED:" + updatedPatient.getId());
            
            logger.info("Patient updated successfully with ID: {}", updatedPatient.getId());
            return new PatientDto(updatedPatient);
        });
    }
    
    @Override
    public boolean deletePatient(Long id) {
        logger.info("Deleting patient with ID: {}", id);
        
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            
            // Send Kafka event
            kafkaTemplate.send("patient-events", "PATIENT_DELETED:" + id);
            
            logger.info("Patient deleted successfully with ID: {}", id);
            return true;
        }
        
        logger.warn("Patient with ID {} not found for deletion", id);
        return false;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> searchPatientsByName(String name) {
        logger.debug("Searching patients by name: {}", name);
        return patientRepository.searchByName(name)
                .stream()
                .map(PatientDto::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PatientDto> getPatientByPhone(String phone) {
        logger.debug("Fetching patient by phone: {}", phone);
        return patientRepository.findByPhone(phone).map(PatientDto::new);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getPatientsByBloodGroup(String bloodGroup) {
        logger.debug("Fetching patients by blood group: {}", bloodGroup);
        return patientRepository.findByBloodGroup(bloodGroup)
                .stream()
                .map(PatientDto::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getPatientsByGender(Patient.Gender gender) {
        logger.debug("Fetching patients by gender: {}", gender);
        return patientRepository.findByGender(gender)
                .stream()
                .map(PatientDto::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getPatientsByInsuranceProvider(String insuranceProvider) {
        logger.debug("Fetching patients by insurance provider: {}", insuranceProvider);
        return patientRepository.findByInsuranceProvider(insuranceProvider)
                .stream()
                .map(PatientDto::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PatientDto> getPatientByInsuranceNumber(String insuranceNumber) {
        logger.debug("Fetching patient by insurance number: {}", insuranceNumber);
        return patientRepository.findByInsuranceNumber(insuranceNumber).map(PatientDto::new);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getPatientsByAllergies(String allergy) {
        logger.debug("Fetching patients by allergies containing: {}", allergy);
        return patientRepository.findByAllergiesContaining(allergy)
                .stream()
                .map(PatientDto::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getPatientsByMedicalHistory(String condition) {
        logger.debug("Fetching patients by medical history containing: {}", condition);
        return patientRepository.findByMedicalHistoryContaining(condition)
                .stream()
                .map(PatientDto::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getPatientsByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Fetching patients created between {} and {}", startDate, endDate);
        return patientRepository.findByCreatedAtBetween(startDate, endDate)
                .stream()
                .map(PatientDto::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getPatientsByBirthYear(int year) {
        logger.debug("Fetching patients born in year: {}", year);
        return patientRepository.findByBirthYear(year)
                .stream()
                .map(PatientDto::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getPatientsByBirthDateRange(LocalDate startDate, LocalDate endDate) {
        logger.debug("Fetching patients born between {} and {}", startDate, endDate);
        return patientRepository.findByDateOfBirthBetween(startDate, endDate)
                .stream()
                .map(PatientDto::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PatientStatistics getPatientStatistics() {
        logger.debug("Fetching patient statistics");
        
        long totalPatients = patientRepository.count();
        long malePatients = patientRepository.countByGender(Patient.Gender.MALE);
        long femalePatients = patientRepository.countByGender(Patient.Gender.FEMALE);
        long otherGenderPatients = patientRepository.countByGender(Patient.Gender.OTHER);
        
        // Count patients with insurance (non-null insurance provider)
        long patientsWithInsurance = patientRepository.findAll()
                .stream()
                .filter(p -> p.getInsuranceProvider() != null && !p.getInsuranceProvider().trim().isEmpty())
                .count();
        
        // Count patients with allergies (non-null allergies)
        long patientsWithAllergies = patientRepository.findAll()
                .stream()
                .filter(p -> p.getAllergies() != null && !p.getAllergies().trim().isEmpty())
                .count();
        
        return new PatientStatistics(totalPatients, malePatients, femalePatients, 
                                   otherGenderPatients, patientsWithInsurance, patientsWithAllergies);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserId(Long userId) {
        return patientRepository.existsByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByPhone(String phone) {
        return patientRepository.existsByPhone(phone);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByInsuranceNumber(String insuranceNumber) {
        return patientRepository.existsByInsuranceNumber(insuranceNumber);
    }
}
