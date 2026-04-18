package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import io.modelcontextprotocol.spec.McpSchema;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
			@McpToolParam(description = "the date and time of the booking") LocalDateTime bookingTime) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof JwtAuthenticationToken jwt)) {
			return McpSchema.CallToolResult.builder()
				.isError(true)
				.addTextContent("Unexpected authentication type")
				.build();
		}
		var slot = appointmentService.findSlotByNameAndDateTime(name, bookingTime);
		if (slot.isPresent()) {
			appointmentService.bookAppointment(slot.get().id(), jwt.getTokenAttributes().get("email").toString());
			return McpSchema.CallToolResult.builder().addTextContent("ok").build();
		}
		return McpSchema.CallToolResult.builder().isError(true).addTextContent("No such appointment").build();
	}

	@McpTool(name = "list-appointments",
			description = "Get a list of all of a user's appointments between startDate and endDate, inclusive.")
	public McpSchema.CallToolResult listAppointmentsForAUser(
			@McpToolParam(description = "start date, inclusive", required = false) LocalDate startDate,
			@McpToolParam(description = "end date, inclusive", required = false) LocalDate endDate) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof JwtAuthenticationToken jwt)) {
			return McpSchema.CallToolResult.builder()
				.isError(true)
				.addTextContent("Unexpected authentication type")
				.build();
		}

		var slots = appointmentService
			.findSlotsByUserEmailAndDateRange(jwt.getTokenAttributes().get("email").toString(), startDate, endDate);
		return McpSchema.CallToolResult.builder().structuredContent(Map.of("appointments", slots)).build();
	}

}
