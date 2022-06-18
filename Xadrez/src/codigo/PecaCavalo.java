package codigo;

import java.util.Stack;

public class PecaCavalo extends Peca {
	// Construtor
	public PecaCavalo() {
		super('C');
	}
	
	public PecaCavalo(char tipoPeca) {
		super(tipoPeca);
	}

	// Logica
	@Override
	public boolean isMovimentoValido(Jogada jogada, Tabuleiro tabuleiro, boolean vezDasBrancas) {
		if (jogada.getDeslocamentoX() == 2 && jogada.getDeslocamentoY() == 1)
			return true;
		if (jogada.getDeslocamentoX() == 1 && jogada.getDeslocamentoY() == 2)
			return true;
		if (jogada.getDeslocamentoX() == 2 && jogada.getDeslocamentoY() == -1)
			return true;
		if (jogada.getDeslocamentoX() == 1 && jogada.getDeslocamentoY() == -2)
			return true;
		if (jogada.getDeslocamentoX() == -2 && jogada.getDeslocamentoY() == 1)
			return true;
		if (jogada.getDeslocamentoX() == -1 && jogada.getDeslocamentoY() == 2)
			return true;
		if (jogada.getDeslocamentoX() == -2 && jogada.getDeslocamentoY() == -1)
			return true;
		if (jogada.getDeslocamentoX() == -1 && jogada.getDeslocamentoY() == -2)
			return true;
		return false;
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
		Stack<Casa> casasAmeacadas = new Stack<Casa>();
		Peca pecaAux = new Peca();
		int intFromCharAux = 0;
		
		// No caso do cavalo, sao potencialmente 8 casas que devem ser verificadas.
		// 2 para frente e 1 para direita
		if (casa.getCoordenadaX() < 'h' && casa.getCoordenadaY() < 7) {
			// Se nao tiver uma peca da mesma cor nessa casa
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) + 1;
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 2));
			if (this.isBranca()) {
				if (!pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 2, pecaAux.getTipoPeca()));
			}
			else if (!pecaAux.isPecaPreta())
				casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 2, pecaAux.getTipoPeca()));
		}
		
		// 1 para frente e 2 para direita
		if (casa.getCoordenadaX() < 'g' && casa.getCoordenadaY() < 8) {
			// Se nao tiver uma peca da mesma cor nessa casa
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) + 2;
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 1));
			if (this.isBranca()) {
				if (!pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 1, pecaAux.getTipoPeca()));
			}
			else if (!pecaAux.isPecaPreta())
				casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 1, pecaAux.getTipoPeca()));
		}
		
		// 2 para frente e 1 para esquerda
		if (casa.getCoordenadaX() > 'a' && casa.getCoordenadaY() < 7) {
			// Se nao tiver uma peca da mesma cor nessa casa
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) - 1;
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 2));
			if (this.isBranca()) {
				if (!pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 2, pecaAux.getTipoPeca()));
			}
			else if (!pecaAux.isPecaPreta())
				casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 2, pecaAux.getTipoPeca()));
		}
		
		// 1 para frente e 2 para esquerda
		if (casa.getCoordenadaX() > 'b' && casa.getCoordenadaY() < 8) {
			// Se nao tiver uma peca da mesma cor nessa casa
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) - 2;
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 1));
			if (this.isBranca()) {
				if (!pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 1, pecaAux.getTipoPeca()));
			}
			else if (!pecaAux.isPecaPreta())
				casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() + 1, pecaAux.getTipoPeca()));
		}
		
		// 2 para tras e 1 para direita
		if (casa.getCoordenadaX() < 'h' && casa.getCoordenadaY() > 2) {
			// Se nao tiver uma peca da mesma cor nessa casa
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) + 1;
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 2));
			if (this.isBranca()) {
				if (!pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 2, pecaAux.getTipoPeca()));
			}			
			else if (!pecaAux.isPecaPreta())
				casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 2, pecaAux.getTipoPeca()));
		}
		
		// 1 para tras e 2 para direita
		if (casa.getCoordenadaX() < 'g' && casa.getCoordenadaY() > 1) {
			// Se nao tiver uma peca da mesma cor nessa casa
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) + 2;
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 1));
			if (this.isBranca()) {
				if (!pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 1, pecaAux.getTipoPeca()));
			}			
			else if (!pecaAux.isPecaPreta())
				casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 1, pecaAux.getTipoPeca()));
		}
			
		// 2 para tras e 1 para esquerda
		if (casa.getCoordenadaX() > 'a' && casa.getCoordenadaY() > 2) {
			// Se nao tiver uma peca da mesma cor nessa casa
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) - 1;
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 2));
			if (this.isBranca()) {
				if (!pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 2, pecaAux.getTipoPeca()));
			}
			else if (!pecaAux.isPecaPreta())
				casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 2, pecaAux.getTipoPeca()));
		}
		
		// 1 para tras e 2 para esquerda
		if (casa.getCoordenadaX() > 'b' && casa.getCoordenadaY() > 1) {
			// Se nao tiver uma peca da mesma cor nessa casa
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX()) - 2;
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 1));
			if (this.isBranca()) {
				if (!pecaAux.isPecaBranca())
					casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 1, pecaAux.getTipoPeca()));
			}
			else if (!pecaAux.isPecaPreta())
				casasAmeacadas.push(new Casa(tabuleiro.indexParaCoordenadaX(intFromCharAux), casa.getCoordenadaY() - 1, pecaAux.getTipoPeca()));
		}			
			
		return casasAmeacadas;
	}
}
