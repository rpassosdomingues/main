import pandas as pd

# Load CSV file
df = pd.read_csv('input.csv')

# Select only the desired columns
desired_columns = ["Species","Aroma","Flavor","Aftertaste","Acidity","Body","Balance","Uniformity","Clean.Cup","Sweetness","Cupper.Points","Total.Cup.Points","Moisture","Category.One.Defects","Quakers","Category.Two.Defects","altitude_low_meters","altitude_high_meters","altitude_mean_meters"]
df = df[desired_columns]

# Save the new DataFrame to a new CSV file
df.to_csv('output.csv', index=False)
