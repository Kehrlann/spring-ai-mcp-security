package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentSlotRepository extends ListCrudRepository<AppointmentSlot, Integer> {

	@Query("SELECT * FROM appointment_slot ORDER BY date_time ASC")
	List<AppointmentSlot> findAll();

}
