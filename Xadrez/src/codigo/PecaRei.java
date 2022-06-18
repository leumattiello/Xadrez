package codigo;

import java.util.*;

public class PecaRei extends Peca {
	public boolean jaMoveu;
	public boolean roque;
	public boolean roqueGrande;
	public boolean xeque;
	public boolean xequeMate;
	
	// Construtores
	public PecaRei () {
		new PecaRei('R');
	}
	
	public PecaRei(char tipoPeca) {
		super(tipoPeca);
		this.jaMoveu = false;
		this.roque = false;
		this.roqueGrande = false;
		this.xeque = false;
		this.xequeMate = false;
	}

	// Logica
	@Override
	public boolean isMovimentoValido(Jogada jogada, Tabuleiro tabuleiro, boolean vezDasBrancas) {
		int deslocamentoAbsolutoX = jogada.getDeslocamentoX() > 0 ? jogada.getDeslocamentoX() : -1*jogada.getDeslocamentoX();
		int deslocamentoAbsolutoY = jogada.getDeslocamentoY() > 0 ? jogada.getDeslocamentoY() : -1*jogada.getDeslocamentoY();

		// Movimento do rei sera valido se for exatamente 1 casa em qualquer direcao
		if (deslocamentoAbsolutoX == 1)
			if (deslocamentoAbsolutoY == 1 || deslocamentoAbsolutoY == 0)
				return true;
		if (deslocamentoAbsolutoX == 0)
			if (deslocamentoAbsolutoY == 1)
				return true;
		
		return false;
	}
	
	@Override
	public Stack<Casa> ameaca(Tabuleiro tabuleiro, Casa casa, boolean vezDasBrancas) {
		Stack<Casa> casasAmeacadas = new Stack<Casa>();
		Peca pecaAux = new Peca();
		int intFromCharAux = 0;
		char coordX = 'a';
		int coordY = 1; // Lembrando que sera utilizado de 1 a 8
		
		// No caso do rei, consultar individualmente cada uma das 8 casas adjacentes.
		// Comecando pela casa a frente e a direita, e progredindo em sentido anti-horario
		
		// Frente e direita
		intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
		coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux + 1);
		coordY = casa.getCoordenadaY() + 1;
		if ('a' <= coordX && coordX <= 'h') {
			if (1 <= coordY && coordY <= 8) {			
				pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
				if (this.isBranca() && !pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
				if (!this.isBranca() && !pecaAux.isPecaPreta())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
			}
		}

		// Frente
		intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
		coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux);
		coordY = casa.getCoordenadaY() + 1;
		if ('a' <= coordX && coordX <= 'h') {
			if (1 <= coordY && coordY <= 8) {
				pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
				if (this.isBranca() && !pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
				if (!this.isBranca() && !pecaAux.isPecaPreta())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
			}
		}

		// Frente e esquerda
		intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
		coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux - 1);
		coordY = casa.getCoordenadaY() + 1;
		if ('a' <= coordX && coordX <= 'h') {
			if (1 <= coordY && coordY <= 8) {
				pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
				if (this.isBranca() && !pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
				if (!this.isBranca() && !pecaAux.isPecaPreta())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
			}
		}

		// Esquerda
		intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
		coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux - 1);
		coordY = casa.getCoordenadaY();
		if ('a' <= coordX && coordX <= 'h') {
			if (1 <= coordY && coordY <= 8) {
				pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
				if (this.isBranca() && !pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
				if (!this.isBranca() && !pecaAux.isPecaPreta())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
			}
		}

		// Tras e esquerda
		intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
		coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux - 1);
		coordY = casa.getCoordenadaY() - 1;
		if ('a' <= coordX && coordX <= 'h') {
			if (1 <= coordY && coordY <= 8) {
				pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
				if (this.isBranca() && !pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
				if (!this.isBranca() && !pecaAux.isPecaPreta())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
			}
		}

		// Tras
		intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
		coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux);
		coordY = casa.getCoordenadaY() - 1;
		if ('a' <= coordX && coordX <= 'h') {
			if (1 <= coordY && coordY <= 8) {
				pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
				if (this.isBranca() && !pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
				if (!this.isBranca() && !pecaAux.isPecaPreta())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
			}
		}

		// Tras e direita
		intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
		coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux + 1);
		coordY = casa.getCoordenadaY() - 1;
		if ('a' <= coordX && coordX <= 'h') {
			if (1 <= coordY && coordY <= 8) {
				pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
				if (this.isBranca() && !pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
				if (!this.isBranca() && !pecaAux.isPecaPreta())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
			}
		}

		// Direita
		intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
		coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux + 1);
		coordY = casa.getCoordenadaY();
		if ('a' <= coordX && coordX <= 'h') {
			if (1 <= coordY && coordY <= 8) {
				pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
				if (this.isBranca() && !pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
				if (!this.isBranca() && !pecaAux.isPecaPreta())
					casasAmeacadas.push(new Casa(coordX,coordY, pecaAux.getTipoPeca()));
			}
		}
		
		return casasAmeacadas;
	}
	
	/**
	 * Este metodo teve de ser sobreposto, pois no caso do rei tem as possibilidades de roque.
	 */
	@Override
	public Stack<Casa> movimentosValidos (Tabuleiro tabuleiro, Casa casa, Jogada ultimaJogada, Boolean vezDasBrancas){
		Stack<Casa> movimentosValidos = new Stack<Casa>();
		Jogada jogadaAux = new Jogada();
		char x = 'a';
		int y = 1;
		
		// Pegue todas as casas ameacadas por esta peca
		movimentosValidos = this.ameaca(tabuleiro, casa, vezDasBrancas);

		// Testar as possibilidades de roque e roque grande
		x = 'g';
		y = this.isBranca() ? 1 : 8;		
		jogadaAux.origem = casa;
		jogadaAux.destino.setCoordenadaX(x);
		jogadaAux.destino.setCoordenadaY(y);
		if (this.verificarRoque(tabuleiro, jogadaAux)) {
			movimentosValidos.push(new Casa (x, y));
		}
		x = 'c';
		jogadaAux.destino.setCoordenadaX(x);
		if (this.verificarRoqueGrande(tabuleiro, jogadaAux)) {
			movimentosValidos.push(new Casa (x, y));
		}		
		
		return movimentosValidos;
	}
	
	/**
	 * @param tabuleiro
	 * @return true se esta em xeque, false caso contrario
	 */
	public boolean isEmXeque(Tabuleiro tabuleiro) {
		Stack<Casa> pilhaDeCasas = new Stack<Casa>();
		Stack<Casa> casasAmeacadas = new Stack<Casa>();
		Casa casaAux = new Casa();
		Peca pecaAux = new Peca();
		
		// Primeiramente, criar uma pilha com todas as casas que possuem peca do adversario
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				pecaAux.setTipoPeca(tabuleiro.casas[i][j]);
				if (pecaAux.isPeca()) // Se tem alguma peca naquela casa
					if (this.isBranca() != pecaAux.isBranca()) { // Se a peca nao eh da mesma cor
						// Nesse caso, tem que empilhar a casa
						pilhaDeCasas.push(new Casa(tabuleiro.indexParaCoordenadaX(j), tabuleiro.indexParaCoordenadaY(i), pecaAux.getTipoPeca()));
					}
			}	
		}
		
		// Desempilhar a pilha de casas, verificando se cada uma das pecas ameaca o rei
		while (!pilhaDeCasas.isEmpty()) {
			casaAux = pilhaDeCasas.pop();
			casaAux.setPeca(casaAux.peca.getTipoPeca());
			casasAmeacadas = casaAux.peca.ameaca(tabuleiro, casaAux, !this.isBranca());
			if (estaSendoAmeacado(casasAmeacadas)){
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Metodo que avalia, dada a configuracao atual do tabuleiro, se quem esta jogando esta em xeque-mate ou nao.
	 * Este algoritmo de verificacao de xeque mate tem o seguinte fundamento.
	 * Antes de mais nada, caso nao seja xeque, retorna false, pois nao tem como ser xeque mate se nao eh xeque, entao evita gastar processamento a toa.
	 * Isto feito, primeiramente serao avaliadas todas as possibilidades de movimentacao do rei. Se alguma delas for valida, nao eh xeque mate, portanto retorna false.
	 * Em seguida, cria uma pilha de todas as casas que possuem peca do jogador atual (exceto o rei).
	 * Cada casa, ao ser desempilhada, ira gerar uma outra pilha de movimentos possiveis.
	 * Se, em algum momento, for encontrado algum movimento valido, retorna false, pois nao eh xeque-mate.
	 * Se nao foi encontrado nenhum movimento valido, o algoritmo termina retornando true.
	 * @param vezDasBrancas
	 * @param tabuleiro
	 * @return
	 */
	public boolean isXequeMate(Jogada ultimaJogada, Tabuleiro tabuleiro) {
		Casa casaAux = new Casa();
		Casa casaAux2 = new Casa();
		Tabuleiro tabuleiroPrevisto = tabuleiro.copiaTabuleiro();
		Jogada jogadaTeste = new Jogada();
		Stack<Casa> possiveisMovimentos = new Stack<Casa>();
		Stack<Casa> casasComPeca = new Stack<Casa>();
		char x = 'a';
		int y = 1;
		
		// Se nao estiver em xeque, nao tem como ser xeque-mate!
		if (!this.isEmXeque(tabuleiro))
			return false;	
		
		// Gera uma pilha com todas as pecas do jogador atual
		casasComPeca = tabuleiroPrevisto.casasComPecasDestaCor(this.isBranca());
		while (!casasComPeca.isEmpty()) {
			// E de cada uma dessas pecas, extrai sua pilha de movimentos validos
			casaAux = casasComPeca.pop();			
			possiveisMovimentos = casaAux.peca.movimentosValidos(tabuleiroPrevisto, casaAux, ultimaJogada, this.isBranca());
			
			// Percorre pilha de casas extraida anteriormente, a cada passo simulando uma possivel jogada, vendo se o rei continua em xeque
			while (!possiveisMovimentos.isEmpty()) {
				casaAux2 = possiveisMovimentos.pop();
				jogadaTeste.origem = casaAux;
				jogadaTeste.destino = casaAux2;
				tabuleiroPrevisto.atualizarTabuleiro(jogadaTeste);
				
				// Assim que encontrar um movimento valido para o processamento e retorna false para indicar que nao eh xeque-mate
				if (!this.isEmXeque(tabuleiroPrevisto))
					return false;
				
				// Essa tentativa nao deu, entao retorna o tabuleiro para a posicao inicial para a verificacao seguinte...
				// Para isso, basta inverter origem e destino, e atualizar o tabuleiro novamente
				jogadaTeste.origem = casaAux2;
				jogadaTeste.destino = casaAux;
				tabuleiroPrevisto = tabuleiro.copiaTabuleiro();					
			}				
		}					
			
		return true;
	}
	
	/**
	 * Recebe como parametro uma pilha de casas, e verifica se esta em uma delas.
	 * @param casasAmeacadas - uma pilha de casas
	 * @return true se esta em xeque, false caso contrario
	 * Obs. Este metodo so faz sentido para pecas unicas (rei e dama)
	 */
	public boolean estaSendoAmeacado (Stack<Casa> casasAmeacadas) {
		while (!casasAmeacadas.isEmpty()) {
			if (casasAmeacadas.pop().peca.getTipoPeca() == this.getTipoPeca())
				return true;
		}		
		return false;
	}
	
	/**
	 * Metodo utilizado para avaliar se a jogada proposta e um Roque.
	 * Deve ser invocado apenas apos as verificacoes basicas do movimento.
	 * @param tabuleiro
	 * @param jogada
	 * @return true, se o movimento for um roque grande valido
	 */
	public boolean verificarRoque (Tabuleiro tabuleiro, Jogada jogada) {
		// Se nao for um movimento do rei, nao pode ser roque
		if (!jogada.origem.peca.isRei())
			return false;
		
		// Nao pode fazer roque quando esta em xeque
		if (this.xeque)
			return false;
		
		if (this.isBranca()) {
			// Verifica se eh um rei branco saindo da coordenada e1...
			if (tabuleiro.getPecaEm('e', 1) == 'R') {
				// Verifica se o destino dele for g1, possivel roque
				if (jogada.destino.getCoordenadaX() == 'g' && jogada.destino.getCoordenadaY() == 1) {
					// Verifica se tem uma torre branca em a1
					if (tabuleiro.getPecaEm('h', 1) == 'T') {
						// Verifica se nao tem nenhuma peca no meio do caminho (entre e1 e h1)
						if (tabuleiro.getPecaEm('f', 1) == ' ' && tabuleiro.getPecaEm('g', 1) == ' ') {
							// Verifica se o rei branco ja se moveu
							if (!this.jaMoveu)
								return true;
						}
					}
				}
			}
		}
		else { // Vez das pretas
			// Verifica se eh um rei preto saindo da coordenada e8...
			if (tabuleiro.getPecaEm('e', 8) == 'r') {
				// Verifica se o destino dele for c1, possivel roque grande
				if (jogada.destino.getCoordenadaX() == 'g' && jogada.destino.getCoordenadaY() == 8) {
					// Verifica se tem uma torre preta em a1
					if (tabuleiro.getPecaEm('h', 8) == 't') {
						// Verifica se nao tem nenhuma peca no meio do caminho (entre e1 e a1)
						if (tabuleiro.getPecaEm('f', 8) == ' ' && tabuleiro.getPecaEm('g', 8) == ' ') {
							// Verifica se o rei branco ja se moveu
							if (!this.jaMoveu)
								return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Metodo utilizado para avaliar se a jogada proposta e um Roque Grande.
	 * Deve ser invocado apenas apos as verificacoes basicas do movimento.
	 * @param tabuleiro
	 * @param jogada
	 * @return true, se o movimento for um roque grande valido
	 */
	public boolean verificarRoqueGrande (Tabuleiro tabuleiro, Jogada jogada) {
		// Se nao for um movimento do rei, nao pode ser roque
		if (!jogada.origem.peca.isRei())
			return false;
		
		// Nao pode fazer roque quando esta em xeque
		if (this.xeque)
			return false;
		
		if (this.isBranca()) {
			// Verifica se eh um rei branco saindo da coordenada e1...
			if (tabuleiro.getPecaEm('e', 1) == 'R') {
				// Verifica se o destino dele for c1, possivel roque grande
				if (jogada.destino.getCoordenadaX() == 'c' && jogada.destino.getCoordenadaY() == 1) {
					// Verifica se tem uma torre branca em a1
					if (tabuleiro.getPecaEm('a', 1) == 'T') {
						// Verifica se nao tem nenhuma peca no meio do caminho (entre e1 e a1)
						if (tabuleiro.getPecaEm('b', 1) == ' ' && tabuleiro.getPecaEm('c', 1) == ' ' && tabuleiro.getPecaEm('d', 1) == ' ') {
							// Verifica se o rei branco ja se moveu
							if (!this.jaMoveu)
								return true;
						}
					}
				}
			}
		}
		else { // Vez das pretas
			// Verifica se eh um rei preto saindo da coordenada e8...
			if (tabuleiro.getPecaEm('e', 8) == 'r') {
				// Verifica se o destino dele for c1, possivel roque grande
				if (jogada.destino.getCoordenadaX() == 'c' && jogada.destino.getCoordenadaY() == 8) {
					// Verifica se tem uma torre preta em a1
					if (tabuleiro.getPecaEm('a', 8) == 't') {
						// Verifica se nao tem nenhuma peca no meio do caminho (entre e1 e a1)
						if (tabuleiro.getPecaEm('b', 8) == ' ' && tabuleiro.getPecaEm('c', 8) == ' ' && tabuleiro.getPecaEm('d', 8) == ' ') {
							// Verifica se o rei branco ja se moveu
							if (!this.jaMoveu)
								return true;
						}
					}
				}
			}
		}
		
		return false;
	}
}
