package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{

	private int aforo;
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	
	
	public Parque(int aforo) {	
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		this.aforo=aforo;
	}


	@Override
	public synchronized void entrarAlParque(String puerta){		
		
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
				
		comprobarAntesDeEntrar();
						
				
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
				
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
				
		notifyAll();
						
		checkInvariante();
		
	}
	
	@Override
	public synchronized void salirDelParque(String puerta) {
		
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		comprobarAntesDeSalir();
		
		contadorPersonasTotales--;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);
		
		
		// Imprimimos el estado del parque
		
		sumarContadoresPuerta();
	
		checkInvariante();
		
		imprimirInfo(puerta, "Salida");
		
		notifyAll();
		
	}
	
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private synchronized int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert contadorPersonasTotales<=aforo : "Se ha superado el maximo de personas permitidas";
		assert contadorPersonasTotales>=0 : "No se han gestionado correctamente las salidas";
	}

	protected void comprobarAntesDeEntrar(){	
		
		while(contadorPersonasTotales==aforo) {
			try {
				wait();
			}catch(InterruptedException e) {
				 e.printStackTrace();
			}
		}
	}

	protected void comprobarAntesDeSalir(){	
		while(contadorPersonasTotales==0) {
			try {
				wait();
			}catch(InterruptedException e) {
				 e.printStackTrace();
			}
		}
	}

}
