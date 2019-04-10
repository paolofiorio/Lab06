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
		
		
		
		
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}

}
