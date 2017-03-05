package sml.mestrado.ufpa.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import sml.mestrado.ufpa.repositorio.CustomerRepositorio;

@RestController
public class CustomerRestController {
	
	@Autowired
	private CustomerRepositorio customerDAO;

	
	@GetMapping("/customers")
	public List getCustomers(HttpServletRequest request) {
		return customerDAO.list();
	}
}
