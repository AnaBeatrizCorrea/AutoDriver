# AutoDriver
Código desenvolvido em conjunto com colegas de faculdade para Seminário de Técnicas de Programação e POO.
# Solução para o seguinte caso:
**AutoDrive — Gestão de Frota e Locadora de Veículos**

1. CENÁRIO DO PROBLEMA DE NEGÓCIO:
A locadora AutoDrive atua no mercado alugando carros de passeio, caminhões para transporte de carga pesada e motocicletas para entregas urbanas. Atualmente, o controle é feito em planilhas manuais. Isso tem gerado prejuízos sérios: clientes são cobrados com valores errados, veículos que deveriam estar na oficina são alugados por engano e não há controle sobre os custos de manutenção de cada categoria.

2. DESAFIOS QUE A ARQUITETURA POO DO GRUPO DEVE RESOLVER:
• Como estruturar o sistema para que o cálculo da diária considere adicionais específicos (eixos em caminhões, opcionais em carros e descontos para frotas de motos)?
• Como garantir que a oficina agende revisões e que um veículo em manutenção fique automaticamente impossibilitado de ser alugado?
• Como disponibilizar relatórios com o custo estimado de manutenção de cada veículo sem expor a lógica interna?

3. EXEMPLO DE CASO DE USO PARA TESTE NO MAIN:
Um cliente empresarial solicita a locação de um caminhão de 3 eixos por 7 dias. O sistema deve verificar se o caminhão está liberado (fora de manutenção), aplicar a taxa de eixos no cálculo da diária e registrar a locação na frota ativa.
