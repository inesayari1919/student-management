package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.studentmanagement.entities.Enrollment;

import java.time.LocalDate;

@SpringBootTest
public class EnrollmentServiceImplTest {

    @Autowired
    IEnrollment enrollmentService;

    @Test
    public void testAddEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setGrade(14.0);
        enrollment.setEnrollmentDate(LocalDate.now());

        Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);

        Assertions.assertNotNull(savedEnrollment);
        Assertions.assertNotNull(savedEnrollment.getIdEnrollment());
    }

    @Test
    public void testRetrieveEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setGrade(10.0);
        Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);

        Enrollment retrievedEnrollment = enrollmentService.getEnrollmentById(savedEnrollment.getIdEnrollment());

        Assertions.assertNotNull(retrievedEnrollment);
        Assertions.assertEquals(10.0, retrievedEnrollment.getGrade());
    }

    @Test
    public void testUpdateEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setGrade(11.5);
        Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);

        savedEnrollment.setGrade(16.0);
        Enrollment updatedEnrollment = enrollmentService.saveEnrollment(savedEnrollment);

        Assertions.assertNotNull(updatedEnrollment);
        Assertions.assertEquals(16.0, updatedEnrollment.getGrade());
    }

    @Test
    public void testDeleteEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setGrade(13.0);
        Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);

        enrollmentService.deleteEnrollment(savedEnrollment.getIdEnrollment());

        Enrollment deletedEnrollment = enrollmentService.getEnrollmentById(savedEnrollment.getIdEnrollment());
        Assertions.assertNull(deletedEnrollment);
    }
}