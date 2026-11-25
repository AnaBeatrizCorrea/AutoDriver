import java.math.*;
import java.util.*;


public class Caminhao extends Veiculo {

	
	private static final BigDecimal TAXA_POR_EIXO = new BigDecimal("50.00");
	private int eixos;


	public Caminhao(String modelo, String nome, int ano, String placa, BigDecimal precoDiaria, BigDecimal km, int diaria, int eixos) {
		super(modelo, nome, placa, ano, diaria, km, precoDiaria);
		setQuantidadeEixos(eixos);
	}

	public int getQuantidadeEixos() {
		return eixos;
	}

	public void setQuantidadeEixos(int eixos) {
		if (eixos < 2) {
			throw new IllegalArgumentException(
			    "O caminhao precisa ter pelo menos 2 eixos."
			);
		}

		this.eixos = eixos;
	}


	@Override
	public void calcularTotal() {
	    super.calcularTotal();
	    BigDecimal totalPorEixo = (TAXA_POR_EIXO.multiply(BigDecimal.valueOf(getQuantidadeEixos())));
	    setTotal(totalPorEixo.add(getTotal()));
	}
	
	@Override
    public BigDecimal calcularCustoManutencao() {
        BigDecimal custoEixos = new BigDecimal("150.00").multiply(BigDecimal.valueOf(eixos));
        return new BigDecimal("600.00").add(custoEixos);
    }

}
