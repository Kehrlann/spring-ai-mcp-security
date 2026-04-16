package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppointmentSlotSeeder implements ApplicationRunner {

	private final AppointmentSlotRepository appointmentSlotRepository;

	private final AppointmentRepository appointmentRepository;

	public AppointmentSlotSeeder(AppointmentSlotRepository appointmentSlotRepository,
			AppointmentRepository appointmentRepository) {
		this.appointmentSlotRepository = appointmentSlotRepository;
		this.appointmentRepository = appointmentRepository;
	}

	@Transactional
	public void reset() {
		appointmentRepository.deleteAll();
		appointmentSlotRepository.deleteAll();
		seed();
	}

	private void seed() {
		LocalDate today = LocalDate.now();
		LocalDate startWednesday = today.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
		LocalDate startSaturday = today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
		LocalDate startSunday = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

		// Generate slots for 8 weeks (2 months)
		for (int week = 0; week < 8; week++) {
			LocalDate wednesday = startWednesday.plusWeeks(week);
			LocalDate saturday = startSaturday.plusWeeks(week);
			LocalDate sunday = startSunday.plusWeeks(week);

			appointmentSlotRepository.save(new AppointmentSlot(null, "Entraînement roller-soccer",
					LocalDateTime.of(wednesday, LocalTime.of(19, 0))));

			appointmentSlotRepository.save(new AppointmentSlot(null, "Entraînement rollball",
					LocalDateTime.of(wednesday, LocalTime.of(20, 30))));

			appointmentSlotRepository.save(
					new AppointmentSlot(null, "Cours roller street", LocalDateTime.of(saturday, LocalTime.of(14, 0))));

			appointmentSlotRepository.save(new AppointmentSlot(null, "Cours adultes: tous niveaux",
					LocalDateTime.of(sunday, LocalTime.of(14, 0))));

			appointmentSlotRepository
				.save(new AppointmentSlot(null, "Cours avancé", LocalDateTime.of(sunday, LocalTime.of(15, 30))));
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (this.appointmentSlotRepository.count() == 0) {
			this.seed();
		}
	}

}
