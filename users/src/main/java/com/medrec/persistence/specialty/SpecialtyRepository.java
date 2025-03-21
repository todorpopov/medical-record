package com.medrec.persistence.specialty;

import com.medrec.persistence.DBUtils;
import com.medrec.persistence.ICrudRepository;
import com.medrec.persistence.ResponseMessage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SpecialtyRepository implements ICrudRepository<Specialty> {
    private static SpecialtyRepository instance;

    private final Logger logger = Logger.getLogger(SpecialtyRepository.class.getName());

    private SpecialtyRepository() {}

    public static SpecialtyRepository getInstance() {
        if (instance == null) {
            instance = new SpecialtyRepository();
        }

        return instance;
    }

    @Override
    public ResponseMessage save(Specialty specialty) {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(specialty);
            tx.commit();

            this.logger.info(String.format("Specialty %s saved successfully", specialty.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s created successfully!", specialty.getSpecialtyName())
            );
        } catch (Exception e) {
            this.logger.severe(String.format("Specialty %s save failed", specialty.toString()));
            return new ResponseMessage(false, e.getMessage());
        }
    }

    public int saveGetId(Specialty specialty) {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(specialty);
            tx.commit();

            this.logger.info(String.format("Specialty %s saved successfully", specialty.toString()));
            return specialty.getId();
        } catch (Exception e) {
            this.logger.severe(String.format("Specialty %s save failed", specialty.toString()));
            return -1;
        }
    }

    @Override
    public Specialty findById(int id) {
        Specialty specialty = null;
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            specialty = session.get(Specialty.class, id);
            tx.commit();

            this.logger.info(String.format("Specialty %s found", specialty.toString()));
        } catch (Exception e) {
            this.logger.severe(String.format("Specialty %s findById failed", id));
        }
        return specialty;
    }

    @Override
    public List<Specialty> findAll() {
        List<Specialty> specialtys = new ArrayList<>();
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            specialtys = session.createQuery("from Specialty", Specialty.class).getResultList();
            tx.commit();
        } catch (Exception e) {
            this.logger.severe(String.format("Specialtys findAll failed: %s", e.getMessage()));
        }

        return specialtys;
    }
    @Override
    public ResponseMessage update(Specialty specialty) {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(specialty);
            tx.commit();

            this.logger.info(String.format("Specialty %s updated successfully", specialty.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s updated successfully!", specialty.getSpecialtyName())
            );
        } catch (Exception e) {
            this.logger.severe(String.format("Specialty %s update failed", specialty.toString()));
            return new ResponseMessage(false, e.getMessage());
        }
    }

    @Override
    public ResponseMessage delete(int id) {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            Specialty specialty = session.get(Specialty.class, id);
            session.remove(specialty);
            tx.commit();

            this.logger.info(String.format("Specialty %s deleted successfully", specialty.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s deleted successfully!", specialty.getSpecialtyName())
            );
        } catch (Exception e) {
            this.logger.severe(String.format("Specialty %s delete failed", id));
            return new ResponseMessage(false, e.getMessage());
        }
    }
}
