import pandas as pd

# Load CSV file
df = pd.read_csv('input.csv', usecols=["Data", "À vista R$"])

# Remover todas as aspas e substituir pontos e vírgulas por nada
df['À vista R$'] = df['À vista R$'].str.replace('"', '').str.replace('.', '').str.replace(',', '.')

# Converter para float
df['À vista R$'] = df['À vista R$'].astype(float)

# Salvar o novo DataFrame em um novo arquivo CSV
df.to_csv('output.csv', index=False)
