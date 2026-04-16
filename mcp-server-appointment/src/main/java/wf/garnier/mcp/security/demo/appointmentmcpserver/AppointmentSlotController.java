package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppointmentSlotController {

	private final AppointmentSlotRepository slotRepository;

	private final AppointmentService appointmentService;

	public AppointmentSlotController(AppointmentSlotRepository slotRepository, AppointmentService appointmentService) {
		this.slotRepository = slotRepository;
		this.appointmentService = appointmentService;
	}

	@GetMapping("/")
	public String index(Model model, @AuthenticationPrincipal OidcUser user) {
		List<AppointmentSlot> slots = slotRepository.findAll();
		model.addAttribute("slots", slots);

		if (user != null) {
			String userEmail = user.getEmail();
			Map<Integer, Boolean> bookings = new LinkedHashMap<>();
			for (AppointmentSlot slot : slots) {
				bookings.put(slot.id(), appointmentService.isBooked(slot.id(), userEmail));
			}
			model.addAttribute("bookings", bookings);
			model.addAttribute("userEmail", userEmail);
		}

		return "index";
	}

	@PostMapping("/book")
	public String bookAppointment(@RequestParam Integer slotId, @AuthenticationPrincipal OidcUser user) {
		if (user != null) {
			appointmentService.bookAppointment(slotId, user.getEmail());
		}
		return "redirect:/";
	}

	@PostMapping("/unbook")
	public String unbookAppointment(@RequestParam Integer slotId, @AuthenticationPrincipal OidcUser user) {
		if (user != null) {
			appointmentService.unbookAppointment(slotId, user.getEmail());
		}
		return "redirect:/";
	}

}
