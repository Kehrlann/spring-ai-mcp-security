package wf.garnier.mcp.security.demo.appointmentmcpserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
class AppointmentSlotRepositoryTests {

	@Autowired
	private AppointmentSlotRepository repository;

	@Test
	void shouldCreateAndFindAllSlots() {
		LocalDateTime now = LocalDateTime.now();
		AppointmentSlot slot1 = new AppointmentSlot(null, "Slot 1", now.plusDays(1));
		AppointmentSlot slot2 = new AppointmentSlot(null, "Slot 2", now);

		repository.save(slot1);
		repository.save(slot2);

		List<AppointmentSlot> slots = repository.findAll();

		assertThat(slots).hasSize(2);
		// Should be sorted by date
		assertThat(slots.get(0).name()).isEqualTo("Slot 2");
		assertThat(slots.get(1).name()).isEqualTo("Slot 1");

		// IDs should be assigned
		assertThat(slots.get(0).id()).isNotNull();
		assertThat(slots.get(1).id()).isNotNull();
	}

}
