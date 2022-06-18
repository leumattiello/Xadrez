package codigo;

import java.util.Stack;

public class Casa {
	private char coordenadaX; // a, b, ..., h  	
	private int coordenadaY; // 1, 2, ..., 8
	public Peca peca;
	
	// Construtores
	public Casa() {
		new Casa ('e', 2);
	}
	
	public Casa(char coordenadaX, int coordenadaY) {
		new Casa(coordenadaX, coordenadaY, 'P');
	}
	
	public Casa(char coordenadaX, int coordenadaY, char peca) {
		setCoordenadaX(coordenadaX);
		setCoordenadaY(coordenadaY);
		this.peca = new Peca();
		this.peca.setTipoPeca(peca);
	}
	
	// Setters e Getters
	public char getCoordenadaX() {
		return coordenadaX;
	}

	public void setCoordenadaX(char coordenadaX) {
		if (coordenadaX >= 'a' && coordenadaX <= 'h') 
			this.coordenadaX = coordenadaX;
		else
			System.out.println("Erro durante atribuição de coordenada");
	}

	public int getCoordenadaY() {
		return coordenadaY;
	}

	public void setCoordenadaY(int coordenadaY) {
		if (coordenadaY >= 1 && coordenadaY <= 8)
			this.coordenadaY = coordenadaY;
		else
			System.out.println("Erro durante atribuição de coordenada");
	}

	public Peca getPeca() {
		return peca;
	}

	public void setPeca(char peca) {
		if (peca == 'p' || peca == 'c' || peca == 'b' || peca == 't' || peca == 'd' || peca == 'r' || peca == ' ' ||
			peca == 'P' || peca == 'C' || peca == 'B' || peca == 'T' || peca == 'D' || peca == 'R') {
			Peca pecaAux;			
			switch (peca) {
			case 'p': {
				pecaAux = new PecaPeao(peca);
				break;
			}
			case 'P': {
				pecaAux = new PecaPeao(peca);
				break;
			}
			case 'c': {
				pecaAux = new PecaCavalo(peca);
				break;
			}
			case 'C': {
				pecaAux = new PecaCavalo(peca);
				break;
			}
			case 'b': {
				pecaAux = new PecaBispo(peca);
				break;
			}
			case 'B': {
				pecaAux = new PecaBispo(peca);
				break;
			}
			case 't': {
				pecaAux = new PecaTorre(peca);
				break;
			}
			case 'T': {
				pecaAux = new PecaTorre(peca);
				break;
			}
			case 'd': {
				pecaAux = new PecaDama(peca);
				break;
			}
			case 'D': {
				pecaAux = new PecaDama(peca);
				break;
			}
			case 'r': {
				pecaAux = new PecaRei(peca);
				break;
			}
			case 'R': {
				pecaAux = new PecaRei(peca);
				break;
			}
			default: pecaAux = new Peca(peca);
			}
			this.peca = pecaAux;
		}
		else
			System.out.println("Erro durante atribuição da peca");
	}
	
	// Logica
	/**
	 * Consulta todas as pecas da mesma cor, para ver de quantas delas esta casa esta no range
	 * @param tabuleiro
	 * @param vezDasBrancas
	 * @return
	 */
	public int estaDefendida(Tabuleiro tabuleiro, boolean vezDasBrancas) {
		Stack<Casa> casasComPeca = new Stack<Casa>();
		Stack<Casa> casasAmeacadas = new Stack<Casa>();
		Casa casaAux = new Casa();
		Tabuleiro tabuleiroAux = new Tabuleiro();
		int resultado = 0;

		// Copia o tabuleiro para podermos fazer simulacoes sem alterar o tabuleiro original
		for (int i = 0; i < 8; i++) 
			for (int j = 0; j < 8; j++)
				tabuleiroAux.casas[i][j] = tabuleiro.casas[i][j];	
		
		// Faz de conta que a casa sob verificacao nao tem peca ali
		tabuleiroAux.setPecaEm(this.getCoordenadaX(), this.getCoordenadaY(), ' ');
		
		// Cria uma pilha com todas as casas que possuam peca da cor defensora
		casasComPeca = tabuleiroAux.casasComPecasDestaCor(vezDasBrancas);
		
		// Desempilha a pilha criada anteriormente, a cada passo verificando o range da peca desempilhada
		while (!casasComPeca.isEmpty()) {
			// Cria uma outra pilha auxiliar de casas com as casas ameacadas a partir da casa aux
			casaAux = casasComPeca.pop();
			casasAmeacadas = casaAux.peca.ameaca(tabuleiroAux, casaAux, vezDasBrancas);
			
			// Conferir dentro do range de ameaca da peca desempilhada se eventualmente a casa "this" eh uma delas
			while (!casasAmeacadas.isEmpty()) {
				casaAux = casasAmeacadas.pop();
				if (casaAux.getCoordenadaX() == this.getCoordenadaX() && casaAux.getCoordenadaY() == this.getCoordenadaY())
					resultado++;
			}
		}
		
		// Se nao foi encontrada nenhuma peca da cor defensora, em cujo range estaria esta casa, entao a casa nao esta defendida. Retorne false.
		return resultado;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Casa))
			return false;
		Casa casa = (Casa) obj;
		return this.getCoordenadaX() == casa.getCoordenadaX() &&
			   this.getCoordenadaY() == casa.getCoordenadaY();
	}

	@Override
	public String toString() {
		return Character.toString(coordenadaX) + Integer.toString(coordenadaY);
	}
}
