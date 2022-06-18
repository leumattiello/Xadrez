package codigo;

import java.util.Stack;

public class PecaDama extends Peca {
	// Construtores
	public PecaDama() {
		super('D');
	}
	
	public PecaDama(char tipoPeca) {
		super(tipoPeca);
	}

	// Logica
	@Override
	public boolean isMovimentoValido(Jogada jogada, Tabuleiro tabuleiro, boolean vezDasBrancas) {
		PecaBispo bispo = vezDasBrancas? new PecaBispo('B') : new PecaBispo('b');
		PecaTorre torre = vezDasBrancas? new PecaTorre('T') : new PecaTorre('t');		
		boolean moveComoBispo = bispo.isMovimentoValido(jogada, tabuleiro, vezDasBrancas);
		boolean moveComoTorre = torre.isMovimentoValido(jogada, tabuleiro, vezDasBrancas);
		
		// Se nao passou nas verificacoes do bispo e nem da torre, lance esta errado
		if (!moveComoBispo && !moveComoTorre)
			return false;
		
		return true;
	}	
	
	@Override
	public Stack<Casa> ameaca(Tabuleiro tabuleiro, Casa casa, boolean vezDasBrancas) {
		Stack<Casa> casasAmeacadas = new Stack<Casa>();
		Stack<Casa> casasAmeacadasBispo = new Stack<Casa>();
		PecaBispo bispo = this.isBranca() ? new PecaBispo('B') : new PecaBispo('b');
		PecaTorre torre = this.isBranca() ? new PecaTorre('T') : new PecaTorre('t');

		// Para a dama eh so fazer a coleta dos algoritmos do bispo + torre.
		casasAmeacadasBispo = bispo.ameaca(tabuleiro, casa, vezDasBrancas);
		casasAmeacadas = torre.ameaca(tabuleiro, casa, vezDasBrancas);
		while (!casasAmeacadasBispo.isEmpty())
			casasAmeacadas.push(casasAmeacadasBispo.pop());
		
		return casasAmeacadas;
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
}
