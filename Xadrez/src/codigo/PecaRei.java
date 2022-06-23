package codigo;

import java.util.*;

public class PecaRei extends Peca {
	private boolean jaMoveu;
	private boolean xeque;
	private boolean xequeMate;
	private boolean roque;
	private boolean roqueGrande;

	
	// 
	// Construtores
	//
	
	public PecaRei () {
		super('R');
		this.jaMoveu = false;
		this.xeque = false;
	}
	
	public PecaRei(char tipoPeca) {
		super(tipoPeca);
		this.jaMoveu = false;
		this.xeque = false;
	}
	

	//
	// Setters, Getters e Overrides Úteis implementados na superclasse
	//
	
	public boolean jaMoveu() {
		return jaMoveu;
	}
	
	public void setJaMoveu(boolean moveu) {
		this.jaMoveu = moveu;
	}
		
	public boolean emXeque() {
		return xeque;
	}

	public void setXeque(boolean xeque) {
		this.xeque = xeque;
	}

	public boolean emXequeMate() {
		return xequeMate;
	}

	public void setXequeMate(boolean xequeMate) {
		this.xequeMate = xequeMate;
	}	
	
	public boolean isRoque() {
		return roque;
	}
	
	public void setRoque(boolean roque) {
		this.roque = roque;
	}
	
	public boolean isRoqueGrande() {
		return roqueGrande;
	}
	
	public void setRoqueGrande(boolean roqueGrande) {
		this.roqueGrande = roqueGrande;
	}
	
	
	//
	// Lógica
	//

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
		char coordX = 'a';
		int coordY = 1; // Lembrando que sera utilizado de 1 a 8
		int intFromCharAux = 0;
		Peca pecaAux = new Peca();
		Stack<Casa> casasAmeacadas = new Stack<Casa>();
		
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
		return this.movimentosValidos(tabuleiro, casa, ultimaJogada, vezDasBrancas, false);
	}
	
	@Override
	public Stack<Casa> movimentosValidos (Tabuleiro tabuleiro, Casa casa, Jogada ultimaJogada, Boolean vezDasBrancas, boolean atualizarFlags){
		char x = 'a';
		int y = 1;
		Jogada jogadaAux = new Jogada();
		Stack<Casa> movimentosValidos = new Stack<Casa>();
		
		// Pegue todas as casas ameacadas por esta peca
		movimentosValidos = this.ameaca(tabuleiro, casa, vezDasBrancas);

		// Testar as possibilidades de roque e roque grande
		x = 'g';
		y = this.isBranca() ? 1 : 8;		
		jogadaAux.setCasaOrigem(casa);
		jogadaAux.getCasaDestino().setCoordenadaX(x);
		jogadaAux.getCasaDestino().setCoordenadaY(y);
		if (atualizarFlags) {
			this.isEmXeque(tabuleiro, atualizarFlags);
		}
		if (this.verificarRoque(tabuleiro, jogadaAux, false)) {
			Casa c = new Casa (x, y, ' ');
			// Usar o campo defesas para marcar que é roque
			c.setDefesas(100);
			movimentosValidos.push(c);
		}
		x = 'c';
		jogadaAux.getCasaDestino().setCoordenadaX(x);
		if (this.verificarRoqueGrande(tabuleiro, jogadaAux, false)) {
			Casa c = new Casa (x, y, ' ');
			// Usar o campo defesas para marcar que é roque
			c.setDefesas(1000);
			movimentosValidos.push(c);
		}		
		
		return movimentosValidos;
	}
	
	/**
	 * @param tabuleiro
	 * @return true se esta em xeque, false caso contrario
	 */
	public boolean isEmXeque(Tabuleiro tabuleiro, boolean atualizaFlags) {
		Peca pecaAux = new Peca();
		Casa casaAux = new Casa();
		Casa casaDoRei = new Casa();
		Stack<Casa> pilhaDeCasas = new Stack<Casa>();
		Stack<Casa> casasAmeacadas = new Stack<Casa>();
		
		if(atualizaFlags)
			this.setXeque(false);
		
		// Primeiramente, criar uma pilha com todas as casas que possuem peca do adversario
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				pecaAux.setTipoPeca(tabuleiro.casas[i][j]);
				if(pecaAux.isRei()) {
					if(pecaAux.isBranca() == this.isBranca()) {
						casaDoRei = new Casa(tabuleiro.indexParaCoordenadaX(j), tabuleiro.indexParaCoordenadaY(i), pecaAux.getTipoPeca());
					}
					
				}
				if (pecaAux.isPeca()) { // Se tem alguma peca naquela casa
					if (this.isBranca() != pecaAux.isBranca()) { // Se a peca nao eh da mesma cor
						// Nesse caso, tem que empilhar a casa
						pilhaDeCasas.push(new Casa(tabuleiro.indexParaCoordenadaX(j), tabuleiro.indexParaCoordenadaY(i), pecaAux.getTipoPeca()));
					}
				}
			}	
		}
		
		// Desempilhar a pilha de casas, verificando se cada uma das pecas ameaca o rei
		while (!pilhaDeCasas.isEmpty()) {
			casaAux = pilhaDeCasas.pop();
			casaAux.setPeca(casaAux.getTipoPeca());
			casasAmeacadas = casaAux.getPeca().ameaca(tabuleiro, casaAux, !this.isBranca());
			if (estaSendoAmeacado(casaDoRei, casasAmeacadas)){
				if(atualizaFlags)
					this.setXeque(true);
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
	public boolean isXequeMate(Jogada ultimaJogada, Tabuleiro tabuleiro, boolean atualizaFlags) {
		boolean xeque = this.isEmXeque(tabuleiro, atualizaFlags);
		Casa casaAux = new Casa();
		Casa casaAux2 = new Casa();
		Tabuleiro tabuleiroPrevisto = tabuleiro.copiaTabuleiro();
		Jogada jogadaTeste = new Jogada();
		Stack<Casa> possiveisMovimentos = new Stack<Casa>();
		Stack<Casa> casasComPeca = new Stack<Casa>();
		
		if (atualizaFlags) {
			this.setXequeMate(false);
		}
		
		// Se nao estiver em xeque, nao tem como ser xeque-mate!
		if (!xeque) {
			return false;	
		}
			
		// Gera uma pilha com todas as pecas do jogador atual
		casasComPeca = tabuleiroPrevisto.casasComPecasDestaCor(this.isBranca());
		while (!casasComPeca.isEmpty()) {
			// E de cada uma dessas pecas, extrai sua pilha de movimentos validos
			casaAux = casasComPeca.pop();			
			possiveisMovimentos = casaAux.getPeca().movimentosValidos(tabuleiroPrevisto, casaAux, ultimaJogada, this.isBranca());
			
			// Percorre pilha de casas extraida anteriormente, a cada passo simulando uma possivel jogada, vendo se o rei continua em xeque
			while (!possiveisMovimentos.isEmpty()) {
				casaAux2 = possiveisMovimentos.pop();
				jogadaTeste.setCasaOrigem(casaAux);
				jogadaTeste.setCasaDestino(casaAux2);
				tabuleiroPrevisto.atualizarTabuleiro(jogadaTeste);
				
				// Assim que encontrar um movimento valido para o processamento e retorna false para indicar que nao eh xeque-mate
				// Mas antes restaura o status de xeque que foi contaminado pela simulação
				if (!this.isEmXeque(tabuleiroPrevisto, false)) {
					return false;
				}
				
				// Essa tentativa nao deu, entao retorna o tabuleiro para a posicao inicial para a verificacao seguinte...
				// Para isso, basta inverter origem e destino, e atualizar o tabuleiro novamente
				jogadaTeste.setCasaOrigem(casaAux2);
				jogadaTeste.setCasaDestino(casaAux);
				tabuleiroPrevisto = tabuleiro.copiaTabuleiro();					
			}				
		}
		
		if(atualizaFlags) {
			this.setXeque(true);
			this.setXequeMate(true);
		}	
		return true;
	}
	
	/**
	 * Recebe como parametro uma pilha de casas, e verifica se esta em uma delas.
	 * @param casasAmeacadas - uma pilha de casas
	 * @return true se esta em xeque, false caso contrario
	 * Obs. Este metodo so faz sentido para pecas unicas (rei e dama)
	 */
	public boolean estaSendoAmeacado (Casa casa, Stack<Casa> casasAmeacadas) {
		return casasAmeacadas.contains(casa);
	}
	
	/**
	 * Metodo utilizado para avaliar se a jogada proposta e um Roque.
	 * Deve ser invocado apenas apos as verificacoes basicas do movimento.
	 * @param tabuleiro
	 * @param jogada
	 * @return true, se o movimento for um roque grande valido
	 */
	public boolean verificarRoque (Tabuleiro tabuleiro, Jogada jogada, boolean atualizarFlags) {
		if(atualizarFlags) {
			this.setRoque(false);
			jogada.setRoque(false);			
		}
		
		// Se nao for um movimento do rei, nao pode ser roque
		if (!jogada.getCasaOrigem().getPeca().isRei())
			return false;
		
		// Nao pode fazer roque quando esta em xeque
		if (this.emXeque() || this.emXequeMate())
			return false;
		
		if (this.isBranca()) {
			// Verifica se eh um rei branco saindo da coordenada e1...
			if (tabuleiro.getPecaEm('e', 1) == 'R') {
				// Verifica se o destino dele for g1, possivel roque
				if (jogada.getCasaDestino().getCoordenadaX() == 'g' && jogada.getCasaDestino().getCoordenadaY() == 1) {
					// Verifica se tem uma torre branca em a1
					if (tabuleiro.getPecaEm('h', 1) == 'T') {
						// Verifica se nao tem nenhuma peca no meio do caminho (entre e1 e h1)
						if (tabuleiro.getPecaEm('f', 1) == ' ' && tabuleiro.getPecaEm('g', 1) == ' ') {
							// Verifica se o rei branco ja se moveu
							if (!this.jaMoveu) {
								// Simula um movimento do rei para f1 e verifica se fica em xeque
								Tabuleiro tabuleiroAux = tabuleiro.copiaTabuleiro();
								Casa casaOrigem = new Casa ('e', 1, 'R');
								Casa casaDestino = new Casa ('f', 1);
								Jogada j = new Jogada (casaOrigem, casaDestino);
								tabuleiroAux.atualizarTabuleiro(j);
								if(!this.isEmXeque(tabuleiroAux, false)){
									if (atualizarFlags) {
										this.setRoque(true);
										jogada.setRoque(true);
									}
									return true;
								}
							}	
						}
					}
				}
			}
		}
		else { // Vez das pretas
			// Verifica se eh um rei preto saindo da coordenada e8...
			if (tabuleiro.getPecaEm('e', 8) == 'r') {
				// Verifica se o destino dele for c1, possivel roque grande
				if (jogada.getCasaDestino().getCoordenadaX() == 'g' && jogada.getCasaDestino().getCoordenadaY() == 8) {
					// Verifica se tem uma torre preta em a1
					if (tabuleiro.getPecaEm('h', 8) == 't') {
						// Verifica se nao tem nenhuma peca no meio do caminho (entre e1 e a1)
						if (tabuleiro.getPecaEm('f', 8) == ' ' && tabuleiro.getPecaEm('g', 8) == ' ') {
							// Verifica se o rei branco ja se moveu
							if (!this.jaMoveu) {
								// Simula um movimento do rei para f8 e verifica se fica em xeque
								Tabuleiro tabuleiroAux = tabuleiro.copiaTabuleiro();
								Casa casaOrigem = new Casa ('e', 8, 'r');
								Casa casaDestino = new Casa ('f', 8);
								Jogada j = new Jogada (casaOrigem, casaDestino);
								tabuleiroAux.atualizarTabuleiro(j);
								if(!this.isEmXeque(tabuleiroAux, false)){
									if(atualizarFlags) {
										this.setRoque(true);
										jogada.setRoque(true);
									}
									return true;
								}
							}
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
	public boolean verificarRoqueGrande (Tabuleiro tabuleiro, Jogada jogada, boolean atualizarFlags) {
		if(atualizarFlags) {
			this.setRoqueGrande(false);
			jogada.setRoqueGrande(false);			
		}
				
		// Se nao for um movimento do rei, nao pode ser roque
		if (!jogada.getCasaOrigem().getPeca().isRei())
			return false;
		
		// Nao pode fazer roque quando esta em xeque
		if (this.emXeque() || this.emXequeMate())
			return false;
		
		if (this.isBranca()) {
			// Verifica se eh um rei branco saindo da coordenada e1...
			if (tabuleiro.getPecaEm('e', 1) == 'R') {
				// Verifica se o destino dele for c1, possivel roque grande
				if (jogada.getCasaDestino().getCoordenadaX() == 'c' && jogada.getCasaDestino().getCoordenadaY() == 1) {
					// Verifica se tem uma torre branca em a1
					if (tabuleiro.getPecaEm('a', 1) == 'T') {
						// Verifica se nao tem nenhuma peca no meio do caminho (entre e1 e a1)
						if (tabuleiro.getPecaEm('b', 1) == ' ' && tabuleiro.getPecaEm('c', 1) == ' ' && tabuleiro.getPecaEm('d', 1) == ' ') {
							// Verifica se o rei branco ja se moveu
							if (!this.jaMoveu) {
								// Simula um movimento do rei para f8 e verifica se fica em xeque
								Tabuleiro tabuleiroAux = tabuleiro.copiaTabuleiro();
								Casa casaOrigem = new Casa ('e', 1, 'R');
								Casa casaDestino = new Casa ('d', 1);
								Jogada j = new Jogada (casaOrigem, casaDestino);
								tabuleiroAux.atualizarTabuleiro(j);
								if(!this.isEmXeque(tabuleiroAux, false)){
									if(atualizarFlags) {
										this.setRoqueGrande(true);
										jogada.setRoqueGrande(true);
									}									
									return true;
								}
							}
						}
					}
				}
			}
		}
		else { // Vez das pretas
			// Verifica se eh um rei preto saindo da coordenada e8...
			if (tabuleiro.getPecaEm('e', 8) == 'r') {
				// Verifica se o destino dele for c1, possivel roque grande
				if (jogada.getCasaDestino().getCoordenadaX() == 'c' && jogada.getCasaDestino().getCoordenadaY() == 8) {
					// Verifica se tem uma torre preta em a1
					if (tabuleiro.getPecaEm('a', 8) == 't') {
						// Verifica se nao tem nenhuma peca no meio do caminho (entre e1 e a1)
						if (tabuleiro.getPecaEm('b', 8) == ' ' && tabuleiro.getPecaEm('c', 8) == ' ' && tabuleiro.getPecaEm('d', 8) == ' ') {
							// Verifica se o rei branco ja se moveu
							if (!this.jaMoveu) {
								// Simula um movimento do rei para f8 e verifica se fica em xeque
								Tabuleiro tabuleiroAux = tabuleiro.copiaTabuleiro();
								Casa casaOrigem = new Casa ('e', 8, 'r');
								Casa casaDestino = new Casa ('d', 8);
								Jogada j = new Jogada (casaOrigem, casaDestino);
								tabuleiroAux.atualizarTabuleiro(j);
								if(!this.isEmXeque(tabuleiroAux, false)){
									if(atualizarFlags) {
										this.setRoqueGrande(true);
										jogada.setRoqueGrande(true);
									}
									return true;
								}
							}
						}
					}
				}
			}
		}
		
		return false;
	}
}