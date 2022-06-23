package codigo;

import java.util.ArrayList;
import java.util.Stack;

public class Casa {
	private char coordenadaX; // a, b, ..., h  	
	private int coordenadaY; // 1, 2, ..., 8
	private int ameacas;
	private int defesas;
	private Peca peca;
	
	
	//
	// Construtores
	//
	
	public Casa() {
		setPeca('P');
		setCoordenadaX('e');
		setCoordenadaY(2);
		this.ameacas = 0;
		this.defesas = 0;
	}
	
	public Casa(char coordenadaX, int coordenadaY) {
		setPeca('P');
		setCoordenadaX(coordenadaX);
		setCoordenadaY(coordenadaY);
		this.ameacas = 0;
		this.defesas = 0;
	}
	
	public Casa(char coordenadaX, int coordenadaY, char peca) {
		setPeca(peca);
		setCoordenadaX(coordenadaX);
		setCoordenadaY(coordenadaY);
		this.ameacas = 0;
		this.defesas = 0;
	}
	
	
	//
	// Setters e Getters
	//
	
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

	public int getAmeacas() {
		return ameacas;
	}

	public void setAmeacas(int ameacas) {
		this.ameacas = ameacas;
	}

	public int getDefesas() {
		return defesas;
	}

	public void setDefesas(int defesas) {
		this.defesas = defesas;
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
	
	public char getTipoPeca() {
		return this.peca.getTipoPeca();
	}
	
	//
	// Overrrides úteis
	//
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Casa(this.getCoordenadaX(), this.getCoordenadaY(), this.getPeca().getTipoPeca());
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
	
	
	//
	// Logica
	//
	
	public void calculaAmeacasEDefesas(Tabuleiro tabuleiro, boolean vezDasBrancas, ArrayList<Casa> origensDosAtaques, ArrayList<Casa> origensDasDefesas ) {
		Casa casaAux;
		Peca pecaAux;
		Tabuleiro tabuleiroAux = tabuleiro.copiaTabuleiro();
		Stack<Casa> casasComPecas = tabuleiro.casasComPecasDestaCor(vezDasBrancas);
		Stack<Casa>	range;
	
		this.ameacas = 0;
		this.defesas = 0;
		origensDasDefesas.clear();
		origensDosAtaques.clear();
		casasComPecas.addAll(tabuleiro.casasComPecasDestaCor(!vezDasBrancas));
		// Remove esta casa da lista para ela nao ser avaliada
		casasComPecas.remove(this);
		
		while(!casasComPecas.isEmpty()) {
			casaAux = casasComPecas.pop();
			pecaAux = casaAux.getPeca();			
			
			// Se for peca da mesma cor, monta a lista de defesa
			if(vezDasBrancas == pecaAux.isBranca()) {
				// Faz de conta no tabuleiro auxiliar que nesta casa não tem peça
				tabuleiroAux.setPecaEm(this.getCoordenadaX(), this.getCoordenadaY(), ' ');
				range = pecaAux.ameaca(tabuleiroAux, casaAux, vezDasBrancas);
				if (range.contains(this)) {
					origensDasDefesas.add(casaAux.copiaCasa());
					this.defesas++; 
				}
				tabuleiroAux = tabuleiro.copiaTabuleiro();
			}
			// Se for peca de cor diferente, monta a lista de sob ameaca
			else {
				range = pecaAux.ameaca(tabuleiro, casaAux, !vezDasBrancas);
				if (range.contains(this)) {
					origensDosAtaques.add(casaAux.copiaCasa());
					this.ameacas++; 
				}
			}
		}
	}
	
	public Casa copiaCasa() {
		try {
			return (Casa) this.clone();
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
