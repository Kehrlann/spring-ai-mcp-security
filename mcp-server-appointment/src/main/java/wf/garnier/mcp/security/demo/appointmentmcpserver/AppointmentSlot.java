package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

public record AppointmentSlot(@Id Integer id, String name, LocalDateTime dateTime) {

}
