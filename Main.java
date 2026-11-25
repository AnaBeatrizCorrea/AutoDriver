import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

public class Main {

	private static Scanner entrada = new Scanner(System.in);
	private static Oficina oficina = new Oficina("AutoDrive", 5);

	public static void main(String[] args) {
		oficina.adicionarFrota(new Frota("Frota Principal"));
		System.out.println("=== AutoDrive - Gestão de Frota e Locadora ===");

		int opcao = -1;
		while (opcao != 0) {
			System.out.println();
			System.out.println("-------------- MENU --------------");
			System.out.println(" 1 - Criar nova frota");
			System.out.println(" 2 - Cadastrar veículo");
			System.out.println(" 3 - Listar frotas e veículos");
			System.out.println(" 4 - Consultar veículo por placa");
			System.out.println(" 5 - Alugar veículo");
			System.out.println(" 6 - Devolver veículo");
			System.out.println(" 7 - Agendar revisão");
			System.out.println(" 8 - Concluir revisão");
			System.out.println(" 9 - Relatório de custo de manutenção");
			System.out.println("10 - Ver frota ativa (veículos alugados)");
			System.out.println("11 - Executar caso de uso (caminhão de 3 eixos, 7 dias)");
			System.out.println(" 0 - Sair");
			opcao = lerInt("Escolha uma opção: ");

			try {
				switch (opcao) {
				case 1:
					criarFrota();
					break;
				case 2:
					cadastrarVeiculo();
					break;
				case 3:
					listarFrotas();
					break;
				case 4:
					consultarVeiculo();
					break;
				case 5:
					alugarVeiculo();
					break;
				case 6:
					oficina.devolverVeiculo(lerVeiculo());
					break;
				case 7:
					agendarRevisao();
					break;
				case 8:
					oficina.concluirRevisao(lerVeiculo());
					break;
				case 9:
					gerarRelatorio();
					break;
				case 10:
					listarFrotaAtiva();
					break;
				case 11:
					executarCasoDeUso();
					break;
				case 0:
					System.out.println("Encerrando o sistema. Até logo!");
					break;
				default:
					System.out.println("Opção inválida. Tente novamente.");
				}
			} catch (RuntimeException e) {
				System.out.println("Erro: " + e.getMessage());
			}
		}
	}

	private static void criarFrota() {
		Frota frota = new Frota(lerTexto("Nome da nova frota: "));
		oficina.adicionarFrota(frota);
		System.out.println("Frota criada: " + frota.getNome());
	}

	private static void cadastrarVeiculo() {
		Frota frota = escolherFrota();
		System.out.println("Tipo: 1 - Carro | 2 - Moto | 3 - Caminhão");
		int tipo = lerInt("Tipo: ");
		if (tipo < 1 || tipo > 3) {
			System.out.println("Tipo inválido.");
			return;
		}

		String nome = lerTexto("Nome: ");
		String modelo = lerTexto("Modelo: ");
		int ano = lerInt("Ano: ");
		String placa = lerTexto("Placa: ").toUpperCase();
		if (oficina.buscarVeiculo(placa) != null) {
			throw new IllegalArgumentException("Já existe um veículo com essa placa.");
		}
		BigDecimal preco = lerDecimal("Preço da diária (R$): ");
		BigDecimal km = lerDecimal("Quilometragem atual: ");

		// os dias de locação entram 0 aqui e são definidos na hora de alugar
		Veiculo veiculo;
		if (tipo == 1) {
			boolean ar = false;
			boolean vidro = false;
			if (lerSimNao("Deseja adicionar opcionais?")) {
				ar = lerSimNao("Ar-condicionado?");
				vidro = lerSimNao("Vidro automático?");
			}
			veiculo = new Carro(modelo, nome, ano, 0, placa, preco, km, ar, vidro);
		} else if (tipo == 2) {
			int qtd = lerInt("Quantidade de motos da frota (desconto a partir de 2): ");
			veiculo = new Moto(modelo, nome, ano, placa, preco, km, 0, BigDecimal.valueOf(qtd));
		} else {
			int eixos = lerInt("Quantidade de eixos: ");
			veiculo = new Caminhao(modelo, nome, ano, placa, preco, km, 0, eixos);
		}
		oficina.adicionarVeiculoNaFrota(frota, veiculo);
		System.out.println("Status inicial: " + veiculo.getStatus());
	}

	private static void listarFrotas() {
		for (Frota frota : oficina.getFrotas()) {
			System.out.println("--- " + frota.getNome() + " (" + frota.getQuantidadeVeiculos() + " veículo(s)) ---");
			for (Veiculo v : frota.getVeiculos()) {
				System.out.println("  - " + v.getClass().getSimpleName() + " | " + v.getNome() + " " + v.getModelo()
						+ " | Placa " + v.getPlaca() + " | Ano " + v.getAno() + " | Status: " + v.getStatus());
			}
		}
	}

	private static void consultarVeiculo() {
		Veiculo v = lerVeiculo();
		System.out.println("Veículo: " + v.getNome() + " " + v.getModelo() + " (" + v.getClass().getSimpleName() + ")");
		System.out.println("Placa: " + v.getPlaca() + " | Ano: " + v.getAno());
		System.out.println("Status: " + v.getStatus());
		System.out.println("Preço da diária: R$ " + dinheiro(v.getPrecoDiaria()));
		System.out.println("Quilometragem: " + v.getKm());
		System.out.println("Custo estimado de manutenção: R$ " + dinheiro(v.calcularCustoManutencao()));
		if (v.getStatus().equalsIgnoreCase("Alugado")) {
			System.out.println("Locação atual: " + v.getDiaria() + " dia(s) | Total: R$ " + dinheiro(v.getTotal()));
		}
	}

	private static void alugarVeiculo() {
		Veiculo v = lerVeiculo();
		String cliente = lerTexto("Nome do cliente: ");
		int dias = lerInt("Quantidade de dias: ");
		locar(v, cliente, dias);
	}

	private static void agendarRevisao() {
		Veiculo v = lerVeiculo();
		String data = lerLinha("Data da revisão (dd/MM/yyyy) [Enter = agora]: ");
		if (data.isEmpty()) {
			oficina.agendarRevisao(v);
		} else {
			oficina.agendarRevisao(v, data, lerLinha("Hora (HH:mm): "));
		}
	}

	private static void gerarRelatorio() {
		System.out.println("1 - Todas as frotas | 2 - Uma frota");
		if (lerInt("Opção: ") == 2) {
			oficina.gerarRelatorioManutencao(escolherFrota());
		} else {
			oficina.gerarRelatorioManutencao();
		}
	}

	private static void listarFrotaAtiva() {
		List<Veiculo> ativos = oficina.getFrotaAtiva();
		if (ativos.isEmpty()) {
			System.out.println("Nenhum veículo alugado no momento.");
			return;
		}
		for (Veiculo v : ativos) {
			System.out.println("- " + v.getClass().getSimpleName() + " " + v.getNome() + " (" + v.getPlaca() + ") | "
					+ v.getDiaria() + " dia(s) | Total: R$ " + dinheiro(v.getTotal()));
		}
	}

	// caso de uso do enunciado: cliente empresarial aluga caminhão de 3 eixos por 7 dias
	private static void executarCasoDeUso() {
		if (oficina.buscarVeiculo("CAM1A23") != null) {
			System.out.println("O caso de uso já foi executado nesta sessão.");
			return;
		}
		Frota frota = oficina.getFrotas().get(0);
		System.out.println("=== Caso de uso: cliente empresarial aluga caminhão de 3 eixos por 7 dias ===");

		Caminhao caminhao = new Caminhao("FH 540", "Volvo FH", 2022, "CAM1A23",
				new BigDecimal("400.00"), new BigDecimal("35000"), 0, 3);
		Carro corolla = new Carro("XEi 2.0", "Corolla", 2023, 0, "CAR2B34",
				new BigDecimal("180.00"), new BigDecimal("10000"), true, true);
		oficina.adicionarVeiculoNaFrota(frota, caminhao);
		oficina.adicionarVeiculoNaFrota(frota, corolla);

		System.out.println();
		System.out.println("1) Status do caminhão (precisa estar fora de manutenção): " + caminhao.getStatus());
		System.out.println("2) Locando por 7 dias, com a taxa de eixos no cálculo:");
		locar(caminhao, "Transportes Alfa Ltda", 7);

		System.out.println();
		System.out.println("3) Tentando alugar o mesmo caminhão de novo:");
		locar(caminhao, "Cliente Teste", 2);

		System.out.println();
		System.out.println("4) Corolla com 10000 km (" + corolla.getStatus() + "), tentando alugar:");
		locar(corolla, "Cliente Teste", 3);

		System.out.println();
		System.out.println("5) Oficina agenda a revisão e o Corolla continua bloqueado:");
		oficina.agendarRevisao(corolla);
		locar(corolla, "Cliente Teste", 3);

		System.out.println();
		System.out.println("6) Revisão concluída, o Corolla é liberado e pode ser alugado:");
		oficina.concluirRevisao(corolla);
		locar(corolla, "Cliente Teste", 3);

		System.out.println();
		System.out.println("7) Relatório de custo de manutenção:");
		oficina.gerarRelatorioManutencao();
	}

	private static void locar(Veiculo v, String cliente, int dias) {
		if (oficina.alugarVeiculo(v, dias)) {
			System.out.println("=== Locação registrada ===");
			System.out.println("Cliente: " + cliente);
			System.out.println("Veículo: " + v.getNome() + " (" + v.getPlaca() + ")");
			System.out.println("Dias: " + dias);
			System.out.println("Total: R$ " + dinheiro(v.getTotal()));
		} else {
			System.out.println("Locação não realizada.");
		}
	}

	private static Frota escolherFrota() {
		List<Frota> frotas = oficina.getFrotas();
		for (int i = 0; i < frotas.size(); i++) {
			System.out.println((i + 1) + " - " + frotas.get(i).getNome());
		}
		int escolha = lerInt("Escolha a frota: ");
		if (escolha < 1 || escolha > frotas.size()) {
			throw new IllegalArgumentException("Frota inválida.");
		}
		return frotas.get(escolha - 1);
	}

	private static Veiculo lerVeiculo() {
		Veiculo v = oficina.buscarVeiculo(lerTexto("Placa do veículo: "));
		if (v == null) {
			throw new IllegalArgumentException("Nenhum veículo encontrado com a placa informada.");
		}
		return v;
	}

	private static String dinheiro(BigDecimal valor) {
		return valor.setScale(2, RoundingMode.HALF_UP).toString();
	}

	// ---------- leitura do teclado ----------

	private static String lerLinha(String mensagem) {
		System.out.print(mensagem);
		if (!entrada.hasNextLine()) { // acabou a entrada
			System.out.println();
			System.exit(0);
		}
		return entrada.nextLine().trim();
	}

	private static String lerTexto(String mensagem) {
		String texto = lerLinha(mensagem);
		while (texto.isEmpty()) {
			System.out.println("O campo não pode ficar vazio.");
			texto = lerLinha(mensagem);
		}
		return texto;
	}

	private static int lerInt(String mensagem) {
		while (true) {
			try {
				return Integer.parseInt(lerLinha(mensagem));
			} catch (NumberFormatException e) {
				System.out.println("Valor inválido. Digite um número inteiro.");
			}
		}
	}

	private static BigDecimal lerDecimal(String mensagem) {
		while (true) {
			try {
				return new BigDecimal(lerLinha(mensagem).replace(',', '.'));
			} catch (NumberFormatException e) {
				System.out.println("Valor inválido. Digite um número (ex: 150,50).");
			}
		}
	}

	private static boolean lerSimNao(String mensagem) {
		while (true) {
			String texto = lerLinha(mensagem + " (s/n): ").toLowerCase();
			if (texto.equals("s")) {
				return true;
			}
			if (texto.equals("n")) {
				return false;
			}
			System.out.println("Responda com s ou n.");
		}
	}
}
