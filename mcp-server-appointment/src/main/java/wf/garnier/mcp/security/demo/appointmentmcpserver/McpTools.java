package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.modelcontextprotocol.spec.McpSchema;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

@Service
class McpTools {

	private final AppointmentService appointmentService;

	McpTools(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@McpTool(name = "list-appointments", description = "List all available appointments",
			annotations = @McpTool.McpAnnotations(destructiveHint = false, readOnlyHint = true, idempotentHint = true,
					openWorldHint = false))
	public List<AppointmentSlot> listAppointments(
			@McpToolParam(description = "start date, inclusive", required = false) LocalDate startDate,
			@McpToolParam(description = "end date, inclusive", required = false) LocalDate endDate) {
		return appointmentService.findSlotsByDateRange(startDate, endDate);
	}

	@McpTool(name = "book-appointment", description = "Book a specific appointment, by name, for a given date")
	public McpSchema.CallToolResult bookAppointment(@McpToolParam(description = "the appointment name") String name,
			@McpToolParam(description = "the date and time of the booking") LocalDateTime bookingTime,
			@McpToolParam(description = "the name of the user doing the booking") String userEmail) {
		var slot = appointmentService.findSlotByNameAndDateTime(name, bookingTime);
		if (slot.isPresent()) {
			appointmentService.bookAppointment(slot.get().id(), userEmail);
			return McpSchema.CallToolResult.builder().addTextContent("ok").build();
		}
		return McpSchema.CallToolResult.builder().isError(true).addTextContent("No such appointment").build();
	}

}
