package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceMockTest {

    @Mock
    EnrollmentRepository enrollmentRepository;

    @InjectMocks
    EnrollmentService enrollmentService;

    @Test
    public void testAddEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setIdEnrollment(1L);
        enrollment.setGrade(15.5);
        enrollment.setEnrollmentDate(LocalDate.now());

        Mockito.when(enrollmentRepository.save(Mockito.any(Enrollment.class))).thenReturn(enrollment);

        Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);

        Assertions.assertNotNull(savedEnrollment);
        Assertions.assertEquals(15.5, savedEnrollment.getGrade());
    }

    @Test
    public void testRetrieveEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setIdEnrollment(1L);

        Mockito.when(enrollmentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(enrollment));

        Enrollment retrievedEnrollment = enrollmentService.getEnrollmentById(1L);

        Assertions.assertNotNull(retrievedEnrollment);
        Assertions.assertEquals(1L, retrievedEnrollment.getIdEnrollment());
    }

    @Test
    public void testUpdateEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setIdEnrollment(1L);
        enrollment.setGrade(12.0);

        Mockito.when(enrollmentRepository.save(Mockito.any(Enrollment.class))).thenReturn(enrollment);

        enrollment.setGrade(18.0);
        Enrollment updatedEnrollment = enrollmentService.saveEnrollment(enrollment);

        Assertions.assertNotNull(updatedEnrollment);
        Assertions.assertEquals(18.0, updatedEnrollment.getGrade());
    }

    @Test
    public void testDeleteEnrollment() {
        Mockito.doNothing().when(enrollmentRepository).deleteById(Mockito.anyLong());

        enrollmentService.deleteEnrollment(1L);

        Mockito.verify(enrollmentRepository, Mockito.times(1)).deleteById(1L);
    }
}