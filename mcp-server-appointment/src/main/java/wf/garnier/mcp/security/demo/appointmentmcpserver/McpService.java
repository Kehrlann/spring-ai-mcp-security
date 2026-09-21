package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import io.modelcontextprotocol.spec.McpSchema;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

@Service
class McpService {

	private final AppointmentService appointmentService;

	McpService(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@McpTool(name = "list-slots",
			description = "List all available appointments slots between startDate and endDate, inclusive.",
			annotations = @McpTool.McpAnnotations(destructiveHint = false, readOnlyHint = true, idempotentHint = true,
					openWorldHint = false))
	public List<AppointmentSlot> listAppointments(
			@McpToolParam(description = "start date, inclusive", required = false) LocalDate startDate,
			@McpToolParam(description = "end date, inclusive", required = false) LocalDate endDate) {
		return appointmentService.findSlotsByDateRange(startDate, endDate);
	}

	@McpTool(name = "book-appointment", description = "Book a specific appointment, by name, for a given date.")
	public McpSchema.CallToolResult bookAppointment(@McpToolParam(description = "the appointment name") String name,
			@McpToolParam(description = "the date and time of the booking") LocalDateTime bookingTime,
			@McpToolParam(description = "the email of the user") String email) {
		var slot = appointmentService.findSlotByNameAndDateTime(name, bookingTime);
		if (slot.isPresent()) {
			appointmentService.bookAppointment(slot.get().id(), email);
			return McpSchema.CallToolResult.builder().addTextContent("ok").build();
		}
		return McpSchema.CallToolResult.builder().isError(true).addTextContent("No such appointment").build();
	}

	@McpTool(name = "list-appointments",
			description = "Get a list of all of a user's appointments between startDate and endDate, inclusive.")
	public McpSchema.CallToolResult listAppointmentsForAUser(
			@McpToolParam(description = "start date, inclusive", required = false) LocalDate startDate,
			@McpToolParam(description = "end date, inclusive", required = false) LocalDate endDate,
			@McpToolParam(description = "the email of the user") String email) {

		var slots = appointmentService.findSlotsByUserEmailAndDateRange(email, startDate, endDate);
		return McpSchema.CallToolResult.builder().structuredContent(Map.of("appointments", slots)).build();
	}

	@McpTool(name = "unbook-appointment", description = "Unbook a specific appointment, by name, for a given date.")
	public McpSchema.CallToolResult unbookAppointment(@McpToolParam(description = "the appointment name") String name,
			@McpToolParam(description = "the date and time of the booking") LocalDateTime bookingTime,
			@McpToolParam(description = "the email of the user") String email) {
		var slot = appointmentService.findSlotByNameAndDateTime(name, bookingTime);
		if (slot.isPresent()) {
			appointmentService.unbookAppointment(slot.get().id(), email);
			return McpSchema.CallToolResult.builder().addTextContent("ok").build();
		}
		return McpSchema.CallToolResult.builder().isError(true).addTextContent("No such appointment").build();
	}

}
