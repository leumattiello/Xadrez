package codigo;

import java.util.*;

public class Jogador {
	private boolean isHumano;
	private boolean isBrancas;
	private String nome;
	
	
	//
	// Construtores
	//
	
	public Jogador() {
		this.isHumano = true;
		this.isBrancas = true;
		this.nome = new String("");
	}
	
	public Jogador(boolean isHumano, boolean isBrancas) {
		this.isHumano = isHumano;
		this.isBrancas = isBrancas;
		this.nome = new String("");
	}
	
	public Jogador(boolean isHumano, boolean isBrancas, String nome) {
		this.isHumano = isHumano;
		this.isBrancas = isBrancas;
		this.nome = nome;
	}

	
	//
	// Setters e Getters
	//
	
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

	
	//
	// Overrides úteis
	//
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Jogador(this.isHumano(), this.isBrancas(), this.getNome());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Jogador))
			return false;
		
		Jogador tmp = (Jogador) obj;
		
		return this.isHumano() == tmp.isHumano() &&
			   this.isBrancas() == tmp.isBrancas() &&
			   this.nome.equals(tmp.getNome());
	}

	@Override
	public String toString() {
		String str = new String("");
		
		str += this.isBrancas() ? "Brancas: " : "Pretas: ";
		str += this.getNome();
		
		return str;
	}	
	
	
	//
	// Lógica
	//

	public int coletarJogada (Tabuleiro tabuleiro, Jogada jogada, Jogada ultimaJogada) {
		if (this.isHumano)
			return jogada.coletarJogada(tabuleiro);
		else {
			// Verifica pedido de empate
			if(tabuleiro.casas[1][1] == 'E') {
				return 5; // Sempre rejeita por padrao
						  // Na subclasse ele verifica
			}
			else { // Jogada normal
				Jogada jogadaAux = new Jogada();
				jogadaAux = this.pensar(tabuleiro, ultimaJogada);
				jogada.setCasaOrigem(jogadaAux.getCasaOrigem());
				jogada.setCasaDestino(jogadaAux.getCasaDestino());
				jogada.setDeslocamentoX(jogadaAux.getDeslocamentoX());
				jogada.setDeslocamentoY(jogadaAux.getDeslocamentoY());
				jogada.setValida(true); // Validade verificada durante formação da jogada 
				return 1;
			}
		}
	}

	/**
	 * Método que deve ser sobreescrito por alguma classe de IA com seu próprio algoritmo.
	 * @param tabuleiro o tabuleiro atual;
	 * @param ultimaJogada a última jogada que foi feita;
	 * @return a jogada escolhida pela inteligência artificial
	 */
	protected Jogada pensar(Tabuleiro tabuleiro, Jogada ultimaJogada) {
		// Deve ser implementado na subclasse
		return new Jogada();
	}
	
	protected ArrayList<Prioridade> lancesPotenciais(Tabuleiro tabuleiro, Jogada ultimaJogada) {
		char tipoPecaAux = ' ';
		PecaRei rei = new PecaRei (this.isBrancas ? 'R' : 'r');
		Casa casaAux = new Casa();
		Tabuleiro tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();
		Stack<Casa> pecasDisponiveis = new Stack<Casa> ();
		Stack<Casa> destinos = new Stack<Casa> ();
		ArrayList<Prioridade> lancesPotenciais = new ArrayList<Prioridade> ();
		ArrayList<Prioridade> lancesPossiveis = new ArrayList<Prioridade> ();
		
		// Criar uma pilha com todas as casas que tem pecas da cor do jogador
		pecasDisponiveis = tabuleiro.casasComPecasDestaCor(this.isBrancas);
		
		// Cria uma pilha com todos os movimentos possiveis
		while (!pecasDisponiveis.isEmpty()) {
			casaAux = pecasDisponiveis.pop();
			destinos = casaAux.getPeca().movimentosValidos(tabuleiro, casaAux, ultimaJogada, this.isBrancas);
			while (!destinos.isEmpty()) {
				lancesPotenciais.add(new Prioridade (casaAux, destinos.pop()));
			}
		}
		
		// Percorre a lista de movimentos possíveis e remove os que deixariam o rei em xeque
		for (Jogada jogadaAux : lancesPotenciais) {
			tipoPecaAux = tabuleiroAuxiliar.getPecaEm(jogadaAux.getCasaOrigem().getCoordenadaX(), jogadaAux.getCasaOrigem().getCoordenadaY());
						
			// Simula a jogada em um tabuleiro auxiliar
			tabuleiroAuxiliar.atualizarTabuleiro(jogadaAux);
			
			// Verifica se a jogada nao deixa em xeque
			if (!rei.isEmXeque(tabuleiroAuxiliar, false)) {
				lancesPossiveis.add(new Prioridade(jogadaAux.getCasaOrigem(), jogadaAux.getCasaDestino(), tipoPecaAux));
			}
			
			// Retorna o tabuleiro auxiliar para a posição para a próxima simulação
			tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();			
		}		
		
		return lancesPossiveis;		
	}
}
