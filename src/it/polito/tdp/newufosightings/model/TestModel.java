package it.polito.tdp.newufosightings.model;

public class TestModel {
	public static void main(String[] args) {
		Model model = new Model();
		double start = System.nanoTime();
		model.creaGrafo(2000, 20);
		double end = System.nanoTime();
//		System.out.format("Tempo impiegato: %.2f minuti", ((end-start)/1e9)/60);
		model.simula(5, 10, 2000);
		
		System.out.println("\n\nRISULTATI SIMULAZIONE\n");
		model.getRisultatiSimulazione().entrySet().forEach(a->System.out.println(a.getKey() + " - " + a.getValue()));
	}
}
