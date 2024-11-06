"""
_________________________________________________________________________________________
_________________________________________________________________________________________
The code implements a time series forecasting model
for coffee values in Brazil.
-----------------------------------------------------------------------------------------
Author: Rafael Passos Domingues
-----------------------------------------------------------------------------------------
It loads historical data, trains machine learning models (SVR, MLP, RandomForest),
makes predictions using a sliding window approach, and evaluates the models' performance,
including comparison with a moving average technique.

Finally, it generates visualizations to show predictions against observed data.
_________________________________________________________________________________________
_________________________________________________________________________________________
"""
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

# Train Models
from sklearn.svm import SVR
from xgboost import XGBRegressor
from sklearn.neural_network import MLPRegressor
from sklearn.ensemble import RandomForestRegressor

from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
from statsmodels.graphics.tsaplots import plot_acf, plot_pacf
from sklearn.metrics import mean_squared_error, mean_absolute_error

class ForecastingModel:
    def __init__(self, file_path):
        self.file_path = file_path
        self.df = None
        self.load_data()
        self.lag = 5

    def load_data(self):
        self.df = pd.read_csv(self.file_path, parse_dates=['Data'], dayfirst=True)
        self.df['Forecast'] = self.df['À vista R$'].astype(float)

    def train_svr(self, X, y):
        X_df = pd.DataFrame(X, columns=['Forecast'])
        svr_model = SVR()
        svr_model.fit(X_df, y)

        return svr_model
    
    def train_xgb(self, X, y):
        X_df = pd.DataFrame(X, columns=['Forecast'])
        xgb_model = XGBRegressor()
        xgb_model.fit(X_df, y)

        return xgb_model

    def train_mlp(self, X, y):
        X_df = pd.DataFrame(X, columns=['Forecast'])
        mlp_model = make_pipeline(StandardScaler(), MLPRegressor())
        mlp_model.fit(X_df, y)

        return mlp_model

    def train_random_forest(self, X, y):
        rf_model = RandomForestRegressor()
        rf_model.fit(X, y)

        return rf_model
    
    def ensemble(self, model1_output, model2_output):
        """
        Combine the outputs of two models by averaging them.

        Parameters:
        - model1_output: Pandas DataFrame or NumPy array, output of the first model.
        - model2_output: Pandas DataFrame or NumPy array, output of the second model.

        Returns:
        - ensemble_output: Pandas DataFrame, average of the two model outputs.
        """
        if isinstance(model1_output, pd.DataFrame):
            model1_output = model1_output.values

        if isinstance(model2_output, pd.DataFrame):
            model2_output = model2_output.values

        # Calculate the average of the two model outputs
        ensemble_output = (model1_output + model2_output) / 2
        ensemble_forecast = pd.DataFrame(ensemble_output, columns=['Forecast'])

        return ensemble_forecast
    
    def calculate_y_true_pred(self, forecast_df, forecast_column='Forecast'):
        """
        Extracts specific columns from a forecast_df DataFrame for
        observed values (y_true) and predicted values (y_pred).

        Returns:
        - y_true: DataFrame containing the column corresponding to the observed values.
        The column name is "Observed".
        - y_pred: DataFrame containing the column corresponding to the model predictions.
        The column name is determined by the forecast_column argument.
        """
        y_true = forecast_df[['Observed']]
        y_pred = forecast_df[[forecast_column]]

        return y_true, y_pred

    def forecast_sliding_window(self, X_test, y_test, steps, model_fit, feature_names):
        """
        Uses a trained model to perform successive predictions with a sliding window approach,
        continually updating the last observation with the predicted values.
        
        The result is a DataFrame containing the corresponding predictions.
        """
        last_observation_X = X_test.iloc[-self.lag:].values.flatten()
        last_observation_y = y_test.iloc[-self.lag:].values.flatten()

        forecast_data = []
        for i in range(steps):
    
            # Combine X and y for the last observation
            last_observation = np.concatenate([last_observation_X, last_observation_y])

            # Forecast the next value using the model
            forecasted_value = model_fit.predict(pd.DataFrame([last_observation], columns=X_test.columns))[0]

            # Update the last observation for X
            last_observation_X[:-1] = last_observation_X[1:]
            last_observation_X[-1] = forecasted_value

            # Update the last observation for y
            last_observation_y[:-1] = last_observation_y[1:]
            last_observation_y[-1] = forecasted_value

            forecast_data.append(forecasted_value)

        forecast_df = pd.DataFrame({'Forecast': forecast_data})

        return forecast_df
    
    def train_test_part(self):
        base_knowledge = self.univariate_to_tabular()
        X = base_knowledge.iloc[:, :-1]
        y = base_knowledge.iloc[:, -1]

        feature_names = [f'X{i}' for i in range(X.shape[1])]
        X.columns = feature_names

        # Use train_test_split from scikit-learn to split the data
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

        return X_train, X_test, y_train, y_test

    def train_models(self, X_train, y_train):
        svm_model = SVR()
        mlp_model = MLPRegressor()
        rf_model = RandomForestRegressor()
        xgb_model = self.train_xgb(X_train, y_train)

        svm_model.fit(X_train, y_train)
        mlp_model.fit(X_train, y_train)
        rf_model.fit(X_train, y_train)
        xgb_model.fit(X_train, y_train)

        return svm_model, mlp_model, rf_model, xgb_model

    def train_and_forecast(self, full_forecast_steps):
        X_train, X_test, y_train, y_test = self.train_test_part()
        svr_model, mlp_model, rf_model, xgb_model = self.train_models(X_train, y_train)

        svr_forecast = self.forecast_sliding_window(X_test, y_test, full_forecast_steps, svr_model, feature_names=['Data'])
        mlp_forecast = self.forecast_sliding_window(X_test, y_test, full_forecast_steps, mlp_model, feature_names=['Data'])
        rf_forecast = self.forecast_sliding_window(X_test, y_test, full_forecast_steps, rf_model, feature_names=['Data'])
        xgb_forecast = self.forecast_sliding_window(X_test, y_test, full_forecast_steps, xgb_model, feature_names=['Data'])
    
        return svr_forecast, mlp_forecast, rf_forecast, xgb_forecast

    def simulate_future_values(self, size=10):
        values_to_simulate = pd.DataFrame(index=self.df.index[-size:])
        values_to_simulate['Observed'] = self.df['À vista R$'][-size:].values
        values_to_simulate.reset_index(drop=True, inplace=True)

        return values_to_simulate

    def univariate_to_tabular(self):
        series = self.df['Forecast'].values
        df_tabular = pd.DataFrame(index=self.df.index[self.lag:])

        for i in range(self.lag):
            df_tabular[f'X{i}'] = np.roll(series, i + 1)[self.lag:]
    
        df_tabular['Y'] = series[self.lag:]
        df_tabular.reset_index(drop=True, inplace=True)

        return df_tabular

    def dynamic_head_tail_adjustment(self, iteration, total_iterations):
        """
        Dynamically adjusts the size of the "head" and "tail" parts
        throughout iterations, allowing for a more comprehensive exploration of the data.
        """
        desired_ratio_head = 0.5  # 50% of data as extreme
        desired_ratio_tail = 0.5  # 50% of data as intermediate part

        current_ratio_head = (iteration + 1) / total_iterations
        current_ratio_tail = 1 - current_ratio_head

        split_point = len(self.df) // 2

        head_size = int(split_point * current_ratio_head * desired_ratio_head)
        tail_size = int(split_point * current_ratio_tail * desired_ratio_tail)

        return head_size, tail_size

    def calculate_metrics(self, y_true, y_pred):
        mse = mean_squared_error(y_true, y_pred)
        mae = mean_absolute_error(y_true, y_pred)
        rmse = np.sqrt(mse)
        mape = np.mean(np.abs((y_true - y_pred) / y_true)) * 100

        return mse, mae, rmse, mape
    
    def evaluate_performance(self, forecast_df, iteration, total_iterations, model_names):
        head_size, tail_size = self.dynamic_head_tail_adjustment(iteration, total_iterations)

        # Lists to store true and predicted values for each pair of models
        y_true_list, y_pred_list = [], []

        # Iterate through the pairs of models
        for model_name1, model_name2 in [('SVM', 'XGBoost'), ('SVM', 'MLP'), ('SVM', 'Random Forest')]:
            # Get the forecast for each model
            model1_forecast = forecast_df[model_name1].values[:head_size] + forecast_df[model_name1].values[-tail_size:]
            model2_forecast = forecast_df[model_name2].values[:head_size] + forecast_df[model_name2].values[-tail_size:]

            # Combine the outputs of two models by averaging them
            ensemble_forecast = self.ensemble(pd.DataFrame({'Forecast': model1_forecast}), pd.DataFrame({'Forecast': model2_forecast}))

            # Extract 'Forecast' column using the correct column name
            y_true, y_pred = self.calculate_y_true_pred(forecast_df.iloc[:head_size + tail_size], forecast_column='Forecast')

            # Append to the lists
            y_true_list.append(y_true)
            y_pred_list.append(ensemble_forecast['Forecast'].values)

            # Calculate and print metrics for each pair
            mse, mae, rmse, mape = self.calculate_metrics(y_true, pd.DataFrame({'Forecast': ensemble_forecast['Forecast']}))
            print('___________________________________')
            print(f'Metrics for {model_name1} + {model_name2}:')
            print('-----------------------------------')
            print('MSE: ', mse)
            print('MAE: ', mae)
            print('RMSE: ', rmse)
            print('MAPE: ', mape)
            print('___________________________________')

        # Choose one pair to plot
        chosen_model1, chosen_model2 = 'SVM', 'XGBoost'
        ensemble_forecast = self.ensemble(pd.DataFrame({'Forecast': forecast_df[chosen_model1]}), pd.DataFrame({'Forecast': forecast_df[chosen_model2]}))

        self.y_true, self.y_pred = self.calculate_y_true_pred(forecast_df.iloc[:head_size + tail_size], forecast_column='Forecast')

        self.plot_learning_performance(y_true, y_pred, chosen_model1, chosen_model2)

        return y_true_list, y_pred_list
    
    def plot_partial_autocorrelation_function(self):
        (ax1, ax2) = plt.subplots(1, 2, figsize=(12, 4))
        plot_acf(self.df['Forecast'].values, lags=20, ax=ax1)
        ax1.set_title("Autocorrelation Function (ACF)")
        plot_pacf(self.df['Forecast'].values, lags=20, ax=ax2)
        ax2.set_title("Partial Autocorrelation Function (PACF)")
        plt.show()

    def plot_learning_performance(y_true, y_pred, chosen_model1, chosen_model2):
        min_len = min(len(y_true), len(y_pred))

        # Plot the chosen pair
        plt.figure(figsize=(10, 4))
        plt.plot(y_true.index[:min_len], y_true.iloc[:min_len, 0], label='True', marker='o')
        plt.plot(y_true.index[:min_len], y_pred.iloc[:min_len, 0], label='Ensemble', marker='o')
        plt.title(f'Moving Average Technique for the price - {chosen_model1} + {chosen_model2}')
        plt.xlabel('Index')
        plt.ylabel('Value')
        plt.legend()
        plt.show()
    
    def plot_time_series_with_forecast(self, future_values_df, ensemble_forecast, model_names):
        print(future_values_df.columns)
        plt.figure(figsize=(12, 6))

        # Convert ensemble_forecast to a DataFrame
        ensemble_forecast_df = pd.DataFrame({'Forecast': ensemble_forecast}, index=self.df['Data'].iloc[-len(ensemble_forecast):])

        # Plotting observed values
        plt.plot(self.df['Data'][:-len(future_values_df)], self.df['Forecast'][:-len(future_values_df)], label='Observed', marker='o')

        # Plotting true future values
        plt.plot(self.df['Data'][-len(future_values_df):], future_values_df['Observed'].values, label='Moving Average', marker='o')

        # Plotting ensemble forecast
        plt.plot(ensemble_forecast_df.index, ensemble_forecast_df['Forecast'].values, label='Ensemble', marker='o')

        """
        # Plotting individual model forecasts
        for i, model_name in enumerate(model_names):
            model_forecast = future_values_df[model_name].values[:len(ensemble_forecast)]
            plt.plot(self.df['Data'][-len(model_forecast):], model_forecast, label=f'{model_name} Forecast', marker='o')
        """

        plt.title('Coffee Price Forecasting - Ensemble Models')
        plt.xlabel('Date')
        plt.ylabel('Price (R$)')
        plt.legend()
        plt.show()

    def main(self, total_iterations=1):
        future_values_df = self.simulate_future_values()

        full_forecast_steps = len(self.df)
        for iteration in range(total_iterations):

            svr_forecast, mlp_forecast, rf_forecast, xgb_forecast = self.train_and_forecast(full_forecast_steps)

            ensemble_svm_xgb = self.ensemble(svr_forecast, xgb_forecast)
            ensemble_svm_mlp = self.ensemble(svr_forecast, mlp_forecast)
            ensemble_all = self.ensemble(ensemble_svm_xgb, ensemble_svm_mlp)

            # Plot ensemble vs observed for each iteration
            model_names = ['SVM', 'XGBoost', 'MLP', 'Random Forest']
            ensemble_forecast = self.ensemble(ensemble_svm_xgb, ensemble_svm_mlp)['Forecast'].values

            forecast_df = pd.DataFrame({
                'Observed': future_values_df['Observed'].values,
                'SVM': svr_forecast['Forecast'].values,
                'XGBoost': xgb_forecast['Forecast'].values,
                'MLP': mlp_forecast['Forecast'].values,
                'Random Forest': rf_forecast['Forecast'].values,
                'Ensemble SVM+XGBoost': ensemble_svm_xgb['Forecast'].values,
                'Ensemble SVM+MLP': ensemble_svm_mlp['Forecast'].values,
                'Ensemble All Models': ensemble_all['Forecast'].values
            })
            # Pass X_test and y_test to evaluate_performance
            X_test, y_test = self.train_test_part()
            self.evaluate_performance(X_test, y_test, forecast_df, iteration, total_iterations, model_names)
            self.plot_time_series_with_forecast(future_values_df, ensemble_forecast, model_names)

# File path for the dataset
file_path = "../data/br_coffee-price_inflation/output.csv"

# Create an instance of the ForecastingModel with the correct file path
forecast_model = ForecastingModel(file_path)

# Execute the main function
forecast_model.main(total_iterations=1)