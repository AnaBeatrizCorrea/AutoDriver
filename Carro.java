import java.math.BigDecimal;
import java.util.Scanner;

public class Carro extends Veiculo {

	private boolean arCondicionado;
	private boolean vidroAutomatico;
	private BigDecimal somaOpcionais = BigDecimal.ZERO;
	private static final BigDecimal TAXA_AR = new BigDecimal("30.00");
	private static final BigDecimal TAXA_VIDRO = new BigDecimal("15.00");

	public Carro(String modelo, String nome, int ano, int diaria,
	             String placa, BigDecimal precoDiaria,
	             BigDecimal km,
	             boolean arCondicionado,
	             boolean vidroAutomatico) {

		super(modelo, nome, placa, ano, diaria, km, precoDiaria);

		this.arCondicionado = arCondicionado;
		this.vidroAutomatico = vidroAutomatico;
	}

	public boolean isArCondicionado() {
		return arCondicionado;
	}

	public void setArCondicionado(boolean arCondicionado) {
		this.arCondicionado = arCondicionado;
	}

	public boolean isVidroAutomatico() {
		return vidroAutomatico;
	}

	public void setVidroAutomatico(boolean vidroAutomatico) {
		this.vidroAutomatico = vidroAutomatico;
	}

	// recebe o Scanner do programa para não abrir outro em System.in
	public void adicionarOpcionais(Scanner prompt) {
		System.out.println("Para um veiculo com Ar Condicionado: 0.");
		System.out.println("Para um veiculo com vidro automatico: 1.");
		System.out.println("Para um veiculo com ambos: 2.");
		System.out.print("Para nenhum dos anteriores: qualquer outra tecla: ");
		String opcoes = prompt.nextLine().trim();

		switch (opcoes) {
		case "0":
			arCondicionado = true;
			break;
		case "1":
			vidroAutomatico = true;
			break;
		case "2":
			vidroAutomatico = true;
			arCondicionado = true;
			break;
		default:
			System.out.println("Sem Ar Condicionado e Vidro Automático.");
		}
	}

	public void calcularOpcionais() {

		somaOpcionais = BigDecimal.ZERO;
		if (arCondicionado)
			somaOpcionais = somaOpcionais.add(TAXA_AR);
		if (vidroAutomatico)
			somaOpcionais = somaOpcionais.add(TAXA_VIDRO);

	}

	@Override
	public void calcularTotal() {
		super.calcularTotal();
		calcularOpcionais();
		setTotal(getTotal().add(somaOpcionais));
	}

	@Override
	public BigDecimal calcularCustoManutencao() {
		BigDecimal custo = new BigDecimal("250.00");
		if (arCondicionado)
			custo = custo.add(new BigDecimal("50.00"));
		if (vidroAutomatico)
			custo = custo.add(new BigDecimal("50.00"));
		return custo;
	}
}
