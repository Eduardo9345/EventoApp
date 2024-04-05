package com.eventoapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.repository.EventoRepository;

@RestController
public class EventoController {
	
	@Autowired
	private EventoRepository er;
	
	@Autowired 
	private ConvidadoRepository cr;
	
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
		
		Iterable<Convidado> convidados = cr.findByEvento(ev);
		mv.addObject("convidados", convidados);
		
		return mv;
	}
	
	@PostMapping("/{codigo}")
	public ModelAndView cadastrarConvidado(@PathVariable long codigo, @Valid Convidado convidado, AbstractBindingResult result, RedirectAttributes attributes) {
		
		//Redirecionar mensagem de erro para a view
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return new ModelAndView("redirect:/{codigo}");
		}

		else {
			Evento evento = er.findById(codigo);
			convidado.setEvento(evento);
			evento.getConvidados().add(convidado);
			cr.save(convidado);
			er.save(evento);
			
			attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
			return new ModelAndView("redirect:/{codigo}");
		}
	}
}
