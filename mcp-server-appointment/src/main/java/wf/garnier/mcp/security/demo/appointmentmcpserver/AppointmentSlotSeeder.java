package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppointmentSlotSeeder implements ApplicationRunner {

	private final AppointmentSlotRepository repository;

	public AppointmentSlotSeeder(AppointmentSlotRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		LocalDate today = LocalDate.now();
		LocalDate startWednesday = today.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
		LocalDate startSaturday = today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
		LocalDate startSunday = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

		// Generate slots for 8 weeks (2 months)
		for (int week = 0; week < 8; week++) {
			LocalDate wednesday = startWednesday.plusWeeks(week);
			LocalDate saturday = startSaturday.plusWeeks(week);
			LocalDate sunday = startSunday.plusWeeks(week);

			repository.create(new AppointmentSlot(null, "Entraînement roller-soccer",
					LocalDateTime.of(wednesday, LocalTime.of(19, 0))));

			repository.create(new AppointmentSlot(null, "Entraînement rollball",
					LocalDateTime.of(wednesday, LocalTime.of(20, 30))));

			repository.create(new AppointmentSlot(null, "Cours roller street",
					LocalDateTime.of(saturday, LocalTime.of(14, 0))));

			repository.create(new AppointmentSlot(null, "Cours adultes: tous niveaux",
					LocalDateTime.of(sunday, LocalTime.of(14, 0))));

			repository.create(new AppointmentSlot(null, "Cours avancé",
					LocalDateTime.of(sunday, LocalTime.of(15, 30))));
		}
	}

}
