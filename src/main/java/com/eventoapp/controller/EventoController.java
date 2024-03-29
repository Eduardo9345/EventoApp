package com.eventoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.eventoapp.models.Evento;
import com.eventoapp.repository.EventoRepository;

@RestController
public class EventoController {
	
	@Autowired
	private EventoRepository er;
	
	@GetMapping("/cadastrarEvento")
	public ModelAndView form() {
		return new ModelAndView("evento/formEvento");
	}
	
	@PostMapping("/cadastrarEvento")
	public ModelAndView form(Evento evento) {
		
		er.save(evento);
		
		return new ModelAndView("redirect:/cadastrarEvento");
	}
	
	@GetMapping("/eventos")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento> eventos = er.findAll();
		mv.addObject("eventos", eventos);
		return mv;
	}
	
	
	@GetMapping("/{codigo}")
	public ModelAndView detalhesEvento(@PathVariable long codigo) {
		Evento ev = er.findById(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento", ev);
		return mv;
	}
	
}
