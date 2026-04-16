package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class AppointmentSlotRepository {

	private final List<AppointmentSlot> slots = new ArrayList<>();

	private final AtomicInteger nextId = new AtomicInteger(1);

	public void create(AppointmentSlot slot) {
		Assert.isNull(slot.id(), "slot ID must be null");
		slots.add(new AppointmentSlot(nextId.getAndIncrement(), slot.name(), slot.dateTime()));
	}

	public List<AppointmentSlot> findAll() {
		return slots.stream()
			.sorted(Comparator.comparing(AppointmentSlot::dateTime))
			.toList();
	}

}
