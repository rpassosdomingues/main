import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

def read_data(filename):
    # Lê os dados do arquivo CSV
    data = pd.read_csv(filename)
    return data

def plot_pie_chart(data):
    # Gráfico de Pizza para a distribuição da variável 'Species'
    species_counts = data['Species'].value_counts()
    plt.figure(figsize=(8, 8))
    plt.pie(species_counts, labels=species_counts.index, autopct='%1.1f%%', startangle=140, colors=['lightcoral', 'lightskyblue'])
    plt.title('Distribuição da Variável "Species"')
    plt.show()

def plot_boxplots(data, exclude_cols):
    # Boxplots para as colunas numéricas
    plt.figure(figsize=(12, 6))
    data.drop(columns=exclude_cols).boxplot()
    plt.title('Boxplots das Estatísticas das Características')
    plt.ylabel('Valor')
    plt.show()

def plot_histograms(data, exclude_cols):
    # Histogramas
    num_cols_to_plot = len(data.columns) - len(exclude_cols)
    num_rows = 3
    num_cols = min(num_cols_to_plot, 6)  # Ajusta o número de colunas para no máximo 6

    fig, axes = plt.subplots(num_rows, num_cols, figsize=(15, 10))

    # Filtra colunas numéricas para plotar, excluindo 'Species' e outras colunas especificadas
    numeric_cols_to_plot = [col for col in data.columns if pd.api.types.is_numeric_dtype(data[col]) and col not in exclude_cols]

    for i, col in enumerate(numeric_cols_to_plot):
        row = i // num_cols
        col = i % num_cols

        axes[row, col].hist(data[col], bins=20, edgecolor='black')
        axes[row, col].set_title(col)
        axes[row, col].set_xlabel('Valor')
        axes[row, col].set_ylabel('Frequência')

    # Remove subgráficos vazios
    for i in range(len(numeric_cols_to_plot), num_rows * num_cols):
        fig.delaxes(axes.flatten()[i])

    plt.tight_layout()
    plt.show()

def plot_correlation_heatmap(data, exclude_cols):
    # Matriz de Correlação
    plt.figure(figsize=(10, 8))
    correlation_matrix = data.drop(columns=exclude_cols).corr()
    sns.heatmap(correlation_matrix, cmap='coolwarm', annot=True, fmt=".2f")
    plt.title('Matriz de Correlação')
    plt.show()

def plot_2d_scatter(data, target_col):
    # Gráfico de Dispersão 2D
    plt.figure(figsize=(18, 6))
    for col in data.columns:
        if col != target_col:
            plt.scatter(data[col], data[target_col], alpha=0.5, label=col)

    plt.title('Gráfico de Dispersão 2D das Características')
    plt.xlabel('Valor da Característica')
    plt.ylabel('Variável Alvo')
    plt.legend()
    plt.show()

def comparative_analysis(data, exclude_cols):
    # Análise Comparativa (Strip Plot)
    plt.figure(figsize=(12, 6))
    
    # Derrete o DataFrame para criar um strip plot
    melted_data = data.melt(id_vars=data.index.name, value_vars=[col for col in data.columns if col not in exclude_cols])
    
    ax = sns.stripplot(x='variable', y='value', data=melted_data, jitter=True, hue='variable', palette='Set2', legend=False)
    
    plt.title('Análise Comparativa das Estatísticas das Características')
    plt.xlabel('Índice da Característica')
    plt.ylabel('Valor')
    
    plt.xticks(rotation=45, ha="right")  # Rotaciona os rótulos e alinha à direita
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    filename = "output.csv"  # Substitua pelo nome do seu arquivo CSV
    dataset = read_data(filename)
    
    # Gráfico de Pizza para 'Species'
    plot_pie_chart(dataset)
    
    exclude_cols = ["Species"]
    
    plot_boxplots(dataset, exclude_cols)
    #plot_histograms(dataset, exclude_cols)
    plot_correlation_heatmap(dataset, exclude_cols)
    plot_2d_scatter(dataset, 'Species')
    comparative_analysis(dataset, exclude_cols)
