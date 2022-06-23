package codigo;

import java.util.Stack;

public class PecaTorre extends Peca {	
	//
	// Construtores
	//
	
	public PecaTorre() {
		super('T');
	}
	
	public PecaTorre(char tipoPeca) {
		super(tipoPeca);
	}
	
	
	//
	// Setters, Getters e Overrides Úteis implementados na superclasse
	//
	
	
	//
	// Lógica
	//
	
	@Override
	public boolean isMovimentoValido(Jogada jogada, Tabuleiro tabuleiro, boolean vezDasBrancas) {
		char auxChar = 'a';
		int deslocamentoAbsolutoX = jogada.getDeslocamentoX() > 0 ? jogada.getDeslocamentoX() : -1*jogada.getDeslocamentoX();
		int deslocamentoAbsolutoY = jogada.getDeslocamentoY() > 0 ? jogada.getDeslocamentoY() : -1*jogada.getDeslocamentoY();
		int deslocamentoAbsoluto = deslocamentoAbsolutoX > deslocamentoAbsolutoY ? deslocamentoAbsolutoX : deslocamentoAbsolutoY;
		int auxIntFromChar = 1;		
		boolean deslocamentoPositivoEmX = jogada.getDeslocamentoX() > 0 ? true : false;
		boolean deslocamentoPositivoEmY = jogada.getDeslocamentoY() > 0 ? true : false;
		boolean deslocamentoEmX = deslocamentoAbsolutoX > 0 ? true : false;
		Casa casaAux = new Casa();
		
		// Deslocamento deve ser de no minimo 1 casa
		if (deslocamentoAbsoluto < 1)
			return false;
		
		// Deslocamento deve ser de 0 em um dos eixos, e nao 0 no outro
		if (deslocamentoAbsolutoX != 0 && deslocamentoAbsolutoY != 0)
			return false;
		if (deslocamentoAbsolutoX == 0 && deslocamentoAbsolutoY == 0)
			return false;
		
		// Nao deve haver nenhuma peca no meio caminho
		if (deslocamentoEmX)
			if (deslocamentoPositivoEmX){ // Moveu-se para a direita
				for (int i = 1; i < deslocamentoAbsoluto; i++) {
					auxIntFromChar = tabuleiro.coordenadaXParaIndex(jogada.getCasaOrigem().getCoordenadaX()) + i;
					auxChar = tabuleiro.indexParaCoordenadaX(auxIntFromChar);
					casaAux.setCoordenadaX(auxChar);
					casaAux.setCoordenadaY(jogada.getCasaOrigem().getCoordenadaY());
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					if (casaAux.getPeca().isPeca())
						return false;
				}
			}
			else { // Moveu-se para a esquerda
				for (int i = 1; i < deslocamentoAbsoluto; i++) {
					auxIntFromChar = tabuleiro.coordenadaXParaIndex(jogada.getCasaOrigem().getCoordenadaX()) - i;
					auxChar = tabuleiro.indexParaCoordenadaX(auxIntFromChar);
					casaAux.setCoordenadaX(auxChar);casaAux.setCoordenadaY(jogada.getCasaOrigem().getCoordenadaY());
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					if (casaAux.getPeca().isPeca())
						return false;
				}
			}
		else 
			if (deslocamentoPositivoEmY) { // Moveu-se para frente
				for (int i = 1; i < deslocamentoAbsoluto; i++) {
					auxIntFromChar = tabuleiro.coordenadaXParaIndex(jogada.getCasaOrigem().getCoordenadaX());
					auxChar = tabuleiro.indexParaCoordenadaX(auxIntFromChar);
					casaAux.setCoordenadaX(auxChar);
					casaAux.setCoordenadaY(jogada.getCasaOrigem().getCoordenadaY() + i);
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					if (casaAux.getPeca().isPeca())
						return false;
				}
			}
			else { // Moveu-se para tras
				for (int i = 1; i < deslocamentoAbsoluto; i++) {
					auxIntFromChar = tabuleiro.coordenadaXParaIndex(jogada.getCasaOrigem().getCoordenadaX());
					auxChar = tabuleiro.indexParaCoordenadaX(auxIntFromChar);
					casaAux.setCoordenadaX(auxChar);
					casaAux.setCoordenadaY(jogada.getCasaOrigem().getCoordenadaY() - i);
					casaAux.setPeca(tabuleiro.getPecaEm(casaAux.getCoordenadaX(), casaAux.getCoordenadaY()));
					if (casaAux.getPeca().isPeca())
						return false;
				}				
			}
		
		return true;
	}	

	@Override
	public Stack<Casa> ameaca(Tabuleiro tabuleiro, Casa casa, boolean vezDasBrancas) {
		char coordX = 'a';
		int coordY = 1; // Lembrando que sera utilizado de 1 a 8
		int intFromCharAux = 0;
		Peca pecaAux = new Peca();
		Stack<Casa> casasAmeacadas = new Stack<Casa>();
		
		// A torre tem que ser verificada em 4 direcoes. Em cada uma delas,
		// percorre a coluna/fileira ate encontrar uma peca. Vai empilhando as casas pelo caminho. 
		// Se a ultima casa verificada for uma peca de outra cor, empilha ela tambem.
		
		// Frente
		for (int i = 1; i < 8; i++) {
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
			coordY = casa.getCoordenadaY() + i;
			coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux);
			
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
		
		// Tras
		for (int i = 1; i < 8; i++) {
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
			coordY = casa.getCoordenadaY() - i;
			coordX = tabuleiro.indexParaCoordenadaX(intFromCharAux);
			
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
		
		// Esquerda
		for (int i = 1; i < 8; i++) {
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
			coordY = casa.getCoordenadaY();
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
		
		// Direita
		for (int i = 1; i < 8; i++) {
			intFromCharAux = tabuleiro.coordenadaXParaIndex(casa.getCoordenadaX());
			coordY = casa.getCoordenadaY();
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
