package src.p03.c01;

public class SistemaLanzador {
	public static void main(String[] args) {
		final int AFORO_MAX=50;
		IParque parque = new Parque(AFORO_MAX);
		char letra_puerta = 'A';
		
		System.out.println("¡Parque abierto!");
		
		for (int i = 0; i < 5; i++) {
			
			String puerta = ""+((char) (letra_puerta++));
			
			// Creación de hilos de entrada
			ActividadEntradaPuerta entradas = new ActividadEntradaPuerta(puerta, parque);
			
			ActividadEntradaPuerta salida = new ActividadEntradaPuerta(puerta, parque);
			
			new Thread (entradas).start();
			
			new Thread(salida).start();
			
		}
	}	
}