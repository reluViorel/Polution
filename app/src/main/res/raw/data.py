import sys, argparse, csv

import pandas as pd
from pandas import DataFrame, Series

path = '/media/viorel/ssd/android/MyApplication/app/src/main/res/raw/dataNox.csv'
processed = '/media/viorel/ssd/android/MyApplication/app/src/main/res/raw/processedNox.csv'

rawdata = pd.read_csv(path)

headers = pd.read_csv(path, nrows=0)
tolist = rawdata.columns.values.tolist()

processed = rawdata[["Network name", "Air pollutant", "Air pollution level", "Exceedance threshold", "Latitude of station", "Longitude of station", "Type of station"]]
processed.fillna(-1)

print processed
processed.to_csv('/media/viorel/ssd/android/MyApplication/app/src/main/res/raw/processedNox.csv', sep=',', header=False)

