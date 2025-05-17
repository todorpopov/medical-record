package com.medrec.persistence.appointment;

import com.medrec.exception_handling.exceptions.BadRequestException;
import com.medrec.exception_handling.exceptions.DatabaseConnectionException;
import com.medrec.exception_handling.exceptions.DatabaseException;
import com.medrec.exception_handling.exceptions.NotFoundException;
import com.medrec.gateway.UsersGateway;
import com.medrec.persistence.DBUtils;
import io.grpc.StatusRuntimeException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

public class AppointmentsRepository {
    private static AppointmentsRepository instance;

    private final UsersGateway usersGateway = UsersGateway.getInstance();

    private final Logger logger = Logger.getLogger(AppointmentsRepository.class.getName());

    private AppointmentsRepository() {}

    public static AppointmentsRepository getInstance() {
        if (instance == null) {
            instance = new AppointmentsRepository();
        }
        return instance;
    }

    private boolean isDoctorFree(int doctorId, LocalDateTime dateTime, Session session, Transaction tx) {
        String hql = "SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.dateTime = :dateTime";
        Appointment appointment = (Appointment) session.createQuery(hql)
            .setParameter("doctorId", doctorId)
            .setParameter("dateTime", dateTime)
            .uniqueResult();

        return appointment == null;
    }

    public Appointment save(String date, String time, int doctorId, int patientId) throws RuntimeException {
        logger.info("Saving new appointment");
        String dateTime = date + "T" + time;

        if (doctorId < 1 || patientId < 1) {
            logger.severe("Doctor or patient id is invalid");
            throw new BadRequestException("invalid_id");
        }

        try {
            this.usersGateway.patientExists(patientId);
            this.usersGateway.doctorExists(doctorId);
        } catch (StatusRuntimeException e) {
            logger.severe("Users service returned exception: " + e.getMessage());
            throw e;
        }

        Transaction tx = null;
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            Session session = DBUtils.getCurrentSession();
            tx = session.beginTransaction();

            if(!isDoctorFree(doctorId, localDateTime, session, tx)) {
                this.logger.warning("Doctor is not free at the specified time");
                throw new BadRequestException("doctor_not_free");
            }

            Appointment appointment = new Appointment(
                localDateTime,
                patientId,
                doctorId,
                "upcoming"
            );

            session.persist(appointment);
            tx.commit();

            logger.info("Appointment saved successfully");
            return appointment;
        } catch (ExceptionInInitializerError e) {
            DBUtils.rollback(tx);
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (BadRequestException e) {
            logger.severe("Bad request exception found: " + e.getMessage());
            throw e;
        } catch (DateTimeParseException e) {
            DBUtils.rollback(tx);
            logger.severe("Could not parse date and time: " + e.getMessage());
            throw new BadRequestException("date_time_not_parsed");
        } catch (HibernateException e) {
            DBUtils.rollback(tx);
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public Appointment getById(int id) throws RuntimeException {
        logger.info("Getting appointment with id " + id);
        if (id < 1) {
            logger.severe("Appointment id is invalid");
            throw new BadRequestException("invalid_id");
        }

        Transaction tx = null;
        try {
            Session session = DBUtils.getCurrentSession();
            tx = session.beginTransaction();
            Appointment appointment = session.get(Appointment.class, id);
            if (appointment == null) {
                this.logger.severe("Could not find appointment with id " + id);
                throw new NotFoundException("appointment_not_found");
            }
            tx.commit();

            logger.info("Appointment found successfully");
            return appointment;
        } catch (ExceptionInInitializerError e) {
            DBUtils.rollback(tx);
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (BadRequestException e) {
            logger.severe("Bad request exception found: " + e.getMessage());
            throw e;
        } catch (NotFoundException e) {
            DBUtils.rollback(tx);
            logger.severe("Not found exception found: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            DBUtils.rollback(tx);
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public List<Appointment> findAllByPatientEmail(String email) throws RuntimeException {
        logger.info("Getting appointments for patient with email " + email);

        Transaction tx = null;
        try {
            int patientId = this.usersGateway.getPatientIdByEmail(email);

            Session session = DBUtils.getCurrentSession();
            tx = session.beginTransaction();
            String hql = "SELECT a FROM Appointment a WHERE a.patientId=:patientId";
            List<Appointment> appointments = session.createQuery(hql, Appointment.class)
                .setParameter("patientId", patientId)
                .getResultList();
            tx.commit();
            return appointments;
        } catch (ExceptionInInitializerError e) {
            DBUtils.rollback(tx);
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (StatusRuntimeException e) {
            logger.severe("Users service returned exception: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            DBUtils.rollback(tx);
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public List<Appointment> findAllByDoctorEmail(String email) throws RuntimeException {
        logger.info("Getting appointments for doctor with email " + email);

        Transaction tx = null;
        try {
            int doctorId = this.usersGateway.getDoctorIdByEmail(email);

            Session session = DBUtils.getCurrentSession();
            tx = session.beginTransaction();
            String hql = "SELECT a FROM Appointment a WHERE a.doctorId=:doctorId";
            List<Appointment> appointments = session.createQuery(hql, Appointment.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
            tx.commit();
            return appointments;
        } catch (ExceptionInInitializerError e) {
            DBUtils.rollback(tx);
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (StatusRuntimeException e) {
            logger.severe("Users service returned exception: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            DBUtils.rollback(tx);
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public List<Appointment> findAll() throws RuntimeException {
        logger.info("Getting all appointments");

        Transaction tx = null;
        try {
            Session session = DBUtils.getCurrentSession();
            tx = session.beginTransaction();
            List<Appointment> appointments = session.createQuery("FROM Appointment", Appointment.class).getResultList();
            tx.commit();
            return appointments;
        } catch (ExceptionInInitializerError e) {
            DBUtils.rollback(tx);
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (HibernateException e) {
            DBUtils.rollback(tx);
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public Appointment update(int id, String status) throws RuntimeException {
        logger.info("Updating appointment with id " + id);

        if (id < 1) {
            logger.severe("Appointment id is invalid");
            throw new BadRequestException("invalid_id");
        }

        if (!(status.equals("upcoming") || status.equals("started") || status.equals("finished"))) {
            logger.severe("Status is invalid");
            throw new BadRequestException("invalid_status");
        }

        Transaction tx = null;
        try {
            Session session = DBUtils.getCurrentSession();
            tx = session.beginTransaction();
            Appointment appointment = session.get(Appointment.class, id);
            if (appointment == null) {
                this.logger.severe("Could not find appointment with id " + id);
                throw new NotFoundException("appointment_not_found");
            }
            appointment.setStatus(status);
            tx.commit();

            logger.info("Appointment status updated successfully");
            return appointment;
        } catch (ExceptionInInitializerError e) {
            DBUtils.rollback(tx);
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (BadRequestException e) {
            logger.severe("Bad request exception found: " + e.getMessage());
            throw e;
        } catch (NotFoundException e) {
            DBUtils.rollback(tx);
            logger.severe("Not found exception found: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            DBUtils.rollback(tx);
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public void delete(int id) throws RuntimeException {
        logger.info("Deleting appointment with id " + id);

        if (id < 1) {
            logger.severe("Appointment id is invalid");
            throw new BadRequestException("invalid_id");
        }

        Transaction tx = null;
        try {
            Session session = DBUtils.getCurrentSession();
            tx = session.beginTransaction();
            Appointment appointment = session.get(Appointment.class, id);
            if (appointment == null) {
                this.logger.severe("Could not find appointment with id " + id);
                throw new NotFoundException("appointment_not_found");
            }
            session.delete(appointment);
            tx.commit();

            this.logger.info("Appointment deleted successfully");
        } catch (ExceptionInInitializerError e) {
            DBUtils.rollback(tx);
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (BadRequestException e) {
            logger.severe("Bad request exception found: " + e.getMessage());
            throw e;
        } catch (NotFoundException e) {
            DBUtils.rollback(tx);
            logger.severe("Not found exception found: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            DBUtils.rollback(tx);
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public void genericCascadeDelete(int entityId, String type) throws RuntimeException {

        logger.info("Cascading appointments for " + type + " with id " + entityId);

        if (entityId < 1) {
            logger.severe("Id is invalid");
            throw new BadRequestException("invalid_id");
        }

        Transaction tx = null;
        try {
            Session session = DBUtils.getCurrentSession();
            tx = session.beginTransaction();

            int num = 0;
            if (type.equals("doctor")) {
                String hql = "DELETE FROM Appointment a WHERE a.doctorId=:doctorId";
                num = session.createQuery(hql, Appointment.class).setParameter("doctorId", entityId).executeUpdate();
            } else if (type.equals("patient")) {
                String hql = "DELETE FROM Appointment a WHERE a.patientId=:patientId";
                num = session.createQuery(hql, Appointment.class).setParameter("patientId", entityId).executeUpdate();
            } else {
                logger.severe("Type is invalid");
                throw new BadRequestException("invalid_entity_type");
            }

            tx.commit();
            this.logger.info(String.format("Successfully deleted %d appointments for %s with id %d", num, type, entityId));
        } catch (ExceptionInInitializerError e) {
            DBUtils.rollback(tx);
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (BadRequestException e) {
            logger.severe("Bad request exception found: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            DBUtils.rollback(tx);
            this.logger.severe("Database exception found: " + e.getMessage());
        }
    }
}
