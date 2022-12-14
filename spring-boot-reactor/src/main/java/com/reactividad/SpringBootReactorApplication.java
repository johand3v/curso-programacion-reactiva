package com.reactividad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.reactividad.model.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Usuario> nombres = Flux.just("Jorge Perez", "Pedro Sanchez", "PEPE Rodriguez", "Marcos Mamani")
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario ->  usuario.getNombre().toLowerCase().equals("jorge"))
				.doOnNext(usuario -> {
			if (usuario == null) {
				throw new RuntimeException("Nombres no pueden ser vacios");
			} else {
				System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
			}
		}).map(usuario -> {
			String nombre = usuario.getNombre().toLowerCase();
			usuario.setNombre(nombre);
			return usuario;
		});

		nombres.subscribe(e -> LOGGER.info(e.toString()),
				error -> LOGGER.error(error.getMessage()), 
				new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						LOGGER.info("se completado...");
						
					}
				});
	}

}
