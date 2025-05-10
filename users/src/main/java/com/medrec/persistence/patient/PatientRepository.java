package com.medrec.persistence.patient;

import com.medrec.dtos.CreatePatientDTO;
import com.medrec.exception_handling.exceptions.*;
import com.medrec.grpc.users.Users;
import com.medrec.persistence.DBUtils;
import com.medrec.persistence.ICrudRepository;
import com.medrec.persistence.doctor.Doctor;
import com.medrec.persistence.doctor.DoctorRepository;
import jakarta.persistence.EntityExistsException;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class PatientRepository implements ICrudRepository<Patient, CreatePatientDTO, Users.UpdatePatientRequest> {
    private static PatientRepository instance;

    private final DoctorRepository doctorRepository = DoctorRepository.getInstance();

    private final Logger logger = Logger.getLogger(PatientRepository.class.getName());

    private PatientRepository() {}

    public static PatientRepository getInstance() {
        if (instance == null) {
            instance = new PatientRepository();
        }

        return instance;
    }

    @Override
    public Patient save(CreatePatientDTO dto) throws RuntimeException {
        try {
            Doctor gp = doctorRepository.findById(dto.getGpId());

            if (!gp.isGp()) {
                throw new InvalidPropertyException("doctor_not_gp");
            }

            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Patient patient = dto.getDomainModel(gp);
            session.persist(patient);
            tx.commit();

            this.logger.info(String.format("Patient %s saved successfully", patient.toString()));
            return patient;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (NotFoundException e) {
            this.logger.severe("General Practitioner not found");
            throw e;
        } catch (InvalidPropertyException e) {
            this.logger.severe("Doctor not GP");
            throw e;
        } catch (ConstraintViolationException e){
            this.logger.severe("Constraint violation: " + e.getMessage());
            throw new ConstrainException("Constraint exception");
        } catch (EntityExistsException e) {
            this.logger.severe("Entity already exists: " + e.getMessage());
            throw new AlreadyExistsException("Entity already exists");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public Patient findById(int id) throws RuntimeException {
        try {
            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Patient patient = session.get(Patient.class, id);
            tx.commit();

            if (patient == null) {
                throw new NotFoundException("patient_not_found");
            }

            this.logger.info(String.format("Patient %s found", patient.toString()));
            return patient;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (NotFoundException e) {
            this.logger.severe("Patient with id + " + id + " not found: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public Patient findByEmail(String email) throws RuntimeException {
        try {
            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            String hql = "from Doctor where email = :email";
            Query query = session.createQuery("from Patient where email = :email").setParameter("email", email);
            Patient patient = (Patient) query.uniqueResult();
            tx.commit();

            if (patient == null) {
                throw new NotFoundException("patient_not_found");
            }

            this.logger.info(String.format("Doctor %s found", patient.toString()));
            return patient;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (NotFoundException e) {
            this.logger.severe("Patient with email + " + email + " not found: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public List<Patient> findAll() throws RuntimeException {
        try {
            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            List<Patient> patients = session.createQuery("from Patient", Patient.class).getResultList();
            tx.commit();
            return patients;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public Patient update(Users.UpdatePatientRequest request) throws RuntimeException {
        int id = request.getPatientId();
        Optional<Patient> patientOptional = Optional.ofNullable(findById(id));

        try {
            if (patientOptional.isEmpty()) {
                throw new NotFoundException("patient_not_found");
            }

            Patient patient = patientOptional.get();

            if (request.hasFirstName()) {
                patient.setFirstName(request.getFirstName());
            }

            if (request.hasLastName()) {
                patient.setLastName(request.getLastName());
            }

            if (request.hasPassword()) {
                patient.setPassword(request.getPassword());
            }

            if (request.hasGpId()) {
                Optional<Doctor> optionalDoctor = Optional.ofNullable(doctorRepository.findById(request.getGpId()));
                if (optionalDoctor.isEmpty()) {
                    throw new NotFoundException("doctor_not_found");
                }
                Doctor newDoctor = optionalDoctor.get();
                if (!newDoctor.isGp()) {
                    throw new DoctorIsNotGpException("Doctor is not GP");
                }
                patient.setDoctor(optionalDoctor.get());
            }

            if (request.hasIsHealthInsured()) {
                patient.setHealthInsured(request.getIsHealthInsured());
            }

            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Patient updatedPatient = session.merge(patient);
            tx.commit();

            this.logger.info("Patient updated successfully");
            return updatedPatient;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (NotFoundException e) {
            this.logger.severe("Not found: " + e.getMessage());
            throw e;
        } catch (DoctorIsNotGpException e) {
            this.logger.severe("Doctor is not GP: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public void delete(int id) throws RuntimeException {
        try {
            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Patient patient = session.get(Patient.class, id);

            if (patient == null) {
                throw new NotFoundException("patient_not_found");
            }

            session.remove(patient);
            tx.commit();

            this.logger.info(String.format("Patient with id %s deleted successfully", id));
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (NotFoundException e) {
            this.logger.severe(String.format("Patient with id + %s not found", id));
            throw e;
        } catch (ConstraintViolationException e) {
            this.logger.severe("Constraint exception: " + e.getMessage());
            throw new ConstrainException("Constraint exception");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }
}
