package codigo;

import java.util.*;

public class Jogador {
	private boolean isHumano;
	private boolean isBrancas;
	public String nome;
	
	public Jogador() {
		this.isHumano = true;
		this.isBrancas = true;
	}
	
	public Jogador(boolean isHumano, boolean isBrancas) {
		this.isHumano = isHumano;
		this.isBrancas = isBrancas;
	}
	
	public Jogador(boolean isHumano, boolean isBrancas, String nome) {
		this.isHumano = isHumano;
		this.isBrancas = isBrancas;
		this.nome = nome;
	}

	public boolean isHumano() {
		return isHumano;
	}

	public void setHumano(boolean isHumano) {
		this.isHumano = isHumano;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isBrancas() {
		return isBrancas;
	}

	public void setBrancas(boolean isBrancas) {
		this.isBrancas = isBrancas;
	}

	public int coletarJogada (Tabuleiro tabuleiro, Jogada jogada, Jogada ultimaJogada) {
		if (this.isHumano)
			return jogada.coletarJogada(tabuleiro);
		else {
			Jogada jogadaAux = new Jogada();
			jogadaAux = this.pensar(tabuleiro, ultimaJogada);
			jogada.setCasaOrigem(jogadaAux.origem);
			jogada.setCasaDestino(jogadaAux.destino);
			jogada.setDeslocamentoX(jogadaAux.getDeslocamentoX());
			jogada.setDeslocamentoY(jogadaAux.getDeslocamentoY());
			return 1;
		}
	}

	/**
	 * Método que deve ser sobreescrito por alguma classe de IA com seu próprio algoritmo.
	 * @param tabuleiro o tabuleiro atual;
	 * @param ultimaJogada a última jogada que foi feita;
	 * @return a jogada escolhida pela inteligência artificial
	 */
	protected Jogada pensar(Tabuleiro tabuleiro, Jogada ultimaJogada) {
		return new Jogada();
	}
	
	protected ArrayList<Jogada> lancesPotenciais(Tabuleiro tabuleiro, Jogada ultimaJogada) {
		char tipoPecaAux = ' ';
		Casa casaAux = new Casa();
		Tabuleiro tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();
		PecaRei rei = new PecaRei (this.isBrancas ? 'R' : 'r');
		Stack<Casa> pecasDisponiveis = new Stack<Casa> ();
		Stack<Casa> destinos = new Stack<Casa> ();
		ArrayList<Jogada> lancesPotenciais = new ArrayList<Jogada> ();
		ArrayList<Jogada> lancesPossiveis = new ArrayList<Jogada> ();
		
		// Criar uma pilha com todas as casas que tem pecas da cor do jogador
		pecasDisponiveis = tabuleiro.casasComPecasDestaCor(this.isBrancas);
		
		// Cria uma pilha com todos os movimentos possiveis
		while (!pecasDisponiveis.isEmpty()) {
			casaAux = pecasDisponiveis.pop();
			destinos = casaAux.peca.movimentosValidos(tabuleiro, casaAux, ultimaJogada, this.isBrancas);
			while (!destinos.isEmpty()) {
				lancesPotenciais.add(new Jogada (casaAux, destinos.pop()));
			}
		}
		
		// Cria uma copia do tabuleiro para simulacoes
		/*for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {				
				tabuleiroAuxiliar.casas[i][j] = tabuleiro.casas[i][j];
			}
		}*/
		
		// Percorre a lista de movimentos possíveis e remove os que deixariam o rei em xeque
		for (Jogada jogadaAux : lancesPotenciais) {
			tipoPecaAux = tabuleiroAuxiliar.getPecaEm(jogadaAux.origem.getCoordenadaX(), jogadaAux.origem.getCoordenadaY());
						
			// Simula a jogada em um tabuleiro auxiliar
			tabuleiroAuxiliar.atualizarTabuleiro(jogadaAux);
			
			// Verifica se a jogada nao deixa em xeque
			if (!rei.isEmXeque(tabuleiroAuxiliar)) {
				lancesPossiveis.add(new Jogada(jogadaAux.origem, jogadaAux.destino, tipoPecaAux));
			}
			
			// Retorna o tabuleiro auxiliar para a posição para a próxima simulação
			tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();			
		}
		
		
		
		return lancesPossiveis;		
	}
}
