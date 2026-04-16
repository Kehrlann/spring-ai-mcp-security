package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.LocalDateTime;

public record AppointmentSlot(Integer id, String name, LocalDateTime dateTime) {

}
