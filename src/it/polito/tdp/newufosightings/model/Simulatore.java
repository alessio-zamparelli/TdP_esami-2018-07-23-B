package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import it.polito.tdp.newufosightings.model.Evento.TipoEvento;

public class Simulatore {

	private PriorityQueue<Evento> queue;
	private int T1;
	private int T2;
	private Map<State, Integer> defconMap;
	private Set<State> inEmergenza;
	private Map<State, Integer> countEmergenze;

	public void init(int T1, int T2, List<Sighting> avvistamenti, List<State> stati) {
		this.T1 = T1;
		this.T2 = T2;
		this.queue = new PriorityQueue<>();
		this.defconMap = new HashMap<>();
		this.countEmergenze = new HashMap<>();
		this.inEmergenza = new HashSet<>();
		System.out.println("\n\n");
		for (State s : stati)
			defconMap.put(s, 5);
		for (Sighting s : avvistamenti) {
			Evento e = new Evento(s.getDatetime(), s.getState(), TipoEvento.AVVISTAMENTO, s);
//			System.out.println("Aggiunto l'evento " + e);
			queue.add(e);
		}
	}

	public void run() {
		Evento e;
		while ((e = queue.poll()) != null) {
			switch (e.getTipoEvento()) {
				case AVVISTAMENTO:
					System.out.println("AVVISTAMENTO: " + e);
					if (!inEmergenza.contains(e.getStato())) { // se lo stato non è gia in emergenza
						int defconAttuale = this.defconMap.get(e.getStato());
						if (defconAttuale > 1) {
							System.out.println("\tDEFCON " + defconAttuale);
							this.defconMap.put(e.getStato(), defconAttuale - 1);
							Evento newEvento = new Evento(e.getData().plusDays(this.T1), e.getStato(),
									TipoEvento.INCREMENTA_DEFCON, e.getSighting());
							queue.add(newEvento);
						} else {
							// sono gia a defcon 1, entrata in emergenza
							System.out.println("\tEMERGENZA!! DEFCON " + defconAttuale);
							Evento newEvento = new Evento(e.getData().plusDays(this.T2), e.getStato(),
									TipoEvento.FINE_EMERGENZA, e.getSighting());
							queue.add(newEvento);
							this.inEmergenza.add(e.getStato());
							int oldTot = 0;
							if (this.countEmergenze.containsKey(e.getStato()))
								oldTot = this.countEmergenze.get(e.getStato());
							this.countEmergenze.put(e.getStato(), oldTot + 1);
						}
					}
					break;

				case FINE_EMERGENZA:
					System.out.println("FINE_EMERGENZA " + e);
					this.inEmergenza.remove(e.getStato());
					this.defconMap.put(e.getStato(), 5);
					break;

				case INCREMENTA_DEFCON:
					System.out.println("INCREMENTA_DEFCON " + e);
					int defconAttuale = this.defconMap.get(e.getStato());
					if (defconAttuale < 5)
						this.defconMap.put(e.getStato(), defconAttuale + 1);
					break;

			}
		}

	}

	public Map<State, Integer> getCountEmergenze() {
		return this.countEmergenze;
	}

}
