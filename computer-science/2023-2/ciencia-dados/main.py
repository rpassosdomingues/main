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
from mlxtend.regressor import StackingRegressor
from sklearn.neural_network import MLPRegressor
from sklearn.ensemble import RandomForestRegressor
from sklearn.linear_model import LinearRegression, Ridge

from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import StandardScaler
from statsmodels.graphics.tsaplots import plot_acf, plot_pacf
from sklearn.metrics import mean_squared_error, mean_absolute_error

class ForecastingModel:
    def __init__(self, file_path):
        self.file_path = file_path
        self.df = None
        self.load_data()

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
    
    def train_models(self, X_train, y_train):
        svr_model = self.train_svr(X_train, y_train)
        xgb_model = self.train_xgb(X_train, y_train)
        mlp_model = self.train_mlp(X_train, y_train)
        rf_model = self.train_random_forest(X_train, y_train)
        return svr_model, xgb_model, mlp_model, rf_model
    
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
    
    def calculate_y_true_pred(self, forecast_df, forecast_column='Ensemble All Models'):
        y_true = forecast_df[['Observed']]
        y_pred = forecast_df[[forecast_column]]

        return y_true, y_pred

    def forecast_sliding_window(self, X_test, steps, model_fit, lag, feature_names):
        last_observation = X_test.iloc[-lag:].values.flatten()
        forecast_data = []

        for i in range(steps):
            forecasted_value = model_fit.predict(last_observation[-lag:].reshape(1, -1))[0]
            last_observation[:-1] = last_observation[1:]
            last_observation[-1] = forecasted_value
            forecast_data.append(forecasted_value)

        forecast_df = pd.DataFrame({'Forecast': forecast_data})
        return forecast_df
    """
    def train_and_forecast(self, head_size, X, y, X_test, forecast_steps):
        
        svr_model = self.train_svr(X, y)
        mlp_model = self.train_mlp(X, y)
        rf_model = self.train_random_forest(X, y)
        xgb_model = self.train_xgb(X, y)

        svr_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, svr_model, lag=4, feature_names=['Forecast'])
        mlp_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, mlp_model, lag=4, feature_names=['Forecast'])
        rf_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, rf_model, lag=4, feature_names=['Forecast'])
        xgb_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, xgb_model, lag=4, feature_names=['Forecast'])

        return svr_forecast, mlp_forecast, rf_forecast, xgb_forecast
    """
    def train_and_forecast(self, head_size, X_test, forecast_steps=5, lag=5):
        # Convert date-time to numerical feature
        X = self.df[['Data']].copy()
        X['DaysSinceStart'] = (X['Data'] - X['Data'].min()).dt.days
        X.drop('Data', axis=1, inplace=True)

        y = self.df['Forecast'].values

        svr_model = self.train_svr(X, y)
        mlp_model = self.train_mlp(X, y)
        rf_model = self.train_random_forest(X, y)
        xgb_model = self.train_xgb(X, y)

        # Convert date-time in the test set to numerical feature
        X_test = X_test.copy()
        X_test['DaysSinceStart'] = (X_test['Data'] - self.df['Data'].min()).dt.days
        X_test.drop('Data', axis=1, inplace=True)

        svr_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, svr_model, lag=4, feature_names=['DaysSinceStart'])
        mlp_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, mlp_model, lag=4, feature_names=['DaysSinceStart'])
        rf_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, rf_model, lag=4, feature_names=['DaysSinceStart'])
        xgb_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, xgb_model, lag=4, feature_names=['DaysSinceStart'])

        return svr_forecast, mlp_forecast, rf_forecast, xgb_forecast

    def simulate_future_values(self, size=10):
        values_to_simulate = pd.DataFrame(index=self.df.index[-size:])
        values_to_simulate['Observed'] = self.df['À vista R$'][-size:].values
        values_to_simulate.reset_index(drop=True, inplace=True)
        return values_to_simulate

    def plot_acf_pacf(self):
        fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(12, 4))
        plot_acf(self.df['Forecast'].values, lags=20, ax=ax1)
        ax1.set_title("Autocorrelation Function (ACF)")
        plot_pacf(self.df['Forecast'].values, lags=20, ax=ax2)
        ax2.set_title("Partial Autocorrelation Function (PACF)")
        plt.show()

    def univariate_to_tabular(self, lag):
        series = self.df['Forecast'].values
        df_tabular = pd.DataFrame(index=self.df.index[lag:])

        for i in range(lag):
            df_tabular[f'X{i}'] = np.roll(series, i + 1)[lag:]
    
        df_tabular['Y'] = series[lag:]
        df_tabular.reset_index(drop=True, inplace=True)

        return df_tabular 

    def train_test_split(self, lag=4):
        base_knowledge = self.univariate_to_tabular(lag=lag)
        X = base_knowledge.iloc[:, :-1]
        y = base_knowledge.iloc[:, -1]

        feature_names = [f'X{i}' for i in range(X.shape[1])]
        X.columns = feature_names

        X_train, X_test, y_train, y_test = X, X, y, y
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

    def dynamic_head_tail_adjustment(self, iteration, total_iterations):
        desired_ratio = 0.5
        current_ratio = (iteration + 1) / total_iterations
        split_point = len(self.df) // 2
        head_size = int(split_point * current_ratio * desired_ratio)
        tail_size = len(self.df) - head_size
        return head_size, tail_size

    def calculate_metrics(self, y_true, y_pred):
        mse = mean_squared_error(y_true, y_pred)
        mae = mean_absolute_error(y_true, y_pred)
        rmse = np.sqrt(mse)
        mape = np.mean(np.abs((y_true - y_pred) / y_true)) * 100

        return mse, mae, rmse, mape
    
    def evaluate_performance(self, forecast_df, iteration, total_iterations, model_names):
        future_values_df = self.simulate_future_values()
        head_size, tail_size = self.dynamic_head_tail_adjustment(iteration, total_iterations)

        # Lists to store true and predicted values for each pair of models
        y_true_list, y_pred_list = [], []

        # Iterate through the pairs of models
        for model_name1, model_name2 in [('SVM', 'XGBoost'), ('SVM', 'MLP'), ('SVM', 'Random Forest')]:
            # Get the forecast for each model
            model1_forecast = forecast_df[model_name1].values[:head_size]
            model2_forecast = forecast_df[model_name2].values[:head_size]

            # Combine the outputs of two models by averaging them
            ensemble_forecast = self.ensemble(pd.DataFrame({'Forecast': model1_forecast}), pd.DataFrame({'Forecast': model2_forecast}))

            # Extract 'Forecast' column using the correct column name
            y_true, y_pred = self.calculate_y_true_pred(forecast_df.iloc[:head_size], forecast_column='Ensemble All Models')

            # Append to the lists
            y_true_list.append(y_true)
            y_pred_list.append(ensemble_forecast['Forecast'].values)

            # Calculate and print metrics for each pair
            mse, mae, rmse, mape = self.calculate_metrics(y_true, pd.DataFrame({'Forecast': ensemble_forecast['Forecast']}))
            print('______________________________')
            print(f'Metrics for {model_name1} + {model_name2}:')
            print('------------------------------')
            print('MSE: ', mse)
            print('MAE: ', mae)
            print('RMSE: ', rmse)
            print('MAPE: ', mape)
            print('______________________________')

        # Choose one pair to plot
        chosen_model1, chosen_model2 = 'SVM', 'XGBoost'
        ensemble_forecast = self.ensemble(pd.DataFrame({'Forecast': forecast_df[chosen_model1]}), pd.DataFrame({'Forecast': forecast_df[chosen_model2]}))

        self.y_true, self.y_pred = self.calculate_y_true_pred(forecast_df.iloc[:head_size], forecast_column='Ensemble All Models')
        min_len = min(len(self.y_true), len(self.y_pred))

        # Plot the chosen pair
        plt.figure(figsize=(10, 4))
        plt.plot(self.y_true.index[:min_len], self.y_true.iloc[:min_len, 0], label='True', marker='o')
        plt.plot(self.y_true.index[:min_len], self.y_pred.iloc[:min_len, 0], label='Ensemble', marker='o')
        plt.title(f'Moving Average Technique for the price - {chosen_model1} + {chosen_model2}')
        plt.xlabel('Index')
        plt.ylabel('Value')
        plt.legend()
        plt.show()

        return y_true_list, y_pred_list

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

    def model_fit(self, X, y):
        # Convert date-time to numerical feature
        X_copy = X.copy()  # Create a copy to avoid modifying the original DataFrame
        X_copy['DaysSinceStart'] = (X_copy['Data'] - X_copy['Data'].min()).dt.days

        # Initializing the fitting models
        lr = LinearRegression()
        svr_lin = SVR(kernel='linear')
        ridge = Ridge(random_state=1)
        svr_poly = SVR(kernel='poly')

        # Training the Linear Regression model
        lr.fit(X, y)

        # Training the SVR Linear model
        svr_lin.fit(X, y)

        # Training the Ridge model
        ridge.fit(X, y)

        # Training the SVR Poly model
        svr_poly.fit(X, y)

        # Initializing and training the Stacking model
        svr_rbf = SVR(kernel='rbf')
        stregr = StackingRegressor(regressors=[svr_lin, lr, ridge, svr_poly], meta_regressor=svr_rbf)
        stregr.fit(X, y)

        # Plotting the results
        plt.figure(figsize=(10, 6))
        plt.scatter(X, y, c='lightgray', label='Real Data')
        plt.plot(X, lr.predict(X), c='darkgreen', lw=2, label='Linear Regression')
        plt.plot(X, svr_lin.predict(X), c='blue', lw=2, label='SVR Linear')
        plt.plot(X, ridge.predict(X), c='orange', lw=2, label='Ridge')
        plt.plot(X, svr_poly.predict(X), c='red', lw=2, label='SVR Poly')
        plt.plot(X, stregr.predict(X), c='purple', lw=2, label='Stacking Ensemble')
        plt.legend()
        plt.title('Results of Models and Ensemble')
        plt.xlabel('X')
        plt.ylabel('y')
        plt.show()

        return X_copy.drop('Data', axis=1)  # Drop the 'Data' column in the copied DataFrame

    def main(self, total_iterations=10):

        X = self.df[['Data']].values
        y = self.df['Forecast'].values

        future_values_df = self.simulate_future_values()
        
        X_train, X_test, y_train, y_test = self.train_test_split(lag=4)

        # Train models
        svr_model, xgb_model, mlp_model, rf_model = self.train_models(X_train, y_train)

        forecast_steps = 10
        for iteration in range(total_iterations):
            head_size, tail_size = self.dynamic_head_tail_adjustment(iteration, total_iterations)

            svr_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, svr_model, lag=4, feature_names=['Data'])
            xgb_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, xgb_model, lag=4, feature_names=['Data'])
            mlp_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, mlp_model, lag=4, feature_names=['Data'])
            rf_forecast = self.forecast_sliding_window(X_test.iloc[:head_size], forecast_steps, rf_model, lag=4, feature_names=['Data'])

            # Combine the outputs of models in pairs
            ensemble_svm_xgb = self.ensemble(svr_forecast, xgb_forecast)
            ensemble_svm_mlp = self.ensemble(svr_forecast, mlp_forecast)
            ensemble_svm_rf = self.ensemble(svr_forecast, rf_forecast)

            # Combine the outputs of all models
            ensemble_all = self.ensemble(ensemble_svm_xgb, ensemble_svm_mlp)

            forecast_df = pd.DataFrame({
                'Observed': future_values_df['Observed'].values[:forecast_steps],
                'SVM': svr_forecast['Forecast'].values, 
                'XGBoost': xgb_forecast['Forecast'].values,
                'MLP': mlp_forecast['Forecast'].values, 
                'Random Forest': rf_forecast['Forecast'].values,
                'Ensemble SVM+XGBoost': ensemble_svm_xgb['Forecast'].values,
                'Ensemble SVM+MLP': ensemble_svm_mlp['Forecast'].values,
                'Ensemble SVM+Random Forest': ensemble_svm_rf['Forecast'].values,
                'Ensemble All Models': ensemble_all['Forecast'].values
            })

            # Plot ensemble vs observed for each iteration
            model_names = ['SVM', 'XGBoost', 'MLP', 'Random Forest']
            ensemble_forecast = self.ensemble(ensemble_svm_xgb, ensemble_svm_mlp)['Forecast'].values
            self.evaluate_performance(forecast_df, iteration, total_iterations, model_names)
            self.plot_time_series_with_forecast(forecast_df, ensemble_forecast, model_names)
            self.model_fit(X, y)

# File path for the dataset
file_path = "data/br_coffee-price_inflation/output.csv"

# Create an instance of the ForecastingModel with the correct file path
forecast_model = ForecastingModel(file_path)

# Execute the main function
forecast_model.main(total_iterations=1)