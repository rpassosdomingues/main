import pandas as pd

# Load CSV file
df = pd.read_csv('br_coffee_import-value.csv', usecols=["Date", "Price"])

# Converte a coluna 'Date' para o formato de data do pandas
df['Date'] = pd.to_datetime(df['Date'], format='%b-%y')

# Extrai o ano e o mês e cria as novas colunas 'ano' e 'mes'
#df['ano'] = df['Date'].dt.year
#df['mes'] = df['Date'].dt.month

# Formata a coluna 'Date' no novo formato desejado
df['Date'] = df['Date'].dt.strftime('%Y/%m')

# Salva o novo DataFrame para um novo arquivo CSV
df.to_csv('output.csv', index=False)
