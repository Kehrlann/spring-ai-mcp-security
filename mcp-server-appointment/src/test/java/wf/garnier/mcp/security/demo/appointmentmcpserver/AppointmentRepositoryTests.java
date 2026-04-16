package wf.garnier.mcp.security.demo.appointmentmcpserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
class AppointmentRepositoryTests {

	@Autowired
	private AppointmentRepository repository;

	@Autowired
	private AppointmentSlotRepository slotRepository;

	@Test
	void shouldSaveAndFindAppointment() {
		AppointmentSlot slot = slotRepository.save(new AppointmentSlot(null, "Slot 1", LocalDateTime.now()));
		Appointment appointment = new Appointment(slot.id(), "user@example.com");
		repository.save(appointment);

		Optional<Appointment> found = repository.findByAppointmentIdAndUserEmail(slot.id(), "user@example.com");

		assertThat(found).isPresent();
		assertThat(found.get().userEmail()).isEqualTo(appointment.userEmail());
	}

	@Test
	void shouldNotFindNonExistentAppointment() {
		Optional<Appointment> found = repository.findByAppointmentIdAndUserEmail(99, "unknown@example.com");

		assertThat(found).isEmpty();
	}

	@Test
	void shouldDeleteAppointment() {
		AppointmentSlot slot = slotRepository.save(new AppointmentSlot(null, "Slot 1", LocalDateTime.now()));
		Appointment appointment = new Appointment(slot.id(), "user@example.com");
		repository.save(appointment);

		repository.deleteByAppointmentIdAndUserEmail(slot.id(), "user@example.com");

		Optional<Appointment> found = repository.findByAppointmentIdAndUserEmail(slot.id(), "user@example.com");
		assertThat(found).isEmpty();
	}

	@Test
	void shouldFindByUserEmail() {
		AppointmentSlot slot1 = slotRepository.save(new AppointmentSlot(null, "Slot 1", LocalDateTime.now()));
		AppointmentSlot slot2 = slotRepository.save(new AppointmentSlot(null, "Slot 2", LocalDateTime.now()));
		AppointmentSlot slot3 = slotRepository.save(new AppointmentSlot(null, "Slot 3", LocalDateTime.now()));

		repository.save(new Appointment(slot1.id(), "user1@example.com"));
		repository.save(new Appointment(slot2.id(), "user1@example.com"));
		repository.save(new Appointment(slot3.id(), "user2@example.com"));

		List<Appointment> found = repository.findByUserEmail("user1@example.com");

		assertThat(found).hasSize(2);
		assertThat(found).extracting(Appointment::appointmentId).containsExactlyInAnyOrder(slot1.id(), slot2.id());
	}

}
