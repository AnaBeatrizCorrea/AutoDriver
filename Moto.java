import java.util.*;
import java.math.*;

public class Moto extends Veiculo {

	private BigDecimal desconto = BigDecimal.ZERO;
	private BigDecimal qtd = BigDecimal.ZERO;

	public Moto(String modelo, String nome, int ano, String placa, BigDecimal precoDiaria, BigDecimal km, int diaria, BigDecimal qtd) {
		super(modelo, nome, placa, ano, diaria, km, precoDiaria);
		setQtd(qtd);
	}

	public BigDecimal getQtd() {
		return qtd;
	}

	public void setQtd(BigDecimal qtd) {
		if (qtd == null || qtd.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("A quantidade de motos não pode ser nula ou negativa.");
		}
		this.qtd = qtd;
	}

	public BigDecimal gerarDesconto() {
		BigDecimal minFrota = new BigDecimal("02.00");
		desconto = BigDecimal.ZERO;
		if (minFrota.compareTo(qtd) <= 0) {
			desconto = new BigDecimal("10.00");
			desconto = desconto.multiply(qtd);
		}
		return desconto;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		if (desconto == null || desconto.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("O desconto não pode ser nulo ou negativo.");
		}
		this.desconto = desconto;
	}

	@Override
	public void calcularTotal() {
		super.calcularTotal();
		gerarDesconto();
		// o desconto não deixa o total ficar negativo
		setTotal(getTotal().subtract(getDesconto()).max(BigDecimal.ZERO));
	}

	@Override
	public BigDecimal calcularCustoManutencao() {
		return new BigDecimal("120.00").add(getKm().multiply(new BigDecimal("0.02"))).setScale(2, RoundingMode.HALF_UP);
	}

}
