package codigo;

import java.util.*;

public class Peca {
	protected char tipoPeca;
	protected boolean cor; // TRUE = brancas, FALSE = pretas
	protected int valorHeuristicoPadrao;
	
	// Construtores
	public Peca() {
		new Peca('P');
	}
	
	public Peca(char tipoPeca) {
		// Tipo da Peca
		setTipoPeca(tipoPeca); // Cor e Valor Heuristico sao setados dentro do metodo setTipoPeca
	}

	// Metodos Setters e Getters
	public char getTipoPeca() {
		return this.tipoPeca;
	}

	public void setTipoPeca(char tipoPeca) {
		this.tipoPeca = tipoPeca;
		
		// Cor da Peca
		if (isPecaPreta())
			this.cor = false;
		else
			this.cor = true;
		
		// ValorHeuristicoPadrao da Peca
		setValorHeuristicoPadrao(0);
		if (isPeao())
			setValorHeuristicoPadrao(1);
		else if (isCavalo())
			setValorHeuristicoPadrao(3);
		else if (isBispo())
			setValorHeuristicoPadrao(3);
		else if (isTorre())
			setValorHeuristicoPadrao(5);
		else if (isDama())
			setValorHeuristicoPadrao(10);
		else if (isRei())
			setValorHeuristicoPadrao(100);
	}

	public boolean isBranca() {
		return cor;
	}

	public void setCor(boolean cor) {
		this.cor = cor;
	}

	public int getValorHeuristicoPadrao() {
		return valorHeuristicoPadrao;
	}

	public void setValorHeuristicoPadrao(int valorHeuristicoPadrao) {
		this.valorHeuristicoPadrao = valorHeuristicoPadrao;
	}

	// Logica
	public boolean isMovimentoValido(Jogada jogada, Tabuleiro tabuleiro, boolean vezDasBrancas) {
		// Implementada nas subclasses atraves de polimorfismo		
		return false;
	}
	
	/**
	 * Este metodo verifica quais casas estarao no range de ataque da peca, estando ela na casa indicada.
	 * O retorno deste metodo tambem pode ser interpretado como uma lista de movimentos potencialmente validos (desconsiderando xeque).
	 * @param tabuleiro - para saber o contexto atual do jogo
	 * @param jogada - para saber onde a peca esta se movendo
	 * @param vezDasBrancas - para saber quem efetuou a jogada
	 * @return uma pilha de Casas com as casas ameacadas. 
	 * 			Naturalmente as casas ameacadas somente podem ser da cor oposta a peca ameacante, ou entao vazias (' ').
	 */
	public Stack<Casa> ameaca(Tabuleiro tabuleiro, Casa casa, boolean vezDasBrancas) {
		// Implementada nas subclasses atraves de polimorfismo
		Stack<Casa> casasAmeacadas = new Stack<Casa>();
		return casasAmeacadas;
	}
	
	/**
	 * 
	 * @param tabuleiro
	 * @return A quantidade de pecas do oponente que estao atacando esta casa
	 */
	public int sobAmeaca(Tabuleiro tabuleiro, Casa casa) {
		Stack<Casa> casasComPecasDoOponente = tabuleiro.casasComPecasDestaCor(!this.isBranca());
		Stack<Casa> ameacas = new Stack<Casa> ();
		Casa casaAux;
		Peca pecaAux;
		int qtdDeAmeacas = 0;
		
		while(!casasComPecasDoOponente.isEmpty()) {
			casaAux = casasComPecasDoOponente.pop();
			pecaAux = casaAux.getPeca();
			ameacas = pecaAux.ameaca(tabuleiro, casaAux, pecaAux.isBranca());
			if (ameacas.contains(casa))
				qtdDeAmeacas++;
		}
		
		return qtdDeAmeacas;
	}
	
	public Stack<Casa> movimentosValidos (Tabuleiro tabuleiro, Casa casa, Jogada ultimaJogada, Boolean vezDasBrancas){
		return this.ameaca(tabuleiro, casa, vezDasBrancas);	
	}
	
	// Metodos auxiliares
	public boolean isPeca() {
		if (getTipoPeca() == 'p' || getTipoPeca() == 'c' || getTipoPeca() == 'b' || getTipoPeca() == 't' || getTipoPeca() == 'd' || getTipoPeca() == 'r' ||
			getTipoPeca() == 'P' || getTipoPeca() == 'C' || getTipoPeca() == 'B' || getTipoPeca() == 'T' || getTipoPeca() == 'D' || getTipoPeca() == 'R')
			return true;
		return false;
	}
	
	public boolean isPeca(char peca) {
		if (peca == 'p' || peca == 'c' || peca == 'b' || peca == 't' || peca == 'd' || peca == 'r' ||
			peca == 'P' || peca == 'C' || peca == 'B' || peca == 'T' || peca == 'D' || peca == 'R')
			return true;
		return false;
	}
	
	public boolean isPecaBranca() {
		return (getTipoPeca() == 'P' || getTipoPeca() == 'C' || getTipoPeca() == 'B' || getTipoPeca() == 'T' || getTipoPeca() == 'D' || getTipoPeca() == 'R');
	}
	
	public boolean isPecaBranca(char peca) {
		return (peca == 'P' || peca == 'C' || peca == 'B' || peca == 'T' || peca == 'D' || peca == 'R');
	}
	
	public boolean isPecaPreta() {
		return (getTipoPeca() == 'p' || getTipoPeca() == 'c' || getTipoPeca() == 'b' || getTipoPeca() == 't' || getTipoPeca() == 'd' || getTipoPeca() == 'r');
	}
	
	public boolean isPecaPreta(char peca) {
		return (peca == 'p' || peca == 'c' || peca == 'b' || peca == 't' || peca == 'd' || peca == 'r');
	}
	
	public boolean isPeao() {
		return (getTipoPeca() == 'P' || getTipoPeca() == 'p');
	}
	
	public boolean isCavalo() {
		return (getTipoPeca() == 'C' || getTipoPeca() == 'c');
	}
		
	public boolean isBispo() {
		return (getTipoPeca() == 'B' || getTipoPeca() == 'b');
	}
	
	public boolean isTorre() {
		return (getTipoPeca() == 'T' || getTipoPeca() == 't');
	}
	
	public boolean isDama() {
		return (getTipoPeca() == 'D' || getTipoPeca() == 'd');
	}
	
	public boolean isRei() {
		return (getTipoPeca() == 'R' || getTipoPeca() == 'r');
	}
		
}
