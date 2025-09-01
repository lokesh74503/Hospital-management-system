// Hospital Management System MongoDB Collections Setup
// MongoDB 7.0

// Switch to the audit database
db = db.getSiblingDB('hms_audit');

// Create collections with proper indexes

// Medical Records Collection (unstructured medical data)
db.createCollection("medical_records", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["patientId", "doctorId", "recordDate"],
            properties: {
                patientId: { bsonType: "long" },
                doctorId: { bsonType: "long" },
                appointmentId: { bsonType: "long" },
                diagnosis: { bsonType: "string" },
                symptoms: { bsonType: "array" },
                treatmentPlan: { bsonType: "string" },
                prescription: { bsonType: "object" },
                labResults: { bsonType: "array" },
                images: { bsonType: "array" },
                recordDate: { bsonType: "date" },
                createdAt: { bsonType: "date" },
                updatedAt: { bsonType: "date" }
            }
        }
    }
});

// Create indexes for medical_records
db.medical_records.createIndex({ "patientId": 1 });
db.medical_records.createIndex({ "doctorId": 1 });
db.medical_records.createIndex({ "appointmentId": 1 });
db.medical_records.createIndex({ "recordDate": -1 });
db.medical_records.createIndex({ "patientId": 1, "recordDate": -1 });

// Prescriptions Collection
db.createCollection("prescriptions", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["patientId", "doctorId", "prescriptionDate"],
            properties: {
                patientId: { bsonType: "long" },
                doctorId: { bsonType: "long" },
                appointmentId: { bsonType: "long" },
                prescriptionDate: { bsonType: "date" },
                medications: { bsonType: "array" },
                dosage: { bsonType: "object" },
                frequency: { bsonType: "string" },
                duration: { bsonType: "string" },
                instructions: { bsonType: "string" },
                sideEffects: { bsonType: "array" },
                isActive: { bsonType: "bool" },
                createdAt: { bsonType: "date" },
                updatedAt: { bsonType: "date" }
            }
        }
    }
});

// Create indexes for prescriptions
db.prescriptions.createIndex({ "patientId": 1 });
db.prescriptions.createIndex({ "doctorId": 1 });
db.prescriptions.createIndex({ "prescriptionDate": -1 });
db.prescriptions.createIndex({ "isActive": 1 });

// Audit Logs Collection (detailed audit trail)
db.createCollection("audit_logs", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["action", "entityType", "timestamp"],
            properties: {
                userId: { bsonType: "long" },
                action: { bsonType: "string" },
                entityType: { bsonType: "string" },
                entityId: { bsonType: "long" },
                oldValues: { bsonType: "object" },
                newValues: { bsonType: "object" },
                ipAddress: { bsonType: "string" },
                userAgent: { bsonType: "string" },
                sessionId: { bsonType: "string" },
                timestamp: { bsonType: "date" },
                metadata: { bsonType: "object" }
            }
        }
    }
});

// Create indexes for audit_logs
db.audit_logs.createIndex({ "userId": 1 });
db.audit_logs.createIndex({ "action": 1 });
db.audit_logs.createIndex({ "entityType": 1, "entityId": 1 });
db.audit_logs.createIndex({ "timestamp": -1 });
db.audit_logs.createIndex({ "userId": 1, "timestamp": -1 });

// System Logs Collection (application logs)
db.createCollection("system_logs", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["level", "message", "timestamp"],
            properties: {
                level: { 
                    enum: ["ERROR", "WARN", "INFO", "DEBUG", "TRACE"] 
                },
                message: { bsonType: "string" },
                service: { bsonType: "string" },
                className: { bsonType: "string" },
                methodName: { bsonType: "string" },
                lineNumber: { bsonType: "int" },
                stackTrace: { bsonType: "string" },
                userId: { bsonType: "long" },
                requestId: { bsonType: "string" },
                timestamp: { bsonType: "date" },
                metadata: { bsonType: "object" }
            }
        }
    }
});

// Create indexes for system_logs
db.system_logs.createIndex({ "level": 1 });
db.system_logs.createIndex({ "service": 1 });
db.system_logs.createIndex({ "timestamp": -1 });
db.system_logs.createIndex({ "userId": 1 });
db.system_logs.createIndex({ "level": 1, "timestamp": -1 });

// Notifications Collection (detailed notification data)
db.createCollection("notifications", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["userId", "type", "title", "message"],
            properties: {
                userId: { bsonType: "long" },
                type: { 
                    enum: ["APPOINTMENT", "BILL", "REMINDER", "SYSTEM", "EMERGENCY"] 
                },
                title: { bsonType: "string" },
                message: { bsonType: "string" },
                priority: { 
                    enum: ["LOW", "MEDIUM", "HIGH", "URGENT"] 
                },
                isRead: { bsonType: "bool" },
                isSent: { bsonType: "bool" },
                sentVia: { 
                    enum: ["EMAIL", "SMS", "PUSH", "IN_APP"] 
                },
                scheduledAt: { bsonType: "date" },
                sentAt: { bsonType: "date" },
                readAt: { bsonType: "date" },
                metadata: { bsonType: "object" },
                createdAt: { bsonType: "date" }
            }
        }
    }
});

// Create indexes for notifications
db.notifications.createIndex({ "userId": 1 });
db.notifications.createIndex({ "type": 1 });
db.notifications.createIndex({ "isRead": 1 });
db.notifications.createIndex({ "isSent": 1 });
db.notifications.createIndex({ "userId": 1, "isRead": 1 });
db.notifications.createIndex({ "scheduledAt": 1 });

// Patient Documents Collection (medical documents, reports, etc.)
db.createCollection("patient_documents", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["patientId", "documentType", "fileName"],
            properties: {
                patientId: { bsonType: "long" },
                documentType: { 
                    enum: ["LAB_REPORT", "XRAY", "MRI", "PRESCRIPTION", "MEDICAL_CERTIFICATE", "INSURANCE", "OTHER"] 
                },
                fileName: { bsonType: "string" },
                originalFileName: { bsonType: "string" },
                fileSize: { bsonType: "long" },
                mimeType: { bsonType: "string" },
                filePath: { bsonType: "string" },
                uploadedBy: { bsonType: "long" },
                description: { bsonType: "string" },
                tags: { bsonType: "array" },
                isActive: { bsonType: "bool" },
                uploadedAt: { bsonType: "date" },
                createdAt: { bsonType: "date" }
            }
        }
    }
});

// Create indexes for patient_documents
db.patient_documents.createIndex({ "patientId": 1 });
db.patient_documents.createIndex({ "documentType": 1 });
db.patient_documents.createIndex({ "uploadedBy": 1 });
db.patient_documents.createIndex({ "isActive": 1 });
db.patient_documents.createIndex({ "patientId": 1, "documentType": 1 });

// Performance Metrics Collection
db.createCollection("performance_metrics", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["service", "metric", "value", "timestamp"],
            properties: {
                service: { bsonType: "string" },
                metric: { bsonType: "string" },
                value: { bsonType: "double" },
                unit: { bsonType: "string" },
                tags: { bsonType: "object" },
                timestamp: { bsonType: "date" }
            }
        }
    }
});

// Create indexes for performance_metrics
db.performance_metrics.createIndex({ "service": 1 });
db.performance_metrics.createIndex({ "metric": 1 });
db.performance_metrics.createIndex({ "timestamp": -1 });
db.performance_metrics.createIndex({ "service": 1, "metric": 1, "timestamp": -1 });

// Insert sample data for testing

// Sample medical record
db.medical_records.insertOne({
    patientId: 1,
    doctorId: 1,
    appointmentId: 1,
    diagnosis: "Hypertension",
    symptoms: ["High blood pressure", "Headache", "Dizziness"],
    treatmentPlan: "Lifestyle modifications and medication",
    prescription: {
        medications: ["Amlodipine", "Lisinopril"],
        dosage: "5mg daily",
        frequency: "Once daily",
        duration: "30 days"
    },
    labResults: [
        {
            testName: "Blood Pressure",
            value: "140/90",
            unit: "mmHg",
            normalRange: "120/80"
        }
    ],
    recordDate: new Date(),
    createdAt: new Date(),
    updatedAt: new Date()
});

// Sample prescription
db.prescriptions.insertOne({
    patientId: 1,
    doctorId: 1,
    appointmentId: 1,
    prescriptionDate: new Date(),
    medications: [
        {
            name: "Amlodipine",
            dosage: "5mg",
            frequency: "Once daily",
            duration: "30 days",
            instructions: "Take with food"
        }
    ],
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
});

// Sample audit log
db.audit_logs.insertOne({
    userId: 1,
    action: "CREATE",
    entityType: "PATIENT",
    entityId: 1,
    oldValues: null,
    newValues: {
        firstName: "Jane",
        lastName: "Doe",
        email: "jane.doe@example.com"
    },
    ipAddress: "192.168.1.1",
    userAgent: "Mozilla/5.0...",
    timestamp: new Date()
});

// Sample notification
db.notifications.insertOne({
    userId: 1,
    type: "APPOINTMENT",
    title: "Appointment Confirmation",
    message: "Your appointment with Dr. Smith has been confirmed for tomorrow at 10:00 AM",
    priority: "MEDIUM",
    isRead: false,
    isSent: true,
    sentVia: "EMAIL",
    sentAt: new Date(),
    createdAt: new Date()
});

print("MongoDB collections and indexes created successfully!");
print("Sample data inserted for testing."); 