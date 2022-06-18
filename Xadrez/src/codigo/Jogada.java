package codigo;
import java.util.*;

public class Jogada {
	public Casa origem;
	public Casa destino;
	private int deslocamentoX;
	private int deslocamentoY;
	private boolean coordenadasValidas;
	
	// Construtor
	public Jogada() {
		this.origem = new Casa();
		this.destino = new Casa();
		this.setDeslocamentoX(0);
		this.setDeslocamentoY(0);
		this.coordenadasValidas = false;
	}
	
	public Jogada(Casa origem, Casa destino) {
		this.origem = new Casa(origem.getCoordenadaX(), origem.getCoordenadaY(), origem.peca.getTipoPeca());
		this.destino = new Casa(destino.getCoordenadaX(), destino.getCoordenadaY(), destino.peca.getTipoPeca());
		this.calculaDeslocamentoX(origem.getCoordenadaX(), destino.getCoordenadaX());
		this.calculaDeslocamentoY(origem.getCoordenadaY(), destino.getCoordenadaY());
		this.coordenadasValidas = false;
	}
	
	public Jogada(Casa origem, Casa destino, char pecaOrigem) {
		this.origem = new Casa(origem.getCoordenadaX(), origem.getCoordenadaY(), origem.peca.getTipoPeca());
		this.destino = new Casa(destino.getCoordenadaX(), destino.getCoordenadaY(), destino.peca.getTipoPeca());
		this.origem.setPeca(pecaOrigem);
		this.calculaDeslocamentoX(origem.getCoordenadaX(), destino.getCoordenadaX());
		this.calculaDeslocamentoY(origem.getCoordenadaY(), destino.getCoordenadaY());
		this.coordenadasValidas = false;
	}
	
	// Setters e getters
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
	
	public Peca getPecaOrigem() {
		return this.origem.getPeca();
	}
	
	public char getTipoPecaOrigem() {
		return this.origem.getPeca().tipoPeca;
	}

	// Logica
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
	
	// Metodos Auxiliares
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
		
		if (isColuna(bruta.charAt(0)))
			origem.setCoordenadaX(bruta.charAt(0));
		else {
			System.out.println("A casa de origem deve ser um valor entre 'a1' e 'h8'");
			return false;
		}
		
		if (isFileira(Integer.parseInt(bruta.substring(1, 2))))
			origem.setCoordenadaY(Integer.parseInt(bruta.substring(1, 2)));
		else {
			System.out.println("A casa de origem deve ser um valor entre 'a1' e 'h8'");
			return false;
		}
		
		if (bruta.charAt(2) != '-') {
			System.out.println("O caracter '-' deve ser utilizado como separador");
			return false;
		}	
		
		if (isColuna(bruta.charAt(3)))
			destino.setCoordenadaX(bruta.charAt(3));
		else {
			System.out.println("A casa de destino deve ser um valor entre 'a1' e 'h8'");
			return false;
		}
		
		if (isFileira(Integer.parseInt(bruta.substring(4))))
			destino.setCoordenadaY(Integer.parseInt(bruta.substring(4)));
		else {
			System.out.println("A casa de destino deve ser um valor entre 'a1' e 'h8'");
			return false;
		}
		
		if (origem.getCoordenadaX() == destino.getCoordenadaX() &&
			origem.getCoordenadaY() == destino.getCoordenadaY()) {
			System.out.println("Casa de origem e destino nao podem ser iguais!");
			return false;
		}			
		
		// Se chegou ate aqui, eh porque coordenadas de origem e destino sao validas.
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
		
		pecaTmp = tabuleiro.getPecaEm(this.destino.getCoordenadaX(), this.destino.getCoordenadaY());
		this.destino.setPeca(pecaTmp);
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
	
	public boolean equalsTo(Jogada jogada) {
		if (this.origem.peca.getTipoPeca() != jogada.origem.peca.getTipoPeca())
			return false;
		if (this.origem.getCoordenadaX() != jogada.origem.getCoordenadaX())
			return false;
		if (this.origem.getCoordenadaY() != jogada.origem.getCoordenadaY())
			return false;
		if (this.destino.peca.getTipoPeca() != jogada.destino.peca.getTipoPeca())
			return false;
		if (this.destino.getCoordenadaX() != jogada.destino.getCoordenadaX())
			return false;
		if (this.destino.getCoordenadaY() != jogada.destino.getCoordenadaY())
			return false;		
		return true;
	}
}
