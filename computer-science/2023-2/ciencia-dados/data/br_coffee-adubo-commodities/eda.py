import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

def read_data(filename1, filename2):
    # Lê os dados dos arquivos CSV
    data1 = pd.read_csv(filename1, parse_dates=['Date'], index_col='Date')
    data2 = pd.read_csv(filename2, parse_dates=['Date'], index_col='Date')
    return data1, data2

def plot_time_series(data1, data2):
    # Série Temporal Sobreposta
    plt.figure(figsize=(18, 6))
    plt.plot(data1.index, data1['Production'], label='Adubo', marker='o')
    plt.plot(data2.index, data2['Price'], label='Café', marker='o')
    
    plt.title('Adubo VS Café')
    plt.xlabel('Data')
    plt.ylabel('Produção e Valor de Importação')
    plt.legend()
    plt.show()

if __name__ == "__main__":
    filename1 = "br_adubo_ipea_1991-2006.csv"
    filename2 = "br_coffee_import-value_1990-2006_ipea.csv"
    dataset1, dataset2 = read_data(filename1, filename2)
    
    plot_time_series(dataset1, dataset2)