package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class AppointmentRepository {

	private final List<Appointment> appointments = new ArrayList<>();

	public void save(Appointment appointment) {
		appointments.add(appointment);
	}

	public Optional<Appointment> findByAppointmentIdAndUserEmail(Integer appointmentId, String userEmail) {
		return appointments.stream()
			.filter(a -> a.appointmentId().equals(appointmentId) && a.userEmail().equals(userEmail))
			.findFirst();
	}

	public void deleteByAppointmentIdAndUserEmail(Integer appointmentId, String userEmail) {
		appointments.removeIf(a -> a.appointmentId().equals(appointmentId) && a.userEmail().equals(userEmail));
	}

	public List<Appointment> findByUserEmail(String userEmail) {
		return appointments.stream().filter(a -> a.userEmail().equals(userEmail)).toList();
	}


}
