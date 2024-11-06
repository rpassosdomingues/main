import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

def read_data(filename):
    # Read data from the CSV file
    data = pd.read_csv(filename)
    return data

def plot_pie_chart(target_count0, target_count1):
    labels = ['Target 0', 'Target 1']
    sizes = [target_count0, target_count1]
    colors = ['lightcoral', 'lightskyblue']
    explode = (0.1, 0)  # explode 1st slice

    plt.figure(figsize=(8, 8))
    plt.pie(sizes, explode=explode, labels=labels, colors=colors, autopct='%1.1f%%', shadow=True, startangle=140)
    plt.title('Database Representativeness')
    plt.show()

def plot_boxplots(data, exclude_cols):
    # Boxplots or Violin Plots
    plt.figure(figsize=(12, 6))
    data.drop(columns=exclude_cols).boxplot()
    plt.title('Boxplots of Feature Statistics')
    plt.ylabel('Value')
    plt.show()

def plot_histograms(data, exclude_cols):
    # Histograms
    num_cols_to_plot = len(data.columns) - len(exclude_cols)
    num_rows = 5
    num_cols = 6

    fig, axes = plt.subplots(num_rows, num_cols, figsize=(10, 11))
    #fig.suptitle('Histograms of Feature Statistics', y=1.02)

    # Filter columns to plot
    cols_to_plot = [col for col in data.columns if col not in exclude_cols]

    for i, col in enumerate(cols_to_plot):
        row = i // num_cols
        col = i % num_cols

        # Utiliza iloc para acessar a coluna pelo índice numérico
        axes[row, col].hist(data.iloc[:, i], bins=20, edgecolor='black')
        #axes[row, col].set_title(cols_to_plot[i])
        #axes[row, col].set_xlabel('Value')
        #axes[row, col].set_ylabel('Frequency')

    # Remove subgráficos vazios
    for i in range(len(cols_to_plot), num_rows * num_cols):
        fig.delaxes(axes.flatten()[i])

    plt.tight_layout()
    plt.show()

def plot_correlation_heatmap(data, exclude_cols):
    # Correlation Heatmap
    plt.figure(figsize=(10, 8))
    correlation_matrix = data.drop(columns=exclude_cols).corr()
    plt.imshow(correlation_matrix, cmap='coolwarm', interpolation='none')
    plt.colorbar()
    plt.xticks(range(len(correlation_matrix)), correlation_matrix.columns)
    plt.yticks(range(len(correlation_matrix)), correlation_matrix.columns)
    plt.title('Correlation Heatmap')
    plt.show()

def plot_2d_scatter(data, target_col):
    # 2D Scatter Plots
    plt.figure(figsize=(18, 6))
    for col in data.columns:
        if col != target_col:
            plt.scatter(data[col], data[target_col], alpha=0.5, label=col)

    plt.title('2D Scatter Plot of Feature Statistics')
    plt.xlabel('Feature Value')
    plt.ylabel('Target')
    plt.legend()
    plt.show()

def comparative_analysis(data, exclude_cols):
    # Comparative Analysis (Strip Plot)
    plt.figure(figsize=(12, 6))
    
    # Melt the DataFrame for creating a strip plot
    melted_data = data.melt(id_vars=data.index.name, value_vars=[col for col in data.columns if col not in exclude_cols])
    
    ax = sns.stripplot(x='variable', y='value', data=melted_data, jitter=True, hue='variable', palette='Set2', legend=False)
    
    plt.title('Comparative Analysis of Feature Statistics')
    plt.xlabel('Feature Index')
    plt.ylabel('Value')
    
    plt.xticks(rotation=45, ha="right")  # Rotaciona os rótulos e alinha à direita
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    filename = "../data/input.csv"
    dataset = read_data(filename)
    
    target_col = "Target"
    exclude_cols = [target_col]
    
    target_count0 = dataset[target_col].value_counts().get(0, 0)
    target_count1 = dataset[target_col].value_counts().get(1, 0)
    plot_pie_chart(target_count0, target_count1)
    
    plot_boxplots(dataset, exclude_cols)
    plot_histograms(dataset, exclude_cols)
    plot_correlation_heatmap(dataset, exclude_cols)
    plot_2d_scatter(dataset, target_col)
    comparative_analysis(dataset, exclude_cols)
