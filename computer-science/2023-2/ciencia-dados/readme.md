# Desvendando novos vínculos da Equação do Preço das Commodities Agrícolas no Brasil

## Objetivos
Buscar correlações entre a variação de preços dos insumos e mudanças climáticas com a variação de preço do café, arroz, milho, trigo e outras commodities agrícolas do mercado brasileiro, isolando variáveis subjetivas intrísecas e extrinsecas a fim de cruzar eventos isolados de variação de preço e extrair features relevantes para previsão do preço das commodities separadamente em médio a longo prazo, indiferente à eventos de oferta e demanda, acordo de cavalheiros, dentre outros fatores.

## Estudos de caso

1. Café VS Inflação e Dolar
2. Café VS Adubo e Agentes Preventivos
3. Café VS Arroz, Trigo e Milho
4. Café VS Dados Climáticos

## Bases de dados selecionadas

- Inflação                                                    | Fonte: http://www.ipeadata.gov.br/Default.aspx
- Dolar                                                       | Fonte: http://www.ipeadata.gov.br/Default.aspx
- Preço de insumos: Adubo e agentes preventivos contra pragas | Fonte: http://www.ipeadata.gov.br/Default.aspx
- Arroz, Trigo e Milho                                        | Fonte: https://www.cepea.esalq.usp.br/br/consultas-ao-banco-de-dados-do-site.aspx
- Índice pluviométrico, umidade e temperatura                 | Fonte: http://sinda.crn.inpe.br/PCD/SITE/novo/site/historico/index.php

## Próximos Passos

- [Feito]     Combinar modelos aos pares
- [Andamento] Obter métricas de desempenho para cada par de modelos combinados
-             Encontrar a combinação de modelos que melhor se ajusta à série temporal 
- [Feito]     Combinar séries temporais isoladamente, cruzando todos os dados possíveis, na mesma janela de tempo
-             Encontrar as combinações de séries temporais fortemente correlacionadas e anti-correlacionadas