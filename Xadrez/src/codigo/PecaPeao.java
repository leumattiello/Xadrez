package codigo;

import java.util.*;

public class PecaPeao extends Peca {
	private boolean enPassant;
	
	
	//
	// Construtores
	//
	
	public PecaPeao() {
		super('P');
		this.enPassant = false;
	}
	
	public PecaPeao(char tipoPeca) {
		super(tipoPeca);
		this.enPassant = false;
	}

	
	//
	// Setters, Getters e Overrides Úteis implementados na superclasse
	//
	
	public boolean isEnPassant() {
		return this.enPassant;
	}
	
	public void setEnPassant(boolean enPassant) {
		this.enPassant = enPassant;
	}
	
	
	//
	// Lógica
	//
	
	@Override
	public boolean isMovimentoValido(Jogada jogada, Tabuleiro tabuleiro, boolean vezDasBrancas) {
		Casa casaAux = new Casa();

		if (vezDasBrancas) { // Se brancas...
			if (jogada.getCasaDestino().getPeca().isPecaPreta()) {// Se estiver comendo, movimento deve ser na diagonal, apenas 1 casa de distancia
				if (!(jogada.getDeslocamentoX() == 1 || jogada.getDeslocamentoX() == -1) || jogada.getDeslocamentoY() != 1)
					return false;
			}
			else { // Se estiver andando ...
				if (jogada.getCasaDestino().getPeca().isPeca()) // Nao pode ter peca no destino
					return false;
				if (jogada.getDeslocamentoX() != 0) // Tem que estar na mesma coluna 
					return false;
				if (jogada.getCasaOrigem().getCoordenadaY() == 2) { // Se o peao estiver na casa 2
					if (!(jogada.getDeslocamentoY() == 1 || jogada.getDeslocamentoY() == 2))// So pode andar 1 ou 2 casas
						return false;
					// Se andar duas casas
					if (jogada.getDeslocamentoY() == 2) {
						// Nao pode ter peca na casa logo a frente
						casaAux.setCoordenadaX(jogada.getCasaOrigem().getCoordenadaX());
						casaAux.setCoordenadaY(jogada.getCasaOrigem().getCoordenadaY() + 1);
						casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
						if (casaAux.getPeca().isPeca())
							return false;
					}
				}
				else { 
					if (jogada.getDeslocamentoY() != 1) // Nesse caso, deslocamento deve ser de exatamente 1
						return false;
				}
			}
		}
		else { // Vez das pretas
			if (jogada.getCasaDestino().getPeca().isPecaBranca()) {// Se estiver comendo, movimento deve ser na diagonal, apenas 1 casa de distancia
				if (!(jogada.getDeslocamentoX() == 1 || jogada.getDeslocamentoX() == -1) || jogada.getDeslocamentoY() != -1)
					return false;
			}
			else { // Se estiver andando ...
				if (jogada.getCasaDestino().getPeca().isPeca()) // Nao pode ter peca no destino
					return false;
				if (jogada.getDeslocamentoX() != 0) // Tem que estar na mesma coluna 
					return false;
				if (jogada.getCasaOrigem().getCoordenadaY() == 7) { // Se o peao estiver na casa 2
					if (!(jogada.getDeslocamentoY() == -1 || jogada.getDeslocamentoY() == -2))// So pode andar 1 ou 2 casas
						return false;
					// Se andar duas casas
					if (jogada.getDeslocamentoY() == -2) {
						// Nao pode ter peca na casa logo a frente
						casaAux.setCoordenadaX(jogada.getCasaOrigem().getCoordenadaX());
						casaAux.setCoordenadaY(jogada.getCasaOrigem().getCoordenadaY() - 1);
						casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
						if (casaAux.getPeca().isPeca())
							return false;
					}
				}
				else { 
					if (jogada.getDeslocamentoY() != -1) // Nesse caso, deslocamento deve ser de exatamente 1
						return false;
				}
			}			
		}		
		
		return true;
	}
	
	/**
	 * Este metodo verifica quais casas estao no range de ataque da peca que ocupa a Casa casa.
	 * @param tabuleiro - para saber o contexto atual do jogo
	 * @param casa - contem informacoes sobre a peca em questao e suas respectivas coordenadas
	 * @param vezDasBrancas - para saber quem esta jogando no momento
	 * @return uma pilha de Casas com as casas ameacadas. 
	 * 			Naturalmente as casas ameacadas somente podem ser da cor oposta a peca ameacante, ou entao vazias (' ').
	 */
	@Override
	public Stack<Casa> ameaca(Tabuleiro tabuleiro, Casa casa, boolean vezDasBrancas) {
		char charAux = 'a';
		int intFromCharAux = 0;
		Peca pecaAux = new Peca();
		Casa casaAux = new Casa();
		Stack<Casa> casasAmeacadas = new Stack<Casa>();
	
		// No caso do peao, sao apenas duas casas que sao possivelmente ameacadas.
		if (vezDasBrancas) {
			// Primeiramente, verificar se o peao nao chegou na 8a casa			
			if (casa.getCoordenadaY() < 8) {
				// Entao verificar uma casa na frente e a direita
				if (casa.getCoordenadaX() < 'h') {// se for na coluna h, nao tem nada a direita para ameacar
					intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) + 1;
					charAux = tabuleiro.indexParaCoordenadaX(intFromCharAux);
					casaAux.setCoordenadaX(charAux);
					casaAux.setCoordenadaY(casa.getCoordenadaY() + 1);
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					pecaAux.setTipoPeca(casaAux.getTipoPeca());
					// Empilhe a casa, exceto se houver uma peca branca nela
					if (!pecaAux.isPecaBranca())
						casasAmeacadas.push(new Casa (casaAux.getCoordenadaX(), casaAux.getCoordenadaY(), casaAux.getTipoPeca()));
				}
				// Verificar uma casa na frente e a esquerda
				if (casa.getCoordenadaX() > 'a') { // se for na coluna a, nao tem nada a esquerda para ameacar
					intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) - 1;
					charAux = tabuleiro.indexParaCoordenadaX(intFromCharAux);
					casaAux.setCoordenadaX(charAux);
					casaAux.setCoordenadaY(casa.getCoordenadaY() + 1);
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					pecaAux.setTipoPeca(casaAux.getTipoPeca());
					// Empilhe a casa, exceto se houver uma peca branca nela
					if (!pecaAux.isPecaBranca())
						casasAmeacadas.push(new Casa (casaAux.getCoordenadaX(), casaAux.getCoordenadaY(), casaAux.getTipoPeca()));
				}
			}
		}
		else { // Vez das pretas
			// Primeiramente, verificar se o peao nao chegou na 8a casa			
			if (casa.getCoordenadaY() > 1) {
				// Entao verificar uma casa na frente e a direita
				if (casa.getCoordenadaX() < 'h') {// se for na coluna h, nao tem nada a direita para ameacar
					intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) + 1;
					charAux = tabuleiro.indexParaCoordenadaX(intFromCharAux);
					casaAux.setCoordenadaX(charAux);
					casaAux.setCoordenadaY(casa.getCoordenadaY() - 1);
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					pecaAux.setTipoPeca(casaAux.getTipoPeca());
					// Empilhe a casa, exceto se houver uma peca preta nela
					if (!pecaAux.isPecaPreta())
						casasAmeacadas.push(new Casa (casaAux.getCoordenadaX(), casaAux.getCoordenadaY(), casaAux.getTipoPeca()));
				}
				// Verificar uma casa na frente e a esquerda
				if (casa.getCoordenadaX() > 'a') { // se for na coluna a, nao tem nada a esquerda para ameacar
					intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) - 1;
					charAux = tabuleiro.indexParaCoordenadaX(intFromCharAux);
					casaAux.setCoordenadaX(charAux);
					casaAux.setCoordenadaY(casa.getCoordenadaY() - 1);
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					pecaAux.setTipoPeca(casaAux.getTipoPeca());
					// Empilhe a casa, exceto se houver uma peca preta nela
					if (!pecaAux.isPecaPreta())
						casasAmeacadas.push(new Casa (casaAux.getCoordenadaX(), casaAux.getCoordenadaY(), casaAux.getTipoPeca()));
				}
			}			
		}		
		
		return casasAmeacadas;
	}
	
	/**
	 * Este metodo teve de ser sobreposto, pois os movimentos validos do peao nao condizem simplesmente com o range de ameacas dele,
	 * devido ao fato de os movimentos de comer e andar possuirem uma logica diferente.
	 */
	@Override
	public Stack<Casa> movimentosValidos (Tabuleiro tabuleiro, Casa casa, Jogada ultimaJogada, Boolean vezDasBrancas){
		char pecaAux = ' ';
		char x = 'a';
		char xAux = 'a';
		int y = 1;
		int intAux = 0;
		Casa casaAux = new Casa();	
		Stack<Casa> movimentosValidos = new Stack<Casa>();
		Stack<Casa> pilhaAux = new Stack<Casa>();
	
		// Pegue todas as casas ameacadas por esta peca
		pilhaAux = this.ameaca(tabuleiro, casa, vezDasBrancas);
		
		// Aproveite apenas aquelas casas ameacadas que possuam peca inimiga
		while (!pilhaAux.isEmpty()) {
			casaAux = pilhaAux.pop();
			pecaAux = casaAux.getTipoPeca();
			if ((this.isBranca() && casaAux.getPeca().isPecaPreta()) ||
				 !this.isBranca() && casaAux.getPeca().isPecaBranca()) {
					casaAux.setPeca(pecaAux); // Chamado para invocar o new Peca () do tipo correto
					verificarCoroacao(casaAux);
					movimentosValidos.push(casaAux);
			}
		}
	
		// Agora teste apenas os movimentos de avancar o peao
		x = casa.getCoordenadaX();
		y = casa.getCoordenadaY();
		if (this.isBranca()) {		
			// Verifica se o peao esta na segunda casa
			if (y == 2) {
				// Verifica se nao tem nenhuma peca (branca ou preta) nas duas casas a frente
				if (tabuleiro.getPecaEm(x, y + 1) == ' ' && tabuleiro.getPecaEm(x, y + 2) == ' ') {
					Casa c = new Casa(x, y + 2, ' ');
					verificarCoroacao(c);
					movimentosValidos.push(c);
				}
			}
			if (y < 8) {
				if (tabuleiro.getPecaEm(x, y + 1) == ' ') {
					Casa c = new Casa(x, y + 1, ' ');
					verificarCoroacao(c);
					movimentosValidos.push(c);
				}
			}
		}
		else {
			// Verifica se o peao esta na segunda casa
			if (y == 7) {
				// Verifica se nao tem nenhuma peca (branca ou preta) nas duas casas a frente
				if (tabuleiro.getPecaEm(x, y - 1) == ' ' && tabuleiro.getPecaEm(x, y - 2) == ' ') {
					Casa c = new Casa(x, y - 2, ' ');
					verificarCoroacao(c);
					movimentosValidos.push(c);
				}
			}
			if (y > 1) {
				if (tabuleiro.getPecaEm(x, y - 1) == ' ') {
					Casa c = new Casa(x, y - 1, ' ');
					verificarCoroacao(c);
					movimentosValidos.push(c);
				}
			}
		}
		
		// Por fim, teste as possibilidades de En Passant
		xAux = x;
		intAux = tabuleiro.coordenadaXParaIndex(xAux);		
		// En Passant para a direita
		if(intAux < 7) {
			xAux = tabuleiro.indexParaCoordenadaX(++intAux);
			pecaAux = tabuleiro.getPecaEm(xAux, y + (this.isPecaBranca() ? 1 : -1));
			casaAux.setCoordenadaX(xAux);
			casaAux.setCoordenadaY(y + (this.isPecaBranca() ? 1 : -1));
			casaAux.setPeca(pecaAux);
			if (verificarEnPassant(tabuleiro, new Jogada(casa, casaAux), ultimaJogada)) {
				Casa c = new Casa(casaAux.getCoordenadaX(), casaAux.getCoordenadaY(), casaAux.getTipoPeca());
				// Usar campo defesas da casa para marcar en passant
				c.setDefesas(90);
				movimentosValidos.push(c);
			}
		}		
		// En Passant para a esquerda
		xAux = x;
		intAux = tabuleiro.coordenadaXParaIndex(xAux);	
		if(intAux > 1) {
			xAux = tabuleiro.indexParaCoordenadaX(--intAux);
			pecaAux = tabuleiro.getPecaEm(xAux, y + (this.isPecaBranca() ? 1 : -1));
			casaAux.setCoordenadaX(xAux);
			casaAux.setCoordenadaY(y + (this.isPecaBranca() ? 1 : -1));
			casaAux.setPeca(pecaAux);
			if (verificarEnPassant(tabuleiro, new Jogada(casa, casaAux), ultimaJogada)) {
				Casa c = new Casa(casaAux.getCoordenadaX(), casaAux.getCoordenadaY(), casaAux.getTipoPeca());
				// Usar campo defesas da casa para marcar en passant
				c.setDefesas(90);
				movimentosValidos.push(c);
			}
		}	
		return movimentosValidos;
	}	
	
	/**
	 * Metodo utilizado para avaliar se a jogada proposta e um En Passant.
	 * Deve ser invocado apos as verificacoes basicas de validade de movimento.
	 * @param tabuleiro
	 * @param jogada
	 * @param ultimaJogada
	 * @param vezDasBrancas
	 * @return true se a jogada for um En Passant valido, false caso contrario
	 */
	public boolean verificarEnPassant (Tabuleiro tabuleiro, Jogada jogada, Jogada ultimaJogada) {
		this.setEnPassant(false);
		jogada.setEnPassant(false);
		
		// Se nao for um peao, automaticamente nao eh en passant
		if (!jogada.getCasaOrigem().getPeca().isPeao()) {
			return false;
		}
		
		// Se a jogada anterior nao foi um peao, automaticamente nao eh en passant
		if (!ultimaJogada.getCasaOrigem().getPeca().isPeao()) {
			return false;
		}
		
		if (this.isBranca()) {
			// Eh um Peao branco na 5a fileira?
			if (jogada.getCasaOrigem().getCoordenadaY() == 5 && jogada.getCasaOrigem().getPeca().isBranca()) 
				// Ele quer se mover exatamente 1 casa a frente?
				if (jogada.getCasaDestino().getCoordenadaY() == 6)
					// A origem da ultima jogada foi exatamente na fileira 7?
					if (ultimaJogada.getCasaOrigem().getCoordenadaY() == 7)
						// A ultima jogada foi na mesma coluna de destino, e foi um deslocamento de 2 casas?
						if (ultimaJogada.getCasaOrigem().getCoordenadaX() == jogada.getCasaDestino().getCoordenadaX() && ultimaJogada.getDeslocamentoY() == -2)
							// Ele quer se mover exatamente 1 casa para a direita ou esquerda?
							if (jogada.getCasaDestino().getCoordenadaX() == jogada.getCasaOrigem().getCoordenadaX() + 1 || jogada.getCasaDestino().getCoordenadaX() == jogada.getCasaOrigem().getCoordenadaX() - 1)		{				
								// Entao eh En Passant!
								this.setEnPassant(true);
								jogada.setEnPassant(true);
								return true;													
							}
		}
		else { // Eh um peao preto na 4a fileira?
			if (jogada.getCasaOrigem().getCoordenadaY() == 4 && !jogada.getCasaOrigem().getPeca().isBranca()) 
				// Ele quer se mover exatamente 1 casa a frente?
				if (jogada.getCasaDestino().getCoordenadaY() == 3)
					// A origem da ultima jogada foi exatamente na 7a fileira?
					if (ultimaJogada.getCasaOrigem().getCoordenadaY() == 2)
						// A ultima jogada foi na mesma coluna de destino, e foi um deslocamento de 2 casas?
						if (ultimaJogada.getCasaOrigem().getCoordenadaX() == jogada.getCasaDestino().getCoordenadaX() && ultimaJogada.getDeslocamentoY() == 2)
							// Ele quer se mover exatamente 1 casa para a direita ou para esquerda?
							if (jogada.getCasaDestino().getCoordenadaX() == jogada.getCasaOrigem().getCoordenadaX() + 1 || jogada.getCasaDestino().getCoordenadaX() == jogada.getCasaOrigem().getCoordenadaX() - 1)		{				
								// Entao eh En Passant!
								this.setEnPassant(true);
								jogada.setEnPassant(true);
								return true;	
							}
		}		
		
		return false;
	}
	
	public boolean verificarCoroacao(Jogada jogadaAtual, Tabuleiro tabuleiro) {
		int y = jogadaAtual.getCasaDestino().getCoordenadaY();
		
		jogadaAtual.setCoroacao(false);
		
		if(jogadaAtual.getCasaOrigem().getPeca().isPeao()) {
			if(y == 1 || y == 8) {
				jogadaAtual.setCoroacao(true);
				return true;
			}
		}
		
		return false;
	}
	
	private boolean verificarCoroacao(Casa destino) {
		int y = destino.getCoordenadaY();
		
		if(y == 1 || y == 8) {
			// Se for coroacao, marca no campo defesas da casa
			destino.setDefesas(80);
			return true;
		}
		
		return false;
	}
}
