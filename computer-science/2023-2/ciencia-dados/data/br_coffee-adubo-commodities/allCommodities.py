import pandas as pd
import matplotlib.pyplot as plt

# Ler os dados do primeiro arquivo
data_file1 = "br_rice-wheat-corn-inflation.csv"
df1 = pd.read_csv(data_file1)

# Converter a coluna "Month" para um formato de data válido
df1['Date'] = pd.to_datetime(df1['Year'].astype(str) + '-' + df1['Month'], format='%Y-%b')
df1.set_index('Date', inplace=True)

# Ler os dados do segundo arquivo
data_file2 = "br_coffee_import-value_ipea.csv"
df2 = pd.read_csv(data_file2)
df2['Date'] = pd.to_datetime(df2['Date'], format='%Y/%m')
df2.set_index('Date', inplace=True)

# Criar um gráfico de séries temporais sobrepostas
plt.figure(figsize=(12, 6))

# Plotar séries do primeiro arquivo
plt.plot(df1.index, df1['Price_wheat_ton_infl'], label='Wheat', linestyle='-', marker='o')
plt.plot(df1.index, df1['Price_rice_ton_infl'], label='Rice', linestyle='-', marker='o')
plt.plot(df1.index, df1['Price_corn_ton_infl'], label='Corn', linestyle='-', marker='o')

# Plotar série do segundo arquivo
plt.plot(df2.index, df2['Price'], label='Coffee', linestyle='-', marker='o')

# Configurar rótulos e título do gráfico
plt.xlabel('Date')
plt.ylabel('Price')
plt.title('Overlapped Time Series')

# Adicionar legenda
plt.legend()

# Exibir o gráfico
plt.show()
