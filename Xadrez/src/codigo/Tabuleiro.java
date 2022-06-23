package codigo;
import java.util.*;

/**
 * @author Humberto Mattiello
 * @date June 2022
 *
 */
public class Tabuleiro {
	public char [][] casas; // Lembrando que se for fazer o acesso direto pelos indices, colocar primeiro a fileira
							// Por exemplo: casas[Y][X]
	
	
	//
	// Construtores
	//
	
	public Tabuleiro() {
		// /*
		char [][] temp = {{'T','C','B','D','R','B','C','T'},
						  {'P','P','P','P','P','P','P','P'},
						  {' ',' ',' ',' ',' ',' ',' ',' '},
						  {' ',' ',' ',' ',' ',' ',' ',' '},
						  {' ',' ',' ',' ',' ',' ',' ',' '},
						  {' ',' ',' ',' ',' ',' ',' ',' '},
						  {'p','p','p','p','p','p','p','p'},
						  {'t','c','b','d','r','b','c','t'}}; 
		// */
		/*
		// Tabuleiro de testes
		char [][] temp = {{'T',' ',' ',' ',' ',' ','R','T'},
						  {' ','p',' ',' ',' ','P',' ','P'},
						  {' ',' ',' ',' ',' ',' ',' ',' '},
						  {' ',' ',' ',' ',' ',' ','p','C'},
						  {' ',' ',' ',' ',' ',' ',' ',' '},
						  {' ',' ',' ',' ',' ',' ',' ',' '},
						  {' ',' ',' ',' ',' ',' ',' ','p'},
						  {'t',' ',' ',' ','r',' ','c','t'}}; 
		*/
		
		this.casas = temp;
	}
	
	public Tabuleiro(char[][] casas) {		
		char [][] temp = {{'T','C','B','D','R','B','C','T'},
			  	  		  {'P','P','P','P','P','P','P','P'},
			  	  		  {' ',' ',' ',' ',' ',' ',' ',' '},
			  	  		  {' ',' ',' ',' ',' ',' ',' ',' '},
			  	  		  {' ',' ',' ',' ',' ',' ',' ',' '},
			  	  		  {' ',' ',' ',' ',' ',' ',' ',' '},
			  	  		  {'p','p','p','p','p','p','p','p'},
			  	  		  {'t','c','b','d','r','b','c','t'}}; 
		
		try {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					temp[i][j] = casas[i][j];
				}
			}
			this.casas = temp;		
		}
		catch (Exception e) {			
			e.printStackTrace();
			this.casas = temp;		
		}
	}
	
	
	//
	// Getters e Setters
	//
	
	/**
	 * Essa funcao faz automaticamente a inversao entre fileira e coluna,
	 * para acesso mais intuitivo a matriz de casas
	 */
	public char getPecaEm(char x, int y) {
		return casas[coordenadaYParaIndex(y)][coordenadaXParaIndex(x)];
	}
	
	public void setPecaEm(char x, int y, char peca) {
		this.casas[coordenadaYParaIndex(y)][coordenadaXParaIndex(x)] = peca;
	}
	
	public char getPecaEm(Casa casa) {
		char x = casa.getCoordenadaX();
		int y = casa.getCoordenadaY();
		return getPecaEm(x, y);
	}
	
	public void setPecaEm(Casa casa, char peca) {
		char x = casa.getCoordenadaX();
		int y = casa.getCoordenadaY();
		setPecaEm(x, y, peca);
	}
	
	public void setPecaEm(Casa casa) {
		char x = casa.getCoordenadaX();
		int y = casa.getCoordenadaY();
		setPecaEm(x, y, casa.getTipoPeca());
	}
	
	//
	// Overrides úteis
	//
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Tabuleiro(this.casas);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Tabuleiro))
			return false;
		
		Tabuleiro tmp = (Tabuleiro) obj;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (tmp.casas[i][j] != this.casas[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		int i = 0, j = 0;
		String tmp = new String();
		Peca pecaTmp = new Peca();

		tmp += (ConsoleColors.RESET);
		tmp += ("  __________________\r\n");
		tmp += (" |                  |\r\n");
		for (i=7; i>=0; i--) {
			tmp += (i + 1);
			tmp += ("| ");
			for(j = 0; j < 8; j++) {
				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					tmp += (ConsoleColors.CYAN_BACKGROUND);
				}
				else {
					tmp += (ConsoleColors.GREEN_BACKGROUND);
				}
				pecaTmp.setTipoPeca(casas[i][j]);
				if (pecaTmp.isBranca()) {
					tmp += (ConsoleColors.WHITE_BOLD_BRIGHT);
					tmp += (casas[i][j]);
				}
				else {
					tmp += ConsoleColors.BLACK_BOLD;
					if(ConsoleColors.useStyle) {
						tmp += Character.toUpperCase(casas[i][j]);
					}
					else {
						tmp += casas[i][j];
					}					
				}				
				tmp += (' ');
				tmp += (ConsoleColors.RESET);
			}
			tmp += (" |\r\n");
			j = 0;
		}
		tmp += (" |__________________|\r\n");
		tmp += ("   a b c d e f g h\r\n");
		
		return tmp;
	}
	
	
	//
	// Lógica
	//
	
	public void imprimeTabuleiro() {
		System.out.print(this.toString());
	}
	
	public void atualizarTabuleiro (Jogada j) {
		// Faz o ajuste normal do movimento
		char tmp = this.getPecaEm(j.getCasaOrigem());
		this.setPecaEm(j.getCasaOrigem(), ' ');
		this.setPecaEm(j.getCasaDestino(), tmp);		
		
		// O En Passant exige um reposicionamento especial (remocao do peao que foi comido)
		if (j.isEnPassant()) {
			// Pega coluna para a qual o peao se moveu
			char colDoEnPassant = j.getCasaDestino().getCoordenadaX();
			
			// Pega a fileira para a qual o peao se moveu, menos 1
			int filDoEnPassant = j.isBrancas() ? j.getCasaDestino().getCoordenadaY() - 1 : j.getCasaDestino().getCoordenadaY() + 1;
			
			// Remove a peca nessas coordenadas
			this.setPecaEm(colDoEnPassant, filDoEnPassant, ' ');
		}
		
		// O Roque exige um posicionamento especial (ajuste da torre)
		if (j.isRoque() && j.isBrancas()) {
			this.setPecaEm('h', 1, ' ');
			this.setPecaEm('f', 1, 'T');
		}
		if (j.isRoque() && !j.isBrancas()) {
			this.setPecaEm('h', 8, ' ');
			this.setPecaEm('f', 8, 't');
		}		
		if (j.isRoqueGrande() && j.isBrancas()) {
			this.setPecaEm('a', 1, ' ');
			this.setPecaEm('d', 1, 'T');
		}
		if (j.isRoqueGrande() && !j.isBrancas()) {
			this.setPecaEm('a', 8, ' ');
			this.setPecaEm('d', 8, 't');
		}
		
		// Coroação também exige um movimento especial
		if (j.isCoroacao()) {
			this.setPecaEm(j.getCasaDestino(), j.getCoroada());
		}		
	}
	
	public Stack<Casa> casasComPecasDestaCor (boolean vezDasBrancas) {
		char pecaAux = ' ';
		char x = 'a';
		int y = 1;
		Peca peca = new Peca();
		Stack<Casa> pilhaDeCasas = new Stack<Casa>();

		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				pecaAux = this.casas[j][i];
				if (vezDasBrancas && peca.isPecaBranca(pecaAux)) {
					x = this.indexParaCoordenadaX(i);
					y = this.indexParaCoordenadaY(j);
					Casa novaCasa = new Casa(x, y, pecaAux);
					novaCasa.setPeca(this.getPecaEm(x, y));
					pilhaDeCasas.push(novaCasa);					
				}
				if (!vezDasBrancas && peca.isPecaPreta(pecaAux)) {
					x = this.indexParaCoordenadaX(i);
					y = this.indexParaCoordenadaY(j);
					Casa novaCasa = new Casa(x, y, pecaAux);
					novaCasa.setPeca(this.getPecaEm(x, y));
					pilhaDeCasas.push(novaCasa);					
				}
			}
		}
		
		return pilhaDeCasas;
	}
	
	/**
	 * 
	 * @return a somatória de material das peças da cor indicada em vezDasBrancas
	 */
	public int material(boolean vezDasBrancas) {
		int tmpMaterial = 0;
		Casa casaAux = new Casa();
		Stack<Casa> casasComPeca = casasComPecasDestaCor(vezDasBrancas);
		
		while (!casasComPeca.isEmpty()) {
			casaAux = casasComPeca.pop();
			if (vezDasBrancas ? casaAux.getPeca().isPecaBranca() : casaAux.getPeca().isPecaPreta()) {
				tmpMaterial += casaAux.getPeca().valorHeuristicoPadrao;
			}
		}
		
		return tmpMaterial;
	}
	
	public Casa ondeEstaORei (boolean vezDasBrancas) {
		char x = 'a';
		int y = 1;
		PecaRei rei = new PecaRei(vezDasBrancas ? 'R' : 'r');
		Casa casaDoRei = new Casa ();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {				
				if (this.casas[i][j] == rei.getTipoPeca()) {
					x = this.indexParaCoordenadaX(j);
					y = this.indexParaCoordenadaY(i);
					casaDoRei.setPeca(rei.getTipoPeca());
					casaDoRei.setCoordenadaX(x);
					casaDoRei.setCoordenadaY(y);
					break;
				}
			}
		}
		
		return casaDoRei;
	}
	
	public Tabuleiro copiaTabuleiro() {
		try {			
			return (Tabuleiro)this.clone();		
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return new Tabuleiro();		
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
