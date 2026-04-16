package wf.garnier.mcp.security.demo.appointmentmcpserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppointmentServiceTests {

	private AppointmentRepository appointmentRepository;

	private AppointmentSlotRepository slotRepository;

	private AppointmentService appointmentService;

	@BeforeEach
	void setUp() {
		appointmentRepository = mock(AppointmentRepository.class);
		slotRepository = mock(AppointmentSlotRepository.class);
		appointmentService = new AppointmentService(appointmentRepository, slotRepository);
	}

	@Test
	void shouldBookAppointmentWhenNotAlreadyBooked() {
		when(appointmentRepository.findByAppointmentIdAndUserEmail(1, "user@example.com")).thenReturn(Optional.empty());

		appointmentService.bookAppointment(1, "user@example.com");

		verify(appointmentRepository).save(new Appointment(1, "user@example.com"));
	}

	@Test
	void shouldNotBookAppointmentWhenAlreadyBooked() {
		when(appointmentRepository.findByAppointmentIdAndUserEmail(1, "user@example.com"))
			.thenReturn(Optional.of(new Appointment(1, "user@example.com")));

		appointmentService.bookAppointment(1, "user@example.com");

		verify(appointmentRepository, never()).save(any());
	}

	@Test
	void shouldUnbookAppointment() {
		appointmentService.unbookAppointment(1, "user@example.com");

		verify(appointmentRepository).deleteByAppointmentIdAndUserEmail(1, "user@example.com");
	}

	@Test
	void shouldReturnIsBookedTrue() {
		when(appointmentRepository.findByAppointmentIdAndUserEmail(1, "user@example.com"))
			.thenReturn(Optional.of(new Appointment(1, "user@example.com")));

		boolean result = appointmentService.isBooked(1, "user@example.com");

		assertThat(result).isTrue();
	}

	@Test
	void shouldReturnIsBookedFalse() {
		when(appointmentRepository.findByAppointmentIdAndUserEmail(1, "user@example.com")).thenReturn(Optional.empty());

		boolean result = appointmentService.isBooked(1, "user@example.com");

		assertThat(result).isFalse();
	}

	@Test
	void shouldFindSlotsByUserEmailAndDateRange() {
		String email = "user@example.com";
		LocalDate today = LocalDate.now();
		LocalDateTime now = LocalDateTime.now();

		when(appointmentRepository.findByUserEmail(email))
			.thenReturn(List.of(new Appointment(1, email), new Appointment(2, email)));

		AppointmentSlot slot1 = new AppointmentSlot(1, "Slot 1", now);
		AppointmentSlot slot2 = new AppointmentSlot(2, "Slot 2", now.plusDays(5));
		AppointmentSlot slot3 = new AppointmentSlot(3, "Slot 3", now.plusDays(10));

		when(slotRepository.findAll()).thenReturn(List.of(slot1, slot2, slot3));

		List<AppointmentSlot> result = appointmentService.findSlotsByUserEmailAndDateRange(email, today.minusDays(1),
				today.plusDays(2));

		assertThat(result).hasSize(1);
		assertThat(result.get(0).id()).isEqualTo(1);
	}

	@Test
	void shouldFindSlotsByDateRange() {
		LocalDate today = LocalDate.now();
		LocalDateTime now = LocalDateTime.now();

		AppointmentSlot slot1 = new AppointmentSlot(1, "Slot 1", now);
		AppointmentSlot slot2 = new AppointmentSlot(2, "Slot 2", now.plusDays(5));

		when(slotRepository.findAll()).thenReturn(List.of(slot1, slot2));

		List<AppointmentSlot> result = appointmentService.findSlotsByDateRange(today.minusDays(1), today.plusDays(2));

		assertThat(result).hasSize(1);
		assertThat(result.get(0).id()).isEqualTo(1);
	}

	@Test
	void shouldFindSlotsByDateRangeWithNullDates() {
		AppointmentSlot slot1 = new AppointmentSlot(1, "Slot 1", LocalDateTime.now());

		when(slotRepository.findAll()).thenReturn(List.of(slot1));

		List<AppointmentSlot> result = appointmentService.findSlotsByDateRange(null, null);

		assertThat(result).hasSize(1);
		assertThat(result.get(0).id()).isEqualTo(1);
	}

	@Test
	void shouldFindSlotByNameAndDateTime() {
		LocalDateTime now = LocalDateTime.now();
		AppointmentSlot slot1 = new AppointmentSlot(1, "Slot 1", now);
		AppointmentSlot slot2 = new AppointmentSlot(2, "Slot 2", now);

		when(slotRepository.findAll()).thenReturn(List.of(slot1, slot2));

		Optional<AppointmentSlot> result = appointmentService.findSlotByNameAndDateTime("Slot 1", now);

		assertThat(result).isPresent();
		assertThat(result.get().id()).isEqualTo(1);
	}

}
