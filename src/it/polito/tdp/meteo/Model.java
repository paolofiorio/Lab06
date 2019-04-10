package it.polito.tdp.meteo;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private List<Citta> citta;
	
	private List<Citta> migliori;


	public Model() {
		MeteoDAO dao= new MeteoDAO();
		this.citta=dao.getAllCitta();
	}

	public List<Citta> getCitta() {
		return citta;
	}

	public Double getUmiditaMedia(Month mese, Citta citta) {
		MeteoDAO dao= new MeteoDAO();
		return dao.getAvgRilevamentiLocalitaMese(citta, mese);
	}

	public List<Citta> trovaSequenza(Month mese) {
		List<Citta> parziale = new ArrayList<Citta>();
		this.migliori=null;
		
		MeteoDAO dao = new MeteoDAO();

		
		for (Citta c : citta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c));
		}
		
		cerca(parziale, 0);
		return migliori;
	}

	private void cerca(List<Citta> parziale, int livello) {
		
		if (livello == NUMERO_GIORNI_TOTALI) {
			// caso terminale
			Double costo = punteggioSoluzione(parziale);
			if (migliori == null || costo < punteggioSoluzione(migliori)) {
				migliori = new ArrayList<>(parziale);
			}

		} else {

			// caso intermedio
			for (Citta prova : citta) {

				if (controllaParziale(prova, parziale)) {

					parziale.add(prova);
					cerca(parziale, livello + 1);
					parziale.remove(parziale.size() - 1);

				}
			}

		}

		
		
	}

	private Double punteggioSoluzione(List<Citta> parziale) {

		double costo = 0.0;

		// sommatoria delle umidità in ciascuna città
		
		for (int giorno = 1; giorno <= NUMERO_GIORNI_TOTALI; giorno++) {
		
			Citta c = parziale.get(giorno - 1);
			double umid = c.getRilevamenti().get(giorno - 1).getUmidita();
			costo += umid;

		}

		// a cui sommo 100 * numero di volte in cui cambio città
		for (int giorno = 2; giorno <= NUMERO_GIORNI_TOTALI; giorno++) {
			if (!parziale.get(giorno - 1).equals(parziale.get(giorno - 2))) {
				costo += COST;
			}
		}

		return costo;
	}

	private boolean controllaParziale(Citta prova, List<Citta> parziale) {

		// verifica giorni massimi
				int conta = 0;
				for (Citta precedente : parziale)
					if (precedente.equals(prova))
						conta++;
				if (conta >= NUMERO_GIORNI_CITTA_MAX)
					return false;
				// verifica giorni minimi
				if (parziale.size() == 0) // primo giorno
					return true;
				if (parziale.size() == 1 || parziale.size() == 2) { // secondo o terzo giorno: non posso cambiare
					return parziale.get(parziale.size() - 1).equals(prova);
				}
				if (parziale.get(parziale.size() - 1).equals(prova)) // giorni successivi, posso SEMPRE rimanere
					return true;
				// sto cambiando citta
				if (parziale.get(parziale.size() - 1).equals(parziale.get(parziale.size() - 2))
						&& parziale.get(parziale.size() - 2).equals(parziale.get(parziale.size() - 3)))
					return true;

		return false;
	}

}
