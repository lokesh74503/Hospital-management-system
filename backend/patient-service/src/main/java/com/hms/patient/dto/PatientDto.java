package com.hms.patient.dto;

import com.hms.patient.model.Patient;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientDto {
    
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;
    
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;
    
    private Patient.Gender gender;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phone;
    
    private String address;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid emergency contact format")
    @Size(max = 20, message = "Emergency contact must be less than 20 characters")
    private String emergencyContact;
    
    @Size(max = 5, message = "Blood group must be less than 5 characters")
    private String bloodGroup;
    
    private String allergies;
    
    private String medicalHistory;
    
    @Size(max = 100, message = "Insurance provider must be less than 100 characters")
    private String insuranceProvider;
    
    @Size(max = 50, message = "Insurance number must be less than 50 characters")
    private String insuranceNumber;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Default constructor
    public PatientDto() {}
    
    // Constructor from Patient entity
    public PatientDto(Patient patient) {
        this.id = patient.getId();
        this.userId = patient.getUserId();
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.dateOfBirth = patient.getDateOfBirth();
        this.gender = patient.getGender();
        this.phone = patient.getPhone();
        this.address = patient.getAddress();
        this.emergencyContact = patient.getEmergencyContact();
        this.bloodGroup = patient.getBloodGroup();
        this.allergies = patient.getAllergies();
        this.medicalHistory = patient.getMedicalHistory();
        this.insuranceProvider = patient.getInsuranceProvider();
        this.insuranceNumber = patient.getInsuranceNumber();
        this.createdAt = patient.getCreatedAt();
        this.updatedAt = patient.getUpdatedAt();
    }
    
    // Convert to Patient entity
    public Patient toEntity() {
        Patient patient = new Patient();
        patient.setId(this.id);
        patient.setUserId(this.userId);
        patient.setFirstName(this.firstName);
        patient.setLastName(this.lastName);
        patient.setDateOfBirth(this.dateOfBirth);
        patient.setGender(this.gender);
        patient.setPhone(this.phone);
        patient.setAddress(this.address);
        patient.setEmergencyContact(this.emergencyContact);
        patient.setBloodGroup(this.bloodGroup);
        patient.setAllergies(this.allergies);
        patient.setMedicalHistory(this.medicalHistory);
        patient.setInsuranceProvider(this.insuranceProvider);
        patient.setInsuranceNumber(this.insuranceNumber);
        return patient;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public Patient.Gender getGender() {
        return gender;
    }
    
    public void setGender(Patient.Gender gender) {
        this.gender = gender;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    
    public String getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    
    public String getAllergies() {
        return allergies;
    }
    
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
    
    public String getMedicalHistory() {
        return medicalHistory;
    }
    
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
    
    public String getInsuranceProvider() {
        return insuranceProvider;
    }
    
    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }
    
    public String getInsuranceNumber() {
        return insuranceNumber;
    }
    
    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "PatientDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", emergencyContact='" + emergencyContact + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", allergies='" + allergies + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                ", insuranceProvider='" + insuranceProvider + '\'' +
                ", insuranceNumber='" + insuranceNumber + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
