package codigo;

import java.util.*;

/**
 * @author Humberto Mattiello
 * @date June 2022
 *
 */
public class Heuristica {
	private boolean isBrancas;
	private int defesaTotal;
	private int materialTotal;
	private int materialOponenteTotal;
	private int casasSobAtaqueTotal;
	private int materialAmeacadoTotal;
	private int[][] mapaDefesa;
	private int[][] mapaSobAtaque;
	private int[][] mapaAmeacas;
	private int[][] mapaMaterial;
	
	
	//
	// Construtores
	//
	
	public Heuristica(boolean isBrancas) {
		this.isBrancas = isBrancas;
		this.defesaTotal = 38;
		this.materialTotal = 140;

		// Mapa defesa brancas
		int[][] tmp = {{0, 1, 1, 1, 1, 1, 1, 0},
			 	   {1, 1, 1, 4, 4, 1, 1, 1},
			 	   {2, 2, 3, 2, 2, 3, 2, 2},
			 	   {0, 0, 0, 0, 0, 0, 0, 0},
			 	   {0, 0, 0, 0, 0, 0, 0, 0},
			 	   {0, 0, 0, 0, 0, 0, 0, 0},
			 	   {0, 0, 0, 0, 0, 0, 0, 0},
			 	   {0, 0, 0, 0, 0, 0, 0, 0}};

		// Mapa material pretas
		int[][] tmp2 = {{-5, -3, -3, -10, -100, -3, -3, -5},
				{-1, -1, -1, -1, -1, -1, -1, -1},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{5, 3, 3, 10, 100, 3, 3, 5}};
		
		// Mapa defesa pretas
		int[][] tmp3 = {{0, 0, 0, 0, 0, 0, 0, 0},
				   {0, 0, 0, 0, 0, 0, 0, 0},
				   {0, 0, 0, 0, 0, 0, 0, 0},
				   {0, 0, 0, 0, 0, 0, 0, 0},
				   {0, 0, 0, 0, 0, 0, 0, 0},
				   {2, 2, 3, 2, 2, 3, 2, 2},
				   {1, 1, 1, 4, 4, 1, 1, 1},
				   {0, 1, 1, 1, 1, 1, 1, 0}};
		
		// Mapa material brancas
		int[][] tmp4 = {{5, 3, 3, 10, 100, 3, 3, 5},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{-1, -1, -1, -1, -1, -1, -1, -1},
				{-5, -3, -3, -10, -100, -3, -3, -5}};
		
		// Mapa sobAtaque, brancas e pretas
		int[][] tmp5 = {{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};
		
		// Mapa ameacas, brancas e pretas
		int[][] tmp6 = {{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};
		
		if (isBrancas) {
			this.mapaDefesa = tmp;
			this.mapaMaterial = tmp4;
		}
		else {
			this.mapaDefesa = tmp3;
			this.mapaMaterial = tmp2;
		}
		
		this.mapaSobAtaque = tmp5;
		this.mapaAmeacas = tmp6;
	}


	//
	// Setters e getters
	//
	
	public boolean isBrancas() {
		return isBrancas;
	}
	
	public void setBrancas(boolean isBrancas) {
		this.isBrancas = isBrancas;
	}

	public int getCasasSobAtaqueTotal() {
		return casasSobAtaqueTotal;
	}

	public void setCasasSobAtaqueTotal(int casasSobAtaqueTotal) {
		this.casasSobAtaqueTotal = casasSobAtaqueTotal;
	}

	public int getDefesaTotal() {
		return defesaTotal;
	}

	public void setDefesaTotal(int defesaTotal) {
		this.defesaTotal = defesaTotal;
	}

	public int getMaterialTotal() {
		return materialTotal;
	}

	public void setMaterialTotal(int materialTotal) {
		this.materialTotal = materialTotal;
	}
	
	public int getMaterialOponenteTotal() {
		return materialOponenteTotal;
	}
	
	public void setMaterialOponenteTotal(int materialTotal) {
		this.materialOponenteTotal = materialTotal;
	}
	
	public int getMaterialAmeacadoTotal() {
		return materialAmeacadoTotal;
	}

	public void setMaterialAmeacadoTotal(int materialAmeacadoTotal) {
		this.materialAmeacadoTotal = materialAmeacadoTotal;
	}
	
	public int getMaterialEm(Casa casa) {
		int j = coordenadaXParaIndex(casa.getCoordenadaX());
		int i = coordenadaYParaIndex(casa.getCoordenadaY());
		return this.mapaMaterial[i][j];
	}
	
	public void setMaterialEm(Casa casa, int material) {
		int j = coordenadaXParaIndex(casa.getCoordenadaX());
		int i = coordenadaYParaIndex(casa.getCoordenadaY());
		this.mapaMaterial[i][j] = material;
	}
	
	public int getCasaSobAtaque(Casa casa) {
		int j = coordenadaXParaIndex(casa.getCoordenadaX());
		int i = coordenadaYParaIndex(casa.getCoordenadaY());
		return mapaSobAtaque[i][j];
	}

	public void setCasaSobAtaque(Casa casa, int qtdAtaques) {
		int j = coordenadaXParaIndex(casa.getCoordenadaX());
		int i = coordenadaYParaIndex(casa.getCoordenadaY());
		this.mapaSobAtaque[i][j] = qtdAtaques;
	}

	public int getMapaAmeacasEm(Casa casa) {
		int j = coordenadaXParaIndex(casa.getCoordenadaX());
		int i = coordenadaYParaIndex(casa.getCoordenadaY());
		return mapaAmeacas[i][j];
	}

	public void setMapaAmeacasEm(Casa casa, int ameaca) {
		int j = coordenadaXParaIndex(casa.getCoordenadaX());
		int i = coordenadaYParaIndex(casa.getCoordenadaY());
		this.mapaAmeacas[i][j] = ameaca;
	}
	
	public int getDefesaEm(Casa casa) {
		int j = coordenadaXParaIndex(casa.getCoordenadaX());
		int i = coordenadaYParaIndex(casa.getCoordenadaY());
		return this.mapaDefesa[i][j];
	}
	
	public void setDefesaEm(Casa casa, int defesa) {
		int j = coordenadaXParaIndex(casa.getCoordenadaX());
		int i = coordenadaYParaIndex(casa.getCoordenadaY());
		this.mapaDefesa[i][j] = defesa;
	}
	
	
	//
	// Overrides Úteis
	//

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Heuristica h = new Heuristica(this.isBrancas());
		
		h.mapaDefesa = this.copiaMapa(1);
		h.mapaSobAtaque = this.copiaMapa(2);
		h.mapaAmeacas = this.copiaMapa(3);
		h.mapaMaterial = this.copiaMapa(4);
		h.setDefesaTotal(this.getDefesaTotal());
		h.setCasasSobAtaqueTotal(this.getCasasSobAtaqueTotal());
		h.setMaterialAmeacadoTotal(this.getMaterialAmeacadoTotal());
		h.setMaterialOponenteTotal(this.getMaterialOponenteTotal());
		h.setMaterialTotal(this.getMaterialTotal());
		
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Heuristica))
			return false;
		
		Heuristica tmp = (Heuristica) obj;
		
		char x = 'a';
		int y = 1;
		Casa casa = new Casa();
		
		if (!tmp.isBrancas() == this.isBrancas()) {
			return false;
		}
		if (tmp.getCasasSobAtaqueTotal() != this.getCasasSobAtaqueTotal()) {
			return false;
		}
		if (tmp.getDefesaTotal() != this.getDefesaTotal()) {
			return false;
		}		
		if (tmp.getMaterialOponenteTotal() != this.getMaterialOponenteTotal()) {
			return false;
		}		
		if (tmp.getMaterialTotal() != this.getMaterialTotal()) {
			return false;
		}
		if (tmp.getMaterialAmeacadoTotal() != this.getMaterialAmeacadoTotal()) {
			return false;
		}
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				x = indexParaCoordenadaX(j);
				y = indexParaCoordenadaY(i);
				casa.setCoordenadaX(x);
				casa.setCoordenadaY(y);
				if (this.getDefesaEm(casa) != tmp.getDefesaEm(casa)) {
					return false;
				}
				if (this.getMaterialEm(casa) != tmp.getMaterialEm(casa)) {
					return false;
				}
				if (this.getCasaSobAtaque(casa) != tmp.getCasaSobAtaque(casa)) {
					return false;
				}
				if (this.getMapaAmeacasEm(casa) != tmp.getMapaAmeacasEm(casa)) {
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public String toString() {
		int i = 7, j = 0;
		String str = new String("");
		
		str += this.isBrancas() ? "Brancas \r\n" : "Pretas \r\n";
		str += "\r\n";
		
		str += "Mapa de Ameaças: \r\n";
		for (i=7; i>=0; i--) {
			for(j = 0; j < 8; j++) {
				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					str += (ConsoleColors.CYAN_BACKGROUND);
				}
				else {
					str += (ConsoleColors.GREEN_BACKGROUND);
				}
				if (isBrancas()) {
					str += (ConsoleColors.WHITE_BOLD_BRIGHT);
					str += (mapaAmeacas[i][j]);
				}
				else {
					System.out.print(ConsoleColors.BLACK_BOLD);
					str += mapaAmeacas[i][j];					
				}				
				str += (' ');
				str += (ConsoleColors.RESET);
			}
			str += ("\r\n");
			j = 0;
		}
		str += ("\r\n");
		
		str += "Mapa de Defesa: \r\n";
		for (i=7; i>=0; i--) {
			for(j = 0; j < 8; j++) {
				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					str += (ConsoleColors.CYAN_BACKGROUND);
				}
				else {
					str += (ConsoleColors.GREEN_BACKGROUND);
				}
				if (isBrancas()) {
					str += (ConsoleColors.WHITE_BOLD_BRIGHT);
					str += (mapaDefesa[i][j]);
				}
				else {
					System.out.print(ConsoleColors.BLACK_BOLD);
					str += mapaDefesa[i][j];					
				}				
				str += (' ');
				str += (ConsoleColors.RESET);
			}
			str += ("\r\n");
			j = 0;
		}
		str += ("\r\n");
		
		str += "Mapa Material: \r\n";
		for (i=7; i>=0; i--) {
			for(j = 0; j < 8; j++) {
				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					str += (ConsoleColors.CYAN_BACKGROUND);
				}
				else {
					str += (ConsoleColors.GREEN_BACKGROUND);
				}
				if (isBrancas()) {
					str += (ConsoleColors.WHITE_BOLD_BRIGHT);
					str += (mapaMaterial[i][j]);
				}
				else {
					System.out.print(ConsoleColors.BLACK_BOLD);
					str += mapaMaterial[i][j];					
				}				
				str += (' ');
				str += (ConsoleColors.RESET);
			}
			str += ("\r\n");
			j = 0;
		}
		str += ("\r\n");
		
		str += "Mapa de Casas Sob Ataque: \r\n";
		for (i=7; i>=0; i--) {
			for(j = 0; j < 8; j++) {
				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					str += (ConsoleColors.CYAN_BACKGROUND);
				}
				else {
					str += (ConsoleColors.GREEN_BACKGROUND);
				}
				if (isBrancas()) {
					str += (ConsoleColors.WHITE_BOLD_BRIGHT);
					str += (mapaSobAtaque[i][j]);
				}
				else {
					System.out.print(ConsoleColors.BLACK_BOLD);
					str += mapaSobAtaque[i][j];					
				}				
				str += (' ');
				str += (ConsoleColors.RESET);
			}
			str += ("\r\n");
			j = 0;
		}
		str += ("\r\n");
		
		return str;
	}
	


	//
	// Lógica
	//
	
	public void atualizaMapas(Tabuleiro tabuleiro) {
		char x = 'a';
		int y = 1;
		int somaDefesa = 0;
		int somaMaterial = 0;
		int somaMaterialOponente = 0;
		int somaCasasSobAtaque = 0;
		int somaMaterialAmeacado = 0;
		Peca peca;
		Casa casa;
		ArrayList<Casa> origensDosAtaques = new ArrayList<Casa>();
		ArrayList<Casa> origensDasDefesas = new ArrayList<Casa>();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				x = indexParaCoordenadaX(j);
				y = indexParaCoordenadaY(i);
				casa = new Casa(x, y, tabuleiro.getPecaEm(x, y));
				peca = new Peca(casa.getTipoPeca());
				casa.calculaAmeacasEDefesas(tabuleiro, this.isBrancas(), origensDosAtaques, origensDasDefesas);
				
				// Atualiza mapaMaterial
				if(this.isBrancas() == peca.isBranca()) {
					this.setMaterialEm(casa, peca.getValorHeuristicoPadrao());
					somaMaterial += this.getMaterialEm(casa);
				}
				else {
					this.setMaterialEm(casa, -1 * peca.getValorHeuristicoPadrao());
					somaMaterialOponente -= this.getMaterialEm(casa);
				}		
							
				// Atualiza mapaDefesa
				this.setDefesaEm(casa, casa.getDefesas());
				somaDefesa += this.getDefesaEm(casa);
				
				// Atualiza mapaSobAtaque
				this.setCasaSobAtaque(casa, casa.getAmeacas());
				somaCasasSobAtaque += this.getCasaSobAtaque(casa);
				
				// Atualiza mapaAmeacas
				// Se nao tiver uma peca da cor defensora na casa, ameaca = 0;
				// Se a casa nao esta sob ataque, ameaca = 0;
				// Se a casa esta sob ataque e nao esta defendida, ameaça = valorHeuristicoDaPeça na casa;
				// Se a casa esta sob ataque e esta defendida, ameaça = valorHeuristicoDaPeca na casa - valorHeuristico da menor ameaca
				if(!casa.getPeca().isPeca()) {
					this.setMapaAmeacasEm(casa, 0);
				}
				else if(0 >= casa.getAmeacas()) {
					this.setMapaAmeacasEm(casa, 0);
				}
				else if(0 >= casa.getDefesas()){
					this.setMapaAmeacasEm(casa, casa.getPeca().getValorHeuristicoPadrao());
				}
				else {
					this.setMapaAmeacasEm(casa, casa.getPeca().getValorHeuristicoPadrao() - menorAmeaca(origensDosAtaques).getPeca().getValorHeuristicoPadrao());
					// Valor nao pode ser negativo. Se for, zera.
					if (0 > this.getMapaAmeacasEm(casa)){
						this.setMapaAmeacasEm(casa, 0);
					}
				}				
				somaMaterialAmeacado += this.getMapaAmeacasEm(casa);
			}
		}
		this.materialTotal = somaMaterial;
		this.materialOponenteTotal = somaMaterialOponente;
		this.defesaTotal = somaDefesa;
		this.casasSobAtaqueTotal = somaCasasSobAtaque;
		this.materialAmeacadoTotal = somaMaterialAmeacado;
	}
	
	public Casa menorAmeaca(ArrayList<Casa> listaDeAtacantes) {
		int ameaca = 0; 
		int i = 0, indice = 0;
		
		if (0 >= listaDeAtacantes.size()) {
			return null;
		}	
		
		int menorAmeaca = listaDeAtacantes.get(0).getPeca().getValorHeuristicoPadrao(); 
	    		
		for (Casa casa : listaDeAtacantes) {
			ameaca = casa.getPeca().getValorHeuristicoPadrao();
			if (ameaca < menorAmeaca) {
				menorAmeaca = ameaca;
				indice = i;
			}	
			i++;
		}
		
		return listaDeAtacantes.get(indice);
	}
	
	public int[][] copiaMapa (int qualMapa){
		int[][] tmp = {{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};
		switch(qualMapa) {
			case 1: {
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						tmp[i][j] = this.mapaDefesa[i][j];
					}
				}
				break;
			}
			case 2: {
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						tmp[i][j] = this.mapaSobAtaque[i][j];
					}
				}
				break;
			}
			case 3: {
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						tmp[i][j] = this.mapaAmeacas[i][j];
					}
				}
				break;
			}
			case 4: {
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						tmp[i][j] = this.mapaMaterial[i][j];
					}
				}
				break;
			}
			default: return tmp;
		}
		return tmp;
	}
	
	public Heuristica copiaHeuristica() {
		try {
			return (Heuristica) this.clone();	
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public int coordenadaXParaIndex (char c) {
		switch (c) {
			case 'a': return 0;
			case 'b': return 1;	
			case 'c': return 2;	
			case 'd': return 3;	
			case 'e': return 4;	
			case 'f': return 5;	
			case 'g': return 6;	
			case 'h': return 7;	
		}
		return 0;
	}	
	
	public char indexParaCoordenadaX (int i) {
		switch (i) {
			case 0: return 'a';
			case 1: return 'b';	
			case 2: return 'c';	
			case 3: return 'd';	
			case 4: return 'e';	
			case 5: return 'f';	
			case 6: return 'g';	
			case 7: return 'h';	
		}
		return 0;
	}	
	
	public int coordenadaYParaIndex (int c) {
		return c - 1;
	}	
	
	public int indexParaCoordenadaY (int i) {
		return i + 1;
	}
}	
