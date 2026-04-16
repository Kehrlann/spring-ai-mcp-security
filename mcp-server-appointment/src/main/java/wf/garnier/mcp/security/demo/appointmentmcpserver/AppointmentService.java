package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;

	private final AppointmentSlotRepository slotRepository;

	public AppointmentService(AppointmentRepository appointmentRepository, AppointmentSlotRepository slotRepository) {
		this.appointmentRepository = appointmentRepository;
		this.slotRepository = slotRepository;
	}

	public void bookAppointment(Integer appointmentId, String userEmail) {
		if (appointmentRepository.findByAppointmentIdAndUserEmail(appointmentId, userEmail).isEmpty()) {
			appointmentRepository.save(new Appointment(appointmentId, userEmail));
		}
	}

	public void unbookAppointment(Integer appointmentId, String userEmail) {
		appointmentRepository.deleteByAppointmentIdAndUserEmail(appointmentId, userEmail);
	}

	public boolean isBooked(Integer appointmentId, String userEmail) {
		return appointmentRepository.findByAppointmentIdAndUserEmail(appointmentId, userEmail).isPresent();
	}

	public List<AppointmentSlot> findSlotsByUserEmailAndDateRange(String userEmail, LocalDate startDate,
			LocalDate endDate) {
		List<Appointment> appointments = appointmentRepository.findByUserEmail(userEmail);
		List<AppointmentSlot> allSlots = slotRepository.findAll();

		return appointments.stream()
			.map(appointment -> allSlots.stream()
				.filter(s -> s.id().equals(appointment.appointmentId()))
				.findFirst()
				.orElse(null))
			.filter(Objects::nonNull)
			.filter(slot -> slotIsWithinDateRange(startDate, endDate, slot))
			.toList();
	}

	public List<AppointmentSlot> findSlotsByDateRange(LocalDate startDate, LocalDate endDate) {
		List<AppointmentSlot> allSlots = slotRepository.findAll();

		if (startDate == null && endDate == null) {
			return allSlots;
		}

		return allSlots.stream().filter(slot -> slotIsWithinDateRange(startDate, endDate, slot)).toList();
	}

	public Optional<AppointmentSlot> findSlotByNameAndDateTime(String name, LocalDateTime dateTime) {
		return slotRepository.findAll().stream()
			.filter(slot -> slot.name().equalsIgnoreCase(name) && slot.dateTime().equals(dateTime))
			.findFirst();
	}

	private static boolean slotIsWithinDateRange(LocalDate startDate, LocalDate endDate, AppointmentSlot slot) {
		LocalDate appointmentDate = slot.dateTime().toLocalDate();
		var isBeforeStartDate = startDate != null && appointmentDate.isBefore(startDate);
		var isAfterEndDate = endDate != null && appointmentDate.isAfter(endDate);

		return !(isBeforeStartDate || isAfterEndDate);
	}

}
