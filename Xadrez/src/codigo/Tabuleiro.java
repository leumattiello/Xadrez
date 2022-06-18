package codigo;
import java.util.*;

/**
 * @author Humberto Mattiello
 * @date July 2021
 *
 */
public class Tabuleiro {
	public char [][] casas; // Lembrando que se for fazer o acesso direto pelos indices, colocar primeiro a fileira
							// Por exemplo: casas[Y][X]
	public int [][] heuristica;
	
	// Construtor
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
		char [][] temp = {{'T','C','B','D','R','B','C','T'},
						  {'P','P','P','P','P',' ',' ','P'},
						  {' ',' ',' ',' ',' ',' ',' ',' '},
						  {' ',' ',' ',' ',' ',' ',' ',' '},
						  {' ',' ',' ',' ',' ',' ',' ',' '},
						  {' ',' ',' ',' ',' ',' ',' ',' '},
						  {'p','p','p','p',' ','p','p','p'},
						  {'t','c','b','d','r','b','c','t'}}; 
		*/
		
		int[][] temp2 = {{0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0}}; 
		
		this.casas = temp;		
		this.heuristica = temp2;
	}
	
	public Tabuleiro(char[][] casas) {
		int[][] temp2 = {{0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0}}; 
		
		this.casas = casas;
		this.heuristica = temp2;
	}
	
	public Tabuleiro (char[][] casas, int[][] heuristica) {
		this.casas = casas;
		this.heuristica = heuristica;
	}
	
	// Logica
	/**
	 * Imprimir as fileiras de tras para frente, por motivos esteticos
	 */
	public void imprimeTabuleiro() {
		Peca pecaTmp = new Peca();
		int i, j = 0;
		System.out.println("  __________________ ");
		System.out.println(" |                  |");
		for (i=7; i>=0; i--) {
			System.out.print(i + 1);
			System.out.print("| ");
			for(j = 0; j < 8; j++) {
				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					System.out.print(ConsoleColors.CYAN_BACKGROUND);
				}
				else {
					System.out.print(ConsoleColors.GREEN_BACKGROUND);
				}
				pecaTmp.setTipoPeca(casas[i][j]);
				if (pecaTmp.isBranca()) {
					System.out.print(ConsoleColors.WHITE_BOLD_BRIGHT);
					System.out.print(casas[i][j]);
				}
				else {
					System.out.print(ConsoleColors.BLACK_BOLD);
					if (ConsoleColors.useStyle) {
						System.out.print(Character.toUpperCase(casas[i][j]));
					}
					else {
						System.out.print(casas[i][j]);
					}					
				}				
				System.out.print(' ');
				System.out.print(ConsoleColors.RESET);
			}
			System.out.println(" |");
			j = 0;
		}
		System.out.println(" |__________________|");
		System.out.println("   a b c d e f g h ");
	}
	
	public void atualizarTabuleiro (Jogada jogada) {
		char tmp;
		
		tmp = this.casas[coordenadaYParaIndex(jogada.origem.getCoordenadaY())][coordenadaXParaIndex(jogada.origem.getCoordenadaX())];
		this.casas[coordenadaYParaIndex(jogada.origem.getCoordenadaY())][coordenadaXParaIndex(jogada.origem.getCoordenadaX())] = ' ';
		this.casas[coordenadaYParaIndex(jogada.destino.getCoordenadaY())][coordenadaXParaIndex(jogada.destino.getCoordenadaX())] = tmp; 
	}
	
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
	
	public Stack<Casa> casasComPecasDestaCor (boolean vezDasBrancas) {
		Stack<Casa> pilhaDeCasas = new Stack<Casa>();
		Peca peca = new Peca();
		char pecaAux = ' ';
		char x = 'a';
		int y = 1;

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
	
	public void atualizarMapaHeuristico() {
		Peca pecaAux = new Peca();
		int i, j = 0;
		
		for (i = 0; i < 8; i++) {
			for (j = 0; j < 8; j++) {
				pecaAux.setTipoPeca(casas[i][j]);
				heuristica[i][j] = pecaAux.getValorHeuristicoPadrao();
			}
		}		
	}
	
	/**
	 * 
	 * @return a somatória de material das peças da cor indicada em vezDasBrancas
	 */
	public int material(boolean vezDasBrancas) {
		Stack<Casa> casasComPeca = casasComPecasDestaCor(vezDasBrancas);
		Casa casaAux = new Casa();
		int tmpMaterial = 0;
		
		while (!casasComPeca.isEmpty()) {
			casaAux = casasComPeca.pop();
			if (vezDasBrancas ? casaAux.peca.isPecaBranca() : casaAux.peca.isPecaPreta()) {
				tmpMaterial += casaAux.peca.valorHeuristicoPadrao;
			}
		}
		
		return tmpMaterial;
	}
	
	public Casa ondeEstaORei (boolean vezDasBrancas) {
		PecaRei rei = new PecaRei(vezDasBrancas ? 'R' : 'r');
		Casa casaDoRei = new Casa ();
		char x = 'a';
		int y = 1;
		
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
		char [][] temp ={{'T','C','B','D','R','B','C','T'},
				 {'P','P','P','P','P','P','P','P'},
				 {' ',' ',' ',' ',' ',' ',' ',' '},
				 {' ',' ',' ',' ',' ',' ',' ',' '},
				 {' ',' ',' ',' ',' ',' ',' ',' '},
				 {' ',' ',' ',' ',' ',' ',' ',' '},
				 {'p','p','p','p','p','p','p','p'},
				 {'t','c','b','d','r','b','c','t'}}; 
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {				
				temp[i][j] = this.casas[i][j];
			}
		}
		return new Tabuleiro (temp);
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
	
	public boolean equalsTo (Tabuleiro tabuleiro) {
		int i, j = 0;
		for (i = 0; i < 8; i++) {
			for (j = 0; j < 8; j++) {
				if (this.casas[i][j] != tabuleiro.casas[i][j])
					return false;
			}
		}
		return true;
	}
}
