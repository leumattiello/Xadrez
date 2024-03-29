package codigo;
import java.util.*;

public class Jogada {
	private boolean coordenadasValidas;
	private boolean isEnPassant;
	private boolean isRoque;
	private boolean isRoqueGrande;
	private boolean isCoroacao;
	private boolean isBrancas;
	private char coroada;
	private int deslocamentoX;
	private int deslocamentoY;
	private Casa origem;
	private Casa destino;	
	
	
	//
	// Construtores
	//
	
	public Jogada() {
		this.isEnPassant = false;
		this.isRoque = false;
		this.isRoqueGrande = false;
		this.isCoroacao = false;
		this.isBrancas = true;
		this.coroada = 'P';
		this.origem = new Casa('a', 6);
		this.destino = new Casa('b', 8);
		this.setDeslocamentoX(0);
		this.setDeslocamentoY(0);
		this.coordenadasValidas = false;
	}
	
	public Jogada(Casa origem, Casa destino) {
		// Marcacao secreta na jogada no campo Defesas da casa
		int marca = destino.getDefesas();
		
		this.origem = new Casa(origem.getCoordenadaX(), origem.getCoordenadaY(), origem.getTipoPeca());
		this.destino = new Casa(destino.getCoordenadaX(), destino.getCoordenadaY(), destino.getTipoPeca());
		this.calculaDeslocamentoX(origem.getCoordenadaX(), destino.getCoordenadaX());
		this.calculaDeslocamentoY(origem.getCoordenadaY(), destino.getCoordenadaY());
		this.coordenadasValidas = false;
		
		marcarLancesEspeciais(marca);
	}
	
	public Jogada(Casa origem, Casa destino, char pecaOrigem) {
		// Marcacao secreta na jogada no campo Defesas da casa
		int marca = destino.getDefesas();				
		
		this.origem = new Casa(origem.getCoordenadaX(), origem.getCoordenadaY(), origem.getTipoPeca());
		this.destino = new Casa(destino.getCoordenadaX(), destino.getCoordenadaY(), destino.getTipoPeca());
		this.origem.setPeca(pecaOrigem);
		this.calculaDeslocamentoX(origem.getCoordenadaX(), destino.getCoordenadaX());
		this.calculaDeslocamentoY(origem.getCoordenadaY(), destino.getCoordenadaY());
		this.coordenadasValidas = false;
		
		marcarLancesEspeciais(marca);
	}
	
	
	//
	// Setters e getters
	//
	
	public int getDeslocamentoX() {
		return deslocamentoX;
	}

	public void setDeslocamentoX(int deslocamentoX) {
		this.deslocamentoX = deslocamentoX;
	}

	public int getDeslocamentoY() {
		return deslocamentoY;
	}

	public void setDeslocamentoY(int deslocamentoY) {
		this.deslocamentoY = deslocamentoY;
	}

	public boolean isCoordenadasValidas() {
		return coordenadasValidas;
	}

	public void setValida(boolean coordenadasValidas) {
		this.coordenadasValidas = coordenadasValidas;
	}
	
	public Casa getCasaOrigem() {
		return this.origem;
	}
	
	public void setCasaOrigem(Casa casa) {
		this.origem = casa;
	}
	
	public Casa getCasaDestino() {
		return this.destino;
	}
	
	public void setCasaDestino(Casa casa) {
		this.destino = casa;
	}
	
	public boolean isEnPassant() {
		return isEnPassant;
	}

	public void setEnPassant(boolean isEnPassant) {
		this.isEnPassant = isEnPassant;
	}

	public boolean isRoque() {
		return isRoque;
	}

	public void setRoque(boolean isRoque) {
		this.isRoque = isRoque;
	}

	public boolean isRoqueGrande() {
		return isRoqueGrande;
	}

	public void setRoqueGrande(boolean isRoqueGrande) {
		this.isRoqueGrande = isRoqueGrande;
	}

	public boolean isCoroacao() {
		return isCoroacao;
	}

	public void setCoroacao(boolean isCoroacao) {
		this.isCoroacao = isCoroacao;
	}

	public boolean isBrancas() {
		return isBrancas;
	}

	public void setBrancas(boolean isBrancas) {
		this.isBrancas = isBrancas;
	}

	public char getCoroada() {
		return coroada;
	}

	public void setCoroada(char coroada) {
		this.coroada = coroada;
	}

	public Peca getPecaOrigem() {
		return this.origem.getPeca();
	}
	
	public char getTipoPecaOrigem() {
		return this.origem.getPeca().tipoPeca;
	}

	
	//
	// Overrides Úteis
	//
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Jogada j = new Jogada(this.origem, this.destino, this.getTipoPecaOrigem());
		
		j.setBrancas(this.isBrancas);
		j.setCoroacao(this.isCoroacao);
		j.setCoroada(this.coroada);
		j.setEnPassant(this.isEnPassant);
		j.setRoque(this.isRoque);
		j.setRoqueGrande(this.isRoqueGrande);
		j.setValida(this.coordenadasValidas);
				
		return j;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Jogada))
			return false;
		
		Jogada tmp = (Jogada) obj;
		
		return this.origem.equals(tmp.getCasaOrigem()) &&
			   this.getCasaDestino().equals(tmp.getCasaDestino());
	}

	@Override
	public String toString() {
		String str = new String("");
		
		str += this.getCasaOrigem().toString();
		str += "-";
		str += this.getCasaDestino().toString();
		
		return str;
	}	
	
	
	//
	// Logica
	//
	
	/**
	 * @param tabuleiro - precisa de informacoes do tabuleiro para parsear a jogada
	 * @return 
	 */
	public int coletarJogada(Tabuleiro tabuleiro) {
		String tmp = new String("");
		Scanner leituraDaJogada = new Scanner(System.in);
		
		while (tmp.equals("")) {
			tmp = leituraDaJogada.nextLine();
		}		
		
		// Verifica se jogador atual desistiu
		if (tmp.startsWith("desistir")) {
			//leituraDaJogada.close();
			return 2;
		}		
		if (tmp.startsWith("empate")) {
			//leituraDaJogada.close();
			return 3;
		}		
		if (tmp.startsWith("aceitar")) {
			//leituraDaJogada.close();
			return 4;
		}		
		if (tmp.startsWith("rejeitar")) {
			//leituraDaJogada.close();
			return 5;
		}
		
		// Parseia jogada, se jogada for potencialmente valida, inicializa as pecas nas casas
		if (parsearJogada(tmp)) {
			this.coordenadasValidas = true;
			inicializaCasas(tabuleiro);
		}
		else {
			this.coordenadasValidas = false;
		}
		
		return 1;
	}
	
	/**
	 * @param bruta
	 * @return true se coordenadas de origem e destino estiverem OK, false caso contrario
	 */
	private boolean parsearJogada(String bruta) {
		try {		
			if (bruta.length() < 5) {
				System.out.println("Sintaxe do lance incorreta.");
				return false;
			}			
			if (isColuna(bruta.charAt(0))) {
				origem.setCoordenadaX(bruta.charAt(0));
			}
			else {
				System.out.println("A casa de origem deve ser um valor entre 'a1' e 'h8'");
				return false;
			}			
			if (isFileira(Integer.parseInt(bruta.substring(1, 2)))) {
				origem.setCoordenadaY(Integer.parseInt(bruta.substring(1, 2)));
			}
			else {
				System.out.println("A casa de origem deve ser um valor entre 'a1' e 'h8'");
				return false;
			}			
			if (bruta.charAt(2) != '-') {
				System.out.println("O caracter '-' deve ser utilizado como separador");
				return false;
			}			
			if (isColuna(bruta.charAt(3))) {
				destino.setCoordenadaX(bruta.charAt(3));
			}
			else {
				System.out.println("A casa de destino deve ser um valor entre 'a1' e 'h8'");
				return false;
			}			
			if (isFileira(Integer.parseInt(bruta.substring(4)))) {
				destino.setCoordenadaY(Integer.parseInt(bruta.substring(4)));
			}
			else {
				System.out.println("A casa de destino deve ser um valor entre 'a1' e 'h8'");
				return false;
			}			
			if (origem.getCoordenadaX() == destino.getCoordenadaX() &&
				origem.getCoordenadaY() == destino.getCoordenadaY()) {
				System.out.println("Casa de origem e destino nao podem ser iguais!");
				return false;
			}			
			
			// Se chegou ate aqui, é porque coordenadas de origem e destino sao validas.
			// Portanto calcular e setar os deslocamento.
			setDeslocamentoX(destino.getCoordenadaX() - origem.getCoordenadaX());
			setDeslocamentoY(destino.getCoordenadaY() - origem.getCoordenadaY());
			
			return true;
		}
		catch (Exception e) {
			System.out.println("Sintaxe do lance incorreta.");
			return false;
		}
	}

	/**
	 * @param tabuleiro - metodo precisa consultar tabuleiro para ver qual peca esta naquela casa
	 */
	private void inicializaCasas(Tabuleiro tabuleiro) {
		char pecaTmp = tabuleiro.getPecaEm(this.origem.getCoordenadaX(), this.origem.getCoordenadaY());	
		this.origem.setPeca(pecaTmp);
		
		pecaTmp = tabuleiro.getPecaEm(this.getCasaDestino().getCoordenadaX(), this.getCasaDestino().getCoordenadaY());
		this.getCasaDestino().setPeca(pecaTmp);
	}
	
	public Jogada copiaJogada() {
		try {			
			return (Jogada)this.clone();		
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;		
		}
	}
	
	private boolean isColuna(char c) {
		return (c >= 'a' && c <= 'h');
	}
	
	private boolean isFileira(int i) {
		return (i >= 1 && i <= 8);
	} 
	
	public int calculaDeslocamentoX(char origem, char destino) {
		this.deslocamentoX = Character.getNumericValue(destino) - Character.getNumericValue(origem);
		return this.deslocamentoX;
	}
	
	public int calculaDeslocamentoY(int origem, int destino) {
		this.deslocamentoY = destino - origem;
		return this.deslocamentoY;
	}
	
	private void marcarLancesEspeciais(int marca) {		
		switch(marca) {
			case 80: { // Coroação
				this.setCoroacao(true);
				this.setCoroada(this.isBrancas() ? 'D' : 'd');
				break;
			}
			case 90: { // En Passant
				this.setEnPassant(true);
				break;
			}
			case 100: { // Roque
				this.setRoque(true);
				break;
			}
			case 1000: { // Roque grande
				this.setRoqueGrande(true);
				break;
			}
		}		
	}
}
