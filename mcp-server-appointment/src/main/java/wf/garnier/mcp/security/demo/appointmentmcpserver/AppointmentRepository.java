package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends ListCrudRepository<Appointment, Integer> {

	Optional<Appointment> findByAppointmentIdAndUserEmail(Integer appointmentId, String userEmail);

	void deleteByAppointmentIdAndUserEmail(Integer appointmentId, String userEmail);

	List<Appointment> findByUserEmail(String userEmail);

}
