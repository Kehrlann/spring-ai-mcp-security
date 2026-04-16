package wf.garnier.mcp.security.demo.appointmentmcpserver;

import org.springframework.data.annotation.Id;

public record Appointment(@Id Integer id, Integer appointmentId, String userEmail) {

	public Appointment(Integer appointmentId, String userEmail) {
		this(null, appointmentId, userEmail);
	}

}
