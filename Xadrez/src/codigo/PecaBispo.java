package codigo;

import java.util.Stack;

public class PecaBispo extends Peca {
	// Construtor
	public PecaBispo() {
		super('B');
	}
	
	public PecaBispo(char tipoPeca) {
		super(tipoPeca);
	}

	// Logica
	@Override
	public boolean isMovimentoValido(Jogada jogada, Tabuleiro tabuleiro, boolean vezDasBrancas) {
		int deslocamentoAbsoluto = jogada.getDeslocamentoX() < 0 ? -1*jogada.getDeslocamentoX() : jogada.getDeslocamentoX();
		boolean deslocamentoPositivoEmX = jogada.getDeslocamentoX() >= 0 ? true : false;
		boolean deslocamentoPositivoEmY = jogada.getDeslocamentoY() >= 0 ? true : false;
		int auxIntFromChar = 0;
		char auxChar = 'a';
		Casa casaAux = new Casa();
		
		// Tem que deslocar pelo menos 1 casa
		if (deslocamentoAbsoluto < 1)
			return false;
		
		// Numero absoluto de casas no deslocamento do bispo deve ser o mesmo tanto no eixo X quanto no Y
		if (jogada.getDeslocamentoX() != jogada.getDeslocamentoY())
			if (jogada.getDeslocamentoX() != -1*jogada.getDeslocamentoY())
				return false;
		
		// Nao deve haver nenhuma peca no meio caminho
		if (deslocamentoPositivoEmX)
			if (deslocamentoPositivoEmY){ // deslocamento positivo em X e Y
				for (int i = 1; i < deslocamentoAbsoluto; i++) {
					auxIntFromChar = tabuleiro.coordenadaXParaIndex(jogada.origem.getCoordenadaX()) + i;
					auxChar = tabuleiro.indexParaCoordenadaX(auxIntFromChar);
					casaAux.setCoordenadaX(auxChar);
					casaAux.setCoordenadaY(jogada.origem.getCoordenadaY() + i);
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					if (casaAux.getPeca().isPeca())
						return false;
				}
			}
			else { // deslocamento positivo em X, negativo em Y
				for (int i = 1; i < deslocamentoAbsoluto; i++) {
					auxIntFromChar = tabuleiro.coordenadaXParaIndex(jogada.origem.getCoordenadaX()) + i;
					auxChar = tabuleiro.indexParaCoordenadaX(auxIntFromChar);
					casaAux.setCoordenadaX(auxChar);casaAux.setCoordenadaY(jogada.origem.getCoordenadaY() - i);
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					if (casaAux.getPeca().isPeca())
						return false;
				}
			}
		else 
			if (deslocamentoPositivoEmY) { // deslocamento negativo em X, positivo em Y
				for (int i = 1; i < deslocamentoAbsoluto; i++) {
					auxIntFromChar = tabuleiro.coordenadaXParaIndex(jogada.origem.getCoordenadaX()) - i;
					auxChar = tabuleiro.indexParaCoordenadaX(auxIntFromChar);
					casaAux.setCoordenadaX(auxChar);
					casaAux.setCoordenadaY(jogada.origem.getCoordenadaY() + i);
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					if (casaAux.getPeca().isPeca())
						return false;
				}
			}
			else { // deslocamento negativo em X e Y
				for (int i = 1; i < deslocamentoAbsoluto; i++) {
					auxIntFromChar = tabuleiro.coordenadaXParaIndex(jogada.origem.getCoordenadaX()) - i;
					auxChar = tabuleiro.indexParaCoordenadaX(auxIntFromChar);
					casaAux.setCoordenadaX(auxChar);
					casaAux.setCoordenadaY(jogada.origem.getCoordenadaY() - i);
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					if (casaAux.getPeca().isPeca())
						return false;
				}				
			}
				
		return true;
	}	

	@Override
	public Stack<Casa> ameaca(Tabuleiro tabuleiro, Casa casa, boolean vezDasBrancas) {
		Stack<Casa> casasAmeacadas = new Stack<Casa>();
		Peca pecaAux = new Peca();
		int intFromCharAux = 0;
		char coordX = 'a';
		int coordY = 1; // Lembrando que sera utilizado de 1 a 8
		
		// Bispo tem que ser verificado em 4 direcoes. Em cada uma delas,
		// percorre a diagonal ate encontrar uma peca. Vai empilhando as casas pelo caminho. 
		// Se a ultima casa verificada for uma peca de outra cor, empilha ela tambem.
		
		// Nordeste (para frente e direita)
		for (int i = 1; i < 8; i++) {
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
			coordY = casa.getCoordenadaY() + i;
			coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux + i);
			
			// Se coordenadas cairem fora do range, sai do loop
			if (!('a' <= coordX && coordX <= 'h'))
				break;
			if (!(1 <= coordY && coordY <= 8))
				break;
			
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
			
			// Se houver uma peca da mesma cor na casa, sai do loop			
			if (this.isBranca() && pecaAux.isPecaBranca())
				break;
			if (!this.isBranca() && pecaAux.isPecaPreta())
				break;
			
			casasAmeacadas.push(new Casa(coordX, coordY, pecaAux.getTipoPeca()));
			
			// Se houver uma peca de cor diferente na casa, sai do loop depois de ter empilhado a casa
			if (this.isBranca() && pecaAux.isPecaPreta())
				break;
			if (!this.isBranca() && pecaAux.isPecaBranca())
				break;
		}
		
		// Noroeste (para frente e esquerda)
		for (int i = 1; i < 8; i++) {
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
			coordY = casa.getCoordenadaY() + i;
			coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux - i);
			
			// Se coordenadas cairem fora do range, sai do loop
			if (!('a' <= coordX && coordX <= 'h'))
				break;
			if (!(1 <= coordY && coordY <= 8))
				break;
			
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
			
			// Se houver uma peca da mesma cor na casa, sai do loop			
			if (this.isBranca() && pecaAux.isPecaBranca())
				break;
			if (!this.isBranca() && pecaAux.isPecaPreta())
				break;
			
			casasAmeacadas.push(new Casa(coordX, coordY, pecaAux.getTipoPeca()));
			
			// Se houver uma peca de cor diferente na casa, sai do loop depois de ter empilhado a casa
			if (this.isBranca() && pecaAux.isPecaPreta())
				break;
			if (!this.isBranca() && pecaAux.isPecaBranca())
				break;
		}	
		
		// Sudoeste (para tras e esquerda)
		for (int i = 1; i < 8; i++) {
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
			coordY = casa.getCoordenadaY() - i;
			coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux - i);
			
			// Se coordenadas cairem fora do range, sai do loop
			if (!('a' <= coordX && coordX <= 'h'))
				break;
			if (!(1 <= coordY && coordY <= 8))
				break;
			
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
			
			// Se houver uma peca da mesma cor na casa, sai do loop			
			if (this.isBranca() && pecaAux.isPecaBranca())
				break;
			if (!this.isBranca() && pecaAux.isPecaPreta())
				break;
			
			casasAmeacadas.push(new Casa(coordX, coordY, pecaAux.getTipoPeca()));
			
			// Se houver uma peca de cor diferente na casa, sai do loop depois de ter empilhado a casa
			if (this.isBranca() && pecaAux.isPecaPreta())
				break;
			if (!this.isBranca() && pecaAux.isPecaBranca())
				break;
		}	
		
		// Sudeste (para tras e direita)
		for (int i = 1; i < 8; i++) {
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
			coordY = casa.getCoordenadaY() - i;
			coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux + i);
			
			// Se coordenadas cairem fora do range, sai do loop
			if (!('a' <= coordX && coordX <= 'h'))
				break;
			if (!(1 <= coordY && coordY <= 8))
				break;
			
			pecaAux.setTipoPeca(tabuleiro.getPecaEm(coordX, coordY));
			
			// Se houver uma peca da mesma cor na casa, sai do loop			
			if (this.isBranca() && pecaAux.isPecaBranca())
				break;
			if (!this.isBranca() && pecaAux.isPecaPreta())
				break;
			
			casasAmeacadas.push(new Casa(coordX, coordY, pecaAux.getTipoPeca()));
			
			// Se houver uma peca de cor diferente na casa, sai do loop depois de ter empilhado a casa
			if (this.isBranca() && pecaAux.isPecaPreta())
				break;
			if (!this.isBranca() && pecaAux.isPecaBranca())
				break;
		}	
		
		return casasAmeacadas;
	}

}
